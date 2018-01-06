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


package com.aionemu.loginserver.network.gameserver.clientpackets;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.IPRange;
import com.aionemu.loginserver.GameServerTable;
import com.aionemu.loginserver.network.gameserver.GsAuthResponse;
import com.aionemu.loginserver.network.gameserver.GsClientPacket;
import com.aionemu.loginserver.network.gameserver.GsConnection;
import com.aionemu.loginserver.network.gameserver.GsConnection.State;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_MACBAN_LIST;
import com.aionemu.loginserver.utils.ThreadPoolManager;

/**
 * This is authentication packet that gs will send to login server for
 * registration.
 *
 * @author -Nemesiss-
 */
public class CM_GS_AUTH extends GsClientPacket {

    private final Logger log = LoggerFactory.getLogger(CM_GS_AUTH.class);
    /**
     * Password for authentication
     */
    private String password;
    /**
     * Id of GameServer
     */
    private byte gameServerId;
    /**
     * Maximum number of players that this Gameserver can accept.
     */
    private int maxPlayers;
    /**
     * Port of this Gameserver.
     */
    private int port;
    /**
     * Default address for server
     */
    private byte[] defaultAddress;
    /**
     * List of IPRanges for this gameServer
     */
    private List<IPRange> ipRanges;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        gameServerId = (byte) readC();

        byte len1 = (byte) readC();
        defaultAddress = readB(len1);
        int size = readD();
        ipRanges = new ArrayList<IPRange>(size);
        for (int i = 0; i < size; i++) {
            ipRanges.add(new IPRange(readB(readC()), readB(readC()), readB(readC())));
        }

        port = readH();
        maxPlayers = readD();
        password = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        final GsConnection client = this.getConnection();

        GsAuthResponse resp = GameServerTable.registerGameServer(client, gameServerId, defaultAddress, ipRanges, port, maxPlayers, password);
        switch (resp) {
            case AUTHED:
                log.info("Gameserver #" + gameServerId + " is now online.");
                client.setState(State.AUTHED);
                client.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        client.sendPacket(new SM_MACBAN_LIST());
                    }
                }, 500);
                break;

            default:
                client.close(new SM_GS_AUTH_RESPONSE(resp), false);
        }
    }
}
