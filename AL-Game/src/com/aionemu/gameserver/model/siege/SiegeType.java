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
package com.aionemu.gameserver.model.siege;

/**
 * @author Sarynth
 */
public enum SiegeType {
	// Standard

	FORTRESS(0),
	ARTIFACT(1),
	// Balauria Commanders?
	BOSSRAID_LIGHT(2),
	BOSSRAID_DARK(3),
	// Unk
	INDUN(4),
	UNDERPASS(5),
	SOURCE(6);

	private int typeId;

	private SiegeType(int id) {
		this.typeId = id;
	}

	public int getTypeId() {
		return this.typeId;
	}
}
