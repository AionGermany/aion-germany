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
package com.aionemu.gameserver.model.stats.calc.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;

/**
 * @author ATracer (based on Mr.Poke EnchantModifier)
 */
public class StatEnchantFunction extends StatAddFunction {

	private static final Logger log = LoggerFactory.getLogger(StatEnchantFunction.class);
	private Item item;
	private int point;

	public StatEnchantFunction(Item owner, StatEnum stat, int point) {
		this.stat = stat;
		this.item = owner;
		this.point = point;
	}

	@Override
	public final int getPriority() {
		return 30;
	}

	@Override
	public void apply(Stat2 stat) {
		if (!item.isEquipped()) {
			return;
		}
		int enchantLvl = this.item.getEnchantLevel();
		if (item.getItemTemplate().isAccessory() || item.getItemTemplate().getCategory() == ItemCategory.HELMET) {
			enchantLvl = item.getAuthorize();
		}
		if (enchantLvl == 0) {
			return;
		}
		if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_HAND.getSlotIdMask()) != 0 || (item.getEquipmentSlot() & ItemSlot.SUB_OFF_HAND.getSlotIdMask()) != 0) {
			return;
		}
		if (item.getItemTemplate().getArmorType() == ArmorType.PLUME) {
			stat.addToBonus(getEnchantAdditionModifier(enchantLvl, stat));
		}
		else {
			stat.addToBase(getEnchantAdditionModifier(enchantLvl, stat));
		}
	}

	private int getEnchantAdditionModifier(int enchantLvl, Stat2 stat) {
		// Enchantment stats gained each level at +21 and higher for wep is doubled
		if (item.getItemTemplate().isWeapon()) {
			if (item.getEnchantLevel() > 20) {
				return (getWeaponModifiers(enchantLvl) * 2);
			}
			return getWeaponModifiers(enchantLvl);
		}
		if (item.getItemTemplate().isAccessory() && !item.getItemTemplate().isPlume()) {
			if (point == 0) {
				return getAccessoryModifiers(enchantLvl);
			}
			return point;
		}
		if (item.getItemTemplate().isArmor() || item.getItemTemplate().isPlume()) {
			// Enchantment stats gained each level at +21 and higher for armor is doubled
			if (item.getItemTemplate().isArmor() && item.getEnchantLevel() > 20) {
				return (getArmorModifiers(enchantLvl, stat) * 2);
			}
			return getArmorModifiers(enchantLvl, stat);
		}
		return 0;
	}

	private int getWeaponModifiers(int enchantLvl) {
		switch (stat) {
			case MAIN_HAND_POWER:
			case OFF_HAND_POWER:
			case PHYSICAL_ATTACK:
				switch (item.getItemTemplate().getWeaponType()) {
					case DAGGER_1H:
					case SWORD_1H:
						return 2 * enchantLvl;
					case POLEARM_2H:
					case SWORD_2H:
					case BOW:
						return 4 * enchantLvl;
					case MACE_1H:
					case STAFF_2H:
						return 3 * enchantLvl;
					default:
						break;
				}
				return 0;
			case BOOST_MAGICAL_SKILL:
				switch (item.getItemTemplate().getWeaponType()) {
					case BOOK_2H:
					case MACE_1H:
					case STAFF_2H:
					case ORB_2H:
					case GUN_1H:
					case CANNON_2H:
					case HARP_2H:
					case KEYBLADE_2H:
						return 20 * enchantLvl;
					default:
						break;
				}
				return 0;
			case MAGICAL_ATTACK:
				switch (item.getItemTemplate().getWeaponType()) {
					case GUN_1H:
						return 2 * enchantLvl;
					case BOOK_2H:
					case ORB_2H:
						return 3 * enchantLvl;
					case CANNON_2H:
					case HARP_2H:
					case KEYBLADE_2H:
						return 4 * enchantLvl;
					default:
						break;
				}
				return 0;
			default:
				return 0;
		}
	}

	private int getAccessoryModifiers(int autorizeLvl) {
		switch (stat) {
			case PVP_ATTACK_RATIO:
			case PVP_ATTACK_RATIO_PHYSICAL:
			case PVP_ATTACK_RATIO_MAGICAL: {
				switch (item.getItemTemplate().getCategory()) {
					case HELMET:
					case EARRINGS:
					case NECKLACE: {
						return 5 * autorizeLvl;
					}
					case BRACELET: {
						switch (autorizeLvl) {
							case 6:
								return 5;
							case 8:
								return 10;
							case 9:
								return 15;
							case 10:
								return 20;
							default:
								break;
						}
					}
					default:
						break;
				}
			}
			case PVP_DEFEND_RATIO:
			case PVP_DEFEND_RATIO_PHYSICAL:
			case PVP_DEFEND_RATIO_MAGICAL: {
				switch (item.getItemTemplate().getCategory()) {
					case RINGS:
					case BELT: {
						return 7 * autorizeLvl;
					}
					case BRACELET: {
						switch (autorizeLvl) {
							case 1:
								return 3;
							case 2:
								return 7;
							case 3:
								return 11;
							case 4:
								return 16;
							case 5:
								return 21;
							case 6:
								return 27;
							case 7:
								return 33;
							case 8:
								return 40;
							case 9:
								return 48;
							case 10:
								return 57;
							default:
								break;
						}
					}
					default:
						break;
				}
			}
			default:
				break;
		}
		return 0;
	}

	private int getArmorModifiers(int enchantLvl, Stat2 applyStat) {
		ArmorType armorType = item.getItemTemplate().getArmorType();
		if (armorType == null) {
			log.warn("Missing item ArmorType itemId: " + item.getItemId() + " EquipmentSlot: " + item.getEquipmentSlot() + " playerObjectId: " + applyStat.getOwner().getObjectId());
			return 0;
		}

		int equipmentSlot = (int) (item.getEquipmentSlot() & 0xFFFFFFFF);
		switch (item.getItemTemplate().getArmorType()) {
			case ROBE: // 4.9 check
				switch (equipmentSlot) {
					case 1 << 5:
					case 1 << 11:
					case 1 << 4:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return enchantLvl;
							case MAXHP:
								return 20 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 2 * enchantLvl;
							case MAGICAL_DEFEND:
								return 2 * enchantLvl;
							default:
								break;
						}
						return 0;
					case 1 << 12:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 2 * enchantLvl;
							case MAXHP:
								return 22 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 3 * enchantLvl;
							case MAGICAL_DEFEND:
								return 2 * enchantLvl;
							default:
								break;
						}
						return 0;
					case 1 << 3:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 3 * enchantLvl;
							case MAXHP:
								return 24 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 4 * enchantLvl;
							case MAGICAL_DEFEND:
								return 3 * enchantLvl;
							default:
								break;
						}
						return 0;
				}
				return 0;
			case LEATHER: // 4.9 check
				switch (equipmentSlot) {
					case 1 << 5:
					case 1 << 11:
					case 1 << 4:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 2 * enchantLvl;
							case MAXHP:
								return 18 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 2 * enchantLvl;
							case MAGICAL_DEFEND:
								return 2 * enchantLvl;
							default:
								break;
						}
						return 0;
					case 1 << 12:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 3 * enchantLvl;
							case MAXHP:
								return 20 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 3 * enchantLvl;
							case MAGICAL_DEFEND:
								return 2 * enchantLvl;
							default:
								break;
						}
						return 0;
					case 1 << 3:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 4 * enchantLvl;
							case MAXHP:
								return 22 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 4 * enchantLvl;
							case MAGICAL_DEFEND:
								return 3 * enchantLvl;
							default:
								break;
						}
						return 0;
				}
				return 0;
			case CHAIN: // 4.9 check
				switch (equipmentSlot) {
					case 1 << 5:
					case 1 << 11:
					case 1 << 4:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 3 * enchantLvl;
							case MAXHP:
								return 16 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 2 * enchantLvl;
							case MAGICAL_DEFEND:
								return 2 * enchantLvl;
							default:
								break;
						}
						return 0;
					case 1 << 12:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 4 * enchantLvl;
							case MAXHP:
								return 18 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 3 * enchantLvl;
							case MAGICAL_DEFEND:
								return 2 * enchantLvl;
							default:
								break;
						}
						return 0;
					case 1 << 3:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 5 * enchantLvl;
							case MAXHP:
								return 20 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 4 * enchantLvl;
							case MAGICAL_DEFEND:
								return 3 * enchantLvl;
							default:
								break;
						}
						return 0;
				}
				return 0;
			case PLATE: // 4.9 check
				switch (equipmentSlot) {
					case 1 << 5:
					case 1 << 11:
					case 1 << 4:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 4 * enchantLvl;
							case MAXHP:
								return 14 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 2 * enchantLvl;
							case MAGICAL_DEFEND:
								return 2 * enchantLvl;
							default:
								break;
						}
						return 0;
					case 1 << 12:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 5 * enchantLvl;
							case MAXHP:
								return 16 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 3 * enchantLvl;
							case MAGICAL_DEFEND:
								return 2 * enchantLvl;
							default:
								break;
						}
						return 0;
					case 1 << 3:
						switch (stat) {
							case PHYSICAL_ATTACK:
								return 1 * enchantLvl;
							case BOOST_MAGICAL_SKILL:
								return 4 * enchantLvl;
							case PHYSICAL_DEFENSE:
								return 6 * enchantLvl;
							case MAXHP:
								return 18 * enchantLvl;
							case PHYSICAL_CRITICAL_RESIST:
								return 4 * enchantLvl;
							case MAGICAL_DEFEND:
								return 3 * enchantLvl;
							default:
								break;
						}
						return 0;
				}
				return 0;
			case SHIELD: // 4.9 check
				switch (stat) {
					case DAMAGE_REDUCE:
						float reduceRate = enchantLvl > 10 ? 0.2f : enchantLvl * 0.02f;
						return Math.round(reduceRate * applyStat.getBase());
					case BLOCK:
						if (enchantLvl > 10) {
							return 30 * (enchantLvl - 10);
						}
						return 0;
					case MAXHP: // TODO
					case PHYSICAL_DEFENSE: // TODO
					case MAGIC_SKILL_BOOST_RESIST: // TODO
						if (item.getEnchantLevel() > 20) {
							// Need to find how much it adds per level.
							// How it works..
							// After enchanting to +21 or higher HP, Physical Defense and
							// Magic Suppresion is being added for each level of enchantment
							return 0;
						}
						return 0;
					default:
						break;
				}
			case PLUME: // 5.3 Added PHYSICAL_ACCURACY and MAGICAL_CRITICAL
				switch (stat) {
					case MAXHP:
						return 150 * enchantLvl;
					case PHYSICAL_ATTACK:
						return 4 * enchantLvl;
					case BOOST_MAGICAL_SKILL:
						return 20 * enchantLvl;
					case PHYSICAL_CRITICAL:
						return 12 * enchantLvl;
					case MAGICAL_ACCURACY:
						return 8 * enchantLvl;
					case PHYSICAL_ACCURACY:
						return 16 * enchantLvl;
					case MAGICAL_CRITICAL:
						return 8 * enchantLvl;
					default:
						break;
				}
				break;
			case WING: // 5.1
				switch (stat) {
					case PHYSICAL_ATTACK:
						return 1 * enchantLvl;
					case BOOST_MAGICAL_SKILL:
						return 4 * enchantLvl;
					case MAXHP:
						return 40 * enchantLvl;
					case PHYSICAL_CRITICAL_RESIST:
						return 2 * enchantLvl;
					case FLY_TIME:
						return 10 * enchantLvl;
					case MAGICAL_CRITICAL_RESIST:
						return 2 * enchantLvl;
					default:
						break;
				}
				break;
			default:
				break;
		}
		return 0;
	}
}
