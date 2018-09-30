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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.commons.utils.GenericValidator;
import com.aionemu.gameserver.configs.main.CacheConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.PlayerInitialData;
import com.aionemu.gameserver.dataholders.PlayerInitialData.LocationData;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.Mailbox;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerBonusTimeStatus;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.PlayerUpgradeArcade;
import com.aionemu.gameserver.model.team.legion.LegionJoinRequestState;
import com.aionemu.gameserver.world.MapRegion;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.google.common.collect.Maps;

import javolution.util.FastMap;

/**
 * @author SoulKeeper, Saelya
 * @author cura
 */
public class MySQL5PlayerDAO extends PlayerDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerDAO.class);
	private FastMap<Integer, PlayerCommonData> playerCommonData = new FastMap<Integer, PlayerCommonData>().shared();
	private FastMap<String, PlayerCommonData> playerCommonDataByName = new FastMap<String, PlayerCommonData>().shared();
	private MapRegion mr = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNameUsed(final String name) {
		PreparedStatement s = DB.prepareStatement("SELECT count(id) as cnt FROM players WHERE ? = players.name");
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
	public Map<Integer, String> getPlayerNames(Collection<Integer> playerObjectIds) {

		if (GenericValidator.isBlankOrNull(playerObjectIds)) {
			return Collections.emptyMap();
		}

		Map<Integer, String> result = Maps.newHashMap();

		String sql = "SELECT id, `name` FROM players WHERE id IN(%s)";
		sql = String.format(sql, StringUtils.join(playerObjectIds, ", "));
		PreparedStatement s = DB.prepareStatement(sql);
		try {
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				result.put(id, name);
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("Failed to load player names", e);
		}
		finally {
			DB.close(s);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changePlayerId(final Player player, final int accountId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players SET account_id=? WHERE id=?");
			stmt.setInt(1, accountId);
			stmt.setInt(2, player.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error saving player: " + player.getObjectId() + " " + player.getName(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storePlayer(final Player player) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players SET name=?, exp=?, recoverexp=?, x=?, y=?, z=?, heading=?, world_id=?, gender=?, race=?, player_class=?, last_online=?, cube_expands=?, advanced_stigma_slot_size=?, warehouse_size=?, note=?, title_id=?, bonus_title_id=?, dp=?, soul_sickness=?, mailbox_letters=?, reposte_energy=?, goldenstar_energy=?, growth_energy=?, bg_points=?, mentor_flag_time=?, initial_gamestats=?, world_owner=?, fatigue=?, fatigueRecover=?, fatigueReset=?, joinRequestLegionId=?, joinRequestState=?, frenzy_points=?, frenzy_count=?, bonus_type=?, bonus_buff_time=?, wardrobe_size=?, wardrobe_slot=?, luna_consume_count=?, muni_keys=?, luna_consume=?, toc_floor=?, minion_skill_points=?, minion_function_time=? WHERE id=?");

			log.debug("[DAO: MySQL5PlayerDAO] storing player " + player.getObjectId() + " " + player.getName());
			PlayerCommonData pcd = player.getCommonData();
			stmt.setString(1, player.getName());
			stmt.setLong(2, pcd.getExp());
			stmt.setLong(3, pcd.getExpRecoverable());
			stmt.setFloat(4, player.getX());
			stmt.setFloat(5, player.getY());
			stmt.setFloat(6, player.getZ());
			stmt.setInt(7, player.getHeading());
			stmt.setInt(8, player.getWorldId());
			stmt.setString(9, player.getGender().toString());
			stmt.setString(10, player.getRace().toString());
			stmt.setString(11, pcd.getPlayerClass().toString());
			stmt.setTimestamp(12, pcd.getLastOnline());
			stmt.setInt(13, player.getCubeExpands());
			stmt.setInt(14, pcd.getAdvancedStigmaSlotSize());
			stmt.setInt(15, player.getWarehouseSize());
			stmt.setString(16, pcd.getNote());
			stmt.setInt(17, pcd.getTitleId());
			stmt.setInt(18, pcd.getBonusTitleId());
			stmt.setInt(19, pcd.getDp());
			stmt.setInt(20, pcd.getDeathCount());
			Mailbox mailBox = player.getMailbox();
			int mails = mailBox != null ? mailBox.size() : pcd.getMailboxLetters();
			stmt.setInt(21, mails);
			stmt.setLong(22, pcd.getCurrentReposteEnergy());
			stmt.setLong(23, pcd.getGoldenStarEnergy());
			stmt.setLong(24, pcd.getGrowthEnergy());
			stmt.setInt(25, player.getCommonData().getBattleGroundPoints());
			stmt.setInt(26, pcd.getMentorFlagTime());
			stmt.setInt(27, pcd.isInitialGameStats());
			if (player.getPosition().getWorldMapInstance() == null) {
				log.error("Error saving player: " + player.getObjectId() + " " + player.getName() + ", world map instance is null. Setting world owner to 0. Position: " + player.getWorldId() + " " + player.getX() + " " + player.getY() + " " + player.getZ());
				stmt.setInt(28, 0);
			}
			else {
				stmt.setInt(28, player.getPosition().getWorldMapInstance().getOwnerId());
			}
			stmt.setInt(29, pcd.getFatigue());
			stmt.setInt(30, pcd.getFatigueRecover());
			stmt.setInt(31, pcd.getFatigueReset());
			stmt.setInt(32, pcd.getJoinRequestLegionId());
			stmt.setString(33, pcd.getJoinRequestState().name());
			stmt.setInt(34, player.getUpgradeArcade().getFrenzyPoints());
			stmt.setInt(35, player.getUpgradeArcade().getFrenzyCount());
			stmt.setString(36, player.getBonusTime().getStatus().toString());
			stmt.setTimestamp(37, pcd.getBonusTime().getTime());
			stmt.setInt(38, pcd.getWardrobeSize());
			stmt.setInt(39, pcd.getWardrobeSlot());
			stmt.setInt(40, pcd.getLunaConsumeCount());
			stmt.setInt(41, pcd.getMuniKeys());
			stmt.setInt(42, pcd.getLunaConsumePoint());
			stmt.setInt(43, pcd.getFloor());
			stmt.setInt(44, pcd.getMinionSkillPoints());
			stmt.setTimestamp(45, pcd.getMinionFunctionTime());
			stmt.setInt(46, player.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error saving player: " + player.getObjectId() + " " + player.getName(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		if (CacheConfig.CACHE_COMMONDATA) {
			PlayerCommonData cached = playerCommonData.get(player.getObjectId());
			if (cached != null) {
				playerCommonData.putEntry(player.getCommonData().getPlayerObjId(), player.getCommonData());
				playerCommonDataByName.putEntry(player.getName().toLowerCase(), player.getCommonData());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveNewPlayer(final PlayerCommonData pcd, final int accountId, final String accountName) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO players(id, `name`, account_id, account_name, x, y, z, heading, world_id, gender, race, player_class, cube_expands, warehouse_size, bonus_type, wardrobe_size, online)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)");

			log.debug("[DAO: MySQL5PlayerDAO] saving new player: " + pcd.getPlayerObjId() + " " + pcd.getName());

			preparedStatement.setInt(1, pcd.getPlayerObjId());
			preparedStatement.setString(2, pcd.getName());
			preparedStatement.setInt(3, accountId);
			preparedStatement.setString(4, accountName);
			preparedStatement.setFloat(5, pcd.getPosition().getX());
			preparedStatement.setFloat(6, pcd.getPosition().getY());
			preparedStatement.setFloat(7, pcd.getPosition().getZ());
			preparedStatement.setInt(8, pcd.getPosition().getHeading());
			preparedStatement.setInt(9, pcd.getPosition().getMapId());
			preparedStatement.setString(10, pcd.getGender().toString());
			preparedStatement.setString(11, pcd.getRace().toString());
			preparedStatement.setString(12, pcd.getPlayerClass().toString());
			preparedStatement.setInt(13, pcd.getCubeExpands());
			preparedStatement.setInt(14, pcd.getWarehouseSize());
			preparedStatement.setString(15, pcd.getBonusTime().getStatus().toString());
			preparedStatement.setInt(16, pcd.getWardrobeSize());
			preparedStatement.execute();
			preparedStatement.close();
		}
		catch (Exception e) {
			log.error("Error saving new player: " + pcd.getPlayerObjId() + " " + pcd.getName(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		if (CacheConfig.CACHE_COMMONDATA) {
			playerCommonData.put(pcd.getPlayerObjId(), pcd);
			playerCommonDataByName.put(pcd.getName().toLowerCase(), pcd);
		}
		return true;
	}

	@Override
	public PlayerCommonData loadPlayerCommonDataByName(final String name) {
		Player player = World.getInstance().findPlayer(name);
		if (player != null) {
			return player.getCommonData();
		}
		PlayerCommonData pcd = playerCommonDataByName.get(name.toLowerCase());
		if (pcd != null) {
			return pcd;
		}
		int playerObjId = 0;

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM players WHERE name = ?");
			stmt.setString(1, name);
			ResultSet rset = stmt.executeQuery();
			if (rset.next()) {
				playerObjId = rset.getInt("id");
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore playerId data for player name: " + name + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}

		if (playerObjId == 0) {
			return null;
		}
		return loadPlayerCommonData(playerObjId);
	}

	@Override
	public PlayerCommonData loadPlayerCommonData(final int playerObjId) {

		PlayerCommonData cached = playerCommonData.get(playerObjId);
		if (cached != null) {
			log.debug("[DAO: MySQL5PlayerDAO] PlayerCommonData for id: " + playerObjId + " obtained from cache");
			return cached;
		}
		final PlayerCommonData cd = new PlayerCommonData(playerObjId);
		boolean success = false;
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM players WHERE id = ?");
			stmt.setInt(1, playerObjId);
			ResultSet resultSet = stmt.executeQuery();
			log.debug("[DAO: MySQL5PlayerDAO] loading from db " + playerObjId);

			if (resultSet.next()) {
				success = true;
				cd.setName(resultSet.getString("name"));
				// set player class before exp
				cd.setPlayerClass(PlayerClass.valueOf(resultSet.getString("player_class")));
				cd.setExp(resultSet.getLong("exp"));
				cd.setRecoverableExp(resultSet.getLong("recoverexp"));
				cd.setRace(Race.valueOf(resultSet.getString("race")));
				cd.setGender(Gender.valueOf(resultSet.getString("gender")));
				cd.setCreationDate(resultSet.getTimestamp("creation_date"));
				cd.setLastOnline(resultSet.getTimestamp("last_online"));
				cd.setNote(resultSet.getString("note"));
				cd.setCubeExpands(resultSet.getInt("cube_expands"));
				cd.setAdvancedStigmaSlotSize(resultSet.getInt("advanced_stigma_slot_size"));
				cd.setTitleId(resultSet.getInt("title_id"));
				cd.setBonusTitleId(resultSet.getInt("bonus_title_id"));
				cd.setWarehouseSize(resultSet.getInt("warehouse_size"));
				cd.setOnline(resultSet.getBoolean("online"));
				cd.setMailboxLetters(resultSet.getInt("mailbox_letters"));
				cd.setDp(resultSet.getInt("dp"));
				cd.setDeathCount(resultSet.getInt("soul_sickness"));
				cd.setCurrentReposteEnergy(resultSet.getLong("reposte_energy"));
				cd.setGoldenStarEnergy(resultSet.getLong("goldenstar_energy"));
				cd.setGrowthEnergy(resultSet.getLong("growth_energy"));
				cd.setBattleGroundPoints(resultSet.getInt("bg_points"));

				float x = resultSet.getFloat("x");
				float y = resultSet.getFloat("y");
				float z = resultSet.getFloat("z");
				byte heading = resultSet.getByte("heading");
				int worldId = resultSet.getInt("world_id");
				PlayerInitialData playerInitialData = DataManager.PLAYER_INITIAL_DATA;
				boolean checkThis = World.getInstance().getWorldMap(worldId).isInstanceType();
				// this helps to pretend an player loading error
				// if you have a better idea do it :)
				if (checkThis) {
					mr = null;
				}
				else {
					mr = World.getInstance().getWorldMap(worldId).getMainWorldMapInstance().getRegion(x, y, z);
				}
				if (mr == null && playerInitialData != null) {
					// unstuck unlucky characters :)
					LocationData ld = playerInitialData.getSpawnLocation(cd.getRace());
					x = ld.getX();
					y = ld.getY();
					z = ld.getZ();
					heading = ld.getHeading();
					worldId = ld.getMapId();
				}

				WorldPosition position = World.getInstance().createPosition(worldId, x, y, z, heading, 0);
				cd.setPosition(position);
				cd.setWorldOwnerId(resultSet.getInt("world_owner"));
				cd.setMentorFlagTime(resultSet.getInt("mentor_flag_time"));
				cd.setInitialGameStats(resultSet.getInt("initial_gamestats"));
				cd.setLastTransferTime(resultSet.getLong("last_transfer_time"));
				cd.setFatigue(resultSet.getInt("fatigue"));
				cd.setFatigueRecover(resultSet.getInt("fatigueRecover"));
				cd.setFatigueReset(resultSet.getInt("fatigueReset"));
				cd.setJoinRequestLegionId(resultSet.getInt("joinRequestLegionId"));
				cd.setJoinRequestState(LegionJoinRequestState.valueOf(resultSet.getString("joinRequestState")));

				PlayerUpgradeArcade pua = new PlayerUpgradeArcade();
				pua.setFrenzyPoints(resultSet.getInt("frenzy_points"));
				pua.setFrenzyCount(resultSet.getInt("frenzy_count"));
				cd.setBonusType(PlayerBonusTimeStatus.valueOf(resultSet.getString("bonus_type")));
				cd.setBonusTime(resultSet.getTimestamp("bonus_buff_time"));
				cd.setUpgradeArcade(pua);
				cd.setWardrobeSize(resultSet.getInt("wardrobe_size"));
				cd.setWardrobeSlot(resultSet.getInt("wardrobe_slot"));
				cd.setLunaConsumeCount(resultSet.getInt("luna_consume_count"));
				cd.setMuniKeys(resultSet.getInt("muni_keys"));
				cd.setLunaConsumePoint(resultSet.getInt("luna_consume"));
				cd.setFloor(resultSet.getInt("toc_floor"));
				cd.setMinionSkillPoints(resultSet.getInt("minion_skill_points"));
				cd.setMinionFunctionTime(resultSet.getTimestamp("minion_function_time"));
			}
			else {
				log.info("Missing PlayerCommonData from db " + playerObjId);
			}
			resultSet.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore PlayerCommonData data for player: " + playerObjId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}

		if (success) {
			if (CacheConfig.CACHE_COMMONDATA) {
				playerCommonData.put(playerObjId, cd);
				playerCommonDataByName.put(cd.getName().toLowerCase(), cd);
			}
			return cd;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlayer(int playerId) {
		PreparedStatement statement = DB.prepareStatement("DELETE FROM players WHERE id = ?");
		try {
			statement.setInt(1, playerId);
		}
		catch (SQLException e) {
			log.error("Some crap, can't set int parameter to PreparedStatement", e);
		}
		if (CacheConfig.CACHE_COMMONDATA) {
			PlayerCommonData pcd = playerCommonData.remove(playerId);
			if (pcd != null) {
				playerCommonDataByName.remove(pcd.getName().toLowerCase());
			}
		}
		DB.executeUpdateAndClose(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getPlayerOidsOnAccount(final int accountId) {
		final List<Integer> result = new ArrayList<Integer>();
		boolean success = DB.select("SELECT id FROM players WHERE account_id = ?", new ParamReadStH() {

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException {
				while (resultSet.next()) {
					result.add(resultSet.getInt("id"));
				}
			}

			@Override
			public void setParams(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, accountId);
			}
		});

		return success ? result : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationDeletionTime(final PlayerAccountData acData) {
		DB.select("SELECT creation_date, deletion_date FROM players WHERE id = ?", new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, acData.getPlayerCommonData().getPlayerObjId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				rset.next();

				acData.setDeletionDate(rset.getTimestamp("deletion_date"));
				acData.setCreationDate(rset.getTimestamp("creation_date"));
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDeletionTime(final int objectId, final Timestamp deletionDate) {
		DB.insertUpdate("UPDATE players set deletion_date = ? where id = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setTimestamp(1, deletionDate);
				preparedStatement.setInt(2, objectId);
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeCreationTime(final int objectId, final Timestamp creationDate) {
		DB.insertUpdate("UPDATE players set creation_date = ? where id = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setTimestamp(1, creationDate);
				preparedStatement.setInt(2, objectId);
				preparedStatement.execute();
			}
		});
	}

	@Override
	public void storeLastOnlineTime(final int objectId, final Timestamp lastOnline) {
		DB.insertUpdate("UPDATE players set last_online = ? where id = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setTimestamp(1, lastOnline);
				preparedStatement.setInt(2, objectId);
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getUsedIDs() {
		PreparedStatement statement = DB.prepareStatement("SELECT id FROM players", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

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
			log.error("Can't get list of id's from players table", e);
		}
		finally {
			DB.close(statement);
		}

		return new int[0];
	}

	/**
	 * {@inheritDoc} - Saelya
	 */
	@Override
	public void onlinePlayer(final Player player, final boolean online) {
		DB.insertUpdate("UPDATE players SET online=? WHERE id=?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				log.debug("[DAO: MySQL5PlayerDAO] online status " + player.getObjectId() + " " + player.getName());

				stmt.setBoolean(1, online);
				stmt.setInt(2, player.getObjectId());
				stmt.execute();
			}
		});
	}

	/**
	 * {@inheritDoc} - Nemiroff
	 */
	@Override
	public void setPlayersOffline(final boolean online) {
		DB.insertUpdate("UPDATE players SET online=?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setBoolean(1, online);
				stmt.execute();
			}
		});
	}

	@Override
	public String getPlayerNameByObjId(final int playerObjId) {
		final String[] result = new String[1];
		DB.select("SELECT name FROM players WHERE id = ?", new ParamReadStH() {

			@Override
			public void handleRead(ResultSet arg0) throws SQLException {
				arg0.next();
				result[0] = arg0.getString("name");
			}

			@Override
			public void setParams(PreparedStatement arg0) throws SQLException {
				arg0.setInt(1, playerObjId);
			}
		});
		return result[0];
	}

	@Override
	public int getPlayerIdByName(final String playerName) {
		final int[] result = new int[1];
		DB.select("SELECT id FROM players WHERE name = ?", new ParamReadStH() {

			@Override
			public void handleRead(ResultSet arg0) throws SQLException {
				arg0.next();
				result[0] = arg0.getInt("id");
			}

			@Override
			public void setParams(PreparedStatement arg0) throws SQLException {
				arg0.setString(1, playerName);
			}
		});
		return result[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAccountIdByName(final String name) {
		Connection con = null;
		int accountId = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `account_id` FROM `players` WHERE `name` = ?");
			s.setString(1, name);
			ResultSet rs = s.executeQuery();
			rs.next();
			accountId = rs.getInt("account_id");
			rs.close();
			s.close();
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return accountId;
	}

	/**
	 * @author xTz
	 */
	@Override
	public void storePlayerName(final PlayerCommonData recipientCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players SET name=? WHERE id=?");

			log.debug("[DAO: MySQL5PlayerDAO] storing playerName " + recipientCommonData.getPlayerObjId() + " " + recipientCommonData.getName());

			stmt.setString(1, recipientCommonData.getName());
			stmt.setInt(2, recipientCommonData.getPlayerObjId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error saving playerName: " + recipientCommonData.getPlayerObjId() + " " + recipientCommonData.getName(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public int getCharacterCountOnAccount(final int accountId) {
		Connection con = null;
		int cnt = 0;

		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS cnt FROM `players` WHERE `account_id` = ? AND (players.deletion_date IS NULL || players.deletion_date > CURRENT_TIMESTAMP)");
			stmt.setInt(1, accountId);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			cnt = rs.getInt("cnt");
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}

		return cnt;
	}

	@Override
	public int getCharacterCountForRace(Race race) {
		Connection con = null;
		int count = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(DISTINCT(`account_name`)) AS `count` FROM `players` WHERE `race` = ? AND `exp` >= ?");
			stmt.setString(1, race.name());
			stmt.setLong(2, DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(GSConfig.RATIO_MIN_REQUIRED_LEVEL));
			ResultSet rs = stmt.executeQuery();
			rs.next();
			count = rs.getInt("count");
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}

		return count;
	}

	@Override
	public int getOnlinePlayerCount() {
		Connection con = null;
		int count = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS `count` FROM `players` WHERE `online` = ?");
			stmt.setBoolean(1, true);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			count = rs.getInt("count");
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}

		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Integer> getInactiveAccounts(final int daysOfInactivity, final int limitation) {
		String SELECT_QUERY = "SELECT account_id FROM players WHERE UNIX_TIMESTAMP(CURDATE())-UNIX_TIMESTAMP(last_online) > ? * 24 * 60 * 60";

		final Map<Integer, Integer> inactiveAccounts = FastMap.newInstance();

		DB.select(SELECT_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, daysOfInactivity);
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next() && (limitation == 0 || limitation > inactiveAccounts.size())) {
					int accountId = rset.getInt("account_id");
					// number of inactive chars on account
					Integer numberOfChars = 0;

					if ((numberOfChars = inactiveAccounts.get(accountId)) != null) {
						inactiveAccounts.put(accountId, numberOfChars + 1);
					}
					else {
						inactiveAccounts.put(accountId, 1);
					}
				}
			}
		});

		// filter accounts with active chars on them
		for (Iterator<Entry<Integer, Integer>> i = inactiveAccounts.entrySet().iterator(); i.hasNext();) {
			Entry<Integer, Integer> entry = i.next();

			// atleast one active char on account
			if (entry.getValue() < this.getCharacterCountOnAccount(entry.getKey())) {
				i.remove();
			}
		}

		return inactiveAccounts.keySet();
	}

	/**
	 * {@inheritDoc} - KID
	 */
	@Override
	public void setPlayerLastTransferTime(final int playerId, final long time) {
		DB.insertUpdate("UPDATE players SET last_transfer_time=? WHERE id=?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setLong(1, time);
				stmt.setInt(2, playerId);
				stmt.execute();
			}
		});
	}

	@Override
	public boolean updateBonusTime(final int playerObjId) {
		return DB.insertUpdate("UPDATE players SET bonus_type = 'NORMAL', bonus_buff_time = NULL WHERE `id` = ? and `bonus_buff_time` < CURRENT_TIMESTAMP", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, playerObjId);
				preparedStatement.execute();
			}
		});
	}

	@Override
	public Timestamp getCharacterCreationDateId(final int obj) {
		Connection con = null;
		Timestamp creationDate;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `creation_date` FROM `players` WHERE `id` = ?");
			s.setInt(1, obj);
			ResultSet rs = s.executeQuery();
			rs.next();
			creationDate = rs.getTimestamp("creation_date");
			rs.close();
			s.close();
		}
		catch (Exception e) {
			return null;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return creationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateLegionJoinRequestState(final int playerId, final LegionJoinRequestState state) {
		DB.insertUpdate("UPDATE players SET joinRequestState=? WHERE id=?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				log.debug("[DAO: MySQL5PlayerDAO] Update joinRequestState for player " + playerId + " to : " + state.name());

				stmt.setString(1, state.name());
				stmt.setInt(2, playerId);
				stmt.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearJoinRequest(final int playerId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players SET joinRequestLegionId=?, joinRequestState=? WHERE id=?");

			log.debug("[DAO: MySQL5PlayerDAO] Cleared LegionJoinRequest for player " + playerId);

			stmt.setInt(1, 0);
			stmt.setString(2, "NONE");
			stmt.setInt(3, playerId);
		}
		catch (Exception e) {

		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getJoinRequestState(final Player player) {
		String SELECT_QUERY = "SELECT * FROM players WHERE id=?";

		DB.select(SELECT_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, player.getObjectId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				if (rset.next()) {
					// log.info(" State: "+LegionJoinRequestState.valueOf(rset.getString("joinRequestState")).name());
					player.getCommonData().setJoinRequestState(LegionJoinRequestState.valueOf(rset.getString("joinRequestState")));
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
