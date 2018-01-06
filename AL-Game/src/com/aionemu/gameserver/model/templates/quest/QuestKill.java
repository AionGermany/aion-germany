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
package com.aionemu.gameserver.model.templates.quest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestKill")
public class QuestKill {

	@XmlAttribute(name = "seq")
	private int seq;
	@XmlAttribute(name = "npc_ids")
	private List<Integer> npcIds;
	@XmlTransient
	private Set<Integer> npcIdSet;

	/**
	 * @return the seq
	 */
	public int getSequenceNumber() {
		return seq;
	}

	/**
	 * @return the npcIds
	 */
	public Set<Integer> getNpcIds() {
		if (npcIdSet == null) {
			npcIdSet = new HashSet<Integer>();
		}
		if (npcIds != null) {
			npcIdSet.addAll(npcIds);
			npcIds.clear();
			npcIds = null;
		}
		return npcIdSet;
	}
}
