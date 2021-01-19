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
package com.aionemu.gameserver.model;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum InstanceEntryCostEnum {

	KINAH(0), PC_COIN(1), LUNA(2);

	private int typeId;

	private InstanceEntryCostEnum(int type) {
		this.typeId = type;
	}

	public static InstanceEntryCostEnum getCotstId(int type) {
		for (InstanceEntryCostEnum pc : values()) {
			if (pc.getTypeId() == type) {
				return pc;
			}
		}
		throw new IllegalArgumentException("There is no InstanceEntryCostEnum with id " + type);
	}

	public int getTypeId() {
		return typeId;
	}
}
