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
 * @author ATracer
 */
@XmlType(name = "armor_type")
@XmlEnum
public enum ArmorType {

	NO_ARMOR(new int[] {}),
	CHAIN(new int[] { 42, 49 }),
	CLOTHES(new int[] { 40 }),
	LEATHER(new int[] { 41, 48 }),
	PLATE(new int[] { 54 }),
	ROBE(new int[] { 103, 106 }),
	SHIELD(new int[] { 43, 50 }),
	ARROW(new int[] {}),
	WING(new int[] {}),
	PLUME(new int[] {}),
	ACCESSORY(new int[] {});

	private int[] requiredSkills;

	private ArmorType(int[] requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public int[] getRequiredSkills() {
		return requiredSkills;
	}

	/**
	 * @return int
	 */
	public int getMask() {
		return 1 << this.ordinal();
	}
}
