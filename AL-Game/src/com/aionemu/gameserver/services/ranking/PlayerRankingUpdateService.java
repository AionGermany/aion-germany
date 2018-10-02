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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerRankingDAO;
import com.aionemu.gameserver.model.ranking.PlayerRankingEnum;
import com.aionemu.gameserver.model.ranking.PlayerRankingResult;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RANK_LIST;

import javolution.util.FastMap;

public class PlayerRankingUpdateService {

	private static final Logger log = LoggerFactory.getLogger(PlayerRankingUpdateService.class);
	private int lastUpdate;
	private final FastMap<Integer, List<SM_RANK_LIST>> players = new FastMap<Integer, List<SM_RANK_LIST>>();

	public void onStart() {
		renewPlayerRanking(PlayerRankingEnum.ARENA_OF_DISCIPLINE.getId());
		renewPlayerRanking(PlayerRankingEnum.ARENA_OF_COOPERATION.getId());
		log.info("[PlayerRankingUpdateService] Player Ranking Loaded");
	}

	private void renewPlayerRanking(int tableId) {
		List<SM_RANK_LIST> newlyCalculated;
		newlyCalculated = loadRankPacket(tableId);
		players.remove(tableId);
		players.put(tableId, newlyCalculated);
		log.info("[PlayerRankingUpdateService] Player Ranking Updated");
	}

	private List<SM_RANK_LIST> loadRankPacket(int tableid) {
		ArrayList<PlayerRankingResult> list = getDAO().getCompetitionRankingPlayers(tableid);
		int page = 1;
		List<SM_RANK_LIST> playerPackets = new ArrayList<SM_RANK_LIST>();
		for (int i = 0; i < list.size(); i += 94) {
			if (list.size() > i + 94) {
				playerPackets.add(new SM_RANK_LIST(tableid, 0, list.subList(i, i + 94), lastUpdate));
				playerPackets.add(new SM_RANK_LIST(tableid, 1, list.subList(i, i + 94), lastUpdate));
			} else {
				playerPackets.add(new SM_RANK_LIST(tableid, 0, list.subList(i, list.size()), lastUpdate));
				playerPackets.add(new SM_RANK_LIST(tableid, 1, list.subList(i, list.size()), lastUpdate));
			}
			page++;
		}
		return playerPackets;
	}

	public List<SM_RANK_LIST> getPlayers(int tableId) {
		return players.get(tableId);
	}

	private PlayerRankingDAO getDAO() {
		return DAOManager.getDAO(PlayerRankingDAO.class);
	}

	public static final PlayerRankingUpdateService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		protected static final PlayerRankingUpdateService INSTANCE = new PlayerRankingUpdateService();
	}
}