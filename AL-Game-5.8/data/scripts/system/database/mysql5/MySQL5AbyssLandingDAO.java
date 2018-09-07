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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.AbyssLandingDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.landing.LandingLocation;

public class MySQL5AbyssLandingDAO extends AbyssLandingDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5AbyssLandingDAO.class);

	public static final String SELECT_QUERY = "SELECT * FROM `abyss_landing`";
	public static final String UPDATE_QUERY = "UPDATE `abyss_landing` SET `level` = ?, `siege` = ?, `commander` = ?, `artefact` = ?, `base` = ?, `monuments` = ?, `quest` = ?, `facility` = ?, `points` = ? WHERE `id` = ?";
	public static final String INSERT_QUERY = "INSERT INTO `abyss_landing` (`id`, `level`, `siege`, `commander`, `artefact`, `base`, `monuments`, `quest`, `facility`, `level_up_date`, `race`, `points`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	@Override
	public void store(LandingLocation location) {
		updateLandingLocation(location);
	}

	@Override
	public boolean loadLandingLocations(final Map<Integer, LandingLocation> locations) {
		boolean success = true;
		Connection con = null;
		List<Integer> loaded = new ArrayList<Integer>();
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				LandingLocation loc = locations.get(resultSet.getInt("id"));
				loc.setLevel(resultSet.getInt("level"));
				loc.setPoints(resultSet.getInt("points"));
				loc.setArtifactPoints(resultSet.getInt("artefact"));
				loc.setBasePoints(resultSet.getInt("base"));
				loc.setCommanderPoints(resultSet.getInt("commander"));
				loc.setQuestPoints(resultSet.getInt("quest"));
				loc.setFacilityPoints(resultSet.getInt("facility"));
				loc.setSiegePoints(resultSet.getInt("siege"));
				loc.setMonumentsPoints(resultSet.getInt("monuments"));
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
		for (Map.Entry<Integer, LandingLocation> entry : locations.entrySet()) {
			LandingLocation sLoc = entry.getValue();
			if (!loaded.contains(sLoc.getId())) {
				insertLandingLocation(sLoc);
			}
		}
		return success;
	}

	@Override
	public boolean updateLandingLocation(final LandingLocation locations) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, locations.getLevel());
			stmt.setInt(2, locations.getSiegePoints());
			stmt.setInt(3, locations.getCommanderPoints());
			stmt.setInt(4, locations.getArtifactPoints());
			stmt.setInt(5, locations.getBasePoints());
			stmt.setInt(6, locations.getMonumentsPoints());
			stmt.setInt(7, locations.getQuestPoints());
			stmt.setInt(8, locations.getFacilityPoints());
			stmt.setInt(9, locations.getPoints());
			stmt.setInt(10, locations.getId());
			stmt.execute();
		}
		catch (Exception e) {
			log.error("Error update Abyss Landing Location: " + "id: " + locations.getId());
			return false;
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		return true;
	}

	private boolean insertLandingLocation(final LandingLocation locations) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, locations.getId());
			stmt.setInt(2, locations.getLevel());
			stmt.setInt(3, locations.getSiegePoints());
			stmt.setInt(4, locations.getCommanderPoints());
			stmt.setInt(5, locations.getArtifactPoints());
			stmt.setInt(6, locations.getBasePoints());
			stmt.setInt(7, locations.getMonumentsPoints());
			stmt.setInt(8, locations.getQuestPoints());
			stmt.setInt(9, locations.getFacilityPoints());
			stmt.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
			stmt.setString(11, locations.getTemplate().getRace().toString());
			stmt.setInt(12, locations.getPoints());
			stmt.execute();
		}
		catch (Exception e) {
			log.error("Error insert Abyss Landing Location: " + locations.getId(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		return true;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
