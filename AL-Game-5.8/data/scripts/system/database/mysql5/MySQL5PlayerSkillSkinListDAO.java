package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerSkillSkinListDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skinskill.SkillSkin;
import com.aionemu.gameserver.model.skinskill.SkillSkinList;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class MySQL5PlayerSkillSkinListDAO extends PlayerSkillSkinListDAO {

    private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerTitleListDAO.class);
    private static final String LOAD_QUERY = "SELECT `skin_id`, `remaining`, `active` FROM `player_skill_skins` WHERE `player_id`=?";
    private static final String INSERT_QUERY = "INSERT INTO `player_skill_skins`(`player_id`,`skin_id`, `remaining`, `active`) VALUES (?,?,?,?)";
    private static final String DELETE_QUERY = "DELETE FROM `player_skill_skins` WHERE `player_id`=? AND `skin_id` =?;";

    @Override
    public SkillSkinList loadSkillSkinList(final int playerId) {
        final SkillSkinList tl = new SkillSkinList();

        DB.select(LOAD_QUERY, new ParamReadStH() {
            @Override
            public void setParams(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, playerId);
            }

            @Override
            public void handleRead(ResultSet rset) throws SQLException {
                while (rset.next()) {
                    int id = rset.getInt("skin_id");
                    int remaining = rset.getInt("remaining");
                    int active = rset.getInt("active");
                    tl.addEntry(id, remaining, active);
                }
            }
        });
        return tl;
    }

    @Override
    public boolean storeSkillSkins(Player player, SkillSkin entry) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);

            stmt.setInt(1, player.getObjectId());
            stmt.setInt(2, entry.getId());
            stmt.setInt(3, entry.getExpireTime());
            stmt.setInt(4, entry.getIsActive());
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Could not store skill skin for player " + player.getObjectId() + " from DB: " + e.getMessage(), e);
            return false;
        } finally {
            DatabaseFactory.close(con);
        }
        return true;
    }

    @Override
    public boolean setActive(final int playerObjId, final int skinId) {
        return DB.insertUpdate("UPDATE player_skill_skins SET active =1 WHERE `player_id`=? AND `skin_id` = ?", new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, playerObjId);
                preparedStatement.setInt(2, skinId);
                preparedStatement.execute();
            }
        });
    }

    @Override
    public boolean setDeactive(final int playerObjId, final int skinId) {
        return DB.insertUpdate("UPDATE player_skill_skins SET active=0 WHERE `player_id`=? AND `skin_id`=?", new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, playerObjId);
                preparedStatement.setInt(2, skinId);
                preparedStatement.execute();
            }
        });
    }

    @Override
    public boolean removeSkillSkin(int playerId, int skinId) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
            stmt.setInt(1, playerId);
            stmt.setInt(2, skinId);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Could not delete skill skin for player " + playerId + " from DB: " + e.getMessage(), e);
            return false;
        } finally {
            DatabaseFactory.close(con);
        }
        return true;
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}    
}
