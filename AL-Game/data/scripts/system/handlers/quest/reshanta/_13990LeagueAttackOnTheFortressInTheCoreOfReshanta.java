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
package quest.reshanta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator by Mariella
 */
public class _13990LeagueAttackOnTheFortressInTheCoreOfReshanta extends QuestHandler {

	private final static int questId = 13990;
	private final static int[] mobs = { 884946, 884947, 884948, 884949, 884950, 884951, 884952, 884953, 884954, 884955, 884956, 884957, 884958, 884959, 884960, 884941, 884942, 884943, 884944, 884945, 884992, 884993, 884994, 884995, 884996, 884997, 884998, 884999, 885000, 885001, 884987, 884988, 884989, 884990, 884991, 884936, 884937, 884938, 884939, 884940, 884982, 884983, 884984, 884985, 884986 };

	public _13990LeagueAttackOnTheFortressInTheCoreOfReshanta() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(835722).addOnQuestStart(questId); // Dayan
		qe.registerQuestNpc(835722).addOnTalkEvent(questId); // Dayan

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1000, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE ) {
	  		if (targetId == 835722) {
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
				case 835722: {
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
			if (targetId == 835722) {
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

			// (0) Step: 1, Count: 10, Mobs : 884946, 884947, 884948, 884949, 884950, 884951, 884952, 884953, 884954, 884955, 884956, 884957, 884958, 884959, 884960, 884941, 884942, 884943, 884944, 884945, 884992, 884993, 884994, 884995, 884996, 884997, 884998, 884999, 885000, 885001, 884987, 884988, 884989, 884990, 884991
			// (1) Step: 1, Count: 3, Mobs : 884936, 884937, 884938, 884939, 884940, 884982, 884983, 884984, 884985, 884986

			switch (var) {
				case 1: {
				// ???
				}
				case 1: {
				// ???
				}
				default:
					break;
			}
			return false;
		}
		return false;
	}
	*/
}
