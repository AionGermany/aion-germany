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
package com.aionemu.gameserver.utils.stats.enums;

/**
 * @author ATracer
 */
public enum FLY_SPEED {

	WARRIOR(9),
	GLADIATOR(9),
	TEMPLAR(9),
	SCOUT(9),
	ASSASSIN(9),
	RANGER(9),
	MAGE(9),
	SORCERER(9),
	SPIRIT_MASTER(9),
	PRIEST(9),
	CLERIC(9),
	CHANTER(9),
	ENGINEER(9),
	RIDER(9),
	GUNNER(9),
	ARTIST(9),
	BARD(9);

	private int value;

	private FLY_SPEED(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
