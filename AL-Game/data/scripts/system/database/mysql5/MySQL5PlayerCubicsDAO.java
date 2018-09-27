package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerCubicsDAO;
import com.aionemu.gameserver.model.cubics.PlayerMCEntry;
import com.aionemu.gameserver.model.cubics.PlayerMCList;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Phantom_KNA
 */
public class MySQL5PlayerCubicsDAO extends PlayerCubicsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerCubicsDAO.class);

	public static final String INSERT_QUERY = "INSERT INTO `player_cubic` (`player_id`, `cubic_id`, `rank`, `level`, `stat_value`, `category`) VALUES (?,?,?,?,?,?)";
	public static final String INSERT_OR_UPDATE = "INSERT INTO `player_cubic` (`player_id`, `cubic_id`, `rank`, `level`, `stat_value`,`category`) VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `cubic_id` = VALUES(`cubic_id`), `rank` = VALUES(`rank`), `level` = VALUES(`level`), `stat_value` = VALUES(`stat_value`), `category` = VALUES(`category`)";
	public static final String SELECT_QUERY = "SELECT `cubic_id`,`rank`,`level`,`stat_value`,`category` FROM `player_cubic` WHERE `player_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_cubic` WHERE `player_id`=? AND `cubic_id`=?";

	@Override
	public PlayerMCList load(Player player) {
		List<PlayerMCEntry> mc = new ArrayList<PlayerMCEntry>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int cubic_id = rset.getInt("cubic_id");
				int rank = rset.getInt("rank");
				int level = rset.getInt("level");
				int stat_value = rset.getInt("stat_value");
				int category = rset.getInt("category");
				mc.add(new PlayerMCEntry(cubic_id, rank, level, stat_value, category, PersistentState.UPDATED));
			}
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			log.error("Could not restore PlayerCubic for playerObjId: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		} finally {
			DatabaseFactory.close(con);
		}
		return new PlayerMCList(mc);
	}

	@Override
	public boolean store(int objectId, int cubic_id, int rank, int level, int stat_value, int category) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_OR_UPDATE);
			stmt.setInt(1, objectId);
			stmt.setInt(2, cubic_id);
			stmt.setInt(3, rank);
			stmt.setInt(4, level);
			stmt.setInt(5, stat_value);
			stmt.setInt(6, category);
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			log.error("Could not store PlayerCubic for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean delete(int playerObjId, int cubic_id) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerObjId);
			stmt.setInt(2, cubic_id);
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			log.error("Could not delete PlayerCubic for player " + playerObjId + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public int getRankById(final int playerObjId, final int cubic_id) {
		Connection con = null;
		int rank = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `rank` FROM `player_cubic` WHERE `player_id`=? AND `cubic_id`=?");
			s.setInt(1, playerObjId);
			s.setInt(2, cubic_id);
			ResultSet rs = s.executeQuery();
			rs.next();
			rank = rs.getInt("rank");
			rs.close();
			s.close();
		} catch (SQLException e) {
			return 0;
		} finally {
			DatabaseFactory.close(con);
		}
		return rank;
	}

	@Override
	public int getLevelById(final int playerObjId, final int cubic_id) {
		Connection con = null;
		int level = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `level` FROM `player_cubic` WHERE `player_id`=? AND `cubic_id`=?");
			s.setInt(1, playerObjId);
			s.setInt(2, cubic_id);
			ResultSet rs = s.executeQuery();
			rs.next();
			level = rs.getInt("level");
			rs.close();
			s.close();
		} catch (SQLException e) {
			return 0;
		} finally {
			DatabaseFactory.close(con);
		}
		return level;
	}

	@Override
	public int getStatValueById(int playerObjId, int cubic_id) {
		Connection con = null;
		int stat_value = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `stat_value` FROM `player_cubic` WHERE `player_id`=? AND `cubic_id`=?");
			s.setInt(1, playerObjId);
			s.setInt(2, cubic_id);
			ResultSet rs = s.executeQuery();
			rs.next();
			stat_value = rs.getInt("stat_value");
			rs.close();
			s.close();
		} catch (SQLException e) {
			return 0;
		} finally {
			DatabaseFactory.close(con);
		}
		return stat_value;
	}

	/**
	 *
	 * @param playerObjId
	 * @param category
	 * @return
	 */
	@Override
	public int getCategoryById(int playerObjId, int category) {
		Connection con = null;
		int categorys = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT count(category) FROM `player_cubic` WHERE `player_id`=? AND `category`=?");
			s.setInt(1, playerObjId);
			s.setInt(2, category);
			ResultSet rs = s.executeQuery();
			rs.next();
			categorys = rs.getInt("count(category)");
			rs.close();
			s.close();
		} catch (SQLException e) {
			return 0;
		} finally {
			DatabaseFactory.close(con);
		}
		return categorys;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
