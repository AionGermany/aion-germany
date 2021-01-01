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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerAchievementActionDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementAction;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementState;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementType;
import com.aionemu.gameserver.model.gameobjects.player.achievement.PlayerAchievement;

public class MySQL5PlayerAchievementActionDAO extends PlayerAchievementActionDAO {

	private Logger log = LoggerFactory.getLogger(MySQL5PlayerAchievementActionDAO.class);
	public static final String INSERT_ACTION = "INSERT INTO player_achievement_actions (object_id, id, player_id, state, step, type, start_date, end_date, achievement_obj) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String LOAD_QUERY = "SELECT * FROM `player_achievement_actions` WHERE `player_id`=? AND `achievement_obj`=?";
	public static final String UPDATE_QUERY = "UPDATE player_achievement_actions set state=?, step=? WHERE `player_id`=? AND `object_id`=?";

	@Override
	public void loadActions(final Player player, final PlayerAchievement achievement) {
		DB.select(LOAD_QUERY, new ParamReadStH() {
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, achievement.getObjectId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next()) {
					AchievementAction action = new AchievementAction(rset.getInt("object_id"));
					action.setStep(rset.getInt("step"));
					action.setId(rset.getInt("id"));
					action.setState(AchievementState.getPlayerClassById(rset.getInt("state")));
					action.setType(AchievementType.getPlayerClassById(rset.getInt("type")));
					action.setStartDate(rset.getTimestamp("start_date"));
					action.setEndateDate(rset.getTimestamp("end_date"));
					action.setAchievementObjectId(rset.getInt("achievement_obj"));
					achievement.getActionMap().put(rset.getInt("object_id"), action);
				}
			}
		});
	}

	@Override
	public boolean storeAction(Player player, AchievementAction action) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_ACTION);
			stmt.setInt(1, action.getObjectId());
			stmt.setInt(2, action.getId());
			stmt.setInt(3, player.getObjectId());
			stmt.setInt(4, action.getState().getValue());
			stmt.setInt(5, action.getStep());
			stmt.setInt(6, action.getType().getValue());
			stmt.setTimestamp(7, action.getStartDate());
			stmt.setTimestamp(8, action.getEndateDate());
			stmt.setInt(9, action.getAchievementObjectId());
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.error("addRank", e);

			return false;
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean update(Player player, AchievementAction action) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, action.getState().getValue());
			stmt.setInt(2, action.getStep());
			stmt.setInt(3, player.getObjectId());
			stmt.setInt(4, action.getObjectId());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not update AchievementAction data for Player " + player.getName() + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public void deleteAchievementsActions(AchievementType type) {
		PreparedStatement statement = DB.prepareStatement("DELETE FROM player_achievement_actions WHERE type = ?");
		try {
			statement.setInt(1, type.getValue());
		} catch (SQLException e) {
			log.error("Some crap, can't set int parameter to PreparedStatement", e);
		}
		DB.executeUpdateAndClose(statement);
	}

	@Override
	public int[] getUsedIDs() {
		PreparedStatement statement = DB.prepareStatement("SELECT object_id FROM player_achievement_actions", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		try {
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				ids[i] = rs.getInt("object_id");
			}
			return ids;
		} catch (SQLException e) {
			log.error("Can't get list of id's from achievements table", e);
		} finally {
			DB.close(statement);
		}

		return new int[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
