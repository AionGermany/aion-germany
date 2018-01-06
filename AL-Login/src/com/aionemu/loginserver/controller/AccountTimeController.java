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

import java.sql.Timestamp;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.dao.AccountPlayTimeDAO;
import com.aionemu.loginserver.dao.AccountTimeDAO;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.model.AccountTime;

/**
 * This class is for account time controlling. When character logins any server,
 * it should get its day online time and rest time. Some aion ingame feautres
 * also depend on player's online time
 *
 * @author EvilSpirit
 */
public class AccountTimeController {

    /**
     * Update account time when character logins. The following field are being
     * updated: - LastLoginTime (set to CurrentTime) - RestTime (set to
     * (RestTime + (CurrentTime-LastLoginTime - SessionDuration))
     *
     * @param account
     */
    public static void updateOnLogin(Account account) {
        AccountTime accountTime = account.getAccountTime();

        /**
         * It seems the account was just created, so new accountTime should be
         * created too
         */
        if (accountTime == null) {
            accountTime = new AccountTime();
        }

        int lastLoginDay = getDays(accountTime.getLastLoginTime().getTime());
        int currentDay = getDays(System.currentTimeMillis());
        int returnday = getDays(accountTime.getLastLoginTime().getTime() + + 30L * 24 * 60 * 60 * 1000);

        /**
         * The character from that account was online not today, so it's account
         * timings should be nulled.
         */
        if (lastLoginDay < currentDay) {
      		DAOManager.getDAO(AccountPlayTimeDAO.class).update(account.getId(), accountTime);
            accountTime.setAccumulatedOnlineTime(0);
            accountTime.setAccumulatedRestTime(0);
        } else {
            long restTime = System.currentTimeMillis() - accountTime.getLastLoginTime().getTime() - accountTime.getSessionDuration();
            accountTime.setAccumulatedRestTime(accountTime.getAccumulatedRestTime() + restTime);

        }

        accountTime.setLastLoginTime(new Timestamp(System.currentTimeMillis()));

        DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(account.getId(), accountTime);
        account.setAccountTime(accountTime);

        if (currentDay >= returnday && account.getReturn() == 0){
            account.setReturn((byte) 1);
            account.setReturnEnd(new Timestamp(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        }

        if (currentDay >= account.getReturnEnd().getTime()) {
            account.setReturn((byte) 0);
        }

    }

    /**
     * Update account time when character logouts. The following field are being
     * updated: - SessionTime (set to CurrentTime - LastLoginTime) -
     * AccumulatedOnlineTime (set to AccumulatedOnlineTime + SessionTime)
     *
     * @param account
     */
    public static void updateOnLogout(Account account) {
        AccountTime accountTime = account.getAccountTime();

        accountTime.setSessionDuration(System.currentTimeMillis() - accountTime.getLastLoginTime().getTime());
        accountTime.setAccumulatedOnlineTime(accountTime.getAccumulatedOnlineTime() + accountTime.getSessionDuration());
        DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(account.getId(), accountTime);
        account.setAccountTime(accountTime);
    }

    /**
     * Checks if account is already expired or not
     *
     * @param account
     * @return true, if account is expired, false otherwise
     */
    public static boolean isAccountExpired(Account account) {
        AccountTime accountTime = account.getAccountTime();

        return accountTime != null && accountTime.getExpirationTime() != null && accountTime.getExpirationTime().getTime() < System.currentTimeMillis();
    }

    /**
     * Checks if account is restricted by penalty or not
     *
     * @param account
     * @return true, is penalty is active, false otherwise
     */
    public static boolean isAccountPenaltyActive(Account account) {
        AccountTime accountTime = account.getAccountTime();

        // 1000 is 'infinity' value
        return accountTime != null && accountTime.getPenaltyEnd() != null && (accountTime.getPenaltyEnd().getTime() == 1000 || accountTime.getPenaltyEnd().getTime() >= System.currentTimeMillis());
    }

    /**
     * Get days from time presented in milliseconds
     *
     * @param millis time in ms
     * @return days
     */
    public static int getDays(long millis) {
        return (int) (millis / 1000 / 3600 / 24);
    }
}
