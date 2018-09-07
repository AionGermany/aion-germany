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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerBindPointDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.BindPointPosition;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author evilset
 */
public class MySQL5PlayerBindPointDAO extends PlayerBindPointDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerBindPointDAO.class);
	public static final String INSERT_QUERY = "REPLACE INTO `player_bind_point` (`player_id`, `map_id`, `x`, `y`, `z`, `heading`) VALUES (?,?,?,?,?,?)";
	public static final String SELECT_QUERY = "SELECT `map_id`, `x`, `y`, `z`, `heading` FROM `player_bind_point` WHERE `player_id`=?";
	public static final String UPDATE_QUERY = "UPDATE player_bind_point set `map_id`=?, `x`=?, `y`=? , `z`=?, `heading`=? WHERE `player_id`=?";

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}

	@Override
	public void loadBindPoint(Player player) {

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			if (rset.next()) {
				int mapId = rset.getInt("map_id");
				float x = rset.getFloat("x");
				float y = rset.getFloat("y");
				float z = rset.getFloat("z");
				byte heading = rset.getByte("heading");
				BindPointPosition bind = new BindPointPosition(mapId, x, y, z, heading);
				bind.setPersistentState(PersistentState.UPDATED);
				player.setBindPoint(bind);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore BindPointPosition data for playerObjId: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean insertBindPoint(Player player) {

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			BindPointPosition bpp = player.getBindPoint();
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, bpp.getMapId());
			stmt.setFloat(3, bpp.getX());
			stmt.setFloat(4, bpp.getY());
			stmt.setFloat(5, bpp.getZ());
			stmt.setByte(6, bpp.getHeading());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not store BindPointPosition data for player " + player.getObjectId() + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean updateBindPoint(Player player) {

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			BindPointPosition bpp = player.getBindPoint();
			stmt.setInt(1, bpp.getMapId());
			stmt.setFloat(2, bpp.getX());
			stmt.setFloat(3, bpp.getY());
			stmt.setFloat(4, bpp.getZ());
			stmt.setByte(5, bpp.getHeading());
			stmt.setFloat(6, player.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not update BindPointPosition data for player " + player.getObjectId() + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean store(final Player player) {
		boolean insert = false;
		BindPointPosition bind = player.getBindPoint();

		switch (bind.getPersistentState()) {
			case NEW:
				insert = insertBindPoint(player);
				break;
			case UPDATE_REQUIRED:
				insert = updateBindPoint(player);
				break;
			default:
				break;
		}
		bind.setPersistentState(PersistentState.UPDATED);
		return insert;
	}
}
