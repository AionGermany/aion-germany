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
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.HouseBidsDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.house.PlayerHouseBid;

/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author Rolandas
 */
public class MySQL5HouseBidsDAO extends HouseBidsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5HouseBidsDAO.class);
	public static final String LOAD_QUERY = "SELECT * FROM `house_bids`";
	public static final String INSERT_QUERY = "INSERT INTO `house_bids` (`player_id`,`house_id`, `bid`, `bid_time`) VALUES (?, ?, ?, ?)";
	public static final String DELETE_QUERY = "DELETE FROM `house_bids` WHERE `house_id` = ?";
	public static final String UPDATE_QUERY = "UPDATE `house_bids` SET bid = ?, bid_time = ? WHERE player_id = ? AND house_id = ?";

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}

	@Override
	public Set<PlayerHouseBid> loadBids() {
		Connection con = null;
		Set<PlayerHouseBid> results = new HashSet<PlayerHouseBid>();
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_QUERY);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int playerId = rset.getInt("player_id");
				int houseId = rset.getInt("house_id");
				long bidOffer = rset.getLong("bid");
				Timestamp time = rset.getTimestamp("bid_time");
				PlayerHouseBid bid = new PlayerHouseBid(playerId, houseId, bidOffer, time);
				results.add(bid);
			}
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot read house bids", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return results;
	}

	@Override
	public boolean addBid(int playerId, int houseId, long bidOffer, Timestamp time) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, houseId);
			stmt.setLong(3, bidOffer);
			stmt.setTimestamp(4, time);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot insert house bid", e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public void changeBid(int playerId, int houseId, long newBidOffer, Timestamp time) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setLong(1, newBidOffer);
			stmt.setTimestamp(2, time);
			stmt.setInt(3, playerId);
			stmt.setInt(4, houseId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot update house bid", e);
		}
		finally {
			DatabaseFactory.close(con);
		}

	}

	@Override
	public void deleteHouseBids(int houseId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, houseId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot delete house bids", e);
		}
		finally {
			DatabaseFactory.close(con);
		}

	}
}
