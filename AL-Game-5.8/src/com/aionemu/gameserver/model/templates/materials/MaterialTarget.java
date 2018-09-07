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
package com.aionemu.gameserver.model.templates.materials;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Rolandas
 */
@XmlType(name = "MaterialTarget")
@XmlEnum
public enum MaterialTarget {

	ALL,
	NPC,
	PLAYER,
	PLAYER_WITH_PET;

	public String value() {
		return name();
	}

	public static MaterialTarget fromValue(String value) {
		return valueOf(value);
	}

	public boolean isTarget(Creature creature) {
		if (this == ALL) {
			return true;
		}
		if (this == NPC) {
			return creature instanceof Npc;
		}
		if (this == PLAYER) {
			return creature instanceof Player;
		}
		if (this == PLAYER_WITH_PET) {
			return creature instanceof Player || creature instanceof Summon && ((Summon) creature).getMaster() != null;
		}
		return false;
	}
}
