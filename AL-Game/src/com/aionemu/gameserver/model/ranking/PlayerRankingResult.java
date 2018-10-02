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
package com.aionemu.gameserver.model.ranking;

import com.aionemu.gameserver.model.PlayerClass;

public class PlayerRankingResult {

	private String playerName;
	private int oldRank;
	private int rank;
	private int pc;
	private PlayerClass playerClass;
	private int playerRace;
	private int playerId;

	public PlayerRankingResult(String playerName, int oldRank, int rank, int pc, PlayerClass playerClass, int playerRace, int playerId) {
		this.playerName = playerName;
		this.oldRank = oldRank;
		this.rank = rank;
		this.pc = pc;
		this.playerClass = playerClass;
		this.playerRace = playerRace;
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getRank() {
		return rank;
	}

	public int getOldRank() {
		return oldRank;
	}

	public int getPlayerRace() {
		return playerRace;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public int getPoints() {
		return pc;
	}
}
