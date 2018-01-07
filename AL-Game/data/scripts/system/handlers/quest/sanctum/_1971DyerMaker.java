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
 * @author KennyLucia
 * @rework FrozenKiller
 */
public class _1971DyerMaker extends QuestHandler {

	private final static int questId = 1971;

	public _1971DyerMaker() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203812).addOnQuestStart(questId);
		qe.registerQuestNpc(203812).addOnTalkEvent(questId);
		qe.registerQuestNpc(203820).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203812) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203820: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1352);
						}
						case SETPRO1: {
							if (player.getInventory().getItemCountByItemId(182215758) != 0) {
								qs.setQuestVarById(0, 1);
								updateQuestStatus(env);
								return closeDialogWindow(env);
							}
							else {
								giveQuestItem(env, 182215758, 1);
								qs.setQuestVarById(0, 1);
								updateQuestStatus(env);
								return closeDialogWindow(env);
							}
						}
						default:
							break;
					}
				}
				case 203812: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_QUEST_REWARD: {
							removeQuestItem(env, 182215758, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 5);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203812) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
