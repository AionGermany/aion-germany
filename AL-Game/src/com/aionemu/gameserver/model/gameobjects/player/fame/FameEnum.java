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
package com.aionemu.gameserver.model.gameobjects.player.fame;

import com.aionemu.gameserver.model.DescriptionId;

public enum FameEnum {

	INGGISON(1, 210050000, 1831919), 
	GELKMAROS(2, 220070000, 1831921), 
	SILENTERA_CANYON(3, 600010000, 1831923), 
	LAKRUM(4, 800050000, 1831927), 
	DUMAHA(5, 800060000, 1831929), 
	CRIMSON_KATALAM(6, 800030000, 1831925), 
	CRIMSON_DANARIA(6, 800040000, 1831925);

	private int value;
	private int worldId;
	private DescriptionId descriptionId;

	private FameEnum(int value, int worlId, int descriptionId) {
		this.value = value;
		this.descriptionId = new DescriptionId(descriptionId);
		this.worldId = worlId;
	}

	public static FameEnum getFameById(int value) {
		for (FameEnum pc : FameEnum.values()) {
			if (pc.getValue() != value)
				continue;
			return pc;
		}
		throw new IllegalArgumentException("There is no fame class with id " + value);
	}

	public int getValue() {
		return value;
	}

	public int getWorldId() {
		return worldId;
	}

	public DescriptionId getDescriptionId() {
		return descriptionId;
	}
}
