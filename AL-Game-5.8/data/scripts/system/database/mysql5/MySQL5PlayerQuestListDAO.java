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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.utils.GenericValidator;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerQuestListDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.QuestStateList;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author MrPoke
 * @modified vlog, Rolandas
 */
public class MySQL5PlayerQuestListDAO extends PlayerQuestListDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerQuestListDAO.class);
	public static final String SELECT_QUERY = "SELECT `quest_id`, `status`, `quest_vars`, `complete_count`, `next_repeat_time`, `reward`, `complete_time` FROM `player_quests` WHERE `player_id`=?";
	public static final String UPDATE_QUERY = "UPDATE `player_quests` SET `status`=?, `quest_vars`=?, `complete_count`=?, `next_repeat_time`=?, `reward`=?, `complete_time`=? WHERE `player_id`=? AND `quest_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_quests` WHERE `player_id`=? AND `quest_id`=?";
	public static final String INSERT_QUERY = "INSERT INTO `player_quests` (`player_id`, `quest_id`, `status`, `quest_vars`, `complete_count`, `next_repeat_time`, `reward`, `complete_time`) VALUES (?,?,?,?,?,?,?,?)";
	private static final Predicate<QuestState> questsToAddPredicate = new Predicate<QuestState>() {

		@Override
		public boolean apply(@Nullable QuestState input) {
			return input != null && PersistentState.NEW == input.getPersistentState();
		}
	};
	private static final Predicate<QuestState> questsToUpdatePredicate = new Predicate<QuestState>() {

		@Override
		public boolean apply(@Nullable QuestState input) {
			return input != null && PersistentState.UPDATE_REQUIRED == input.getPersistentState();
		}
	};
	private static final Predicate<QuestState> questsToDeletePredicate = new Predicate<QuestState>() {

		@Override
		public boolean apply(@Nullable QuestState input) {
			return input != null && PersistentState.DELETED == input.getPersistentState();
		}
	};

	@Override
	public QuestStateList load(final Player player) {
		QuestStateList questStateList = new QuestStateList();

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int questId = rset.getInt("quest_id");
				int questVars = rset.getInt("quest_vars");
				int completeCount = rset.getInt("complete_count");
				Timestamp nextRepeatTime = rset.getTimestamp("next_repeat_time");
				Integer reward = rset.getInt("reward");
				if (rset.wasNull()) {
					reward = 0;
				}
				Timestamp completeTime = rset.getTimestamp("complete_time");
				QuestStatus status = QuestStatus.valueOf(rset.getString("status"));
				QuestState questState = new QuestState(questId, status, questVars, completeCount, nextRepeatTime, reward, completeTime);
				questState.setPersistentState(PersistentState.UPDATED);
				questStateList.addQuest(questId, questState);
			}
			rset.close();
		}
		catch (Exception e) {
			log.error("Could not restore QuestStateList data for player: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		return questStateList;
	}

	@Override
	public void store(Player player) {

		Collection<QuestState> qsList = player.getQuestStateList().getAllQuestState();
		if (GenericValidator.isBlankOrNull(qsList)) {
			return;
		}

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			con.setAutoCommit(false);

			deleteQuest(con, player.getObjectId(), qsList);

			addQuests(con, player.getObjectId(), qsList);
			updateQuests(con, player.getObjectId(), qsList);
		}
		catch (SQLException e) {
			log.error("Can't save quests for player " + player.getObjectId(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}

		for (QuestState qs : qsList) {
			qs.setPersistentState(PersistentState.UPDATED);
		}
	}

	private void addQuests(Connection con, int playerId, Collection<QuestState> states) {

		states = Collections2.filter(states, questsToAddPredicate);

		if (GenericValidator.isBlankOrNull(states)) {
			return;
		}

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(INSERT_QUERY);

			for (QuestState qs : states) {
				ps.setInt(1, playerId);
				ps.setInt(2, qs.getQuestId());
				ps.setString(3, qs.getStatus().toString());
				ps.setInt(4, qs.getQuestVars().getQuestVars());
				ps.setInt(5, qs.getCompleteCount());
				if (qs.getNextRepeatTime() != null) {
					ps.setTimestamp(6, qs.getNextRepeatTime());
				}
				else {
					ps.setNull(6, Types.TIMESTAMP);
				}
				if (qs.getReward() == null) {
					ps.setNull(7, Types.INTEGER);
				}
				else {
					ps.setInt(7, qs.getReward());
				}
				if (qs.getCompleteTime() == null) {
					ps.setNull(8, Types.TIMESTAMP);
				}
				else {
					ps.setTimestamp(8, qs.getCompleteTime());
				}
				ps.addBatch();
			}

			ps.executeBatch();
			con.commit();
		}
		catch (SQLException e) {
			log.error("Failed to insert new quests for player " + playerId);
		}
		finally {
			DatabaseFactory.close(ps);
		}
	}

	private void updateQuests(Connection con, int playerId, Collection<QuestState> states) {

		states = Collections2.filter(states, questsToUpdatePredicate);

		if (GenericValidator.isBlankOrNull(states)) {
			return;
		}

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(UPDATE_QUERY);

			for (QuestState qs : states) {
				ps.setString(1, qs.getStatus().toString());
				ps.setInt(2, qs.getQuestVars().getQuestVars());
				ps.setInt(3, qs.getCompleteCount());
				if (qs.getNextRepeatTime() != null) {
					ps.setTimestamp(4, qs.getNextRepeatTime());
				}
				else {
					ps.setNull(4, Types.TIMESTAMP);
				}
				if (qs.getReward() == null) {
					ps.setNull(5, Types.SMALLINT);
				}
				else {
					ps.setInt(5, qs.getReward());
				}
				if (qs.getCompleteTime() == null) {
					ps.setNull(6, Types.TIMESTAMP);
				}
				else {
					ps.setTimestamp(6, qs.getCompleteTime());
				}
				ps.setInt(7, playerId);
				ps.setInt(8, qs.getQuestId());
				ps.addBatch();
			}

			ps.executeBatch();
			con.commit();
		}
		catch (SQLException e) {
			log.error("Failed to update existing quests for player " + playerId);
		}
		finally {
			DatabaseFactory.close(ps);
		}
	}

	private void deleteQuest(Connection con, int playerId, Collection<QuestState> states) {

		states = Collections2.filter(states, questsToDeletePredicate);

		if (GenericValidator.isBlankOrNull(states)) {
			return;
		}

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(DELETE_QUERY);

			for (QuestState qs : states) {
				ps.setInt(1, playerId);
				ps.setInt(2, qs.getQuestId());
				ps.addBatch();
			}

			ps.executeBatch();
			con.commit();
		}
		catch (SQLException e) {
			log.error("Failed to delete existing quests for player " + playerId);
		}
		finally {
			DatabaseFactory.close(ps);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
