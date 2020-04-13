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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.lugbug.LugbugQuestTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Falke_34
 */
@XmlRootElement(name = "lugbug_quests")
@XmlAccessorType(XmlAccessType.FIELD)
public class LugbugQuestData {

	@XmlElement(name = "lugbug_quest")
	private List<LugbugQuestTemplate> lugbug_quests;

	@XmlTransient
	private TIntObjectHashMap<LugbugQuestTemplate> lugbugQuestData = new TIntObjectHashMap<LugbugQuestTemplate>();

	@XmlTransient
	private Map<Integer, LugbugQuestTemplate> lugbugQuestDataMap = new HashMap<Integer, LugbugQuestTemplate>(1);

	void afterUnmarshal(Unmarshaller unmarshaller, Object object) {
		for (LugbugQuestTemplate lugbugQuest : lugbug_quests) {
			lugbugQuestData.put(lugbugQuest.getQuestId(), lugbugQuest);
			lugbugQuestDataMap.put(lugbugQuest.getQuestId(), lugbugQuest);
		}
	}

	public int size() {
		return lugbugQuestData.size();
	}

	public LugbugQuestTemplate getlugbugQuestsId(int id) {
		return lugbugQuestData.get(id);
	}

	public Map<Integer, LugbugQuestTemplate> getAll() {
		return lugbugQuestDataMap;
	}
}
