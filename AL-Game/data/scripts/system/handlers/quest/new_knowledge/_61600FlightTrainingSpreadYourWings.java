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
package quest.new_knowledge;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author QuestGenerator by Mariella
 * @rework FrozenKiller
 */
public class _61600FlightTrainingSpreadYourWings extends QuestHandler {

	private final static int questId = 61600;
	private String[] rings = { "HEIRON_FORTRESS_210040000_1", "HEIRON_FORTRESS_210040000_2", "HEIRON_FORTRESS_210040000_3", "HEIRON_FORTRESS_210040000_4", "HEIRON_FORTRESS_210040000_5", "HEIRON_FORTRESS_210040000_6" };

	public _61600FlightTrainingSpreadYourWings() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204505).addOnQuestStart(questId); // Sulates
		qe.registerQuestNpc(204505).addOnTalkEvent(questId); // Sulates
		qe.registerQuestNpc(203930).addOnTalkEvent(questId); // Daedalus		
		qe.registerQuestNpc(204513).addOnTalkEvent(questId); // Amene
		qe.registerOnQuestTimerEnd(questId);
		for (String ring : rings) {
			qe.registerOnPassFlyingRings(ring, questId);
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
	  		if (targetId == 204505) {
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
				case 203930: {
					switch (dialog) {
						case QUEST_SELECT: {
							switch (var) {
								case 0: {
									return sendQuestDialog(env, 1011);		
								}
								case 7: {
									return sendQuestDialog(env, 3398);		
								}
								default:
									break;
							}
						}
						case SETPRO1: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							QuestService.questTimerStart(env, 120);
							return closeDialogWindow(env);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						case SET_SUCCEED: {
							qs.setQuestVar(8);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							QuestService.questTimerEnd(env);
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
			if (dialog == DialogAction.USE_OBJECT) {
				return sendQuestDialog(env, 10002);
			}
			return sendQuestEndDialog(env);
		}

		return false;
	}
	
	@Override
	public boolean onPassFlyingRingEvent(QuestEnv env, String flyingRing) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (rings[0].equals(flyingRing)) {
				changeQuestStep(env, 1, 2, false);
				return true;
			}
			else if (rings[1].equals(flyingRing)) {
				changeQuestStep(env, 2, 3, false);
				return true;
			}
			else if (rings[2].equals(flyingRing)) {
				changeQuestStep(env, 3, 4, false);
				return true;
			}
			else if (rings[3].equals(flyingRing)) {
				changeQuestStep(env, 4, 5, false);
				return true;
			}
			else if (rings[4].equals(flyingRing)) {
				changeQuestStep(env, 5, 6, false);
				return true;
			}
			else if (rings[5].equals(flyingRing)) {
				changeQuestStep(env, 6, 7, false);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onQuestTimerEndEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			qs.setQuestVar(0);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}