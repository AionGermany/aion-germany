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
package com.aionemu.gameserver.model.templates.housing;

/**
 * @author Rolandas
 */
public enum HouseType {

	ESTATE(0, 3, "a"),
	MANSION(1, 2, "b"),
	HOUSE(2, 1, "c"),
	STUDIO(3, 0, "d"),
	PALACE(4, 4, "s");

	private HouseType(int index, int id, String abbrev) {
		this.abbrev = abbrev;
		this.limitTypeIndex = index;
		this.id = id;
	}

	private String abbrev;
	private int limitTypeIndex;
	private int id;

	public String getAbbreviation() {
		return abbrev;
	}

	public int getLimitTypeIndex() {
		return limitTypeIndex;
	}

	public int getId() {
		return id;
	}

	public String value() {
		return name();
	}

	public static HouseType fromValue(String value) {
		return valueOf(value);
	}
}
