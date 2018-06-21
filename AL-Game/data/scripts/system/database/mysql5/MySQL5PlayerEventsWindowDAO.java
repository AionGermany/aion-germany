package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerEventsWindowDAO;
import com.aionemu.gameserver.model.event_window.PlayerEventWindowEntry;
import com.aionemu.gameserver.model.event_window.PlayerEventWindowList;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ranastic
 */
public class MySQL5PlayerEventsWindowDAO extends PlayerEventsWindowDAO {

    private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerEventsWindowDAO.class);
    
    @Override
    public PlayerEventWindowList load(Player player) {
        List<PlayerEventWindowEntry> eventWindow = new ArrayList<PlayerEventWindowEntry>();
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT `event_id`, `last_stamp`, `elapsed` FROM `player_events_window` WHERE `account_id`=?");
            stmt.setInt(1, player.getPlayerAccount().getId());
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int id = rset.getInt("event_id");
                Timestamp lastStamp = rset.getTimestamp("last_stamp");
                int elapsed = rset.getInt("elapsed");
                eventWindow.add(new PlayerEventWindowEntry(id, lastStamp, elapsed, PersistentState.UPDATED));
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
            log.error("Could not restore Event Window account: " +player.getObjectId() + " from DB: " + e.getMessage(), e);
        } finally {
            DatabaseFactory.close(con);
        }
        return new PlayerEventWindowList(eventWindow);
    }
    
    @Override
    public boolean store(int accountId, int eventId, Timestamp last_stamp, int elapsed) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO `player_events_window` (`account_id`, `event_id`, `last_stamp`, `elapsed`) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE `event_id` = VALUES(`event_id`), `last_stamp` = VALUES(`last_stamp`)");
            stmt.setInt(1, accountId);
            stmt.setInt(2, eventId);
            stmt.setTimestamp(3, last_stamp);
            stmt.setInt(4, elapsed);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Could not store event window for account " + accountId + " from DB: " + e.getMessage(), e);
            return false;
        } finally {
            DatabaseFactory.close(con);
        }
        return true;
    }

    @Override
    public void insert(int accountId, int eventId, Timestamp last_stamp) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO `player_events_window` (`account_id`, `event_id`, `last_stamp`) VALUES (?,?,?)");
            stmt.setInt(1, accountId);
            stmt.setInt(2, eventId);
            stmt.setTimestamp(3, last_stamp);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Can't insert into events window: " + e.getMessage());
        }
        finally {
            DatabaseFactory.close(con);
        }
    }

    @Override
    public void delete(final int accountId, final int eventId) {
        DB.insertUpdate("DELETE FROM player_events_window WHERE account_id = ? AND event_id = ?", new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, accountId);
                preparedStatement.setInt(2, eventId);
            }
        });
    }

    @Override
    public List<Integer> getEventsWindow(final int accountId) {
        final List<Integer> ids = new ArrayList<Integer>();
        DB.select("SELECT event_id FROM player_events_window WHERE account_id = ?", new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, accountId);
            }
            @Override
            public void handleRead(ResultSet resultSet) throws SQLException {
                while(resultSet.next()) {
                    ids.add(resultSet.getInt("event_id"));
                }
            }
        });
        return ids;
    }

    @Override
    public Timestamp getLastStamp(int accountId, int eventId) {
        PreparedStatement s = DB.prepareStatement("SELECT last_stamp FROM player_events_window WHERE account_id = ? AND event_id = ?");
        try {
            s.setInt(1, accountId);
            s.setInt(2, eventId);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getTimestamp("last_stamp");
        } catch (SQLException e) {
            log.error("Can't get last received Stamp!" + e);
            return new Timestamp(System.currentTimeMillis());
        } finally {
            DB.close(s);
        }
    }

    @Override
    public double getElapsed(int accountId) {
        PreparedStatement s = DB.prepareStatement("SELECT elapsed FROM player_events_window WHERE account_id = ?");
        try {
            s.setInt(1, accountId);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getDouble("elapsed");
        } catch (SQLException e) {
            log.error("Can't get elapsed time!\n" + e);
            return 0;
        } finally {
            DB.close(s);
        }
    }

    @Override
    public void updateElapsed(int accountId, double elapsed) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE player_events_window SET elapsed = ? WHERE account_id = ?");

            stmt.setDouble(1, elapsed);
            stmt.setInt(2, accountId);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Error updating elapsed ", e);
        }
        finally {
            DatabaseFactory.close(con);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }    
}
