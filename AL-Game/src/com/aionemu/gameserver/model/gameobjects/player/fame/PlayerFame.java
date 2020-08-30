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
package com.aionemu.gameserver.model.gameobjects.player.fame;

import com.aionemu.gameserver.model.gameobjects.player.fame.FameEnum;
import com.aionemu.gameserver.services.player.PlayerFameService;

public class PlayerFame {

	private int id;
	private int level;
	private long exp;
	private long expLoss;
	private FameEnum fameEnum;
	private int playerId;

	public PlayerFame(int id, int level, long exp, long expLoss, int playerId) {
		this.id = id;
		this.level = level;
		this.exp = exp;
		this.expLoss = expLoss;
		this.fameEnum = FameEnum.getFameById(id);
		this.playerId = playerId;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public Long getMaxExp() {
		return PlayerFameService.getInstance().getExpForLevel(level);
	}

	public FameEnum getFameEnum() {
		return fameEnum;
	}

	public long getExpLoss() {
		return expLoss;
	}

	public void setExpLoss(long expLoss) {
		this.expLoss = expLoss;
	}

	public int getPlayerId() {
		return this.playerId;
	}
}
