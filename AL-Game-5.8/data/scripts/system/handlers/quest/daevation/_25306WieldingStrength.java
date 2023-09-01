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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Phantom_KNA
 */
public class _25306WieldingStrength extends QuestHandler {

	private final static int questId = 25306;
	int[] mobs = { 233941, 233942, 234692, 234313, 234312, 233900, 233944, 234704 };
	int[] mobs2 = { 234292, 234529, 234296, 234298, 234294 };
	int[] mobs3 = { 883276, 883278, 883277, 883279, 883280, 883282, 883283, 883281 };
	int[] boss = { 236277, 231073, 231130 };

	public _25306WieldingStrength() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnMovieEndQuest(865, questId);
		qe.registerQuestNpc(805339).addOnQuestStart(questId); // Skuldun
		qe.registerQuestNpc(805339).addOnTalkEvent(questId); // Skuldun
		qe.registerQuestNpc(805339).addOnTalkEvent(questId); // Skuldun
		qe.registerQuestNpc(805340).addOnTalkEvent(questId); // Ashman
		qe.registerQuestNpc(232853).addOnKillEvent(questId); // Governor Varga of the Occupation Squad
		qe.registerQuestNpc(233491).addOnKillEvent(questId); // Captain Abrahn
		qe.registerQuestNpc(234190).addOnKillEvent(questId); // Kunax the Slayer
		qe.registerQuestNpc(233544).addOnKillEvent(questId); // Supreme Commander Pashid
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob : mobs2) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob : mobs3) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob : boss) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		int targetId = 0;

		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 805339) { // Skuldun
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null) {
			return false;
		}

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 805340: // Ashman
					switch (dialog) {
						case QUEST_SELECT: {
							switch (var) {
								case 0:
									return sendQuestDialog(env, 1011);
								case 1:
									return sendQuestDialog(env, 1352);
								case 2:
									return sendQuestDialog(env, 1693);
								case 8:
									return sendQuestDialog(env, 3739);
							}
						}
						case SETPRO1: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 1, 2, false, 10000, 10001);
						}
						case SETPRO3: {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case SETPRO9: {
							qs.setQuestVar(9);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				case 805339: // Skuldun
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							if (var == 9) {
								return sendQuestDialog(env, 4080);
							}
						}
						case SELECT_QUEST_REWARD: {
							changeQuestStep(env, 9, 10, true); // reward
							return sendQuestDialog(env, 5);
						}
						default:
							break;
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805339) { // Skuldun
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (movieId == 865) {
			qs.setQuestVar(10);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 59) {
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 59) {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 4) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 59) {
					return defaultOnKillEvent(env, mobs2, var1, var1 + 1, 1);
				}
				else if (var1 == 59) {
					qs.setQuestVar(5);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 5) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 29) {
					return defaultOnKillEvent(env, mobs3, var1, var1 + 1, 1);
				}
				else if (var1 == 29) {
					qs.setQuestVar(6);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 7) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 4) {
					return defaultOnKillEvent(env, boss, var1, var1 + 1, 1);
				}
				else if (var1 == 4) {
					qs.setQuestVar(8);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 6) {
				int targetId = env.getTargetId();
				switch (targetId) {
					case 232853:
					case 233491:
					case 234190:
					case 233544:
						if (qs.getQuestVarById(1) != 0) {
							qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
							updateQuestStatus(env);
						}
						else {
							qs.setQuestVarById(0, 7);
							updateQuestStatus(env);
						}
				}
			}
		}
		return false;
	}
}
