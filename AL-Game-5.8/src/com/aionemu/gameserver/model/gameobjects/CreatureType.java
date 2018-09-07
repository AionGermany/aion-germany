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
package com.aionemu.gameserver.model.gameobjects;

/**
 * @author Luno modified by Wakizashi (CHEST)
 */
public enum CreatureType {

	NULL(-1),
	/**
	 * These are regular monsters
	 */
	ATTACKABLE(0),
	/**
	 * These are Peace npc
	 */
	PEACE(2),
	/**
	 * These are monsters that are pre-aggressive
	 */
	AGGRESSIVE(8),
	// unk
	INVULNERABLE(10),
	/**
	 * These are non attackable NPCs
	 */
	FRIEND(38),
	SUPPORT(54);

	private int someClientSideId;

	private CreatureType(int id) {
		this.someClientSideId = id;
	}

	public int getId() {
		return someClientSideId;
	}

	public static CreatureType getCreatureType(int id) {
		for (CreatureType ct : values()) {
			if (ct.getId() == id) {
				return ct;
			}
		}
		return null;
	}
}
