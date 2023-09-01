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
package quest.cygnea;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 * @rework FrozenKiller
 */
public class _10507ABigPlotToFoil extends QuestHandler {

	public static final int questId = 10507;

	public _10507ABigPlotToFoil() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 804711, 804712, 804713, 804714, 804715 };
		qe.registerQuestNpc(236264).addOnKillEvent(questId);
		qe.registerQuestNpc(236265).addOnKillEvent(questId);
		qe.registerQuestNpc(702668).addOnKillEvent(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("LF5_SensoryArea_Q10507"), questId);
		qe.registerOnMovieEndQuest(993, questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10506, true);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 6) {
				int[] mobs = { 236264, 236265 };
				int[] chest = { 702668 };
				int targetId = env.getTargetId();
				int var1 = qs.getQuestVarById(1);
				int var2 = qs.getQuestVarById(2);
				switch (targetId) {
					case 236264:
					case 236265: {
						if (var1 < 4) {
							return defaultOnKillEvent(env, mobs, 0, 4, 1);
						}
						else if (var1 == 4) {
							if (var2 == 3) {
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, mobs, 4, 5, 1);
							}
						}
						break;
					}
					case 702668: {
						if (var2 < 2) {
							return defaultOnKillEvent(env, chest, 0, 2, 2);
						}
						else if (var2 == 2) {
							if (var1 == 5) {
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, chest, 2, 3, 2);
							}
						}
						break;
					}
				}
			}
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
			if (targetId == 804711) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					case SETPRO1:
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					case SETPRO5:
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804712) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					case SETPRO2:
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804713) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					case SETPRO3:
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804714) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					case SETPRO4:
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804715) {
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
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804711) {
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

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 7) {
				playQuestMovie(env, 993);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (movieId == 993) {
			qs.setQuestVar(8);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			// QuestService.addNewSpawn(210070000, player.getInstanceId(), 236273, player.getX()+2, player.getY()+2, player.getZ()+1, (byte) 0);
			return true;
		}
		return false;
	}
}
