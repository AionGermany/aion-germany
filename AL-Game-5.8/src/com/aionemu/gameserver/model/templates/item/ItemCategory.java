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
import javax.xml.bind.annotation.XmlType;

/**
 * @author LokiReborn
 */
@XmlType(name = "item_category")
@XmlEnum
public enum ItemCategory {

	MANASTONE,
	ANCIENT_MANASTONE,
	GODSTONE,
	AMPLIFICATION,
	ENCHANTMENT,
	FLUX,
	BALIC_EMOTION,
	BALIC_MATERIAL,
	RAWHIDE,
	SOULSTONE,
	RECIPE,
	GATHERABLE,
	GATHERABLE_BONUS,
	DROP_MATERIAL,
	SWORD,
	DAGGER,
	MACE,
	ORB,
	SPELLBOOK,
	GREATSWORD,
	POLEARM,
	STAFF,
	BOW,
	SHIELD,
	HARP,
	GUN,
	CANNON,
	KEYBLADE,
	JACKET,
	PANTS,
	SHARD,
	SHOES,
	GLOVES,
	SHOULDERS,
	NECKLACE,
	EARRINGS,
	RINGS,
	HELMET,
	BELT,
	SKILLBOOK,
	STIGMA,
	COINS,
	MEDALS,
	QUEST,
	KEY,
	TEMPERING,
	CRAFT_BOOST,
	COMBINATION,
	PLUME,
	STENCHANTMENT,
	BRACELET,
	ESTIMA,
	NONE
}
