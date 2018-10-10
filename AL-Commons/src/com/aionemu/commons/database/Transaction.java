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

package com.aionemu.commons.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class allows easy manipulations with transactions, it should be used
 * when critical or synchronized data should be commited to two or more tables.
 * This class allows us to avoid data synchronization problems in db.
 * <p/>
 * Class is not designed to be thread-safe, should be synchronized
 * externally.<br>
 * Class is not fail-safe, if error happens - exception will be thrown.
 * 
 * @author SoulKeeper
 */
public class Transaction {

	/**
	 * Logger for transactions
	 */
	private static final Logger log = LoggerFactory.getLogger(Transaction.class);

	/**
	 * Connection that is allocated for this transaction
	 */
	private Connection connection;

	/**
	 * Package private constructor, should be instantiated via
	 * {@link com.aionemu.commons.database.DB#beginTransaction()} class
	 * 
	 * @param con
	 *            Connection that will be used for this transaction
	 * @throws java.sql.SQLException
	 *             if can't disable autocommit mode
	 */
	Transaction(Connection con) throws SQLException {
		this.connection = con;
		connection.setAutoCommit(false);
	}

	/**
	 * Adds Insert / Update Query to the transaction
	 * 
	 * @param sql
	 *            SQL string
	 * @throws SQLException
	 *             if something went wrong
	 */
	public void insertUpdate(String sql) throws SQLException {
		insertUpdate(sql, null);
	}

	/**
	 * Adds Insert / Update Query to this transaction. Utilizes IUSth for
	 * Batching and Query Editing. MUST MANUALLY EXECUTE QUERY / BATACH IN IUSth
	 * (No need to close Statement after execution)
	 * 
	 * @param sql
	 *            Sql query
	 * @param iusth
	 *            query helper
	 * @throws SQLException
	 *             if something went wrong
	 */
	public void insertUpdate(String sql, IUStH iusth) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sql);
		if (iusth != null) {
			iusth.handleInsertUpdate(statement);
		} else {
			statement.executeUpdate();
		}
	}

	/**
	 * Creates new savepoint
	 * 
	 * @param name
	 *            name of savepoint
	 * @return created savepoint
	 * @throws SQLException
	 *             if can't create save point
	 */
	public Savepoint setSavepoint(String name) throws SQLException {
		return connection.setSavepoint(name);
	}

	/**
	 * Releases savepoint of transaction
	 * 
	 * @param savepoint
	 *            savepoint to release
	 * @throws SQLException
	 *             if something went wrong
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint(savepoint);
	}

	/**
	 * Commits transaction
	 * 
	 * @throws SQLException
	 *             if something is wrong with transaction
	 */
	public void commit() throws SQLException {
		commit(null);
	}

	/**
	 * Commits transaction. If rollBackToOnError is null - whole transaction
	 * will be rolledback
	 * 
	 * @param rollBackToOnError
	 *            savepoint that should be used to rollback
	 * @throws SQLException
	 *             if something went wrongF
	 */
	public void commit(Savepoint rollBackToOnError) throws SQLException {

		try {
			connection.commit();
		} catch (SQLException e) {
			log.warn("Error while commiting transaction", e);

			try {
				if (rollBackToOnError != null) {
					connection.rollback(rollBackToOnError);
				} else {
					connection.rollback();
				}
			} catch (SQLException e1) {
				log.error("Can't rollback transaction", e1);
			}
		}

		connection.setAutoCommit(true);
		connection.close();
	}
}
