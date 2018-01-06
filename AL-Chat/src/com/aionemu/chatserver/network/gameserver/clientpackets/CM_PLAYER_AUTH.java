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


package com.aionemu.chatserver.network.gameserver.clientpackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.chatserver.model.ChatClient;
import com.aionemu.chatserver.network.gameserver.GsClientPacket;
import com.aionemu.chatserver.network.gameserver.GsConnection;
import com.aionemu.chatserver.network.gameserver.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import com.aionemu.chatserver.service.ChatService;

/**
 * @author ATracer
 */
public class CM_PLAYER_AUTH extends GsClientPacket {

    private static final Logger log = LoggerFactory.getLogger(CM_PLAYER_AUTH.class);
    private int playerId;
    private String playerLogin;
    private String nick;

    public CM_PLAYER_AUTH(ByteBuffer buf, GsConnection connection) {
        super(buf, connection, 0x01);
    }

    @Override
    protected void readImpl() {
        playerId = readD();
        playerLogin = readS();
        nick = readS();
    }

    @Override
    protected void runImpl() {
        ChatClient chatClient = null;
        try {
            chatClient = ChatService.getInstance().registerPlayer(playerId, playerLogin, nick);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error registering player on ChatServer: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("Error registering player on ChatServer: " + e.getMessage());
        }

        if (chatClient != null) {
            getConnection().sendPacket(new SM_PLAYER_AUTH_RESPONSE(chatClient));
        } else {
            log.info("Player was not authed " + playerId);
        }
    }
}
