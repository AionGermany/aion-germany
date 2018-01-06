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
package com.aionemu.gameserver.model.templates.pet;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlType(name = "FoodType")
@XmlEnum
public enum FoodType {

	AETHER_CRYSTAL_BISCUIT,
	AETHER_GEM_BISCUIT,
	AETHER_POWDER_BISCUIT,
	ARMOR,
	BALAUR_SCALES,
	BONES,
	EXCLUDES, // Excluded items
	FLUIDS,
	HEALTHY_FOOD_ALL,
	HEALTHY_FOOD_SPICY,
	MISCELLANEOUS,
	POPPY_SNACK,
	POPPY_SNACK_TASTY,
	POPPY_SNACK_NUTRITIOUS,
	SOULS,
	SHUGO_EVENT_COIN,
	STINKY, // Other excuded items
	THORNS;

	public String value() {
		return name();
	}

	public static FoodType fromValue(String value) {
		return valueOf(value);
	}
}
