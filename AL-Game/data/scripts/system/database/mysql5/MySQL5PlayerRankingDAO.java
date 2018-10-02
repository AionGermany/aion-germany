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
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.PlayerRankingDAO;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfCooperationRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfDisciplineRank;
import com.aionemu.gameserver.model.ranking.PlayerRankingResult;

public class MySQL5PlayerRankingDAO extends PlayerRankingDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerRankingDAO.class);

	public static final String SELECT_PLAYERS_RANKING = "SELECT player_ranking.rank, player_ranking.last_rank, player_ranking.points, player_ranking.player_id, players.name, players.id, players.player_class, players.race FROM player_ranking INNER JOIN players ON player_ranking.player_id = players.id WHERE player_ranking.table_id = ? AND player_ranking.points > 0 ORDER BY player_ranking.points DESC LIMIT 0, 300";
	public static final String SELECT_MY_HISTORY = "SELECT * FROM player_ranking  WHERE player_id = ? AND table_id = ?";
	public static final String INSERT_QUERY = "INSERT INTO player_ranking (player_id, table_id, rank, last_rank, points, last_points, high_points, low_points, position_match) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_QUERY = "UPDATE player_ranking SET  rank = ?, last_rank = ?, points = ?, last_points = ?, high_points = ?, low_points = ?, position_match = ? WHERE player_id = ? AND table_id = ?";

	@Override
	public ArrayList<PlayerRankingResult> getCompetitionRankingPlayers(int tableId) {
		Connection con = null;
		final ArrayList<PlayerRankingResult> results = new ArrayList<PlayerRankingResult>();
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_PLAYERS_RANKING);
			stmt.setInt(1, tableId);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString("players.name");
				int rank = resultSet.getInt("player_ranking.rank");
				int last_rank = resultSet.getInt("player_ranking.last_rank");
				int pc = resultSet.getInt("player_ranking.points");
				int playerId = resultSet.getInt("players.id");
				String playerClassStr = resultSet.getString("players.player_class");
				PlayerClass playerClass = PlayerClass.getPlayerClassByString(playerClassStr);
				String race = resultSet.getString("players.race");
				if (playerClass == null)
					continue;
				PlayerRankingResult rsl = new PlayerRankingResult(name, last_rank, rank, pc, playerClass,
						Race.getRaceByString(race.toString()).getRaceId(), playerId);
				results.add(rsl);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			log.error("getCompetitionRankingPlayers", e);
		} finally {
			DatabaseFactory.close(con);
		}
		return results;
	}

	@Override
	public boolean storeDisciplineRank(Player player) {
		ArenaOfDisciplineRank rank = player.getDisciplineRank();
		boolean result = false;
		switch (rank.getPersistentState()) {
		case NEW:
			result = addDisciplineRank(player.getObjectId(), rank);
			break;
		case UPDATE_REQUIRED:
			result = updateDisciplineRank(player.getObjectId(), rank);
			break;
		default:
			break;
		}
		rank.setPersistentState(PersistentState.UPDATED);
		return result;
	}

	@Override
	public boolean storeCooperationRank(Player player) {
		ArenaOfCooperationRank rank = player.getCooperationRank();
		boolean result = false;
		switch (rank.getPersistentState()) {
		case NEW:
			result = addCooperationRank(player.getObjectId(), rank);
			break;
		case UPDATE_REQUIRED:
			result = updateCooperationRank(player.getObjectId(), rank);
			break;
		default:
			break;
		}
		rank.setPersistentState(PersistentState.UPDATED);
		return result;
	}

	private boolean addDisciplineRank(final int objectId, final ArenaOfDisciplineRank rank) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, 541);
			stmt.setInt(3, rank.getRank());
			stmt.setInt(4, rank.getBestRank());
			stmt.setInt(5, rank.getPoints());
			stmt.setInt(6, rank.getLastPoints());
			stmt.setInt(7, rank.getHighPoints());
			stmt.setInt(8, rank.getLowPoints());
			stmt.setInt(9, rank.getPossitionMatch());
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.error("addDisciplineRank", e);

			return false;
		} finally {
			DatabaseFactory.close(con);
		}
	}

	private boolean addCooperationRank(final int objectId, final ArenaOfCooperationRank rank) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, 541);
			stmt.setInt(3, rank.getRank());
			stmt.setInt(4, rank.getBestRank());
			stmt.setInt(5, rank.getPoints());
			stmt.setInt(6, rank.getLastPoints());
			stmt.setInt(7, rank.getHighPoints());
			stmt.setInt(8, rank.getLowPoints());
			stmt.setInt(9, rank.getPossitionMatch());
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.error("addCooperationRank", e);

			return false;
		} finally {
			DatabaseFactory.close(con);
		}
	}

	private boolean updateDisciplineRank(final int objectId, ArenaOfDisciplineRank rank) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, rank.getRank());
			stmt.setInt(2, rank.getBestRank());
			stmt.setInt(3, rank.getPoints());
			stmt.setInt(4, rank.getLastPoints());
			stmt.setInt(5, rank.getHighPoints());
			stmt.setInt(6, rank.getLowPoints());
			stmt.setInt(7, rank.getPossitionMatch());
			stmt.setInt(8, objectId);
			stmt.setInt(9, 541);
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.error("updateDisciplineRank", e);

			return false;
		} finally {
			DatabaseFactory.close(con);
		}
	}

	private boolean updateCooperationRank(final int objectId, ArenaOfCooperationRank rank) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, rank.getRank());
			stmt.setInt(2, rank.getBestRank());
			stmt.setInt(3, rank.getPoints());
			stmt.setInt(4, rank.getLastPoints());
			stmt.setInt(5, rank.getHighPoints());
			stmt.setInt(6, rank.getLowPoints());
			stmt.setInt(7, rank.getPossitionMatch());
			stmt.setInt(8, objectId);
			stmt.setInt(9, 541);
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.error("updateCooperationRank", e);

			return false;
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public ArenaOfDisciplineRank loadArenaOfDisciplineRank(int playerId, int tableId) {
		ArenaOfDisciplineRank ranking = null;
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_MY_HISTORY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, tableId);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				int rank = resultSet.getInt("rank");
				int best_rank = resultSet.getInt("last_rank");
				int point = resultSet.getInt("points");
				int last_point = resultSet.getInt("last_points");
				int high_point = resultSet.getInt("high_points");
				int low_point = resultSet.getInt("low_points");
				int position_match = resultSet.getInt("position_match");
				ranking = new ArenaOfDisciplineRank(rank, best_rank, point, last_point, high_point, low_point,
						position_match);
				ranking.setPersistentState(PersistentState.UPDATED);
			} else {
				ranking = new ArenaOfDisciplineRank(0, 0, 0, 0, 0, 0, 0);
				ranking.setPersistentState(PersistentState.NEW);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			log.error("loadArenaOfDisciplineRank", e);
		} finally {
			DatabaseFactory.close(con);
		}
		return ranking;
	}

	@Override
	public ArenaOfCooperationRank loadArenaOfCooperationRank(int playerId, int tableId) {
		ArenaOfCooperationRank ranking = null;
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_MY_HISTORY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, tableId);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				int rank = resultSet.getInt("rank");
				int best_rank = resultSet.getInt("last_rank");
				int point = resultSet.getInt("points");
				int last_point = resultSet.getInt("last_points");
				int high_point = resultSet.getInt("high_points");
				int low_point = resultSet.getInt("low_points");
				int position_match = resultSet.getInt("position_match");
				ranking = new ArenaOfCooperationRank(rank, best_rank, point, last_point, high_point, low_point,
						position_match);
				ranking.setPersistentState(PersistentState.UPDATED);
			} else {
				ranking = new ArenaOfCooperationRank(0, 0, 0, 0, 0, 0, 0);
				ranking.setPersistentState(PersistentState.NEW);
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			log.error("loadArenaOfCooperationRank", e);
		} finally {
			DatabaseFactory.close(con);
		}
		return ranking;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return com.aionemu.gameserver.dao.MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
