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
package quest.enshar;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author pralinka
 */
public class _25000GettingToKnowYou extends QuestHandler {

	private final static int questId = 25000;

	public _25000GettingToKnowYou() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(804718).addOnQuestStart(questId);
		qe.registerQuestNpc(804753).addOnTalkEvent(questId);
		qe.registerQuestNpc(804990).addOnTalkEvent(questId);
		qe.registerQuestNpc(805000).addOnTalkEvent(questId);
		qe.registerQuestNpc(804718).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804718) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 804753) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
				}
				else if (dialog == DialogAction.SETPRO1) {
					changeQuestStep(env, 0, 1, false);
					return closeDialogWindow(env);
				}
			}
			if (targetId == 804990) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (var == 1) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == DialogAction.SETPRO2) {
					changeQuestStep(env, 1, 2, false);
					return closeDialogWindow(env);
				}
			}
			if (targetId == 805000) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (var == 2) {
						return sendQuestDialog(env, 1693);
					}
				}
				else if (dialog == DialogAction.SET_SUCCEED) {
					changeQuestStep(env, 2, 3, true);
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804718) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
