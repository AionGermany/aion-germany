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
package quest.highdaeva;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Falke_34, FrozenKiller
 * @rework CmDDavid
 */
public class _20521RecoveredMemories extends QuestHandler {

	private final static int questId = 20521;

	public _20521RecoveredMemories() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcids = new int[] { 806135, 806079, 703165, 731667, 731668, 806136, 731669, 806137, 703130 };
		qe.registerOnLevelUp(questId);
		qe.registerOnMovieEndQuest(871, questId);
		qe.registerQuestNpc(857783).addOnKillEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_Q_13TH_ARCHIVE_301570000"), questId);
		qe.registerOnMovieEndQuest(34, questId);
		qe.registerOnMovieEndQuest(923, questId);
		qe.registerQuestNpc(857785).addOnKillEvent(questId);
		qe.registerOnMovieEndQuest(927, questId);
		qe.registerQuestNpc(857903).addOnKillEvent(questId);
		qe.registerOnLogOut(questId);
		for (int i : npcids) {
			qe.registerQuestNpc(i).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
			return false;
		int var = qs.getQuestVars().getQuestVars();
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 806135: // Konratu
					switch (env.getDialog()) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 1011);
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1);
						default:
							break;
					}
					break;
				case 806079: // Peregran
					switch (env.getDialog()) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 1352);
						case SETPRO2:
							return defaultCloseDialog(env, 1, 2);
						default:
							break;
					}
					break;
				case 703165: // Tower Fragment
					if (var == 2) {
						changeQuestStep(env, 2, 3, false);
						playQuestMovie(env, 871);
						return defaultCloseDialog(env, 3, 4);
					}
					break;
				case 731667: // Atreia's Protector
					switch (env.getDialog()) {
						case USE_OBJECT:
							return sendQuestDialog(env, 2716);
						case SETPRO6:
							return defaultCloseDialog(env, 5, 6);
						default:
							break;
					}
					break;
				case 731668: // Israphel's Plan
					switch (env.getDialog()) {
						case USE_OBJECT:
							return sendQuestDialog(env, 3057);
						case SETPRO7:
							return defaultCloseDialog(env, 6, 7);
						default:
							break;
					}
					break;
				case 806136: // Leibo
					switch (env.getDialog()) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 3739);
						case SETPRO9:
							return defaultCloseDialog(env, 8, 9);
						default:
							break;
					}
					break;
				case 731669: // Lost Memory
					switch (env.getDialog()) {
						case USE_OBJECT:
							return sendQuestDialog(env, 4080);
						case SETPRO10:
							playQuestMovie(env, 1, 34);
							return defaultCloseDialog(env, 9, 10);
						default:
							break;
					}
					break;
				case 806137: // Leibo
					switch (env.getDialog()) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 6841);
						case SETPRO12:
							SpawnTemplate spawn1 = SpawnEngine.addNewSpawn(301570000, 857914, 355.74426f, 500.22577f, 468.937f, (byte) 109, 0);
							SpawnTemplate spawn2 = SpawnEngine.addNewSpawn(301570000, 857914, 357.532f, 499.3291f, 468.93698f, (byte) 111, 0);
							SpawnTemplate spawn3 = SpawnEngine.addNewSpawn(301570000, 857915, 349.2551f, 505.73633f, 468.93698f, (byte) 0, 0);
							SpawnTemplate spawn4 = SpawnEngine.addNewSpawn(301570000, 857915, 349.36804f, 518.781f, 468.93698f, (byte) 0, 0);
							SpawnTemplate spawn5 = SpawnEngine.addNewSpawn(301570000, 857916, 349.9573f, 512.21313f, 468.937f, (byte) 0, 0);
							SpawnEngine.spawnObject(spawn1, env.getPlayer().getInstanceId());
							SpawnEngine.spawnObject(spawn2, env.getPlayer().getInstanceId());
							SpawnEngine.spawnObject(spawn3, env.getPlayer().getInstanceId());
							SpawnEngine.spawnObject(spawn4, env.getPlayer().getInstanceId());
							SpawnEngine.spawnObject(spawn5, env.getPlayer().getInstanceId());
							return defaultCloseDialog(env, 11, 12);
						default:
							break;
					}
					break;
				case 703130:
					if (var == 12) {
						playQuestMovie(env, 923);
						return closeDialogWindow(env);
					}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (dialog == DialogAction.USE_OBJECT) {
				return sendQuestDialog(env, 10002);
			}
			else {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 20520);
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 871) {
			WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(301570000, 0);
			InstanceService.registerPlayerWithInstance(newInstance, env.getPlayer());
			TeleportService2.teleportTo(env.getPlayer(), 301570000, newInstance.getInstanceId(), 683.4529f, 433.97238f, 468.86707f, (byte) 63);
			return true;
		}
		else if (movieId == 34) {
			SpawnTemplate spawn1 = SpawnEngine.addNewSpawn(301570000, 857948, 459.73734f, 654.10614f, 468.97748f, (byte) 20, 0);
			SpawnTemplate spawn2 = SpawnEngine.addNewSpawn(301570000, 857948, 445.6286f, 662.73706f, 468.97742f, (byte) 20, 0);
			SpawnTemplate spawn3 = SpawnEngine.addNewSpawn(301570000, 857903, 451.52322f, 655.9117f, 468.97745f, (byte) 20, 0);
			SpawnEngine.spawnObject(spawn1, env.getPlayer().getInstanceId());
			SpawnEngine.spawnObject(spawn2, env.getPlayer().getInstanceId());
			SpawnEngine.spawnObject(spawn3, env.getPlayer().getInstanceId());
			return true;
		}
		else if (movieId == 923) {
			changeQuestStep(env, 12, 13, false);
			SpawnTemplate spawn = SpawnEngine.addNewSpawn(301570000, 857788, 231.44054f, 512.0783f, 468.8022f, (byte) 0, 0);
			SpawnEngine.spawnObject(spawn, env.getPlayer().getInstanceId());
			return true;
		}
		else if (movieId == 927) {
			Npc npc = env.getPlayer().getPosition().getWorldMapInstance().getNpc(703130);
			npc.getController().delete();
			SpawnTemplate spawn1 = SpawnEngine.addNewSpawn(301570000, 857914, 255.45573f, 509.56747f, 468.84967f, (byte) 0, 0);
			SpawnTemplate spawn2 = SpawnEngine.addNewSpawn(301570000, 806180, 224.43904f, 512.28107f, 469.8968f, (byte) 0, 0);
			spawn2.setStaticId(35);
			SpawnEngine.spawnObject(spawn1, env.getPlayer().getInstanceId());
			SpawnEngine.spawnObject(spawn2, env.getPlayer().getInstanceId());
			PacketSendUtility.sendPacket(env.getPlayer(), new SM_SYSTEM_MESSAGE(1403304));
			return true;
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		switch (targetId) {
			case 857783:
				if (var == 4) {
					changeQuestStep(env, 4, 5, false);
					return true;
				}
				break;
			case 857903:
				if (var == 10) {
					changeQuestStep(env, 10, 11, false);
					return true;
				}
				break;
			case 857785:
				if (var == 13) {
					changeQuestStep(env, 13, 14, true);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return playQuestMovie(env, 927);
				}
				break;
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("IDETERNITY_Q_13TH_ARCHIVE_301570000")) {
			Player player = env.getPlayer();
			if (player == null)
				return false;
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				int var = qs.getQuestVarById(0);
				if (var == 7) {
					changeQuestStep(env, 7, 8, false);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		Player player = env.getPlayer();
		if (player != null) {
			QuestState qs = player.getQuestStateList().getQuestState(questId);

			if (qs != null && qs.getStatus() == QuestStatus.START) {
				int var = qs.getQuestVars().getQuestVars();
				if (var > 2)
					qs.setQuestVar(2);
			}
		}
		return false;
	}
}
