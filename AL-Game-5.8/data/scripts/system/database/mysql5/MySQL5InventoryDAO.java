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
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.utils.GenericValidator;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.PlayerStorage;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.services.item.ItemService;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import javolution.util.FastList;

/**
 * @author ATracer
 */
public class MySQL5InventoryDAO extends InventoryDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5InventoryDAO.class);
	public static final String SELECT_QUERY = "SELECT `item_unique_id`, `item_id`, `item_count`, `item_color`, `color_expires`, `item_creator`, `expire_time`, `activation_count`, `is_equiped`, `is_soul_bound`, `slot`, `enchant`, `item_skin`, `fusioned_item`, `optional_socket`, `optional_fusion_socket`, `charge`, `rnd_bonus`, `rnd_count`, `pack_count`, `authorize`, `is_packed`, `is_amplified`, `buff_skill`, `reduction_level`, `luna_reskin`, `isEnhance`, `enhanceSkillId`, `enhanceSkillEnchant` FROM `inventory` WHERE `item_owner`=? AND `item_location`=? AND `is_equiped`=?";
	public static final String INSERT_QUERY = "INSERT INTO `inventory` (`item_unique_id`, `item_id`, `item_count`, `item_color`, `color_expires`, `item_creator`, `expire_time`, `activation_count`, `item_owner`, `is_equiped`, is_soul_bound, `slot`, `item_location`, `enchant`, `item_skin`, `fusioned_item`, `optional_socket`, `optional_fusion_socket`, `charge`, `rnd_bonus`, `rnd_count`, `pack_count`, `authorize`, `is_packed`, `is_amplified`, `buff_skill`, `reduction_level`, `luna_reskin`, `isEnhance`, `enhanceSkillId`, `enhanceSkillEnchant`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE inventory SET  item_count=?, item_color=?, color_expires=?, item_creator=?, expire_time=?, activation_count=?,item_owner=?, is_equiped=?, is_soul_bound=?, slot=?, item_location=?, enchant=?, item_skin=?, fusioned_item=?, optional_socket=?, optional_fusion_socket=?, charge=?, rnd_bonus=?, rnd_count=?, pack_count=?, authorize=?, is_packed=?, is_amplified=?, buff_skill=?, reduction_level=?, luna_reskin=?, isEnhance=?, enhanceSkillId=?, enhanceSkillEnchant=? WHERE item_unique_id=?";
	public static final String DELETE_QUERY = "DELETE FROM inventory WHERE item_unique_id=?";
	public static final String DELETE_CLEAN_QUERY = "DELETE FROM inventory WHERE item_owner=? AND item_location != 2"; // legion warehouse needs not to be excluded, since players and legions are IDAwareDAOs
	public static final String SELECT_ACCOUNT_QUERY = "SELECT `account_id` FROM `players` WHERE `id`=?";
	public static final String SELECT_LEGION_QUERY = "SELECT `legion_id` FROM `legion_members` WHERE `player_id`=?";
	public static final String DELETE_ACCOUNT_WH = "DELETE FROM inventory WHERE item_owner=? AND item_location=2";
	public static final String SELECT_QUERY2 = "SELECT * FROM `inventory` WHERE `item_owner`=? AND `item_location`=?";
	private static final Predicate<Item> itemsToInsertPredicate = new Predicate<Item>() {

		@Override
		public boolean apply(@Nullable Item input) {
			return input != null && PersistentState.NEW == input.getPersistentState();
		}
	};
	private static final Predicate<Item> itemsToUpdatePredicate = new Predicate<Item>() {

		@Override
		public boolean apply(@Nullable Item input) {
			return input != null && PersistentState.UPDATE_REQUIRED == input.getPersistentState();
		}
	};
	private static final Predicate<Item> itemsToDeletePredicate = new Predicate<Item>() {

		@Override
		public boolean apply(@Nullable Item input) {
			return input != null && PersistentState.DELETED == input.getPersistentState();
		}
	};

	@Override
	public Storage loadStorage(int playerId, StorageType storageType) {
		final Storage inventory = new PlayerStorage(storageType);
		final int storage = storageType.getId();
		final int equipped = 0;

		if (storageType == StorageType.ACCOUNT_WAREHOUSE) {
			playerId = loadPlayerAccountId(playerId);
		}

		final int owner = playerId;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, owner);
			stmt.setInt(2, storage);
			stmt.setInt(3, equipped);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				Item item = constructItem(storage, rset);
				item.setPersistentState(PersistentState.UPDATED);
				if (item.getItemTemplate() == null) {
					log.error(playerId + "loaded error item, itemUniqueId is: " + item.getObjectId());
				}
				else {
					inventory.onLoadHandler(item);
				}
			}
			rset.close();
		}
		catch (Exception e) {
			log.error("Could not restore storage data for player: " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		return inventory;
	}

	@Override
	public List<Item> loadStorageDirect(int playerId, StorageType storageType) {
		List<Item> list = FastList.newInstance();
		final int storage = storageType.getId();

		if (storageType == StorageType.ACCOUNT_WAREHOUSE) {
			playerId = loadPlayerAccountId(playerId);
		}

		final int owner = playerId;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY2);
			stmt.setInt(1, owner);
			stmt.setInt(2, storageType.getId());
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				list.add(constructItem(storage, rset));
			}
			rset.close();
		}
		catch (Exception e) {
			log.error("Could not restore loadStorageDirect data for player: " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
		return list;
	}

	@Override
	public Equipment loadEquipment(Player player) {
		final Equipment equipment = new Equipment(player);

		int playerId = player.getObjectId();
		final int storage = 0;
		final int equipped = 1;

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, storage);
			stmt.setInt(3, equipped);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				Item item = constructItem(storage, rset);
				item.setPersistentState(PersistentState.UPDATED);
				equipment.onLoadHandler(item);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore Equipment data for player: " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return equipment;
	}

	@Override
	public List<Item> loadEquipment(int playerId) {
		final List<Item> items = new ArrayList<Item>();
		final int storage = 0;
		final int equipped = 1;

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, storage);
			stmt.setInt(3, equipped);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				Item item = constructItem(storage, rset);
				items.add(item);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore Equipment data for player: " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return items;
	}

	private Item constructItem(final int storage, ResultSet rset) throws SQLException {
		int itemUniqueId = rset.getInt("item_unique_id");
		int itemId = rset.getInt("item_id");
		long itemCount = rset.getLong("item_count");
		int itemColor = rset.getInt("item_color");
		int colorExpireTime = rset.getInt("color_expires");
		String itemCreator = rset.getString("item_creator");
		int expireTime = rset.getInt("expire_time");
		int activationCount = rset.getInt("activation_count");
		int isEquiped = rset.getInt("is_equiped");
		int isSoulBound = rset.getInt("is_soul_bound");
		long slot = rset.getLong("slot");
		int enchant = rset.getInt("enchant");
		int itemSkin = rset.getInt("item_skin");
		int fusionedItem = rset.getInt("fusioned_item");
		int optionalSocket = rset.getInt("optional_socket");
		int optionalFusionSocket = rset.getInt("optional_fusion_socket");
		int charge = rset.getInt("charge");
		int randomBonus = rset.getInt("rnd_bonus");
		int rndCount = rset.getInt("rnd_count");
		int packCount = rset.getInt("pack_count");
		int isPacked = rset.getInt("is_packed");
		int max_authorize = rset.getInt("authorize");
		int isAmplified = rset.getInt("is_amplified");
		int amplificationSkill = rset.getInt("buff_skill");
		int reductionLevel = rset.getInt("reduction_level");
		int isLunaReskin = rset.getInt("luna_reskin");
		boolean isEnhance = rset.getBoolean("isEnhance");
		int enhanceSkillId = rset.getInt("enhanceSkillId");
		int enhanceSkillEnchant = rset.getInt("enhanceSkillEnchant");
		Item item = new Item(itemUniqueId, itemId, itemCount, itemColor, colorExpireTime, itemCreator, expireTime, activationCount, isEquiped == 1, isSoulBound == 1, slot, storage, enchant, itemSkin, fusionedItem, optionalSocket, optionalFusionSocket, charge, randomBonus, rndCount, packCount, max_authorize, isPacked == 1, isAmplified == 1, amplificationSkill, reductionLevel, isLunaReskin == 1, isEnhance, enhanceSkillId, enhanceSkillEnchant);
		return item;
	}

	private int loadPlayerAccountId(final int playerId) {
		Connection con = null;
		int accountId = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_ACCOUNT_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			if (rset.next()) {
				accountId = rset.getInt("account_id");
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore accountId data for player: " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return accountId;
	}

	public int loadLegionId(final int playerId) {
		Connection con = null;
		int legionId = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGION_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			if (rset.next()) {
				legionId = rset.getInt("legion_id");
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Failed to load legion id for player id: " + playerId, e);
		}
		finally {
			DatabaseFactory.close(con);
		}

		return legionId;
	}

	@Override
	public boolean store(Player player) {
		int playerId = player.getObjectId();
		Integer accountId = player.getPlayerAccount() != null ? player.getPlayerAccount().getId() : null;
		Integer legionId = player.getLegion() != null ? player.getLegion().getLegionId() : null;

		List<Item> allPlayerItems = player.getDirtyItemsToUpdate();
		return store(allPlayerItems, playerId, accountId, legionId);
	}

	@Override
	public boolean store(Item item, Player player) {
		int playerId = player.getObjectId();
		int accountId = player.getPlayerAccount().getId();
		Integer legionId = player.getLegion() != null ? player.getLegion().getLegionId() : null;

		return store(item, playerId, accountId, legionId);
	}

	@Override
	public boolean store(List<Item> items, int playerId) {

		Integer accountId = null;
		Integer legionId = null;

		for (Item item : items) {

			if (accountId == null && item.getItemLocation() == StorageType.ACCOUNT_WAREHOUSE.getId()) {
				accountId = loadPlayerAccountId(playerId);
			}

			if (legionId == null && item.getItemLocation() == StorageType.LEGION_WAREHOUSE.getId()) {
				int localLegionId = loadLegionId(playerId);
				if (localLegionId > 0) {
					legionId = localLegionId;
				}
			}
		}

		return store(items, playerId, accountId, legionId);
	}

	@Override
	public boolean store(List<Item> items, Integer playerId, Integer accountId, Integer legionId) {
		Collection<Item> itemsToUpdate = Collections2.filter(items, itemsToUpdatePredicate);
		Collection<Item> itemsToInsert = Collections2.filter(items, itemsToInsertPredicate);
		Collection<Item> itemsToDelete = Collections2.filter(items, itemsToDeletePredicate);

		boolean deleteResult = false;
		boolean insertResult = false;
		boolean updateResult = false;

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			con.setAutoCommit(false);
			deleteResult = deleteItems(con, itemsToDelete);
			insertResult = insertItems(con, itemsToInsert, playerId, accountId, legionId);
			updateResult = updateItems(con, itemsToUpdate, playerId, accountId, legionId);
		}
		catch (SQLException e) {
			log.error("Can't open connection to save player inventory: " + playerId);
		}
		finally {
			DatabaseFactory.close(con);
		}

		for (Item item : items) {
			item.setPersistentState(PersistentState.UPDATED);
		}

		if (!itemsToDelete.isEmpty() && deleteResult) {
			ItemService.releaseItemIds(itemsToDelete);
		}

		return deleteResult && insertResult && updateResult;
	}

	private int getItemOwnerId(Item item, Integer playerId, Integer accountId, Integer legionId) {
		if (item.getItemLocation() == StorageType.ACCOUNT_WAREHOUSE.getId()) {
			return accountId;
		}

		if (item.getItemLocation() == StorageType.LEGION_WAREHOUSE.getId()) {
			return legionId != null ? legionId : playerId;
		}

		return playerId;
	}

	private boolean insertItems(Connection con, Collection<Item> items, Integer playerId, Integer accountId, Integer legionId) {

		if (GenericValidator.isBlankOrNull(items)) {
			return true;
		}

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(INSERT_QUERY);

			for (Item item : items) {
				stmt.setInt(1, item.getObjectId());
				stmt.setInt(2, item.getItemTemplate().getTemplateId());
				stmt.setLong(3, item.getItemCount());
				stmt.setInt(4, item.getItemColor());
				stmt.setInt(5, item.getColorExpireTime());
				stmt.setString(6, item.getItemCreator());
				stmt.setInt(7, item.getExpireTime());
				stmt.setInt(8, item.getActivationCount());
				stmt.setInt(9, getItemOwnerId(item, playerId, accountId, legionId));
				stmt.setBoolean(10, item.isEquipped());
				stmt.setInt(11, item.isSoulBound() ? 1 : 0);
				stmt.setLong(12, item.getEquipmentSlot());
				stmt.setInt(13, item.getItemLocation());
				stmt.setInt(14, item.getEnchantLevel());
				stmt.setInt(15, item.getItemSkinTemplate().getTemplateId());
				stmt.setInt(16, item.getFusionedItemId());
				stmt.setInt(17, item.getOptionalSocket());
				stmt.setInt(18, item.getOptionalFusionSocket());
				stmt.setInt(19, item.getChargePoints());
				stmt.setInt(20, item.getBonusNumber());
				stmt.setInt(21, item.getRandomCount());
				stmt.setInt(22, item.getPackCount());
				stmt.setInt(23, item.getAuthorize());
				stmt.setBoolean(24, item.isPacked());
				stmt.setBoolean(25, item.isAmplified());
				stmt.setInt(26, item.getAmplificationSkill());
				stmt.setInt(27, item.getReductionLevel());
				stmt.setBoolean(28, item.isLunaReskin());
				stmt.setBoolean(29, item.isEnhance());
				stmt.setInt(30, item.getEnhanceSkillId());
				stmt.setInt(31, item.getEnhanceEnchantLevel());
				stmt.addBatch();
			}

			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e) {
			log.error("Failed to execute insert batch", e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt);
		}
		return true;
	}

	private boolean updateItems(Connection con, Collection<Item> items, Integer playerId, Integer accountId, Integer legionId) {

		if (GenericValidator.isBlankOrNull(items)) {
			return true;
		}

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(UPDATE_QUERY);

			for (Item item : items) {
				stmt.setLong(1, item.getItemCount());
				stmt.setInt(2, item.getItemColor());
				stmt.setInt(3, item.getColorExpireTime());
				stmt.setString(4, item.getItemCreator());
				stmt.setInt(5, item.getExpireTime());
				stmt.setInt(6, item.getActivationCount());
				stmt.setInt(7, getItemOwnerId(item, playerId, accountId, legionId));
				stmt.setBoolean(8, item.isEquipped());
				stmt.setInt(9, item.isSoulBound() ? 1 : 0);
				stmt.setLong(10, item.getEquipmentSlot());
				stmt.setInt(11, item.getItemLocation());
				stmt.setInt(12, item.getEnchantLevel());
				stmt.setInt(13, item.getItemSkinTemplate().getTemplateId());
				stmt.setInt(14, item.getFusionedItemId());
				stmt.setInt(15, item.getOptionalSocket());
				stmt.setInt(16, item.getOptionalFusionSocket());
				stmt.setInt(17, item.getChargePoints());
				stmt.setInt(18, item.getBonusNumber());
				stmt.setInt(19, item.getRandomCount());
				stmt.setInt(20, item.getPackCount());
				stmt.setInt(21, item.getAuthorize());
				stmt.setBoolean(22, item.isPacked());
				stmt.setBoolean(23, item.isAmplified());
				stmt.setInt(24, item.getAmplificationSkill());
				stmt.setInt(25, item.getReductionLevel());
				stmt.setBoolean(26, item.isLunaReskin());
				stmt.setBoolean(27, item.isEnhance());
				stmt.setInt(28, item.getEnhanceSkillId());
				stmt.setInt(29, item.getEnhanceEnchantLevel());
				stmt.setInt(30, item.getObjectId());
				stmt.addBatch();
			}

			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e) {
			log.error("Failed to execute update batch", e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt);
		}
		return true;
	}

	private boolean deleteItems(Connection con, Collection<Item> items) {

		if (GenericValidator.isBlankOrNull(items)) {
			return true;
		}

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(DELETE_QUERY);
			for (Item item : items) {
				stmt.setInt(1, item.getObjectId());
				stmt.addBatch();
			}

			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e) {
			log.error("Failed to execute delete batch", e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt);
		}
		return true;
	}

	/**
	 * Since inventory is not using FK - need to clean items
	 */
	@Override
	public boolean deletePlayerItems(final int playerId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_CLEAN_QUERY);
			stmt.setInt(1, playerId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error Player all items. PlayerObjId: " + playerId, e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public void deleteAccountWH(final int accountId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_ACCOUNT_WH);
			stmt.setInt(1, accountId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error deleting all items from account WH. AccountId: " + accountId, e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public int[] getUsedIDs() {
		PreparedStatement statement = DB.prepareStatement("SELECT item_unique_id FROM inventory", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		try {
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				ids[i] = rs.getInt("item_unique_id");
			}
			return ids;
		}
		catch (SQLException e) {
			log.error("Can't get list of id's from inventory table", e);
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
}
