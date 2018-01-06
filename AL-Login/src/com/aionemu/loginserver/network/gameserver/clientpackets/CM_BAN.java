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

import java.sql.Timestamp;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.GameServerInfo;
import com.aionemu.loginserver.GameServerTable;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.controller.BannedIpController;
import com.aionemu.loginserver.dao.AccountDAO;
import com.aionemu.loginserver.dao.AccountTimeDAO;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.model.AccountTime;
import com.aionemu.loginserver.network.gameserver.GsClientPacket;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_BAN_RESPONSE;

/**
 * The universal packet for account/IP bans
 *
 * @author Watson
 */
public class CM_BAN extends GsClientPacket {

    /**
     * Ban type 1 = account 2 = IP 3 = Full ban (account and IP)
     */
    private byte type;
    /**
     * Account to ban
     */
    private int accountId;
    /**
     * IP or mask to ban
     */
    private String ip;
    /**
     * Time in minutes. 0 = infinity; If time < 0 then it's unban command
     */
    private int time;
    /**
     * Object ID of Admin, who request the ban
     */
    private int adminObjId;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        this.type = (byte) readC();
        this.accountId = readD();
        this.ip = readS();
        this.time = readD();
        this.adminObjId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        boolean result = false;

        // Ban account
        if ((type == 1 || type == 3) && accountId != 0) {
            Account account = null;

            // Find account on GameServers
            for (GameServerInfo gsi : GameServerTable.getGameServers()) {
                if (gsi.isAccountOnGameServer(accountId)) {
                    account = gsi.getAccountFromGameServer(accountId);
                    break;
                }
            }

            // 1000 is 'infinity' value
            Timestamp newTime = null;
            if (time >= 0) {
                newTime = new Timestamp(time == 0 ? 1000 : System.currentTimeMillis() + time * 60000);
            }

            if (account != null) {
                AccountTime accountTime = account.getAccountTime();
                accountTime.setPenaltyEnd(newTime);
                account.setAccountTime(accountTime);
                result = true;
            } else {
                AccountTime accountTime = DAOManager.getDAO(AccountTimeDAO.class).getAccountTime(accountId);
                accountTime.setPenaltyEnd(newTime);
                result = DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(accountId, accountTime);
            }
        }

        // Ban IP
        if (type == 2 || type == 3) {
            if (accountId != 0) // If we got account ID, then ban last IP
            {
                String newip = DAOManager.getDAO(AccountDAO.class).getLastIp(accountId);
                if (!newip.isEmpty()) {
                    ip = newip;
                }
            }
            if (!ip.isEmpty()) {
                // Unban first. For banning it needs to update time
                if (BannedIpController.isBanned(ip)) {
                    // Result set for unban request
                    result = BannedIpController.unbanIp(ip);
                }
                if (time >= 0) // Ban
                {
                    Timestamp newTime = time != 0 ? new Timestamp(System.currentTimeMillis() + time * 60000) : null;
                    result = BannedIpController.banIp(ip, newTime);
                }
            }
        }

        // Now kick account
        if (accountId != 0) {
            AccountController.kickAccount(accountId);
        }

        // Respond to GS
        sendPacket(new SM_BAN_RESPONSE(type, accountId, ip, time, adminObjId, result));
    }
}
