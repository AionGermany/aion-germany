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

import java.sql.Timestamp;

/**
 * This class represents Account model
 *
 * @author SoulKeeper
 */
public class Account {

    /**
     * Id of account, object if assigned, null if not
     */
    private Integer id;
    /**
     * Account name
     */
    private String name;
    /**
     * Password hash
     */
    private String passwordHash;
    /**
     * Access level of account 0 = regular user, > 0 = GM
     */
    private byte accessLevel;
    /**
     * Membership of this account (regular, premium etc)
     */
    private byte membership;
    /**
     * Account activated
     */
    private byte activated;
    /**
     * last server visited by user -1 if none
     */
    private byte lastServer;
    /**
     * Last ip of user -1 if none
     */
    private String lastIp;
    /**
     * Last mac of user xx-xx-xx-xx-xx-xx if none
     */
    private String lastMac = "xx-xx-xx-xx-xx-xx";
    /**
     * The only ip that is allowed to this account
     */
    private String ipForce;
    /**
     * AccountTime data
     */
    private AccountTime accountTime;

    private byte isReturn;

    private Timestamp returnEnd;

    /**
     * Returns account id, null if not stored in DB
     *
     * @return account id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets account id
     *
     * @param id account id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns account name
     *
     * @return account name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets account name
     *
     * @param name account name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns password hash
     *
     * @return password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets password hash
     *
     * @param passwordHash password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns access level of account
     *
     * @return access level of account
     */
    public byte getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets access level of account
     *
     * @param accessLevel access level of account
     */
    public void setAccessLevel(byte accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * @return the membership
     */
    public byte getMembership() {
        return membership;
    }

    /**
     * @param membership the membership to set
     */
    public void setMembership(byte membership) {
        this.membership = membership;
    }

    /**
     * Returns account activated
     *
     * @return access level of account
     */
    public byte getActivated() {
        return activated;
    }

    /**
     * Sets access level of account
     *
     * @param activated access level of account
     */
    public void setActivated(byte activated) {
        this.activated = activated;
    }

    /**
     * Returns last server that player visited
     *
     * @return last server that player visited
     */
    public byte getLastServer() {
        return lastServer;
    }

    /**
     * Sets last server that player visited
     *
     * @param lastServer last server that player visited
     */
    public void setLastServer(byte lastServer) {
        this.lastServer = lastServer;
    }

    /**
     * Returns last ip that player played from
     *
     * @return last ip that player played from
     */
    public String getLastIp() {
        return lastIp;
    }

    /**
     * Sets last ip that player players from
     *
     * @param lastIp last ip that player played from
     */
    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    /**
     * Returns last mac that player played from
     *
     * @return last mac that player played from
     */
    public String getLastMac() {
        return lastMac;
    }

    /**
     * Sets last mac that player players from
     *
     * @param lastMac last mac that player played from
     */
    public void setLastMac(String lastMac) {
        this.lastMac = lastMac;
    }

    /**
     * Returns IP that player is forced to use with his account
     *
     * @return ip that player is forsed to use with his account
     */
    public String getIpForce() {
        return ipForce;
    }

    /**
     * Sets ip that player has to use with his account
     *
     * @param ipForce sets ip that players has to use with his account
     */
    public void setIpForce(String ipForce) {
        this.ipForce = ipForce;
    }

    /**
     * @return accountTime
     */
    public AccountTime getAccountTime() {
        return accountTime;
    }

    /**
     * @param accountTime
     */
    public void setAccountTime(AccountTime accountTime) {
        this.accountTime = accountTime;
    }

    public byte getReturn() {
        return isReturn;
    }

    public void setReturn(byte isReturn) {
        this.isReturn = isReturn;
    }

    public Timestamp getReturnEnd() {
        return returnEnd;
    }

    public void setReturnEnd(Timestamp end) {
        this.returnEnd = end;
    }

    /**
     * Retunrns true if players name and password has are equals
     *
     * @param o another player to check
     * @return true if names and password hash matches
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)) {
            return false;
        }

        Account account = (Account) o;

        // noinspection SimplifiableIfStatement
        if (name != null ? !name.equals(account.name) : account.name != null) {
            return false;
        }

        return !(passwordHash != null ? !passwordHash.equals(account.passwordHash) : account.passwordHash != null);

    }

    /**
     * Returns player hashcode.
     *
     * @return player hashcode
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;

        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);

        return result;
    }
}
