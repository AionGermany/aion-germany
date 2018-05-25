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
import com.aionemu.gameserver.dao.Free2PlayDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.f2p.F2p;
import com.aionemu.gameserver.model.gameobjects.player.f2p.F2pAccount;

/**
 * edited by teenwolf
 */
public class MySQL5Free2PlayDAO extends Free2PlayDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5Free2PlayDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `f2paccount` (`account_id`, `time`) VALUES (?,?)";
	public static final String SELECT_QUERY = "SELECT `time` FROM `f2paccount` WHERE `account_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `f2paccount` WHERE `account_id`=?";
	public static final String UPDATE_QUERY = "UPDATE `f2paccount` set `time`=? where `account_id`=?";

	@Override
	public void loadF2pInfo(Player player, int accountId) {
		Connection con = null;
		F2p f2p = new F2p(player);
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, accountId);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int time = rset.getInt("time");
				f2p.add(new F2pAccount(time), false);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore f2p time for accountId: " + accountId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		player.setF2p(f2p);
	}

	@Override
	public boolean storeF2p(int accountId, int time) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, accountId);
			stmt.setInt(2, time);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not store f2p for accountId " + accountId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean updateF2p(int accountId, int time) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, time);
			stmt.setInt(2, accountId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not update f2p for accountId " + accountId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean deleteF2p(int accountId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, accountId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not delete f2p for accountId " + accountId + " from DB: " + e.getMessage(), e);
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
