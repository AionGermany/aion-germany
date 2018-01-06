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
 * Class for storing account time data (last login time, last session duration
 * time, accumulated online time today, accumulated rest time today)
 *
 * @author EvilSpirit
 */
public class AccountTime {

    /**
     * Time the account has last logged in
     */
    private Timestamp lastLoginTime;
    /**
     * Time after the account will expired
     */
    private Timestamp expirationTime;
    /**
     * Time when the penalty will end
     */
    private Timestamp penaltyEnd;
    /**
     * The duration of the session
     */
    private long sessionDuration;
    /**
     * Accumulated Online Time
     */
    private long accumulatedOnlineTime;
    /**
     * Accumulated Rest Time
     */
    private long accumulatedRestTime;

    /**
     * Default constructor. Set the lastLoginTime to current time
     */
    public AccountTime() {
        this.lastLoginTime = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @return lastLoginTime
     */
    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime
     */
    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * @return sessionDuration
     */
    public long getSessionDuration() {
        return sessionDuration;
    }

    /**
     * @param sessionDuration
     */
    public void setSessionDuration(long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    /**
     * @return accumulatedOnlineTime
     */
    public long getAccumulatedOnlineTime() {
        return accumulatedOnlineTime;
    }

    /**
     * @param accumulatedOnlineTime
     */
    public void setAccumulatedOnlineTime(long accumulatedOnlineTime) {
        this.accumulatedOnlineTime = accumulatedOnlineTime;
    }

    /**
     * @return accumulatedRestTime
     */
    public long getAccumulatedRestTime() {
        return accumulatedRestTime;
    }

    /**
     * @param accumulatedRestTime
     */
    public void setAccumulatedRestTime(long accumulatedRestTime) {
        this.accumulatedRestTime = accumulatedRestTime;
    }

    /**
     * @return expirationTime
     */
    public Timestamp getExpirationTime() {
        return expirationTime;
    }

    /**
     * @param expirationTime
     */
    public void setExpirationTime(Timestamp expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * @return penaltyEnd
     */
    public Timestamp getPenaltyEnd() {
        return penaltyEnd;
    }

    /**
     * @param penaltyEnd
     */
    public void setPenaltyEnd(Timestamp penaltyEnd) {
        this.penaltyEnd = penaltyEnd;
    }
}
