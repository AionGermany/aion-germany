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
package instance;

import java.util.Map;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Alcapwnd
 */
@InstanceID(301380000)
public class DanuarSanctuaryInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;
	private boolean isInstanceDestroyed;
	private int RefugeRuneTasksStart;
	@SuppressWarnings("unused")
	private int BossesKill;
	private Race instanceRace;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		SpawnRace();
	}

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		switch (player.getRace()) {
			case ELYOS:
				// Start
				spawn(804862, 402.55734f, 1169.1189f, 55.263813f, (byte) 33); // Alkatron
				break;
			case ASMODIANS:
				// Start
				spawn(804863, 375.02567f, 1168.3021f, 55.248016f, (byte) 33); // Dintanum
				break;
			default:
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
		if (isInstanceDestroyed) {
			return;
		}
		// Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 233391:
				RefugeRuneTasksStart++;
				if (RefugeRuneTasksStart == 1) {
					VritraLegionMove(50000);
					VritraLegionMove(500000);
					VritraLegionMove(1000000);
					VritraLegionMove(1500000);
					VritraLegionMove(2000000);
				}
				break;

			// No drops Npc's
			case 233187:
			case 233139:
			case 233427:
			case 233082:
			case 233085:
			case 233081:
			case 233138:
				despawnNpc(npc);
				break;

			case 730865:
				Npc smoke = (Npc) spawn(282786, 1099.0125f, 978.01031f, 288.21808f, (byte) 0);
				CreatureActions.delete(smoke);
				despawnNpc(npc);
				break;

			case 730866:
				Npc smoke2 = (Npc) spawn(282786, 937.55585f, 876.63904f, 305.12701f, (byte) 0);
				CreatureActions.delete(smoke2);
				despawnNpc(npc);
				break;

			case 233189:
				Npc smoke3 = (Npc) spawn(282786, 1056.257080f, 808.344727f, 286.157471f, (byte) 0);
				CreatureActions.delete(smoke3);
				despawnNpc(npc);
				break;

			case 233188:
				spawn(282009, 906.1991f, 859.88177f, 278.64731f, (byte) 0, 1699);
				despawnNpc(npc);
				break;
		}
	}

	private void SpawnRace() {
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 1031.87f, 317.41f, 301.03f, (byte) 0);
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 1021.13f, 308.83f, 301.03f, (byte) 15);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 1026.92f, 322.69f, 301.03f, (byte) 119);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 1019.32f, 310.76f, 301.03f, (byte) 15);
		spawn((instanceRace == Race.ELYOS ? 233076 : 233073), 1031.24f, 333.21f, 300.98f, (byte) 90);
		// zone A2
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 1088.40f, 362.21f, 300.98f, (byte) 90);
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 1095.10f, 381.89f, 300.95f, (byte) 77);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 1089.12f, 372.47f, 300.98f, (byte) 90);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 1096.83f, 380.47f, 300.95f, (byte) 76);
		spawn((instanceRace == Race.ELYOS ? 233076 : 233073), 1076.59f, 369.56f, 300.98f, (byte) 0);
		// zone B1
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 931.43f, 939.95f, 292.86f, (byte) 49);
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 945.57f, 936.32f, 292.88f, (byte) 61);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 939.16f, 943.09f, 292.90f, (byte) 79);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 945.97f, 934.18f, 292.89f, (byte) 60);
		spawn((instanceRace == Race.ELYOS ? 233076 : 233073), 929.16f, 933.11f, 292.93f, (byte) 19);
		// zone B2
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 853.25f, 921.08f, 292.81f, (byte) 18);
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 837.16f, 914.54f, 292.73f, (byte) 4);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 844.38f, 909.79f, 292.79f, (byte) 20);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 836.81f, 916.27f, 292.75f, (byte) 4);
		spawn((instanceRace == Race.ELYOS ? 233076 : 233073), 852.84f, 909.26f, 292.76f, (byte) 50);
		// zone C1
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 1114.67f, 1194.44f, 304.26f, (byte) 71);
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 1122.51f, 1205.53f, 304.47f, (byte) 85);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 1127.44f, 1198.98f, 304.18f, (byte) 71);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 1124.37f, 1204.99f, 304.45f, (byte) 84);
		spawn((instanceRace == Race.ELYOS ? 233076 : 233073), 1127.11f, 1191.40f, 304.37f, (byte) 43);
		// zone C2
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 1031.56f, 1136.31f, 304.37f, (byte) 11);
		spawn((instanceRace == Race.ELYOS ? 233078 : 233075), 1018.34f, 1131.81f, 304.37f, (byte) 1);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 1025.08f, 1127.81f, 304.37f, (byte) 12);
		spawn((instanceRace == Race.ELYOS ? 233077 : 233074), 1018.20f, 1133.70f, 304.37f, (byte) 1);
		spawn((instanceRace == Race.ELYOS ? 233076 : 233073), 1028.60f, 1122.31f, 304.37f, (byte) 41);
	}

	private void VritraLegionMove(final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (time == 50000) {
					sendMsg(1401855); // content
				}
				if (time == 500000) {
					sendMsg(1401856); // content
				}
				if (time == 1000000) {
					sendMsg(1401857); // content
				}
				if (time == 1500000) {
					sendMsg(1401858); // content
				}
				if (time == 2000000) {
					sendMsg(1401859); // content
					// Despawn Boss
					despawnNpc(getNpc(233380));
					despawnNpc(getNpc(233381));
					despawnNpc(getNpc(233382));
				}
			}
		}, time);
	}

	private void destroyDoor1(final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (time == 5000) {
					killNpc(getNpc(730865));
				}
			}
		}, time);
	}

	private void destroyDoor2(final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (time == 5000) {
					killNpc(getNpc(730866));
				}
			}
		}, time);
	}

	private void destroyDoor3(final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (time == 2000) {
					killNpc(getNpc(233189));
				}
			}
		}, time);
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		int quest = player.getRace().equals(Race.ASMODIANS) ? 26983 : 16983;
		switch (npc.getNpcId()) {
			case 701860: // Mystik Keystone
				sendMsg(1401838);
				doors.get(159).setOpen(true);
				break;
			case 701859: // Mystik Keystone
				sendMsg(1401838);
				doors.get(350).setOpen(true);
				break;
			case 701861: // Mystik Keystone
				sendMsg(1401838);
				doors.get(690).setOpen(true);
				break;
			case 701862: // Cubic Keystone
				sendMsg(1401838);
				doors.get(160).setOpen(true);
				if (player != null) {
					QuestState qs = player.getQuestStateList().getQuestState(player.getRace() == Race.ASMODIANS ? 26983 : 16983);
					if (qs != null && qs.getStatus() == QuestStatus.START) {
						if (qs.getQuestVarById(0) == 0) {
							qs.setQuestVar(qs.getQuestVarById(0) + 1);
							qs.setStatus(QuestStatus.REWARD);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(quest, qs.getStatus(), qs.getQuestVars().getQuestVars()));
							return;
						}
					}
				}
				break;
			case 701864: // Pyramidal Keystone
				sendMsg(1401838);
				doors.get(154).setOpen(true);
				if (player != null) {
					QuestState qs = player.getQuestStateList().getQuestState(player.getRace() == Race.ASMODIANS ? 26983 : 16983);
					if (qs != null && qs.getStatus() == QuestStatus.START) {
						if (qs.getQuestVarById(0) == 0) {
							qs.setQuestVar(qs.getQuestVarById(0) + 1);
							qs.setStatus(QuestStatus.REWARD);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(quest, qs.getStatus(), qs.getQuestVars().getQuestVars()));
							return;
						}
					}
				}
				break;
			case 701863: // Spherical Keystone
				sendMsg(1401838);
				doors.get(10).setOpen(true);
				if (player != null) {
					QuestState qs = player.getQuestStateList().getQuestState(player.getRace() == Race.ASMODIANS ? 26983 : 16983);
					if (qs != null && qs.getStatus() == QuestStatus.START) {
						if (qs.getQuestVarById(0) == 0) {
							qs.setQuestVar(qs.getQuestVarById(0) + 1);
							qs.setStatus(QuestStatus.REWARD);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(quest, qs.getStatus(), qs.getQuestVars().getQuestVars()));
							return;
						}
					}
				}
				break;
			case 730863: // Cannon Deck (Destroy door 1)
				SkillEngine.getInstance().getSkill(npc, 20385, 65, npc).useNoAnimationSkill();
				destroyDoor2(5000);
				break;
			case 730864: // Cannon Deck (Destroy door 2)
				SkillEngine.getInstance().getSkill(npc, 20385, 65, npc).useNoAnimationSkill();
				destroyDoor1(5000);
				break;
			case 233331: // Drill door (Destroy door 3)
				if (player.getInventory().getItemCountByItemId(185000174) > 0) {
					player.getInventory().decreaseByItemId(185000174, 1);
					SkillEngine.getInstance().getSkill(npc, 21084, 1, npc).useWithoutPropSkill();
					destroyDoor3(2000);
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401066));
				}
				break;
		}
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private void killNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDie(npc);
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 388.85321f, 1183.4175f, 55.6f, (byte) 90);
		return true;
	}
}
