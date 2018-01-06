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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.dao.AccountDAO;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.network.gameserver.GsClientPacket;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_LS_CONTROL_RESPONSE;

/**
 * @author Aionchs-Wylovech
 */
public class CM_LS_CONTROL extends GsClientPacket {

    private String accountName;
    private int param;
    private int type;
    private String playerName;
    private String adminName;
    private boolean result;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {

        type = readC();
        adminName = readS();
        accountName = readS();
        playerName = readS();
        param = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {

        Account account = DAOManager.getDAO(AccountDAO.class).getAccount(accountName);
        switch (type) {
            case 1:
                account.setAccessLevel((byte) param);
                break;
            case 2:
                account.setMembership((byte) param);
                break;
        }
        result = DAOManager.getDAO(AccountDAO.class).updateAccount(account);
        sendPacket(new SM_LS_CONTROL_RESPONSE(type, result, playerName, account.getId(), param, adminName));
    }
}
