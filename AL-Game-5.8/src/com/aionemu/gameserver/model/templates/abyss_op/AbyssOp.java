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
package com.aionemu.gameserver.model.templates.abyss_op;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

@XmlType(name = "abyss_op")
@XmlAccessorType(XmlAccessType.NONE)
public class AbyssOp {

	@XmlAttribute(name = "id", required = true)
	private int id;

	@XmlAttribute(name = "npc_id", required = true)
	private int npcId;

	@XmlAttribute(name = "type", required = true)
	private AbyssOpType abyssOpType;

	@XmlAttribute(name = "siege_id", required = true)
	private int siegeId;

	@XmlAttribute(name = "race")
	protected Race race = Race.PC_ALL;

	@XmlAttribute(name = "group_id", required = true)
	private int groupId;

	@XmlAttribute(name = "points", required = true)
	private int points;

	public int getId() {
		return this.id;
	}

	public AbyssOpType getAbyssOpType() {
		return abyssOpType;
	}

	public Race getRace() {
		return race;
	}

	public int getSiegeId() {
		return siegeId;
	}

	public int getGroupId() {
		return groupId;
	}

	public int getPoints() {
		return points;
	}

	public int getNpcId() {
		return npcId;
	}
}
