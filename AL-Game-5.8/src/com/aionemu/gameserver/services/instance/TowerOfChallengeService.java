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
package com.aionemu.gameserver.services.instance;

import java.util.HashMap;
import java.util.Map.Entry;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TOWER_OF_CHALLENGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author FrozenKiller
 */

public class TowerOfChallengeService {

	private final HashMap<Integer, SpawnTemplate> spawns = new HashMap<Integer, SpawnTemplate>();

	public void teleportTowerFloor(Player player, int enterFromNpc) {
		int pfloor = player.getFloor();
		if (pfloor == 0) {
			// Todo Check when enter at any other LVL then 1 / 0
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor", pfloor + 1);
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor", pfloor + 1);
			sendPacket(player, "Condition_Infinity_PRE_SEASON_Floor", pfloor);			
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", pfloor);
			player.setFloor(pfloor + 1);
			teleportTowerFloor(player, 0);
		} else if (pfloor >= 1 && pfloor <= 39) {
			switch (pfloor) {
				case 1:
				case 3:
				case 5:
				case 7:	
				case 9:
				case 11:	
				case 13:
				case 15:
				case 17:
				case 19:
				case 21:
				case 23:
				case 25:
				case 27:
				case 29:
				case 31:
				case 33:
				case 35:
				case 37:
				case 39:
					spawnWall(player, 2);
					teleportFloor(player, 1219.33264f, 249.4528f, 240.85301f, (byte) 0);
					break;
				case 2:
				case 6:
				case 10:
				case 14:
				case 18:
				case 22:
				case 26:
				case 30:
				case 34:
				case 38:
					spawnWall(player, 3);
					teleportFloor(player, 1219.33264f, 1249.4528f, 240.85301f, (byte) 0);
					break;
				case 4:
				case 8:
				case 12:
				case 16:
				case 20:
				case 24:
				case 28:
				case 32:
				case 36:
					spawnWall(player, 4);
					teleportFloor(player, 219.33264f, 1249.4528f, 240.85301f, (byte) 0);
					break;
			}
		} 
		else if (pfloor == 40) {
			teleportFloor(player, 210.42656f, 249.58434f, 971.3951f, (byte) 0);
		}

		spawnNextFloor(player, pfloor);

		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//deleteNpc(701773);
			}
		}, 2500);
		
		if (enterFromNpc == 1) {
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor", pfloor);
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor", pfloor);
			sendPacket(player, "Condition_Infinity_PRE_SEASON_Floor", pfloor - 1);			
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", pfloor - 1);
		} else {
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor", pfloor);
		}
	}
	
	private void spawnNextFloor(Player player, int next) {
		switch (next) {
			case 1:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247247, 1241.2692f, 249.52225f, 240.637f, (byte) 60)); //IDInfinity_Normal_01_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247248, 1246.9672f, 254.06393f, 240.637f, (byte) 60)); //IDInfinity_Normal_01_02
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247248, 1246.7649f, 244.09093f, 240.63698f, (byte) 60)); //IDInfinity_Normal_01_02
				spawnFloor(player);
				break;
			case 2:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247249, 1241.3691f, 1244.5542f, 241.02441f, (byte) 60)); //IDInfinity_Normal_02_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247249, 1241.3145f, 1254.4287f, 241.02438f, (byte) 60)); //IDInfinity_Normal_02_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247250, 1256.5892f, 1246.8022f, 241.08029f, (byte) 60)); //IDInfinity_Normal_02_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247250, 1256.9415f, 1251.6005f, 241.08029f, (byte) 60)); //IDInfinity_Normal_02_02
				spawnFloor(player);
				break;
			case 3:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247251, 1245.6285f, 255.52124f, 240.637f, (byte) 106)); //IDInfinity_Normal_03_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247251, 1238.7719f, 242.37012f, 240.637f, (byte) 54)); //IDInfinity_Normal_03_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247251, 1241.1976f, 249.35072f, 240.637f, (byte) 60)); //IDInfinity_Normal_03_01
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247252, 1251.5302f, 249.42406f, 240.637f, (byte) 60)); //IDInfinity_Normal_03_02
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247252, 1252.3186f, 238.15843f, 240.637f, (byte) 60)); //IDInfinity_Normal_03_02
				spawns.put(6, SpawnEngine.addNewSingleTimeSpawn(302400000, 247252, 1252.5344f, 260.83862f, 240.637f, (byte) 60)); //IDInfinity_Normal_03_02
				spawnFloor(player);
				break;
			case 4: //Silikor of Memory
				SpawnTemplate spawn1 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247236, 241.05719f, 1249.5562f, 240.63419f, (byte) 60);
				SpawnEngine.spawnObject(spawn1, player.getPosition().getInstanceId());
				break;
			case 5:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247351, 1252.388f, 265.35858f, 240.71442f, (byte) 60)); //Incredibly Tasty Lump of Meat (1)
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247351, 1251.2031f, 235.39067f, 240.71443f, (byte) 60)); //Incredibly Tasty Lump of Meat (1)
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247253, 1246.535f, 236.35332f, 240.63701f, (byte) 60)); //IDInfinity_Normal_05_01
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247253, 1249.6444f, 260.4562f, 240.637f, (byte) 60)); //IDInfinity_Normal_05_01
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247253, 1246.9946f, 266.1868f, 240.71442f, (byte) 60)); //IDInfinity_Normal_05_01
				spawns.put(6, SpawnEngine.addNewSingleTimeSpawn(302400000, 247253, 1248.757f, 231.82843f, 240.7144f, (byte) 60)); //IDInfinity_Normal_05_01
				spawns.put(7, SpawnEngine.addNewSingleTimeSpawn(302400000, 247254, 1255.6487f, 238.7923f, 240.71443f, (byte) 60)); //IDInfinity_Normal_05_02
				spawns.put(8, SpawnEngine.addNewSingleTimeSpawn(302400000, 247254, 1254.4502f, 260.83884f, 240.71443f, (byte) 60)); //IDInfinity_Normal_05_02
				spawns.put(9, SpawnEngine.addNewSingleTimeSpawn(302400000, 247254, 1251.25f, 240.84523f, 240.63698f, (byte) 60)); //IDInfinity_Normal_05_02
				spawnFloor(player);
				break;
			case 6:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247352, 1243.6519f, 1267.2726f, 241.08026f, (byte) 60)); //Incredibly Tasty Lump of Meat (2)
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247352, 1241.7797f, 1230.8771f, 241.00584f, (byte) 60)); //Incredibly Tasty Lump of Meat (2)
                spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247255, 1237.1116f, 1231.8397f, 241.08028f, (byte) 60)); //IDInfinity_Normal_06_01
                spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247255, 1239.1317f, 1268.3289f, 240.90775f, (byte) 60)); //IDInfinity_Normal_06_01
                spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247255, 1241.7402f, 1233.985f, 241.08029f, (byte) 60)); //IDInfinity_Normal_06_01
                spawns.put(6, SpawnEngine.addNewSingleTimeSpawn(302400000, 247255, 1242.7787f, 1262.9916f, 241.08026f, (byte) 60)); //IDInfinity_Normal_06_01
                spawns.put(7, SpawnEngine.addNewSingleTimeSpawn(302400000, 247256, 1238.0654f, 1228.2698f, 240.89232f, (byte) 60)); //IDInfinity_Normal_06_02
                spawns.put(8, SpawnEngine.addNewSingleTimeSpawn(302400000, 247256, 1244.4294f, 1270.1027f, 240.8923f, (byte) 60)); //IDInfinity_Normal_06_02
                spawns.put(9, SpawnEngine.addNewSingleTimeSpawn(302400000, 247256, 1245.4271f, 1229.1735f, 240.8923f, (byte) 60)); //IDInfinity_Normal_06_02
                spawns.put(10, SpawnEngine.addNewSingleTimeSpawn(302400000, 247256, 1242.4255f, 1228.5845f, 240.89235f, (byte) 60)); //IDInfinity_Normal_06_02
                spawns.put(11, SpawnEngine.addNewSingleTimeSpawn(302400000, 247256, 1247.3359f, 1264.5875f, 241.08034f, (byte) 60)); //IDInfinity_Normal_06_02
				spawnFloor(player);
				break;
			case 7: // TODO
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247354, 1239.11308f, 266.2734f, 240.71161f, (byte) 0)); //Incredibly Tasty Lump of Meat (3)
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247354, 1255.88971f, 259.3857f, 240.7116f, (byte) 0)); //Incredibly Tasty Lump of Meat (3)
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247354, 1252.90434f, 233.1603f, 240.71167f, (byte) 0)); //Incredibly Tasty Lump of Meat (3)
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247257, 1241.0854f, 229.55382f, 240.71445f, (byte) 60)); //IDInfinity_Normal_07_01
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247257, 1241.8986f, 232.38576f, 240.70241f, (byte) 60)); //IDInfinity_Normal_07_01
				spawns.put(6, SpawnEngine.addNewSingleTimeSpawn(302400000, 247257, 1256.554f, 255.30605f, 240.63698f, (byte) 60)); //IDInfinity_Normal_07_01
				spawns.put(7, SpawnEngine.addNewSingleTimeSpawn(302400000, 247257, 1255.6273f, 250.58907f, 240.637f, (byte) 60)); //IDInfinity_Normal_07_01
				spawns.put(8, SpawnEngine.addNewSingleTimeSpawn(302400000, 247258, 1246.7533f, 230.85693f, 240.7144f, (byte) 60)); //IDInfinity_Normal_07_02
				spawns.put(9, SpawnEngine.addNewSingleTimeSpawn(302400000, 247258, 1259.9473f, 250.42622f, 240.71443f, (byte) 60)); //IDInfinity_Normal_07_02
				spawns.put(10, SpawnEngine.addNewSingleTimeSpawn(302400000, 247258, 1263.2615f, 251.22383f, 240.85583f, (byte) 60)); //IDInfinity_Normal_07_02
				spawns.put(11, SpawnEngine.addNewSingleTimeSpawn(302400000, 247258, 1249.5948f, 230.61604f, 240.84071f, (byte) 60)); //IDInfinity_Normal_07_02
				spawns.put(12, SpawnEngine.addNewSingleTimeSpawn(302400000, 247258, 1242.2306f, 235.38986f, 240.63701f, (byte) 60)); //IDInfinity_Normal_07_02
				spawnFloor(player);
				break;
			case 8: //Tog Leader
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247237, 241.35645f, 1249.6826f, 240.6342f, (byte) 60)); //IDInfinity_Named_05_02
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247353, 252.90434f, 1233.1603f, 240.71167f, (byte) 60)); //IDInfinity_Berrel
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247353, 239.11308f, 1266.2734f, 240.71161f, (byte) 60)); //IDInfinity_Berrel
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247353, 255.88971f, 1259.3857f, 240.7116f, (byte) 60)); //IDInfinity_Berrel
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247401, 245.34207f, 1244.6718f, 240.63422f, (byte) 60)); //IDInfinity_Summon_Tog
				spawns.put(6, SpawnEngine.addNewSingleTimeSpawn(302400000, 247401, 240.31384f, 1243.3389f, 240.6342f, (byte) 60)); //IDInfinity_Summon_Tog
				spawns.put(7, SpawnEngine.addNewSingleTimeSpawn(302400000, 247401, 240.57507f, 1254.5868f, 240.6342f, (byte) 60)); //IDInfinity_Summon_Tog
				spawns.put(8, SpawnEngine.addNewSingleTimeSpawn(302400000, 247401, 244.92566f, 1253.663f, 240.63419f, (byte) 60)); //IDInfinity_Summon_Tog
				spawns.put(9, SpawnEngine.addNewSingleTimeSpawn(302400000, 247401, 248.17233f, 1250.285f, 240.63419f, (byte) 60)); //IDInfinity_Summon_Tog
				spawns.put(10, SpawnEngine.addNewSingleTimeSpawn(302400000, 247401, 244.30582f, 1248.8861f, 240.63419f, (byte) 60)); //IDInfinity_Summon_Tog
				spawnFloor(player);
				break;
			case 9:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247259, 1240.7164f, 248.11723f, 240.637f, (byte) 60)); //IDInfinity_Normal_09_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247259, 1241.2675f, 251.26204f, 240.637f, (byte) 60)); //IDInfinity_Normal_09_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247260, 1257.0488f, 252.81845f, 240.63698f, (byte) 35)); //IDInfinity_Normal_09_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247260, 1256.417f, 243.78656f, 240.637f, (byte) 84)); //IDInfinity_Normal_09_02
				spawnFloor(player);
				break;
			case 10:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247261, 1261.6514f, 1246.107f, 240.8923f, (byte) 60)); //IDInfinity_Normal__10_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247261, 1261.4808f, 1252.1948f, 240.89235f, (byte) 60)); //IDInfinity_Normal__10_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247262, 1253.0315f, 1239.0323f, 241.08029f, (byte) 14)); //IDInfinity_Normal_10_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247262, 1241.3953f, 1249.3434f, 241.02437f, (byte) 60)); //IDInfinity_Normal_10_02
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247262, 1251.6865f, 1261.4036f, 241.08029f, (byte) 108)); //IDInfinity_Normal_10_02
				spawnFloor(player);
				break;
			case 11: //TODO
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247263, 1242.3531f, 233.14145f, 240.63701f, (byte) 0)); //IDInfinity_Normal_11_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247263, 1244.5714f, 265.27576f, 240.63702f, (byte) 60)); //IDInfinity_Normal_11_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247264, 1238.4176f, 242.4964f, 240.637f, (byte) 109)); //IDInfinity_Normal_11_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247264, 1242.0688f, 246.70786f, 240.63701f, (byte) 60)); //IDInfinity_Normal_11_02
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247264, 1241.9987f, 251.95267f, 240.63702f, (byte) 60)); //IDInfinity_Normal_11_02
				spawns.put(6, SpawnEngine.addNewSingleTimeSpawn(302400000, 247264, 1241.7067f, 256.83353f, 240.637f, (byte) 58)); //IDInfinity_Normal_11_02
				spawnFloor(player);
				break;
			case 12: //Marabata
				SpawnTemplate spawn2 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247238, 229.59697f, 1248.6145f, 240.63419f, (byte) 0);
				SpawnEngine.spawnObject(spawn2, player.getPosition().getInstanceId());
				break;
			case 13:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247265, 1240.9697f, 249.44327f, 240.63698f, (byte) 60)); //IDInfinity_Normal_13_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247265, 1259.8796f, 245.47765f, 240.71445f, (byte) 60)); //IDInfinity_Normal_13_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247265, 1259.7805f, 252.79193f, 240.71445f, (byte) 60)); //IDInfinity_Normal_13_01
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247266, 1241.0222f, 240.99756f, 240.63701f, (byte) 60)); //IDInfinity_Normal_13_02
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247266, 1241.067f, 257.68057f, 240.637f, (byte) 60)); //IDInfinity_Normal_13_02
				spawnFloor(player);
				break;
			case 14:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247267, 1241.0045f, 1241.095f, 241.08028f, (byte) 60)); //IDInfinity_Normal_14_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247267, 1241.0754f, 1257.7725f, 241.0803f, (byte) 60)); //IDInfinity_Normal_14_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247268, 1256.5597f, 1245.7635f, 241.08029f, (byte) 60)); //IDInfinity_Normal_14_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247268, 1261.2161f, 1249.2194f, 240.89232f, (byte) 60)); //IDInfinity_Normal_14_02
				spawnFloor(player);
				break;
			case 15:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247269, 1241.453f, 249.3844f, 240.637f, (byte) 60)); //IDInfinity_Normal_15_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247270, 1241.7888f, 242.21098f, 240.63701f, (byte) 60)); //IDInfinity_Normal_15_02
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247270, 1257.3326f, 251.68457f, 240.637f, (byte) 60)); //IDInfinity_Normal_15_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247270, 1257.2244f, 246.67181f, 240.637f, (byte) 60)); //IDInfinity_Normal_15_02
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247270, 1241.6874f, 256.9002f, 240.637f, (byte) 60)); //IDInfinity_Normal_15_02
				spawnFloor(player);
				break;
			case 16: //Exploding Machine Beetle
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247239, 246.17299f, 1244.3868f, 240.63422f, (byte) 60));
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247239, 246.26004f, 1254.6833f, 240.6342f, (byte) 60));
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247239, 241.15605f, 1249.5082f, 240.63419f, (byte) 60));
				spawnFloor(player);
				break;
			case 17:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247271, 1241.0356f, 250.98846f, 240.637f, (byte) 60)); //IDInfinity_Normal_17_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247271, 1241.0374f, 247.39714f, 240.63701f, (byte) 60)); //IDInfinity_Normal_17_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247272, 1242.6578f, 233.23705f, 240.63701f, (byte) 61)); //IDInfinity_Normal_17_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247272, 1246.1481f, 265.00378f, 240.63702f, (byte) 50)); //IDInfinity_Normal_17_02
				spawnFloor(player);
				break;
			case 18:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247273, 1256.9932f, 1246.4897f, 241.08029f, (byte) 60)); //IDInfinity_Normal_18_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247273, 1257.1473f, 1252.755f, 241.08025f, (byte) 60)); //IDInfinity_Normal_18_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247274, 1248.3267f, 1248.777f, 241.07922f, (byte) 90)); //IDInfinity_Normal_18_02
				spawnFloor(player);
				break;
			case 19:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247275, 1254.4971f, 235.24953f, 240.71443f, (byte) 0)); //IDInfinity_Normal_19_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247275, 1255.5437f, 238.22548f, 240.71443f, (byte) 0)); //IDInfinity_Normal_19_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247276, 1243.769f, 233.1296f, 240.63701f, (byte) 5)); //IDInfinity_Normal_19_02
				spawnFloor(player);
				break;
			case 20: //Kadena
				SpawnTemplate spawn3 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247240, 241.10446f, 1249.5867f, 240.63419f, (byte) 60);
				SpawnEngine.spawnObject(spawn3, player.getPosition().getInstanceId());
				break;
			case 21:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247277, 1241.5881f, 249.19284f, 240.63698f, (byte) 60)); //IDInfinity_Normal_21_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247278, 1246.5281f, 254.57443f, 240.637f, (byte) 60)); //IDInfinity_Normal_21_02
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247278, 1246.6598f, 244.26442f, 240.63698f, (byte) 60)); //IDInfinity_Normal_21_02
				spawnFloor(player);
				break;
			case 22:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247279, 1245.9485f, 1234.1766f, 241.08025f, (byte) 9)); //IDInfinity_Normal_22_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247279, 1243.4496f, 1265.5426f, 241.08026f, (byte) 119)); //IDInfinity_Normal_22_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247280, 1260.2617f, 1244.6887f, 240.8923f, (byte) 60)); //IDInfinity_Normal_22_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247280, 1260.1921f, 1254.2461f, 240.8923f, (byte) 60)); //IDInfinity_Normal_22_02
				spawnFloor(player);
				break;
			case 23:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247281, 1241.2513f, 249.4836f, 240.637f, (byte) 60)); //IDInfinity_Normal_23_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247282, 1233.8463f, 248.4259f, 240.637f, (byte) 34)); //IDInfinity_Normal_23_02
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247282, 1248.6375f, 250.13062f, 240.637f, (byte) 90)); //IDInfinity_Normal_23_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247282, 1260.9657f, 249.19987f, 240.71443f, (byte) 60)); //IDInfinity_Normal_23_02
				spawnFloor(player);
				break;
			case 24: //Keltra
				SpawnTemplate spawn4 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247241, 241.20648f, 1249.4929f, 240.63419f, (byte) 60);
				SpawnEngine.spawnObject(spawn4, player.getPosition().getInstanceId());
				break;
			case 25:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247283, 1254.3684f, 254.59427f, 240.63698f, (byte) 60)); //IDInfinity_Normal_25_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247283, 1254.5072f, 244.11295f, 240.637f, (byte) 60)); //IDInfinity_Normal_25_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247284, 1260.5731f, 249.24434f, 240.71443f, (byte) 60)); //IDInfinity_Normal_25_02
				spawnFloor(player);
				break;
			case 26:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247285, 1244.7651f, 1233.7242f, 241.08025f, (byte) 64)); //IDInfinity_Normal_26_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247285, 1252.2819f, 1260.7755f, 241.08022f, (byte) 43)); //IDInfinity_Normal_26_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247285, 1249.3314f, 1249.5176f, 241.08029f, (byte) 60)); //IDInfinity_Normal_26_01
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247286, 1260.869f, 1249.5547f, 240.89235f, (byte) 60)); //IDInfinity_Normal_26_02
				spawnFloor(player);
				break;
			case 27:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247287, 1244.4629f, 247.09074f, 240.63698f, (byte) 60)); //IDInfinity_Normal_27_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247287, 1240.3837f, 253.84094f, 240.637f, (byte) 60)); //IDInfinity_Normal_27_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247287, 1240.275f, 249.53305f, 240.637f, (byte) 60)); //IDInfinity_Normal_27_01
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247287, 1240.3291f, 244.9343f, 240.637f, (byte) 60)); //IDInfinity_Normal_27_01
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247287, 1244.4069f, 251.21172f, 240.637f, (byte) 60)); //IDInfinity_Normal_27_01
				spawnFloor(player);
				break;
			case 28: //Bollvig Black Heart
				SpawnTemplate spawn5 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247242, 241.14766f, 1249.5754f, 240.6342f, (byte) 60);
				SpawnEngine.spawnObject(spawn5, player.getPosition().getInstanceId());
				break;
			case 29:
				SpawnTemplate spawn6 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247289, 1240.9298f, 249.40704f, 240.63698f, (byte) 60); //IDInfinity_Normal_29_01
				SpawnEngine.spawnObject(spawn6, player.getPosition().getInstanceId());
				break;
			case 30:
				SpawnTemplate spawn7 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247292, 1241.0773f, 1249.4753f, 241.02435f, (byte) 60); //IDInfinity_Normal_30_01
				SpawnEngine.spawnObject(spawn7, player.getPosition().getInstanceId());
				break;
			case 31:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247293, 1252.2051f, 254.27914f, 240.63698f, (byte) 60)); //IDInfinity_Normal_31_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247294, 1252.3057f, 245.20596f, 240.637f, (byte) 60)); //IDInfinity_Normal_31_02
				spawnFloor(player);
				break;
			case 32: //Brigade General Vasharti
				SpawnTemplate spawn8 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247243, 241.03787f, 1249.4756f, 240.6342f, (byte) 60);
				SpawnEngine.spawnObject(spawn8, player.getPosition().getInstanceId());
				break;
			case 33:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247296, 1258.4886f, 242.86151f, 240.71445f, (byte) 60)); //IDInfinity_Normal_33_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247296, 1258.7715f, 255.54105f, 240.71442f, (byte) 60)); //IDInfinity_Normal_33_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247296, 1254.7808f, 252.40225f, 240.63698f, (byte) 60)); //IDInfinity_Normal_33_01
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247296, 1254.8104f, 245.96501f, 240.637f, (byte) 60)); //IDInfinity_Normal_33_01
				spawnFloor(player);
				break;
			case 34:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247297, 1243.7131f, 1242.5458f, 241.08018f, (byte) 64)); //IDInfinity_Normal_34_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247297, 1237.6771f, 1256.2189f, 241.0803f, (byte) 7)); //IDInfinity_Normal_34_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247298, 1248.3123f, 1242.8148f, 241.08029f, (byte) 60)); //IDInfinity_Normal_34_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247298, 1241.6437f, 1249.5491f, 241.0244f, (byte) 60)); //IDInfinity_Normal_34_02
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247298, 1248.5295f, 1256.5908f, 241.08018f, (byte) 60)); //IDInfinity_Normal_34_02
				spawnFloor(player);
				break;
			case 35:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247299, 1241.7411f, 249.50934f, 240.637f, (byte) 60)); //IDInfinity_Normal_35_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247299, 1244.6454f, 245.96233f, 240.63698f, (byte) 60)); //IDInfinity_Normal_35_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247299, 1244.3328f, 253.34087f, 240.637f, (byte) 60)); //IDInfinity_Normal_35_01
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247300, 1252.2407f, 237.7893f, 240.637f, (byte) 75)); //IDInfinity_Normal_35_02
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247300, 1262.272f, 249.75966f, 240.85583f, (byte) 60)); //IDInfinity_Normal_35_02
				spawns.put(6, SpawnEngine.addNewSingleTimeSpawn(302400000, 247300, 1253.3423f, 260.23605f, 240.637f, (byte) 108)); //IDInfinity_Normal_35_02
				spawnFloor(player);
				break;
			case 36: //Vanktrist
				SpawnTemplate spawn9 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247244, 241.15395f, 1249.4897f, 240.63419f, (byte) 60);
				SpawnEngine.spawnObject(spawn9, player.getPosition().getInstanceId());
				break;
			case 37:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247301, 1247.3936f, 244.1596f, 240.63698f, (byte) 60)); //IDInfinity_Normal_37_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247301, 1247.2323f, 254.65862f, 240.637f, (byte) 60)); //IDInfinity_Normal_37_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247302, 1241.087f, 250.7317f, 240.637f, (byte) 60)); //IDInfinity_Normal_37_02
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247302, 1241.0918f, 248.23453f, 240.63701f, (byte) 60)); //IDInfinity_Normal_37_02
				spawnFloor(player);
				break;
			case 38:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247303, 1248.1031f, 1242.6842f, 241.08029f, (byte) 60)); //IDInfinity_Normal_38_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247303, 1241.1295f, 1249.5371f, 241.02437f, (byte) 60)); //IDInfinity_Normal_38_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247303, 1248.4532f, 1256.7592f, 241.08018f, (byte) 60)); //IDInfinity_Normal_38_01
				spawns.put(4, SpawnEngine.addNewSingleTimeSpawn(302400000, 247304, 1243.905f, 1242.573f, 241.08018f, (byte) 64)); //IDInfinity_Normal_38_02
				spawns.put(5, SpawnEngine.addNewSingleTimeSpawn(302400000, 247304, 1233.7372f, 1248.0378f, 241.08028f, (byte) 34)); //IDInfinity_Normal_38_02
				spawnFloor(player);
				break;
			case 39:
				spawns.put(1, SpawnEngine.addNewSingleTimeSpawn(302400000, 247305, 1245.1248f, 253.14214f, 240.637f, (byte) 60)); //IDInfinity_Normal_39_01
				spawns.put(2, SpawnEngine.addNewSingleTimeSpawn(302400000, 247305, 1245.084f, 245.73096f, 240.63698f, (byte) 60)); //IDInfinity_Normal_39_01
				spawns.put(3, SpawnEngine.addNewSingleTimeSpawn(302400000, 247306, 1241.2275f, 249.48973f, 240.637f, (byte) 60)); //IDInfinity_Normal_39_02
				spawnFloor(player);
				break;
			case 40: //Grendal the Witch TODO
				SpawnTemplate spawn10 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247245, 241.21867f, 249.41464f, 971.14136f, (byte) 60);
				SpawnEngine.spawnObject(spawn10, player.getPosition().getInstanceId());
				break;
			default:
				break;
		}
	}
	
	private void spawnFloor(Player player) {
		for (Entry<Integer, SpawnTemplate> floor : spawns.entrySet()) {
			SpawnTemplate spawn = floor.getValue();
			SpawnEngine.spawnObject(spawn, player.getPosition().getInstanceId());
		}
		spawns.clear();
	}
	
	// Level 1 staticId 284 (x = 2xx & y = 2xx)
	// Level 2 staticId's 107 (x = 12xx & y = 2xx)
	// Level 3 staticId's 108 (x = 12xx & y = 12xx)
	// Level 4 staticId's 56 (x = 2xx & y = 12xx)
	public void spawnWall(Player player, int level ) { // Static's 56 107 108 284 
		switch (level) {
		case 1:
			SpawnTemplate spawnwall1 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247394, 264.6257f, 248.647f, 242.10599f, (byte) 0); // Fense or Wall
			spawnwall1.setStaticId(284);
			SpawnEngine.spawnObject(spawnwall1, player.getPosition().getInstanceId());
			break;
		case 2:
			SpawnTemplate spawnwall2 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247394, 1264.6257f, 248.647f, 242.10599f, (byte) 0); // Fense or Wall
			spawnwall2.setStaticId(107);
			SpawnEngine.spawnObject(spawnwall2, player.getPosition().getInstanceId());
			break;
		case 3:
			SpawnTemplate spawnwall3 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247394, 1264.6257f, 1248.647f, 242.10599f, (byte) 0); // Fense or Wall
			spawnwall3.setStaticId(108);
			SpawnEngine.spawnObject(spawnwall3, player.getPosition().getInstanceId());
			break;
		case 4:
			SpawnTemplate spawnwall4 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247394, 264.6257f, 1248.647f, 242.10599f, (byte) 0); // Fense or Wall
			spawnwall4.setStaticId(56);
			SpawnEngine.spawnObject(spawnwall4, player.getPosition().getInstanceId());
			break;
		}
	}
	
	// Level 1 staticId's 57, 60, 115 (x = 2xx & y = 2xx)
	// Level 2 staticId's 66, 67, 119 (x = 12xx & y = 2xx)
	// Level 3 staticId's 89, 90, 120 (x = 12xx & y = 12xx)
	// Level 4 staticId's 58, 59, 114 (x = 2xx & y = 12xx)
	
	public void spawnSteps(Player player, int level) { 
		if (level == 1) {
			SpawnTemplate spawnFlame = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 279f, 255f, 242f, (byte) 0); 
			spawnFlame.setStaticId(57);//Left Blue Flames (before Stair)
			SpawnTemplate spawnFlame1 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 279f, 253f, 242f, (byte) 0);
			spawnFlame1.setStaticId(60);//Right Blue Flames (before Stair)
			SpawnTemplate spawnsteps = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 279f, 253f, 242f, (byte) 0);
			spawnsteps.setStaticId(115); // Stair
			SpawnEngine.spawnObject(spawnFlame, player.getInstanceId());
			SpawnEngine.spawnObject(spawnFlame1, player.getInstanceId());
			SpawnEngine.spawnObject(spawnsteps, player.getInstanceId());
		} else if (level == 2) {
			SpawnTemplate spawnFlame = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 1279f, 255f, 242f, (byte) 0); 
			spawnFlame.setStaticId(66);//Left Blue Flames (before Stair)
			SpawnTemplate spawnFlame1 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 1279f, 253f, 242f, (byte) 0);
			spawnFlame1.setStaticId(67);//Right Blue Flames (before Stair)
			SpawnTemplate spawnsteps = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 1279f, 253f, 242f, (byte) 0);
			spawnsteps.setStaticId(119); // Stair
			SpawnEngine.spawnObject(spawnFlame, player.getInstanceId());
			SpawnEngine.spawnObject(spawnFlame1, player.getInstanceId());
			SpawnEngine.spawnObject(spawnsteps, player.getInstanceId());
		} else if (level == 3){
			SpawnTemplate spawnFlame = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 1279f, 1255f, 242f, (byte) 0); 
			spawnFlame.setStaticId(89);//Left Blue Flames (before Stair)
			SpawnTemplate spawnFlame1 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 1279f, 1253f, 242f, (byte) 0);
			spawnFlame1.setStaticId(90);//Right Blue Flames (before Stair)
			SpawnTemplate spawnsteps = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 1282f, 1253f, 242f, (byte) 0);
			spawnsteps.setStaticId(120); // Stair
			SpawnEngine.spawnObject(spawnFlame, player.getInstanceId());
			SpawnEngine.spawnObject(spawnFlame1, player.getInstanceId());
			SpawnEngine.spawnObject(spawnsteps, player.getInstanceId());
		} else {
			SpawnTemplate spawnFlame = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 279f, 1255f, 242f, (byte) 0); 
			spawnFlame.setStaticId(58);//Left Blue Flames (before Stair)
			SpawnTemplate spawnFlame1 = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 279f, 1253f, 242f, (byte) 0);
			spawnFlame1.setStaticId(59);//Right Blue Flames (before Stair)
			SpawnTemplate spawnsteps = SpawnEngine.addNewSingleTimeSpawn(302400000, 247395, 282f, 1253f, 242f, (byte) 0);
			spawnsteps.setStaticId(114); // Stair
			SpawnEngine.spawnObject(spawnFlame, player.getInstanceId());
			SpawnEngine.spawnObject(spawnFlame1, player.getInstanceId());
			SpawnEngine.spawnObject(spawnsteps, player.getInstanceId());
		}
	}
	
	private void sendPacket(Player player, final String variable, final int floor) {
		PacketSendUtility.sendPacket(player, new SM_TOWER_OF_CHALLENGE(player, variable, floor));
	}
	
	private void teleportFloor(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, player.getPosition().getMapId(), player.getPosition().getInstanceId(), x, y, z, h);
	}
	
//	private void deleteNpc(int npcId) {
//		if (getNpc(npcId) != null) {
//			getNpc(npcId).getController().onDelete();
//		}
//	}
	
	public static TowerOfChallengeService getInstance() {
		return TowerOfChallengeService.SingletonHolder.TowerOfChallengeService;
	}

	private static class SingletonHolder {
		protected static final TowerOfChallengeService TowerOfChallengeService = new TowerOfChallengeService();
	}
}