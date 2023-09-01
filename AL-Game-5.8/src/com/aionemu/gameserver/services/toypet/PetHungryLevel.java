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
package com.aionemu.gameserver.services.toypet;

/**
 * @author Rolandas
 */
public enum PetHungryLevel {

	HUNGRY(0),
	CONTENT(1),
	SEMIFULL(2),
	FULL(3);

	private byte value;

	PetHungryLevel(int value) {
		this.value = (byte) value;
	}

	/**
	 * @return the value
	 */
	public byte getValue() {
		return value;
	}

	public PetHungryLevel getNextValue() {
		byte levelValue = value;
		switch (levelValue) {
			case 0:
				return CONTENT;
			case 1:
				return SEMIFULL;
			case 2:
				return FULL;
			case 3:
				return HUNGRY;
			default:
				return HUNGRY;
		}
	}

	public static PetHungryLevel fromId(int value) {
		return PetHungryLevel.values()[value];
	}
}
