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
package quest.sanctum;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 */
public class _1908UlaguruSpeaks extends QuestHandler {

	private final static int questId = 1908;

	public _1908UlaguruSpeaks() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203864).addOnQuestStart(questId);
		qe.registerQuestNpc(203864).addOnTalkEvent(questId);
		qe.registerQuestNpc(204120).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203864) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203890) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 0) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == DialogAction.SETPRO1) {
					return defaultCloseDialog(env, 0, 1);
				}
			}
			else if (targetId == 203864) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 1) {
						return sendQuestDialog(env, 2375);
					}
				}
				else if (dialog == DialogAction.SETPRO21) {
					qs.setQuestVar(21);
					return sendQuestDialog(env, 2376);
				}
				else if (dialog == DialogAction.SETPRO22) {
					qs.setQuestVar(22);
					return sendQuestDialog(env, 2461);
				}
				else if (dialog == DialogAction.SETPRO23) {
					qs.setQuestVar(23);
					return sendQuestDialog(env, 2546);
				}
				else if (dialog == DialogAction.SELECT_QUEST_REWARD) {
					return defaultCloseDialog(env, qs.getQuestVarById(0), qs.getQuestVarById(0), true, true);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203864) {
				if (qs.getQuestVarById(0) == 21) {
					return sendQuestEndDialog(env);
				}
				else if (qs.getQuestVarById(0) == 22) {
					return sendQuestEndDialog(env, 1);
				}
				else if (qs.getQuestVarById(0) == 23) {
					return sendQuestEndDialog(env, 2);
				}
			}
		}
		return false;
	}
}
