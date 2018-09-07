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
package quest.altgard;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author pralinka
 * @rework FrozenKiller
 */
public class _24016AStrangeNewThreat extends QuestHandler {

	private final static int questId = 24016;
	private final static int[] npcs = { 203557, 700140, 700141 };

	public _24016AStrangeNewThreat() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnDie(questId);
		qe.registerOnEnterWorld(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(233876).addOnKillEvent(questId);
		qe.registerOnMovieEndQuest(154, questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (qs == null) {
			return false;
		}
		
		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 203557: { // Suthran
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SELECT_ACTION_1013: {
							playQuestMovie(env, 66);
							return sendQuestDialog(env, 1013);
						}
						case SETPRO1: {
							TeleportService2.teleportTo(player, 220030000, 2453.1934f, 2555.148f, 316.267f, (byte) 60, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 0, 1, false); // 1
							return closeDialogWindow(env);
						}
						default:
							return sendQuestStartDialog(env);
					}
				}
				case 700140: { // Gate Guardian Stone
					if (dialog == DialogAction.USE_OBJECT) {
						if (var == 2) {
							QuestService.addNewSpawn(320030000, player.getInstanceId(), 233876, (float) 260.12, (float) 234.93, (float) 216.00, (byte) 90);
							return useQuestObject(env, 2, 3, false, false); // 3
						}
						else {
							return closeDialogWindow(env);
						}
					}
				}
				case 700141: {
					if (dialog == DialogAction.USE_OBJECT) {
						if (var == 4) {
							playQuestMovie(env, 154);
							return useQuestObject(env, 4, 4, true, false); // reward
						}
						else {
							return closeDialogWindow(env);
						}
					}
				}
				default:
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203557) { // Suthran
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 1352);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 154) {
			TeleportService2.teleportTo(env.getPlayer(), 220030000, 1683, 1753, 260, (byte) 60, TeleportAnimation.BEAM_ANIMATION);
			return true;
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var >= 2 && player.getWorldId() != 320030000) {
				changeQuestStep(env, var, 1, false);
				return true;
			}
			else if (var == 1 && player.getWorldId() == 320030000) {
				changeQuestStep(env, 1, 2, false); // 2
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 233876, 3, 4); // 4
	}

	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var >= 2) {
				qs.setQuestVar(1);
				updateQuestStatus(env);
				return true;
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
		int[] altgardQuests = { 24011, 24012, 24013, 24014, 24015 };
		return defaultOnLvlUpEvent(env, altgardQuests, true);
	}
}
