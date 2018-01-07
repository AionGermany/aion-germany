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
import com.aionemu.gameserver.dao.GuideDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.guide.Guide;

/**
 * @author xTz
 */
public class MySQL5GuideDAO extends GuideDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5GuideDAO.class);
	public static final String DELETE_QUERY = "DELETE FROM `guides` WHERE `guide_id`=?";
	public static final String SELECT_QUERY = "SELECT * FROM `guides` WHERE `player_id`=?";
	public static final String SELECT_GUIDE_QUERY = "SELECT * FROM `guides` WHERE `guide_id`=? AND `player_id`=?";

	@Override
	public boolean supports(String arg0, int arg1, int arg2) {
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}

	@Override
	public boolean deleteGuide(int guide_id) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, guide_id);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error delete guide_id: " + guide_id, e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public List<Guide> loadGuides(int playerId) {
		final List<Guide> guides = new ArrayList<Guide>();

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int guide_id = rset.getInt("guide_id");
				int player_id = rset.getInt("player_id");
				String title = rset.getString("title");

				Guide guide = new Guide(guide_id, player_id, title);
				guides.add(guide);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore Guide data for player: " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return guides;
	}

	@Override
	public Guide loadGuide(int player_id, int guide_id) {
		Guide guide = null;

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_GUIDE_QUERY);
			stmt.setInt(1, guide_id);
			stmt.setInt(2, player_id);

			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				String title = rset.getString("title");
				guide = new Guide(guide_id, player_id, title);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore Survey data for player: " + player_id + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return guide;
	}

	@Override
	public void saveGuide(int guide_id, Player player, String title) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO guides(guide_id, title, player_id)" + "VALUES (?, ?, ?)");

			stmt.setInt(1, guide_id);
			stmt.setString(2, title);
			stmt.setInt(3, player.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error saving playerName: " + player, e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public int[] getUsedIDs() {
		PreparedStatement statement = DB.prepareStatement("SELECT guide_id FROM guides", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		try {
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				ids[i] = rs.getInt("guide_id");
			}
			return ids;
		}
		catch (SQLException e) {
			log.error("Can't get list of id's from guides table", e);
		}
		finally {
			DB.close(statement);
		}
		return new int[0];
	}
}
