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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerLumielDAO;
import com.aionemu.gameserver.model.gameobjects.player.LumielTransform;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import javolution.util.FastMap;

public class MySQL5PlayerLumielDAO extends PlayerLumielDAO {

	private Logger log = LoggerFactory.getLogger(MySQL5PlayerLumielDAO.class);

	public static final String INSERT_ACHIEVEMENT = "INSERT INTO lumiel_transform (player_id, lumiel_id, points) VALUES (?, ?, ?)";
	public static final String LOAD_QUERY = "SELECT * FROM `lumiel_transform` WHERE `player_id`=?";
	public static final String UPDATE_QUERY = "UPDATE lumiel_transform set points=? WHERE `player_id`=? AND `lumiel_id`=?";

	@Override
	public Map<Integer, LumielTransform> loadPlayerLumiel(final Player player) {
		final Map<Integer, LumielTransform> playerLumiels = new FastMap<Integer, LumielTransform>();
		DB.select(LOAD_QUERY, new ParamReadStH() {
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, player.getObjectId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next()) {
					LumielTransform lumiel = new LumielTransform(rset.getInt("lumiel_id"), rset.getInt("points"));
					playerLumiels.put(lumiel.getId(), lumiel);
				}
			}
		});
		return playerLumiels;
	}

	@Override
	public boolean addPlayerLumiel(Player player, LumielTransform lumielTransform) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_ACHIEVEMENT);
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, lumielTransform.getId());
			stmt.setLong(3, lumielTransform.getPoints());
			stmt.execute();
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.error("addLumiel error", e);

			return false;
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean updateLumielTransform(Player player, LumielTransform lumielTransform) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setLong(1, lumielTransform.getPoints());
			stmt.setInt(2, player.getObjectId());
			stmt.setInt(3, lumielTransform.getId());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not update PlayerLumiel data for Player " + player.getName() + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    } 
}
