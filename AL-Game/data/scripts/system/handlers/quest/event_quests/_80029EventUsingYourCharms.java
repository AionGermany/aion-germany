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

/**
 * @author Rolandas
 */
public class _80029EventUsingYourCharms extends QuestHandler {

	private final static int questId = 80029;

	public _80029EventUsingYourCharms() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799766).addOnQuestStart(questId);
		qe.registerQuestNpc(799766).addOnTalkEvent(questId);
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
			if (targetId == 799766) {
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
					return sendQuestRewardDialog(env, 799766, 5);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		return sendQuestRewardDialog(env, 799766, 0);
	}
}
