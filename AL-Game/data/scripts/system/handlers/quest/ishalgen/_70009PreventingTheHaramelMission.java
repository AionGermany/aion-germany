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
package quest.ishalgen;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author QuestGenerator by Mariella
 */
public class _70009PreventingTheHaramelMission extends QuestHandler {

	private final static int questId = 70009;
	private final static int[] mobs  = { 653196, 653205, 653218 };

	public _70009PreventingTheHaramelMission() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(703488).addOnTalkEvent(questId);  // Third Odella Transport Track
		qe.registerQuestNpc(730319).addOnTalkEvent(questId);  // Haramel Secret Passage
		qe.registerQuestNpc(806883).addOnTalkEvent(questId);  // Cheska
		qe.registerQuestNpc(799524).addOnTalkEvent(questId);  // Gestanerk
		qe.registerQuestNpc(806814).addOnTalkEvent(questId);  // Marko

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 70000, false);
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
			int var = qs.getQuestVarById(0);

			switch (targetId) {
				case 703488: {
					switch (dialog) {
						// ToDo: check correct action for this npc
						case USE_OBJECT: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return false;
						}
						default: 
							break;
					}
					break;
				}
				case 730319: {
					switch (dialog) {
						// ToDo: check correct action for this npc
						case USE_OBJECT: {
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return false;
						}
						default: 
							break;
					}
					break;
				}
				case 806883: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1693);
						}
						case SETPRO3: {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 799524: {
					switch (dialog) {
						case CHECK_USER_HAS_QUEST_ITEM: {
							if (QuestService.collectItemCheck(env,true)) {
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return sendQuestDialog(env, 10000);
							} else {
								return sendQuestDialog(env, 10001);
							}
						}
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 3398);
							} else {
								return sendQuestDialog(env, 3739);
							}
						}
						case SETPRO8: {
							qs.setQuestVar(8);
							updateQuestStatus(env);
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
			if (targetId == 806814) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
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
			int var  = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 3, Count: 1, Mobs : 653196
			// (1) Step: 5, Count: 1, Mobs : 653205
			// (2) Step: 8, Count: 1, Mobs : 653218

			switch (var) {
				case 3: {
					return defaultOnKillEvent(env, 653196, 3, 4, 0);
				}
				case 5: {
					return defaultOnKillEvent(env, 653205, 5, 6, 0);
				}
				case 8: {
					qs.setQuestVar(9);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
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
