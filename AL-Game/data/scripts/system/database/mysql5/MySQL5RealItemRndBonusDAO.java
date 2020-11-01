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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.RealItemRndBonusDAO;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.RealRandomBonus;
import com.aionemu.gameserver.model.items.RealRandomBonusStat;
import com.aionemu.gameserver.model.stats.container.StatEnum;

public class MySQL5RealItemRndBonusDAO extends RealItemRndBonusDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5RealItemRndBonusDAO.class);
	public static final String SELECT_QUERY = "SELECT * FROM real_item_rnd_bonus WHERE item_obj_id = ?";
	public static final String INSERT_QUERY = "INSERT INTO real_item_rnd_bonus (`item_obj_id`, `stat_name`, `stat_val`, `is_fusion`) VALUES (?, ?, ?, ?)";
	public static final String DELETE_QUERY_ALL = "DELETE FROM real_item_rnd_bonus WHERE item_obj_id = ?";
	public static final String DELETE_QUERY_MAIN = "DELETE FROM real_item_rnd_bonus WHERE item_obj_id = ? AND is_fusion = 0";
	public static final String DELETE_QUERY_FUSION = "DELETE FROM real_item_rnd_bonus WHERE item_obj_id = ? AND is_fusion = 1";
	public static final String UPDATE_QUERY = "UPDATE real_item_rnd_bonus SET `item_obj_id` = ?, `is_fusion` = ? WHERE item_obj_id = ?";

	@Override
	public void loadRandomBonuses(final Item item) {
		Connection con = null;
		List<RealRandomBonusStat> stats = new ArrayList<RealRandomBonusStat>();
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, item.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while(rset.next()) {
				stats.add(new RealRandomBonusStat(StatEnum.findByStringName(rset.getString("stat_name")), rset.getInt("stat_val"), rset.getInt("is_fusion") == 0 ? false : true));
				System.out.println("RANDOM-STATS Loaded " + rset.getString("stat_name"));
			}
			RealRandomBonus bonus = new RealRandomBonus(item.getObjectId(), stats);
			item.setRealRndBonus(bonus);
			rset.close();
            stmt.close();
		}
		catch(Exception e) {
			log.error("Error in MySQL5RealItemRandBonusDAO.loadRandomBonuses", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return;
 	}

	@Override
	public void updateRandomBonuses(RealRandomBonus bonus) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();

			for (RealRandomBonusStat stat : bonus.getStats()) {
				stmt = con.prepareStatement(INSERT_QUERY);
				stmt.setInt(1, bonus.getItemObjectId());
				stmt.setString(2, stat.getStat().toString());
				stmt.setInt(3, stat.getValue());
				stmt.setInt(4, !stat.isFusion() ? 0 : 1);
				stmt.execute();
			}
			stmt.close();
		}
		catch(Exception e) {
			log.error("Error in MySQL5RealRandomBonusesDAO.updateRandomBonuses", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return;
	}

	@Override
	public void deleteAllRandomBonuses(Item item) {
		Connection con = null;
		PreparedStatement stmt = null;	
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(DELETE_QUERY_ALL);
			stmt.setInt(1, item.getObjectId());
			stmt.executeUpdate();
		}
		catch(Exception e) {
			log.error("Error in MySQL5RealRandomBonusesDAO.deleteRandomBonuses", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return;
	}

	@Override
	public void updateFusionRandomBonuses(Item first, Item second) {
		Connection con = null;
		PreparedStatement stmt = null;	
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, first.getObjectId());
            stmt.setInt(2, 1);
            stmt.setInt(3, second.getObjectId());
			stmt.executeUpdate();
		}
		catch(Exception e) {
			log.error("Error in MySQL5RealRandomBonusesDAO.deleteRandomBonuses", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return;
	}

	
	@Override
	public void deleteMainRandomBonuses(Item item) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(DELETE_QUERY_MAIN);
			stmt.setInt(1, item.getObjectId());
			stmt.executeUpdate();
		}
		catch(Exception e) {
			log.error("Error in MySQL5RealRandomBonusesDAO.deleteRandomBonuses", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return;
	}

	@Override
	public void deleteFusionRandomBonuses(Item item) {
		Connection con = null;
		PreparedStatement stmt = null;	
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(DELETE_QUERY_FUSION);
			stmt.setInt(1, item.getObjectId());
			stmt.executeUpdate();
		}
		catch(Exception e) {
			log.error("Error in MySQL5RealRandomBonusesDAO.deleteRandomBonuses", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
