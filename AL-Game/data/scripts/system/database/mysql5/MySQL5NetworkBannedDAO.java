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
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.NetworkBannedDAO;
import com.aionemu.gameserver.network.NetworkBanEntry;

import javolution.util.FastMap;

/**
 * @author Alex
 */
public class MySQL5NetworkBannedDAO extends NetworkBannedDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5NetworkBannedDAO.class);

	@Override
	public Map<String, NetworkBanEntry> load() {
		Map<String, NetworkBanEntry> map = new FastMap<String, NetworkBanEntry>();
		PreparedStatement ps = DB.prepareStatement("SELECT `ip`,`time`,`details` FROM `network_ban`");
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String address = rs.getString("ip");
				map.put(address, new NetworkBanEntry(address, rs.getTimestamp("time"), rs.getString("details")));
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
	public boolean update(NetworkBanEntry entry) {
		boolean success = false;
		PreparedStatement ps = DB.prepareStatement("REPLACE INTO `network_ban` (`ip`,`time`,`details`) VALUES (?,?,?)");
		try {
			ps.setString(1, entry.getNetworkIP());
			ps.setTimestamp(2, entry.getTime());
			ps.setString(3, entry.getDetails());
			success = ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			log.error("Error storing NetworkBanEntry " + entry.getNetworkIP(), e);
		}
		finally {
			DB.close(ps);
		}

		return success;
	}

	@Override
	public boolean remove(String ip) {
		boolean success = false;
		PreparedStatement ps = DB.prepareStatement("DELETE FROM `network_ban` WHERE ip=?");
		try {
			ps.setString(1, ip);
			success = ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			log.error("Error removing BannedHDDEntry " + ip, e);
		}
		finally {
			DB.close(ps);
		}

		return success;
	}

	@Override
	public void cleanExpiredBans() {
		DB.insertUpdate("DELETE FROM `network_ban` WHERE time < current_date");
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
