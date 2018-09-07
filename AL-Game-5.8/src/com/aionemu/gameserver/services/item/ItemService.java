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
package com.aionemu.gameserver.services.item;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemSkillEnhance;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.RndArray;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;

/**
 * @author KID
 * @rework Blackfire
 */
public class ItemService {

	private static final Logger log = LoggerFactory.getLogger("ITEM_LOG");
	public static final ItemUpdatePredicate DEFAULT_UPDATE_PREDICATE = new ItemUpdatePredicate(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_ITEM_COLLECT);

	public static void loadItemStones(Collection<Item> itemList) {
		if (itemList != null && itemList.size() > 0) {
			DAOManager.getDAO(ItemStoneListDAO.class).load(itemList);
		}
	}

	public static long addItem(Player player, int itemId, long count) {
		return addItem(player, itemId, count, DEFAULT_UPDATE_PREDICATE);
	}

	public static long addItem(Player player, int itemId, long count, ItemUpdatePredicate predicate) {
		return addItem(player, itemId, count, null, predicate);
	}

	/**
	 * Add new item based on all sourceItem values
	 */
	public static long addItem(Player player, Item sourceItem) {
		return addItem(player, sourceItem.getItemId(), sourceItem.getItemCount(), sourceItem, DEFAULT_UPDATE_PREDICATE);
	}

	public static long addItem(Player player, Item sourceItem, ItemUpdatePredicate predicate) {
		return addItem(player, sourceItem.getItemId(), sourceItem.getItemCount(), sourceItem, predicate);
	}

	public static long addItem(Player player, int itemId, long count, Item sourceItem) {
		return addItem(player, itemId, count, sourceItem, DEFAULT_UPDATE_PREDICATE);
	}

	/**
	 * Add new item based on sourceItem values
	 */
	public static long addItem(Player player, int itemId, long count, Item sourceItem, ItemUpdatePredicate predicate) {
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (count <= 0 || itemTemplate == null) {
			return 0;
		}
		Preconditions.checkNotNull(itemTemplate, "No item with id " + itemId);
		Preconditions.checkNotNull(predicate, "Predicate is not supplied");

		if (LoggingConfig.LOG_ITEM) {
			log.info("[ITEM] ID/Count" + (LoggingConfig.ENABLE_ADVANCED_LOGGING ? "/Item Name - " + itemTemplate.getTemplateId() + "/" + count + "/" + itemTemplate.getName() : " - " + itemTemplate.getTemplateId() + "/" + count) + " to player " + player.getName());
		}

		Storage inventory = player.getInventory();
		if (itemTemplate.isKinah()) {
			// quests do not add here
			inventory.increaseKinah(count);
			return 0;
		}

		if (itemTemplate.isStackable()) {
			count = addStackableItem(player, itemTemplate, count, predicate);
		}
		else {
			count = addNonStackableItem(player, itemTemplate, count, sourceItem, predicate);
		}

		if (inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
		}
		return count;
	}

	/**
	 * Add non-stackable item to inventory
	 */
	private static long addNonStackableItem(Player player, ItemTemplate itemTemplate, long count, Item sourceItem, ItemUpdatePredicate predicate) {
		Storage inventory = player.getInventory();
		ItemSkillEnhance skillEnhance = DataManager.ITEM_SKILL_ENHANCE_DATA.getSkillEnhance(itemTemplate.getSkillEnhance());
		while (!inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
			Item newItem = ItemFactory.newItem(itemTemplate.getTemplateId());

			if (newItem.getExpireTime() != 0) {
				ExpireTimerTask.getInstance().addTask(newItem, player);
			}
			if (sourceItem != null) {
				copyItemInfo(sourceItem, newItem);
			}
            if (itemTemplate.getSkillEnhance() != 0) {
                newItem.setEnhanceSkillId(RndArray.get(skillEnhance.getSkillId()));
                newItem.setEnhanceEnchantLevel(1);
                newItem.setIsEnhance(true);
            }
			predicate.changeItem(newItem);
			inventory.add(newItem, predicate.getAddType());
			count--;
		}
		return count;
	}

	/**
	 * Copy some item values like item stones and enchant level
	 */
	private static void copyItemInfo(Item sourceItem, Item newItem) {
		newItem.setOptionalSocket(sourceItem.getOptionalSocket());
		newItem.setItemCreator(sourceItem.getItemCreator());
		if (sourceItem.hasManaStones()) {
			for (ManaStone manaStone : sourceItem.getItemStones()) {
				ItemSocketService.addManaStone(newItem, manaStone.getItemId());
			}
		}
		if (sourceItem.getGodStone() != null) {
			newItem.addGodStone(sourceItem.getGodStone().getItemId());
		}
		if (sourceItem.getEnchantLevel() > 0) {
			newItem.setEnchantLevel(sourceItem.getEnchantLevel());
		}
		if (sourceItem.isSoulBound()) {
			newItem.setSoulBound(true);
		}
		newItem.setBonusNumber(sourceItem.getBonusNumber());
		newItem.setRandomStats(sourceItem.getRandomStats());
		newItem.setRandomCount(sourceItem.getRandomCount());
		newItem.setIdianStone(sourceItem.getIdianStone());
		newItem.setItemColor(sourceItem.getItemColor());
		newItem.setItemSkinTemplate(sourceItem.getItemSkinTemplate());
		newItem.setIsEnhance(sourceItem.isEnhance());
	}

	/**
	 * Add stackable item to inventory
	 */
	private static long addStackableItem(Player player, ItemTemplate itemTemplate, long count, ItemUpdatePredicate predicate) {
		Storage inventory = player.getInventory();
		Collection<Item> items = inventory.getItemsByItemId(itemTemplate.getTemplateId());
		for (Item item : items) {
			if (count == 0) {
				break;
			}
			count = inventory.increaseItemCount(item, count, predicate.getUpdateType(item, true));
		}

		// If Power Shard's are Equiped and there are no Power Shard's in Inventory / or max Stack is reached (in Inventory) they get added to Equiped Power Shard's
		if (itemTemplate.getCategory() == ItemCategory.SHARD) {
			Equipment equipment = player.getEquipment();
			items = equipment.getEquippedItemsByItemId(itemTemplate.getTemplateId());
			for (Item item : items) {
				if (count == 0) {
					break;
				}
				count = equipment.increaseEquippedItemCount(item, count);
			}
		}

		while (!inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
			Item newItem = ItemFactory.newItem(itemTemplate.getTemplateId(), count);
			count -= newItem.getItemCount();
			inventory.add(newItem, predicate.getAddType());
		}
		return count;
	}

	public static boolean addQuestItems(Player player, List<QuestItems> questItems) {
		return addQuestItems(player, questItems, DEFAULT_UPDATE_PREDICATE);
	}

	public static boolean addQuestItems(Player player, List<QuestItems> questItems, ItemUpdatePredicate predicate) {
		int slotReq = 0, specialSlot = 0;

		for (QuestItems qi : questItems) {
			if (qi.getItemId() != ItemId.KINAH.value() && qi.getCount() != 0) {
				ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(qi.getItemId());
				long stackCount = template.getMaxStackCount();
				long count = qi.getCount() / stackCount;
				if (qi.getCount() % stackCount != 0) {
					count++;
				}
				if (template.getExtraInventoryId() > 0) {
					specialSlot += count;
				}
				else {
					slotReq += count;
				}
			}
		}
		Storage inventory = player.getInventory();
		if (slotReq > 0 && inventory.getFreeSlots() < slotReq) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
			return false;
		}
		if (specialSlot > 0 && inventory.getSpecialCubeFreeSlots() < specialSlot) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
			return false;
		}
		for (QuestItems qi : questItems) {
			addItem(player, qi.getItemId(), qi.getCount(), predicate);
		}
		return true;
	}

	public static void releaseItemId(Item item) {
		IDFactory.getInstance().releaseId(item.getObjectId());
	}

	public static void releaseItemIds(Collection<Item> items) {
		Collection<Integer> idIterator = Collections2.transform(items, AionObject.OBJECT_TO_ID_TRANSFORMER);
		IDFactory.getInstance().releaseIds(idIterator);
	}

	public static class ItemUpdatePredicate {

		private final ItemUpdateType itemUpdateType;
		private final ItemAddType itemAddType;

		public ItemUpdatePredicate(ItemAddType itemAddType, ItemUpdateType itemUpdateType) {
			this.itemUpdateType = itemUpdateType;
			this.itemAddType = itemAddType;
		}

		public ItemUpdatePredicate() {
			this(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_ITEM_COLLECT);
		}

		public ItemUpdateType getUpdateType(Item item, boolean isIncrease) {
			if (item.getItemTemplate().isKinah()) {
				return ItemUpdateType.getKinahUpdateTypeFromAddType(itemAddType, isIncrease);
			}
			return itemUpdateType;
		}

		public ItemAddType getAddType() {
			return itemAddType;
		}

		/**
		 * @param item
		 */
		public boolean changeItem(Item item) {
			return true;
		}
	}

	public static boolean dropItemToInventory(int playerObjectId, int itemId) {
		return dropItemToInventory(World.getInstance().findPlayer(playerObjectId), itemId);
	}

	public static boolean dropItemToInventory(Player player, int itemId) {
		if (player == null || !player.isOnline()) {
			return false;
		}

		Storage storage = player.getInventory();
		if (storage.getFreeSlots() < 1) {
			List<Item> items = storage.getItemsByItemId(itemId);
			boolean hasFreeStack = false;
			for (Item item : items) {
				if (item.getPersistentState() == PersistentState.DELETED || item.getItemCount() < item.getItemTemplate().getMaxStackCount()) {
					hasFreeStack = true;
					break;
				}
			}
			if (!hasFreeStack) {
				return false;
			}
		}
		// TODO: check the exact type in retail
		return addItem(player, itemId, 1, new ItemUpdatePredicate(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_CASH_ITEM)) == 0;
	}

	public static boolean checkRandomTemplate(int randomItemId) {
		ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(randomItemId);
		return template != null;
	}

	/**
	 * @param object
	 * @param unk
	 * @param unk1
	 * @param unk2
	 */
	public static Item newItem(int resultItemId, int count, Object object, int unk, int unk1, int unk2) {
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(resultItemId);
		if (count <= 0 || itemTemplate == null) {
			return null;
		}
		Preconditions.checkNotNull(itemTemplate, "No item with id " + resultItemId);

		Item newItem = ItemFactory.newItem(itemTemplate.getTemplateId());

		return newItem;
	}

	public static void makeUpgradeItem(Player player, Item sourceItem, Item newItem) {
		Storage inventory = player.getInventory();
		newItem.setOptionalSocket(sourceItem.getOptionalSocket());
		int enchantLevel = sourceItem.getEnchantLevel();
		
		if (sourceItem.getFusionedItemId() != 0) {
			newItem.setFusionedItem(sourceItem.getFusionedItemTemplate());
		}

		if (sourceItem.hasManaStones()) {
			ItemSocketService.copyManaStones(sourceItem, newItem);
		}

		if (sourceItem.hasGodStone()) {
			newItem.addGodStone(sourceItem.getGodStone().getItemId());
		}
		
		if (sourceItem.getItemTemplate().isPlume()) {
			newItem.setAuthorize(1);
			newItem.setEnchantLevel(0);
		} else {
			if (enchantLevel >= 20) {
				newItem.setEnchantLevel(enchantLevel - 5);
				newItem.setAmplificationSkill(sourceItem.getAmplificationSkill());
				newItem.setAmplified(true);
			}
			else {
				newItem.setEnchantLevel(enchantLevel);
			}
		}

		if (sourceItem.isSoulBound()) {
			newItem.setSoulBound(true);
		}

		if (sourceItem.getBonusNumber() > 0) {
			newItem.setBonusNumber(sourceItem.getBonusNumber());
			newItem.setRandomStats(sourceItem.getRandomStats());
			newItem.setRandomCount(sourceItem.getRandomCount());
		}

		ItemUpdatePredicate predicate = DEFAULT_UPDATE_PREDICATE;
		predicate.changeItem(newItem);
		inventory.add(newItem, predicate.getAddType());
	}
}
