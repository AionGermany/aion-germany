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

import javolution.util.FastList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.loginserver.dao.PremiumDAO;

/**
 * @author KID
 */
public class MySQL5PremiumDAO extends PremiumDAO {
	private final Logger log = LoggerFactory.getLogger("PREMIUM_CTRL");

	@Override
	public long getPoints(int accountId) {
		long points = 0;
		PreparedStatement st = DB.prepareStatement("SELECT toll FROM account_data WHERE id=?");
		try {
			st.setInt(1, accountId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				points = rs.getLong("toll");
			}
		}
		catch (Exception e) {
			log.error("getPoints [select points] "+accountId, e);
		}
		finally {
			DB.close(st);
		}
		
		FastList<Integer> rewarded = FastList.newInstance();
		st = DB.prepareStatement("SELECT uniqId,points FROM account_rewards WHERE accountId=? AND rewarded=0");
		try {
			st.setInt(1, accountId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				int uniqId = rs.getInt("uniqId");
				points += rs.getLong("points");
				log.info("Account "+accountId+" has received uniqId #"+uniqId);
				rewarded.add(uniqId);
			}
		}
		catch (Exception e) {
			log.error("getPoints [get rewards] "+accountId, e);
		}
		finally {
			DB.close(st);
		}
		
		if(rewarded.size() > 0) {
			Connection con = null;
			try {
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt;
				for(int uniqid : rewarded) {
					stmt = con.prepareStatement("UPDATE account_rewards SET rewarded=1,received=NOW() WHERE uniqId=?");
					stmt.setInt(1, uniqid);
					stmt.execute();
					stmt.close();
				}
			}
			catch (Exception e) {
				log.error("getPoints [update uniq] "+accountId, e);
			}
			finally {
				DatabaseFactory.close(con);
			}
		}
		
		return points;
	}

	@Override
	public boolean updatePoints(int accountId, long points, long required) {
		Connection con = null;
		boolean s = true;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE account_data SET toll=? WHERE id=?");
			stmt.setLong(1, points - required);
			stmt.setInt(2, accountId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("updatePoints "+accountId, e);
			s = false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		
		return s;
	}
	
	@Override
	public long getLuna(int accountId) {
		long luna = 0;
		PreparedStatement st = DB.prepareStatement("SELECT luna FROM account_data WHERE id=?");
		try {
			st.setInt(1, accountId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				luna = rs.getLong("luna");
			}
		}
		catch (Exception e) {
			log.error("getLuna [select Luna] "+accountId, e);
		}
		finally {
			DB.close(st);
		}
		return luna;
	}
		
	
	@Override
	public boolean updateLuna(int accountId, long luna) {
		Connection con = null;
		boolean s = true;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE account_data SET luna = ? WHERE id = ?");
			stmt.setLong(1, luna);
			stmt.setInt(2, accountId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("updateLuna "+accountId, e);
			s = false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		
		return s;
	}

	@Override
	public boolean supports(String database, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(database, majorVersion, minorVersion);
	}
}
