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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.QuestService;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "quests")
public class QuestsData {

	@XmlElement(name = "quest", required = true)
	protected List<QuestTemplate> questsData;
	private TIntObjectHashMap<QuestTemplate> questData = new TIntObjectHashMap<QuestTemplate>();
	private TIntObjectHashMap<List<QuestTemplate>> sortedByFactionId = new TIntObjectHashMap<List<QuestTemplate>>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		questData.clear();
		sortedByFactionId.clear();
		for (QuestTemplate quest : questsData) {
			questData.put(quest.getId(), quest);
			int npcFactionId = quest.getNpcFactionId();
			if (npcFactionId == 0) {
				continue;
			}
			if (!sortedByFactionId.containsKey(npcFactionId)) {
				List<QuestTemplate> factionQuests = new ArrayList<QuestTemplate>();
				factionQuests.add(quest);
				sortedByFactionId.put(npcFactionId, factionQuests);
			}
			else {
				sortedByFactionId.get(npcFactionId).add(quest);
			}
		}
	}

	public QuestTemplate getQuestById(int id) {
		return questData.get(id);
	}

	public List<QuestTemplate> getQuestsByNpcFaction(int npcFactionId, Player player) {
		List<QuestTemplate> factionQuests = sortedByFactionId.get(npcFactionId);
		List<QuestTemplate> quests = new ArrayList<QuestTemplate>();
		QuestEnv questEnv = new QuestEnv(null, player, 0, 0);
		for (QuestTemplate questTemplate : factionQuests) {
			if (!QuestEngine.getInstance().isHaveHandler(questTemplate.getId())) {
				continue;
			}
			if (questTemplate.getMinlevelPermitted() != 0 && player.getLevel() < questTemplate.getMinlevelPermitted()) {
				continue;
			}
			questEnv.setQuestId(questTemplate.getId());
			if (QuestService.checkStartConditions(questEnv, false)) {
				quests.add(questTemplate);
			}
		}
		return quests;
	}

	public int size() {
		return questData.size();
	}

	/**
	 * @return the questsData
	 */
	public List<QuestTemplate> getQuestsData() {
		return questsData;
	}

	/**
	 * @param questsData
	 *            the questsData to set
	 */
	public void setQuestsData(List<QuestTemplate> questsData) {
		this.questsData = questsData;
		afterUnmarshal(null, null);
	}
}
