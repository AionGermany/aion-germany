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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author vlog
 * @author GiGatR00n v4.7.5.x
 */
@XmlEnum
public enum RandomType {

	ENCHANTMENT,
	MANASTONE,
	MANASTONE_COMMON_GRADE_10(10),
	MANASTONE_COMMON_GRADE_20(20),
	MANASTONE_COMMON_GRADE_30(30),
	MANASTONE_COMMON_GRADE_40(40),
	MANASTONE_COMMON_GRADE_50(50),
	MANASTONE_COMMON_GRADE_60(60),
	MANASTONE_COMMON_GRADE_70(70),
	MANASTONE_RARE_GRADE_10(10),
	MANASTONE_RARE_GRADE_20(20),
	MANASTONE_RARE_GRADE_30(30),
	MANASTONE_RARE_GRADE_40(40),
	MANASTONE_RARE_GRADE_50(50),
	MANASTONE_RARE_GRADE_60(60),
	MANASTONE_RARE_GRADE_70(70),
	MANASTONE_LEGEND_GRADE_10(10),
	MANASTONE_LEGEND_GRADE_20(20),
	MANASTONE_LEGEND_GRADE_30(30),
	MANASTONE_LEGEND_GRADE_40(40),
	MANASTONE_LEGEND_GRADE_50(50),
	MANASTONE_LEGEND_GRADE_60(60),
	MANASTONE_LEGEND_GRADE_70(70),
	MANASTONE_EPIC_GRADE_50(50), // The EPIC Manastones: Ancient and Stamp (Unavailable)
	MANASTONE_EPIC_GRADE_60(60), // The EPIC Manastones: Ancient and Stamp (Unavailable)
	MANASTONE_EPIC_GRADE_70(70), // The EPIC Manastones: Ancient and Stamp (Available)
	FINE_SUPERB_BETRAYER_CLOTHSET_65,
	FINE_SUPERB_BETRAYER_LEATHERSET_65,
	FINE_SUPERB_BETRAYER_CHAINSET_65,
	FINE_SUPERB_BETRAYER_PLATESET_65,
	FINE_SUPERB_BETRAYER_GLADIATOR_WEAPON_65,
	FINE_SUPERB_BETRAYER_TEMPLAR_WEAPON_65,
	FINE_SUPERB_BETRAYER_PRIEST_WEAPON_65,
	FINE_SUPERB_BETRAYER_MAGE_WEAPON_65,
	FINE_SUPERB_BETRAYER_SCOUT_WEAPON_65,
	FINE_SUPERB_BETRAYER_BARD_WEAPON_65,
	FINE_SUPERB_BETRAYER_RIDER_WEAPON_65,
	FINE_SUPERB_BETRAYER_AETHERTECH_WEAPON_65,
	KUNAX_GLADIATOR_WEAPON_65,
	KUNAX_TEMPLAR_WEAPON_65,
	KUNAX_PRIEST_WEAPON_65,
	KUNAX_MAGE_WEAPON_65,
	KUNAX_SCOUT_WEAPON_65,
	KUNAX_BARD_WEAPON_65,
	KUNAX_RIDER_WEAPON_65,
	KUNAX_AETHERTECH_WEAPON_65,
	KUNAX_CLOTHSET_65,
	KUNAX_LEATHERSET_65,
	KUNAX_CHAINSET_65,
	KUNAX_PLATESET_65,
	KUNAX_HELMET_CLOTH_65,
	KUNAX_HELMET_LEATHER_65,
	KUNAX_HELMET_CHAIN_65,
	KUNAX_HELMET_PLATE_65,
	KUNAX_ACCESSORY_MAGICAL_65,
	KUNAX_ACCESSORY_PHYSICAL_65,
	IDIAN_EPIC,
	IDIAN_ICY_LEGEND,
	IDIAN_CELESTIAL_EPIC,
	IDIAN_TRIUMPHAL_EPIC,
	IDIAN_GOLDEN_EPIC,
	IDIAN_HARLOCK_EPIC,
	IDIAN_INFUSED_EPIC,
	IDIAN_TIDAL_UNIQUE,
	IDIAN_NOBLE_TIDAL_EPIC,
	IDIAN_BLAZING_FIGHTER_EPIC,
	ANCIENTITEMS,
	CHUNK_EARTH,
	CHUNK_ROCK,
	CHUNK_SAND,
	CHUNK_GEMSTONE,
	SCROLLS,
	POTION;

	private int level;

	private RandomType() {
	}

	private RandomType(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
}
