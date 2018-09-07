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
import java.util.Set;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Cheatkiller
 * @updated 4.3 Eloann
 */
@InstanceID(300510000)
public class TiamatStrongHoldInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;
	private boolean isInstanceDestroyed;
	private int drakans;
	private boolean startSuramaEvent;

	@Override
	public void onDie(Npc npc) {
		if (isInstanceDestroyed) {
			return;
		}
		switch (npc.getNpcId()) {
			case 730612:
				firstWave();
				break;
			case 219373:
			case 219369:
			case 219411:
			case 219370:
				drakans++;
				if (drakans == 5) {
					secondWave();
				}
				else if (drakans == 12) {
					thirdWave();
				}
				break;
			case 219352: // Invincible Shabokan
				sendMsg(1401614);
				spawn(219308, 1175.65f, 1069.08f, 498.52f, (byte) 0); // satra special forces magicker
				spawn(701501, 1075.4409f, 1078.5071f, 787.685f, (byte) 16); // balaur medal box
				doors.get(48).setOpen(true);
				spawnKahrun();
				break;
			case 219357: // Adjutant Anuhart
				sendMsg(1401614);
				spawn(701501, 1077.1716f, 1058.1995f, 787.685f, (byte) 61); // balaur medal box
				doors.get(37).setOpen(true);
				isDeadBosses();
				break;
			case 219358: // Brigade General Tahabata
				sendMsg(1401614);
				spawn(701541, 677.35785f, 1069.5361f, 499.86716f, (byte) 0); // tahabata's treasure chest
				spawn(730622, 652.4821f, 1069.0302f, 498.7787f, (byte) 0, 82); // central passage teleport
				spawn(283180, 679.88f, 1068.88f, 504.2f, (byte) 119); // tiamat's eye
				isDeadBosses();
				break;
			case 219353: // Brigade General Chantra
				sendMsg(1401614);
				spawn(701501, 1071.5909f, 1040.6797f, 787.685f, (byte) 23); // balaur medal box
				doors.get(711).setOpen(true);
				isDeadBosses();
				break;
			case 219354: // Brigade General Terath
				sendMsg(1401614);
				spawn(283178, 1030.03f, 301.83f, 411f, (byte) 26); // tiamat's eye
				spawn(701501, 1086.274f, 1098.3997f, 787.685f, (byte) 90); // balaur medal box
				spawn(730622, 1029.792f, 267.0502f, 409.7982f, (byte) 0, 83); // central passage teleport
				isDeadBosses();
				break;
			case 219355: // Traitor Kumbanda
				sendMsg(1401614);
				spawn(701501, 1063.5973f, 1092.7402f, 787.685f, (byte) 107); // balaur medal box
				doors.get(51).setOpen(true);
				doors.get(54).setOpen(true);
				doors.get(78).setOpen(true);
				doors.get(11).setOpen(true);
				doors.get(79).setOpen(true);
				isDeadBosses();
				break;
			case 219356: // Brigade General Laksyaka
				sendMsg(1401614);
				spawn(701501, 1099.8691f, 1047.1895f, 787.685f, (byte) 64); // balaur medal box
				spawn(730622, 644.4221f, 1319.6221f, 488.7422f, (byte) 0, 15); // central passage teleport
				spawn(701527, 1073.948f, 1068.8732f, 787.685f, (byte) 61); // stronghold treasure chest
				spawn(800438, 665.63409f, 1319.7051f, 487.9f, (byte) 61); // murugan the loyal
				spawn(283179, 629.1f, 1319.5f, 501.2f, (byte) 0); // tiamat's eye
				isDeadBosses();
				break;
		}
	}

	private void firstWave() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				attackPlayer((Npc) spawn(219373, 1505.09f, 1068.54f, 491.38f, (byte) 0));
				attackPlayer((Npc) spawn(219369, 1510.54f, 1058.04f, 491.5f, (byte) 0));
				attackPlayer((Npc) spawn(219411, 1517.38f, 1063.5f, 491.52f, (byte) 0));
				attackPlayer((Npc) spawn(219411, 1516.81f, 1073.6f, 491.52f, (byte) 0));
				attackPlayer((Npc) spawn(219369, 1510.41f, 1078.8f, 491.52f, (byte) 0));
			}
		}, 5000);
	}

	private void secondWave() {
		attackPlayer((Npc) spawn(219370, 1426.08f, 1068.41f, 491.38f, (byte) 0));
		attackPlayer((Npc) spawn(219369, 1430.3f, 1061.13f, 491.5f, (byte) 0));
		attackPlayer((Npc) spawn(219411, 1428.5f, 1056.6f, 491.52f, (byte) 0));
		attackPlayer((Npc) spawn(219411, 1439.49f, 1058.5f, 491.4f, (byte) 0));
		attackPlayer((Npc) spawn(219369, 1430.3f, 1075.49f, 491.52f, (byte) 0));
		attackPlayer((Npc) spawn(219411, 1439.4f, 1078.6f, 491.4f, (byte) 0));
		attackPlayer((Npc) spawn(219411, 1428.5f, 1080.9f, 491.46f, (byte) 0));
	}

	private void thirdWave() {
		attackPlayer((Npc) spawn(219370, 1296.1f, 1068.3f, 491.38f, (byte) 0));
		attackPlayer((Npc) spawn(219411, 1290.9f, 1059.13f, 491.5f, (byte) 0));
		attackPlayer((Npc) spawn(219369, 1300.6f, 1056.4f, 491.52f, (byte) 0));
		attackPlayer((Npc) spawn(219411, 1302.78f, 1053.55f, 491.4f, (byte) 0));
		attackPlayer((Npc) spawn(219411, 1290.94f, 1077.8f, 491.52f, (byte) 0));
		attackPlayer((Npc) spawn(219369, 1300.6f, 1080.3f, 491.4f, (byte) 0));
		attackPlayer((Npc) spawn(219411, 1302.78f, 1082.8f, 491.5f, (byte) 0));
	}

	private void attackPlayer(final Npc npc) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						npc.setTarget(player);
						((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
						npc.setState(1);
						npc.getMoveController().moveToTargetObject();
						PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
					}
				}
			}
		}, 2000);
	}

	private void spawnKahrun() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				moveToForward((Npc) spawn(800463, 1201.272f, 1074.5463f, 491f, (byte) 61), 1039.5f, 1075.9f, 497.3f, false); // elite reian fighter
				moveToForward((Npc) spawn(800463, 1201.272f, 1072.5137f, 491f, (byte) 61), 1130, 1072, 497.3f, false);
				moveToForward((Npc) spawn(800463, 1192.8656f, 1071.1085f, 491f, (byte) 61), 1112, 1070, 497, false);
				moveToForward((Npc) spawn(800463, 1201.272f, 1064.1759f, 491f, (byte) 61), 1039, 1061, 497.3f, false);
				moveToForward((Npc) spawn(800463, 1208.4175f, 1071.1797f, 491f, (byte) 61), 1133, 1072.5f, 497.3f, false);
				moveToForward((Npc) spawn(800463, 1192.8656f, 1068.3411f, 491f, (byte) 61), 1114, 1067, 496.7f, false);
				moveToForward((Npc) spawn(800463, 1208.4175f, 1068.3979f, 491f, (byte) 61), 1133.32f, 1066.47f, 497.3f, false);
				moveToForward((Npc) spawn(800463, 1201.272f, 1066.2085f, 491f, (byte) 61), 1128.8f, 1067, 497.3f, false);
				moveToForward((Npc) spawn(800380, 1190.323f, 1068.1558f, 491.03488f, (byte) 61), 1108, 1066, 497.3f, false); // elite reian sniper
				moveToForward((Npc) spawn(800374, 1188.4259f, 1066.4757f, 491.55029f, (byte) 61), 1094, 1064, 497.4f, true); // elite reian assaulter
				moveToForward((Npc) spawn(800374, 1188.2158f, 1074.2047f, 491.55029f, (byte) 61), 1092.5f, 1074.6f, 497.4f, true);
				moveToForward((Npc) spawn(800376, 1190.3859f, 1071.6548f, 491.03488f, (byte) 61), 1109, 1073, 497.2f, false); // elite reian assaulter
				moveToForward((Npc) spawn(800461, 1184.7582f, 1068.6f, 491.03488f, (byte) 61), 1111, 1068.6f, 497.33f, false); // sorus
				moveToForward((Npc) spawn(800460, 1184.7358f, 1070.77f, 491.03488f, (byte) 61), 1111, 1071, 497, false); // ariana
				moveToForward((Npc) spawn(800347, 1178.0425f, 1072.28f, 491.02545f, (byte) 61), 1106, 1072, 497.2f, false); // garnon
				moveToForward((Npc) spawn(800336, 1178.0559f, 1069.6f, 491.02545f, (byte) 61), 1104, 1069, 497, true); // kahrun
			}
		}, 7000);
	}

	private void moveToForward(final Npc npc, float x, float y, float z, boolean despawn) {
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
		if (despawn) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (npc.getNpcId() == 800336) {
						spawn(800338, 1104, 1069f, 497, (byte) 61);
						Npc kahrun = getNpc(800338);
						NpcShoutsService.getInstance().sendMsg(kahrun, 1500599, kahrun.getObjectId(), 0, 1000);
						NpcShoutsService.getInstance().sendMsg(kahrun, 1500600, kahrun.getObjectId(), 0, 5000);
					}
					npc.getController().onDelete();
				}
			}, 13000);
		}
	}

	private void spawnColonels() {
		int rand = Rnd.get(0, 3);
		switch (rand) {
			case 0:
				spawn(219364, 763.4179f, 1445.6504f, 495.6519f, (byte) 90);
				spawn(219395, 893.7009f, 1445.4846f, 495.6421f, (byte) 90);
				spawn(219395, 893.3f, 1190.71f, 495.6f, (byte) 30);
				spawn(219395, 762.6f, 1192.1f, 495.6f, (byte) 30);
				break;
			case 1:
				spawn(219395, 763.4179f, 1445.6504f, 495.6519f, (byte) 90);
				spawn(219364, 893.7009f, 1445.4846f, 495.6421f, (byte) 90);
				spawn(219395, 893.3f, 1190.71f, 495.6f, (byte) 30);
				spawn(219395, 762.6f, 1192.1f, 495.6f, (byte) 30);
				break;
			case 2:
				spawn(219395, 763.4179f, 1445.6504f, 495.6519f, (byte) 90);
				spawn(219395, 893.7009f, 1445.4846f, 495.6421f, (byte) 90);
				spawn(219364, 893.3f, 1190.71f, 495.6f, (byte) 30);
				spawn(219395, 762.6f, 1192.1f, 495.6f, (byte) 30);
				break;
			case 3:
				spawn(219395, 763.4179f, 1445.6504f, 495.6519f, (byte) 90);
				spawn(219395, 893.7009f, 1445.4846f, 495.6421f, (byte) 90);
				spawn(219395, 893.3f, 1190.71f, 495.6f, (byte) 30);
				spawn(219364, 762.6f, 1192.1f, 495.6f, (byte) 30);
				break;
		}
	}

	private boolean isDeadBosses() {
		Npc boss = getNpc(219356); // Brigade General Laksyaka
		Npc boss1 = getNpc(219355); // Traitor Kumbanda
		Npc boss2 = getNpc(219354); // Brigade General Terath
		Npc boss3 = getNpc(219353); // Brigade General Chantra
		Npc boss4 = getNpc(219358); // Brigade General Tahabata
		Npc boss5 = getNpc(219357); // Adjutant Anuhart
		Npc boss6 = getNpc(219352); // Invincible Shabokan
		if (isDead(boss) && isDead(boss1) && isDead(boss2) && isDead(boss3) && isDead(boss4) && isDead(boss5) && isDead(boss6)) {
			spawn(800464, 1119.7076f, 1071.1401f, 496.8615f, (byte) 119); // reian sorcerer
			spawn(800465, 1119.7421f, 1068.4998f, 496.8616f, (byte) 3); // reian sorcerer
			spawn(730629, 1121.3807f, 1069.8124f, 500.3319f, (byte) 0, 555); // tiamat stronghold exit
			return true;
		}
		return false;
	}

	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}

	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("LAKSYAKA_LEGION_HQ_300510000")) {
			if (!startSuramaEvent) {
				startSuramaEvent = true;
				spawn(800433, 725.93f, 1319.9f, 490.7f, (byte) 61); // surama the traitor
			}
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 701494: // research center access controller
				doors.get(22).setOpen(true);
				break;
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		doors.get(610).setOpen(true);
		doors.get(706).setOpen(true);
		spawnColonels();
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, true, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 1534.8584f, 1067.791f, 491.3514f, (byte) 60);
		return true;
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 701527: // stronghold treasure chest
				int index = dropItems.size() + 1;
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000201, 1));
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000147, 2));
					}
				}
				break;
			case 219363:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npc.getNpcId(), 185000120, 1));
				break;
			case 219364:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npc.getNpcId(), 185000121, 1));
				break;
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
