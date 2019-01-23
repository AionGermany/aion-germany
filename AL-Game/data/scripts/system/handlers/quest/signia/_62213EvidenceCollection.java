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
package quest.signia;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator by Mariella
 */
public class _62213EvidenceCollection extends QuestHandler {

	private final static int questId = 62213;

	public _62213EvidenceCollection() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(804702).addOnQuestStart(questId); // Hidden Kaion
		qe.registerQuestNpc(804702).addOnTalkEvent(questId); // Hidden Kaion
		qe.registerQuestNpc(731537).addOnQuestStart(questId); // Ancient Fossil
		qe.registerQuestNpc(731537).addOnTalkEvent(questId); // Ancient Fossil
		qe.registerQuestNpc(804701).addOnQuestStart(questId); // Kaion
		qe.registerQuestNpc(804701).addOnTalkEvent(questId); // Kaion
		qe.registerQuestItem(182216330, questId);			 // Rubbing Utensils
		qe.registerQuestItem(182216331, questId);			 // Wall Relief Rubbing
		qe.registerQuestItem(182216332, questId);			 // Ancient Fossil
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 60200, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE ) {
	  		if (targetId == 804702) {
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
			int var = qs.getQuestVarById(0);

			switch (targetId) {
				case 804702: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							} else {
								return sendQuestDialog(env, 1352);
							}
						}
						case SETPRO1: {
						//  giveQuestItem(env, 182216330, 1);
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
//						case QUEST_SELECT: {
//							if var==0
//								return sendQuestDialog(env, 1693);
//							else
//								return sendQuestDialog(env, 2034);
//						}
						case SETPRO3: {
						//  giveQuestItem(env, 182216330, 1);
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
//						case QUEST_SELECT: {
//							if var==0
//								return sendQuestDialog(env, 2375);
//							else
//								return sendQuestDialog(env, 2716);
//						}
						case SETPRO5: {
						//  giveQuestItem(env, 182216330, 1);
							qs.setQuestVar(5);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 731537: {
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
			if (targetId == 804701) {
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
}
