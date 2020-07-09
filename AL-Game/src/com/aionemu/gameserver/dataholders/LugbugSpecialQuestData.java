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

import com.aionemu.gameserver.model.templates.lugbug.LugbugSpecialQuestTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Falke_34
 */
@XmlRootElement(name = "lugbug_special_quests")
@XmlAccessorType(XmlAccessType.FIELD)
public class LugbugSpecialQuestData {

	@XmlElement(name = "lugbug_special_quest")
	private List<LugbugSpecialQuestTemplate> lugbug_special_quests;

	@XmlTransient
	private TIntObjectHashMap<LugbugSpecialQuestTemplate> lugbugSpecialQuestData = new TIntObjectHashMap<LugbugSpecialQuestTemplate>();

	@XmlTransient
	private Map<Integer, LugbugSpecialQuestTemplate> lugbugSpecialQuestDataMap = new HashMap<Integer, LugbugSpecialQuestTemplate>(1);

	void afterUnmarshal(Unmarshaller unmarshaller, Object object) {
		for (LugbugSpecialQuestTemplate lugbugSpecialQuest : lugbug_special_quests) {
			lugbugSpecialQuestData.put(lugbugSpecialQuest.getId(), lugbugSpecialQuest);
			lugbugSpecialQuestDataMap.put(lugbugSpecialQuest.getId(), lugbugSpecialQuest);
		}
	}

	public int size() {
		return lugbugSpecialQuestData.size();
	}

	public LugbugSpecialQuestTemplate getlugbugSpecialQuestsId(int id) {
		return lugbugSpecialQuestData.get(id);
	}

	public Map<Integer, LugbugSpecialQuestTemplate> getAll() {
		return lugbugSpecialQuestDataMap;
	}
}
