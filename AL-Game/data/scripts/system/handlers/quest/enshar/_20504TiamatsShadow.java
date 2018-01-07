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
package quest.enshar;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author pralinka
 */
public class _20504TiamatsShadow extends QuestHandler {

	public static final int questId = 20504;
	int[] mobs = { 219943, 219944, 219945, 219946, 219947, 219948 };

	public _20504TiamatsShadow() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 804730, 804742, 804731 };
		qe.registerQuestNpc(219949).addOnKillEvent(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 20503, true);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			int targetId = env.getTargetId();
			if (var == 1) {
				int[] mobs1 = { 219943, 219944, 219945 };

				switch (targetId) {
					case 219943:
					case 219944:
					case 219945: {
						if (var1 >= 0 && var1 < 4) {
							return defaultOnKillEvent(env, mobs1, var1, var1 + 1, 1);
						}
						else if (var1 == 4) {
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return true;
						}
						break;
					}
				}
			}
			else if (var == 3) {
				int[] mobs2 = { 219946, 219947, 219948 };

				switch (targetId) {
					case 219946:
					case 219947:
					case 219948: {
						if (var1 >= 0 && var1 < 4) {
							return defaultOnKillEvent(env, mobs2, var1, var1 + 1, 1);
						}
						else if (var1 == 4) {
							qs.setQuestVar(5);
							updateQuestStatus(env);
							QuestService.addNewSpawn(220080000, 1, 804742, player.getX() + 1, player.getY() + 1, player.getZ(), (byte) 0);
							return true;
						}
						break;
					}
				}
			}
			// else if (var == 5) {
			// switch (targetId) {
			// case 219949: {
			// QuestService.addNewSpawn(220080000, 1, 804742, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
			// // qs.setQuestVar(5);
			// // updateQuestStatus(env);
			// return true;
			// }
			// }
			// }
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 804730) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					case SETPRO1:
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					case SETPRO3:
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804742) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					case SETPRO6:
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804731) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					case SET_SUCCEED:
						qs.setQuestVar(7);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804730) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
