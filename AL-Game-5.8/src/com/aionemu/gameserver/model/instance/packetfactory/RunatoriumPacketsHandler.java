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
package com.aionemu.gameserver.model.instance.packetfactory;

import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.RunatoriumReward;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

/**
 * @author GiGatR00n v4.7.5.x
 */
public class RunatoriumPacketsHandler {

	private final FastList<Future<?>> idgelTask = FastList.newInstance();

	private WorldMapInstance instance;
	private RunatoriumReward rr;

	private Integer mapId;
	private int instanceId;

	/**
	 * Determines whether the instance is destroying? it used to avoids spawn of NPCs on InstanceDestroy Event
	 */
	private boolean isInstanceDestroyed = false;

	public RunatoriumPacketsHandler(Integer mapId, int instanceId, WorldMapInstance instance, RunatoriumReward rr) {
		this.mapId = mapId;
		this.instanceId = instanceId;
		this.instance = instance;
		this.rr = rr;
	}

	/**
	 * Send Requirement Packets For every player entered Instance <br>
	 * <br>
	 * Type: 6 Preparing Time & Starting Score/PvPKill (BothTeam)<br>
	 * Type: 3 Spawn(0)/Dead(60) Player (For Each <PLAYER> Killed) OnDie()|OnRevive()<br>
	 * Type: 7 Instance Players Info<br>
	 */
	public void sendPreparingPacket(final Player player) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player GroupMember) {
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(6, rr.getRemainingTime(), rr, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(3, rr.getRemainingTime(), rr, player.getObjectId(), 0, player.getRace().getRaceId()));
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(7, rr.getRemainingTime(), rr, instance.getPlayersInside()));
			}
		});
	}

	/**
	 * Send 1-Time when Start_Progress and End_Progress <br>
	 * <br>
	 * Start_Progress = 0<br>
	 * End_Progress = 2<br>
	 */
	public void sendScoreTypePacket() {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(2, rr.getRemainingTime(), rr, player.getObjectId()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, rr.getRemainingTime(), rr, instance.getPlayersInside()));
			}
		});
	}

	/**
	 * Send when Boss has been Killed or Timeout reached <br>
	 * <br>
	 * OnBossKilled()<br>
	 * OnTimeOut()<br>
	 */
	public void sendRewardPacket() {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, rr.getRemainingTime(), rr, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, rr.getRemainingTime(), rr, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(5, rr.getRemainingTime(), rr, player.getObjectId()));
			}
		});
	}

	/**
	 * Send Instance Info Packet e.g. Remaining Time, Current Score/PvPKill (BothTeam), Player Status, ...
	 */
	public void sendInstanceInfoPacket() {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, rr.getRemainingTime(), rr, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, rr.getRemainingTime(), rr, instance.getPlayersInside()));
			}
		});
	}

	/**
	 * Send only when a NPC has been Killed <br>
	 * <br>
	 * OnNpcDie()
	 */
	public void sendNpcScorePacket(final Player player) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player GroupMember) {
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(11, rr.getRemainingTime(), rr, player.getObjectId()));
			}
		});
	}

	/**
	 * Send only when Player <b>Disconnects</b> or <b>Leave</b> Instance <br>
	 * <br>
	 * OnLeaveInstance()
	 */
	public void sendPlayerLeavePacket(final Player player) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player GroupMember) {
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(8, rr.getRemainingTime(), rr, player.getObjectId()));
			}
		});
	}

	/**
	 * Send only when a PLAYER has been Killed (in PvP or PvE) <br>
	 * <br>
	 * Spawn(0)/Dead(60) Player (For Each <PLAYER> Killed) OnDie()|OnRevive()
	 */
	public void sendPlayerDiePacket(final Player player) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player GroupMember) {
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(4, rr.getRemainingTime(), rr, player.getObjectId(), 60, player.getRace().getRaceId()));
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(3, rr.getRemainingTime(), rr, player.getObjectId(), 60, player.getRace().getRaceId()));
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(7, rr.getRemainingTime(), rr, instance.getPlayersInside()));
			}
		});
	}

	/**
	 * Send only when a PLAYER has been Revived OnRevive() <br>
	 * <br>
	 * Spawn(0) Player
	 */
	public void sendPlayerRevivedPacket(final Player player) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player GroupMember) {
				PacketSendUtility.sendPacket(GroupMember, new SM_INSTANCE_SCORE(4, rr.getRemainingTime(), rr, player.getObjectId(), 0, player.getRace().getRaceId()));
			}
		});
	}

	public void startInstanceTask() {
		this.rr.setInstanceStartTime();

		SpawnShallowWater();

		/* 1:37 Minutes Preparing for Battle (NA Retail v4.7.5.15) */
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				SpawnFlameVents();
				openFirstDoors();
				rr.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
				sendScoreTypePacket();// Send 1-Time when Start_Progress and End_Progress
			}
		}, rr.getPreparingTime()));

		/* after 10-Minutes Spawns 6x Intelligence Supply Box (NA Retail v4.7.5.15) */
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendInstanceInfoPacket();
				/* Intelligence Supply Box (6x) */
				sp(702581, 257.0131f, 265.5847f, 85.81963f, (byte) 30, 0);
				sp(702581, 253.07826f, 246.24838f, 92.942505f, (byte) 75, 0);
				sp(702582, 216.00116f, 210.73024f, 79.86218f, (byte) 105, 0);
				sp(702582, 272.0522f, 251.8284f, 85.81963f, (byte) 90, 0);
				sp(702583, 313.1777f, 307.97986f, 79.86218f, (byte) 45, 0);
				sp(702583, 276.37427f, 271.68332f, 92.942535f, (byte) 15, 0);
			}
		}, 600000 + rr.getPreparingTime()));

		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendInstanceInfoPacket();
				sendMsgByRace(1402367, Race.PC_ALL, 0);// Destroyer Kunax has appeared in the Slaying Arena.
				sp(234190, 264.4382f, 258.58527f, 85.81963f, (byte) 30, 0);// .....Destroyer Kunax.
				sp(234751, 273.76373f, 258.95422f, 85.81963f, (byte) 15, 0);// ....Sheban Elite Stalwart.
				sp(234751, 250.63719f, 244.0336f, 92.942505f, (byte) 75, 0);// ....Sheban Elite Stalwart.
				sp(234753, 256.33893f, 259.0221f, 85.81963f, (byte) 75, 0);// .....Sheban Elite Marauder.
				sp(234753, 278.2619f, 273.78964f, 92.942535f, (byte) 15, 0);// ....Sheban Elite Marauder.
				sp(234752, 264.9393f, 267.92413f, 85.81963f, (byte) 45, 0);// .....Sheban Elite Sniper.
				sp(234754, 264.97153f, 249.82945f, 85.81963f, (byte) 105, 0);// ...Sheban Elite Medic.
			}
		}, 600000 + rr.getPreparingTime()));

		/* Unstable Ide Energy */
		idgelTask.add(ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				SpawnUnstableIdeEnergies();
			}
		}, 20000, 45000));
	}

	protected void SpawnShallowWater() {
		sp(855009, 264.4382f, 258.58527f, 85.81963f, (byte) 38, 0);
	}

	protected void SpawnFlameVents() {
		sp(802192, 199.18739f, 191.76154f, 80.602165f, (byte) 15, 0);// ...Flame Vent Elyos
		sp(802549, 329.79938f, 326.1139f, 80.60217f, (byte) 75, 0);// .....Flame Vent Asmodian
	}

	protected void SpawnUnstableIdeEnergies() {
		/*
		 * Get Random Position (Elyos - Asmo)
		 */
		float Rx = Rnd.get(-2, 2);
		float Ry = Rnd.get(-2, 2);

		/* Searching for random player */
		sp(855008, 280.00867f, 238.88274f, 85.375f, (byte) 99, 0);
		sp(855008, 294.65027f, 274.3337f, 90.36505f, (byte) 98, 1500);
		sp(855008, 209.18584f, 256.8443f, 85.17249f, (byte) 104, 2000);
		sp(855008, 272.3378f, 292.4274f, 89.31072f, (byte) 0, 2500);
		sp(855008, 266.35608f, 278.44217f, 85.34033f, (byte) 0, 3000);
		sp(855008, 305.10504f, 311.90793f, 79.86219f, (byte) 0, 3500);

		/* Environment Factors - Static Points but in Random Time */
		sp(855012, 218.77754f + Rx, 210.01488f + Ry, 79.86219f, (byte) 73, Rnd.get(1000, 20000));
		sp(855012, 201.71503f + Rx, 194.91075f + Ry, 79.86219f, (byte) 74, Rnd.get(1000, 20000));
		sp(855012, 207.26093f + Rx, 228.40112f + Ry, 79.975296f, (byte) 33, Rnd.get(1000, 20000));
		sp(855012, 199.48077f + Rx, 244.93388f + Ry, 82.53585f, (byte) 30, Rnd.get(1000, 20000));
		sp(855012, 212.02956f + Rx, 245.16733f + Ry, 82.635704f, (byte) 58, Rnd.get(1000, 20000));
		sp(855012, 204.86206f + Rx, 259.98297f + Ry, 85.17249f, (byte) 119, Rnd.get(1000, 20000));
		sp(855012, 228.49147f + Rx, 259.14127f + Ry, 89.225716f, (byte) 119, Rnd.get(1000, 20000));
		sp(855012, 240.44017f + Rx, 234.15927f + Ry, 92.97302f, (byte) 74, Rnd.get(1000, 20000));
		sp(855012, 253.71873f + Rx, 247.44237f + Ry, 92.94253f, (byte) 15, Rnd.get(1000, 20000));
		sp(855012, 264.46152f + Rx, 223.30254f + Ry, 89.31f, (byte) 114, Rnd.get(1000, 20000));
		sp(855012, 290.363f + Rx, 234.34332f + Ry, 89.3f, (byte) 10, Rnd.get(1000, 20000));
		sp(855012, 299.15704f + Rx, 258.41733f + Ry, 89.27f, (byte) 6, Rnd.get(1000, 20000));
		sp(855012, 288.68448f + Rx, 283.96835f + Ry, 92.971634f, (byte) 15, Rnd.get(1000, 20000));
		sp(855012, 275.99603f + Rx, 270.95178f + Ry, 92.94253f, (byte) 72, Rnd.get(1000, 20000));
		sp(855012, 264.91425f + Rx, 294.6127f + Ry, 89.29f, (byte) 52, Rnd.get(1000, 20000));
		sp(855012, 240.26009f + Rx, 284.79178f + Ry, 89.3f, (byte) 70, Rnd.get(1000, 20000));
		sp(855012, 322.18576f + Rx, 258.4865f + Ry, 85.17249f, (byte) 119, Rnd.get(1000, 20000));
		sp(855012, 317.50922f + Rx, 274.21152f + Ry, 82.1668f, (byte) 90, Rnd.get(1000, 20000));
		sp(855012, 329.54337f + Rx, 274.05835f + Ry, 82.232315f, (byte) 90, Rnd.get(1000, 20000));
		sp(855012, 322.64337f + Rx, 290.91113f + Ry, 79.902466f, (byte) 90, Rnd.get(1000, 20000));
		sp(855012, 309.56665f + Rx, 307.76706f + Ry, 79.86219f, (byte) 15, Rnd.get(1000, 20000));
		sp(855012, 326.81918f + Rx, 323.3281f + Ry, 79.86219f, (byte) 13, Rnd.get(1000, 20000));
	}

	protected void openFirstDoors() {
		openDoor(1);
		openDoor(99);
	}

	protected void openDoor(int doorId) {
		StaticDoor door = instance.getDoors().get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	public void ClearTasks() {
		isInstanceDestroyed = true;

		for (FastList.Node<Future<?>> n = idgelTask.head(), end = idgelTask.tail(); (n = n.getNext()) != end;) {
			if (n.getValue() != null) {
				n.getValue().cancel(true);
			}
		}
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
		sp(npcId, x, y, z, h, 0, time, 0, null);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
		sp(npcId, x, y, z, h, 0, time, msg, race);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					spawn(npcId, x, y, z, h, entityId);
					if (msg > 0) {
						sendMsgByRace(msg, race, 0);
					}
				}
			}
		}, time));
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
				}
			}
		}, time));
	}

	protected void sendMsgByRace(final int msg, final Race race, int time) {
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					instance.doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player player) {
							if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
								PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
							}
						}
					});
				}
			}
		}, time));
	}

	protected void sendMsg(final String str) {
		if (!isInstanceDestroyed) {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendMessage(player, str);
				}
			});
		}
	}

	protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading) {
		SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(mapId, npcId, x, y, z, heading);
		return SpawnEngine.spawnObject(template, instanceId);
	}

	protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading, int staticId) {
		SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(mapId, npcId, x, y, z, heading);
		template.setStaticId(staticId);
		return SpawnEngine.spawnObject(template, instanceId);
	}

	protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading, String walkerId, int walkerIdx) {
		SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(this.mapId.intValue(), npcId, x, y, z, heading, walkerId, walkerIdx);
		return SpawnEngine.spawnObject(template, this.instanceId);
	}
}
