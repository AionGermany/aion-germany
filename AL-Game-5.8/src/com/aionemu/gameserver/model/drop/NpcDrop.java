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
package com.aionemu.gameserver.model.drop;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author MrPoke
 */
public class NpcDrop implements DropCalculator {

	protected List<DropGroup> dropGroup;
	protected int npcId;

	/**
	 * @param dropGroup
	 * @param npcId
	 */
	public NpcDrop(List<DropGroup> dropGroup, int npcId) {
		super();
		this.dropGroup = dropGroup;
		this.npcId = npcId;
	}

	public List<DropGroup> getDropGroup() {
		if (dropGroup == null) {
			return Collections.emptyList();
		}
		return this.dropGroup;
	}

	/**
	 * Gets the value of the npcId property.
	 */
	public int getNpcId() {
		return npcId;
	}

	@Override
	public int dropCalculator(Set<DropItem> result, int index, float dropModifier, Race race, Collection<Player> groupMembers) {
		if (dropGroup == null || dropGroup.isEmpty()) {
			return index;
		}
		for (DropGroup dg : dropGroup) {
			if (dg.getRace() == Race.PC_ALL || dg.getRace() == race) {
				index = dg.dropCalculator(result, index, dropModifier, race, groupMembers);
			}
		}
		return index;
	}
}
