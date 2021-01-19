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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.AccountTransformDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.AccountTransfo;

import javolution.util.FastMap;

public class MySQL5AccountTransformDAO extends AccountTransformDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5AccountTransformDAO.class);
	private static final String LOAD_QUERY = "SELECT * FROM `account_transform` WHERE `account_id`=?";
	private static final String INSERT_QUERY = "INSERT INTO `account_transform`(`account_id`,`card_id`, `count`) VALUES (?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE account_transform set `count`=? WHERE `account_id`=? AND `card_id`=?";

	@Override
	public Map<Integer, AccountTransfo> loadAccountTransfo(final Account account) {
		final Map<Integer, AccountTransfo> tl = new FastMap<Integer, AccountTransfo>();
		DB.select(LOAD_QUERY, new ParamReadStH() {
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, account.getId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next()) {
					int id = rset.getInt("card_id");
					int count = rset.getInt("count");
					tl.put(id, new AccountTransfo(id, count));
				}
			}
		});
		return tl;
	}

	@Override
	public boolean addTransfo(Account account, AccountTransfo trans) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, account.getId());
			stmt.setInt(2, trans.getCardId());
			stmt.setInt(3, trans.getCount());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not store transform book for account " + account.getName() + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public boolean updateTransfo(Account account, AccountTransfo transfo) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, transfo.getCount());
			stmt.setInt(2, account.getId());
			stmt.setInt(3, transfo.getCardId());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not update Transform Card data for Account " + account.getName() + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public void deleteTransfo(Account account, int id) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM account_transform WHERE account_id = ? AND card_id = ?");
			stmt.setInt(1, account.getId());
			stmt.setInt(2, id);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error removing transformation #" + id, e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
