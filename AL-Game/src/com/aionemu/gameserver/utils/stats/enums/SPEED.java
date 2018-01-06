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
public enum SPEED {

	WARRIOR(6),
	GLADIATOR(6),
	TEMPLAR(6),
	SCOUT(6),
	ASSASSIN(6),
	RANGER(6),
	MAGE(6),
	SORCERER(6),
	SPIRIT_MASTER(6),
	PRIEST(6),
	CLERIC(6),
	CHANTER(6),
	ENGINEER(6),
	RIDER(6),
	GUNNER(6),
	ARTIST(6),
	BARD(6);

	private int value;

	private SPEED(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
