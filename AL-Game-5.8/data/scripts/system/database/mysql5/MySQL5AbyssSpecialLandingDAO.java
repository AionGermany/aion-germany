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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.AbyssSpecialLandingDAO;
import com.aionemu.gameserver.model.landing_special.LandingSpecialLocation;
import com.aionemu.gameserver.model.landing_special.LandingSpecialStateType;

public class MySQL5AbyssSpecialLandingDAO extends AbyssSpecialLandingDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5AbyssSpecialLandingDAO.class);

	public static final String SELECT_QUERY = "SELECT * FROM `special_landing`";
	public static final String UPDATE_QUERY = "UPDATE `special_landing` SET `type` = ? WHERE `id` = ?";
	public static final String INSERT_QUERY = "INSERT INTO `special_landing` (`id`, `type`) VALUES(?, ?)";

	@Override
	public boolean loadLandingSpecialLocations(final Map<Integer, LandingSpecialLocation> locations) {
		boolean success = true;
		Connection con = null;
		List<Integer> loaded = new ArrayList<Integer>();
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				LandingSpecialLocation loc = locations.get(resultSet.getInt("id"));
				loc.setType(LandingSpecialStateType.valueOf(resultSet.getString("type")));
				loaded.add(loc.getId());
			}
			resultSet.close();
		}
		catch (Exception e) {
			log.warn("Error loading Siege informaiton from database: " + e.getMessage(), e);
			success = false;
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		for (Map.Entry<Integer, LandingSpecialLocation> entry : locations.entrySet()) {
			LandingSpecialLocation sLoc = entry.getValue();
			if (!loaded.contains(sLoc.getId())) {
				insertLandingLocation(sLoc);
			}
		}
		return success;
	}

	@Override
	public void store(LandingSpecialLocation location) {
		updateLandingSpecialLocation(location);
	}

	@Override
	public boolean updateLandingSpecialLocation(final LandingSpecialLocation locations) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, locations.getType().toString());
			stmt.setInt(2, locations.getId());
			stmt.execute();
		}
		catch (Exception e) {
			log.error("Error update special Landing Location: " + "id: " + locations.getId());
			return false;
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		return true;
	}

	private boolean insertLandingLocation(final LandingSpecialLocation locations) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, locations.getId());
			stmt.setString(2, LandingSpecialStateType.DESPAWN.toString());
			stmt.execute();
		}
		catch (Exception e) {
			log.error("Error insert special Landing Location: " + locations.getId(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		return true;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return com.aionemu.gameserver.dao.MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
