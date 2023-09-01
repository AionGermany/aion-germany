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

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.HouseObjectCooldownsDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import javolution.util.FastMap;

/**
 * @author Rolandas
 */
public class MySQL5HouseObjectCooldownsDAO extends HouseObjectCooldownsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5CraftCooldownsDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `house_object_cooldowns` (`player_id`, `object_id`, `reuse_time`) VALUES (?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `house_object_cooldowns` WHERE `player_id`=?";
	public static final String SELECT_QUERY = "SELECT `object_id`, `reuse_time` FROM `house_object_cooldowns` WHERE `player_id`=?";

	@Override
	public void loadHouseObjectCooldowns(final Player player) {
		Connection con = null;
		FastMap<Integer, Long> houseObjectCoolDowns = new FastMap<Integer, Long>();
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);

			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();

			while (rset.next()) {
				int objectId = rset.getInt("object_id");
				long reuseTime = rset.getLong("reuse_time");
				int delay = (int) ((reuseTime - System.currentTimeMillis()) / 1000);

				if (delay > 0) {
					houseObjectCoolDowns.put(objectId, reuseTime);
				}
			}
			player.getHouseObjectCooldownList().setHouseObjectCooldowns(houseObjectCoolDowns);
			rset.close();
			stmt.close();
		}
		catch (SQLException e) {
			log.error("LoadHouseObjectCooldowns", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void storeHouseObjectCooldowns(final Player player) {
		deleteHouseObjectCoolDowns(player);
		Map<Integer, Long> houseObjectCoolDowns = player.getHouseObjectCooldownList().getHouseObjectCooldowns();

		if (houseObjectCoolDowns == null) {
			return;
		}

		for (Map.Entry<Integer, Long> entry : houseObjectCoolDowns.entrySet()) {
			final int templateId = entry.getKey();
			final long reuseTime = entry.getValue();

			if (reuseTime < System.currentTimeMillis()) {
				continue;
			}

			Connection con = null;

			try {
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);

				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, templateId);
				stmt.setLong(3, reuseTime);
				stmt.execute();
			}
			catch (SQLException e) {
				log.error("storeHouseObjectCoolDowns", e);
			}
			finally {
				DatabaseFactory.close(con);
			}
		}
	}

	private void deleteHouseObjectCoolDowns(final Player player) {
		Connection con = null;

		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);

			stmt.setInt(1, player.getObjectId());
			stmt.execute();
		}
		catch (SQLException e) {
			log.error("deleteHouseObjectCoolDowns", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
