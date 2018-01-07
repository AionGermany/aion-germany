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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerGameStatsDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Glass
 * @modify xXMashUpXx
 * @modify off hand magical attack Ever'
 */
public class MySQL5PlayerGameStatsDAO extends PlayerGameStatsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerGameStatsDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `player_game_stats` (`player_id`, `defense_physic`, `block`, `parry`, `magical_critical`, `evasion`, `precision`, `attack`, `magical_precision`, `attack_speed`, `magical_resist`, `magical_attack`, `physical_critical`, `attack_range`, `magical_defense`, `agility`, `knowledge`, `will`, `magical_boost`, `magical_boost_resist`, `physical_critical_resist`, `magical_critical_resist`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `player_game_stats` WHERE `player_id`=?";
	public static final String UPDATE_QUERY = "UPDATE player_game_stats set `defense_physic` = ?, `block` = ?, `parry` = ?, `magical_critical` = ?, `evasion` = ?, `precision` = ?, `attack` = ?, `magical_precision` = ?, `attack_speed` = ?, `magical_resist` = ?, `magical_attack` = ?, `physical_critical` = ?, `attack_range` = ?, `magical_defense` = ?, `agility` = ?, `knowledge` = ?, `will` = ?, `magical_boost` = ?, `magical_boost_resist` = ?, `physical_critical_resist` = ?, `magical_critical_resist` = ? WHERE `player_id` = ?";

	@Override
	public void insertPlayerGameStat(final Player player) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, player.getGameStats().getPDef().getCurrent());
			stmt.setInt(3, player.getGameStats().getBlock().getCurrent());
			stmt.setInt(4, player.getGameStats().getParry().getCurrent());
			stmt.setInt(5, player.getGameStats().getMCritical().getCurrent());
			stmt.setInt(6, player.getGameStats().getEvasion().getCurrent());
			stmt.setInt(7, player.getGameStats().getAccuracy().getCurrent());
			stmt.setInt(8, player.getGameStats().getPower().getCurrent());
			stmt.setInt(9, player.getGameStats().getMAccuracy().getCurrent());
			stmt.setInt(10, player.getGameStats().getAttackSpeed().getCurrent());
			stmt.setInt(11, player.getGameStats().getMResist().getCurrent());
			stmt.setInt(12, player.getGameStats().getMainHandMAttack().getCurrent());
			stmt.setInt(13, player.getGameStats().getMainHandPCritical().getCurrent());
			stmt.setInt(14, player.getGameStats().getAttackRange().getCurrent());
			stmt.setInt(15, player.getGameStats().getMDef().getCurrent());
			stmt.setInt(16, player.getGameStats().getAgility().getCurrent());
			stmt.setInt(17, player.getGameStats().getKnowledge().getCurrent());
			stmt.setInt(18, player.getGameStats().getWill().getCurrent());
			stmt.setInt(19, player.getGameStats().getMBoost().getCurrent());
			stmt.setInt(20, player.getGameStats().getMBResist().getCurrent());
			stmt.setInt(21, player.getGameStats().getStrikeResist().getCurrent());
			stmt.setInt(22, player.getGameStats().getSpellResist().getCurrent());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not store PlayerGameStat data for player " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void deletePlayerGameStat(final int playerId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not delete PlayerGameStat data for player " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void updatePlayerGameStat(final Player player) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, player.getGameStats().getPDef().getCurrent());
			stmt.setInt(2, player.getGameStats().getBlock().getCurrent());
			stmt.setInt(3, player.getGameStats().getParry().getCurrent());
			stmt.setInt(4, player.getGameStats().getMCritical().getCurrent());
			stmt.setInt(5, player.getGameStats().getEvasion().getCurrent());
			stmt.setInt(6, player.getGameStats().getAccuracy().getCurrent());
			stmt.setInt(7, player.getGameStats().getPower().getCurrent());
			stmt.setInt(8, player.getGameStats().getMAccuracy().getCurrent());
			stmt.setInt(9, player.getGameStats().getAttackSpeed().getCurrent());
			stmt.setInt(10, player.getGameStats().getMResist().getCurrent());
			stmt.setInt(11, player.getGameStats().getMainHandMAttack().getCurrent());
			stmt.setInt(12, player.getGameStats().getMainHandPCritical().getCurrent());
			stmt.setInt(13, player.getGameStats().getAttackRange().getCurrent());
			stmt.setInt(14, player.getGameStats().getMDef().getCurrent());
			stmt.setInt(15, player.getGameStats().getAgility().getCurrent());
			stmt.setInt(16, player.getGameStats().getKnowledge().getCurrent());
			stmt.setInt(17, player.getGameStats().getWill().getCurrent());
			stmt.setInt(18, player.getGameStats().getMBoost().getCurrent());
			stmt.setInt(19, player.getGameStats().getMBResist().getCurrent());
			stmt.setInt(20, player.getGameStats().getStrikeResist().getCurrent());
			stmt.setInt(21, player.getGameStats().getSpellResist().getCurrent());
			stmt.setInt(22, player.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not update PlayerGameStat data for player " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.aionemu.commons.database.dao.DAO#supports(java.lang.String, int, int)
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
