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
package quest.daevation;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author FrozenKiller
 */
public class _15321SupportingKaisinelsWings extends QuestHandler {

	public static final int questId = 15321;
	private final static int[] mobs = { 235829, 235851 };
	private final static int[] mobs2 = { 235915, 235917, 235920 };
	private final static int[] mobs3 = { 215502, 215503 };
	private final static int[] mobs4 = { 233910, 233911, 233912, 233915, 233916, 233955, 234159 };
	private final static int[] mobs5 = { 234518, 234248, 234250, 234251 };
	private final static int[] mobs6 = { 883276, 883277, 883278, 883279, 883280, 883281, 883282, 883283 }; // Missing Npc's in Upper Reshanta (NPC's are from Quest_Data)

	public _15321SupportingKaisinelsWings() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(805330).addOnQuestStart(questId);
		qe.registerQuestNpc(805330).addOnTalkEvent(questId); // Potencia
		qe.registerQuestNpc(805332).addOnTalkEvent(questId); // Nephoria
		qe.registerQuestNpc(805333).addOnTalkEvent(questId); // Prestelle
		qe.registerQuestNpc(805334).addOnTalkEvent(questId); // Somation
		qe.registerQuestNpc(805335).addOnTalkEvent(questId); // Spintel
		qe.registerQuestNpc(805336).addOnTalkEvent(questId); // Asteness
		qe.registerQuestNpc(805337).addOnTalkEvent(questId); // Tallesleon
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob2 : mobs2) {
			qe.registerQuestNpc(mob2).addOnKillEvent(questId);
		}
		for (int mob3 : mobs3) {
			qe.registerQuestNpc(mob3).addOnKillEvent(questId);
		}
		for (int mob4 : mobs4) {
			qe.registerQuestNpc(mob4).addOnKillEvent(questId);
		}
		for (int mob5 : mobs5) {
			qe.registerQuestNpc(mob5).addOnKillEvent(questId);
		}
		for (int mob6 : mobs6) {
			qe.registerQuestNpc(mob6).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 805330) { // Potencia
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 4762);
					}
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE:
						QuestService.startQuest(env);
						return closeDialogWindow(env);
					case QUEST_REFUSE_1:
					case QUEST_REFUSE_SIMPLE:
						return closeDialogWindow(env);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 805332) { // Nephoria
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					case SETPRO1: {
						changeQuestStep(env, 0, 1, false); // 1
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805333) { // Prestelle
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1693);
					}
					case SETPRO3: {
						changeQuestStep(env, 2, 3, false); // 3
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805334) { // Somation
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 2375);
					}
					case SETPRO5: {
						changeQuestStep(env, 4, 5, false); // 5
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805335) { // Spintel
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 3057);
					}
					case SETPRO7: {
						changeQuestStep(env, 6, 7, false); // 7
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805336) { // Asteness
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 3739);
					}
					case SETPRO9: {
						changeQuestStep(env, 8, 9, false); // 9
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805337) { // Tallesleon
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 6500);
					}
					case SETPRO11: {
						changeQuestStep(env, 10, 11, false); // 11
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805330) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					case SELECTED_QUEST_NOREWARD: {
						return sendQuestEndDialog(env);
					}
					default:
						break;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
		if (var == 1 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
		}
		else if (var == 1 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 1, 2, false); // 2
			updateQuestStatus(env);
			return true;
		}
		if (var == 3 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, mobs2, var1, var1 + 1, 1);
		}
		else if (var == 3 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 3, 4, false); // 4
			updateQuestStatus(env);
			return true;
		}
		if (var == 5 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, mobs3, var1, var1 + 1, 1);
		}
		else if (var == 5 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 5, 6, false); // 6
			updateQuestStatus(env);
			return true;
		}
		if (var == 7 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, mobs4, var1, var1 + 1, 1);
		}
		else if (var == 7 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 7, 8, false); // 8
			updateQuestStatus(env);
			return true;
		}
		if (var == 9 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, mobs5, var1, var1 + 1, 1);
		}
		else if (var == 9 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 9, 10, false); // 10
			updateQuestStatus(env);
			return true;
		}
		if (var == 11 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, mobs6, var1, var1 + 1, 1);
		}
		else if (var == 11 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			qs.setQuestVar(12);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}
