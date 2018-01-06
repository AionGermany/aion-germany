/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */


package com.aionemu.loginserver.network.aion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.AConnection;
import com.aionemu.commons.network.Dispatcher;
import com.aionemu.commons.network.PacketProcessor;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.controller.AccountTimeController;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.network.aion.serverpackets.SM_INIT;
import com.aionemu.loginserver.network.factories.AionPacketHandlerFactory;
import com.aionemu.loginserver.network.ncrypt.CryptEngine;
import com.aionemu.loginserver.network.ncrypt.EncryptedRSAKeyPair;
import com.aionemu.loginserver.network.ncrypt.KeyGen;

/**
 * Object representing connection between LoginServer and Aion Client.
 *
 * @author -Nemesiss-
 */
public class LoginConnection extends AConnection {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(LoginConnection.class);
    /**
     * PacketProcessor for executing packets.
     */
    private final static PacketProcessor<LoginConnection> processor = new PacketProcessor<LoginConnection>(1, 8, 50, 3);
    /**
     * Server Packet "to send" Queue
     */
    private final Deque<AionServerPacket> sendMsgQueue = new ArrayDeque<AionServerPacket>();
    /**
     * Unique Session Id of this connection
     */
    private int sessionId = hashCode();
    /**
     * Account object for this connection. if state = AUTHED_LOGIN account cant
     * be null.
     */
    private Account account;
    /**
     * Crypt to encrypt/decrypt packets
     */
    private CryptEngine cryptEngine;
    /**
     * True if this user is connecting to GS.
     */
    private boolean joinedGs;
    /**
     * Scrambled key pair for RSA
     */
    private EncryptedRSAKeyPair encryptedRSAKeyPair;
    /**
     * Session Key for this connection.
     */
    private SessionKey sessionKey;
    /**
     * Current state of this connection
     */
    private State state;

    /**
     * Possible states of AionConnection
     */
    public static enum State {

        /**
         * Means that client just connects
         */
        CONNECTED,
        /**
         * Means that clients GameGuard is authenticated
         */
        AUTHED_GG,
        /**
         * Means that client is logged in.
         */
        AUTHED_LOGIN
    }

    /**
     * Constructor
     *
     * @param sc
     * @param d
     * @throws IOException
     */
    public LoginConnection(SocketChannel sc, Dispatcher d) throws IOException {
        super(sc, d, 8192 * 2, 8192 * 2);

    }

    /**
     * Called by Dispatcher. ByteBuffer data contains one packet that should be
     * processed.
     *
     * @param data
     * @return True if data was processed correctly, False if some error
     * occurred and connection should be closed NOW.
     */
    @Override
    protected final boolean processData(ByteBuffer data) {
        if (!decrypt(data)) {
            return false;
        }

        AionClientPacket pck = AionPacketHandlerFactory.handle(data, this);

        /**
         * Execute packet only if packet exist (!= null) and read was ok.
         */
        if ((pck != null) && pck.read()) {
            processor.executePacket(pck);
        }

        return true;
    }

    /**
     * This method will be called by Dispatcher, and will be repeated till
     * return false.
     *
     * @param data
     * @return True if data was written to buffer, False indicating that there
     * are not any more data to write.
     */
    @Override
    protected final synchronized boolean writeData(ByteBuffer data) {
        AionServerPacket packet = sendMsgQueue.pollFirst();

        if (packet == null) {
            return false;
        }

        packet.setBuf(data);
        packet.write(this);

        return true;
    }

    /**
     * This method is called by Dispatcher when connection is ready to be
     * closed.
     *
     * @return time in ms after witch onDisconnect() method will be called.
     * Always return 0.
     */
    @Override
    protected final long getDisconnectionDelay() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void onDisconnect() {
        /**
         * Remove account only if not joined GameServer yet.
         */
        if ((account != null) && !joinedGs) {
            AccountController.removeAccountOnLS(account);
            AccountTimeController.updateOnLogout(account);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void onServerClose() {
        // TODO mb some packet should be send to client before closing?
        close( /* packet, */true);
    }

    /**
     * Decrypt packet.
     *
     * @param buf
     * @return true if success
     */
    private boolean decrypt(ByteBuffer buf) {
        int size = buf.remaining();
        final int offset = buf.arrayOffset() + buf.position();
        boolean ret = cryptEngine.decrypt(buf.array(), offset, size);

        if (!ret) {
            log.warn("Wrong checksum from client: " + this);
        }

        return ret;
    }

    /**
     * Encrypt packet.
     *
     * @param buf
     * @return encrypted packet size.
     */
    public final int encrypt(ByteBuffer buf) {
        int size = buf.limit() - 2;
        final int offset = buf.arrayOffset() + buf.position();

        size = cryptEngine.encrypt(buf.array(), offset, size);

        return size;
    }

    /**
     * Sends AionServerPacket to this client.
     *
     * @param bp AionServerPacket to be sent.
     */
    public final synchronized void sendPacket(AionServerPacket bp) {
        /**
         * Connection is already closed or waiting for last (close packet) to be
         * sent
         */
        if (isWriteDisabled()) {
            return;
        }

        log.debug("sending packet: " + bp);
        sendMsgQueue.addLast(bp);
        enableWriteInterest();
    }

    /**
     * Its guaranted that closePacket will be sent before closing connection,
     * but all past and future packets wont. Connection will be closed [by
     * Dispatcher Thread], and onDisconnect() method will be called to clear all
     * other things. forced means that server shouldn't wait with removing this
     * connection.
     *
     * @param closePacket Packet that will be send before closing.
     * @param forced have no effect in this implementation.
     */
    public final synchronized void close(AionServerPacket closePacket, boolean forced) {
        if (isWriteDisabled()) {
            return;
        }

        log.info("sending packet: " + closePacket + " and closing connection after that.");

        pendingClose = true;
        isForcedClosing = forced;
        sendMsgQueue.clear();
        sendMsgQueue.addLast(closePacket);
        enableWriteInterest();
    }

    /**
     * Return Scrambled modulus
     *
     * @return Scrambled modulus
     */
    public final byte[] getEncryptedModulus() {
        return encryptedRSAKeyPair.getEncryptedModulus();
    }

    /**
     * Return RSA private key
     *
     * @return rsa private key
     */
    public final RSAPrivateKey getRSAPrivateKey() {
        return (RSAPrivateKey) encryptedRSAKeyPair.getRSAKeyPair().getPrivate();
    }

    /**
     * Returns unique sessionId of this connection.
     *
     * @return SessionId
     */
    public final int getSessionId() {
        return sessionId;
    }

    /**
     * Current state of this connection
     *
     * @return state
     */
    public final State getState() {
        return state;
    }

    /**
     * Set current state of this connection
     *
     * @param state
     */
    public final void setState(State state) {
        this.state = state;
    }

    /**
     * Returns Account object that this client logged in or null
     *
     * @return Account
     */
    public final Account getAccount() {
        return account;
    }

    /**
     * Set Account object for this connection.
     *
     * @param account
     */
    public final void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Returns Session Key of this connection
     *
     * @return SessionKey
     */
    public final SessionKey getSessionKey() {
        return sessionKey;
    }

    /**
     * Set Session Key for this connection
     *
     * @param sessionKey
     */
    public final void setSessionKey(SessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * Set joinedGs value to true
     */
    public final void setJoinedGs() {
        joinedGs = true;
    }

    /**
     * @return String info about this connection
     */
    @Override
    public String toString() {
        return (account != null) ? account + " " + getIP() : "not loged " + getIP();
    }

    /**
     * This method should no be modified, hashcode in this class is used to
     * ensure that each connection hash unique id
     *
     * @return unique identifier
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void closeNow() {
        this.close(false);
    }

    @Override
    protected void initialized() {
        // TODO Auto-generated method stub
        state = State.CONNECTED;
        log.info("Connection attemp from: " + getIP());
        encryptedRSAKeyPair = KeyGen.getEncryptedRSAKeyPair();
        SecretKey blowfishKey = KeyGen.generateBlowfishKey();

        cryptEngine = new CryptEngine();
        cryptEngine.updateKey(blowfishKey.getEncoded());

        /**
         * Send Init packet
         */
        sendPacket(new SM_INIT(this, blowfishKey));
    }
}
