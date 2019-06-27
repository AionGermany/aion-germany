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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatEnchantFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.stats.listeners.ItemEquipmentListener;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemEnchantChance;
import com.aionemu.gameserver.model.templates.item.ItemEnchantChanceList;
import com.aionemu.gameserver.model.templates.item.ItemEnchantTable;
import com.aionemu.gameserver.model.templates.item.ItemEnchantTemplate;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.Stigma;
import com.aionemu.gameserver.model.templates.item.actions.EnchantItemAction;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.item.ItemSocketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.RndArray;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author ATracer
 * @modified Wakizashi, Source, vlog
 * @rework GiGatR00n v4.7.5.x, Blackfire, Kill3r
 */
public class EnchantService {

	private static final Logger log = LoggerFactory.getLogger(EnchantService.class);

	public static void amplifyItemCommand(Player player, Item item) {
		int buffId = 0;
		if (item == null || player == null)
			return;

		if (!item.getItemTemplate().getExceedEnchant())
			return;

		if (item.getEnchantOrAuthorizeLevel() < 15)
			return;

		if (item.getItemTemplate().isArmor()) {
			buffId = getArmorBuff(item);
		}
		else if (item.getItemTemplate().isWeapon()) {
			buffId = getWeaponBuff(player);
		}

		item.setAmplified(true);
		item.setAmplificationSkill(buffId);
		ItemPacketService.updateItemAfterInfoChange(player, item);
	}

	private static int getArmorBuff(Item armor) {
		int skillId = 0;
		ItemCategory itemCategory = armor.getItemTemplate().getCategory();
		// Skill range of armor buffs 13038 - 13147
		// Shield is armor, but have weapon buffs
		switch(itemCategory) {
			case JACKET:
				skillId = Rnd.get(13128, 13147);
				break;
			case GLOVES:
				skillId = Rnd.get(13038, 13060);
				break;
			case SHOULDERS:
				skillId = Rnd.get(13082, 13107);
				break;
			case PANTS:
				skillId = Rnd.get(13061, 13081);
				break;
			case SHOES:
				skillId = Rnd.get(13108, 13127);
				break;
			case SHIELD:
				skillId = Rnd.get(13001, 13037);
				break;
			default:
				break;
		}
		return skillId;
	}

	private static int getWeaponBuff(Player player) {
		int skillId = 0;
		// Skill range of weapon buffs 13001 - 13037
		skillId = Rnd.get(13001, 13037);
		if (player.getSkillList().getSkillEntry(skillId) != null) {
			skillId = Rnd.get(13001, 13037);
		}
		return skillId;
	}

	/**
	 * @param player
	 * @param parentItem
	 *            the enchantment stone
	 * @param targetItem
	 *            the item to enchant
	 * @param supplementItem
	 *            the item, giving additional chance
	 * @return true, if successful
	 */
	public static boolean enchantItem(Player player, Item parentItem, Item targetItem, Item supplementItem) {
		ItemTemplate enchantStone = parentItem.getItemTemplate();
		int enchantStoneLevel = enchantStone.getLevel();
		int targetItemLevel = targetItem.getItemTemplate().getLevel();
		int enchantitemLevel = targetItem.getEnchantOrAuthorizeLevel() + 1;

		// Modifier, depending on the quality of the item
		// Decreases the chance of enchant
		int qualityCap = 0;

		switch (targetItem.getItemTemplate().getItemQuality()) {
			case JUNK:
			case COMMON:
				qualityCap = 5;
				break;
			case RARE:
				qualityCap = 10;
				break;
			case LEGEND:
				qualityCap = 15;
				break;
			case UNIQUE:
				qualityCap = 20;
				break;
			case EPIC:
				qualityCap = 25;
				break;
			case MYTHIC:
				qualityCap = 30;
				break;
			default:
				break;
		}

		// Start value of success
		float success = EnchantsConfig.ENCHANT_STONE;

		// Since 4.7.5 we need to calculate the success for the new enchantment stones a little bit different
		// Every new enchantment stone got a declared level range, so we pickup random a level value for the
		// success calculation.
		switch (parentItem.getItemId()) {
			case 166000191: // Alpha
				enchantStoneLevel = Rnd.get(1, 29);
				break;
			case 166000192: // Beta
				enchantStoneLevel = Rnd.get(30, 59);
				break;
			case 166000193: // Gamma
				enchantStoneLevel = Rnd.get(60, 84);
				break;
			case 166000194: // Delta
				enchantStoneLevel = Rnd.get(85, 104);
				break;
			case 166000195: // Epsilon
				enchantStoneLevel = Rnd.get(105, 190);
				break;
			case 166020000: // Omega Enchantment Stone's (All variants)
			case 166020001:
			case 166020002:
			case 166020003:
			case 166020004:
			case 166020005:
			case 166020007:
			case 166020008:
			case 166020006:
			case 166022003:
			case 166022007:
				enchantStoneLevel = Rnd.get(150, 230);
				break;
			case 166022000: // Irridescent Omega Enchantment Stone
			case 166022001: // [Event] Irridescent Omega Enchantment Stone (7 Days)
			case 166022002: // [Event] Irridescent Omega Enchantment Stone
				enchantStoneLevel = Rnd.get(190, 270);
				break;
			case 166000196: // 5.0 Enchantment Stone
				enchantStoneLevel = Rnd.get(105, 190);
				break;
			case 166010001: // 5.6 Shining Enchantment Stone
				enchantStoneLevel = Rnd.get(150, 230);
				break;
			default:
				break;
		}

		// Extra success chance
		// The greater the enchant stone level, the greater the
		// level difference modifier
		int levelDiff = enchantStoneLevel - targetItemLevel;
		success += levelDiff > 0 ? levelDiff * 3f / qualityCap : 0;

		// Level difference
		// Can be negative, if the item quality too high
		// And the level difference too small
		success += levelDiff - qualityCap;

		// Enchant next level difficulty
		// The greater item enchant level,
		// the lower start success chance
		success -= targetItem.getEnchantOrAuthorizeLevel() * qualityCap / (enchantitemLevel > 10 ? 5f : 6f);

		// Supplement is used
		if (supplementItem != null) {
			// Amount of supplement items
			int supplementUseCount = 1;
			// Additional success rate for the supplement
			ItemTemplate supplementTemplate = supplementItem.getItemTemplate();
			float addSuccessRate = 0f;

			EnchantItemAction action = supplementTemplate.getActions().getEnchantAction();
			if (action != null) {
				if (action.isManastoneOnly()) {
					return false;
				}
				addSuccessRate = action.getChance() * 2;
			}

			action = enchantStone.getActions().getEnchantAction();
			if (action != null) {
				supplementUseCount = action.getCount();
			}

			// Beginning from the level 11 of the enchantment of the item,
			// There will be 2 times more supplements required
			if (enchantitemLevel > 10) {
				supplementUseCount = supplementUseCount * 2;
			}

			// Check the required amount of the supplements
			if (player.getInventory().getItemCountByItemId(supplementTemplate.getTemplateId()) < supplementUseCount) {
				return false;
			}

			// Adjust Add Success Rate to rates in Config
			switch (parentItem.getItemTemplate().getItemQuality()) {
				case LEGEND:
					addSuccessRate *= EnchantsConfig.LESSER_SUP;
					break;
				case UNIQUE:
					addSuccessRate *= EnchantsConfig.REGULAR_SUP;
					break;
				case EPIC:
					addSuccessRate *= EnchantsConfig.GREATER_SUP;
					break;
				case MYTHIC:
					addSuccessRate *= EnchantsConfig.MYTHIC_SUP;
					break;
				default:
					break;
			}

			// Add success rate of the supplement to the overall chance
			success += addSuccessRate;

			// Put supplements to wait for update
			player.subtractSupplements(supplementUseCount, supplementTemplate.getTemplateId());
		}
		
		if (targetItem.isAmplified() && enchantStone.isEnchantmentStone()) {
			success += 180 - targetItem.getEnchantOrAuthorizeLevel() * 1.0f;
		}
		
		if (success > 100) {
			success = 100;
		}
		
		if (targetItem.getItemTemplate().isEstima()) { // TODO
			success = 50;
		}
		
		boolean result = false;
		float random = Rnd.get(1, 1000) / 10f; // TODO TEST OTHER VALUES

		// If the random number < or = overall success rate,
		// The item will be successfully enchanted
		if (random <= success) {
			result = true;
		}
		
		switch (targetItem.getItemTemplate().getItemQuality()) {
			case ANCIENT:
			case RELIC:
			case FINALITY:
				int chanceId = getChanceId(targetItem, parentItem);
				ItemEnchantChance eItem = DataManager.ITEM_ENCHANT_CHANCES_DATA.getChanceById(chanceId);
				ItemEnchantChanceList eData = eItem.getChancesById(targetItem.getEnchantOrAuthorizeLevel());
				if (random <= eData.getChance()) {
					result = true;
				} else {
					result = false;
				}
				success = eData.getChance();
			default:
				break;
		}

		// For test purpose. To use by administrator
		if (player.getAccessLevel() > 2) {
			PacketSendUtility.sendMessage(player, (result ? "Success" : "Fail") + " Rnd:" + random + " Luck:" + success);
		}

		return result;
	}

	public static void enchantStigmaAct(Player player, Item parentItem, Item targetItem, int currentEnchant, boolean result) {
		int oldEnchant = targetItem.getEnchantOrAuthorizeLevel();
		int finalPlusMessage = 0;
		
		if (result) {
			currentEnchant += Rnd.get(1, 3);
			if (currentEnchant > 10) {
				currentEnchant = 10;
			}
			finalPlusMessage = currentEnchant - oldEnchant;
		} else {
			currentEnchant = 0;
		}
		
		if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
			AuditLogger.info(player, "Possible enchant hack, can't remove 2nd stigma.");
			return;
		}
		
		targetItem.setEnchantOrAuthorizeLevel(currentEnchant);

		if (targetItem.isEquipped()) {
			player.getGameStats().updateStatsVisually();
			player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
		} else {
			player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
		}

		ItemPacketService.updateItemAfterInfoChange(player, targetItem);

		if (result) {
			if (targetItem.isEquipped()) {
				Stigma stigmaInfo = targetItem.getItemTemplate().getStigma();
				for (Stigma.StigmaSkill sSkill : stigmaInfo.getSkills()) {
					String sSkillStack = DataManager.SKILL_DATA.getSkillTemplate(sSkill.getSkillId()).getStack();
					for (PlayerSkillEntry psSkill : player.getSkillList().getStigmaSkills()) {
						if (psSkill.getSkillTemplate().getStack().equals(sSkillStack)) {
							SkillLearnService.removeSkill(player, psSkill.getSkillId());
							player.getEffectController().removeEffect(psSkill.getSkillId());
						}
					}
				}
				player.getSkillList().deleteHiddenStigmaSilent(player);
				List<Integer> realSkillId = targetItem.getItemTemplate().getStigma().getSkillIdOnly();
				for (int skillId : realSkillId) {
					if (skillId != 0) {
						if (targetItem.isEquipped()) {
							player.getSkillList().addStigmaSkill(player, skillId, 1);
						}
					} else {
						log.error("No have Stigma skill for enchanted stigma item.");
					}
				}
				StigmaService.recheckHiddenStigma(player);
			}
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_MATTER_ENCHANT_SUCCESS(new DescriptionId(targetItem.getNameId()), finalPlusMessage));
		} else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_ENCHANT_FAIL(new DescriptionId(targetItem.getNameId())));
		}
	}

	/**
	 * @param player
	 * @param targetItem
	 * @param parentItem
	 */
	public static boolean breakItem(Player player, Item targetItem, Item parentItem) {
		Storage inventory = player.getInventory();

		int kinah = 20000;
		int stone = 188100335; // Enchantment Stone Dust.

		if (inventory.getItemByObjId(targetItem.getObjectId()) == null) {
			return false;
		}

		ItemTemplate itemTemplate = targetItem.getItemTemplate();
		int quality = itemTemplate.getItemQuality().getQualityId();

		if (!itemTemplate.isArmor() && !itemTemplate.isWeapon()) {
			AuditLogger.info(player, "Break item hack, no armor or weapon.");
			return false;
		}
		
		if (player.getInventory().getKinah() >= kinah) {
			player.getInventory().decreaseKinah(kinah);
		} else {
			return false;
		}

		// Quality modifier
		if (itemTemplate.isSoulBound() && !itemTemplate.isArmor()) {
			quality += 1;
		} else if (!itemTemplate.isSoulBound() && itemTemplate.isArmor()) {
			quality -= 1;
		}

		int number = 0;
		// int level = 1;
		switch (quality) {
			case 0: // JUNK
			case 1: // COMMON
				number = Rnd.get(50, 200);
				break;
			case 2: // RARE
				number = Rnd.get(200, 400);
				break;
			case 3: // LEGEND
				number = Rnd.get(400, 600);
				break;
			case 4: // UNIQUE
				number = Rnd.get(600, 800);
				break;
			case 5: // EPIC
				number = Rnd.get(800, 1000);
				break;
			case 6: // MYTHIC
				number = Rnd.get(1000, 2000);
				break;
			case 7: // ANCIENT
				number = Rnd.get(1200, 2200);
				break;
			case 8: // RELIC
				number = Rnd.get(1500, 2500);
				break;
			case 9: // FINALITY
				number = Rnd.get(2000, 3000);
				break;
			default:
				break;
		}
		int enchantItemId = stone;
		if (inventory.delete(targetItem) != null) {
			ItemService.addItem(player, enchantItemId, number);
		} else {
			AuditLogger.info(player, "Possible break item hack, do not remove item.");
		}
		return true;
	}

	public static int BreakKinah(Item item) {
		return 20000;
	}

	private static final int[] HighDaevaStoneItems = { 169405421, 169405422, 169405423 };

	public static boolean breakItem2(Player player, Item targetItem) { // ONLY FOR TESTING
		Storage inventory = player.getInventory();

		if (inventory.getItemByObjId(targetItem.getObjectId()) == null) {
			return false;
		}

		ItemTemplate itemTemplate = targetItem.getItemTemplate();
		int quality = itemTemplate.getItemQuality().getQualityId();

		// Quality modifier
		if (itemTemplate.isSoulBound() && !itemTemplate.isArmor()) {
			quality += 1;
		} else if (!itemTemplate.isSoulBound() && itemTemplate.isArmor()) {
			quality -= 1;
		}

		int number = 0;
		switch (quality) {
			case 0: // JUNK
			case 1: // COMMON
				number = Rnd.get(1, 200);
				break;
			case 2: // RARE
				number = Rnd.get(1, 400);
				break;
			case 3: // LEGEND
				number = Rnd.get(1, 600);
				break;
			case 4: // UNIQUE
				number = Rnd.get(1, 800);
				break;
			case 5: // EPIC
				number = Rnd.get(1, 1000);
				break;
			case 6: // MYTHIC
				number = Rnd.get(1, 2000);
				break;
			case 7: // ANCIENT
				number = Rnd.get(1200, 2200);
				break;
			case 8: // RELIC
				number = Rnd.get(1500, 2500);
				break;
			case 9: // FINALITY
				number = Rnd.get(2000, 3000);
				break;
		}

		int enchantItemId = 188100335; // Enchantment Stone Dust
		int stoneType = Rnd.get(169405421, 169405423); // Ancient Crafting Stone,Primeval Crafting Stone,Crafting Stone of Eternity

		if (inventory.delete(targetItem) != null) {
			if (itemTemplate.getLevel() >= 66 && player.getLevel() >= 66) {
				ItemService.addItem(player, enchantItemId, number);
				ItemService.addItem(player, stoneType, 1);
			} else {
				ItemService.addItem(player, enchantItemId, number);
			}
		} else {
			AuditLogger.info(player, "Possible break item hack, do not remove item.");
		}
		return true;
	}

	public static void enchantItemAct(Player player, Item parentItem, Item targetItem, int currentEnchant, boolean result) {
		int addLevel = 1;
		int buffId = 0;
		int EnchantKinah = EnchantKinah(targetItem);
		int rnd = Rnd.get(100); // crit modifier
		int stoneId = parentItem.getItemId();
		switch (stoneId) {
			case 166020000: // Omega Enchantment Stone's
			case 166020001:
			case 166020002:
			case 166020003:
			case 166020004:
			case 166020005:
			case 166020007:
			case 166020008:
			case 166020006:
			case 166022003:
			case 166022007:
				if (rnd < 5) {
					addLevel = 3;
				} else if (rnd < 15) {
					addLevel = 2;
				} else if (rnd < 100) {
					addLevel = 1;
				}
				break;
			case 166022000: // Irridescent Omega Enchantment Stone's
			case 166022001:
			case 166022002:
				if (rnd < 10) {
					addLevel = 3;
				} else if (rnd < 25) {
					addLevel = 2;
				} else if (rnd <= 100) {
					addLevel = 1;
				}
				break;
			// Probability of enchanting an item by +2 or +3 with just one stone is much higher than for regular items.
			// In case of armor, when using Upgrade Stone there is a possibility of enchanting an item by +4 level with one stone.
			case 166000196: // 5.0 Enchantment Stone
			case 166010001: // 5.6 Shining Enchantment Stone
				if (targetItem.getItemTemplate().getLevel() >= 66 && targetItem.getItemTemplate().isArmor()) {
					if (rnd < 5) {
						addLevel = 4;
					} else if (rnd < 15) {
						addLevel = 3;
					} else if (rnd < 40) {
						addLevel = 2;
					} else if (rnd <= 100) {
						addLevel = 1;
					}
				} else {
					if (rnd < 7) {
						addLevel = 3;
					} else if (rnd < 25) {
						addLevel = 2;
					} else if (rnd <= 100) {
						addLevel = 1;
					}
				}
				break;
			default:
				if (rnd < 2) {
					addLevel = 3;
				} else if (rnd < 7) {
					addLevel = 2;
				} else if (rnd <= 100) {
					addLevel = 1;
				}
				break;
		}

		if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
			AuditLogger.info(player, "Possible enchant hack, do not remove enchant stone.");
			return;
		} else if (targetItem.isAmplified() && player.getInventory().getKinah() < EnchantKinah) {
			AuditLogger.info(player, "Possible enchant hack, do not remove kinah");
			return;
		} else {
			player.getInventory().decreaseKinah(EnchantKinah);			
		}
		// Decrease required supplements
		player.updateSupplements();
		// Items that are Fabled or Eternal can get up to +15.
		// Note: Amplificated items only can enchant over 15 with a
		// Omega Enchantment Stone
		if (result || player.isGM() && AdminConfig.INSTANT_ENCHANT_SUCCESS) {
			switch (targetItem.getItemTemplate().getItemQuality()) {
				case COMMON:
				case RARE:
				case LEGEND:
					if (targetItem.isAmplified()) {
						switch (stoneId) {
							case 166020000: // Omega Enchantment Stone's
							case 166020001:
							case 166020002:
							case 166020003:
							case 166020004:
							case 166020005:
							case 166020007:
							case 166020008:
							case 166020006:
							case 166022003:
							case 166022007:
								currentEnchant += 1;
								break;
							case 166022000: // Irridescent Omega Enchantment Stone's
							case 166022001:
							case 166022002:
								currentEnchant += addLevel;
								break;
							default:
								break;
						}
					} else if (!targetItem.getItemTemplate().isEstima() && !targetItem.isAmplified() && currentEnchant >= getEnchantOrAuthorizeMaxLevel(targetItem) - 1) {
						if (targetItem.getItemTemplate().isArmor()) {
							buffId = getArmorBuff(targetItem);
						} else if (targetItem.getItemTemplate().isWeapon()) {
							buffId = getWeaponBuff(player);
						}
						currentEnchant += 1;
						targetItem.setAmplified(true);
						targetItem.setAmplificationSkill(buffId);
					} else {
						currentEnchant += 1;
					}
					break;
				case UNIQUE:
				case EPIC:
				case MYTHIC:
					if (targetItem.isAmplified()) { // Omega Enchantment Stone's
						switch (stoneId) {
							case 166020000: // Omega Enchantment Stone's
							case 166020001:
							case 166020002:
							case 166020003:
							case 166020004:
							case 166020005:
							case 166020007:
							case 166020008:
							case 166020006:
							case 166022003:
							case 166022007:
								currentEnchant += 1;
								break;
							case 166022000: // Irridescent Omega Enchantment Stone's
							case 166022001:
							case 166022002:
								currentEnchant += addLevel;
								break;
							default:
								break;
						}
					} else if (!targetItem.getItemTemplate().isEstima() && !targetItem.isAmplified() && currentEnchant >= getEnchantOrAuthorizeMaxLevel(targetItem) - 1) {
						if (targetItem.getItemTemplate().isArmor()) {
							buffId = getArmorBuff(targetItem);
						} else if (targetItem.getItemTemplate().isWeapon()) {
							buffId = getWeaponBuff(player);
						}
						currentEnchant += 1;
						targetItem.setAmplified(true);
						targetItem.setAmplificationSkill(buffId);
					} else {
						currentEnchant += 1;
					}
					break;
				case ANCIENT:
				case RELIC:
				case FINALITY:
					int chanceId = getChanceId(targetItem, parentItem);
					ItemEnchantChance eItem = DataManager.ITEM_ENCHANT_CHANCES_DATA.getChanceById(chanceId);
					ItemEnchantChanceList eData = eItem.getChancesById(targetItem.getEnchantOrAuthorizeLevel());
					if (rnd <= eData.getCrit()) {
						currentEnchant += 2;
					} else {
						currentEnchant += 1;
					}
//					System.out.println("ChanceId: " + chanceId);
//					System.out.println("Level: " + eData.getLevel());
//					System.out.println("Chance: " + eData.getChance());
//					System.out.println("Crit: " + eData.getCrit());
//					System.out.println("Random: " + rnd);
//					System.out.println("EnchantType: " + targetItem.getItemTemplate().getEnchantType());
					break;
				default:
					break;
			}
		} else {
			switch(parentItem.getItemTemplate().getItemQuality()) {
				case ANCIENT:
				case RELIC:
				case FINALITY:
					currentEnchant = targetItem.getEnchantOrAuthorizeLevel();
					break;
				default:
					switch (stoneId) {
						case 166020000:
						case 166020001:
						case 166020002:
						case 166020003:
						case 166020004:
						case 166020005:
						case 166022000:
						case 166022001:
						case 166022002:
							if (currentEnchant > 10 && currentEnchant <= getEnchantOrAuthorizeMaxLevel(targetItem)) {
								currentEnchant -= 1;
							}
							break;
						default:
							if (currentEnchant > 10 && !targetItem.isAmplified()) {
								//When socketing fails at +11~+15, the value falls back to +10.
								currentEnchant = 10;
							}
							else if (currentEnchant > 0 && !targetItem.isAmplified()) {
								currentEnchant -= 1;
							}
							else if (targetItem.getItemTemplate().getLevel() >= 66 || targetItem.getItemTemplate().isEstima()) {
								ItemService.addItem(player, 188100335, 500); // Enchantment Stone Dust
								ItemService.addItem(player, RndArray.get(HighDaevaStoneItems), 1);
								player.getInventory().delete(targetItem);
							}
							break;
					}
					break;
			}
		}
		if (targetItem.isAmplified() && currentEnchant < 15) {
			targetItem.setAmplified(false);
			int skillId = targetItem.getAmplificationSkill();
			if (player.getSkillList().isSkillPresent(skillId)) {
				SkillLearnService.removeSkill(player, skillId);
			}
		}
		
		targetItem.setEnchantOrAuthorizeLevel(currentEnchant);
		ItemPacketService.updateItemAfterInfoChange(player, targetItem);
		
		if (result) {
			if (targetItem.isEquipped()) {
				player.getGameStats().updateStatsVisually();
				player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
			} else {
				player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
			}
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEED_NEW(new DescriptionId(targetItem.getNameId()), targetItem.getEnchantOrAuthorizeLevel()));
		} else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(new DescriptionId(targetItem.getNameId())));
		}
	}
	
	private static int getChanceId(Item targetItem, Item parentItem) {
		switch (targetItem.getItemTemplate().getEnchantType()) {
			case PVP: {
				switch (targetItem.getItemTemplate().getItemQuality()) {
					case ANCIENT: {
						switch (parentItem.getItemTemplate().getItemQuality()) {
							case ANCIENT:
								return 2; 
							case RELIC:
								return 5;
							case FINALITY:
								return 8;
							default:
								break;
						}
						break;
					}
					case RELIC: {
						switch (parentItem.getItemTemplate().getItemQuality()) {
							case ANCIENT:
								return 3; 
							case RELIC:
								return 6;
							case FINALITY:
								return 9;
							default:
								break;
						}
						break;
					}
					case FINALITY: {
						switch (parentItem.getItemTemplate().getItemQuality()) {
							case ANCIENT:
								return 4; 
							case RELIC:
								return 7;
							case FINALITY:
								return 10;
							default:
								break;
						}
						break;
					}
					default:
						break;
				}
				break;
			}
			case PVE: {
				switch (targetItem.getItemTemplate().getItemQuality()) {
					case ANCIENT: {
						switch (parentItem.getItemTemplate().getItemQuality()) {
							case ANCIENT:
								return 11; 
							case RELIC:
								return 14;
							case FINALITY:
								return 17;
							default:
								break;
						}
						break;
					}
					case RELIC: {
						switch (parentItem.getItemTemplate().getItemQuality()) {
							case ANCIENT:
								return 12; 
							case RELIC:
								return 15;
							case FINALITY:
								return 18;
							default:
								break;
						}
						break;
					}
					case FINALITY: {
						switch (parentItem.getItemTemplate().getItemQuality()) {
							case ANCIENT:
								return 13; 
							case RELIC:
								return 16;
							case FINALITY:
								return 19;
							default:
								break;
						}
						break;
					}
					default:
						break;
				}
				break;
			}
			default:
				break;
		}
		return 0;
	}
	
	private static int getEnchantOrAuthorizeMaxLevel(Item targetItem) {
		int level;
		if (targetItem.getItemTemplate().getMaxAuthorize() > 0) {
			level = targetItem.getItemTemplate().getMaxAuthorize();
			return level;
		} else {
			level = targetItem.getItemTemplate().getMaxEnchantLevel();
			return level;
		}
	}
	
	/**
	 * @param player
	 * @param parentItem  the manastone
	 * @param targetItem  the item to socket
	 * @param supplementItem
	 * @param targetWeapon fusioned weapon
	 */
	public static boolean socketManastone(Player player, Item parentItem, Item targetItem, Item supplementItem, int targetWeapon) {

		int targetItemLevel = 1;
		int stoneLevel = parentItem.getItemTemplate().getLevel();
		int slotLevel = (int) (10 * Math.ceil((targetItemLevel + 10) / 10d));
		boolean result = false;

		// Start value of success
		float success = EnchantsConfig.MANA_STONE;

		// Manastone level shouldn't be greater as 20 + item level
		// Example: item level: 1 - 10. Manastone level should be <= 20
		if (stoneLevel > slotLevel) {
			return false;
		}
		
		// The current amount of socketed stones
		int stoneCount = 0;
		if (targetItem.getItemTemplate().isWeapon()) {
			switch (targetWeapon) {
				case 0: { // Fusioned weapon. Primary weapon level.
					targetItemLevel = targetItem.getFusionedItemTemplate().getLevel();
					stoneCount = targetItem.getFusionStones().size(); // Count the inserted stones in the secondary weapon
					if (!targetItem.hasFusionedItem() || stoneCount > targetItem.getSockets(true)) {
						AuditLogger.info(player, "Fusion Manastone socket overload [Weapon ItemId]: " + targetItem.getItemTemplate().getTemplateId());
						return false;
					}
					break;
				}
				case 1: { // Fusioned weapon. Secondary weapon level.
					targetItemLevel = targetItem.getItemTemplate().getLevel();
					stoneCount = targetItem.getItemStones().size(); // Count the inserted stones in the primary weapon
					if (stoneCount > targetItem.getSockets(false)) {
						AuditLogger.info(player, "Manastone socket overload [Weapon ItemId]: " + targetItem.getItemTemplate().getTemplateId());
						return false;
					}
					break;
				}
				default:
					AuditLogger.info(player, "Unknow targetWeapon (Todo)");
					break;
			}
		} else {
			stoneCount = targetItem.getItemStones().size();
			if (stoneCount > targetItem.getSockets(false)) {
				AuditLogger.info(player, "Manastone socket overload [Armor ItemId]: " + targetItem.getItemTemplate().getTemplateId());
				return false;
			}
		}
		
		// Stone quality modifier
		success += parentItem.getItemTemplate().getItemQuality() == ItemQuality.COMMON ? 25f : 15f;

		// Next socket difficulty modifier
		float socketDiff = stoneCount * 1.25f + 1.75f;

		// Level difference
		success += (slotLevel - stoneLevel) / socketDiff;

		// The supplement item is used
		if (supplementItem != null) {
			int supplementUseCount = 0;
			ItemTemplate manastoneTemplate = parentItem.getItemTemplate();

			int manastoneCount;
			if (targetWeapon == 1) { // Not fusioned
				manastoneCount = targetItem.getItemStones().size() + 1;
			} else { // Fusioned
				manastoneCount = targetItem.getFusionStones().size() + 1;
			}

			// Additional success rate for the supplement
			ItemTemplate supplementTemplate = supplementItem.getItemTemplate();
			float addSuccessRate = 0f;

			boolean isManastoneOnly = false;
			EnchantItemAction action = manastoneTemplate.getActions().getEnchantAction();
			if (action != null) {
				supplementUseCount = action.getCount();
			}

			action = supplementTemplate.getActions().getEnchantAction();
			if (action != null) {
				addSuccessRate = action.getChance();
				isManastoneOnly = action.isManastoneOnly();
			}

			// Adjust addsuccessrate to rates in config
			switch (parentItem.getItemTemplate().getItemQuality()) {
				case LEGEND:
					addSuccessRate *= EnchantsConfig.LESSER_SUP;
					break;
				case UNIQUE:
					addSuccessRate *= EnchantsConfig.REGULAR_SUP;
					break;
				case EPIC:
					addSuccessRate *= EnchantsConfig.GREATER_SUP;
					break;
				case MYTHIC:
				case ANCIENT: // TODO
				case RELIC: // TODO
				case FINALITY: // TODO
					addSuccessRate *= EnchantsConfig.MYTHIC_SUP;
					break;
				default:
					break;
			}

			if (isManastoneOnly) {
				supplementUseCount = 1;
			} else if (stoneCount > 0) {
				supplementUseCount = supplementUseCount * manastoneCount;
			}

			if (player.getInventory().getItemCountByItemId(supplementTemplate.getTemplateId()) < supplementUseCount) {
				return false;
			}

			// Add successRate
			success += addSuccessRate;

			// Put up supplements to wait for update
			player.subtractSupplements(supplementUseCount, supplementTemplate.getTemplateId());
		}

		float random = Rnd.get(1, 1000) / 10f;

		if (random <= success) {
			result = true;
		}

		// For test purpose. To use by administrator
		if (player.getAccessLevel() > 2) {
			PacketSendUtility.sendMessage(player, (result ? "Success" : "Fail") + " Rnd:" + random + " Luck:" + success);
		}

		return result;
	}

	public static void socketManastoneAct(Player player, Item parentItem, Item targetItem, int targetWeapon, boolean result) {

		// Decrease required supplements
		player.updateSupplements();
		
		if (player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1) && result) {
			if (parentItem.getItemTemplate().getTemplateId() == 166401000) {
				targetItem.setOptionalSocket(targetItem.getItemTemplate().getOptionSlotBonus());
			}
			switch (targetWeapon) {
				case 0: {
					ManaStone fusionStone = ItemSocketService.addFusionStone(targetItem, parentItem.getItemTemplate().getTemplateId());
					ItemEquipmentListener.addStoneStats(targetItem, fusionStone, player.getGameStats());
					break;
				}
				case 1: {
					ManaStone manaStone = ItemSocketService.addManaStone(targetItem, parentItem.getItemTemplate().getTemplateId());
					ItemEquipmentListener.addStoneStats(targetItem, manaStone, player.getGameStats());
					break;
				}
				default:
					break;
			}
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_SUCCEED(new DescriptionId(targetItem.getNameId())));
		} else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_FAILED(new DescriptionId(targetItem.getNameId())));
		}
		
		if (targetItem.isEquipped()) {
			player.getGameStats().updateStatsAndSpeedVisually();			
		}
		ItemPacketService.updateItemAfterInfoChange(player, targetItem);
	}

	/**
	 * @param player
	 * @param item
	 */
	public static void onItemEquip(Player player, Item item) {
		List<IStatFunction> modifiers = new ArrayList<IStatFunction>();
		try {
			if (item.getItemTemplate().isWeapon()) {
				switch (item.getItemTemplate().getWeaponType()) {
					case BOOK_2H:
					case ORB_2H:
					case HARP_2H:
					case GUN_1H:
					case CANNON_2H:
					case KEYBLADE_2H:
						modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
						modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ATTACK));
						break;
					case MACE_1H:
					case STAFF_2H:
						modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
						break;
					case DAGGER_1H:
					case BOW:
					case POLEARM_2H:
					case SWORD_1H:
					case SWORD_2H:
					case KEYHAMMER_2H:
						modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
						if (item.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask()) {
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAIN_HAND_POWER));
						} else {
							modifiers.add(new StatEnchantFunction(item, StatEnum.OFF_HAND_POWER));
						}
						break;
					default:
						break;
				}

				// For the future...it looks like ncsoft will enable it also for weapons
				if (item.getItemTemplate().getAuthorizeName() > 0) {
					ItemEnchantTemplate ie = DataManager.ITEM_ENCHANT_DATA.getEnchantTemplate(item.getItemTemplate().getAuthorizeName());
					if (item.getItemTemplate().getMaxAuthorize() > 0 && ie != null) {
						try {
							modifiers.addAll(ie.getStats(item.getEnchantOrAuthorizeLevel()));
						}
						catch (Exception e) {
							log.error("Cant add tempering modifiers for item: " + item.getItemId() + " , " + ie.getStats(item.getEnchantOrAuthorizeLevel()));
						}
					}
				}

				if (CustomConfig.ENABLE_ENCHANT_BONUS) {
					ItemEnchantTable it = DataManager.ITEM_ENCHANT_TABLE_DATA.getTableWeapon(item.getItemTemplate().getCategory());
					if (item.getItemTemplate().getMaxAuthorize() > 0 && it != null && item.getEnchantOrAuthorizeLevel() < 22) {
						try {
							modifiers.addAll(it.getStats(item.getEnchantOrAuthorizeLevel()));
						}
						catch (Exception ex) {
							log.error("Cant add enchant table modifiers for item: " + item.getItemId() + " , " + it.getStats(item.getEnchantOrAuthorizeLevel()));
						}
					}
				}

				if (item.isAmplified() && item.getEnchantOrAuthorizeLevel() >= 20) {
					player.getSkillList().addSkill(player, item.getAmplificationSkill(), 1);
				}
			} else if (item.getItemTemplate().isArmor() && item.getItemTemplate().getItemSlot() != 32768) {
				switch (item.getItemTemplate().getCategory()) {
					case HELMET:
					case EARRINGS:
					case NECKLACE: {
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO_PHYSICAL));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO_MAGICAL));
						break;
					}
					case RINGS:
					case BELT: {
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO_PHYSICAL));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO_MAGICAL));
						break;
					}
					case BRACELET: {
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO_PHYSICAL));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO_MAGICAL));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO_PHYSICAL));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO_MAGICAL));
						break;
					}
					case PLUME: {
						int id = item.getItemTemplate().getAuthorizeName();
						switch (id) {
							case 10051: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10052: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10056: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10057: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10063: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10064: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10065: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10066: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10103: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10104: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10105: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 11105: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10106: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 11106: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10107: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10108: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10109: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10110: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10223: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 11223: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10224: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 11224: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10225: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10226: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10227: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 10228: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 11000: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
							 // modifiers.add(new StatEnchantFunction(item, StatEnum.PVE_ATTACK_RATIO));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 11001: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
							 // modifiers.add(new StatEnchantFunction(item, StatEnum.PVE_DEFEND_RATIO));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 11002: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							case 11003: {
								modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
								modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
								break;
							}
							default:
								break;
						}
					}
					default: // Other (default) Armor
						if (item.getItemTemplate().getArmorType() != ArmorType.SHIELD) {
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_DEFENSE));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_DEFEND));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL_RESIST));
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK)); // 4.9
							modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL)); // 4.9
						}
						break;
				}
			} else if (item.getItemTemplate().isArmor() && item.getItemTemplate().getItemSlot() == 32768) { // 5.0 Wings Enchant
				modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
				modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
				modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
				modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL_RESIST));
				modifiers.add(new StatEnchantFunction(item, StatEnum.FLY_TIME));
				modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_CRITICAL_RESIST));
			}
			else if (item.getItemTemplate().getArmorType() == ArmorType.SHIELD) {
				modifiers.add(new StatEnchantFunction(item, StatEnum.DAMAGE_REDUCE));
				modifiers.add(new StatEnchantFunction(item, StatEnum.BLOCK));
			}
			
			// For the future...it looks like ncsoft will enable it also for armors
			if (item.getItemTemplate().getAuthorizeName() > 0 && !item.getItemTemplate().isAccessory()) {
				ItemEnchantTemplate ie = DataManager.ITEM_ENCHANT_DATA.getEnchantTemplate(item.getItemTemplate().getAuthorizeName());
				if (item.getEnchantOrAuthorizeLevel() > 0 && ie != null) {
					try {
						modifiers.addAll(ie.getStats(item.getEnchantOrAuthorizeLevel()));
					}
					catch (Exception e) {
						log.error("Cant add tempering modifiers for item: " + item.getItemId() + " , " + ie.getStats(item.getEnchantOrAuthorizeLevel()));
					}
				}
			}
				
			if (CustomConfig.ENABLE_ENCHANT_BONUS && !item.getItemTemplate().isAccessory()) {
				ItemEnchantTable it = DataManager.ITEM_ENCHANT_TABLE_DATA.getTableArmor(item.getItemTemplate().getArmorType(), item.getItemTemplate().getCategory());
				if (item.getEnchantOrAuthorizeLevel() > 0 && it != null && item.getEnchantOrAuthorizeLevel() < 22) {
					try {
						modifiers.addAll(it.getStats(item.getEnchantOrAuthorizeLevel()));
					}
					catch (Exception ex) {
						log.error("Cant add enchant table modifiers for item: " + item.getItemId() + " , " + it.getStats(item.getEnchantOrAuthorizeLevel()));
					}
				}
			}
			
			if (item.isAmplified() && item.getEnchantOrAuthorizeLevel() >= 20) {
				player.getSkillList().addSkill(player, item.getAmplificationSkill(), 1);
			}
			
			if (CustomConfig.ENABLE_ENCHANT_BONUS && item.getItemTemplate().getCategory() != ItemCategory.PLUME && item.getEnchantOrAuthorizeLevel() > 0) {
				ItemEnchantTable it = DataManager.ITEM_ENCHANT_TABLE_DATA.getTableAuthorize();
				if (item.getEnchantOrAuthorizeLevel() > 0 && it != null && item.getEnchantOrAuthorizeLevel() < 22) {
					try {
						modifiers.addAll(it.getStats(item.getEnchantOrAuthorizeLevel()));
					}
					catch (Exception ex) {
						log.error("Cant add enchant table modifiers for item: " + item.getItemId() + " , " + it.getStats(item.getEnchantOrAuthorizeLevel()));
					}
				}
			}
			if (!modifiers.isEmpty()) {
				player.getGameStats().addEffect(item, modifiers);
				player.getGameStats().updateStatsAndSpeedVisually();
			}
		}
		catch (Exception ex) {
			log.error("Error on item equip.", ex);
		}
	}

	/**
	 * new 4.9 http://aionpowerbook.com/powerbook/Glory:_Shield
	 */
	public static void getGloryShield(Player player) {
		int armorEnchanted = 0;
		int weaponEnchanted = 0;
		int skillId = player.getRace() == Race.ASMODIANS ? 4695 : 4694;
		Equipment equip = player.getEquipment();

		if (equip == null) {
			return;
		}

		for (Item item : equip.getEquippedItemsWithoutStigma()) {
			if (item.getEnchantOrAuthorizeLevel() >= 20) {
				if (item.getItemTemplate().isArmor()) {
					armorEnchanted++;
				}
			} else if (item.getItemTemplate().isWeapon()) {
				weaponEnchanted++;
			}
		}

		if (armorEnchanted >= 5 && weaponEnchanted >= 1) {
			if (player.getSkillList().isSkillPresent(4694) || player.getSkillList().isSkillPresent(4695)) {
				return;
			}
			player.getSkillList().addSkill(player, skillId, 1);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403100));
		} else {
			if (player.getSkillList().isSkillPresent(skillId)) {
				SkillLearnService.removeSkill(player, skillId);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403101));
			}
		}
	}

	public static int EnchantKinah(Item item) {
		if (!item.isAmplified()) {
			return 0;
		}
		ItemQuality quality = item.getItemTemplate().getItemQuality();
		int enchantLevel = item.getEnchantOrAuthorizeLevel();
		int baseKinah = 2500000;
		switch (quality) {
			case RARE:
				if (enchantLevel <= 16) {
					return baseKinah;
				} else if (enchantLevel == 17) {
					return (baseKinah * 2);
				} else if (enchantLevel > 17 && enchantLevel <= 20) {
					return (enchantLevel - 14) * baseKinah;
				} else if (enchantLevel > 20 && enchantLevel <= 25) {
					return (enchantLevel - 17) * (baseKinah * 2);
				} else {
					return (16 * baseKinah);
				}
			case LEGEND:
				if (enchantLevel <= 15) {
					return baseKinah;
				} else if (enchantLevel == 16) {
					return (baseKinah * 2);
				} else if (enchantLevel > 16 && enchantLevel <= 19) {
					return (enchantLevel - 13) * baseKinah;
				} else if (enchantLevel > 19 && enchantLevel <= 25) {
					return (enchantLevel - 16) * (baseKinah * 2);
				} else {
					return (18 * baseKinah);
				}
			case EPIC:
				baseKinah = 5000000;
				if (enchantLevel <= 15) {
					return baseKinah;
				} else if (enchantLevel == 16) {
					return (baseKinah * 2);
				} else if (enchantLevel > 16 && enchantLevel <= 18) {
					return (enchantLevel - 13) * baseKinah;
				} else if (enchantLevel > 18) {
					return (enchantLevel - 16) * (baseKinah * 2);
				} else {
					return (18 * baseKinah);
				}
			case MYTHIC:
			case ANCIENT: // TODO
			case RELIC: // TODO
			case FINALITY: // TODO
				baseKinah = 10000000;
				if (enchantLevel <= 15) {
					return baseKinah;
				} else if (enchantLevel == 16) {
					return (baseKinah * 2);
				} else if (enchantLevel > 16 && enchantLevel <= 19) {
					return (enchantLevel - 13) * baseKinah;
				} else if (enchantLevel > 19 && enchantLevel <= 25) {
					return (enchantLevel - 16) * (baseKinah * 2);
				} else {
					return (18 * baseKinah);
				}
			default:
				return 0;
		}
	}

	public static void reductItemAct(Player player, Item parentItem, Item targetItem, int currentReduction, boolean result, int count) {
		if (result) {
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemId(), 0, 1));
			if (currentReduction + count > 5) {
				targetItem.setReductionLevel(5);
			} else {
				targetItem.setReductionLevel(currentReduction + count);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_SUCCEED(targetItem.getNameId(), count));
			}
			
			if (targetItem.getReductionLevel() == 5) {
				// The max. recommended level reduction for %0 has been reached.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_SUCCEED_MAX(targetItem.getNameId()));
			}
		} else {
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemId(), 0, 2));
			// The reduction of %0 recommended level failed.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_FAIL(targetItem.getNameId()));
		}
		
		PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
		
		if (targetItem.isEquipped()) {
			player.getGameStats().updateStatsVisually();
			player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
		} else {
			player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
		}

		ItemPacketService.updateItemAfterInfoChange(player, targetItem);
	}
}
