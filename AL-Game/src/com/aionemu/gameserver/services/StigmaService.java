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
package com.aionemu.gameserver.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.templates.HiddenStigmasTemplate;
import com.aionemu.gameserver.model.templates.item.RequireSkill;
import com.aionemu.gameserver.model.templates.item.Stigma;
import com.aionemu.gameserver.model.templates.item.Stigma.StigmaSkill;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.skillengine.model.SkillLearnTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author ATracer
 * @modified cura
 * @updated ever & Kill3r 4.8
 */
public class StigmaService {

	private static final Logger log = LoggerFactory.getLogger(StigmaService.class);

	public static boolean extendAdvancedStigmaSlots(Player player) {
		int newAdvancedSlotSize = player.getCommonData().getAdvancedStigmaSlotSize() + 1;
		if (newAdvancedSlotSize <= 6) { // maximum
			player.getCommonData().setAdvancedStigmaSlotSize(newAdvancedSlotSize);
			PacketSendUtility.sendPacket(player, SM_CUBE_UPDATE.stigmaSlots(player.getCommonData().getAdvancedStigmaSlotSize()));
			return true;
		}
		return false;
	}

	/**
	 * @param player
	 * @param resultItem
	 * @param slot
	 * @return
	 */
	public static boolean notifyEquipAction(Player player, Item resultItem, long slot) {
		if (resultItem.getItemTemplate().isStigma()) {
			if (ItemSlot.isRegularStigma(slot)) {
				// check the number of stigma wearing
				if (getPossibleStigmaCount(player) <= player.getEquipment().getEquippedItemsRegularStigma().size()) {
					AuditLogger.info(player, "Possible client hack stigma count big :O");
					return false;
				}
			}
			else if (ItemSlot.isAdvancedStigma(slot)) {
				// check the number of advanced stigma wearing
				if (getPossibleAdvencedStigmaCount(player) <= player.getEquipment().getEquippedItemsAdvancedStigma().size()) {
					AuditLogger.info(player, "Possible client hack advanced stigma count big :O");
					return false;
				}
			}

			if (!resultItem.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass())) {
				AuditLogger.info(player, "Possible client hack not valid for class.");
				return false;
			}

			// You cannot equip 2 stigma skills at 1 slot , was possible before.. o.o
			if (!StigmaService.isPossibleEquippedStigma(player, resultItem)) {
				AuditLogger.info(player, "Player tried to get Multiple Stigma's from One Stigma Stone!");
				return false;
			}

			Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();

			if (stigmaInfo == null) {
				log.warn("Stigma info missing for item: " + resultItem.getItemTemplate().getTemplateId());
				return false;
			}

			int kinahCount = stigmaInfo.getKinah();
			if (player.getInventory().getKinah() < kinahCount) {
				// You do not have enough Kinah to equip the Stigma Stone.
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300413));
				AuditLogger.info(player, "Possible client hack kinah count low.");
				return false;
			}

			if (!player.getInventory().tryDecreaseKinah(kinahCount)) {
				return false;
			}

			// default main stigma skill
			player.getSkillList().addStigmaSkill(player, stigmaInfo.getSkills(), true);

			// other stigma lvls of the same skill
			List<Integer> stigmaData = stigmaInfo.getSkillIdOnly();
			List<StigmaSkill> stigmaUpperLvls;

			if (stigmaData.size() != 1) { // Dual Skill i.e Exhausting Wave
				for (Integer id : stigmaData) {
					stigmaUpperLvls = getStigmaInfoUpToLevel(player.getCommonData().getLevel(), id); // only 2 skill here '1:534 1:434
					if (stigmaUpperLvls != null) {
						for (StigmaSkill stigma : stigmaUpperLvls) {
							player.getSkillList().addStigmaSkill(player, stigma.getSkillId(), resultItem.getEnchantLevel() + 1, false, true);
						}
					}
				}
			}
			else {
				// Single Skill
				stigmaUpperLvls = getStigmaInfoUpToLevel(player.getCommonData().getLevel(), stigmaData.get(0)); // only 1 skill here '1:534'
				if (stigmaUpperLvls != null) {
					for (StigmaSkill stigma : stigmaUpperLvls) {
						player.getSkillList().addStigmaSkill(player, stigma.getSkillId(), resultItem.getEnchantLevel() + 1, false, true);
					}
				}
			}
			List<Integer> sStigma = player.getEquipment().getEquippedItemsAllStigmaIds();
			sStigma.add(resultItem.getItemId()); // The last item ur about to add is not in getEquippedItemsAllStigma , so adding manual
		}
		return true;
	}

	/**
	 * Sends the player current Level and startingStigmaSkillId to process and obtain all the stigma skill level of that skill. Which then returns all those stigmas as a List<StigmaSkill>
	 * 
	 * @param playerLvl
	 *            Players Current Level
	 * @param startingStigmaSkillid
	 *            The Skill ID of that stigma skill you want to get the other levels of.
	 * @return A List of Stigma Skill of other lvls of that stigma.
	 */
	public static List<StigmaSkill> getStigmaInfoUpToLevel(int playerLvl, int startingStigmaSkillid) {
		List<StigmaSkill> stigmaList = new ArrayList<StigmaSkill>();
		SkillLearnTemplate[] temps = DataManager.SKILL_TREE_DATA.getTemplatesForSkill(startingStigmaSkillid);
		String skillName;

		for (SkillLearnTemplate skillTemp : temps) {
			skillName = skillTemp.getName();
			for (int id = startingStigmaSkillid; id <= 5000; id++) {
				if (startingStigmaSkillid == 3330 && id == 3331) {
					id = 4591; // exception for Shadowfall, its lvl1 id is 3330 and lvl2 is starting from 4591
				}
				else if (startingStigmaSkillid == 1210 && id == 1216) {
					id = 4603;
				}
				SkillLearnTemplate[] NextTemps = DataManager.SKILL_TREE_DATA.getTemplatesForSkill(id);
				for (SkillLearnTemplate skillTemp2 : NextTemps) {
					if (skillTemp2.getName().equalsIgnoreCase(skillName)) {
						if (playerLvl >= skillTemp2.getMinLevel()) {
							// log.info("!! The player is eligible to learn this skill at lvl " + playerLvl + " .. New skill Lvl : " + skillTemp2.getMinLevel() + " Old skill Lvl : "
							// +skillTemp.getMinLevel());
							int lv = skillTemp2.getSkillLevel(), i = skillTemp2.getSkillId();
							StigmaSkill sti = new StigmaSkill(lv, i);

							stigmaList.add(sti);
						}
					}
					else {
						return stigmaList;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param player
	 * @param resultItem
	 * @return
	 */
	public static boolean notifyUnequipAction(Player player, Item resultItem) {
		if (resultItem.getItemTemplate().isStigma()) {
			Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();
			int itemId = resultItem.getItemId();
			Equipment equipment = player.getEquipment();
			if (itemId == 140000007 || itemId == 140000005) {
				if (equipment.hasDualWeaponEquipped(ItemSlot.LEFT_HAND)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_STIGMA_CANNT_UNEQUIP_STONE_FIRST_UNEQUIP_CURRENT_EQUIPPED_ITEM);
					return false;
				}
			}
			for (Item item : player.getEquipment().getEquippedItemsAllStigma()) {
				Stigma si = item.getItemTemplate().getStigma();
				if (resultItem == item || si == null) {
					continue;
				}

				for (StigmaSkill sSkill : stigmaInfo.getSkills()) {
					for (RequireSkill rs : si.getRequireSkill()) {
						if (rs.getSkillIds().contains(sSkill.getSkillId())) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300410, new DescriptionId(resultItem.getItemTemplate().getNameId()), new DescriptionId(item.getItemTemplate().getNameId())));
							return false;
						}
					}
				}
			}

			for (StigmaSkill sSkill : stigmaInfo.getSkills()) {
				int nameId = DataManager.SKILL_DATA.getSkillTemplate(sSkill.getSkillId()).getNameId();
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300403, new DescriptionId(nameId)));

				// remove skill
				SkillLearnService.removeSkill(player, sSkill.getSkillId());

				// remove effect
				player.getEffectController().removeEffect(sSkill.getSkillId());

				// remove hidden stigma
				player.getSkillList().deleteHiddenStigma(player);
			}

			// for remaining lvls for the same skill ..
			List<Integer> stigmaData = stigmaInfo.getSkillIdOnly();
			List<StigmaSkill> stigmaUpperLvls;

			if (stigmaData.size() != 1) {
				for (Integer id : stigmaData) {
					stigmaUpperLvls = getStigmaInfoUpToLevel(player.getCommonData().getLevel(), id); // only 2 skill here '1:534 1:434
					if (stigmaUpperLvls != null) {
						for (StigmaSkill stigma : stigmaUpperLvls) {
							player.getSkillList().removeSkill(stigma.getSkillId());

							// remove effect
							player.getEffectController().removeEffect(stigma.getSkillId());
						}
					}
				}
			}
			else {
				stigmaUpperLvls = getStigmaInfoUpToLevel(player.getCommonData().getLevel(), stigmaData.get(0)); // only 1 skill here '1:534'
				if (stigmaUpperLvls != null) {
					for (StigmaSkill stigma : stigmaUpperLvls) {
						player.getSkillList().removeSkill(stigma.getSkillId());

						// remove effect
						player.getEffectController().removeEffect(stigma.getSkillId());
					}
				}
			}

		}
		return true;
	}

	public static void recheckHiddenStigma(Player player) {
		if (player.getLevel() >= 55 && player.getEquipment().getEquippedItemsAllStigma().size() >= 6) {
			for (Item stigma : player.getEquipment().getEquippedItemsAllStigma()) {
				if (stigma.getItemTemplate().isNoEnchant())
					return;
			}
			int hiddenStigmaSkillId = DataManager.HIDDEN_STIGMA_DATA.getHiddenStigmaSkill(player);

			if (hiddenStigmaSkillId != 0) {
				if (!player.getSkillList().isHaveHiddenStigma(player) || (player.getSkillList().isHaveHiddenStigma(player) && player.getSkillList().getStigmaSkillEntry(hiddenStigmaSkillId) == null)) {
					player.getSkillList().addHiddenStigmaSkill(player, hiddenStigmaSkillId, 1);
				}
			}
		}
	}

	/**
	 * @param player
	 */
	public static void onPlayerLogin(Player player) {
		List<Item> equippedItems = player.getEquipment().getEquippedItemsAllStigma();
		for (Item item : equippedItems) { // All Equipped Items are Stigmas
			if (item.getItemTemplate().isStigma()) {
				Stigma stigmaInfo = item.getItemTemplate().getStigma();
				
				if (stigmaInfo == null) {
					log.warn("Stigma info missing for item: " + item.getItemTemplate().getTemplateId());
					return;
				}
				if (item.getItemTemplate().getTemplateId() == 140001132) {
					System.out.println("!!! USED !!!");
				}
				player.getSkillList().addStigmaSkill(player, stigmaInfo.getSkills(), false);
				player.getSkillList().deleteHiddenStigmaSilent(player);
				recheckHiddenStigma(player);
				
				List<Integer> stigmaData = stigmaInfo.getSkillIdOnly();
				List<StigmaSkill> stigmaUpperLvls;
				
				if (stigmaData.size() != 1) {
					for (Integer id : stigmaData) {
						stigmaUpperLvls = getStigmaInfoUpToLevel(player.getCommonData().getLevel(), id); // only 2 skill here '1:534 1:434'
						if (stigmaUpperLvls != null) {
							for (StigmaSkill stigma : stigmaUpperLvls) {
								player.getSkillList().addStigmaSkill(player, stigma.getSkillId(), item.getEnchantLevel() + 1, false, false);
							}
						}
					}
				}
				else {
					stigmaUpperLvls = getStigmaInfoUpToLevel(player.getCommonData().getLevel(), stigmaData.get(0)); // only 1 skill here '1:534'
					if (stigmaUpperLvls != null) {
						for (StigmaSkill stigma : stigmaUpperLvls) {
							player.getSkillList().addStigmaSkill(player, stigma.getSkillId(), item.getEnchantLevel() + 1, false, false);
						}
					}
				}
			}
		}

		for (Item item : equippedItems) {
			if (item.getItemTemplate().isStigma()) {
				if (!isPossibleEquippedStigma(player, item)) {
					AuditLogger.info(player, "Possible client hack stigma count big :O");
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}

				Stigma stigmaInfo = item.getItemTemplate().getStigma();

				if (stigmaInfo == null) {
					log.warn("Stigma info missing for item: " + item.getItemTemplate().getTemplateId());
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}

				if (!item.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass())) {
					AuditLogger.info(player, "Possible client hack not valid for class.");
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
				}
			}
		}
	}

	/**
	 * Get the number of available Stigma
	 *
	 * @param player
	 * @return
	 */
	private static int getPossibleStigmaCount(Player player) {
		if (player == null || player.getLevel() < 20)
			return 0;

		if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
			return 6;
		}

		/*
		 * Stigma Quest Elyos: 1929, Asmodians: 2900
		 */
		boolean isCompleteQuest = false;

		if (player.getRace() == Race.ELYOS) {
			isCompleteQuest = player.isCompleteQuest(1929) || (player.getQuestStateList().getQuestState(1929).getStatus() == QuestStatus.START && player.getQuestStateList().getQuestState(1929).getQuestVars().getQuestVars() == 98);
		}
		else {
			isCompleteQuest = player.isCompleteQuest(2900) || (player.getQuestStateList().getQuestState(2900).getStatus() == QuestStatus.START && player.getQuestStateList().getQuestState(2900).getQuestVars().getQuestVars() == 99);
		}

		int playerLevel = player.getLevel();

		if (isCompleteQuest) {
			if (playerLevel < 30)
				return 2;
			else if (playerLevel < 40)
				return 3;
			else if (playerLevel < 50)
				return 4;
			else if (playerLevel < 55)
				return 5;
			else
				return 6;
		}
		return 0;
	}

	/**
	 * Get the number of available Advenced Stigma
	 *
	 * @param player
	 * @return
	 */
	private static int getPossibleAdvencedStigmaCount(Player player) {
		if (player == null || player.getLevel() < 45)
			return 0;

		if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
			return 6;
		}

		/*
		 * Advenced Stigma Quest 1st - Elyos: 3930, Asmodians: 4934 2nd - Elyos: 3931, Asmodians: 4935 3rd- Elyos: 3932, Asmodians: 4936 4th - Elyos: 11049, Asmodians: 21049 5th - Elyos: 30217,
		 * Asmodians: 30317
		 */
		if (player.getRace() == Race.ELYOS) {
			// Check whether Stigma Quests
			if (!player.isCompleteQuest(1929))
				return 0;
			if (player.isCompleteQuest(11550))
				return 6;
			else if (player.isCompleteQuest(30217) || player.isCompleteQuest(11276))
				return 5;
			else if (player.isCompleteQuest(11049))
				return 4;
			else if (player.isCompleteQuest(3932))
				return 3;
			else if (player.isCompleteQuest(3931))
				return 2;
			else if (player.isCompleteQuest(3930))
				return 1;
		}
		else {
			// Check whether Stigma Quests
			if (!player.isCompleteQuest(2900))
				return 0;
			if (player.isCompleteQuest(21550))
				return 6;
			else if (player.isCompleteQuest(30317) || player.isCompleteQuest(21278))
				return 5;
			else if (player.isCompleteQuest(21049))
				return 4;
			else if (player.isCompleteQuest(4936))
				return 3;
			else if (player.isCompleteQuest(4935))
				return 2;
			else if (player.isCompleteQuest(4934))
				return 1;
		}
		return 0;
	}

	/**
	 * Stigma is a worn check available slots
	 *
	 * @param player
	 * @param item
	 * @return
	 */
	private static boolean isPossibleEquippedStigma(Player player, Item item) {
		if (player == null || (item == null || !item.getItemTemplate().isStigma()))
			return false;

		long itemSlotToEquip = item.getEquipmentSlot();

		// Stigma
		if (ItemSlot.isRegularStigma(itemSlotToEquip)) {
			int stigmaCount = getPossibleStigmaCount(player);

			if (stigmaCount > 0) {
				if (stigmaCount == 1) {
					if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask())
						return true;
				}
				else if (stigmaCount == 2) {
					if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask())
						return true;
				}
				else if (stigmaCount == 3) {
					if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask())
						return true;
				}
				else if (stigmaCount == 4) {
					if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA4.getSlotIdMask())
						return true;
				}
				else if (stigmaCount == 5) {
					if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA4.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA5.getSlotIdMask())
						return true;
				}
				else if (stigmaCount == 6) {
					return true;
				}
			}
		}
		// Advenced Stigma
		else if (ItemSlot.isAdvancedStigma(itemSlotToEquip)) {
			int advStigmaCount = getPossibleAdvencedStigmaCount(player);

			if (advStigmaCount > 0) {
				if (advStigmaCount == 1) {
					if (itemSlotToEquip == ItemSlot.ADV_STIGMA1.getSlotIdMask())
						return true;
				}
				else if (advStigmaCount == 2) {
					if (itemSlotToEquip == ItemSlot.ADV_STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA2.getSlotIdMask())
						return true;
				}
				else if (advStigmaCount == 3) {
					if (itemSlotToEquip == ItemSlot.ADV_STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA2.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA3.getSlotIdMask())
						return true;
				}
				else if (advStigmaCount == 4) {
					if (itemSlotToEquip == ItemSlot.ADV_STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA2.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA3.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA4.getSlotIdMask())
						return true;
				}
				else if (advStigmaCount == 5) {
					if (itemSlotToEquip == ItemSlot.ADV_STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA2.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA3.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA4.getSlotIdMask() || itemSlotToEquip == ItemSlot.ADV_STIGMA5.getSlotIdMask())
						return true;
				}
				else if (advStigmaCount == 6) {
					return true;
				}
			}
		}
		return false;
	}

	public static void reparseHiddenStigmas() {
		for (HiddenStigmasTemplate classStigmas : DataManager.HIDDEN_STIGMA_DATA.getHiddenStigmasByClass()) {
			for (HiddenStigmasTemplate.HiddenStigmaTemplate hst : classStigmas.getHiddenStigmas()) {
				hst.dataProcessing();
			}
		}
	}
}
