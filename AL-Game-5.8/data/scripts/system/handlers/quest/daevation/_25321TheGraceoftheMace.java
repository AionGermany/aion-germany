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
 * @author Phantom_KNA
 */
public class _25321TheGraceoftheMace extends QuestHandler {

	public static final int questId = 25321;

	private final static int[] mobs = { 219693, 219694, 219695, 219696, 219697, 219698, 219699 };
	private final static int[] mobs2 = { 219778, 219779, 219780 };
	private final static int[] mobs3 = { 236363, 236364, 236364, 236365, 236366, 236367, 236368, 236369, 236370, 236371, 236372, 236373, 236374, 236375, 236376, 236377, 236378, 236379, 236380, 236381, 236382, 236383, 236384, 236385, 236386, 236387, 236388, 236389, 236390, 236586, 236587, 236588, 236589, 236590, 236591, 236592, 236593, 236594, 236595, 236596, 236597, 236598, 236599, 236600, 236601, 236602, 236603, 236604, 236605, 236606, 236607, 236608, 236609, 236610, 236611, 236612, 236613 };
	private final static int[] mobs4 = { 234698, 234699, 234700, 234701, 234702, 234703 };
	private final static int[] mobs5 = { 234244, 234246, 234247 };
	private final static int[] mobs6 = { 883301, 883302, 883303, 883304, 883305, 883306, 883307, 883308 };

	public _25321TheGraceoftheMace() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(805342).addOnQuestStart(questId);
		qe.registerQuestNpc(805342).addOnTalkEvent(questId); // Skuldun
		qe.registerQuestNpc(805344).addOnTalkEvent(questId); // Nuiage
		qe.registerQuestNpc(805345).addOnTalkEvent(questId); // Sturunwind
		qe.registerQuestNpc(805346).addOnTalkEvent(questId); // Korperchen
		qe.registerQuestNpc(805347).addOnTalkEvent(questId); // Fenke
		qe.registerQuestNpc(805348).addOnTalkEvent(questId); // Sach
		qe.registerQuestNpc(805349).addOnTalkEvent(questId); // Jelewoe
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
			if (targetId == 805342) { // Skuldun
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
			if (targetId == 805344) { // Nuiage
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
			}
			else if (targetId == 805345) { // Sturunwind
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1693);
					}
					case SETPRO3: {
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805346) { // Korperchen
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 2375);
					}
					case SETPRO5: {
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805347) { // Fenke
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 3057);
					}
					case SETPRO7: {
						changeQuestStep(env, 6, 7, false);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805348) { // Sach
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 3739);
					}
					case SETPRO9: {
						changeQuestStep(env, 8, 9, false);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 805349) { // Jelewoe
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 6500);
					}
					case SETPRO11: {
						changeQuestStep(env, 10, 11, false);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805342) {
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
			changeQuestStep(env, 1, 2, false);
			updateQuestStatus(env);
			return true;
		}
		if (var == 3 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, mobs2, var1, var1 + 1, 1);
		}
		else if (var == 3 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 3, 4, false);
			updateQuestStatus(env);
			return true;
		}
		if (var == 5 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, mobs3, var1, var1 + 1, 1);
		}
		else if (var == 5 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 5, 6, false);
			updateQuestStatus(env);
			return true;
		}
		if (var == 7 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, mobs4, var1, var1 + 1, 1);
		}
		else if (var == 7 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 7, 8, false);
			updateQuestStatus(env);
			return true;
		}
		if (var == 9 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, mobs5, var1, var1 + 1, 1);
		}
		else if (var == 9 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 9, 10, false);
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
