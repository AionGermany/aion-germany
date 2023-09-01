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

import com.aionemu.gameserver.model.instance.playerreward.RiftOfOblivionPlayerReward;

/**
 * @author Falke_34
 */
public class RiftOfOblivionReward extends InstanceReward<RiftOfOblivionPlayerReward> {

	private int points;
	private int npcKills;
	private int rank;
	private int icyOrbOfMemory;
	private boolean isRewarded;

	public RiftOfOblivionReward(Integer mapId, int instanceId) {
		super(mapId, instanceId);
	}

	public int getRank() {
		return rank;
	}

	public int getIcyOrbOfMemory() {
		return icyOrbOfMemory;
	}

	public void addNpcKill() {
		npcKills++;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getPoints() {
		return points;
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

	public void setIcyOrbOfMemory(int icyOrbOfMemory) {
		this.icyOrbOfMemory = icyOrbOfMemory;
	}
}
