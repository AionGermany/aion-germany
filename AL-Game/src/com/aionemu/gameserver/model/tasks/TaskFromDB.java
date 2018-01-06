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
package com.aionemu.gameserver.model.tasks;

import java.sql.Timestamp;

/**
 * @author Divinity
 */
public class TaskFromDB {

	private int id;
	private String name;
	private String type;
	private Timestamp lastActivation;
	private String startTime;
	private int delay;
	private String params[];

	/**
	 * Constructor
	 *
	 * @param id
	 *            : int
	 * @param name
	 *            : String
	 * @param type
	 *            : String
	 * @param lastActivation
	 *            : Timestamp
	 * @param startTime
	 *            : String
	 * @param delay
	 *            : int
	 * @param param
	 *            : String
	 */
	public TaskFromDB(int id, String name, String type, Timestamp lastActivation, String startTime, int delay, String param) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.lastActivation = lastActivation;
		this.startTime = startTime;
		this.delay = delay;

		if (param != null) {
			this.params = param.split(" ");
		}
		else {
			this.params = new String[0];
		}
	}

	/**
	 * Task's id
	 *
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Task's name
	 *
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Task's type : - FIXED_IN_TIME (HH:MM:SS)
	 *
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Task's last activation
	 *
	 * @return Timestamp
	 */
	public Timestamp getLastActivation() {
		return lastActivation;
	}

	/**
	 * Task's starting time (HH:MM:SS format)
	 *
	 * @return String
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * Task's delay
	 *
	 * @return int
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Task's param(s)
	 *
	 * @return String[]
	 */
	public String[] getParams() {
		return params;
	}
}
