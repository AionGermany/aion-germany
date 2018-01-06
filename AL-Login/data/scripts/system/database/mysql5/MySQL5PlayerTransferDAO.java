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

import javolution.util.FastList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.loginserver.dao.PlayerTransferDAO;
import com.aionemu.loginserver.service.ptransfer.PlayerTransferTask;

/**
 * @author KID
 */
public class MySQL5PlayerTransferDAO extends PlayerTransferDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerTransferDAO.class);

	@Override
	public FastList<PlayerTransferTask> getNew() {
		FastList<PlayerTransferTask> list = FastList.newInstance();
		PreparedStatement st = DB.prepareStatement("SELECT * FROM player_transfers WHERE `status` = ?");
		try {
			st.setInt(1, 0);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				PlayerTransferTask task = new PlayerTransferTask();
				task.id = rs.getInt("id");
				task.sourceServerId = (byte) rs.getShort("source_server");
				task.targetServerId = (byte) rs.getShort("target_server");
				task.sourceAccountId = rs.getInt("source_account_id");
				task.targetAccountId = rs.getInt("target_account_id");
				task.playerId = rs.getInt("player_id");
				list.add(task);
			}
		}
		catch (Exception e) {
			log.error("Can't select getNew: ", e);
		}
		finally {
			DB.close(st);
		}

		return list;
	}

	@Override
	public boolean update(final PlayerTransferTask task) {
		String table = "";
		switch (task.status) {
			case PlayerTransferTask.STATUS_ACTIVE:
				table = ", time_performed=NOW()";
				break;
			case PlayerTransferTask.STATUS_DONE:
			case PlayerTransferTask.STATUS_ERROR:
				table = ", time_done=NOW()";
				break;
		}
		return DB.insertUpdate("UPDATE player_transfers SET status=?, comment=?" + table + " WHERE id=?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setByte(1, task.status);
				preparedStatement.setString(2, task.comment);
				preparedStatement.setInt(3, task.id);
				preparedStatement.execute();
			}
		});
	}

	@Override
	public boolean supports(String database, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(database, majorVersion, minorVersion);
	}
}
