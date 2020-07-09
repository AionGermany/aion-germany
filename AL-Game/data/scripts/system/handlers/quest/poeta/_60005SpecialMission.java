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
package quest.poeta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator by Mariella
 * @rework FrozenKiller
 */
public class _60005SpecialMission extends QuestHandler {

	private final static int questId = 60005;
	private final static int[] mobs = { 651893, 651894 };

	public _60005SpecialMission() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203067).addOnTalkEvent(questId); // Kalio
		qe.registerQuestNpc(203065).addOnTalkEvent(questId); // Melampus
		qe.registerQuestNpc(820001).addOnTalkEvent(questId); // Acting Royer
		qe.registerQuestNpc(730007).addOnTalkEvent(questId); // Forest Protector Noah

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 60000, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203067: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SETPRO1: {
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 203065: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1352);
						}
						case SETPRO2: {
							changeQuestStep(env, 1, 2, false);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 820001: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2034);
						}
						case SET_SUCCEED: {
							changeQuestStep(env, 3, 4, true);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				default:
					break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730007) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 2, Count: 3, Mobs : 651893, 651894

			if (var == 2 && var1 < 2) {
			   return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
			} else {
				qs.setQuestVarById(1, 0);;
				changeQuestStep(env, 2, 3, false);
			}
		}
		return false;
	}
}
