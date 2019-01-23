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
package quest.vengar;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator by Mariella
 */
public class _72213VengarsSecrets extends QuestHandler {

	private final static int questId = 72213;

	public _72213VengarsSecrets() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(804724).addOnQuestStart(questId); // Kronac
		qe.registerQuestNpc(804724).addOnTalkEvent(questId); // Kronac
		qe.registerQuestNpc(804725).addOnQuestStart(questId); // Canob
		qe.registerQuestNpc(804725).addOnTalkEvent(questId); // Canob
		qe.registerQuestNpc(804726).addOnQuestStart(questId); // Muratu
		qe.registerQuestNpc(804726).addOnTalkEvent(questId); // Muratu
		qe.registerQuestNpc(804727).addOnQuestStart(questId); // Batuga
		qe.registerQuestNpc(804727).addOnTalkEvent(questId); // Batuga
		qe.registerQuestNpc(804914).addOnQuestStart(questId); // Grangund
		qe.registerQuestNpc(804914).addOnTalkEvent(questId); // Grangund
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 70200, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE ) {
	  		if (targetId == 804724) {
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
				case 804724: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SETPRO1: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 0, 0, true, 5, 2716);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default: 
							break;
					}
					break;
				}
				case 804725: {
					switch (dialog) {
//						case QUEST_SELECT: {
//							if var==0
//								return sendQuestDialog(env, 1352);
//							else
//								return sendQuestDialog(env, 1693);
//						}
						case SETPRO2: {
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
//						case QUEST_SELECT: {
//							if var==0
//								return sendQuestDialog(env, 2034);
//							else
//								return sendQuestDialog(env, 2375);
//						}
						case SETPRO4: {
							qs.setQuestVar(4);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 804726: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SETPRO5: {
							qs.setQuestVar(5);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 804727: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2716);
						}
						case SET_SUCCEED: {
							qs.setQuestVar(6);
							qs.setStatus(QuestStatus.REWARD);
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
			if (targetId == 804914) {
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
}
