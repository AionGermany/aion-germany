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
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.minion.MinionDopingBag;

/**
 * @author Falke_34
 */
public class MySQL5PlayerMinionsDAO extends PlayerMinionsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerMinionsDAO.class);

	@Override
	public void insertPlayerMinion(MinionCommonData minionCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO player_minions(player_id, object_id, minion_id, name, grade, level) VALUES(?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, minionCommonData.getMasterObjectId());
			stmt.setInt(2, minionCommonData.getObjectId());
			stmt.setInt(3, minionCommonData.getMinionId());
			stmt.setString(4, minionCommonData.getName());
			stmt.setString(5, minionCommonData.getMinionGrade());
			stmt.setInt(6, minionCommonData.getMinionLevel());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error inserting new minion #" + minionCommonData.getMinionId() + "[" + minionCommonData.getName() + "]", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void removePlayerMinion(Player player, int minionObjectId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM player_minions WHERE player_id = ? AND object_id = ?");
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, minionObjectId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error removing minion #" + minionObjectId, e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public List<MinionCommonData> getPlayerMinions(Player player) {
		List<MinionCommonData> minions = new ArrayList<MinionCommonData>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_minions WHERE player_id = ?");
			stmt.setInt(1, player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				MinionCommonData minionCommonData = new MinionCommonData(rs.getInt("minion_id"), player.getObjectId(), rs.getString("name"), rs.getString("grade"), rs.getInt("level"), rs.getInt("growthpoints"));
				minionCommonData.setObjectId(rs.getInt("object_id"));
				minionCommonData.setBirthday(rs.getTimestamp("birthday"));
				minionCommonData.setLock(rs.getInt("is_locked") == 1 ? true : false);
				if (minionCommonData.getDopingBag() != null) {
                    String Bag = rs.getString("buff_bag");
                    if (Bag != null && Bag.length() > 6) {
                        String[] ids = Bag.split(",");
                        for (int i = 0; i < ids.length; i++) {
                            minionCommonData.getDopingBag().setItem(Integer.parseInt(ids[i]), i);
                        }
                    }
                }
				minions.add(minionCommonData);
			}
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error getting minions for " + player.getObjectId(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return minions;
	}

	@Override
	public void updateMinionName(MinionCommonData minionCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE player_minions SET name = ? WHERE player_id = ? AND object_id = ?");
			stmt.setString(1, minionCommonData.getName());
			stmt.setInt(2, minionCommonData.getMasterObjectId());
			stmt.setInt(3, minionCommonData.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error update minion #" + minionCommonData.getMinionId(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public void updatePlayerMinionGrowthPoint(Player player, MinionCommonData minionCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE player_minions SET growthpoints = ? WHERE player_id = ? AND object_id = ?");
			stmt.setInt(1, minionCommonData.getMinionGrowthPoint());
			stmt.setInt(2, minionCommonData.getMasterObjectId());
			stmt.setInt(3, minionCommonData.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error update minion #" + minionCommonData.getMinionId(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}
	
	public boolean PlayerMinions(int playerid, int minionObjId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_minions WHERE player_id = ?");
			stmt.setInt(1, playerid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getInt("object_id") == minionObjId) return true;
			}
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error getting minions for " + playerid, e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return false;
	}

	@Override
	public void evolutionMinion(Player player, MinionCommonData minionCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE player_minions SET minion_id = ?, growthpoints = 0, level = ? WHERE player_id = ? AND object_id = ?");
			stmt.setInt(1, minionCommonData.getMinionId());
			stmt.setInt(2, minionCommonData.getMinionLevel());
			stmt.setInt(3, minionCommonData.getMasterObjectId());
			stmt.setInt(4, minionCommonData.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error update minion #" + minionCommonData.getMinionId(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void lockMinions(Player player, int minionObjId, int isLocked) {
		Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE player_minions SET is_locked = ? WHERE player_id = ? AND object_id = ?");
            stmt.setInt(1, isLocked);
            stmt.setInt(2, player.getObjectId());
            stmt.setInt(3, minionObjId);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Error update minionId #" + minionObjId, e);
        } finally {
            DatabaseFactory.close(con);
        }
	}
	
	@Override
    public void saveDopingBag(Player player, MinionCommonData minionCommonData, MinionDopingBag bag) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE player_minions SET buff_bag = ? WHERE player_id = ? AND object_id = ?");
            String itemIds = bag.getFoodItem() + "," + bag.getDrinkItem();
            for (int itemId : bag.getScrollsUsed()) {
                itemIds += "," + Integer.toString(itemId);
            }
            stmt.setString(1, itemIds);
            stmt.setInt(2, player.getObjectId());
            stmt.setInt(3, minionCommonData.getObjectId());
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Error update doping for minion #" + minionCommonData.getObjectId(), e);
        } finally {
            DatabaseFactory.close(con);
        }
    }
	public void saveBirthday(MinionCommonData minionCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_minions WHERE player_id = ? AND object_id = ?");
			stmt.setInt(1, minionCommonData.getMasterObjectId());
			stmt.setInt(2, minionCommonData.getObjectId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				minionCommonData.setBirthday(rs.getTimestamp("birthday"));
			}
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error getting minions for " + minionCommonData.getMasterObjectId(), e);
        } finally {
            DatabaseFactory.close(con);
        }
	}


	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
