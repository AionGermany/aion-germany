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
package com.aionemu.gameserver.model.templates.worldbuff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.TribeClass;

import javolution.util.FastList;

/**
 * @author Steve
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorldBuff")
public class WorldBuffTemplate {

	@XmlAttribute(name = "skill_ids")
	protected FastList<Integer> skillId;
	@XmlAttribute(name = "duration")
	protected int duration;
	@XmlAttribute(name = "no_remove")
	protected boolean noRemove;
	@XmlAttribute(name = "type")
	protected WorldBuffType type;
	@XmlAttribute(name = "npc_tribe")
	protected FastList<TribeClass> tribe;
	@XmlAttribute(name = "npc_ids")
	protected FastList<Integer> npcId;

	public FastList<Integer> getSkillIds() {
		if (skillId == null) {
			return FastList.newInstance();
		}
		return skillId;
	}

	public int getDuration() {
		return duration;
	}

	public boolean isNoRemoveAtDie() {
		return noRemove;
	}

	public WorldBuffType getType() {
		return type;
	}

	public FastList<TribeClass> getTribe() {
		if (tribe == null) {
			tribe = FastList.newInstance();
		}
		return tribe;
	}

	public FastList<Integer> getNpcIds() {
		if (npcId == null) {
			return FastList.newInstance();
		}
		return npcId;
	}
}
