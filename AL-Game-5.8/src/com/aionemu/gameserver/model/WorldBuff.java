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

import javolution.util.FastList;

/**
 * @author Steve
 */
public class WorldBuff {

	private int worldId;
	private FastList<Integer> skillId;

	public WorldBuff() {
	}

	public WorldBuff(FastList<Integer> skillId, int worldId) {
		this.worldId = worldId;
		this.skillId = skillId;
	}

	public int getWorldId() {
		return worldId;
	}

	public FastList<Integer> getSkillIds() {
		if (skillId == null) {
			skillId = FastList.newInstance();
		}
		return skillId;
	}
}
