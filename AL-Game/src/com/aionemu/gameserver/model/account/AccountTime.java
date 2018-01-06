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
package com.aionemu.gameserver.model.account;

/**
 * Class for storing account's online and rest time
 *
 * @author EvilSpirit
 */
public class AccountTime {

	/**
	 * Accumulated online time in millis
	 */
	private long accumulatedOnlineTime;
	/**
	 * Accumulated rest(offline) time in millis
	 */
	private long accumulatedRestTime;

	/**
	 * get daily accumulated online time in millis
	 *
	 * @return time in millis
	 */
	public long getAccumulatedOnlineTime() {
		return accumulatedOnlineTime;
	}

	/**
	 * get daily accumulated online time in millis
	 *
	 * @param accumulatedOnlineTime
	 *            time in millis
	 */
	public void setAccumulatedOnlineTime(long accumulatedOnlineTime) {
		this.accumulatedOnlineTime = accumulatedOnlineTime;
	}

	/**
	 * get daily accumulated rest (offline) time since first login
	 *
	 * @return time in millis
	 */
	public long getAccumulatedRestTime() {
		return accumulatedRestTime;
	}

	/**
	 * get daily accumulated rest (offline) time since first login
	 *
	 * @param accumulatedRestTime
	 *            time in millis
	 */
	public void setAccumulatedRestTime(long accumulatedRestTime) {
		this.accumulatedRestTime = accumulatedRestTime;
	}

	/**
	 * Returns hour part rounded down.<br>
	 * For instance if time is 1 hr 32 min - it will return 1 hr
	 *
	 * @return hours part of accumulated online time
	 */
	public int getAccumulatedOnlineHours() {
		return toHours(accumulatedOnlineTime);
	}

	/**
	 * Returns minutes part.<br>
	 * For instance: if time is 1 hr 32 min - it will return 32 min
	 *
	 * @return minutes part of accumulated online time
	 */
	public int getAccumulatedOnlineMinutes() {
		return toMinutes(accumulatedOnlineTime);
	}

	/**
	 * Returns hour part rounded down.<br>
	 * For instance if time is 1 hr 32 min - it will return 1 hr
	 *
	 * @return hours part of accumulated rest time
	 */
	public int getAccumulatedRestHours() {
		return toHours(accumulatedRestTime);
	}

	/**
	 * Returns minutes part.<br>
	 * For instance: if time is 1 hr 32 min - it will return 32 min
	 *
	 * @return minutes part of accumulated rest time
	 */
	public int getAccumulatedRestMinutes() {
		return toMinutes(accumulatedRestTime);
	}

	/**
	 * Converts milliseconds to hours.<br>
	 * For instance if millis = 1 hr 32 min, 1 hour will be returned
	 *
	 * @param millis
	 *            milliseconds
	 * @return hours
	 */
	private static int toHours(long millis) {
		return (int) (millis / 1000) / 3600;
	}

	/**
	 * Converts milliseconds to minutes.<br>
	 * For instance if millis = 1 hr 32 min, 32 min will be returned
	 *
	 * @param millis
	 *            milliseconds
	 * @return minutes
	 */
	private static int toMinutes(long millis) {
		return (int) ((millis / 1000) % 3600) / 60;
	}
}
