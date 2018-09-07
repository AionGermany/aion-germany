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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.challenge.ChallengeQuestTemplate;
import com.aionemu.gameserver.model.templates.challenge.ChallengeTaskTemplate;

/**
 * @author ViAl
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "task" })
@XmlRootElement(name = "challenge_tasks")
public class ChallengeData {

	protected List<ChallengeTaskTemplate> task;
	@XmlTransient
	protected Map<Integer, ChallengeTaskTemplate> tasksById = new HashMap<Integer, ChallengeTaskTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (ChallengeTaskTemplate t : task) {
			tasksById.put(t.getId(), t);
		}
		task.clear();
		task = null;
	}

	public Map<Integer, ChallengeTaskTemplate> getTasks() {
		return this.tasksById;
	}

	public ChallengeTaskTemplate getTaskByTaskId(int taskId) {
		return tasksById.get(taskId);
	}

	public ChallengeTaskTemplate getTaskByQuestId(int questId) {
		for (ChallengeTaskTemplate ct : tasksById.values()) {
			for (ChallengeQuestTemplate cq : ct.getQuests()) {
				if (cq.getId() == questId) {
					return ct;
				}
			}
		}
		return null;
	}

	public ChallengeQuestTemplate getQuestByQuestId(int questId) {
		for (ChallengeTaskTemplate ct : tasksById.values()) {
			for (ChallengeQuestTemplate cq : ct.getQuests()) {
				if (cq.getId() == questId) {
					return cq;
				}
			}
		}
		return null;
	}

	public int size() {
		return this.tasksById.size();
	}
}
