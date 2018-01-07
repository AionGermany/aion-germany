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
package quest.pandaemonium;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 */
public class _2919BookOfOblivion extends QuestHandler {

	private final static int questId = 2919;

	public _2919BookOfOblivion() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204206).addOnQuestStart(questId);
		qe.registerQuestNpc(204206).addOnTalkEvent(questId);
		qe.registerQuestNpc(204215).addOnTalkEvent(questId);
		qe.registerQuestNpc(204192).addOnTalkEvent(questId);
		qe.registerQuestNpc(700212).addOnTalkEvent(questId);
		qe.registerQuestNpc(204224).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204206) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204215) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 0) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == DialogAction.SETPRO2) {
					return defaultCloseDialog(env, 0, 1);
				}
			}
			else if (targetId == 204192) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 1) {
						return sendQuestDialog(env, 1693);
					}
				}
				else if (dialog == DialogAction.SETPRO3) {
					return defaultCloseDialog(env, 1, 2);
				}
			}
			else if (targetId == 700212) {
				if (dialog == DialogAction.USE_OBJECT) {
					if (qs.getQuestVarById(0) == 2) {
						return sendQuestDialog(env, 2034);
					}
					else if (qs.getQuestVarById(0) == 6) {
						return sendQuestDialog(env, 3057);
					}
				}
				else if (dialog == DialogAction.SETPRO4) {
					return defaultCloseDialog(env, 2, 3);
				}
				else if (dialog == DialogAction.SETPRO7) {
					giveQuestItem(env, 182207013, 1);
					return defaultCloseDialog(env, 6, 7);
				}
			}
			else if (targetId == 204206) {
				if (qs.getQuestVarById(0) == 7) {
					if (dialog == DialogAction.USE_OBJECT) {
						return sendQuestDialog(env, 3398);
					}
				}
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 3) {
						return sendQuestDialog(env, 2375);
					}
				}
				else if (dialog == DialogAction.SETPRO5) {
					return defaultCloseDialog(env, 3, 4);
				}
				else if (dialog == DialogAction.SELECT_QUEST_REWARD) {
					removeQuestItem(env, 182207013, 1);
					return defaultCloseDialog(env, 7, 7, true, true);
				}
			}
			else if (targetId == 204224) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 4) {
						return sendQuestDialog(env, 2716);
					}
				}
				else if (dialog == DialogAction.CHECK_USER_HAS_QUEST_ITEM) {
					return checkQuestItems(env, 4, 6, false, 2802, 2717);
				}
				else if (dialog == DialogAction.SETPRO6) {
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204206) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 5);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
