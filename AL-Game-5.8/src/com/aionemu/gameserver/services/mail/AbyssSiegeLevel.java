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
package com.aionemu.gameserver.services.mail;

/**
 * @author Rolandas
 */
public enum AbyssSiegeLevel {

	NONE(0),
	HERO_DECORATION(1),
	MEDAL(2),
	ELITE_SOLDIER(3),
	VETERAN_SOLDIER(4),
	TITLE(5); // TODO

	private int value;

	AbyssSiegeLevel(int value) {
		this.value = value;
	}

	public int getId() {
		return this.value;
	}

	public static AbyssSiegeLevel getLevelById(int id) {
		for (AbyssSiegeLevel al : values()) {
			if (al.getId() == id) {
				return al;
			}
		}
		throw new IllegalArgumentException("There is no AbyssSiegeLevel with ID " + id);
	}
}
