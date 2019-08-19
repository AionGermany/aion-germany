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
import com.aionemu.commons.database.IUStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerLunaShopDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerLunaShop;

public class MySQL5PlayerLunaShopDAO extends PlayerLunaShopDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerLunaShopDAO.class);

	public static final String ADD_QUERY = "INSERT INTO `player_luna_shop` (`player_id`, `free_under`, `free_munition`, `free_chest`) VALUES (?,?,?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM `player_luna_shop` WHERE `player_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_luna_shop`";
	public static final String UPDATE_QUERY = "UPDATE player_luna_shop set `free_under`=?, `free_munition`=?, `free_chest`=? WHERE `player_id`=?";

	@Override
	public void load(Player player) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			if (rset.next()) {
				boolean under = rset.getBoolean("free_under");
				boolean factory = rset.getBoolean("free_munition");
				boolean chest = rset.getBoolean("free_chest");
				PlayerLunaShop pls = new PlayerLunaShop(under, factory, chest);
				pls.setPersistentState(PersistentState.UPDATED);
				player.setPlayerLunaShop(pls);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore PlayerLunaShop data for playerObjId: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean add(final int playerId, final boolean freeUnderpath, final boolean freeFactory, final boolean freeChest) {
		return DB.insertUpdate(ADD_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
				ps.setInt(1, playerId);
				ps.setBoolean(2, freeUnderpath);
				ps.setBoolean(3, freeFactory);
				ps.setBoolean(4, freeChest);
				ps.execute();
				ps.close();
			}
		});
	}

	@Override
	public boolean delete() {
		return DB.insertUpdate(DELETE_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
				ps.execute();
				ps.close();
			}
		});
	}

	@Override
	public boolean store(Player player) {
		Connection con = null;
		boolean insert = false;
		try {
			con = DatabaseFactory.getConnection();
			con.setAutoCommit(false);
			PlayerLunaShop bind = player.getPlayerLunaShop();
			switch (bind.getPersistentState()) {
				case UPDATE_REQUIRED:
				case NEW:
					insert = updateLunaShop(con, player);
					log.info("LunaShop DB updated.");
					break;
				default:
					break;
			}
			bind.setPersistentState(PersistentState.UPDATED);
		}
		catch (SQLException e) {
			log.error("Can't open connection to save player updateLunaShop: " + player.getObjectId());
		}
		finally {
			DatabaseFactory.close(con);
		}
		return insert;
	}

	public boolean updateLunaShop(Connection con, Player player) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(UPDATE_QUERY);
			PlayerLunaShop lr = player.getPlayerLunaShop();
			stmt.setBoolean(1, lr.isFreeUnderpath());
			stmt.setBoolean(2, lr.isFreeFactory());
			stmt.setBoolean(3, lr.isFreeChest());
			stmt.setInt(4, player.getObjectId());
			stmt.addBatch();
			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e) {
			log.error("Could not update PlayerLunaShop data for player " + player.getObjectId() + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt);
		}
		return true;
	}

	@Override
	public boolean setLunaShopByObjId(int obj, final boolean freeUnderpath, final boolean freeFactory, final boolean freeChest) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setBoolean(1, freeUnderpath);
			stmt.setBoolean(2, freeFactory);
			stmt.setBoolean(3, freeChest);
			stmt.setInt(4, obj);
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
