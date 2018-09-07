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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerNpcFactionsDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.npcFaction.ENpcFactionQuestState;
import com.aionemu.gameserver.model.gameobjects.player.npcFaction.NpcFaction;
import com.aionemu.gameserver.model.gameobjects.player.npcFaction.NpcFactions;

/**
 * @author MrPoke
 */
public class MySQL5PlayerNpcFactionsDAO extends PlayerNpcFactionsDAO {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerNpcFactionsDAO.class);
	public static final String SELECT_QUERY = "SELECT `faction_id`, `active`, `time`, `state`, `quest_id` FROM player_npc_factions WHERE `player_id`=?";
	public static final String INSERT_QUERY = "INSERT INTO player_npc_factions (`player_id`, `faction_id`, `active`, `time`, `state`, `quest_id`) VALUES (?,?,?,?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE player_npc_factions SET `active`=?, `time`=?, `state`=?, `quest_id`=?  WHERE `player_id`=? AND `faction_id`=?";

	@Override
	public void loadNpcFactions(Player player) {

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			NpcFactions factions = new NpcFactions(player);
			player.setNpcFactions(factions);
			while (rset.next()) {
				int faction_id = rset.getInt("faction_id");
				boolean active = rset.getBoolean("active");
				int time = rset.getInt("time");
				int questId = rset.getInt("quest_id");
				ENpcFactionQuestState state = ENpcFactionQuestState.valueOf(rset.getString("state"));
				NpcFaction faction = new NpcFaction(faction_id, time, active, state, questId);
				faction.setPersistentState(PersistentState.UPDATED);
				factions.addNpcFaction(faction);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore Npc faction data for playerObjId: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void storeNpcFactions(Player player) {
		for (NpcFaction npcFaction : player.getNpcFactions().getNpcFactions()) {
			switch (npcFaction.getPersistentState()) {
				case NEW:
					insertNpcFaction(player.getObjectId(), npcFaction);
					break;
				case UPDATE_REQUIRED:
					updateNpcFaction(player.getObjectId(), npcFaction);
					break;
				default:
					break;
			}
		}
	}

	private void insertNpcFaction(int playerObjectId, NpcFaction faction) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, playerObjectId);
			stmt.setInt(2, faction.getId());
			stmt.setBoolean(3, faction.isActive());
			stmt.setInt(4, faction.getTime());
			stmt.setString(5, faction.getState().name());
			stmt.setInt(6, faction.getQuestId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not insert Npc faction data for playerObjId: " + playerObjectId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	private void updateNpcFaction(int playerObjectId, NpcFaction faction) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setBoolean(1, faction.isActive());
			stmt.setInt(2, faction.getTime());
			stmt.setString(3, faction.getState().name());
			stmt.setInt(4, faction.getQuestId());
			stmt.setInt(5, playerObjectId);
			stmt.setInt(6, faction.getId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not update Npc faction data for playerObjId: " + playerObjectId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String arg0, int arg1, int arg2) {
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}
