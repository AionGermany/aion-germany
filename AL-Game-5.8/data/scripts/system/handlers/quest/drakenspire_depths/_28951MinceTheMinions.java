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

package quest.drakenspire_depths;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _28951MinceTheMinions extends QuestHandler {

	private final static int questId = 28951;

	private final static int[] minion = { 236106, 236109, 236113, 236116, 236119, 236120, 236124, 236126, 236128, 236129, 236131, 236132, 236137, 236141, 236142, 236154, 236155, 236156, 236157, 236159, 236162, 236163, 236165, 236166, 236167, 236168, 236170, 236172, 236174, 236175, 236177, 236186, 236185, 236187, 236192, 236194, 236199, 236201, 236204, 236205, 236206, 236216, 236217, 236218, 236219, 236220 };

	public _28951MinceTheMinions() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(209743).addOnQuestStart(questId);
		qe.registerQuestNpc(209743).addOnTalkEvent(questId);
		qe.registerQuestNpc(804711).addOnTalkEvent(questId);
		for (int mob : minion) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 209743) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 804738) {
				if (qs.getQuestVarById(0) == 25) {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_QUEST_REWARD: {
							changeQuestStep(env, 25, 26, true);
							return sendQuestEndDialog(env);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804738) {
				if (env.getDialogId() == 1352) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		int var = qs.getQuestVarById(1);

		if (qs.getQuestVarById(1) < 25) {
			return defaultOnKillEvent(env, minion, var, var + 1);
		}
		else if (qs.getQuestVarById(1) >= 25) {
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}
