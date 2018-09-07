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
package com.aionemu.gameserver.skillengine.model;

/**
 * @author MrPoke
 */
public enum SkillMoveType {

	RESIST(0),
	DEFAULT(16),
	PULL(50), // OLD 18 NEW 50 (5.6)
	OPENAERIAL(20),
	KNOCKBACK(28),
	MOVEBEHIND(48),
	STAGGER(112), // 5.1
	STUMBLE(16), // 5.1
	NEWPULL(54); // 5.1

	private int id;

	private SkillMoveType(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}
