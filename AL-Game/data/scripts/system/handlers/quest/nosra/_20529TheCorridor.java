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
package quest.nosra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _20529TheCorridor extends QuestHandler {

	public static final int questId = 20529;

	public _20529TheCorridor() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182216087, questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806079).addOnTalkEvent(questId); // Peregran
		qe.registerQuestNpc(806300).addOnTalkEvent(questId); // Vast
		qe.registerQuestNpc(731716).addOnTalkEvent(questId); // Generador de Pasillo
		qe.registerQuestNpc(806298).addOnTalkEvent(questId); // Wizabo
		qe.registerQuestNpc(703325).addOnTalkEvent(questId); // Falla
		qe.registerQuestNpc(806299).addOnTalkEvent(questId); // Wizabo Downcast
		qe.registerOnEnterZone(ZoneName.get("301690000"), questId); // Mina de Eter
		qe.registerQuestNpc(244128).addOnKillEvent(questId);
		qe.registerQuestNpc(244127).addOnKillEvent(questId);
		qe.registerQuestNpc(244129).addOnKillEvent(questId);
		qe.registerOnMovieEndQuest(878, questId);
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
		DialogAction dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 806079: { // Peregrans
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 10) {
								changeQuestStep(env, 10, 11, true); // 11
								return closeDialogWindow(env);
							}
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1); // 1
						default:
							break;
					}
				}
					break;
				case 806300: { // Vast
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						case SETPRO2:
							return defaultCloseDialog(env, 1, 2); // 2
						default:
							break;
					}
				}
					break;
				case 731716: { // Generador de Pasillo
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
							else if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 2, 3, false, 10000, 10001);
						}
						case SETPRO4:
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(301690000, 0);
							InstanceService.registerPlayerWithInstance(newInstance, env.getPlayer());
							TeleportService2.teleportTo(env.getPlayer(), 301690000, newInstance.getInstanceId(), 323.2161f, 267.80365f, 259.34897f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 3, 4);

						default:
							break;
					}
				}
					break;
				case 806298: { // Wizabo
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						case SETPRO6:
							return defaultCloseDialog(env, 5, 6); // 2
						default:
							break;
					}
				}
				case 703325: { // Falla
					switch (dialog) {
						case USE_OBJECT:
							if (var == 7) {
								playQuestMovie(env, 878);
								// return defaultCloseDialog(env, 10, 11);
							}
						default:
							break;
					}
				}
					break;
				case 806299: { // Downcast Wizabo
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 9) {
								return sendQuestDialog(env, 4080);
							}
						case SETPRO10:
							TeleportService2.teleportTo(player, 220110000, 1811.9086f, 1982.908f, 199.19757f, (byte) 112, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 9, 10);
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806079) {
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
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 878) {
			changeQuestStep(env, 7, 8, false);
			Npc npc = env.getPlayer().getPosition().getWorldMapInstance().getNpc(703325);
			npc.getController().delete();
			SpawnTemplate spawn = SpawnEngine.addNewSpawn(301690000, 244129, 172.97343f, 156.89346f, 230.44385f, (byte) 93, 0);
			SpawnTemplate spawn1 = SpawnEngine.addNewSpawn(301690000, 806299, 173.752f, 153.86577f, 230.34056f, (byte) 33, 0);
			SpawnEngine.spawnObject(spawn, env.getPlayer().getInstanceId());
			SpawnEngine.spawnObject(spawn1, env.getPlayer().getInstanceId());
			return true;
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 4 && zoneName == ZoneName.get("301690000")) {
				changeQuestStep(env, 4, 5, false);
				return true;
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
			int targetId = env.getTargetId();
			if (var == 6) {
				int var1 = qs.getQuestVarById(1);
				int var2 = qs.getQuestVarById(2);
				switch (targetId) {
					case 244127: {
						if (var1 < 6) {
							return defaultOnKillEvent(env, 244127, 0, 6, 1);
						}
						else if (var1 == 6) {
							if (var2 == 3) {
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, 244127, 6, 7, 1);
							}
						}
						break;
					}
					case 244128: {
						if (var2 < 2) {
							return defaultOnKillEvent(env, 244128, 0, 2, 2);
						}
						else if (var2 == 2) {
							if (var1 == 7) {
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, 244128, 2, 3, 2);
							}
						}
						break;
					}
				}
			}
			else if (var == 8) {
				switch (targetId) {
					case 244129: {
						if (qs.getQuestVarById(1) != 0) {
							qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
							updateQuestStatus(env);
						}
						else {
							qs.setQuestVarById(0, 9);
							updateQuestStatus(env);
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
}
