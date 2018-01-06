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
package com.aionemu.gameserver.model.monsterbook;

public class MonsterbookEntry {

	private int id;
	private int killCount;
	private int level;
	private int claimReward;

	public MonsterbookEntry(int id, int killCount, int level, int claimReward) {
		this.id = id;
		this.killCount = killCount;
		this.level = level;
		this.claimReward = claimReward;
	}

	public int getId() {
		return id;
	}

	public int getKillCount() {
		return killCount;
	}

	public int getLevel() {
		return level;
	}

	public int claimRewardLevel() {
		return claimReward;
	}
}
