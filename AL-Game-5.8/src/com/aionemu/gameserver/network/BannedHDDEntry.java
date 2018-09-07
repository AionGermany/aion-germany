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
package com.aionemu.gameserver.network;

import java.sql.Timestamp;

/**
 * @author Alex
 */
public class BannedHDDEntry {

	private final String hdd_serial;
	private String details;
	private Timestamp timeEnd;

	public BannedHDDEntry(String address, long newTime) {
		this.hdd_serial = address;
		this.updateTime(newTime);
	}

	public BannedHDDEntry(String address, Timestamp time, String details) {
		this.hdd_serial = address;
		this.timeEnd = time;
		this.details = details;
	}

	public final void setDetails(String details) {
		this.details = details;
	}

	public final void updateTime(long newTime) {
		this.timeEnd = new Timestamp(newTime);
	}

	public final String getHDDSerial() {
		return hdd_serial;
	}

	public final Timestamp getTime() {
		return timeEnd;
	}

	public final boolean isActive() {
		return timeEnd != null && timeEnd.getTime() > System.currentTimeMillis();
	}

	public final boolean isActiveTill(long time) {
		return timeEnd != null && timeEnd.getTime() > time;
	}

	public final String getDetails() {
		return details;
	}
}
