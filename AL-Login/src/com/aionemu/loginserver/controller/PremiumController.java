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


package com.aionemu.loginserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.GameServerInfo;
import com.aionemu.loginserver.GameServerTable;
import com.aionemu.loginserver.dao.PremiumDAO;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_PREMIUM_RESPONSE;

/**
 * @author KID
 */
public class PremiumController {

    private Logger log = LoggerFactory.getLogger("PREMIUM_CTRL");
    private static PremiumController controller = new PremiumController();

    public static PremiumController getController() {
        return controller;
    }
    public static byte RESULT_FAIL = 1;
    public static byte RESULT_LOW_POINTS = 2;
    public static byte RESULT_OK = 3;
    public static byte RESULT_ADD = 4;
    private PremiumDAO dao;

    public PremiumController() {
        dao = DAOManager.getDAO(PremiumDAO.class);
        log.info("PremiumController is ready for requests.");
    }

    public void requestBuy(int accountId, int requestId, long cost, byte serverId) {
        long points = this.dao.getPoints(accountId);

        GameServerInfo server = GameServerTable.getGameServerInfo(serverId);
        if (server == null || server.getConnection() == null || !server.isAccountOnGameServer(accountId)) {
            log.error("Account " + accountId + " requested " + requestId + " from gs #" + serverId + " and server is down.");
            return;
        }

        //adding new tolls
        if (cost < 0) {
            long ncnt = points + (cost * -1);
            dao.updatePoints(accountId, ncnt, 0);
            server.getConnection().sendPacket(new SM_PREMIUM_RESPONSE(requestId, RESULT_ADD, ncnt));
            return;
        }

        if (points < cost) {
            server.getConnection().sendPacket(new SM_PREMIUM_RESPONSE(requestId, RESULT_LOW_POINTS, points));
            return;
        }

        if (dao.updatePoints(accountId, points, cost)) {
            points -= cost;
            server.getConnection().sendPacket(new SM_PREMIUM_RESPONSE(requestId, RESULT_OK, points));
            log.info("Acount " + accountId + " succed in purchasing lot #" + requestId + " for " + cost + " from server #" + serverId);
        } else {
            server.getConnection().sendPacket(new SM_PREMIUM_RESPONSE(requestId, RESULT_FAIL, points));
            log.info("Acount " + accountId + " failed in purchasing lot #" + requestId + " for " + cost + " from server #" + serverId + ". !updatePoints");
        }
    }
}
