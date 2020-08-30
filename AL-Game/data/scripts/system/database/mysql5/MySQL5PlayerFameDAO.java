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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerFameDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.fame.PlayerFame;

import javolution.util.FastList;
import javolution.util.FastMap;

public class MySQL5PlayerFameDAO extends PlayerFameDAO {

    private Logger log = LoggerFactory.getLogger(MySQL5PlayerFameDAO.class);

    public static final String INSERT_ACHIEVEMENT = "INSERT INTO player_fame (player_id, fame_id, level, exp) VALUES (?, ?, ?, ?)";
    public static final String LOAD_QUERY = "SELECT * FROM `player_fame` WHERE `player_id`=?";
    public static final String UPDATE_QUERY = "UPDATE player_fame set level=?, exp=?, exp_loss=? WHERE `player_id`=? AND `fame_id`=?";
    public static final String REDUCE_QUERY = "UPDATE player_fame set exp=?, level=? WHERE `player_id`=? AND `fame_id`=?";
    public static final String LOAD_WEEKLY = "SELECT * FROM `player_fame`";


    @Override
    public Map<Integer, PlayerFame> loadPlayerFame(final Player player) {
        final Map<Integer, PlayerFame> playerfames = new FastMap<Integer, PlayerFame>();
        DB.select(LOAD_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, player.getObjectId());
            }
            @Override
            public void handleRead(ResultSet rset) throws SQLException {
                while (rset.next()) {
                    PlayerFame fame = new PlayerFame(rset.getInt("fame_id"), rset.getInt("level"), rset.getLong("exp"), rset.getLong("exp_loss"), player.getObjectId());
                    playerfames.put(fame.getId(), fame);
                }
            }
        });
        return playerfames;
    }

    @Override
    public boolean addPlayerFame(Player player, PlayerFame fame) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_ACHIEVEMENT);
            stmt.setInt(1, player.getObjectId());
            stmt.setInt(2, fame.getId());
            stmt.setInt(3, fame.getLevel());
            stmt.setLong(4, fame.getExp());
            stmt.execute();
            stmt.close();
            return true;
        } 
        catch (SQLException e) {
            log.error("addFame error", e);

            return false;
        } 
        finally {
            DatabaseFactory.close(con);
        }
    }

    @Override
    public boolean updatePlayerFame(Player player, PlayerFame fame) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
            stmt.setInt(1, fame.getLevel());
            stmt.setLong(2, fame.getExp());
            stmt.setLong(3, fame.getExpLoss());
            stmt.setInt(4, player.getObjectId());
            stmt.setInt(5, fame.getId());
            stmt.execute();
            stmt.close();
        } 
        catch (Exception e) {
            log.error("Could not update PlayerFame data for Player " + player.getName() + " from DB: " + e.getMessage(), e);
            return false;
        } 
        finally {
            DatabaseFactory.close(con);
        }
        return true;
    }

    @Override
    public List<PlayerFame> weeklyFame() {
        final List<PlayerFame> playerfames = new FastList<PlayerFame>();
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(LOAD_WEEKLY);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                PlayerFame fame = new PlayerFame(rset.getInt("fame_id"), rset.getInt("level"), rset.getLong("exp"), rset.getLong("exp_loss"), rset.getInt("player_id"));
                playerfames.add(fame);
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
        }
        finally {
            DatabaseFactory.close(con);
        }
        return playerfames;
    }

    @Override
    public boolean reduceWeekly(PlayerFame fame) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(REDUCE_QUERY);
            stmt.setLong(1, fame.getExp());
            stmt.setInt(2, fame.getLevel());
            stmt.setInt(3, fame.getPlayerId());
            stmt.setInt(4, fame.getId());
            stmt.execute();
            stmt.close();
        } 
        catch (Exception e) {
            return false;
        } 
        finally {
            DatabaseFactory.close(con);
        }
        return true;
    }

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    } 
}
