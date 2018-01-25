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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerPassportsDAO;

/**
 * @author Alcapwnd
 * @reworked Lyras
 * @rework FrozenKiller
 */
public class MySQL5PlayerPassportsDAO extends PlayerPassportsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerPassportsDAO.class);

	@Override
	public void insertPassport(final int accountId, final int passportId, final int stamps, final Timestamp last_stamp) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO `player_passports` (`account_id`, `passport_id`, `stamps`, `last_stamp`) VALUES (?,?,?,?)");
			stmt.setInt(1, accountId);
			stmt.setInt(2, passportId);
			stmt.setInt(3, stamps);
			stmt.setTimestamp(4, last_stamp);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Can't insert into passports: " + e.getMessage());
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void updatePassport(final int accountId, final int passportId, final int stamps, final boolean rewarded, final Timestamp last_stamp) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE player_passports SET stamps = ?, rewarded = ?, last_stamp = ? WHERE account_id = ? AND passport_id = ?");

			stmt.setInt(1, stamps);
			stmt.setInt(2, rewarded ? 1 : 0);
			stmt.setTimestamp(3, last_stamp);
			stmt.setInt(4, accountId);
			stmt.setInt(5, passportId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error updating passports ", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public int getStamps(final int accountId, final int passportId) {
		PreparedStatement s = DB.prepareStatement("SELECT * FROM player_passports WHERE account_id = ? AND passport_id = ?");
		try {
			s.setInt(1, accountId);
			s.setInt(2, passportId);
			ResultSet rs = s.executeQuery();
			rs.next();
			return rs.getInt("stamps");
		}
		catch (SQLException e) {
			return 0;
		}
		finally {
			DB.close(s);
		}
	}

	@Override
	public Timestamp getLastStamp(final int accountId, final int passportId) {
		PreparedStatement s = DB.prepareStatement("SELECT last_stamp FROM player_passports WHERE account_id = ? AND passport_id = ?");
		try {
			s.setInt(1, accountId);
			s.setInt(2, passportId);
			ResultSet rs = s.executeQuery();
			rs.next();
			return rs.getTimestamp("last_stamp");
		}
		catch (SQLException e) {
			log.error("Can't get last Passport Stamp!" + e);
			return new Timestamp(System.currentTimeMillis());
		}
		finally {
			DB.close(s);
		}
	}

	@Override
	public List<Integer> getPassports(final int accountId) {
		final List<Integer> ids = new ArrayList<Integer>();

		DB.select("SELECT passport_id FROM player_passports WHERE account_id = ?", new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, accountId);
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					ids.add(resultSet.getInt("passport_id"));
				}
			}
		});
		return ids;
	}

	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
