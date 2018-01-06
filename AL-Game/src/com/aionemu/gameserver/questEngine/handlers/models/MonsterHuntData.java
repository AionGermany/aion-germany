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
package com.aionemu.gameserver.questEngine.handlers.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.quest.QuestKill;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.MonsterHunt;

import javolution.util.FastMap;

/**
 * @author MrPoke, modified Bobobear
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonsterHuntData", propOrder = { "monster" })
// @XmlSeeAlso({KillSpawnedData.class, MentorMonsterHuntData.class})
public class MonsterHuntData extends XMLQuest {

	@XmlElement(name = "monster", required = true)
	protected List<Monster> monster;
	@XmlAttribute(name = "start_npc_ids", required = true)
	protected List<Integer> startNpcIds;
	@XmlAttribute(name = "end_npc_ids")
	protected List<Integer> endNpcIds;
	@XmlAttribute(name = "start_dialog_id")
	protected int startDialog;
	@XmlAttribute(name = "end_dialog_id")
	protected int endDialog;
	@XmlAttribute(name = "aggro_start_npcs")
	protected List<Integer> aggroNpcs;
	@XmlAttribute(name = "invasion_world")
	protected int invasionWorld;

	@Override
	public void register(QuestEngine questEngine) {
		FastMap<Monster, Set<Integer>> monsterNpcs = new FastMap<Monster, Set<Integer>>();
		QuestTemplate questTemplate = DataManager.QUEST_DATA.getQuestById(id);
		for (Monster m : monster) {
			if (CustomConfig.QUESTDATA_MONSTER_KILLS) {
				// if sequence numbers specified use it
				if (m.getNpcSequence() != null && questTemplate.getQuestKill() != null) {
					QuestKill killNpcs = null;
					for (int index = 0; index < questTemplate.getQuestKill().size(); index++) {
						if (questTemplate.getQuestKill().get(index).getSequenceNumber() == m.getNpcSequence()) {
							killNpcs = questTemplate.getQuestKill().get(index);
							break;
						}
					}
					if (killNpcs != null) {
						monsterNpcs.put(m, killNpcs.getNpcIds());
					}
				} // if no sequence was specified, check all npc ids to match quest data
					// else if (m.getNpcSequence() == null && questTemplate.getQuestKill() != null) {
					// Set<Integer> npcSet = new HashSet<Integer>(m.getNpcIds());
					// QuestKill matchedKillNpcs = null;
					// int maxMatchCount = 0;
					// for (int index = 0; index < questTemplate.getQuestKill().size(); index++) {
					// QuestKill killNpcs = questTemplate.getQuestKill().get(index);
					// int matchCount = 0;
					// for (int npcId : killNpcs.getNpcIds()) {
					// if (!npcSet.contains(npcId)) {
					// continue;
					// }
					// matchCount++;
					// }
					// if (matchCount > maxMatchCount) {
					// maxMatchCount = matchCount;
					// matchedKillNpcs = killNpcs;
					// }
					// }
					// if (matchedKillNpcs != null) {
					// // add npcs not present in quest data (weird!)
					// npcSet.addAll(matchedKillNpcs.getNpcIds());
					// monsterNpcs.put(m, npcSet);
					// }
					// }
				else {
					monsterNpcs.put(m, new HashSet<Integer>(m.getNpcIds()));
				}
			}
			else {
				monsterNpcs.put(m, new HashSet<Integer>(m.getNpcIds()));
			}
		}
		MonsterHunt template = new MonsterHunt(id, startNpcIds, endNpcIds, monsterNpcs, startDialog, endDialog, aggroNpcs, invasionWorld);
		questEngine.addQuestHandler(template);
	}
}
