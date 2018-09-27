/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 * Aion-Lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Aion-Lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.cubics;

/**
 * @author Phantom_KNA
 */
public class MCEntry {

	private int cubeid;
	private int rank;
	private int level;
	private int stat_value;
	private int category;

	public MCEntry(int cubeid, int rank, int level, int stat_value, int category) {
		this.cubeid = cubeid;
		this.rank = rank;
		this.level = level;
		this.stat_value = stat_value;
		this.category = category;
	}

	public int getCubeId() {
		return this.cubeid;
	}

	public int getRank() {
		return this.rank;
	}

	public int getLevel() {
		return this.level;
	}

	public int getStatValue() {
		return this.stat_value;
	}

	public int getCategory() {
		return this.category;
	}
}
