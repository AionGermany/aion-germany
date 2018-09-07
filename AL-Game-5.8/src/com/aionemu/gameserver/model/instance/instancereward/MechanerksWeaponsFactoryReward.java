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
package com.aionemu.gameserver.model.instance.instancereward;

import com.aionemu.gameserver.model.instance.playerreward.MechanerksWeaponsFactoryPlayerReward;

/**
 * @author Falke_34
 */
public class MechanerksWeaponsFactoryReward extends InstanceReward<MechanerksWeaponsFactoryPlayerReward> {

	private int points;
	private int npcKills;
	private int rank;
	private int goldChest;
	private int treasureChest;
	private int secretChest;
	private boolean isRewarded;

	public MechanerksWeaponsFactoryReward(Integer mapId, int instanceId) {
		super(mapId, instanceId);
	}

	public int getRank() {
		return rank;
	}

	public int getTreasureChest() {
		return treasureChest;
	}

	public void addNpcKill() {
		npcKills++;
	}

	public void setSecretChest(int secretChest) {
		this.secretChest = secretChest;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getPoints() {
		return points;
	}

	public int getSecretChest() {
		return secretChest;
	}

	public int getNpcKills() {
		return npcKills;
	}

	public void setRewarded() {
		isRewarded = true;
	}

	public void addPoints(int points) {
		this.points += points;
	}

	@Override
	public boolean isRewarded() {
		return isRewarded;
	}

	public void setGoldChest(int goldChest) {
		this.goldChest = goldChest;
	}

	public int getGoldChest() {
		return goldChest;
	}

	public void setTreasureChest(int treasureChest) {
		this.treasureChest = treasureChest;
	}
}
