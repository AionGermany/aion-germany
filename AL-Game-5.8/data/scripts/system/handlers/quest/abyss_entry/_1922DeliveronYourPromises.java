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
package quest.abyss_entry;

import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Hellboy
 * @author aion4Free
 * @author Gigi
 * @author vlog
 */
public class _1922DeliveronYourPromises extends QuestHandler {

	private final static int questId = 1922;
	private int choice = 0;
	private final static int[] npc_ids = { 203830, 203901, 203764 };
	private final static int[] mob_ids = { 213580, 213581, 213582 };

	public _1922DeliveronYourPromises() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerOnQuestTimerEnd(questId);
		qe.registerOnEnterWorld(questId);
		for (int npc : npc_ids) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mob_ids) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("SANCTUM_UNDERGROUND_ARENA_310080000"), questId);
		qe.registerOnMovieEndQuest(165, questId);
		qe.registerOnMovieEndQuest(166, questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203830: { // Fuchsia
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 4) {
								return sendQuestSelectionDialog(env);
							}
						}
						case SETPRO12: {
							choice = 1;
							return defaultCloseDialog(env, 0, 4); // 4
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default:
							break;
					}
					break;
				}
				case 203901: { // Telemachus
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							if (var == 7) {
								return sendQuestDialog(env, 3739);
							}
						}
						case SELECT_QUEST_REWARD:
							if (var == 7) {
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 6);
							}
						default:
							break;
					}
					break;
				}
				case 203764: { // Epeios
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							if (var == 4) {
								return sendQuestDialog(env, 1693);
							}
							else if (qs.getQuestVarById(4) == 10) {
								return sendQuestDialog(env, 2034);
							}
						}
						case SETPRO3: {
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(310080000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService2.teleportTo(player, 310080000, newInstance.getInstanceId(), 276, 293, 163, (byte) 90);
							changeQuestStep(env, 4, 5, false); // 5
							return closeDialogWindow(env);
						}
						case SETPRO4: {
							changeQuestStep(env, 5, 7, false); // 7
							return closeDialogWindow(env);
						}
						default:
							break;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203901) { // Telemachus
				return sendQuestEndDialog(env, choice);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		if (qs.getStatus() == QuestStatus.START) {
			if (qs.getQuestVarById(0) == 5) {
				if (qs.getQuestVarById(4) < 9) {
					return defaultOnKillEvent(env, mob_ids, 0, 9, 4); // 4: 1 - 9
				}
				else if (qs.getQuestVarById(4) == 9) {
					defaultOnKillEvent(env, mob_ids, 9, 10, 4); // 4: 10
					QuestService.questTimerEnd(env);
					for (Npc npcInside : player.getPosition().getWorldMapInstance().getNpcs()) {
						CreatureActions.delete(npcInside);
					}
					playQuestMovie(env, 166);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onQuestTimerEndEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var4 = qs.getQuestVarById(4);
			if (var4 < 10) {
				qs.setQuestVar(4);
				updateQuestStatus(env);
				TeleportService2.teleportTo(player, 110010000, 1466.036f, 1337.2749f, 566.41583f, (byte) 86);
				return true;
			}
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
			int var0 = qs.getQuestVarById(0);
			int var4 = qs.getQuestVars().getVarById(4);
			if (var0 == 5 && var4 != 10) {
				if (player.getWorldId() != 310080000) {
					QuestService.questTimerEnd(env);
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
				else {
					playQuestMovie(env, 165);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 165) {
			QuestService.questTimerStart(env, 240);
			return true;
		}
		else if (movieId == 166) {
			Player player = env.getPlayer();
			TeleportService2.teleportTo(player, 110010000, (float) 1468, (float) 1336, (float) 567, (byte) 30);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1921);
	}
}
