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
import com.aionemu.gameserver.dao.PlayerWardrobeDAO;
import com.aionemu.gameserver.model.dorinerk_wardrobe.PlayerWardrobeEntry;
import com.aionemu.gameserver.model.dorinerk_wardrobe.PlayerWardrobeList;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public class MySQL5PlayerWardrobeDAO extends PlayerWardrobeDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerWardrobeDAO.class);

	public static final String INSERT_OR_UPDATE = "INSERT INTO `player_wardrobe` (`player_id`, `item_id`, `slot`, `reskin_count`) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE `item_id` = VALUES(`item_id`), `slot` = VALUES(`slot`)";
	public static final String SELECT_QUERY = "SELECT `item_id`,`slot`,`reskin_count` FROM `player_wardrobe` WHERE `player_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_wardrobe` WHERE `player_id`=? AND `item_id`=?";

	@Override
	public PlayerWardrobeList load(Player player) {
		List<PlayerWardrobeEntry> w = new ArrayList<PlayerWardrobeEntry>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int itemId = rset.getInt("item_id");
				int slot = rset.getInt("slot");
				int reskin = rset.getInt("slot");
				w.add(new PlayerWardrobeEntry(itemId, slot, reskin, PersistentState.UPDATED));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore Wardrobe time for playerObjId: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return new PlayerWardrobeList(w);
	}

	@Override
	public boolean store(int objectId, int itemId, int slot, int reskin) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_OR_UPDATE);
			stmt.setInt(1, objectId);
			stmt.setInt(2, itemId);
			stmt.setInt(3, slot);
			stmt.setInt(4, reskin);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not store Wardrobe for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean delete(int objectId, int itemId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, itemId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not delete Wardrobe for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public int getItemSize(int playerObjId) {
		Connection con = null;
		int size = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS `size` FROM `player_wardrobe` WHERE `player_id`=?");
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
	public int getWardrobeItemBySlot(final int obj, int slot) {
		Connection con = null;
		int wardrobeItemId = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `item_id` FROM `player_wardrobe` WHERE `player_id`=? AND `slot`=?");
			s.setInt(1, obj);
			s.setInt(2, slot);
			ResultSet rs = s.executeQuery();
			rs.next();
			wardrobeItemId = rs.getInt("item_id");
			rs.close();
			s.close();
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return wardrobeItemId;
	}

	@Override
	public int getReskinCountBySlot(final int obj, int slot) {
		Connection con = null;
		int reskinCount = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `reskin_count` FROM `player_wardrobe` WHERE `player_id`=? AND `slot`=?");
			s.setInt(1, obj);
			s.setInt(2, slot);
			ResultSet rs = s.executeQuery();
			rs.next();
			reskinCount = rs.getInt("reskin_count");
			rs.close();
			s.close();
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return reskinCount;
	}

	@Override
	public boolean setReskinCountBySlot(int obj, int slot, int reskin_count) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE player_wardrobe set `reskin_count`=? WHERE `player_id`=? AND `slot`=?");
			stmt.setInt(1, reskin_count);
			stmt.setInt(2, obj);
			stmt.setInt(3, slot);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
