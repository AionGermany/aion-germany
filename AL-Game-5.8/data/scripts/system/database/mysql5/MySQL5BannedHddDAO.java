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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.gameserver.dao.BannedHddDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.network.BannedHDDEntry;

import javolution.util.FastMap;

/**
 * @author Alex
 */
public class MySQL5BannedHddDAO extends BannedHddDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5BannedHddDAO.class);

	@Override
	public Map<String, BannedHDDEntry> load() {
		Map<String, BannedHDDEntry> map = new FastMap<String, BannedHDDEntry>();
		PreparedStatement ps = DB.prepareStatement("SELECT `hdd_serial`,`time`,`details` FROM `banned_hdd`");
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String address = rs.getString("hdd_serial");
				map.put(address, new BannedHDDEntry(address, rs.getTimestamp("time"), rs.getString("details")));
			}
		}
		catch (SQLException e) {
			log.error("Error loading last saved server time", e);
		}
		finally {
			DB.close(ps);
		}
		return map;
	}

	@Override
	public boolean update(BannedHDDEntry entry) {
		boolean success = false;
		PreparedStatement ps = DB.prepareStatement("REPLACE INTO `banned_hdd` (`hdd_serial`,`time`,`details`) VALUES (?,?,?)");
		try {
			ps.setString(1, entry.getHDDSerial());
			ps.setTimestamp(2, entry.getTime());
			ps.setString(3, entry.getDetails());
			success = ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			log.error("Error storing BannedHDDEntry " + entry.getHDDSerial(), e);
		}
		finally {
			DB.close(ps);
		}

		return success;
	}

	@Override
	public boolean remove(String hdd_serial) {
		boolean success = false;
		PreparedStatement ps = DB.prepareStatement("DELETE FROM `banned_hdd` WHERE hdd_serial=?");
		try {
			ps.setString(1, hdd_serial);
			success = ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			log.error("Error removing BannedHDDEntry " + hdd_serial, e);
		}
		finally {
			DB.close(ps);
		}

		return success;
	}

	@Override
	public void cleanExpiredBans() {
		DB.insertUpdate("DELETE FROM `banned_hdd` WHERE time < current_date");
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
