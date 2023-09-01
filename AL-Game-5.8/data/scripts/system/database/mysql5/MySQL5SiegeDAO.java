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
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeRace;

/**
 * @author Sarynth
 */
public class MySQL5SiegeDAO extends SiegeDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5SiegeDAO.class);
	public static final String SELECT_QUERY = "SELECT `id`, `race`, `legion_id`, `occupy_count` FROM `siege_locations`";
	public static final String INSERT_QUERY = "INSERT INTO `siege_locations` (`id`, `race`, `legion_id`, `occupy_count`) VALUES(?, ?, ?, ?)";
	public static final String UPDATE_QUERY = "UPDATE `siege_locations` SET  `race` = ?, `legion_id` = ?, `occupy_count` = ? WHERE `id` = ?";

	@Override
	public boolean loadSiegeLocations(final Map<Integer, SiegeLocation> locations) {
		boolean success = true;
		Connection con = null;
		List<Integer> loaded = new ArrayList<Integer>();

		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				SiegeLocation loc = locations.get(resultSet.getInt("id"));
				loc.setRace(SiegeRace.valueOf(resultSet.getString("race")));
				loc.setLegionId(resultSet.getInt("legion_id"));
				loc.setOccupyCount(resultSet.getInt("occupy_count"));
				loaded.add(loc.getLocationId());
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

		for (Map.Entry<Integer, SiegeLocation> entry : locations.entrySet()) {
			SiegeLocation sLoc = entry.getValue();
			if (!loaded.contains(sLoc.getLocationId())) {
				insertSiegeLocation(sLoc);
			}
		}

		return success;
	}

	@Override
	public boolean updateSiegeLocation(final SiegeLocation siegeLocation) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, siegeLocation.getRace().toString());
			stmt.setInt(2, siegeLocation.getLegionId());
			stmt.setInt(3, siegeLocation.getOccupyCount());
			stmt.setInt(4, siegeLocation.getLocationId());
			stmt.execute();
		}
		catch (Exception e) {
			log.error("Error update Siege Location: " + siegeLocation.getLocationId() + " to race: " + siegeLocation.getRace().toString(), e + "Occupy_Count: " + siegeLocation.getOccupyCount());
			return false;
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		return true;
	}

	/**
	 * @param siegeLocation
	 * @return success
	 */
	private boolean insertSiegeLocation(final SiegeLocation siegeLocation) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, siegeLocation.getLocationId());
			stmt.setString(2, siegeLocation.getRace().toString());
			stmt.setInt(3, siegeLocation.getLegionId());
			stmt.setInt(4, siegeLocation.getOccupyCount());
			stmt.execute();
		}
		catch (Exception e) {
			log.error("Error insert Siege Location: " + siegeLocation.getLocationId(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt, con);

		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
