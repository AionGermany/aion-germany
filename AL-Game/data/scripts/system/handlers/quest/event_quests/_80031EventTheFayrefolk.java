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
package quest.event_quests;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.events.EventService;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Rolandas
 */
public class _80031EventTheFayrefolk extends QuestHandler {

	private final static int questId = 80031;

	public _80031EventTheFayrefolk() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799781).addOnQuestStart(questId);
		qe.registerQuestNpc(799781).addOnTalkEvent(questId);
		qe.registerOnLevelUp(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 799781) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else if (env.getDialog() == DialogAction.QUEST_ACCEPT_1) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					defaultCloseDialog(env, 0, 0, true, true);
					return sendQuestDialog(env, 5);
				}
				else if (env.getDialog() == DialogAction.SELECTED_QUEST_NOREWARD) {
					return sendQuestRewardDialog(env, 799781, 5);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		return sendQuestRewardDialog(env, 799781, 0);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (!EventService.getInstance().checkQuestIsActive(questId) && qs != null) {
			QuestService.abandonQuest(player, questId);
		}
		return true;
	}
}
