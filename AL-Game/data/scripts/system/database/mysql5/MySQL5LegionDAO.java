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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.LegionDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team.legion.LegionEmblem;
import com.aionemu.gameserver.model.team.legion.LegionEmblemType;
import com.aionemu.gameserver.model.team.legion.LegionHistory;
import com.aionemu.gameserver.model.team.legion.LegionHistoryType;
import com.aionemu.gameserver.model.team.legion.LegionJoinRequest;
import com.aionemu.gameserver.model.team.legion.LegionTerritory;
import com.aionemu.gameserver.model.team.legion.LegionWarehouse;

import javolution.util.FastList;

/**
 * @author Simple
 * @modified cura
 */
public class MySQL5LegionDAO extends LegionDAO {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(MySQL5LegionDAO.class);
	/**
	 * Legion Queries
	 */
	private static final String INSERT_LEGION_QUERY = "INSERT INTO legions(id, `name`) VALUES (?, ?)";
	private static final String SELECT_LEGION_QUERY1 = "SELECT * FROM legions WHERE id=?";
	private static final String SELECT_LEGION_QUERY2 = "SELECT * FROM legions WHERE name=?";
	private static final String DELETE_LEGION_QUERY = "DELETE FROM legions WHERE id = ?";
	private static final String UPDATE_LEGION_QUERY = "UPDATE legions SET name=?, level=?, contribution_points=?, deputy_permission=?, centurion_permission=?, legionary_permission=?, volunteer_permission=?, disband_time=?, description=?, joinType=?, minJoinLevel=?, territory=? WHERE id=?";
	/**
	 * Legion Description Query *
	 */
	private static final String UPDATE_LEGION_DESCRIPTION_QUERY = "UPDATE legions SET description=?, joinType=?, minJoinLevel=? WHERE id=?";
	/**
	 * Announcement Queries *
	 */
	private static final String INSERT_ANNOUNCEMENT_QUERY = "INSERT INTO legion_announcement_list(`legion_id`, `announcement`, `date`) VALUES (?, ?, ?)";
	private static final String SELECT_ANNOUNCEMENTLIST_QUERY = "SELECT * FROM legion_announcement_list WHERE legion_id=? ORDER BY date ASC LIMIT 0,7;";
	private static final String DELETE_ANNOUNCEMENT_QUERY = "DELETE FROM legion_announcement_list WHERE legion_id = ? AND date = ?";
	/**
	 * Emblem Queries *
	 */
	private static final String INSERT_EMBLEM_QUERY = "INSERT INTO legion_emblems(legion_id, emblem_id, color_r, color_g, color_b, emblem_type, emblem_data) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_EMBLEM_QUERY = "UPDATE legion_emblems SET emblem_id=?, color_r=?, color_g=?, color_b=?, emblem_type=?, emblem_data=? WHERE legion_id=?";
	private static final String SELECT_EMBLEM_QUERY = "SELECT * FROM legion_emblems WHERE legion_id=?";
	/**
	 * Storage Queries *
	 */
	private static final String SELECT_STORAGE_QUERY = "SELECT `item_unique_id`, `item_id`, `item_count`, `item_color`, `color_expires`, `item_creator`, `expire_time`, `activation_count`, `is_equiped`, `slot`, `enchant`, `item_skin`, `fusioned_item`, `optional_socket`, `optional_fusion_socket`, `charge`, `rnd_bonus`, `rnd_count`, `pack_count`, `authorize`, `is_packed`, `is_amplified`, `buff_skill`, `reduction_level`, `luna_reskin`, `isEnhance`, `enhanceSkillId`, `enhanceSkillEnchant` FROM `inventory` WHERE `item_owner`=? AND `item_location`=? AND `is_equiped`=?";
	/**
	 * History Queries *
	 */
	private static final String INSERT_HISTORY_QUERY = "INSERT INTO legion_history(`legion_id`, `date`, `history_type`, `name`, `tab_id`, `description`) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String SELECT_HISTORY_QUERY = "SELECT * FROM `legion_history` WHERE legion_id=? ORDER BY date ASC;";
	private static final String CLEAR_LEGION_SIEGE = "UPDATE siege_locations SET legion_id=0 WHERE legion_id=?";
	/**
	 * Announcement Queries *
	 */
	private static final String INSERT_RECRUIT_LIST_QUERY = "INSERT INTO legion_join_requests(`legionId`, `playerId`, `playerName`, `playerClassId`, `playerRaceId`, `playerLevel`, `playerGenderId`, `joinRequestMsg`, `date`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_RECRUIT_LIST_QUERY = "SELECT * FROM legion_join_requests WHERE legionId=? ORDER BY date ASC;";
	private static final String DELETE_RECRUIT_LIST_QUERY = "DELETE FROM legion_join_requests WHERE legionId = ? AND playerId = ?";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNameUsed(final String name) {
		PreparedStatement s = DB.prepareStatement("SELECT count(id) as cnt FROM legions WHERE ? = legions.name");
		try {
			s.setString(1, name);
			ResultSet rs = s.executeQuery();
			rs.next();
			return rs.getInt("cnt") > 0;
		}
		catch (SQLException e) {
			log.error("Can't check if name " + name + ", is used, returning possitive result", e);
			return true;
		}
		finally {
			DB.close(s);
		}
	}

	@Override
	public Collection<Integer> getLegionIdswithTerritories() {
		Collection<Integer> legionIds = new ArrayList<Integer>();
		PreparedStatement s = DB.prepareStatement("SELECT id FROM legions WHERE territory > 0");
		try {

			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				legionIds.add(rs.getInt("id"));
			}
		}
		catch (SQLException e) {
			log.error("Error on getting legions with territoryId... Error: ", e);
		}
		finally {
			DB.close(s);
		}

		return legionIds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveNewLegion(final Legion legion) {
		boolean success = DB.insertUpdate(INSERT_LEGION_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				log.debug("[DAO: MySQL5LegionDAO] saving new legion: " + legion.getLegionId() + " " + legion.getLegionName());

				preparedStatement.setInt(1, legion.getLegionId());
				preparedStatement.setString(2, legion.getLegionName());
				preparedStatement.execute();
			}
		});
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeLegion(final Legion legion) {
		DB.insertUpdate(UPDATE_LEGION_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				log.debug("[DAO: MySQL5LegionDAO] storing player " + legion.getLegionId() + " " + legion.getLegionName());

				stmt.setString(1, legion.getLegionName());
				stmt.setInt(2, legion.getLegionLevel());
				stmt.setLong(3, legion.getContributionPoints());
				stmt.setInt(4, legion.getDeputyPermission());
				stmt.setInt(5, legion.getCenturionPermission());
				stmt.setInt(6, legion.getLegionaryPermission());
				stmt.setInt(7, legion.getVolunteerPermission());
				stmt.setInt(8, legion.getDisbandTime());
				stmt.setString(9, legion.getLegionDiscription());
				stmt.setInt(10, legion.getLegionJoinType());
				stmt.setInt(11, legion.getMinLevel());
				stmt.setInt(12, (legion.getTerritory() != null && legion.getTerritory().getId() > 0) ? legion.getTerritory().getId() : 0);
				stmt.setInt(13, legion.getLegionId());

				if (!legion.getJoinRequestMap().isEmpty()) {
					for (LegionJoinRequest ljr : legion.getJoinRequestMap().values()) {
						storeLegionJoinRequest(ljr);
					}
				}
				stmt.execute();
			}
		});
	}

	@Override
	public void updateLegionDescription(final Legion legion) {
		DB.insertUpdate(UPDATE_LEGION_DESCRIPTION_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				log.debug("[DAO: MySQL5LegionDAO] Updating Legion Description " + legion.getLegionId() + " " + legion.getLegionName() + " Description : " + legion.getLegionDiscription());

				stmt.setString(1, legion.getLegionDiscription());
				stmt.setInt(2, legion.getLegionJoinType());
				stmt.setInt(3, legion.getMinLevel());
				stmt.setInt(4, legion.getLegionId());
				stmt.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Legion loadLegion(final String legionName) {
		final Legion legion = new Legion();

		boolean success = DB.select(SELECT_LEGION_QUERY2, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setString(1, legionName);
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					legion.setLegionName(legionName);
					legion.setLegionId(resultSet.getInt("id"));
					legion.setLegionLevel(resultSet.getInt("level"));
					legion.addContributionPoints(resultSet.getLong("contribution_points"));

					legion.setLegionPermissions(resultSet.getShort("deputy_permission"), resultSet.getShort("centurion_permission"), resultSet.getShort("legionary_permission"), resultSet.getShort("volunteer_permission"));
					legion.setDescription(resultSet.getString("description"));
					legion.setJoinType(resultSet.getInt("joinType"));
					legion.setMinJoinLevel(resultSet.getInt("minJoinLevel"));

					int terrId = resultSet.getInt("territory");
					LegionTerritory t = new LegionTerritory(terrId);
					if (terrId > 0) {
						t.setLegionId(legion.getLegionId());
						t.setLegionName(legion.getLegionName());
					}
					legion.setTerritory(t);

					for (LegionJoinRequest ljr : loadLegionJoinRequests(legion.getLegionId())) {
						legion.addJoinRequest(ljr);
					}

					legion.setDisbandTime(resultSet.getInt("disband_time"));
				}
			}
		});

		log.debug("[MySQL5LegionDAO] Loaded " + legion.getLegionId() + " legion.");

		return (success && legion.getLegionId() != 0) ? legion : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Legion loadLegion(final int legionId) {
		final Legion legion = new Legion();

		boolean success = DB.select(SELECT_LEGION_QUERY1, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, legionId);
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					legion.setLegionId(legionId);
					legion.setLegionName(resultSet.getString("name"));
					legion.setLegionLevel(resultSet.getInt("level"));
					legion.addContributionPoints(resultSet.getLong("contribution_points"));

					legion.setLegionPermissions(resultSet.getShort("deputy_permission"), resultSet.getShort("centurion_permission"), resultSet.getShort("legionary_permission"), resultSet.getShort("volunteer_permission"));
					legion.setDescription(resultSet.getString("description"));
					legion.setJoinType(resultSet.getInt("joinType"));
					legion.setMinJoinLevel(resultSet.getInt("minJoinLevel"));

					int terrId = resultSet.getInt("territory");
					LegionTerritory t = new LegionTerritory(terrId);
					if (terrId > 0) {
						t.setLegionId(legion.getLegionId());
						t.setLegionName(legion.getLegionName());
					}
					legion.setTerritory(t);

					for (LegionJoinRequest ljr : loadLegionJoinRequests(legionId)) {
						legion.addJoinRequest(ljr);
					}

					legion.setDisbandTime(resultSet.getInt("disband_time"));
				}
			}
		});

		log.debug("[MySQL5LegionDAO] Loaded " + legion.getLegionId() + " legion.");

		return (success && legion.getLegionName() != "") ? legion : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteLegion(int legionId) {
		PreparedStatement statement = DB.prepareStatement(DELETE_LEGION_QUERY);
		try {
			statement.setInt(1, legionId);
		}
		catch (SQLException e) {
			log.error("deleteLegion #1", e);
		}
		DB.executeUpdateAndClose(statement);

		statement = DB.prepareStatement(CLEAR_LEGION_SIEGE);
		try {
			statement.setInt(1, legionId);
		}
		catch (SQLException e) {
			log.error("deleteLegion #2", e);
		}
		DB.executeUpdateAndClose(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getUsedIDs() {
		PreparedStatement statement = DB.prepareStatement("SELECT id FROM legions", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		try {
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				ids[i] = rs.getInt("id");
			}
			return ids;
		}
		catch (SQLException e) {
			log.error("Can't get list of id's from legions table", e);
		}
		finally {
			DB.close(statement);
		}

		return new int[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeMap<Timestamp, String> loadAnnouncementList(final int legionId) {
		final TreeMap<Timestamp, String> announcementList = new TreeMap<Timestamp, String>();

		boolean success = DB.select(SELECT_ANNOUNCEMENTLIST_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, legionId);
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					String message = resultSet.getString("announcement");
					Timestamp date = resultSet.getTimestamp("date");

					announcementList.put(date, message);
				}
			}
		});

		log.debug("[MySQL5LegionDAO] Loaded announcementList " + legionId + " legion.");

		return success ? announcementList : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveNewAnnouncement(final int legionId, final Timestamp currentTime, final String message) {
		boolean success = DB.insertUpdate(INSERT_ANNOUNCEMENT_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				log.debug("[DAO: MySQL5LegionDAO] saving new announcement.");

				preparedStatement.setInt(1, legionId);
				preparedStatement.setString(2, message);
				preparedStatement.setTimestamp(3, currentTime);
				preparedStatement.execute();
			}
		});
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAnnouncement(int legionId, Timestamp unixTime) {
		PreparedStatement statement = DB.prepareStatement(DELETE_ANNOUNCEMENT_QUERY);
		try {
			statement.setInt(1, legionId);
			statement.setTimestamp(2, unixTime);
		}
		catch (SQLException e) {
			log.error("Some crap, can't set int parameter to PreparedStatement", e);
		}
		DB.executeUpdateAndClose(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeLegionEmblem(final int legionId, final LegionEmblem legionEmblem) {
		if (!validEmblem(legionEmblem)) {
			return;
		}
		if (!(checkEmblem(legionId))) {
			createLegionEmblem(legionId, legionEmblem);
		}
		else {
			switch (legionEmblem.getPersistentState()) {
				case UPDATE_REQUIRED:
					updateLegionEmblem(legionId, legionEmblem);
					break;
				case NEW:
					createLegionEmblem(legionId, legionEmblem);
					break;
				default:
					break;
			}
		}
		legionEmblem.setPersistentState(PersistentState.UPDATED);
	}

	private boolean validEmblem(final LegionEmblem legionEmblem) {
		return (legionEmblem.getEmblemType().toString().equals("CUSTOM") && legionEmblem.getCustomEmblemData() == null) ? false : true;
	}

	/**
	 * @param legionid
	 */
	public boolean checkEmblem(final int legionid) {
		PreparedStatement st = DB.prepareStatement(SELECT_EMBLEM_QUERY);
		try {
			st.setInt(1, legionid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			log.error("Can't check " + legionid + " legion emblem: ", e);
		}
		finally {
			DB.close(st);
		}
		return false;
	}

	/**
	 * @param legionId
	 * @param legionEmblem
	 * @return
	 */
	private void createLegionEmblem(final int legionId, final LegionEmblem legionEmblem) {
		DB.insertUpdate(INSERT_EMBLEM_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, legionId);
				preparedStatement.setInt(2, legionEmblem.getEmblemId());
				preparedStatement.setInt(3, legionEmblem.getColor_r());
				preparedStatement.setInt(4, legionEmblem.getColor_g());
				preparedStatement.setInt(5, legionEmblem.getColor_b());
				preparedStatement.setString(6, legionEmblem.getEmblemType().toString());
				preparedStatement.setBytes(7, legionEmblem.getCustomEmblemData());
				preparedStatement.execute();
			}
		});
	}

	/**
	 * @param legionId
	 * @param legionEmblem
	 */
	private void updateLegionEmblem(final int legionId, final LegionEmblem legionEmblem) {
		DB.insertUpdate(UPDATE_EMBLEM_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, legionEmblem.getEmblemId());
				stmt.setInt(2, legionEmblem.getColor_r());
				stmt.setInt(3, legionEmblem.getColor_g());
				stmt.setInt(4, legionEmblem.getColor_b());
				stmt.setString(5, legionEmblem.getEmblemType().toString());
				stmt.setBytes(6, legionEmblem.getCustomEmblemData());
				stmt.setInt(7, legionId);
				stmt.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LegionEmblem loadLegionEmblem(final int legionId) {
		final LegionEmblem legionEmblem = new LegionEmblem();

		DB.select(SELECT_EMBLEM_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, legionId);
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					legionEmblem.setEmblem(resultSet.getInt("emblem_id"), resultSet.getInt("color_r"), resultSet.getInt("color_g"), resultSet.getInt("color_b"), LegionEmblemType.valueOf(resultSet.getString("emblem_type")), resultSet.getBytes("emblem_data"));
				}
			}
		});
		legionEmblem.setPersistentState(PersistentState.UPDATED);

		return legionEmblem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LegionWarehouse loadLegionStorage(Legion legion) {
		final LegionWarehouse inventory = new LegionWarehouse(legion);
		final int legionId = legion.getLegionId();
		final int storage = StorageType.LEGION_WAREHOUSE.getId();
		final int equipped = 0;

		DB.select(SELECT_STORAGE_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, legionId);
				stmt.setInt(2, storage);
				stmt.setInt(3, equipped);
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next()) {
					int itemUniqueId = rset.getInt("item_unique_id");
					int itemId = rset.getInt("item_id");
					long itemCount = rset.getLong("item_count");
					int itemColor = rset.getInt("item_color");
					int colorExpireTime = rset.getInt("color_expires");
					String itemCreator = rset.getString("item_creator");
					int expireTime = rset.getInt("expire_time");
					int activationCount = rset.getInt("activation_count");
					int isEquiped = rset.getInt("is_equiped");
					int slot = rset.getInt("slot");
					int enchant = rset.getInt("enchant");
					int itemSkin = rset.getInt("item_skin");
					int fusionedItem = rset.getInt("fusioned_item");
					int optionalSocket = rset.getInt("optional_socket");
					int optionalFusionSocket = rset.getInt("optional_fusion_socket");
					int charge = rset.getInt("charge");
					int randomBonus = rset.getInt("rnd_bonus");
					int rndCount = rset.getInt("rnd_count");
					int packCount = rset.getInt("pack_count");
					int max_authorize = rset.getInt("authorize");
					int isPacked = rset.getInt("is_packed");
					int isAmplified = rset.getInt("is_amplified");
					int buffSkill = rset.getInt("buff_skill");
					int reductionLevel = rset.getInt("reduction_level");
					int isLunaReskin = rset.getInt("luna_reskin");
					boolean isEnhance = rset.getBoolean("isEnhance");
					int enhanceSkillId = rset.getInt("enhanceSkillId");
					int enhanceSkillEnchant = rset.getInt("enhanceSkillEnchant");
					Item item = new Item(itemUniqueId, itemId, itemCount, itemColor, colorExpireTime, itemCreator, expireTime, activationCount, isEquiped == 1, false, slot, storage, enchant, itemSkin, fusionedItem, optionalSocket, optionalFusionSocket, charge, randomBonus, rndCount, packCount, max_authorize, isPacked == 1, isAmplified == 1, buffSkill, reductionLevel, isLunaReskin == 1, isEnhance, enhanceSkillId, enhanceSkillEnchant);
					item.setPersistentState(PersistentState.UPDATED);
					inventory.onLoadHandler(item);
				}
			}
		});
		return inventory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadLegionHistory(final Legion legion) {

		final Collection<LegionHistory> history = legion.getLegionHistory();

		DB.select(SELECT_HISTORY_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, legion.getLegionId());
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					history.add(new LegionHistory(LegionHistoryType.valueOf(resultSet.getString("history_type")), resultSet.getString("name"), resultSet.getTimestamp("date"), resultSet.getInt("tab_id"), resultSet.getString("description")));
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveNewLegionHistory(final int legionId, final LegionHistory legionHistory) {
		boolean success = DB.insertUpdate(INSERT_HISTORY_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, legionId);
				preparedStatement.setTimestamp(2, legionHistory.getTime());
				preparedStatement.setString(3, legionHistory.getLegionHistoryType().toString());
				preparedStatement.setString(4, legionHistory.getName());
				preparedStatement.setInt(5, legionHistory.getTabId());
				preparedStatement.setString(6, legionHistory.getDescription());
				preparedStatement.execute();
			}
		});
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeLegionJoinRequest(final LegionJoinRequest legionJoinRequest) {
		DB.insertUpdate(INSERT_RECRUIT_LIST_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				log.debug("[DAO: MySQL5LegionDAO] storing Recrutit Request " + legionJoinRequest.getLegionId() + " for Player: " + legionJoinRequest.getPlayerName());

				stmt.setInt(1, legionJoinRequest.getLegionId());
				stmt.setInt(2, legionJoinRequest.getPlayerId());
				stmt.setString(3, legionJoinRequest.getPlayerName());
				stmt.setInt(4, legionJoinRequest.getPlayerClass());
				stmt.setInt(5, legionJoinRequest.getRace());
				stmt.setInt(6, legionJoinRequest.getLevel());
				stmt.setInt(7, legionJoinRequest.getGenderId());
				stmt.setString(8, legionJoinRequest.getMsg());
				stmt.setTimestamp(9, legionJoinRequest.getDate());
				stmt.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FastList<LegionJoinRequest> loadLegionJoinRequests(final int legionId) {
		final FastList<LegionJoinRequest> requestList = new FastList<LegionJoinRequest>();

		DB.select(SELECT_RECRUIT_LIST_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, legionId);
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					LegionJoinRequest ljr = new LegionJoinRequest();
					ljr.setLegionId(resultSet.getInt("legionId"));
					ljr.setPlayerId(resultSet.getInt("playerId"));
					ljr.setPlayerName(resultSet.getString("playerName"));
					ljr.setPlayerClass(resultSet.getInt("playerClassId"));
					ljr.setRace(resultSet.getInt("playerRaceId"));
					ljr.setLevel(resultSet.getInt("playerLevel"));
					ljr.setGenderId(resultSet.getInt("playerGenderId"));
					ljr.setDate(resultSet.getTimestamp("date"));
					// ljr.setPersistentState(PersistentState.UPDATED);
					requestList.add(ljr);
				}
			}
		});

		return requestList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteLegionJoinRequest(int legionId, int playerId) {
		PreparedStatement statement = DB.prepareStatement(DELETE_RECRUIT_LIST_QUERY);
		try {
			statement.setInt(1, legionId);
			statement.setInt(2, playerId);
		}
		catch (SQLException e) {
			log.error("deleteLegionJoinRequest #1", e);
		}
		DB.executeUpdateAndClose(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteLegionJoinRequest(LegionJoinRequest ljr) {
		deleteLegionJoinRequest(ljr.getLegionId(), ljr.getPlayerId());
	}
}
