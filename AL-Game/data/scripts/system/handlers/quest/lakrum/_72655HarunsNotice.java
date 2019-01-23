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
package quest.lakrum;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator by Mariella
 */
public class _72655HarunsNotice extends QuestHandler {

	private final static int questId = 72655;
	private final static int[] mobs = { 886123, 886124, 886125, 886126, 886127, 886128, 886129, 886130, 886131, 886132, 886387, 886388, 886389, 886390, 886391, 886133, 886134, 886135, 886136, 886137, 886138, 886139, 886140, 886141, 886142, 886392, 886393, 886394, 886395, 886396, 886143, 886144, 886145, 886146, 886147, 886148, 886149, 886150, 886151, 886152, 886397, 886398, 886399, 886400, 886401, 886153, 886154, 886155, 886156, 886157, 886158, 886159, 886160, 886161, 886162, 886402, 886403, 886404, 886405, 886406 };

	public _72655HarunsNotice() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(836634).addOnQuestStart(questId); // Elgard
		qe.registerQuestNpc(836634).addOnTalkEvent(questId); // Elgard

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 70506, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE ) {
	  		if (targetId == 836634) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 4762);
					}
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE: {
						return sendQuestStartDialog(env);
					}
					case QUEST_REFUSE_SIMPLE: {
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 836634: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SETPRO1: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
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
			if (targetId == 836634) {
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
	/*
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 1, Count: 10, Mobs : 886123, 886124, 886125, 886126, 886127, 886128, 886129, 886130, 886131, 886132, 886387, 886388, 886389, 886390, 886391, 886133, 886134, 886135, 886136, 886137, 886138, 886139, 886140, 886141, 886142, 886392, 886393, 886394, 886395, 886396, 886143, 886144, 886145, 886146, 886147, 886148, 886149, 886150, 886151, 886152, 886397, 886398, 886399, 886400, 886401, 886153, 886154, 886155, 886156, 886157, 886158, 886159, 886160, 886161, 886162, 886402, 886403, 886404, 886405, 886406

			if (var == 1 && var1 < 9) {
			   return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
			} else {
				qs.setQuestVar(2);
				updateQuestStatus(env);
			}
		}
		return false;
	}
	*/
}
