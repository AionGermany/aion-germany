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
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfCooperationRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfDisciplineRank;
import com.aionemu.gameserver.model.ranking.PlayerRankingResult;

import java.util.ArrayList;

public abstract class PlayerRankingDAO implements DAO {

	@Override
	public final String getClassName() {
		return PlayerRankingDAO.class.getName();
	}

	public abstract ArrayList<PlayerRankingResult> getCompetitionRankingPlayers(int tableId);

	public abstract ArenaOfDisciplineRank loadArenaOfDisciplineRank(int playerId, int tableId);

	public abstract ArenaOfCooperationRank loadArenaOfCooperationRank(int playerId, int tableId);

	public abstract boolean storeDisciplineRank(Player player);

	public abstract boolean storeCooperationRank(Player player);
}
