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


package com.aionemu.loginserver.model;

/**
 * This object is storing Account and corresponding to it reconnectionKey for
 * client that will be reconnecting to LoginServer from GameServer using fast
 * reconnect feature
 *
 * @author -Nemesiss-
 */
public class ReconnectingAccount {

    /**
     * Account object of account that will be reconnecting.
     */
    private final Account account;
    /**
     * Reconnection Key that will be used for authenticating
     */
    private final int reconnectionKey;

    /**
     * Constructor.
     *
     * @param account
     * @param reconnectionKey
     */
    public ReconnectingAccount(Account account, int reconnectionKey) {
        this.account = account;
        this.reconnectionKey = reconnectionKey;
    }

    /**
     * Return Account.
     *
     * @return account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Return reconnection key for this account
     *
     * @return reconnectionKey
     */
    public int getReconnectionKey() {
        return reconnectionKey;
    }
}
