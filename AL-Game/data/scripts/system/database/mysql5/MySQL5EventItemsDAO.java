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
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.EventItemsDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.event.MaxCountOfDay;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

/**
 * @author Alex
 */
public class MySQL5EventItemsDAO extends EventItemsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5EventItemsDAO.class);

	public static final String INSERT_QUERY = "INSERT INTO `event_items` (`player_id`, `item_id`, `counts`) VALUES (?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `event_items` WHERE `player_id`=?";
	public static final String SELECT_QUERY = "SELECT `item_id`, `counts` FROM `event_items` WHERE `player_id`=?";

	private static final Predicate<MaxCountOfDay> maxCountOfDay = new Predicate<MaxCountOfDay>() {

		@Override
		public boolean apply(@Nullable MaxCountOfDay input) {
			return input != null;
		}
	};

	@Override
	public void loadItems(final Player player) {
		DB.select(SELECT_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, player.getObjectId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next()) {
					int itemId = rset.getInt("item_id");
					int counts = rset.getInt("counts");
					player.addItemMaxCountOfDay(itemId, counts);
				}
			}
		});
	}

	@Override
	public void storeItems(Player player) {
		// player.clearItemMaxThisCount();
		deleteItems(player);
		Map<Integer, MaxCountOfDay> itemsm = player.getItemMaxThisCounts();

		if (itemsm == null) {
			return;
		}

		Map<Integer, MaxCountOfDay> map = Maps.filterValues(itemsm, maxCountOfDay);
		final Iterator<Map.Entry<Integer, MaxCountOfDay>> iterator = map.entrySet().iterator();
		if (!iterator.hasNext()) {
			return;
		}
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseFactory.getConnection();
			con.setAutoCommit(false);
			st = con.prepareStatement(INSERT_QUERY);
			while (iterator.hasNext()) {
				Map.Entry<Integer, MaxCountOfDay> entry = iterator.next();
				st.setInt(1, player.getObjectId());
				st.setInt(2, entry.getKey());
				st.setInt(3, entry.getValue().getThisCount());
				st.addBatch();
			}
			st.executeBatch();
			con.commit();
			player.clearItemMaxThisCount();
		}
		catch (SQLException e) {
			log.error("Error while storing event_items for player " + player.getObjectId(), e);
		}
		finally {
			DatabaseFactory.close(st, con);
		}
	}

	@Override
	public void deleteItems(final int itemId) {
		DB.insertUpdate("DELETE FROM `event_items` WHERE `item_id`= ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, itemId);
				stmt.execute();
			}
		});
	}

	private void deleteItems(final Player player) {
		DB.insertUpdate(DELETE_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, player.getObjectId());
				stmt.execute();
			}
		});
	}

	@Override
	public boolean supports(String arg0, int arg1, int arg2) {
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}
