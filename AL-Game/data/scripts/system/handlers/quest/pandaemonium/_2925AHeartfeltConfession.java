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
public class _2925AHeartfeltConfession extends QuestHandler {

	private final static int questId = 2925;

	public _2925AHeartfeltConfession() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204261).addOnQuestStart(questId);
		qe.registerQuestNpc(204261).addOnTalkEvent(questId);
		qe.registerQuestNpc(204235).addOnTalkEvent(questId);
		qe.registerQuestNpc(204127).addOnTalkEvent(questId);
		qe.registerQuestNpc(204193).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204261) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204235) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 0) {
						if (!player.getEquipment().getEquippedItemsByItemId(110100288).isEmpty()) {
							return sendQuestDialog(env, 1011);
						}
						else {
							return sendQuestDialog(env, 1097);
						}
					}
					else if (qs.getQuestVarById(0) == 4) {
						return sendQuestDialog(env, 2375);
					}
				}
				else if (dialog == DialogAction.SETPRO1) {
					return defaultCloseDialog(env, 0, 1);
				}
				else if (dialog == DialogAction.SETPRO5) {
					return defaultCloseDialog(env, 4, 5);
				}
			}
			else if (targetId == 204261) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 1) {
						return sendQuestDialog(env, 1352);
					}
					else if (qs.getQuestVarById(0) == 5) {
						return sendQuestDialog(env, 2716);
					}
				}
				else if (dialog == DialogAction.SETPRO2) {
					removeQuestItem(env, 110100288, 1);
					return defaultCloseDialog(env, 1, 2);
				}
				else if (dialog == DialogAction.SELECT_ACTION_2717) {
					changeQuestStep(env, 5, 5, true);
					return sendQuestDialog(env, 10002);
				}
			}
			else if (targetId == 204127) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 2) {
						return sendQuestDialog(env, 1693);
					}
				}
				else if (dialog == DialogAction.SETPRO3) {
					return defaultCloseDialog(env, 2, 3);
				}
			}
			else if (targetId == 204193) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 3) {
						return sendQuestDialog(env, 2034);
					}
				}
				else if (dialog == DialogAction.SETPRO4) {
					return defaultCloseDialog(env, 3, 4);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204261) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
