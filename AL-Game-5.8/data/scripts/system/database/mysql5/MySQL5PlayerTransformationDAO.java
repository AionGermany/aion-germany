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
import com.aionemu.gameserver.dao.PlayerTransformationDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public class MySQL5PlayerTransformationDAO extends PlayerTransformationDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerTransformationDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `player_transformation` (`player_id`, `panel_id`, `item_id`) VALUES (?,?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM `player_transformation` WHERE `player_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_transformation` WHERE `player_id`=?";

	@Override
	public void loadPlTransfo(final Player player) {
		DB.select(SELECT_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, player.getObjectId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next()) {
					int panelId = rset.getInt("panel_id");
					int itemId = rset.getInt("item_id");
					player.getTransformModel().setPanelId(panelId);
					player.getTransformModel().setItemId(itemId);
				}
			}
		});
	}

	@Override
	public boolean storePlTransfo(int playerId, int panelId, int itemId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, panelId);
			stmt.setInt(3, itemId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not store Player Tranformation." + playerId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean deletePlTransfo(int playerId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not delete f2p for player " + playerId + " from DB: " + e.getMessage(), e);
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
