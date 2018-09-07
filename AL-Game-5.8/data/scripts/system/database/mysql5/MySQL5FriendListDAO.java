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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.FriendListDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;

/**
 * @author Ben
 */
public class MySQL5FriendListDAO extends FriendListDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5FriendListDAO.class);
	public static final String LOAD_QUERY = "SELECT * FROM `friends` WHERE `player`=?";
	public static final String ADD_QUERY = "INSERT INTO `friends` (`player`,`friend`) VALUES (?, ?)";
	public static final String DEL_QUERY = "DELETE FROM `friends` WHERE `player` = ? AND `friend` = ?";
	public static final String SET_NOTE = "UPDATE `friends` SET `note` = ? WHERE `player` = ? AND `friend` = ?";

	@Override
	public FriendList load(final Player player) {
		final List<Friend> friends = new ArrayList<Friend>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			PlayerDAO dao = DAOManager.getDAO(PlayerDAO.class);
			while (rset.next()) {
				int objId = rset.getInt("friend");

				PlayerCommonData pcd = dao.loadPlayerCommonData(objId);
				if (pcd != null) {
					Friend friend = new Friend(pcd);
					friends.add(friend);
				}
			}
		}
		catch (Exception e) {
			log.error("Could not restore QuestStateList data for player: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}

		return new FriendList(player, friends);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addFriends(final Player player, final Player friend) {
		return DB.insertUpdate(ADD_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
				ps.setInt(1, player.getObjectId());
				ps.setInt(2, friend.getObjectId());
				ps.addBatch();

				ps.setInt(1, friend.getObjectId());
				ps.setInt(2, player.getObjectId());
				ps.addBatch();

				ps.executeBatch();
			}
		});

	}

	@Override
	public boolean delFriends(final int playerOid, final int friendOid) {
		return DB.insertUpdate(DEL_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
				ps.setInt(1, playerOid);
				ps.setInt(2, friendOid);
				ps.addBatch();

				ps.setInt(1, friendOid);
				ps.setInt(2, playerOid);
				ps.addBatch();

				ps.executeBatch();
			}
		});
	}

	@Override
	public void setFriendNote(final int playerId, final int friendId, final String note) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SET_NOTE);
			stmt.setString(1, note);
			stmt.setInt(2, playerId);
			stmt.setInt(3, friendId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
