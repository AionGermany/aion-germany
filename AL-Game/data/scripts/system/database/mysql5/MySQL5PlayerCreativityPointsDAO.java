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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerCreativityPointsDAO;
import com.aionemu.gameserver.model.cp.PlayerCPEntry;
import com.aionemu.gameserver.model.cp.PlayerCPList;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public class MySQL5PlayerCreativityPointsDAO extends PlayerCreativityPointsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerCreativityPointsDAO.class);

	public static final String INSERT_QUERY = "INSERT INTO `player_cp` (`player_id`, `slot`, `point`) VALUES (?,?,?)";
	public static final String INSERT_OR_UPDATE = "INSERT INTO `player_cp` (`player_id`, `slot`, `point`) VALUES(?,?,?) ON DUPLICATE KEY UPDATE `slot` = VALUES(`slot`), `point` = VALUES(`point`)";
	public static final String UPDATE_QUERY = "UPDATE `player_cp` set point=? where player_id=? AND slot=?";
	public static final String SELECT_QUERY = "SELECT `slot`,`point` FROM `player_cp` WHERE `player_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_cp` WHERE `player_id`=? AND `slot`=?";

	@Override
	public PlayerCPList loadCP(Player player) {
		List<PlayerCPEntry> cp = new ArrayList<PlayerCPEntry>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int slot = rset.getInt("slot");
				int point = rset.getInt("point");
				cp.add(new PlayerCPEntry(slot, point, PersistentState.UPDATED));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore CP time for playerObjId: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return new PlayerCPList(cp);
	}

	@Override
	public boolean storeCP(int objectId, int slot, int point) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_OR_UPDATE);
			stmt.setInt(1, objectId);
			stmt.setInt(2, slot);
			stmt.setInt(3, point);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not store CP for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean deleteCP(int objectId, int slot) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, slot);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not delete CP for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public int getSlotSize(int playerObjId) {
		Connection con = null;
		int size = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS `size` FROM `player_cp` WHERE `player_id`=?");
			stmt.setInt(1, playerObjId);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			size = rs.getInt("size");
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return size;
	}

	@Override
	public int getCPSlotObjId(final int obj) {
		Connection con = null;
		int cpSlot = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `slot` FROM `player_cp` WHERE `player_id`=?");
			s.setInt(1, obj);
			ResultSet rs = s.executeQuery();
			rs.next();
			cpSlot = rs.getInt("slot");
			rs.close();
			s.close();
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return cpSlot;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
