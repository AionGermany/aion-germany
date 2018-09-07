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
package com.aionemu.gameserver.model.challenge;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.templates.challenge.ChallengeQuestTemplate;
import com.aionemu.gameserver.model.templates.challenge.ChallengeTaskTemplate;

/**
 * @author ViAl
 */
public class ChallengeTask {

	private final int taskId;
	private final int ownerId;
	private Map<Integer, ChallengeQuest> quests;
	private Timestamp completeTime;
	private ChallengeTaskTemplate template;

	/**
	 * Used for loading tasks from DAO.
	 *
	 * @param header
	 * @param quests
	 * @param completeTime
	 */
	public ChallengeTask(int taskId, int ownerId, Map<Integer, ChallengeQuest> quests, Timestamp completeTime) {
		this.taskId = taskId;
		this.ownerId = ownerId;
		this.quests = quests;
		this.completeTime = completeTime;
		this.template = DataManager.CHALLENGE_DATA.getTaskByTaskId(taskId);
	}

	/**
	 * Used for creating new tasks in runtime.
	 *
	 * @param ownerId
	 * @param template
	 */
	public ChallengeTask(int ownerId, ChallengeTaskTemplate template) {
		this.taskId = template.getId();
		this.ownerId = ownerId;
		Map<Integer, ChallengeQuest> quests = new HashMap<Integer, ChallengeQuest>();
		for (ChallengeQuestTemplate qt : template.getQuests()) {
			ChallengeQuest quest = new ChallengeQuest(qt, 0);
			quest.setPersistentState(PersistentState.NEW);
			quests.put(qt.getId(), quest);
		}
		this.quests = quests;
		this.completeTime = new Timestamp(1000);
		this.template = template;
	}

	public int getTaskId() {
		return this.taskId;
	}

	public int getOwnerId() {
		return this.ownerId;
	}

	public int getQuestsCount() {
		return quests.size();
	}

	public Map<Integer, ChallengeQuest> getQuests() {
		return quests;
	}

	public ChallengeQuest getQuest(int questId) {
		return quests.get(questId);
	}

	public Timestamp getCompleteTime() {
		return completeTime;
	}

	public synchronized void updateCompleteTime() {
		completeTime.setTime(System.currentTimeMillis());
	}

	public ChallengeTaskTemplate getTemplate() {
		return this.template;
	}

	public boolean isCompleted() {
		boolean isCompleted = true;
		for (ChallengeQuest quest : quests.values()) {
			if (quest.getCompleteCount() < quest.getMaxRepeats()) {
				isCompleted = false;
				break;
			}
		}
		return isCompleted;
	}
}
