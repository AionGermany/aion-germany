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
package com.aionemu.gameserver.services.abyss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.RankingConfig;
import com.aionemu.gameserver.dao.AbyssRankDAO;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author ATracer
 * @author ThunderBolt - GloryPoints
 * @rework Phantom_KNA
 */
public class AbyssRankUpdateService {

	private static final Logger log = LoggerFactory.getLogger(AbyssRankUpdateService.class);
	private static final String GP_UPDATA_TIME = "0 10 2 ? * *";
	private static final Logger debuglog = LoggerFactory.getLogger("ABYSSRANK_LOG");

	private AbyssRankUpdateService() {
	}

	public static AbyssRankUpdateService getInstance() {
		return SingletonHolder.instance;
	}

	public void GpointUpdata() {
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AbyssRankUpdateService.this.loadGpRank();
			}
		}, GP_UPDATA_TIME);
	}

	private void loadGpRank() {
		List<Integer> rankPlayers = DAOManager.getDAO(AbyssRankDAO.class).RankPlayers(9);
		reduceGP(rankPlayers);
	}

	private void reduceGP(List<Integer> rankPlayers) {
		for (int playerId : rankPlayers) {
			AbyssRank rank = DAOManager.getDAO(AbyssRankDAO.class).loadAbyssRank(playerId);
			Player player = World.getInstance().findPlayer(playerId);
			int lostGp = rank.getRank().getDailyReduceGp();
			// Only Rank Officer
			if (rank.getRank().getId() < AbyssRankEnum.STAR1_OFFICER.getId())
				continue;
			if (player != null) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402082, new Object[0]));
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402209, player.getName(), rank.getRank().getDailyReduceGp()));
				AbyssPointsService.addGp(player, lostGp * -1);
			}
			else {
				int newGP = rank.getGp() - lostGp;
				if (newGP < 0)
					newGP = 0;
				debuglog.info("[GP REWARD LOG] Scheduled delete. Player: " + playerId + ". Last: " + rank.getGp() + ". New: " + newGP);
				DAOManager.getDAO(AbyssRankDAO.class).updateGloryPoints(playerId, newGP);
			}
		}
	}

	public void scheduleUpdate() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < System.currentTimeMillis() / 1000) {
			performUpdate();
		}

		log.debug("[AbyssRankUpdateService] Starting ranking update task based on cron expression: " + RankingConfig.TOP_RANKING_UPDATE_RULE);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performUpdate();
			}
		}, RankingConfig.TOP_RANKING_UPDATE_RULE, true);
	}

	/**
	 * Perform update of all ranks
	 */
	public void performUpdate() {
		log.debug("[AbyssRankUpdateService] executing rank update");
		long startTime = System.currentTimeMillis();

		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				player.getAbyssRank().doUpdate();
				DAOManager.getDAO(AbyssRankDAO.class).storeAbyssRank(player);
			}
		});

		updateLimitedRanks();
		updateLimitedGpRanks();
		AbyssRankingCache.getInstance().reloadRankings();
		log.debug("[AbyssRankUpdateService] execution time: " + (System.currentTimeMillis() - startTime) / 1000);
	}

	/**
	 * Update player ranks based on quota for all players (online/offline)
	 */
	private void updateLimitedRanks() {
		updateAllRanksForRace(Race.ASMODIANS, AbyssRankEnum.GRADE9_SOLDIER.getRequiredAp(), RankingConfig.TOP_RANKING_MAX_OFFLINE_DAYS);
		updateAllRanksForRace(Race.ELYOS, AbyssRankEnum.GRADE9_SOLDIER.getRequiredAp(), RankingConfig.TOP_RANKING_MAX_OFFLINE_DAYS);
	}

	private void updateLimitedGpRanks() {
		updateAllRanksGpForRace(Race.ASMODIANS, AbyssRankEnum.STAR1_OFFICER.getRequiredGp(), RankingConfig.TOP_RANKING_MAX_OFFLINE_DAYS);
		updateAllRanksGpForRace(Race.ELYOS, AbyssRankEnum.STAR1_OFFICER.getRequiredGp(), RankingConfig.TOP_RANKING_MAX_OFFLINE_DAYS);
	}

	private void updateAllRanksForRace(Race race, int apLimit, int activeAfterDays) {
		Map<Integer, Integer> playerApMap = DAOManager.getDAO(AbyssRankDAO.class).loadPlayersAp(race, apLimit, activeAfterDays);
		List<Entry<Integer, Integer>> playerApEntries = new ArrayList<Entry<Integer, Integer>>(playerApMap.entrySet());
		Collections.sort(playerApEntries, new PlayerApComparator<Integer, Integer>());

		selectRank(AbyssRankEnum.GRADE1_SOLDIER, playerApEntries);
		selectRank(AbyssRankEnum.GRADE2_SOLDIER, playerApEntries);
		selectRank(AbyssRankEnum.GRADE3_SOLDIER, playerApEntries);
		selectRank(AbyssRankEnum.GRADE4_SOLDIER, playerApEntries);
		selectRank(AbyssRankEnum.GRADE5_SOLDIER, playerApEntries);
		selectRank(AbyssRankEnum.GRADE6_SOLDIER, playerApEntries);
		selectRank(AbyssRankEnum.GRADE7_SOLDIER, playerApEntries);
		selectRank(AbyssRankEnum.GRADE8_SOLDIER, playerApEntries);
		selectRank(AbyssRankEnum.GRADE9_SOLDIER, playerApEntries);

		updateToNoQuotaRank(playerApEntries);
	}

	private void updateAllRanksGpForRace(Race race, int gpLimit, int activeAfterDays) {
		Map<Integer, Integer> playerGpMap = DAOManager.getDAO(AbyssRankDAO.class).loadPlayersGp(race, gpLimit, activeAfterDays);
		List<Entry<Integer, Integer>> playerGpEntries = new ArrayList<Entry<Integer, Integer>>(playerGpMap.entrySet());
		Collections.sort(playerGpEntries, new PlayerGpComparator<Integer, Integer>());

		selectGpRank(AbyssRankEnum.SUPREME_COMMANDER, playerGpEntries);
		selectGpRank(AbyssRankEnum.COMMANDER, playerGpEntries);
		selectGpRank(AbyssRankEnum.GREAT_GENERAL, playerGpEntries);
		selectGpRank(AbyssRankEnum.GENERAL, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR5_OFFICER, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR4_OFFICER, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR3_OFFICER, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR2_OFFICER, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR1_OFFICER, playerGpEntries);

		updateToNoQuotaGpRank(playerGpEntries);
	}

	private void selectRank(AbyssRankEnum rank, List<Entry<Integer, Integer>> playerApEntries) {
		int quota = rank.getId() < 9 ? (rank.getQuota() - AbyssRankEnum.getRankById(rank.getId() + 1).getQuota()) : rank.getQuota();
		for (int i = 0; i < quota; i++) {
			if (playerApEntries.isEmpty()) {
				return;
			}
			// check next player in list
			Entry<Integer, Integer> playerAp = playerApEntries.get(0);
			// check if there are some players left in map
			if (playerAp == null) {
				return;
			}
			int playerId = playerAp.getKey();
			int ap = playerAp.getValue();
			// check if this (and the rest) player has required ap count
			if (ap < rank.getRequiredAp()) {
				return;
			}
			// remove player and update its rank
			playerApEntries.remove(0);
			updateRankTo(rank, playerId);
		}
	}

	private void selectGpRank(AbyssRankEnum rank, List<Entry<Integer, Integer>> playerGpEntries) {
		int quota = (rank.getId() > 9 && rank.getId() < 18) ? rank.getQuota() - AbyssRankEnum.getRankById(rank.getId() + 1).getQuota() : rank.getQuota();
		for (int i = 0; i < quota; i++) {
			if (playerGpEntries.isEmpty()) {
				return;
			}
			// check next player in list
			Entry<Integer, Integer> playerGp = playerGpEntries.get(0);
			// check if there are some players left in map
			if (playerGp == null) {
				return;
			}
			int playerId = playerGp.getKey();
			int gp = playerGp.getValue();
			// check if this (and the rest) player has required gp count
			if (gp < rank.getRequiredGp()) {
				return;
			}
			// remove player and update its rankGp
			playerGpEntries.remove(0);
			updateGpRankTo(rank, playerId);
		}
	}

	private void updateToNoQuotaRank(List<Entry<Integer, Integer>> playerApEntries) {
		for (Entry<Integer, Integer> playerApEntry : playerApEntries) {
			updateRankTo(AbyssRankEnum.GRADE1_SOLDIER, playerApEntry.getKey());
		}
	}

	private void updateToNoQuotaGpRank(List<Entry<Integer, Integer>> playerGpEntries) {
		for (Entry<Integer, Integer> playerGpEntry : playerGpEntries) {
			updateGpRankTo(AbyssRankEnum.SUPREME_COMMANDER, playerGpEntry.getKey());
		}
	}

	protected void updateRankTo(AbyssRankEnum newRank, int playerId) {
		// check if rank is changed for online players
		Player onlinePlayer = World.getInstance().findPlayer(playerId);
		if (onlinePlayer != null) {
			AbyssRank abyssRank = onlinePlayer.getAbyssRank();
			AbyssRankEnum currentRank = abyssRank.getRank();
			if (currentRank != newRank) {
				abyssRank.setRank(newRank);
				AbyssPointsService.checkRankChanged(onlinePlayer, currentRank, newRank);
			}
		}
		else {
			DAOManager.getDAO(AbyssRankDAO.class).updateAbyssRank(playerId, newRank);
		}
	}

	protected void updateGpRankTo(AbyssRankEnum newRank, int playerId) {
		// check if rankGp is changed for online players
		Player onlinePlayer = World.getInstance().findPlayer(playerId);
		if (onlinePlayer != null) {
			AbyssRank abyssRank = onlinePlayer.getAbyssRank();
			AbyssRankEnum currentRank = abyssRank.getRank();
			if (currentRank != newRank) {
				abyssRank.setRank(newRank);
				AbyssPointsService.checkRankGpChanged(onlinePlayer, currentRank, newRank);
			}
		}
		else {
			DAOManager.getDAO(AbyssRankDAO.class).updateAbyssRank(playerId, newRank);
		}
	}

	private static class SingletonHolder {

		protected static final AbyssRankUpdateService instance = new AbyssRankUpdateService();
	}

	private static class PlayerApComparator<K, V extends Comparable<V>> implements Comparator<Entry<K, V>> {

		@Override
		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			return -o1.getValue().compareTo(o2.getValue()); // descending order
		}
	}

	private static class PlayerGpComparator<K, V extends Comparable<V>> implements Comparator<Entry<K, V>> {

		@Override
		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			return -o1.getValue().compareTo(o2.getValue()); // descending order
		}
	}
}
