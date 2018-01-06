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
package com.aionemu.gameserver.network.loginserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.AConnection;
import com.aionemu.commons.network.Dispatcher;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.network.factories.LsPacketHandlerFactory;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_GS_AUTH;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Object representing connection between LoginServer and GameServer.
 *
 * @author -Nemesiss-
 */
public class LoginServerConnection extends AConnection {

	/**
	 * Logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(LoginServerConnection.class);

	/**
	 * Possible states of GsConnection
	 */
	public static enum State {

		/**
		 * game server just connect
		 */
		CONNECTED,
		/**
		 * game server is authenticated
		 */
		AUTHED
	}

	/**
	 * Server Packet "to send" Queue
	 */
	private final Deque<LsServerPacket> sendMsgQueue = new ArrayDeque<LsServerPacket>();
	/**
	 * Current state of this connection
	 */
	private State state;
	private LsPacketHandler lsPacketHandler;

	/**
	 * Constructor.
	 *
	 * @param sc
	 * @param d
	 * @throws IOException
	 */
	public LoginServerConnection(SocketChannel sc, Dispatcher d) throws IOException {
		super(sc, d, 8192 * 8, 8192 * 8);
		LsPacketHandlerFactory lsPacketHandlerFactory = LsPacketHandlerFactory.getInstance();
		this.lsPacketHandler = lsPacketHandlerFactory.getPacketHandler();

		state = State.CONNECTED;
		GameServer.log.info("Connected to LoginServer!");
		System.out.println("");
	}

	@Override
	protected void initialized() {
		/**
		 * send first packet - authentication.
		 */
		this.sendPacket(new SM_GS_AUTH());
	}

	/**
	 * Called by Dispatcher. ByteBuffer data contains one packet that should be processed.
	 *
	 * @param data
	 * @return True if data was processed correctly, False if some error occurred and connection should be closed NOW.
	 */
	@Override
	public boolean processData(ByteBuffer data) {
		LsClientPacket pck = lsPacketHandler.handle(data, this);
		log.debug("[LoginServer] recived packet: " + pck);

		/**
		 * Execute packet only if packet exist (!= null) and read was ok.
		 */
		if (pck != null && pck.read()) {
			ThreadPoolManager.getInstance().executeLsPacket(pck);
		}

		return true;
	}

	/**
	 * This method will be called by Dispatcher, and will be repeated till return false.
	 *
	 * @param data
	 * @return True if data was written to buffer, False indicating that there are not any more data to write.
	 */
	@Override
	protected final boolean writeData(ByteBuffer data) {
		synchronized (guard) {
			LsServerPacket packet = sendMsgQueue.pollFirst();
			if (packet == null) {
				return false;
			}

			packet.write(this, data);
			return true;
		}
	}

	/**
	 * This method is called by Dispatcher when connection is ready to be closed.
	 *
	 * @return time in ms after witch onDisconnect() method will be called. Always return 0.
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
		LoginServer.getInstance().loginServerDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onServerClose() {
		// TODO mb some packet should be send to loginserver before closing?
		close(/* packet, */true);
	}

	/**
	 * Sends GsServerPacket to this client.
	 *
	 * @param bp
	 *            GsServerPacket to be sent.
	 */
	public final void sendPacket(LsServerPacket bp) {
		synchronized (guard) {
			/**
			 * Connection is already closed or waiting for last (close packet) to be sent
			 */
			if (isWriteDisabled()) {
				return;
			}

			log.debug("[LoginServer] sending packet: " + bp);

			sendMsgQueue.addLast(bp);
			enableWriteInterest();
		}
	}

	/**
	 * Its guaranted that closePacket will be sent before closing connection, but all past and future packets wont. Connection will be closed [by Dispatcher Thread], and onDisconnect() method will be
	 * called to clear all other things. forced means that server shouldn't wait with removing this connection.
	 *
	 * @param closePacket
	 *            Packet that will be send before closing.
	 * @param forced
	 *            have no effect in this implementation.
	 */
	public final void close(LsServerPacket closePacket, boolean forced) {
		synchronized (guard) {
			if (isWriteDisabled()) {
				return;
			}

			log.debug("[LoginServer] sending packet: " + closePacket + " and closing connection after that.");

			pendingClose = true;
			isForcedClosing = forced;
			sendMsgQueue.clear();
			sendMsgQueue.addLast(closePacket);
			enableWriteInterest();
		}
	}

	/**
	 * @return Current state of this connection.
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            Set current state of this connection.
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return String info about this connection
	 */
	@Override
	public String toString() {
		return "LoginServer " + getIP();
	}
}
