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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.stats.listeners.ItemEquipmentListener;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.model.templates.itemset.ItemSetTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.StigmaService;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

import javolution.util.FastList;

/**
 * @author Avol, ATracer, kosyachok
 * @modified cura
 */
public class Equipment {

	private static final Logger log = LoggerFactory.getLogger(Equipment.class);
	private SortedMap<Long, Item> equipment = new TreeMap<Long, Item>();
	private Player owner;
	private Set<Long> markedFreeSlots = new HashSet<Long>();
	private PersistentState persistentState = PersistentState.UPDATED;
	private static final long[] ARMOR_SLOTS = new long[] { ItemSlot.BOOTS.getSlotIdMask(), ItemSlot.GLOVES.getSlotIdMask(), ItemSlot.PANTS.getSlotIdMask(), ItemSlot.SHOULDER.getSlotIdMask(), ItemSlot.TORSO.getSlotIdMask() };

	public Equipment(Player player) {
		this.owner = player;
	}

	/**
	 * @param itemUniqueId
	 * @param slot
	 * @return item or null in case of failure
	 */
	public Item equipItem(int itemUniqueId, long slot) {
		Item item = owner.getInventory().getItemByObjId(itemUniqueId);

		if (item == null) {
			return null;
		}

		ItemTemplate itemTemplate = item.getItemTemplate();

		if (item.getItemTemplate().isClassSpecific(owner.getCommonData().getPlayerClass()) == false) {
			// Your Class cannot use the selected item
			PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_INVALID_CLASS);
			return null;
		}
		int requiredLevel = item.getItemTemplate().getRequiredLevel(owner.getCommonData().getPlayerClass()) - item.getReductionLevel();
		if (requiredLevel == -1 || requiredLevel > owner.getLevel()) {
			// You cannot use %1 until you reach level %0
			PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_LOW_LEVEL_MUST_BE_THIS_LEVEL(item.getNameId(), itemTemplate.getLevel()));
			return null;
		}
		if (itemTemplate.getRace() != Race.PC_ALL && itemTemplate.getRace() != owner.getRace()) {
			// Your race cannot use this item
			PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_INVALID_RACE);
			return null;
		}
		ItemUseLimits limits = itemTemplate.getUseLimits();
		if (limits.getGenderPermitted() != null && limits.getGenderPermitted() != owner.getGender()) {
			// This item cannot be used by your gender
			PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_INVALID_GENDER);
			return null;
		}
		if (!verifyRankLimits(item)) {
			// You cannot use the selected item until you reach the %0 rank
			PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_INVALID_RANK(AbyssRankEnum.getRankById(limits.getMinRank()).getDescriptionId()));
			return null;
		}
		long itemSlotToEquip = 0;

		synchronized (equipment) {
			markedFreeSlots.clear();

			// validate item against current equipment and mark free slots
			long oldSlot = item.getEquipmentSlot();
			item.setEquipmentSlot(slot);
			switch (item.getEquipmentType()) {
				case ARMOR:
					if (!validateEquippedArmor(item, true)) {
						item.setEquipmentSlot(oldSlot);
						return null;
					}
					break;
				case WEAPON:
					if (!validateEquippedWeapon(item, true)) {
						item.setEquipmentSlot(oldSlot);
						return null;
					}
					break;
				default:
					break;
			}

			// check whether there is already item in specified slot
			long itemSlotMask = 0;
			switch (item.getItemTemplate().getCategory()) {
				case STIGMA:
				case ESTIMA:
					itemSlotMask = slot;
					break;
				default:
					itemSlotMask = itemTemplate.getItemSlot();
					break;
			}

			ItemSlot[] possibleSlots = ItemSlot.getSlotsFor(itemSlotMask);
			// find correct slot
			for (int i = 0; i < possibleSlots.length; i++) {
				ItemSlot possibleSlot = possibleSlots[i];
				long slotId = possibleSlot.getSlotIdMask();
				if (equipment.get(slotId) == null || markedFreeSlots.contains(slotId)) {
					if (item.getItemTemplate().isTwoHandWeapon()) {
						itemSlotMask &= ~slotId;
					}
					else {
						itemSlotToEquip = slotId;
						break;
					}
				}
			}
			if (item.getItemTemplate().isTwoHandWeapon()) {
				if (itemSlotMask != 0) {
					return null;
				}
				itemSlotToEquip = itemTemplate.getItemSlot();
			}

			if (!StigmaService.notifyEquipAction(owner, item, slot)) {
				return null;
			}

			// equip first occupied slot if there is no free
			if (itemSlotToEquip == 0) {
				itemSlotToEquip = possibleSlots[0].getSlotIdMask();
			}
		}

		if (itemSlotToEquip == 0) {
			return null;
		}

		if (itemTemplate.isSoulBound() && !item.isSoulBound()) {
			soulBindItem(owner, item, itemSlotToEquip);
			return null;
		}

		return equip(itemSlotToEquip, item);
	}

	/**
	 * @param itemSlotToEquip
	 *            - must be slot combination for dual weapons
	 * @param item
	 */
	private Item equip(long itemSlotToEquip, Item item) {
		if (item.getOptionalSocket() == -1) {
			log.warn("item can't be equiped because hasTune" + item.getObjectId());
			return null;
		}
		synchronized (equipment) {
			ItemSlot[] allSlots = ItemSlot.getSlotsFor(itemSlotToEquip);
			if (allSlots.length > 1 && !item.getItemTemplate().isTwoHandWeapon()) {
				throw new IllegalArgumentException("itemSlotToEquip can not be composite!");
			}

			// remove item first from inventory to have at least one slot free
			owner.getInventory().remove(item);

			// do unequip of necessary items
			Item equippedItem = equipment.get(allSlots[0].getSlotIdMask());
			if (equippedItem != null) {
				if (equippedItem.getItemTemplate().isTwoHandWeapon()) {
					unEquip(equippedItem.getEquipmentSlot());
				}
				else {
					for (ItemSlot slot : allSlots) {
						unEquip(slot.getSlotIdMask());
					}
				}
			}

			switch (item.getEquipmentType()) {
				case ARMOR:
					validateEquippedArmor(item, false);
					break;
				case WEAPON:
					validateEquippedWeapon(item, false);
					break;
				default:
					break;
			}

			if (equipment.get(allSlots[0].getSlotIdMask()) != null) {
				log.error("CHECKPOINT : putting item to already equiped slot. Info slot: " + itemSlotToEquip + " new item: " + item.getItemTemplate().getTemplateId() + " old item: " + equipment.get(allSlots[0].getSlotIdMask()).getItemTemplate().getTemplateId());
				return null;
			}

			// equip target item
			for (ItemSlot slot : allSlots) {
				equipment.put(slot.getSlotIdMask(), item);
			}
			item.setEquipped(true);
			item.setEquipmentSlot(itemSlotToEquip);
			ItemPacketService.updateItemAfterEquip(owner, item);

			// update stats
			notifyItemEquipped(item);
			owner.getLifeStats().updateCurrentStats();
			setPersistentState(PersistentState.UPDATE_REQUIRED);
			QuestEngine.getInstance().onEquipItem(new QuestEnv(null, owner, 0, 0), item.getItemId());

			return item;
		}
	}

	private void notifyItemEquipped(Item item) {
		ItemEquipmentListener.onItemEquipment(item, owner);
		owner.getObserveController().notifyItemEquip(item, owner);
		tryUpdateSummonStats();
	}

	private void notifyItemUnequip(Item item) {
		ItemEquipmentListener.onItemUnequipment(item, owner);
		owner.getObserveController().notifyItemUnEquip(item, owner);
		tryUpdateSummonStats();
	}

	private void tryUpdateSummonStats() {
		Summon summon = owner.getSummon();
		if (summon != null) {
			summon.getGameStats().updateStatsAndSpeedVisually();
		}
	}

	/**
	 * Called when CM_EQUIP_ITEM packet arrives with action 1
	 *
	 * @param itemUniqueId
	 * @param slot
	 * @return item or null in case of failure
	 */
	public Item unEquipItem(int itemUniqueId, long slot) {
		// if inventory is full unequip action is disabled
		if (owner.getInventory().isFull()) {
			PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_UI_INVENTORY_FULL);
			return null;
		}

		synchronized (equipment) {
			Item itemToUnequip = null;

			for (Item item : equipment.values()) {
				if (item.getObjectId() == itemUniqueId) {
					itemToUnequip = item;
					break;
				}
			}

			if (itemToUnequip == null || !itemToUnequip.isEquipped()) {
				return null;
			}

			// Looks very odd - but its retail like
			if (itemToUnequip.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask()) {
				Item ohWeapon = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
				if (ohWeapon != null && ohWeapon.getItemTemplate().isWeapon()) {
					if (owner.getInventory().getFreeSlots() < 2) {
						return null;
					}
					unEquip(ItemSlot.SUB_HAND.getSlotIdMask());
				}
			}

			// if unequip power shard
			if (itemToUnequip.getItemTemplate().isArmor() && itemToUnequip.getItemTemplate().getCategory() == ItemCategory.SHARD) {
				owner.unsetState(CreatureState.POWERSHARD);
				PacketSendUtility.sendPacket(owner, new SM_EMOTION(owner, EmotionType.POWERSHARD_OFF, 0, 0));
			}

			if (!StigmaService.notifyUnequipAction(owner, itemToUnequip)) {
				return null;
			}

			unEquip(itemToUnequip.getEquipmentSlot());

			return itemToUnequip;
		}
	}

	/**
	 * @param slot
	 *            - Must be composite for dual weapons
	 */
	private void unEquip(long slot) {
		ItemSlot[] allSlots = ItemSlot.getSlotsFor(slot);
		Item item = equipment.remove(allSlots[0].getSlotIdMask());
		if (item == null) { // NPE check, there is no item in the given slot.
			return;
		}
		if (allSlots.length > 1) {
			if (!item.getItemTemplate().isTwoHandWeapon()) {
				equipment.put(allSlots[0].getSlotIdMask(), item);
				throw new IllegalArgumentException("slot can not be composite!");
			}
			equipment.remove(allSlots[1].getSlotIdMask());
		}

		item.setEquipped(false);
		item.setEquipmentSlot(0);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		notifyItemUnequip(item);
		owner.getLifeStats().updateCurrentStats();
		owner.getGameStats().updateStatsAndSpeedVisually();
		owner.getInventory().put(item);
	}

	/**
	 * TODO: Move to SkillEngine Use skill stack SKILL_P_EQUIP_DUAL to check that instead
	 *
	 * @return true if player can equip two one-handed weapons
	 */
	private boolean hasDualWieldingSkills() {
		return owner.getSkillList().isSkillPresent(55) || owner.getSkillList().isSkillPresent(171) || owner.getSkillList().isSkillPresent(143) || owner.getSkillList().isSkillPresent(144) || owner.getSkillList().isSkillPresent(207);
	}

	/**
	 * Used during equip process and analyzes equipped slots
	 *
	 * @param item
	 * @param itemInMainHand
	 * @param itemInSubHand
	 * @return
	 */
	private boolean validateEquippedWeapon(Item item, boolean validateOnly) {
		// Disable arrow equipment
		if (item.getItemTemplate().getArmorType() == ArmorType.ARROW) {
			return false;
		}

		// check present skill
		int[] requiredSkills = item.getItemTemplate().getWeaponType().getRequiredSkills();

		if (!checkAvailableEquipSkills(requiredSkills)) {
			return false;
		}

		Item itemInRightHand, itemInLeftHand;
		long rightSlot, leftSlot;
		if ((item.getEquipmentSlot() & ItemSlot.MAIN_OR_SUB.getSlotIdMask()) != 0) {
			rightSlot = ItemSlot.MAIN_HAND.getSlotIdMask();
			leftSlot = ItemSlot.SUB_HAND.getSlotIdMask();
			itemInRightHand = equipment.get(rightSlot);
			itemInLeftHand = equipment.get(leftSlot);
		}
		else if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_OR_SUB_OFF.getSlotIdMask()) != 0) {
			rightSlot = ItemSlot.MAIN_OFF_HAND.getSlotIdMask();
			leftSlot = ItemSlot.SUB_OFF_HAND.getSlotIdMask();
			itemInRightHand = equipment.get(rightSlot);
			itemInLeftHand = equipment.get(leftSlot);
		}
		else {
			return false;
		}

		// for dual weapons, they occupy two slots, so check if the same item
		if (itemInRightHand == itemInLeftHand) {
			itemInLeftHand = null;
		}

		int requiredInventorySlots = 0;
		boolean mainIsTwoHand = itemInRightHand != null && itemInRightHand.getItemTemplate().isTwoHandWeapon();

		if (item.getItemTemplate().isTwoHandWeapon()) {
			if (mainIsTwoHand) {
				if (validateOnly) {
					requiredInventorySlots++;
					markedFreeSlots.add(rightSlot);
					markedFreeSlots.add(leftSlot);
				}
				else {
					unEquip(rightSlot | leftSlot);
				}
			}
			else {
				if (itemInRightHand != null) {
					if (validateOnly) {
						requiredInventorySlots++;
						markedFreeSlots.add(rightSlot);
					}
					else {
						unEquip(rightSlot);
					}
				}
				if (itemInLeftHand != null) {
					if (validateOnly) {
						requiredInventorySlots++;
						markedFreeSlots.add(leftSlot);
					}
					else {
						unEquip(leftSlot);
					}
				}
			}
		}
		else { // adding one-handed weapon
			if (itemInRightHand != null) { // main hand is already occupied
				boolean addingLeftHand = (item.getEquipmentSlot() & ItemSlot.LEFT_HAND.getSlotIdMask()) != 0;
				// if occupied by 2H weapon, we have to unequip both slots, skills are not required
				if (mainIsTwoHand) {
					if (validateOnly) {
						requiredInventorySlots++;
						markedFreeSlots.add(rightSlot);
						markedFreeSlots.add(leftSlot);
					}
					else {
						unEquip(rightSlot | leftSlot);
					}
				} // main hand is already occupied and adding unknown hand, needs skills to be checked
				else if (hasDualWieldingSkills()) {
					// if adding to empty left hand that is ok
					if (itemInLeftHand == null && addingLeftHand) {
						switch (owner.getPlayerClass()) {
							case SCOUT:
							case ASSASSIN:
							case RANGER:
							case GUNNER:
							case GLADIATOR:
								return true;
							default:
								unEquip(rightSlot);
								return false;
						}
					}
					long switchSlot = addingLeftHand ? leftSlot : rightSlot;

					if (validateOnly) {
						requiredInventorySlots++;
						markedFreeSlots.add(switchSlot);
					}
					else {
						unEquip(switchSlot);
					}
				}
				else {
					// requiredInventorySlots are 0
					if (addingLeftHand && itemInLeftHand != null) {
						// this is not good, if inventory is full, should switch slots
						if (validateOnly) {
							markedFreeSlots.add(leftSlot);
						}
						// for dual waepons, they occupy two slots, and not player can equip two one-handed weapons 4.9 - 5.1
						if (itemInRightHand != null && itemInLeftHand != null) {
							return false;
						}
						else {
							unEquip(leftSlot);
						}
					}
					else {
						// replace main hand, doesn't matter which slot is equiped
						// client sends slot 2 even for double-click
						if (validateOnly) {
							markedFreeSlots.add(rightSlot);
						}
						else {
							unEquip(rightSlot);
						}
						item.setEquipmentSlot(rightSlot);
						return true;
					}
				}
			}
		}

		// check again = required slots
		return requiredInventorySlots == 0 || owner.getInventory().getFreeSlots() >= requiredInventorySlots;
	}

	/**
	 * @param requiredSkills
	 * @return
	 */
	private boolean checkAvailableEquipSkills(int[] requiredSkills) {
		boolean isSkillPresent = false;

		// if no skills required - validate as true
		if (requiredSkills.length == 0) {
			return true;
		}

		for (int skill : requiredSkills) {
			if (owner.getSkillList().isSkillPresent(skill)) {
				isSkillPresent = true;
				break;
			}
		}
		return isSkillPresent;
	}

	/**
	 * Used during equip process and analyzes equipped slots
	 *
	 * @param item
	 * @param itemInMainHand
	 * @return
	 */
	private boolean validateEquippedArmor(Item item, boolean validateOnly) {
		// allow wearing of jewelry etc stuff
		ArmorType armorType = item.getItemTemplate().getArmorType();
		if (armorType == null) {
			return true;
		}
		if (armorType == ArmorType.ARROW) {
			return false;
		}

		// check present skill
		int[] requiredSkills = armorType.getRequiredSkills();
		if (!checkAvailableEquipSkills(requiredSkills)) {
			return false;
		}

		ItemSlot slotToCheck1 = ItemSlot.MAIN_HAND;
		ItemSlot slotToCheck2 = ItemSlot.SUB_HAND;
		if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_OR_SUB_OFF.getSlotIdMask()) != 0) {
			slotToCheck1 = ItemSlot.MAIN_OFF_HAND;
			slotToCheck2 = ItemSlot.SUB_OFF_HAND;
		}

		Item itemInMainHand = equipment.get(slotToCheck1.getSlotIdMask());
		if (itemInMainHand != null && armorType == ArmorType.SHIELD && itemInMainHand.getItemTemplate().isTwoHandWeapon()) {
			if (validateOnly) {
				if (owner.getInventory().isFull()) {
					return false;
				}
				markedFreeSlots.add(slotToCheck1.getSlotIdMask());
				markedFreeSlots.add(slotToCheck2.getSlotIdMask());
			}
			else {
				// remove 2H weapon
				unEquip(slotToCheck1.getSlotIdMask() | slotToCheck2.getSlotIdMask());
			}
		}
		return true;
	}

	/**
	 * Will look item in equipment item set
	 *
	 * @param value
	 * @return Item
	 */
	public Item getEquippedItemByObjId(int value) {
		synchronized (equipment) {
			for (Item item : equipment.values()) {
				if (item.getObjectId() == value) {
					return item;
				}
			}
		}

		return null;
	}

	/**
	 * @param value
	 * @return List<Item>
	 */
	public List<Item> getEquippedItemsByItemId(int value) {
		List<Item> equippedItemsById = new ArrayList<Item>();
		synchronized (equipment) {
			for (Item item : equipment.values()) {
				if (item.getItemTemplate().getTemplateId() == value) {
					equippedItemsById.add(item);
				}
			}
		}

		return equippedItemsById;
	}

	/**
	 * @return List<Item>
	 */
	public List<Item> getEquippedItems() {
		HashSet<Item> equippedItems = new HashSet<Item>();
		for (Item i : equipment.values()) {
			equippedItems.add(i);
		}

		return Arrays.asList(equippedItems.toArray(new Item[0]));
	}

	public List<Integer> getEquippedItemIds() {
		HashSet<Integer> equippedIds = new HashSet<Integer>();
		for (Item i : equipment.values()) {
			equippedIds.add(i.getItemId());
		}
		return Arrays.asList(equippedIds.toArray(new Integer[0]));
	}

	/**
	 * @return List<Item>
	 */
	public FastList<Item> getEquippedItemsWithoutStigma() {
		FastList<Item> equippedItems = FastList.newInstance();
		Item twoHanded = null;
		for (Item item : equipment.values()) {
			if (!ItemSlot.isStigma(item.getEquipmentSlot())) {
				if (item.getItemTemplate().isTwoHandWeapon()) {
					if (twoHanded != null) {
						continue;
					}
					twoHanded = item;
				}
				equippedItems.add(item);
			}
		}

		return equippedItems;
	}

	public FastList<Item> getEquippedItemsWithoutStigmaOld() {
		FastList<Item> equippedItems = FastList.newInstance();
		Item twoHanded = null;
		Item offTwoHanded = null;
		for (Item item : equipment.values()) {
			if (!ItemSlot.isStigma(item.getEquipmentSlot())) {
				if (item.getItemTemplate().isTwoHandWeapon()) {
					if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_OR_SUB_OFF.getSlotIdMask()) != 0 && offTwoHanded != null) {
						continue;
					}
					else if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_OR_SUB_OFF.getSlotIdMask()) != 0) {
						offTwoHanded = item;
					}

					if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_OR_SUB_OFF.getSlotIdMask()) == 0 && twoHanded != null) {
						continue;
					}
					else if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_OR_SUB_OFF.getSlotIdMask()) == 0) {
						twoHanded = item;
					}
				}
				equippedItems.add(item);
			}
		}

		return equippedItems;
	}

	/**
	 * @return List<Item>
	 */
	public FastList<Item> getEquippedForApparence() {
		FastList<Item> equippedItems = FastList.newInstance();
		Item twoHanded = null;
		for (Item item : equipment.values()) {
			long slot = item.getEquipmentSlot();
			if (!ItemSlot.isStigma(slot)) {
				if (slot <= ItemSlot.BRACELET.getSlotIdMask()) {
					if (item.getItemTemplate().isTwoHandWeapon()) {
						if (twoHanded != null) {
							continue;
						}
						twoHanded = item;
					}
					equippedItems.add(item);
				}
			}
		}
		return equippedItems;
	}

	/**
	 * @return List<Item>
	 */
	public List<Item> getEquippedItemsAllStigma() {
		List<Item> equippedItems = new ArrayList<Item>();
		for (Item item : equipment.values()) {
			if (ItemSlot.isStigma(item.getEquipmentSlot())) {
				equippedItems.add(item);
			}
		}
		return equippedItems;
	}

	public List<Integer> getEquippedItemsAllStigmaIds() {
		List<Integer> equippedItemIds = new ArrayList<Integer>();
		for (Item item : equipment.values()) {
			if (ItemSlot.isStigma(item.getEquipmentSlot())) {
				equippedItemIds.add(item.getItemId());
			}
		}
		return equippedItemIds;
	}

	/**
	 * @return List<Item>
	 */
	public List<Item> getEquippedItemsRegularStigma() {
		List<Item> equippedItems = new ArrayList<Item>();
		for (Item item : equipment.values()) {
			if (ItemSlot.isRegularStigma(item.getEquipmentSlot())) {
				equippedItems.add(item);
			}
		}
		return equippedItems;
	}

	/**
	 * @return List<Item>
	 */
	public List<Item> getEquippedItemsAdvancedStigma() {
		List<Item> equippedItems = new ArrayList<Item>();
		for (Item item : equipment.values()) {
			if (ItemSlot.isAdvancedStigma(item.getEquipmentSlot())) {
				equippedItems.add(item);
			}
		}
		return equippedItems;
	}

	/**
	 * @return Number of parts equipped belonging to requested itemset
	 */
	public int itemSetPartsEquipped(int itemSetTemplateId) {
		int number = 0;
		Item twoHanded = null;

		for (Item item : equipment.values()) {
			if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_HAND.getSlotIdMask()) != 0 || (item.getEquipmentSlot() & ItemSlot.SUB_OFF_HAND.getSlotIdMask()) != 0) {
				continue;
			}
			if (item.getItemTemplate().isTwoHandWeapon()) {
				if (twoHanded != null) {
					continue;
				}
				twoHanded = item;
			}
			ItemSetTemplate setTemplate = item.getItemTemplate().getItemSet();
			if (setTemplate != null && setTemplate.getId() == itemSetTemplateId) {
				++number;
			}
		}

		return number;
	}

	/**
	 * Should be called only when loading from DB for items isEquipped=1
	 *
	 * @param item
	 */
	public void onLoadHandler(Item item) {
		ItemTemplate template = item.getItemTemplate();
		// unequip arrows during upgrade to 4.0, and put back to inventory
		// do some check for item level as well
		if (template.getArmorType() != null) {
			if (!validateEquippedArmor(item, true)) {
				putItemBackToInventory(item);
				return;
			}
		}
		if (template.getWeaponType() != null) {
			if (!validateEquippedWeapon(item, true)) {
				putItemBackToInventory(item);
				return;
			}
		}
		if (template.isTwoHandWeapon()) {
			ItemSlot[] oldSlots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
			if (oldSlots.length != 2) {
				// update slot during upgrade to 4.0
				long currentSlot = item.getEquipmentSlot();
				if ((item.getEquipmentSlot() & ItemSlot.MAIN_OR_SUB.getSlotIdMask()) != 0) {
					item.setEquipmentSlot(ItemSlot.MAIN_OR_SUB.getSlotIdMask());
				}
				else {
					item.setEquipmentSlot(ItemSlot.MAIN_OFF_OR_SUB_OFF.getSlotIdMask());
				}
				if (currentSlot != item.getEquipmentSlot()) {
					setPersistentState(PersistentState.UPDATE_REQUIRED);
				}
				oldSlots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
			}
			for (ItemSlot sl : oldSlots) {
				if (equipment.containsKey(sl.getSlotIdMask())) {
					log.warn("Duplicate equipped item in slot : " + sl.getSlotIdMask() + " " + owner.getObjectId());
					putItemBackToInventory(item);
					break;
				}
				equipment.put(sl.getSlotIdMask(), item);
			}
			return;
		}

		if (equipment.containsKey(item.getEquipmentSlot())) {
			log.warn("Duplicate equipped item in slot: " + item.getEquipmentSlot() + " " + owner.getObjectId());
			putItemBackToInventory(item);
			return;
		}
		equipment.put(item.getEquipmentSlot(), item);
	}

	private void putItemBackToInventory(Item item) {
		item.setEquipped(false);
		item.setEquipmentSlot(0);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		owner.getInventory().put(item);
	}

	/**
	 * Should be called only when equipment object totally constructed on player loading. Applies every equipped item stats modificators
	 */
	public void onLoadApplyEquipmentStats() {
		Item twoHanded = null;
		for (Item item : equipment.values()) {
			if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_HAND.getSlotIdMask()) == 0 && (item.getEquipmentSlot() & ItemSlot.SUB_OFF_HAND.getSlotIdMask()) == 0) {
				if (item.getItemTemplate().isTwoHandWeapon()) {
					if (twoHanded != null) {
						continue;
					}
					twoHanded = item;
				}
				if (item.getOptionalSocket() == -1) {
					log.warn("on load all eqipment, item can't be equiped because hasTune" + item.getObjectId());
					continue;
				}
				ItemEquipmentListener.onItemEquipment(item, owner);
				owner.getLifeStats().synchronizeWithMaxStats();
			}
		}
	}

	/**
	 * @return true or false
	 */
	public boolean isShieldEquipped() {
		Item subHandItem = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		return subHandItem != null && subHandItem.getItemTemplate().getArmorType() == ArmorType.SHIELD;
	}

	public Item getEquippedShield() {
		Item subHandItem = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		return (subHandItem != null && subHandItem.getItemTemplate().getArmorType() == ArmorType.SHIELD) ? subHandItem : null;
	}

	public Item getEquipedPlume() {
		Item plume = equipment.get(ItemSlot.PLUME.getSlotIdMask());
		return (plume != null && plume.getItemTemplate().getCategory() == ItemCategory.PLUME) ? plume : null;
	}

	/**
	 * @return true if player is equipping the requested ArmorType
	 */
	public boolean isArmorTypeEquipped(ArmorType type) {
		for (Item item : equipment.values()) {
			if (item == null || item.getItemTemplate().getWeaponType() != null) {
				continue;
			}
			// TODO: Check it! Not sure for dual hand
			if (item.getItemTemplate().getArmorType() == type && item.isEquipped() && item.getEquipmentSlot() != ItemSlot.SUB_OFF_HAND.getSlotIdMask()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return <tt>WeaponType</tt> of current weapon in main hand or null
	 */
	public WeaponType getMainHandWeaponType() {
		Item mainHandItem = equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());
		if (mainHandItem == null) {
			return null;
		}

		return mainHandItem.getItemTemplate().getWeaponType();
	}

	/**
	 * @return <tt>WeaponType</tt> of current weapon in off hand or null
	 */
	public WeaponType getOffHandWeaponType() {
		Item offHandItem = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		Item mainHandItem = equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());
		if (mainHandItem == offHandItem) {
			offHandItem = null;
		}
		if (offHandItem != null && offHandItem.getItemTemplate().isWeapon()) {
			return offHandItem.getItemTemplate().getWeaponType();
		}

		return null;
	}

	public boolean isArrowEquipped() {
		Item arrow = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		if (arrow != null && arrow.getItemTemplate().getArmorType() == ArmorType.ARROW) {
			return true;
		}

		return false;
	}

	public boolean isPowerShardEquipped() {
		Item leftPowershard = equipment.get(ItemSlot.POWER_SHARD_LEFT.getSlotIdMask());
		if (leftPowershard != null) {
			return true;
		}

		Item rightPowershard = equipment.get(ItemSlot.POWER_SHARD_RIGHT.getSlotIdMask());
		if (rightPowershard != null) {
			return true;
		}

		return false;
	}

	public Item getMainHandPowerShard() {
		Item mainHandPowerShard = equipment.get(ItemSlot.POWER_SHARD_RIGHT.getSlotIdMask());
		if (mainHandPowerShard != null) {
			return mainHandPowerShard;
		}

		return null;
	}

	public Item getOffHandPowerShard() {
		Item offHandPowerShard = equipment.get(ItemSlot.POWER_SHARD_LEFT.getSlotIdMask());
		if (offHandPowerShard != null) {
			return offHandPowerShard;
		}

		return null;
	}

	/**
	 * @param powerShardItem
	 * @param count
	 */
	public void usePowerShard(Item powerShardItem, int count) {
		decreaseEquippedItemCount(powerShardItem.getObjectId(), count);

		if (powerShardItem.getItemCount() <= 0) {// Search for next same power shards stack
			List<Item> powerShardStacks = owner.getInventory().getItemsByItemId(powerShardItem.getItemTemplate().getTemplateId());
			if (powerShardStacks.size() != 0) {
				equipItem(powerShardStacks.get(0).getObjectId(), powerShardItem.getEquipmentSlot());
			}
			else {
				PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_MSG_WEAPON_BOOST_MODE_BURN_OUT);
				owner.unsetState(CreatureState.POWERSHARD);
			}
		}
	}

	/**
	 * increase item count and return left count
	 */
	public long increaseEquippedItemCount(Item item, long count) {
		// Only Shards can be increased
		if (item.getItemTemplate().getCategory() != ItemCategory.SHARD) {
			return count;
		}

		long leftCount = item.increaseItemCount(count);
		ItemPacketService.updateItemAfterInfoChange(owner, item, ItemUpdateType.STATS_CHANGE);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return leftCount;
	}

	private void decreaseEquippedItemCount(int itemObjId, int count) {
		Item equippedItem = getEquippedItemByObjId(itemObjId);

		// Only Shards can be decreased
		if (equippedItem.getItemTemplate().getCategory() != ItemCategory.SHARD) {
			return;
		}

		if (equippedItem.getItemCount() >= count) {
			equippedItem.decreaseItemCount(count);
		}
		else {
			equippedItem.decreaseItemCount(equippedItem.getItemCount());
		}

		if (equippedItem.getItemCount() == 0) {
			equipment.remove(equippedItem.getEquipmentSlot());
			PacketSendUtility.sendPacket(owner, new SM_DELETE_ITEM(equippedItem.getObjectId()));
			DAOManager.getDAO(InventoryDAO.class).store(equippedItem, owner);
		}

		ItemPacketService.updateItemAfterInfoChange(owner, equippedItem, ItemUpdateType.STATS_CHANGE);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * Switch OFF and MAIN hands
	 */
	public void switchHands() {
		Item mainHandItem = equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());
		Item subHandItem = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		Item mainOffHandItem = equipment.get(ItemSlot.MAIN_OFF_HAND.getSlotIdMask());
		Item subOffHandItem = equipment.get(ItemSlot.SUB_OFF_HAND.getSlotIdMask());

		List<Item> equippedWeapon = new ArrayList<Item>();

		if (mainHandItem != null) {
			equippedWeapon.add(mainHandItem);
		}
		if (subHandItem != null && subHandItem != mainHandItem) {
			equippedWeapon.add(subHandItem);
		}
		if (mainOffHandItem != null) {
			equippedWeapon.add(mainOffHandItem);
		}
		if (subOffHandItem != null && subOffHandItem != mainOffHandItem) {
			equippedWeapon.add(subOffHandItem);
		}

		for (Item item : equippedWeapon) {
			if (item.getItemTemplate().isTwoHandWeapon()) {
				ItemSlot[] slots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
				for (ItemSlot slot : slots) {
					equipment.remove(slot.getSlotIdMask());
				}
			}
			else {
				equipment.remove(item.getEquipmentSlot());
			}
			item.setEquipped(false);
			PacketSendUtility.sendPacket(owner, new SM_INVENTORY_UPDATE_ITEM(owner, item, ItemUpdateType.EQUIP_UNEQUIP));
			if (owner.getGameStats() != null) {
				if ((item.getEquipmentSlot() & ItemSlot.MAIN_HAND.getSlotIdMask()) != 0 || (item.getEquipmentSlot() & ItemSlot.SUB_HAND.getSlotIdMask()) != 0) {
					notifyItemUnequip(item);
				}
			}
		}

		for (Item item : equippedWeapon) {
			long oldSlots = item.getEquipmentSlot();
			if ((oldSlots & ItemSlot.RIGHT_HAND.getSlotIdMask()) != 0) {
				oldSlots ^= ItemSlot.RIGHT_HAND.getSlotIdMask();
			}
			if ((oldSlots & ItemSlot.LEFT_HAND.getSlotIdMask()) != 0) {
				oldSlots ^= ItemSlot.LEFT_HAND.getSlotIdMask();
			}
			item.setEquipmentSlot(oldSlots);
		}

		for (Item item : equippedWeapon) {
			if (item.getItemTemplate().isTwoHandWeapon()) {
				ItemSlot[] slots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
				for (ItemSlot slot : slots) {
					equipment.put(slot.getSlotIdMask(), item);
				}
			}
			else {
				equipment.put(item.getEquipmentSlot(), item);
			}
			item.setEquipped(true);
			ItemPacketService.updateItemAfterEquip(owner, item);
		}

		if (owner.getGameStats() != null) {
			for (Item item : equippedWeapon) {
				if ((item.getEquipmentSlot() & ItemSlot.MAIN_HAND.getSlotIdMask()) != 0 || (item.getEquipmentSlot() & ItemSlot.SUB_HAND.getSlotIdMask()) != 0) {
					notifyItemEquipped(item);
				}
			}
		}

		owner.getLifeStats().updateCurrentStats();
		owner.getGameStats().updateStatsAndSpeedVisually();

		// remove stance effect when switchhand
		if (owner.getController().isUnderStance()) {
			owner.getController().stopStance();
		}

		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * @param weaponType
	 */
	public boolean isWeaponEquipped(WeaponType weaponType) {
		if (equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask()) != null && equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask()).getItemTemplate().getWeaponType() == weaponType) {
			return true;
		}
		if (equipment.get(ItemSlot.SUB_HAND.getSlotIdMask()) != null && equipment.get(ItemSlot.SUB_HAND.getSlotIdMask()).getItemTemplate().getWeaponType() == weaponType) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if dual one-handed weapon is equiped in any slot combination
	 *
	 * @param slot
	 *            masks
	 * @return
	 */
	public boolean hasDualWeaponEquipped(ItemSlot slot) {
		ItemSlot[] slotValues = ItemSlot.getSlotsFor(slot.getSlotIdMask());
		if (slotValues.length == 0) {
			return false;
		}
		for (ItemSlot s : slotValues) {
			Item weapon = equipment.get(s.getSlotIdMask());
			if (weapon == null || weapon.getItemTemplate().isTwoHandWeapon()) {
				continue;
			}
			if (weapon.getItemTemplate().getWeaponType() != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param armorType
	 */
	public boolean isArmorEquipped(ArmorType armorType) {
		if (armorType == null) {
			return false;
		}
		for (long slot : ARMOR_SLOTS) {
			if (equipment.get(slot) != null && equipment.get(slot).getItemTemplate().getArmorType() == armorType) {
				return true;
			}
		}
		return false;
	}

	public boolean isKeybladeEquipped() {
		Item keyblade = getMainHandWeapon(); // equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());

		if ((keyblade != null) && (keyblade.getItemTemplate().getWeaponType() == WeaponType.KEYBLADE_2H)) {
			return true;
		}

		return false;
	}

	/**
	 * Only used for new Player creation. Although invalid, but fits its purpose
	 *
	 * @param slot
	 * @return
	 */
	public boolean isSlotEquipped(long slot) {
		return !(equipment.get(slot) == null);
	}

	public Item getMainHandWeapon() {
		return equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());
	}

	public Item getOffHandWeapon() {
		Item result = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		if (getMainHandWeapon() == result) {
			return null;
		}
		return result;
	}

	/**
	 * @return the persistentState
	 */
	public PersistentState getPersistentState() {
		return persistentState;
	}

	/**
	 * @param persistentState
	 *            the persistentState to set
	 */
	public void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	/**
	 * @param player
	 */
	public void setOwner(Player player) {
		this.owner = player;
	}

	/**
	 * @param player
	 * @param item
	 * @param slot
	 * @return
	 */
	private boolean soulBindItem(final Player player, final Item item, final long slot) {
		if (player.getInventory().getItemByObjId(item.getObjectId()) == null || player.isInState(CreatureState.GLIDING)) {
			return false;
		}
		if (CreatureActions.isAlreadyDead(player)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_INVALID_STANCE(2800119));
			return false;
		}
		else if (player.isInPlayerMode(PlayerMode.RIDE)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_INVALID_STANCE(2800114));
			return false;
		}
		else if (player.isInState(CreatureState.CHAIR)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_INVALID_STANCE(2800117));
			return false;
		}
		else if (player.isInState(CreatureState.RESTING)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_INVALID_STANCE(2800115));
			return false;
		}
		else if (player.isInState(CreatureState.FLYING)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_INVALID_STANCE(2800111));
			return false;
		}
		else if (player.isInState(CreatureState.WEAPON_EQUIPPED)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_INVALID_STANCE(2800159));
			return false;
		}

		RequestResponseHandler responseHandler = new RequestResponseHandler(player) {

			@Override
			public void acceptRequest(Creature requester, Player responder) {
				player.getController().cancelUseItem();

				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, item.getObjectId(), item.getItemId(), 5000, 4), true);

				player.getController().cancelTask(TaskId.ITEM_USE);

				final ActionObserver moveObserver = new ActionObserver(ObserverType.MOVE) {

					@Override
					public void moved() {
						player.getController().cancelTask(TaskId.ITEM_USE);
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_ITEM_CANCELED(item.getNameId()));
						PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, item.getObjectId(), item.getItemId(), 0, 8), true);
					}
				};
				player.getObserveController().attach(moveObserver);

				// item usage animation
				player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						player.getObserveController().removeObserver(moveObserver);

						PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, item.getObjectId(), item.getItemId(), 0, 6), true);
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_ITEM_SUCCEED(item.getNameId()));

						item.setSoulBound(true);
						ItemPacketService.updateItemAfterInfoChange(owner, item);

						equip(slot, item);
						PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), getEquippedForApparence()), true);
					}
				}, 5000));
			}

			@Override
			public void denyRequest(Creature requester, Player responder) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_ITEM_CANCELED(item.getNameId()));
			}
		};

		boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_SOUL_BOUND_ITEM_DO_YOU_WANT_SOUL_BOUND, responseHandler);
		if (requested) {
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_SOUL_BOUND_ITEM_DO_YOU_WANT_SOUL_BOUND, 0, 0, new DescriptionId(item.getNameId())));
		}
		else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_CLOSE_OTHER_MSG_BOX_AND_RETRY);
		}
		return false;
	}

	private boolean verifyRankLimits(Item item) {
		int rank = owner.getAbyssRank().getRank().getId();
		if (!item.getItemTemplate().getUseLimits().verifyRank(rank)) {
			return false;
		}
		if (item.getFusionedItemTemplate() != null) {
			return item.getFusionedItemTemplate().getUseLimits().verifyRank(rank);
		}
		return true;
	}

	public void checkRankLimitItems() {
		for (Item item : getEquippedItems()) {
			if (!verifyRankLimits(item)) {
				unEquipItem(item.getObjectId(), item.getEquipmentSlot());
				PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_MSG_UNEQUIP_RANKITEM(item.getNameId()));
				// TODO: Check retail what happens with full inv and the task msgs.
			}
		}
	}
}

