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
 * This class represents banned ip
 *
 * @author SoulKeeper
 */
public class BannedIP {

    /**
     * Returns id of ip ban
     */
    private Integer id;
    /**
     * Returns ip mask
     */
    private String mask;
    /**
     * Returns expiration time
     */
    private Timestamp timeEnd;

    /**
     * Checks if ban is still active
     *
     * @return true if ban is still active
     */
    public boolean isActive() {
        return timeEnd == null || timeEnd.getTime() > System.currentTimeMillis();
    }

    /**
     * Returns ban id
     *
     * @return ban id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets ban id
     *
     * @param id ban id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retuns ip mask
     *
     * @return ip mask
     */
    public String getMask() {
        return mask;
    }

    /**
     * Sets ip mask
     *
     * @param mask ip mask
     */
    public void setMask(String mask) {
        this.mask = mask;
    }

    /**
     * Returns expiration time of ban
     *
     * @return expiration time of ban
     */
    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    /**
     * Sets expiration time of ban
     *
     * @param timeEnd expiration time of ban
     */
    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * Returns true if this ip ban is equal to another. Based on {@link #mask}
     *
     * @param o another ip ban
     * @return true if ban's are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BannedIP)) {
            return false;
        }

        BannedIP bannedIP = (BannedIP) o;

        return !(mask != null ? !mask.equals(bannedIP.mask) : bannedIP.mask != null);
    }

    /**
     * Returns ban's hashcode. Based on mask
     *
     * @return ban's hashcode
     */
    @Override
    public int hashCode() {
        return mask != null ? mask.hashCode() : 0;
    }
}
