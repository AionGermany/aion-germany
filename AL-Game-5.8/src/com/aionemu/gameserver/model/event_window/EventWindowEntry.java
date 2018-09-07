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
package com.aionemu.gameserver.model.event_window;

import java.sql.Timestamp;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class EventWindowEntry {

	private int id;
	private Timestamp lastStamp;
	private int elapsed;

	/**
	 * entry for id timestamp and elapsed time
	 */
	public EventWindowEntry(int id, Timestamp timestamp, int elapsed) {
		this.id = id;
		this.lastStamp = timestamp;
		this.elapsed = elapsed;
	}

	/**
	 * get id timestamp + elapsed time
	 */
	public int getId() {
		return id;
	}

	public Timestamp getLastStamp() {
		return lastStamp;
	}

	public int getElapsed() {
		return elapsed;
	}
}
