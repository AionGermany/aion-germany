/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY;without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package instance;

import java.util.List;
import java.util.Map;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TOWER_OF_CHALLENGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.instance.TowerOfChallengeService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34, FrozenKiller
 */

@InstanceID(302400000)
public class TowerOfChallengeInstance extends GeneralInstanceHandler {

	private Race spawnRace;

	// Floor Named
	private int IDInfinity_Named_04;
	private int IDInfinity_Named_08;
	private int IDInfinity_Named_12;
	private int IDInfinity_Named_16;
	private int IDInfinity_Named_20;
	private int IDInfinity_Named_24;
	private int IDInfinity_Named_28;
	private int IDInfinity_Named_32;
	private int IDInfinity_Named_36;
	private int IDInfinity_Named_40;

	// Floor Normal
	private int IDInfinity_Normal_01;
	private int IDInfinity_Normal_02;
	private int IDInfinity_Normal_03;
	private int IDInfinity_Normal_05;
	private int IDInfinity_Normal_06;
	private int IDInfinity_Normal_07;
	private int IDInfinity_Normal_09;
	private int IDInfinity_Normal_10;
	private int IDInfinity_Normal_11;
	private int IDInfinity_Normal_13;
	private int IDInfinity_Normal_14;
	private int IDInfinity_Normal_15;
	private int IDInfinity_Normal_17;
	private int IDInfinity_Normal_18;
	private int IDInfinity_Normal_19;
	private int IDInfinity_Normal_21;
	private int IDInfinity_Normal_22;
	private int IDInfinity_Normal_23;
	private int IDInfinity_Normal_25;
	private int IDInfinity_Normal_26;
	private int IDInfinity_Normal_27;
	private int IDInfinity_Normal_29;
	private int IDInfinity_Normal_30;
	private int IDInfinity_Normal_31;
	private int IDInfinity_Normal_33;
	private int IDInfinity_Normal_34;
	private int IDInfinity_Normal_35;
	private int IDInfinity_Normal_37;
	private int IDInfinity_Normal_38;
	private int IDInfinity_Normal_39;

	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(302400000, 0);
		InstanceService.registerPlayerWithInstance(newInstance, player);
		if (spawnRace == null) {
			spawnRace = player.getRace();
			spawnInggrilInggness1();
			TowerOfChallengeService.getInstance().spawnSteps(player, 1);
			TowerOfChallengeService.getInstance().spawnWall(player, 1);
			int pfloor = player.getFloor();
			sendPacket(player, "Condition_Infinity_PRE_SEASON_Floor", 27);//LOG = 27
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor", pfloor);
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", pfloor);
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		spawnFloorRings();
		sendMsgByRace(1404397, Race.PC_ALL, 5000);
    }
	
	private void sendPacket(Player player, final String variable, final int floor) {
		PacketSendUtility.sendPacket(player, new SM_TOWER_OF_CHALLENGE(player, variable, floor));
	}
	
	private void spawnInggrilInggness1() {
		final int Inggril_Inggness1 = spawnRace == Race.ASMODIANS ? 247386 : 247376;
		spawn(Inggril_Inggness1, 255.19067f, 249.43465f, 241.0831f, (byte) 60);
	}
	private void spawnInggrilInggness2() {
		final int Inggril_Inggness2 = spawnRace == Race.ASMODIANS ? 247386 : 247376;
		spawn(Inggril_Inggness2, 255.19067f, 249.43465f, 241.0831f, (byte) 60);
	}
	
	
	private void spawnFloorRings() { // TODO
        FlyRing f2 = new FlyRing(new FlyRingTemplate("FLOOR2", mapId,
        new Point3D(1290, 249, 260),
        new Point3D(1290, 254, 244),
        new Point3D(1290, 249, 244), 30), instanceId);
        f2.spawn();
        FlyRing f3 = new FlyRing(new FlyRingTemplate("FLOOR3", mapId,
        new Point3D(1290, 1249, 260),
        new Point3D(1290, 1254, 244),
        new Point3D(1290, 1249, 244), 30), instanceId);
        f3.spawn();
        FlyRing f4 = new FlyRing(new FlyRingTemplate("FLOOR4", mapId,
        new Point3D(290, 1249, 260),
        new Point3D(290, 1254, 244),
        new Point3D(290, 1249, 244), 30), instanceId);
        f4.spawn();
    }

	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
		// Floor 1
		case 247247:
		case 247248:
			despawnNpc(npc);
			IDInfinity_Normal_01++;
			if (IDInfinity_Normal_01 == 3) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 1);
				player.getCommonData().addExp(40000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 2
		case 247249:
		case 247250:
			despawnNpc(npc);
			IDInfinity_Normal_02++;
			if (IDInfinity_Normal_02 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 2);
				ItemService.addItem(player, ItemId.KINAH.value(), 2000000);
			}
			break;
		// Floor 3
		case 247251:
		case 247252:
			despawnNpc(npc);
			IDInfinity_Normal_03++;
			if (IDInfinity_Normal_03 == 6) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 3);
				AbyssPointsService.addAp(player, 10000);
			}
			break;
		// Floor 4
		case 247236:
			despawnNpc(npc);
			IDInfinity_Named_04++;
			if (IDInfinity_Named_04 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 4);
				ItemService.addItem(player, 188057798, 1);
				AbyssPointsService.addGp(player, 20);
			}
			break;
		// Floor 5
		case 247253:
		case 247254:
			despawnNpc(npc);
			IDInfinity_Normal_05++;
			if (IDInfinity_Normal_05 == 7) {
				deleteNpc(247394);
				despawnNpcs(getNpcs(247351));// Meat.
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 5);
				player.getCommonData().addExp(40000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 6
		case 247255:
		case 247256:
			despawnNpc(npc);
			IDInfinity_Normal_06++;
			if (IDInfinity_Normal_06 == 9) {
				deleteNpc(247394);
				despawnNpcs(getNpcs(247352));// Meat.
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 6);
				ItemService.addItem(player, ItemId.KINAH.value(), 2200000);
			}
			break;
		// Floor 7
		case 247257:
		case 247258:
			despawnNpc(npc);
			IDInfinity_Normal_07++;
			if (IDInfinity_Normal_07 == 9) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 7);
				AbyssPointsService.addAp(player, 10500);
			}
			break;
		// Floor 8
		case 247237:
			despawnNpc(npc);
			IDInfinity_Named_08++;
			if (IDInfinity_Named_08 == 1) {
				deleteNpc(247394);
				despawnNpcs(getNpcs(247478));// IDInfinity_Meat.
				despawnNpcs(getNpcs(247353));// IDInfinity_Berrel.
				despawnNpcs(getNpcs(247401));// IDInfinity_Summon_Tog.
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 8);
				ItemService.addItem(player, 162000181, 100);
				ItemService.addItem(player, 162000178, 100);
				AbyssPointsService.addGp(player, 40);
			}
			break;
		// Floor 9
		case 247259:
		case 247260:
			despawnNpc(npc);
			IDInfinity_Normal_09++;
			if (IDInfinity_Normal_09 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 9);
				player.getCommonData().addExp(40000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 10
		case 247261:
		case 247262:
			despawnNpc(npc);
			IDInfinity_Normal_10++;
			if (IDInfinity_Normal_10 == 5) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 10);
				ItemService.addItem(player, ItemId.KINAH.value(), 2400000);
			}
			break;
		// Floor 11
		case 247263:
		case 247264:
			despawnNpc(npc);
			IDInfinity_Normal_11++;
			if (IDInfinity_Normal_11 == 6) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 11);
				AbyssPointsService.addAp(player, 11000);
			}
			break;
		// Floor 12
		case 247238: //TODO X Y Z 
			despawnNpc(npc);
			IDInfinity_Named_12++;
			if (IDInfinity_Named_12 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 12);
				ItemService.addItem(player, 188055089, 10);
				AbyssPointsService.addGp(player, 60);
			}
			break;
		// Floor 13
		case 247265:
		case 247266:
			despawnNpc(npc);
			IDInfinity_Normal_13++;
			if (IDInfinity_Normal_13 == 5) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 13);
				player.getCommonData().addExp(50000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 14
		case 247267:
		case 247268: // TODO check Template
			despawnNpc(npc);
			IDInfinity_Normal_14++;
			if (IDInfinity_Normal_14 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 14);
				ItemService.addItem(player, ItemId.KINAH.value(), 2600000);
			}
			break;
		// Floor 15
		case 247269:
		case 247270:
			despawnNpc(npc);
			IDInfinity_Normal_15++;
			if (IDInfinity_Normal_15 == 5) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 15);
				AbyssPointsService.addAp(player, 11500);
			}
			break;
		// Floor 16
		case 247239: // TODO
			despawnNpc(npc);
			IDInfinity_Named_16++;
			if (IDInfinity_Named_16 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 16);
				ItemService.addItem(player, 190080007, 1);
				AbyssPointsService.addGp(player, 80);
			}
			break;
		// Floor 17
		case 247271:
		case 247272:
			despawnNpc(npc);
			IDInfinity_Normal_17++;
			if (IDInfinity_Normal_17 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 17);
				player.getCommonData().addExp(50000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 18 TODO KILL (Falke 4 Spawns 3)
		case 247273:
		case 247274:
			despawnNpc(npc);
			IDInfinity_Normal_18++;
			if (IDInfinity_Normal_18 == 3) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 18);
				ItemService.addItem(player, ItemId.KINAH.value(), 2800000);
			}
			break;
		// Floor 19 TODO KILL (Falke 4 Spawns 3)
		case 247275:
		case 247276:
			despawnNpc(npc);
			IDInfinity_Normal_19++;
			if (IDInfinity_Normal_19 == 3) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 19);
				AbyssPointsService.addAp(player, 12000);
			}
			break;
		// Floor 20 TODO Delete npc 247239
		case 247240:
			despawnNpc(npc);
			IDInfinity_Named_20++;
			if (IDInfinity_Named_20 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 20);
				ItemService.addItem(player, 186000454, 5);
				AbyssPointsService.addGp(player, 100);
				AbyssPointsService.addAp(player, 12000);
				player.getCommonData().addExp(50000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
				ItemService.addItem(player, ItemId.KINAH.value(), 2800000);
			}
			break;
		// Floor 21 TODO KILL (Falke 4 Spawns 3)
		case 247277:
		case 247278:
			despawnNpc(npc);
			IDInfinity_Normal_21++;
			if (IDInfinity_Normal_21 == 3) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 21);
				player.getCommonData().addExp(65000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 22
		case 247279:
		case 247280:
			despawnNpc(npc);
			IDInfinity_Normal_22++;
			if (IDInfinity_Normal_22 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 22);
				ItemService.addItem(player, ItemId.KINAH.value(), 300000);
			}
			break;
		// Floor 23
		case 247281:
		case 247282:
			despawnNpc(npc);
			IDInfinity_Normal_23++;
			if (IDInfinity_Normal_23 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 23);
				AbyssPointsService.addAp(player, 12500);
			}
			break;
		// Floor 24 todo despawn npc 247239 ?
		case 247241: //Todo aggro range ?
			despawnNpc(npc);
			IDInfinity_Named_24++;
			if (IDInfinity_Named_24 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 24);
				ItemService.addItem(player, 188055336, 1);
				AbyssPointsService.addGp(player, 120);
			}
			break;
		// Floor 25
		case 247283:
		case 247284:
			despawnNpc(npc);
			IDInfinity_Normal_25++;
			if (IDInfinity_Normal_25 == 3) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 25);
				player.getCommonData().addExp(65000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 26
		case 247285:
		case 247286:
			despawnNpc(npc);
			IDInfinity_Normal_26++;
			if (IDInfinity_Normal_26 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 26);
				ItemService.addItem(player, ItemId.KINAH.value(), 3200000);
			}
			break;
		// Floor 27
		case 247287:
			despawnNpc(npc);
			IDInfinity_Normal_27++;
			if (IDInfinity_Normal_27 == 5) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 27);
				AbyssPointsService.addAp(player, 13000);
			}
			break;
		// Floor 28
		case 247242:
			despawnNpc(npc);
			IDInfinity_Named_28++;
			if (IDInfinity_Named_28 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 28);
				ItemService.addItem(player, 188057799, 1);
				AbyssPointsService.addGp(player, 140);
			}
			break;
		// Floor 29
		case 247289:
			despawnNpc(npc);
			IDInfinity_Normal_29++;
			if (IDInfinity_Normal_29 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 29);
				player.getCommonData().addExp(65000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 30
		case 247292:
			despawnNpc(npc);
			IDInfinity_Normal_30++;
			if (IDInfinity_Normal_30 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 30);
				ItemService.addItem(player, ItemId.KINAH.value(), 3400000);
			}
			break;
		// Floor 31
		case 247293:
		case 247294:
			despawnNpc(npc);
			IDInfinity_Normal_31++;
			if (IDInfinity_Normal_31 == 2) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 31);
				AbyssPointsService.addAp(player, 13500);
			}
			break;
		// Floor 32
		case 247243:
			despawnNpc(npc);
			IDInfinity_Named_32++;
			if (IDInfinity_Named_32 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 32);
				ItemService.addItem(player, 188057790, 1);
				AbyssPointsService.addGp(player, 160);
			}
			break;
		// Floor 33
		case 247296:
			despawnNpc(npc);
			IDInfinity_Normal_33++;
			if (IDInfinity_Normal_33 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 33);
				player.getCommonData().addExp(80000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 34
		case 247297:
		case 247298:
			despawnNpc(npc);
			IDInfinity_Normal_34++;
			if (IDInfinity_Normal_34 == 5) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 34);
				ItemService.addItem(player, ItemId.KINAH.value(), 3600000);
			}
			break;
		// Floor 35
		case 247299:
		case 247300:
			despawnNpc(npc);
			IDInfinity_Normal_35++;
			if (IDInfinity_Normal_35 == 6) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 35);
				AbyssPointsService.addAp(player, 14000);
			}
			break;
		// Floor 36
		case 247244:
			despawnNpc(npc);
			IDInfinity_Named_36++;
			if (IDInfinity_Named_36 == 1) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 4);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 36);
				ItemService.addItem(player, 188055335, 1);
				AbyssPointsService.addGp(player, 180);
			}
			break;
		// Floor 37
		case 247301:
		case 247302:
			despawnNpc(npc);
			IDInfinity_Normal_37++;
			if (IDInfinity_Normal_37 == 4) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 37);
				player.getCommonData().addExp(80000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
			}
			break;
		// Floor 38
		case 247303:
		case 247304:
			despawnNpc(npc);
			IDInfinity_Normal_38++;
			if (IDInfinity_Normal_38 == 5) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 3);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 38);
				ItemService.addItem(player, ItemId.KINAH.value(), 3800000);
			}
			break;
		// Floor 39
		case 247305:
		case 247306:
			despawnNpc(npc);
			IDInfinity_Normal_39++;
			if (IDInfinity_Normal_39 == 3) {
				deleteNpc(247394);
				TowerOfChallengeService.getInstance().spawnSteps(player, 2);
				player.setFloor(player.getFloor() + 1);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 39);
				AbyssPointsService.addAp(player, 15000);
			}
			break;
		// Floor 40
		case 247245:
			despawnNpc(npc);
			IDInfinity_Named_40++;
			if (IDInfinity_Named_40 == 1) {
				deleteNpc(247394);
				sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 40);
				ItemService.addItem(player, 166100011, 175);
				ItemService.addItem(player, 188057796, 1);
				AbyssPointsService.addGp(player, 200);
				AbyssPointsService.addAp(player, 15000);
				player.getCommonData().addExp(80000000, RewardType.TOWER_OF_CHALLENGE_REWARD);
				ItemService.addItem(player, ItemId.KINAH.value(), 3800000);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
    public boolean onPassFlyingRing(Player player, String flyingRing) {
        if (flyingRing.equals("FLOOR2") || flyingRing.equals("FLOOR3") || flyingRing.equals("FLOOR4")) {
			instance.doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					if (player.isOnline()) {
						TowerOfChallengeService.getInstance().teleportTowerFloor(player, 0);
					}
				}
			});
		}
		return false;
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	protected void despawnNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			npc.getController().onDelete();
		}
	}
	protected List<Npc> getNpcs(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpcs(npcId);
		}
		return null;
	}
	
	public void onFailTower(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
    public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
    
	@Override
	public void onStopTraining(Player player) {
		onExitInstance(player);
	}
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}
	
	@Override
    public boolean onReviveEvent(Player player) {
		for (Npc npc: instance.getNpcs()) {
			npc.getController().onDelete();
		}
		spawnInggrilInggness2();
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INFINITY_INDUN_RESURRECT, 0, 0));
		onFailTower(player);
		return true;
    }
	
	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
