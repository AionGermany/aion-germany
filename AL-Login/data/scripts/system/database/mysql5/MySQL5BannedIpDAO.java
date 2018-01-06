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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.commons.database.ReadStH;
import com.aionemu.loginserver.dao.BannedIpDAO;
import com.aionemu.loginserver.model.BannedIP;

/**
 * BannedIP DAO implementation for MySQL5
 * 
 * @author SoulKeeper
 */
public class MySQL5BannedIpDAO extends BannedIpDAO {

	@Override
	public BannedIP insert(String mask) {
		return insert(mask, null);
	}

	@Override
	public BannedIP insert(final String mask, final Timestamp expireTime) {
		BannedIP result = new BannedIP();
		result.setMask(mask);
		result.setTimeEnd(expireTime);

		if (insert(result)){
			return result;
		}
		return null;
	}

	@Override
	public boolean insert(final BannedIP bannedIP) {
		boolean insert = DB.insertUpdate("INSERT INTO banned_ip(mask, time_end) VALUES (?, ?)", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, bannedIP.getMask());
				if (bannedIP.getTimeEnd() == null)
					preparedStatement.setNull(2, Types.TIMESTAMP);
				else
					preparedStatement.setTimestamp(2, bannedIP.getTimeEnd());
				preparedStatement.execute();
			}
		});

		if (!insert)
			return false;

		final BannedIP result = new BannedIP();
		DB.select("SELECT * FROM banned_ip WHERE mask = ?", new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, bannedIP.getMask());
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				resultSet.next(); // mask is unique, only one result allowed
				result.setId(resultSet.getInt("id"));
				result.setMask(resultSet.getString("mask"));
				result.setTimeEnd(resultSet.getTimestamp("time_end"));
			}
		});
		return true;
	}

	@Override
	public boolean update(final BannedIP bannedIP) {
		return DB.insertUpdate("UPDATE banned_ip SET mask = ?, time_end = ? WHERE id = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, bannedIP.getMask());
				if (bannedIP.getTimeEnd() == null)
					preparedStatement.setNull(2, Types.TIMESTAMP);
				else
					preparedStatement.setTimestamp(2, bannedIP.getTimeEnd());
				preparedStatement.setInt(3, bannedIP.getId());
				preparedStatement.execute();
			}
		});
	}

	@Override
	public boolean remove(final String mask) {
		return DB.insertUpdate("DELETE FROM banned_ip WHERE mask = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, mask);
				preparedStatement.execute();
			}
		});
	}

	@Override
	public boolean remove(final BannedIP bannedIP) {
		return DB.insertUpdate("DELETE FROM banned_ip WHERE mask = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				// Changed from id to mask because we don't get id of last inserted ban
				preparedStatement.setString(1, bannedIP.getMask());
				preparedStatement.execute();
			}
		});
	}

	@Override
	public Set<BannedIP> getAllBans() {

		final Set<BannedIP> result = new HashSet<BannedIP>();
		DB.select("SELECT * FROM banned_ip", new ReadStH() {

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					BannedIP ip = new BannedIP();
					ip.setId(resultSet.getInt("id"));
					ip.setMask(resultSet.getString("mask"));
					ip.setTimeEnd(resultSet.getTimestamp("time_end"));
					result.add(ip);
				}
			}
		});
		return result;
	}

	@Override
	public void cleanExpiredBans() {
		DB.insertUpdate("DELETE FROM banned_ip WHERE time_end < current_timestamp AND time_end IS NOT NULL");
	}

	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
