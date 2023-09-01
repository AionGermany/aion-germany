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
package com.aionemu.gameserver.model.broker;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.broker.filter.BrokerContainsExtraFilter;
import com.aionemu.gameserver.model.broker.filter.BrokerContainsFilter;
import com.aionemu.gameserver.model.broker.filter.BrokerFilter;
import com.aionemu.gameserver.model.broker.filter.BrokerMinMaxFilter;
import com.aionemu.gameserver.model.broker.filter.BrokerPlayerClassExtraFilter;
import com.aionemu.gameserver.model.broker.filter.BrokerRecipeFilter;
import com.aionemu.gameserver.model.gameobjects.Item;

/**
 * @author kosyachok
 * @author Simple
 * @author ATracer
 */
public enum BrokerItemMask {

	/**
	 * Weapon Section + sub categories
	 */
	WEAPON(9010, new BrokerMinMaxFilter(1000, 1018), null, true),
	WEAPON_SWORD(1000, new BrokerContainsFilter(1000), BrokerItemMask.WEAPON, false),
	WEAPON_MACE(1001, new BrokerContainsFilter(1001), BrokerItemMask.WEAPON, false),
	WEAPON_DAGGER(1002, new BrokerContainsFilter(1002), BrokerItemMask.WEAPON, false),
	WEAPON_ORB(1005, new BrokerContainsFilter(1005), BrokerItemMask.WEAPON, false),
	WEAPON_SPELLBOOK(1006, new BrokerContainsFilter(1006), BrokerItemMask.WEAPON, false),
	WEAPON_GREATSWORD(1009, new BrokerContainsFilter(1009), BrokerItemMask.WEAPON, false),
	WEAPON_POLEARM(1013, new BrokerContainsFilter(1013), BrokerItemMask.WEAPON, false),
	WEAPON_STAFF(1015, new BrokerContainsFilter(1015), BrokerItemMask.WEAPON, false),
	WEAPON_BOW(1017, new BrokerContainsFilter(1017), BrokerItemMask.WEAPON, false),
	WEAPON_GUN(1018, new BrokerContainsFilter(1018), BrokerItemMask.WEAPON, false),
	WEAPON_CANNON(1019, new BrokerContainsFilter(1019), BrokerItemMask.WEAPON, false),
	WEAPON_KEYBLADE(1020, new BrokerContainsFilter(1020), BrokerItemMask.WEAPON, false),
	WEAPON_KEYHAMMER(1021, new BrokerContainsFilter(1021), BrokerItemMask.WEAPON, false),
	/**
	 * Armor Section + sub categories
	 */
	ARMOR(9020, new BrokerMinMaxFilter(1101, 1160), null, true),
	ARMOR_CLOTHING(8010, new BrokerContainsFilter(1100, 1110, 1120, 1130, 1140), BrokerItemMask.ARMOR, true),
	ARMOR_CLOTHING_JACKET(1100, new BrokerContainsFilter(1100), BrokerItemMask.ARMOR_CLOTHING, false),
	ARMOR_CLOTHING_GLOVES(1110, new BrokerContainsFilter(1110), BrokerItemMask.ARMOR_CLOTHING, false),
	ARMOR_CLOTHING_PAULDRONS(1120, new BrokerContainsFilter(1120), BrokerItemMask.ARMOR_CLOTHING, false),
	ARMOR_CLOTHING_PANTS(1130, new BrokerContainsFilter(1130), BrokerItemMask.ARMOR_CLOTHING, false),
	ARMOR_CLOTHING_SHOES(1140, new BrokerContainsFilter(1140), BrokerItemMask.ARMOR_CLOTHING, false),
	ARMOR_CLOTH(8020, new BrokerContainsFilter(1101, 1111, 1121, 1131, 1141), BrokerItemMask.ARMOR, true),
	ARMOR_CLOTH_JACKET(1101, new BrokerContainsFilter(1101), BrokerItemMask.ARMOR_CLOTH, false),
	ARMOR_CLOTH_GLOVES(1111, new BrokerContainsFilter(1111), BrokerItemMask.ARMOR_CLOTH, false),
	ARMOR_CLOTH_PAULDRONS(1121, new BrokerContainsFilter(1121), BrokerItemMask.ARMOR_CLOTH, false),
	ARMOR_CLOTH_PANTS(1131, new BrokerContainsFilter(1131), BrokerItemMask.ARMOR_CLOTH, false),
	ARMOR_CLOTH_SHOES(1141, new BrokerContainsFilter(1141), BrokerItemMask.ARMOR_CLOTH, false),
	ARMOR_LEATHER(8030, new BrokerContainsFilter(1103, 1113, 1123, 1133, 1143), BrokerItemMask.ARMOR, true),
	ARMOR_LEATHER_JACKET(1103, new BrokerContainsFilter(1103), BrokerItemMask.ARMOR_LEATHER, false),
	ARMOR_LEATHER_GLOVES(1113, new BrokerContainsFilter(1113), BrokerItemMask.ARMOR_LEATHER, false),
	ARMOR_LEATHER_PAULDRONS(1123, new BrokerContainsFilter(1123), BrokerItemMask.ARMOR_LEATHER, false),
	ARMOR_LEATHER_PANTS(1133, new BrokerContainsFilter(1133), BrokerItemMask.ARMOR_LEATHER, false),
	ARMOR_LEATHER_SHOES(1143, new BrokerContainsFilter(1143), BrokerItemMask.ARMOR_LEATHER, false),
	ARMOR_CHAIN(8040, new BrokerContainsFilter(1105, 1115, 1125, 1135, 1145), BrokerItemMask.ARMOR, true),
	ARMOR_CHAIN_JACKET(1105, new BrokerContainsFilter(1105), BrokerItemMask.ARMOR_CHAIN, false),
	ARMOR_CHAIN_GLOVES(1115, new BrokerContainsFilter(1115), BrokerItemMask.ARMOR_CHAIN, false),
	ARMOR_CHAIN_PAULDRONS(1125, new BrokerContainsFilter(1125), BrokerItemMask.ARMOR_CHAIN, false),
	ARMOR_CHAIN_PANTS(1135, new BrokerContainsFilter(1135), BrokerItemMask.ARMOR_CHAIN, false),
	ARMOR_CHAIN_SHOES(1145, new BrokerContainsFilter(1145), BrokerItemMask.ARMOR_CHAIN, false),
	ARMOR_PLATE(8050, new BrokerContainsFilter(1106, 1116, 1126, 1136, 1146), BrokerItemMask.ARMOR, true),
	ARMOR_PLATE_JACKET(1106, new BrokerContainsFilter(1106), BrokerItemMask.ARMOR_PLATE, false),
	ARMOR_PLATE_GLOVES(1116, new BrokerContainsFilter(1116), BrokerItemMask.ARMOR_PLATE, false),
	ARMOR_PLATE_PAULDRONS(1126, new BrokerContainsFilter(1126), BrokerItemMask.ARMOR_PLATE, false),
	ARMOR_PLATE_PANTS(1136, new BrokerContainsFilter(1136), BrokerItemMask.ARMOR_PLATE, false),
	ARMOR_PLATE_SHOES(1146, new BrokerContainsFilter(1146), BrokerItemMask.ARMOR_PLATE, false),
	ARMOR_SHIELD(1150, new BrokerContainsFilter(1150), BrokerItemMask.ARMOR, false),
	/**
	 * Accessory Section + sub categories
	 */
	ACCESSORY(9030, new BrokerMinMaxFilter(1200, 1270), null, true),
	ACCESSORY_EARRINGS(1200, new BrokerContainsFilter(1200), BrokerItemMask.ACCESSORY, false),
	ACCESSORY_NECKLACE(1210, new BrokerContainsFilter(1210), BrokerItemMask.ACCESSORY, false),
	ACCESSORY_RING(1220, new BrokerContainsFilter(1220), BrokerItemMask.ACCESSORY, false),
	ACCESSORY_BELT(1230, new BrokerContainsFilter(1230), BrokerItemMask.ACCESSORY, false),
	ACCESSORY_HEADGEAR(7030, new BrokerMinMaxFilter(1250, 1270), BrokerItemMask.ACCESSORY, false),
	/**
	 * Skill related Section + sub categories
	 */
	SKILL_RELATED(9040, new BrokerContainsFilter(1400, 1695), null, true),
	SKILL_RELATED_STIGMA(1400, new BrokerContainsFilter(1400), BrokerItemMask.SKILL_RELATED, true),
	SKILL_RELATED_STIGMA_GLADIATOR(6010, new BrokerPlayerClassExtraFilter(1400, PlayerClass.GLADIATOR), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_TEMPLAR(6011, new BrokerPlayerClassExtraFilter(1400, PlayerClass.TEMPLAR), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_ASSASSIN(6012, new BrokerPlayerClassExtraFilter(1400, PlayerClass.ASSASSIN), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_RANGER(6013, new BrokerPlayerClassExtraFilter(1400, PlayerClass.RANGER), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_SORCERER(6014, new BrokerPlayerClassExtraFilter(1400, PlayerClass.SORCERER), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_SPIRITMASTER(6015, new BrokerPlayerClassExtraFilter(1400, PlayerClass.SPIRIT_MASTER), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_CLERIC(6016, new BrokerPlayerClassExtraFilter(1400, PlayerClass.CLERIC), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_CHANTER(6017, new BrokerPlayerClassExtraFilter(1400, PlayerClass.CHANTER), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_BARD(6018, new BrokerPlayerClassExtraFilter(1400, PlayerClass.BARD), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_GUNNER(6019, new BrokerPlayerClassExtraFilter(1400, PlayerClass.GUNNER), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_STIGMA_RIDER(6020, new BrokerPlayerClassExtraFilter(1400, PlayerClass.RIDER), BrokerItemMask.SKILL_RELATED_STIGMA, false),
	SKILL_RELATED_SKILL_MANUAL(1695, new BrokerContainsFilter(1695), BrokerItemMask.SKILL_RELATED, true),
	SKILL_RELATED_SKILL_MANUAL_GLADIATOR(6020, new BrokerPlayerClassExtraFilter(1695, PlayerClass.GLADIATOR), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_TEMPLAR(6021, new BrokerPlayerClassExtraFilter(1695, PlayerClass.TEMPLAR), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_ASSASSIN(6022, new BrokerPlayerClassExtraFilter(1695, PlayerClass.ASSASSIN), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_RANGER(6023, new BrokerPlayerClassExtraFilter(1695, PlayerClass.RANGER), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_SORCERER(6024, new BrokerPlayerClassExtraFilter(1695, PlayerClass.SORCERER), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_SPIRITMASTER(6025, new BrokerPlayerClassExtraFilter(1695, PlayerClass.SPIRIT_MASTER), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_CLERIC(6026, new BrokerPlayerClassExtraFilter(1695, PlayerClass.CLERIC), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_CHANTER(6027, new BrokerPlayerClassExtraFilter(1695, PlayerClass.CHANTER), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_BARD(6028, new BrokerPlayerClassExtraFilter(1695, PlayerClass.BARD), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_GUNNER(6029, new BrokerPlayerClassExtraFilter(1695, PlayerClass.GUNNER), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	SKILL_RELATED_SKILL_MANUAL_RIDER(6030, new BrokerPlayerClassExtraFilter(1695, PlayerClass.RIDER), BrokerItemMask.SKILL_RELATED_SKILL_MANUAL, false),
	/**
	 * Home Decor Section + sub categories
	 */
	HOME_DECOR(9070, new BrokerContainsFilter(1710, 1711), null, true),
	HOME_DECOR_OUT_DOOR(1710, new BrokerContainsFilter(1710), BrokerItemMask.HOME_DECOR, false),
	HOME_DECOR_IN_DOOR(1711, new BrokerContainsFilter(1711), BrokerItemMask.HOME_DECOR, false),
	/**
	 * Furniture Section + sub categories
	 */
	FURNITURE(9080, new BrokerContainsFilter(1700, 1701, 1702, 1703, 1704), null, true),
	FURNITURE_OUT_DOOR(1703, new BrokerContainsFilter(1703), BrokerItemMask.FURNITURE, false),
	FURNITURE_IN_DOOR(8070, new BrokerContainsFilter(1700, 1701, 1702), BrokerItemMask.FURNITURE, true),
	FURNITURE_IN_DOOR_WALL_MOUNTED(1700, new BrokerContainsFilter(1700), BrokerItemMask.FURNITURE_IN_DOOR, false),
	FURNITURE_IN_DOOR_FREE_STANDING(1701, new BrokerContainsFilter(1701), BrokerItemMask.FURNITURE_IN_DOOR, false),
	FURNITURE_IN_DOOR_RUGS(1702, new BrokerContainsFilter(1702), BrokerItemMask.FURNITURE_IN_DOOR, false),
	FURNITURE_IN_DOOR_OUT_DOOR(1704, new BrokerContainsFilter(1704), BrokerItemMask.FURNITURE, false),
	/**
	 * Craft Section + sub categories
	 */
	CRAFT(9050, new BrokerContainsFilter(1520, 1522), null, true),
	CRAFT_MATERIALS(1520, new BrokerContainsFilter(1520), BrokerItemMask.CRAFT, true),
	CRAFT_MATERIALS_COLLECTION(6030, new BrokerContainsExtraFilter(15200), BrokerItemMask.CRAFT_MATERIALS, false),
	CRAFT_MATERIALS_GAIN(6031, new BrokerContainsExtraFilter(15201), BrokerItemMask.CRAFT_MATERIALS, false),
	CRAFT_MATERIALS_PARTS(6032, new BrokerContainsExtraFilter(15202), BrokerItemMask.CRAFT_MATERIALS, false),
	CRAFT_DESIGN(1522, new BrokerContainsFilter(1522), BrokerItemMask.CRAFT, true),
	CRAFT_DESIGN_WEAPONSMITHING(6040, new BrokerRecipeFilter(40002, 1522), BrokerItemMask.CRAFT_DESIGN, false),
	CRAFT_DESIGN_ARMORSMITHING(6041, new BrokerRecipeFilter(40003, 1522), BrokerItemMask.CRAFT_DESIGN, false),
	CRAFT_DESIGN_TAILORING(6042, new BrokerRecipeFilter(40004, 1522), BrokerItemMask.CRAFT_DESIGN, false),
	CRAFT_DESIGN_HANDICRAFTING(6043, new BrokerRecipeFilter(40008, 1522), BrokerItemMask.CRAFT_DESIGN, false),
	CRAFT_DESIGN_ALCHEMY(6044, new BrokerRecipeFilter(40007, 1522), BrokerItemMask.CRAFT_DESIGN, false),
	CRAFT_DESIGN_COOKING(6045, new BrokerRecipeFilter(40001, 1522), BrokerItemMask.CRAFT_DESIGN, false),
	CRAFT_DESIGN_CONSTRUCTION(6046, new BrokerRecipeFilter(40010, 1522), BrokerItemMask.CRAFT_DESIGN, false),
	/**
	 * Consumables Section + sub categories
	 */
	CONSUMABLES(9060, new BrokerContainsFilter(1410, 1600, 1620, 1640, 1660, 16603, 1661, 1665, 1670, 1680, 1690, 16912, 1692, 1693, 1694, 1696), null, true),
	CONSUMABLES_FOOD(1600, new BrokerContainsFilter(1600), BrokerItemMask.CONSUMABLES, false),
	CONSUMABLES_POTION(1620, new BrokerContainsFilter(1620), BrokerItemMask.CONSUMABLES, false),
	CONSUMABLES_SCROLL(7060, new BrokerContainsFilter(1640), BrokerItemMask.CONSUMABLES, false),
	CONSUMABLES_MODIFY(8060, new BrokerContainsFilter(1660, 1665, 1670, 16603, 1680, 1692, 16912), BrokerItemMask.CONSUMABLES, true),
	CONSUMABLES_MODIFY_ENCHANTMENT_STONE(1660, new BrokerContainsFilter(1660), BrokerItemMask.CONSUMABLES_MODIFY, false),
	CONSUMABLES_MODIFY_MANASTONE(1670, new BrokerContainsFilter(1670), BrokerItemMask.CONSUMABLES_MODIFY, false),
	CONSUMABLES_MODIFY_TEMPERING(7064, new BrokerContainsExtraFilter(16603), BrokerItemMask.CONSUMABLES_MODIFY, false), // 4.7 (166030001 to 166030006)
	CONSUMABLES_MODIFY_GODSTONE(1680, new BrokerContainsFilter(1680), BrokerItemMask.CONSUMABLES_MODIFY, false),
	CONSUMABLES_MODIFY_DYE(7061, new BrokerContainsFilter(1692), BrokerItemMask.CONSUMABLES_MODIFY, false),
	CONSUMABLES_MODIFY_PAINT(7065, new BrokerContainsExtraFilter(16912), BrokerItemMask.CONSUMABLES_MODIFY, false),
	CONSUMABLES_MODIFY_AMPLIFICATION_STONE(7066, new BrokerContainsFilter(1665), BrokerItemMask.CONSUMABLES_MODIFY, false), // 1665 amplification stone
	CONSUMABLES_MODIFY_OTHER(7063, new BrokerContainsFilter(1661), BrokerItemMask.CONSUMABLES_MODIFY, false),
	CONSUMABLES_OTHER(7062, new BrokerContainsFilter(1410, 1690, 1693, 1694, 1696), BrokerItemMask.CONSUMABLES, false),
	/**
	 * Other Section
	 */
	OTHER(7070, new BrokerContainsFilter(1850, 1860, 1870, 1880, 1881, 1887), null, false),
	UNKNOWN(1, new BrokerContainsFilter(0), null, false);

	private int typeId;
	private BrokerFilter filter;
	private BrokerItemMask parent;
	private boolean childrenExist;

	/**
	 * @param typeId
	 * @param filter
	 * @param parent
	 * @param childrenExist
	 */
	private BrokerItemMask(int typeId, BrokerFilter filter, BrokerItemMask parent, boolean childrenExist) {
		this.typeId = typeId;
		this.filter = filter;
		this.parent = parent;
		this.childrenExist = childrenExist;
	}

	/**
	 * @return the typeId
	 */
	public int getId() {
		return typeId;
	}

	/**
	 * @param item
	 * @return
	 */
	public boolean isMatches(Item item) {
		return filter.accept(item.getItemTemplate());
	}

	/**
	 * @param maskId
	 * @return
	 */
	public boolean isChildrenMask(int maskId) {
		for (BrokerItemMask p = parent; p != null; p = p.parent) {
			if (p.typeId == maskId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return BrokerListType by id.
	 *
	 * @param id
	 * @return BrokerListType
	 */
	public static BrokerItemMask getBrokerMaskById(int id) {
		for (BrokerItemMask mt : values()) {
			if (mt.typeId == id) {
				return mt;
			}
		}
		return UNKNOWN;
	}

	/**
	 * @return the childrenExist
	 */
	public boolean hasChildren() {
		return childrenExist;
	}
}
