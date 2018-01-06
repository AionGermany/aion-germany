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


package com.aionemu.loginserver.network.aion.clientpackets;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.controller.BannedIpController;
import com.aionemu.loginserver.network.aion.AionAuthResponse;
import com.aionemu.loginserver.network.aion.AionClientPacket;
import com.aionemu.loginserver.network.aion.LoginConnection;
import com.aionemu.loginserver.network.aion.LoginConnection.State;
import com.aionemu.loginserver.network.aion.SessionKey;
import com.aionemu.loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;
import com.aionemu.loginserver.network.aion.serverpackets.SM_LOGIN_OK;
import com.aionemu.loginserver.utils.BruteForceProtector;

/**
 * @author -Nemesiss-, KID, Lyahim
 */
public class CM_LOGIN extends AionClientPacket {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(CM_LOGIN.class);
    /**
     * byte array contains encrypted login and password.
     */
    private byte[] data;

    /**
     * Constructs new instance of <tt>CM_LOGIN </tt> packet.
     *
     * @param buf
     * @param client
     */
    public CM_LOGIN(ByteBuffer buf, LoginConnection client) {
        super(buf, client, 0x0b);
    }

    @Override
    protected void readImpl() {
    	readD();
    	if (getRemainingBytes() >= 128) {
      		data = readB(128);
    	}
    }

    @Override
    protected void runImpl() {
        if (data == null) {
            return;
        }

        byte[] decrypted;
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.DECRYPT_MODE, getConnection().getRSAPrivateKey());
            decrypted = rsaCipher.doFinal(data, 0, 128);
        } catch (GeneralSecurityException e) {
            sendPacket(new SM_LOGIN_FAIL(AionAuthResponse.SYSTEM_ERROR));
            return;
        }
        String user = new String(decrypted, 64, 32).trim().toLowerCase();
        String password = new String(decrypted, 96, 32).trim();

        @SuppressWarnings("unused")
        int ncotp = decrypted[0x7c];
        ncotp |= decrypted[0x7d] << 8;
        ncotp |= decrypted[0x7e] << 16;
        ncotp |= decrypted[0x7f] << 24;

        LoginConnection client = getConnection();
        AionAuthResponse response = AccountController.login(user, password, client);
        switch (response) {
            case AUTHED:
                client.setState(State.AUTHED_LOGIN);
                client.setSessionKey(new SessionKey(client.getAccount()));
                client.sendPacket(new SM_LOGIN_OK(client.getSessionKey()));
                log.debug("" + user + " got authed state");
                break;
            case INVALID_PASSWORD:
                if (Config.ENABLE_BRUTEFORCE_PROTECTION) {
                    String ip = client.getIP();
                    if (BruteForceProtector.getInstance().addFailedConnect(ip)) {
                        Timestamp newTime = new Timestamp(System.currentTimeMillis() + Config.WRONG_LOGIN_BAN_TIME * 60000);
                        BannedIpController.banIp(ip, newTime);
                        log.debug(user + " on " + ip + " banned for " + Config.WRONG_LOGIN_BAN_TIME + " min. bruteforce");
                        client.close(new SM_LOGIN_FAIL(AionAuthResponse.BAN_IP), false);
                    } else {
                        log.debug(user + " got invalid password attemp state");
                        client.sendPacket(new SM_LOGIN_FAIL(response));
                    }
                } else {
                    log.debug(user + " got invalid password attemp state");
                    client.sendPacket(new SM_LOGIN_FAIL(response));
                }
                break;
            default:
                log.debug(user + " got unknown (" + response.toString() + ") attemp state");
                client.close(new SM_LOGIN_FAIL(response), false);
                break;
        }
    }
}
