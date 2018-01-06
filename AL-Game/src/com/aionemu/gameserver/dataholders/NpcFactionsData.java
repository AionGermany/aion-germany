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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.factions.NpcFactionTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author vlog
 */
@XmlRootElement(name = "npc_factions")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcFactionsData {

	@XmlElement(name = "npc_faction", required = true)
	protected List<NpcFactionTemplate> npcFactionsData;
	private TIntObjectHashMap<NpcFactionTemplate> factionsById = new TIntObjectHashMap<NpcFactionTemplate>();
	private TIntObjectHashMap<NpcFactionTemplate> factionsByNpcId = new TIntObjectHashMap<NpcFactionTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		factionsById.clear();
		for (NpcFactionTemplate template : npcFactionsData) {
			factionsById.put(template.getId(), template);
			if (template.getNpcId() != 0) {
				factionsByNpcId.put(template.getNpcId(), template);
			}
		}
	}

	public NpcFactionTemplate getNpcFactionById(int id) {
		return factionsById.get(id);
	}

	public NpcFactionTemplate getNpcFactionByNpcId(int id) {
		return factionsByNpcId.get(id);
	}

	public List<NpcFactionTemplate> getNpcFactionsData() {
		return npcFactionsData;
	}

	public int size() {
		return npcFactionsData.size();
	}
}
