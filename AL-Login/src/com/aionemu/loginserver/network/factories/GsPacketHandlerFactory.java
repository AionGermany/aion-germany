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


package com.aionemu.loginserver.network.factories;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.loginserver.network.gameserver.GsClientPacket;
import com.aionemu.loginserver.network.gameserver.GsConnection;
import com.aionemu.loginserver.network.gameserver.GsConnection.State;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_AUTH;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_DISCONNECTED;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_LIST;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_RECONNECT_KEY;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_TOLL_INFO;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_BAN;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_GS_AUTH;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_GS_CHARACTER;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_GS_PONG;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_LS_CONTROL;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_MAC;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_MACBAN_CONTROL;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_PREMIUM_CONTROL;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_PTRANSFER_CONTROL;

/**
 * @author -Nemesiss-
 */
public class GsPacketHandlerFactory {

    /**
     * logger for this class
     */
    private static final Logger log = LoggerFactory.getLogger(GsPacketHandlerFactory.class);

    /**
     * Reads one packet from given ByteBuffer
     *
     * @param data
     * @param client
     * @return GsClientPacket object from binary data
     */
    public static GsClientPacket handle(ByteBuffer data, GsConnection client) {
        GsClientPacket msg = null;
        State state = client.getState();
        int id = data.get() & 0xff;

        switch (state) {
            case CONNECTED: {
                switch (id) {
                    case 0:
                        msg = new CM_GS_AUTH();
                        break;
                    case 13:
                        msg = new CM_MAC();
                        break;
                    default:
                        unknownPacket(state, id);
                }
                break;
            }
            case AUTHED: {
                switch (id) {
                    case 1:
                        msg = new CM_ACCOUNT_AUTH();
                        break;
                    case 2:
                        msg = new CM_ACCOUNT_RECONNECT_KEY();
                        break;
                    case 3:
                        msg = new CM_ACCOUNT_DISCONNECTED();
                        break;
                    case 4:
                        msg = new CM_ACCOUNT_LIST();
                        break;
                    case 5:
                        msg = new CM_LS_CONTROL();
                        break;
                    case 6:
                        msg = new CM_BAN();
                        break;
                    case 8:
                        msg = new CM_GS_CHARACTER();
                        break;
                    case 9:
                        msg = new CM_ACCOUNT_TOLL_INFO();
                        break;
                    case 10:
                        msg = new CM_MACBAN_CONTROL();
                        break;
                    case 11:
                        msg = new CM_PREMIUM_CONTROL();
                        break;
                    case 12:
                        msg = new CM_GS_PONG();
                        break;
                    case 13:
                        msg = new CM_MAC();
                        break;
                    case 14:
                        msg = new CM_PTRANSFER_CONTROL();
                        break;
                    default:
                        unknownPacket(state, id);
                }
                break;
            }
        }

        if (msg != null) {
            msg.setConnection(client);
            msg.setBuffer(data);
        }

        return msg;
    }

    /**
     * Logs unknown packet.
     *
     * @param state
     * @param id
     */
    private static void unknownPacket(State state, int id) {
        log.warn(String.format("Unknown packet recived from Game Server: 0x%02X state=%s", id, state.toString()));
    }
}
