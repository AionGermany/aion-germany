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
package com.aionemu.gameserver.services.ranking;

import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerRankingDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfCooperationRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfDisciplineRank;
import com.aionemu.gameserver.model.ranking.PlayerRankingEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MY_DOCUMENTATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RANK_LIST;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

public class PlayerRankingService {

	private int lastUpdate;
	private final FastMap<Integer, List<SM_RANK_LIST>> players = new FastMap<Integer, List<SM_RANK_LIST>>();

	public void loadPacketPlayer(Player player, int tableid) {
		if (tableid == 541) {
			loadArenaOfDisciplineScore(player);
		} else if (tableid == 741) {
			loadArenaOfCooperationScore(player);
		} else {
			return;
		}
	}

	public void loadArenaOfDisciplineScore(Player player) {
		ArenaOfDisciplineRank rank = getDAO().loadArenaOfDisciplineRank(player.getObjectId(), PlayerRankingEnum.ARENA_OF_DISCIPLINE.getId());
		player.setDisciplineRank(rank);
		PacketSendUtility.sendPacket(player, new SM_MY_DOCUMENTATION(PlayerRankingEnum.ARENA_OF_DISCIPLINE.getId(), player.getDisciplineRank()));
	}

	public void loadArenaOfCooperationScore(Player player) {
		ArenaOfCooperationRank rank = getDAO().loadArenaOfCooperationRank(player.getObjectId(), PlayerRankingEnum.ARENA_OF_COOPERATION.getId());
		player.setCooperationRank(rank);
		PacketSendUtility.sendPacket(player, new SM_MY_DOCUMENTATION(PlayerRankingEnum.ARENA_OF_COOPERATION.getId(), player.getCooperationRank()));
	}

	private PlayerRankingDAO getDAO() {
		return DAOManager.getDAO(PlayerRankingDAO.class);
	}

	public static final PlayerRankingService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		protected static final PlayerRankingService INSTANCE = new PlayerRankingService();
	}
}