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
package com.aionemu.gameserver.model.stats.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.aionemu.gameserver.configs.main.HighDaevaConfig;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.RandomStats;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.CreatureGameStats;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.WeaponStats;
import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.model.templates.itemset.FullBonus;
import com.aionemu.gameserver.model.templates.itemset.ItemSetTemplate;
import com.aionemu.gameserver.model.templates.itemset.PartBonus;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.services.StigmaService;

/**
 * @author xavier modified by Wakizashi rework Phantom_KNA
 */
public class ItemEquipmentListener {

	/**
	 * @param item
	 * @param cgs
	 */
	public static void onItemEquipment(Item item, Player owner) {
		owner.getController().cancelUseItem();
		ItemTemplate itemTemplate = item.getItemTemplate();

		onItemEquipment(item, owner.getGameStats(), owner);

		// Check if belongs to ItemSet
		if (itemTemplate.isItemSet()) {
			recalculateItemSet(itemTemplate.getItemSet(), owner, item.getItemTemplate().isWeapon());
		}
		if (item.hasManaStones()) {
			addStonesStats(item, item.getItemStones(), owner.getGameStats());
		}

		if (item.hasFusionStones()) {
			addStonesStats(item, item.getFusionStones(), owner.getGameStats());
		}
		IdianStone idianStone = item.getIdianStone();
		if (idianStone != null) {
			idianStone.onEquip(owner);
		}
		addGodstoneEffect(owner, item);
		RandomStats randomStats = item.getRandomStats();
		if (randomStats != null) {
			randomStats.onEquip(owner);
		}
		if (item.getConditioningInfo() != null) {
			owner.getObserveController().addObserver(item.getConditioningInfo());
			item.getConditioningInfo().setPlayer(owner);
		}
		if (item.getAmplificationSkill() > 0) {
			owner.getSkillList().addSkill(owner, item.getAmplificationSkill(), 1);
		}
		if (item.getItemTemplate().isStigma()) {
			StigmaService.recheckHiddenStigma(owner);
		}
		EnchantService.onItemEquip(owner, item);
		EnchantService.getGloryShield(owner);
	}

	/**
	 * @param item
	 * @param owner
	 */
	public static void onItemUnequipment(Item item, Player owner) {
		owner.getController().cancelUseItem();

		ItemTemplate itemTemplate = item.getItemTemplate();
		// Check if belongs to ItemSet
		if (itemTemplate.isItemSet()) {
			recalculateItemSet(itemTemplate.getItemSet(), owner, item.getItemTemplate().isWeapon());
		}

		owner.getGameStats().endEffect(item);

		if (item.hasManaStones()) {
			removeStoneStats(item.getItemStones(), owner.getGameStats());
		}

		if (item.hasFusionStones()) {
			removeStoneStats(item.getFusionStones(), owner.getGameStats());
		}

		if (item.getConditioningInfo() != null) {
			owner.getObserveController().removeObserver(item.getConditioningInfo());
			item.getConditioningInfo().setPlayer(null);
		}
		IdianStone idianStone = item.getIdianStone();
		if (idianStone != null) {
			idianStone.onUnEquip(owner);
		}
		removeGodstoneEffect(owner, item);
		RandomStats randomStats = item.getRandomStats();
		if (randomStats != null) {
			randomStats.onUnEquip(owner);
		}
		if (item.isAmplified() && item.getEnchantLevel() >= 20) {
			SkillLearnService.removeSkill(owner, item.getAmplificationSkill());
		}
		if (item.getItemTemplate().isStigma()) {
			StigmaService.recheckHiddenStigma(owner);
		}
		EnchantService.getGloryShield(owner);
	}

	/**
	 * @param itemTemplate
	 * @param slot
	 * @param cgs
	 */
	private static void onItemEquipment(Item item, CreatureGameStats<?> cgs, Player player) {
		ItemTemplate itemTemplate = item.getItemTemplate();
		long slot = item.getEquipmentSlot();
		List<StatFunction> modifiers = itemTemplate.getModifiers();
		if (modifiers == null) {
			return;
		}

		List<StatFunction> allModifiers = null;
//		List<StatFunction> decreaseAllModifiers = null;

		if ((slot & ItemSlot.MAIN_OR_SUB.getSlotIdMask()) != 0) {
			allModifiers = wrapModifiers(item, modifiers);
			if (item.hasFusionedItem()) {
				// add all bonus modifiers according to rules
				ItemTemplate fusionedItemTemplate = item.getFusionedItemTemplate();
				WeaponType weaponType = fusionedItemTemplate.getWeaponType();
				List<StatFunction> fusionedItemModifiers = fusionedItemTemplate.getModifiers();
				if (fusionedItemModifiers != null) {
					allModifiers.addAll(wrapModifiers(item, fusionedItemModifiers));
				}
				// add 10% of Magic Boost and Attack
				WeaponStats weaponStats = fusionedItemTemplate.getWeaponStats();
				if (weaponStats != null) {
					int boostMagicalSkill = Math.round(0.1f * weaponStats.getBoostMagicalSkill());
					int attack = Math.round(0.1f * weaponStats.getMeanDamage());
					if (weaponType == WeaponType.ORB_2H || weaponType == WeaponType.BOOK_2H || weaponType == WeaponType.GUN_1H || weaponType == WeaponType.CANNON_2H || weaponType == WeaponType.HARP_2H || weaponType == WeaponType.KEYBLADE_2H) {
						allModifiers.add(new StatAddFunction(StatEnum.MAGICAL_ATTACK, attack, false));
						allModifiers.add(new StatAddFunction(StatEnum.BOOST_MAGICAL_SKILL, boostMagicalSkill, false));
					}
					else {
						allModifiers.add(new StatAddFunction(StatEnum.MAIN_HAND_POWER, attack, false));
					}
				}
			}
			if (HighDaevaConfig.ITEM_NOT_FOR_HIGHDAEVA_ENABLE) {
				if (player.getLevel() >= 65 && !itemTemplate.isHighdaeva()) {
					for (StatFunction a : modifiers) {
						int value = a.getValue();
						int formula = (int) (value * (20.0f / 100.0f));
						allModifiers.add(new StatAddFunction(a.getName(), -formula, false));
					}
				}
			}
			// HighDaeva item level limitations
			if (player.getLevel() >= 65 && itemTemplate.isHighdaeva()) {
				int pLevel = player.getLevel();
				int iLevel = itemTemplate.getLevel();
				float percentageDecrease = 0;
				if (iLevel - pLevel == 1) {
					percentageDecrease = 2.0f;
				}
				else if (iLevel - pLevel == 2) {
					percentageDecrease = 4.0f;
				}
				else if (iLevel - pLevel == 3) {
					percentageDecrease = 6.0f;
				}
				else if (iLevel - pLevel == 4) {
					percentageDecrease = 8.0f;
				}
				else if (iLevel - pLevel == 5) {
					percentageDecrease = 10.0f;
				}
				else if (iLevel - pLevel == 6) {
					percentageDecrease = 12.0f;
				}
				else if (iLevel - pLevel == 7) {
					percentageDecrease = 14.0f;
				}
				else if (iLevel - pLevel == 8) {
					percentageDecrease = 16.0f;
				}
				else if (iLevel - pLevel == 9) {
					percentageDecrease = 18.0f;
				}
				else if (iLevel - pLevel == 10) {
					percentageDecrease = 20.0f;
				}
				for (StatFunction a : modifiers) {
					int value = a.getValue();
					int formula = (int) (value * (percentageDecrease / 100.0f));
					allModifiers.add(new StatAddFunction(a.getName(), -formula, false));
				}
			}
		}
		else {
			allModifiers = modifiers;
		}
		item.setCurrentModifiers(allModifiers);
		cgs.addEffect(item, allModifiers);
	}

	/**
	 * Filter stats based on the following rules:<br>
	 * 1) don't include fusioned stats which will be taken only from 1 weapon <br>
	 * 2) wrap stats which are different for MAIN and OFF hands<br>
	 * 3) add the rest<br>
	 *
	 * @param item
	 * @param modifiers
	 * @return
	 */
	private static List<StatFunction> wrapModifiers(Item item, List<StatFunction> modifiers) {
		List<StatFunction> allModifiers = new ArrayList<StatFunction>();
		for (StatFunction modifier : modifiers) {
			switch (modifier.getName()) {
				// why they are removed look at DuplicateStatFunction
				case ATTACK_SPEED:
				case PVP_ATTACK_RATIO:
				case PVP_DEFEND_RATIO:
				case BOOST_CASTING_TIME:
					continue;
				default:
					allModifiers.add(modifier);
			}
		}
		return allModifiers;
	}

	/**
	 * @param itemSetTemplate
	 * @param player
	 * @param isWeapon
	 */
	private static void recalculateItemSet(ItemSetTemplate itemSetTemplate, Player player, boolean isWeapon) {
		if (itemSetTemplate == null) {
			return;
		}

		// 1.- Check equipment for items already equip with this itemSetTemplate id
		int itemSetPartsEquipped = player.getEquipment().itemSetPartsEquipped(itemSetTemplate.getId());

		if (itemSetTemplate.getFullbonus() != null) {
			if (itemSetPartsEquipped > itemSetTemplate.getFullbonus().getCount()) { // DO NOT REMOVE
				return;
			}
		}

		player.getGameStats().endEffect(itemSetTemplate);

		// If main hand and off hand is same , no bonus
		int mainHandItemId = 0;
		int offHandItemId = 0;
		if (player.getEquipment().getMainHandWeapon() != null) {
			mainHandItemId = player.getEquipment().getMainHandWeapon().getItemId();
		}
		if (player.getEquipment().getOffHandWeapon() != null) {
			offHandItemId = player.getEquipment().getOffHandWeapon().getItemId();
		}
		boolean mainAndOffNotSame = mainHandItemId != offHandItemId;

		// 2.- Check Item Set Parts and add effects one by one if not done already
		for (PartBonus itempartbonus : itemSetTemplate.getPartbonus()) {
			if (mainAndOffNotSame && isWeapon) {
				// If the partbonus was not applied before, do it now
				if (itempartbonus.getCount() <= itemSetPartsEquipped) {
					player.getGameStats().addEffect(itemSetTemplate, itempartbonus.getModifiers());
				}
			}
			else if (!isWeapon) {
				// If the partbonus was not applied before, do it now
				if (itempartbonus.getCount() <= itemSetPartsEquipped) {
					if (itempartbonus.getModifiers() != null) {
						player.getGameStats().addEffect(itemSetTemplate, itempartbonus.getModifiers());
					}
				}
			}
		}

		// 3.- Finally check if all items are applied and set the full bonus if not already applied
		FullBonus fullbonus = itemSetTemplate.getFullbonus();
		if (fullbonus != null && itemSetPartsEquipped == fullbonus.getCount()) {
			// Add the full bonus with index = total parts + 1 to avoid confusion with part bonus equal to number of
			// objects
			player.getGameStats().addEffect(itemSetTemplate, fullbonus.getModifiers());
		}
	}

	/**
	 * All modifiers of stones will be applied to character
	 *
	 * @param item
	 * @param cgs
	 */
	private static void addStonesStats(Item item, Set<? extends ManaStone> itemStones, CreatureGameStats<?> cgs) {
		if (itemStones == null || itemStones.size() == 0) {
			return;
		}

		for (ManaStone stone : itemStones) {
			addStoneStats(item, stone, cgs);
		}
	}

	/**
	 * Used when socketing of equipped item
	 *
	 * @param item
	 * @param stone
	 * @param cgs
	 */
	public static void addStoneStats(Item item, ManaStone stone, CreatureGameStats<?> cgs) {
		List<StatFunction> modifiers = stone.getModifiers();
		if (modifiers == null) {
			return;
		}

		cgs.addEffect(stone, modifiers);
	}

	/**
	 * All modifiers of stones will be removed
	 *
	 * @param itemStones
	 * @param cgs
	 */
	public static void removeStoneStats(Set<? extends ManaStone> itemStones, CreatureGameStats<?> cgs) {
		if (itemStones == null || itemStones.size() == 0) {
			return;
		}

		for (ManaStone stone : itemStones) {
			List<StatFunction> modifiers = stone.getModifiers();
			if (modifiers != null) {
				cgs.endEffect(stone);
			}
		}
	}

	/**
	 * @param item
	 */
	private static void addGodstoneEffect(Player player, Item item) {
		if (item.getGodStone() != null) {
			item.getGodStone().onEquip(player);
		}
	}

	/**
	 * @param item
	 */
	private static void removeGodstoneEffect(Player player, Item item) {
		if (item.getGodStone() != null) {
			item.getGodStone().onUnEquip(player);
		}
	}

	public static void addIdianBonusStats(Item item, List<StatFunction> modifiers, CreatureGameStats<?> cgs) {
		cgs.addEffect(item, modifiers);
	}

	public static void removeIdianBonusStats(Item item, CreatureGameStats<?> cgs) {
		cgs.endEffect(item);
	}
}
