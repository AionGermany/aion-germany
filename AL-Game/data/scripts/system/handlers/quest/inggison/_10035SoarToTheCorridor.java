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
package quest.inggison;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 * @rework FrozenKiller Note: Todo missing Movie or Bombexplode ?
 */
public class _10035SoarToTheCorridor extends QuestHandler {

	private final static int questId = 10035;
	private final static int[] mobs = { 220021, 220022, 216775 };

	public _10035SoarToTheCorridor() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 798928, 799025, 798958, 798996, 702663, 798926 };
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("ANGRIEF_GATE_210050000"), questId);
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798928: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 7) {
								return sendQuestDialog(env, 3398);
							}
						}
						case SETPRO1: {
							return defaultCloseDialog(env, 0, 1);
						}
						case SET_SUCCEED: {
							qs.setQuestVar(8);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 8, 8, true, false);
						}
						default:
							break;
					}
					break;
				}
				case 799025: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2);
						}
						default:
							break;

					}
					break;
				}
				case 798958: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO3: {
							return defaultCloseDialog(env, 2, 3);
						}
						default:
							break;
					}
					break;
				}
				case 798996: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						}
						case SETPRO4: {
							giveQuestItem(env, 182215629, 1);
							playQuestMovie(env, 517);
							return defaultCloseDialog(env, 3, 4);
						}
						default:
							break;
					}
					break;
				}
				case 702663: {
					switch (dialog) {
						case USE_OBJECT: {
							if (var == 6) {
								removeQuestItem(env, 182215629, 1);
								QuestService.addNewSpawn(210050000, player.getInstanceId(), 700641, 1377, 2297, 296, (byte) 90);
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return closeDialogWindow(env);
							}
						}
						default:
							break;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798926) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 5) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 9) {
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 9) {
					qs.setQuestVar(6);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (zoneName.equals(ZoneName.get("ANGRIEF_GATE_210050000"))) {
				if (var == 4) {
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10034, true);
	}
}
