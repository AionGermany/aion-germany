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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.StaticDoorService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author Kill3r
 */
@InstanceID(300610000)
public class RaksangRuinsInstance extends GeneralInstanceHandler {

	/**
	 * 700141 asmo gate need to be opened when last boss is dead 730445 elyos gate at last Level 1 = Right Side part Level 2 = Left Side Part Level 3(Hell) = Bottom part - only visible when going
	 * there. case 236013: // Withering Husk (Waves) case 236011: // Trained Worg (Waves) case 236010: // Trained Porgus (Waves)
	 */

	protected boolean isInstanceDestroyed = false;
	private boolean wave1_done = false; // of Level2
	private boolean wave2_done = false; // of level2
	private boolean wave3_done = false; // of level2
	// private boolean wave4_done = false; //of level2 TODO
	private int stageNum = 0;
	private int porgusCnt;
	private int worgCnt;
	private int huskCnt;
	private boolean level2_wavesCalled = false;
	private Map<Integer, StaticDoor> doors;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		doors = instance.getDoors();
		// Elyos Npc's
		// 206378 lvl1
		// 206379 lvl2
		// 206380 lvl3
		// Asmo Npc's
		// 206395 lvl1
		// 206396 lvl2
		// 206397 lvl3
		stageNum = Rnd.get(1, 3);
		super.onInstanceCreate(instance);
		spawnRings();
	}

	public boolean isInstanceDestroyed() {
		return isInstanceDestroyed;
	}

	@Override
	public void onLeaveInstance(Player player) {
		player.getInventory().decreaseByItemId(164000342, 20);
		super.onLeaveInstance(player);
	}

	@Override
	public void onDie(Npc npc) {
		Player p = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getNpcId()) {
			case 236016:
			case 236078:
			case 236079:
				deleteNpc(npc.getNpcId(), npc.getObjectId());
				if ((getNpcs(236016).isEmpty() && getNpcs(236078).isEmpty() && getNpcs(236079).isEmpty())) {
					if (checkTombStones(1)) {
						openDoor(p, 457, true);
					}
				}
				break;
			case 236304: // Drill Instructor Pratica
				openDoor(p, 118, true);
				break;
			// End of Level 1
			case 236013: // Withering Husk (Waves)
				if (npc.getNpcId() == 236013) {
					huskCnt++;
				}
			case 236011: // Trained Worg (Waves)
				if (npc.getNpcId() == 236011) {
					worgCnt++;
				}
			case 236010: // Trained Porgus (Waves)
				if (npc.getNpcId() == 236010) {
					porgusCnt++;
				}
				if (porgusCnt == 5 && worgCnt == 0) { // Wave 2 (wave1 handled on passFlyRing)
					if (!wave1_done) {
						wave1_done = true;
						PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1402832)); // Prepare for Combat! More enemies swarming in!
						callWaves2();
					}
				}
				if (porgusCnt == 8 && worgCnt == 2) { // wave 3
					if (wave1_done && !wave2_done) {
						wave2_done = true;
						callWaves3();
					}
				}
				if (worgCnt == 9 && huskCnt == 1) { // wave 4
					if (wave1_done && wave2_done && !wave3_done) {
						wave3_done = true;
						PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1402834)); // Only a few enemies left!
						callWaves4();
					}
				}
				break;
			case 236084: // Classified Drill Camp Instructor
				openDoor(p, 307, true);
				break;
			case 236303: // Drill Instructor Diplito
				openDoor(p, 294, true);
				break;
			case 236306: // Reviver Nasto (Boss-Middle) (Level 1 Boss || Level 2 Boss)
				switch (p.getRace()) {
					case ASMODIANS:
						spawn(730445, 621.3292f, 684.80444f, 522.0487f, (byte) 85); // Rift (out)
						break;
					case ELYOS:
						spawn(730445, 621.3292f, 684.80444f, 522.0487f, (byte) 85); // Rift (out)
						break;
					default:
						break;
				}
				break;
		}
	}

	@Override
	public void onEnterInstance(Player player) {
		// Elyos Npc's
		// 206378 lvl1
		// 206379 lvl2
		// 206380 lvl3
		// Asmo Npc's
		// 206395 lvl1
		// 206396 lvl2
		// 206397 lvl3
		switch (player.getRace()) {
			case ASMODIANS:
				switch (stageNum) {
					case 1:
						spawn(206395, 818.103f, 931.0215f, 1207.4312f, (byte) 13);
						break;
					case 2:
						spawn(206396, 818.103f, 931.0215f, 1207.4312f, (byte) 13);
						break;
					case 3:
						spawn(206397, 818.103f, 931.0215f, 1207.4312f, (byte) 13);
						break;
				}
				break;
			case ELYOS:
				switch (stageNum) {
					case 1:
						spawn(206378, 818.103f, 931.0215f, 1207.4312f, (byte) 13);
						break;
					case 2:
						spawn(206379, 818.103f, 931.0215f, 1207.4312f, (byte) 13);
						break;
					case 3:
						spawn(206380, 818.103f, 931.0215f, 1207.4312f, (byte) 13);
						break;
				}
				break;
			default:
				break;
		}
	}

	public void spawnRings() { // Ring for wave at level2
		FlyRing waveStarter = new FlyRing(new FlyRingTemplate("waves_level2", mapId, new Point3D(566.06354f, 245.26855f, 928.0467f), new Point3D(568.7914f, 246.79665f, 928.0467f), new Point3D(564.0763, 242.45244f, 928.0466f), 5), instanceId);

		waveStarter.spawn();
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		if (flyingRing.equals("waves_level2") && !level2_wavesCalled) {
			level2_wavesCalled = true;
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402780)); // Prepare for Combat! Enemies approaching!
			callWaves1();
		}
		return false;
	}

	public void callWaves4() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236011, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Worg
				spawn(236011, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Worg
			}
		}, 800); // 1sec(about)
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236013, 623.9586f, 195.92781f, 924.93005f, (byte) 39); // Withering Husk
			}
		}, 4000); // 4sec
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236011, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Worg
				spawn(236011, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Worg
			}
		}, 7000); // 7sec
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236075, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Crumbling Skelesword
				spawn(236075, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Crumbling Skelesword
			}
		}, 11000); // 11sec
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236014, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Ragelich Adept
			}
		}, 16000); // 16sec
	}

	public void callWaves3() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236011, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Worg
				spawn(236011, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Worg
			}
		}, 800); // 1sec(about)
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236011, 619.6694f, 200.17451f, 924.5908f, (byte) 44); // Trained Worg
				spawn(236011, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Worg
				spawn(236011, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Worg
			}
		}, 6000); // 6sec
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236013, 623.9586f, 195.92781f, 924.93005f, (byte) 39); // Withering Husk
				spawn(236011, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Worg
				spawn(236011, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Worg
			}
		}, 16000); // 16sec
	}

	public void callWaves2() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236010, 619.6694f, 200.17451f, 924.5908f, (byte) 44); // Trained Porgus
			}
		}, 1000); // 1sec
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236010, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Porgus
				spawn(236010, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Porgus
			}
		}, 5000); // 5sec
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236011, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Worg
				spawn(236011, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Worg
			}
		}, 10000); // 10sec
	}

	public void callWaves1() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236010, 619.6694f, 200.17451f, 924.5908f, (byte) 44); // Trained Porgus
			}
		}, 1000); // 1sec
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236010, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Porgus
				spawn(236010, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Porgus
			}
		}, 5000); // 5sec
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(236010, 619.39197f, 196.22458f, 925.2501f, (byte) 37); // Trained Porgus
				spawn(236010, 625.27203f, 201.7753f, 924.5915f, (byte) 43); // Trained Porgus
			}
		}, 10000); // 10sec
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) { // Handles the wave on Level 1
		switch (npc.getNpcId()) { // Can use Switches in any order (depends on what the player use)
			case 702673: // Switch 1
				deleteNpc(npc.getNpcId(), npc.getObjectId());
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 1

					@Override
					public void run() {
						spawn(236077, 945.9489f, 773.0383f, 734.05475f, (byte) 7); // Crumbling Skeleton
						spawn(236077, 942.5316f, 781.50635f, 734.0187f, (byte) 7); // Crumbling Skeleton
					}
				}, 2000); // 2 Sec
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 2

					@Override
					public void run() {
						spawn(236075, 947.35596f, 769.2971f, 734.05475f, (byte) 6); // Crumbling Skelesword
						spawn(236075, 943.47876f, 779.94714f, 734.0187f, (byte) 6); // Crumbling Skelesword
					}
				}, 10000); // 10 Sec
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 3

					@Override
					public void run() {
						spawn(236076, 941.248f, 777.2723f, 734.0187f, (byte) 6); // Aetherflesh Husk
					}
				}, 10000); // 18 Sec
				break;
			case 702674: // Switch 2
				deleteNpc(npc.getNpcId(), npc.getObjectId());
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 1

					@Override
					public void run() {
						spawn(236075, 969.46814f, 777.41296f, 734.05475f, (byte) 66); // Crumbling Skelesword
						spawn(236075, 968.4426f, 780.47174f, 734.05475f, (byte) 66); // Crumbling Skelesword
					}
				}, 5000); // 5 Sec
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 2

					@Override
					public void run() {
						spawn(236075, 951.9105f, 770.3287f, 734.05475f, (byte) 6); // Crumbling Skelesword
						spawn(236075, 950.66437f, 773.2599f, 733.9997f, (byte) 6); // Crumbling Skelesword
					}
				}, 10000); // 10 Sec
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 3

					@Override
					public void run() {
						spawn(236075, 964.9466f, 771.17566f, 734.05475f, (byte) 38); // Crumbling Skelesword
					}
				}, 18000); // 18 Sec
				break;
			case 702675: // Switch 3
				deleteNpc(npc.getNpcId(), npc.getObjectId());
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 1

					@Override
					public void run() {
						spawn(236077, 966.03644f, 788.0274f, 734.0461f, (byte) 65); // Crumbling Skeleton
						spawn(236077, 963.9405f, 794.1113f, 734.2379f, (byte) 66); // Crumbling Skeleton
					}
				}, 2000); // 2 Sec
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 2

					@Override
					public void run() {
						spawn(236077, 962.9926f, 788.976f, 734.0461f, (byte) 66); // Crumbling Skeleton
						spawn(236077, 960.68884f, 795.6427f, 734.2963f, (byte) 66); // Crumbling Skeleton
					}
				}, 10000); // 10sec
				ThreadPoolManager.getInstance().schedule(new Runnable() { // wave 3

					@Override
					public void run() {
						spawn(236077, 960.7599f, 790.6686f, 734.0461f, (byte) 66); // Crumbling Skeleton
					}
				}, 20000); // 20sec
				break;

			case 702690: // Switch 4
				deleteNpc(npc.getNpcId(), npc.getObjectId());
				// Summon Wave 4
				break;
			case 702691: // Switch 5
				deleteNpc(npc.getNpcId(), npc.getObjectId());
				// Summon Wave 5
				break;
			case 702692: // Switch 6
				deleteNpc(npc.getNpcId(), npc.getObjectId());
				// Summon Wave 6
				break;
		}
	}

	/**
	 * When TombStones are Dead, first Door opens 457 = First Door in Tormet > 118 after second Boss 64 = Second Door in Terror
	 *
	 * @param level
	 * @return if tombStones are all dead
	 */
	private boolean checkTombStones(int level) {
		switch (level) {
			case 1: // Right Part (Torment's Forge)
				if (getNpcs(702673).isEmpty() && getNpcs(702674).isEmpty() && getNpcs(702675).isEmpty()) {
					return true;
				}
				break;
			case 3: // Bottom Part (Hidden - Hell)
				break;
		}
		return false;
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		stageNum = 0;
		doors.clear();
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		// int index = dropItems.size() + 1; TODO ?
		switch (npcId) {
			case 702694:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000342, 20));
				break;
		}
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	private void deleteNpc(int npcId, int objId) {
		if (this.getNpc(npcId, objId) != null) {
			this.getNpc(npcId, objId).getController().onDelete();
		}
	}

	protected void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	protected void despawnNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			npc.getController().onDelete();
		}
	}

	protected Npc getNpc(int npcId, int npcObjId) {
		if (!isInstanceDestroyed) {
			for (Npc npc : instance.getNpcs()) {
				if (npc.getNpcId() == npcId && npc.getObjectId() == npcObjId) {
					return npc;
				}
			}
		}
		return null;
	}

	protected List<Npc> getNpcs(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpcs(npcId);
		}
		return null;
	}

	protected void openDoor(Player player, int doorId, boolean autoopen) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			if (autoopen) {
				StaticDoorService.getInstance().openStaticDoor(player, doorId);
			}
			else {
				door.setOpen(true);
			}
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		player.getInventory().decreaseByItemId(164000342, 20);
		TeleportService2.moveToInstanceExit(player, WorldMapType.RAKSANG_RUINS.getId(), player.getRace());
	}
}
