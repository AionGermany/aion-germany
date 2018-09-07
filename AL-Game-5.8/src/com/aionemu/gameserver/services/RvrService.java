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
package com.aionemu.gameserver.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.RvRConfig;
import com.aionemu.gameserver.configs.schedule.RvrSchedule;
import com.aionemu.gameserver.configs.schedule.RvrSchedule.Rvr;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.rvr.RvrLocation;
import com.aionemu.gameserver.model.rvr.RvrStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.rvrspawns.RvrSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.rvrservice.DirectPortal;
import com.aionemu.gameserver.services.rvrservice.RvrStartRunnable;
import com.aionemu.gameserver.services.rvrservice.Rvrlf3df3;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

public class RvrService {

	private RvrSchedule rvrSchedule;
	private Map<Integer, RvrLocation> rvr;
	private static Logger log = LoggerFactory.getLogger(SvsService.class);
	private static final int duration = RvRConfig.RVR_DURATION;

	// Brigade General's Urgent Order 4.9.1
	private final Map<Integer, Rvrlf3df3<?>> activeRvr = new FastMap<Integer, Rvrlf3df3<?>>().shared();

	public void initRvrLocations() {
		if (RvRConfig.RVR_ENABLED) {
			rvr = DataManager.RVR_DATA.getRvrLocations();
			for (RvrLocation loc : getRvrLocations().values()) {
				spawn(loc, RvrStateType.PEACE);
			}
			log.info("[RvRService] Loaded " + rvr.size() + " RvR Locations.");
		}
		else {
			rvr = Collections.emptyMap();
		}
	}

	public void initRvr() {
		log.info("[RvRService] started ...");
		rvrSchedule = RvrSchedule.load();
		for (Rvr rvr : rvrSchedule.getRvrsList()) {
			for (String rvrTime : rvr.getRvrTimes()) {
				CronService.getInstance().schedule(new RvrStartRunnable(rvr.getId()), rvrTime);
			}
		}
	}

	public void RvrDisabled() {
		log.info("[RvRService] Disabled ...");
	}

	public void startRvr(final int id) {
		final Rvrlf3df3<?> directPortal;
		synchronized (this) {
			if (activeRvr.containsKey(id)) {
				return;
			}
			directPortal = new DirectPortal(rvr.get(id));
			activeRvr.put(id, directPortal);
		}
		directPortal.start();
		rvrCountdownMsg(id);
		LF6RvrCountdownMsg(id);
		DF6RvrCountdownMsg(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopRvr(id);
			}
		}, duration * 3600 * 1000);
	}

	public void stopRvr(int id) {
		if (!isRvrInProgress(id)) {
			return;
		}
		Rvrlf3df3<?> directPortal;
		synchronized (this) {
			directPortal = activeRvr.remove(id);
		}
		if (directPortal == null || directPortal.isFinished()) {
			return;
		}
		directPortal.stop();
	}

	public void spawn(RvrLocation loc, RvrStateType rstate) {
		if (rstate.equals(RvrStateType.RVR)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getRvrSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				RvrSpawnTemplate rvrtemplate = (RvrSpawnTemplate) st;
				if (rvrtemplate.getRStateType().equals(rstate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(rvrtemplate, 1));
				}
			}
		}
	}

	/**
	 * Rvr Countdown
	 */
	public boolean rvrCountdownMsg(int id) {
		switch (id) {
			case 1:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// Brigade General's Urgent Order.
						PacketSendUtility.sendSys3Message(player, "\uE005", "<Brigade General's Urgent Order> start at Heiron/Beluslan !!!");
						// The Legion's Corridor has opened.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL_OPEN, 20000);
						// The Legion's Corridor will close in 45 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_01, 900000);
						// The Legion's Corridor will close in 30 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_02, 1800000);
						// The Legion's Corridor will close in 15 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_03, 2700000);
						// The Legion's Corridor will close in 10 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_04, 3000000);
						// The Legion's Corridor will close in 5 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_05, 3300000);
						// The Legion's Corridor will close in 1 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_06, 3540000);
						// When the Legion's Corridor closes, you will automatically return to the entrance where you came from.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL_CLOSE_COMPULSION_TELEPORT, 3600000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean LF6G1Spawn01Msg(int id) {
		switch (id) {
			case 3:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Asmodian warship will invade in 10 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_01);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean LF6G1Spawn02Msg(int id) {
		switch (id) {
			case 3:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Asmodian warship will invade in 5 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_02);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean LF6G1Spawn03Msg(int id) {
		switch (id) {
			case 3:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Asmodian warship will invade in 3 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_03);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean LF6G1Spawn04Msg(int id) {
		switch (id) {
			case 3:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Asmodian warship will invade in 1 minute.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_04, 0);
						// An Asmodian warship will arrive shortly.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G2_Event_Start, 10000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean LF6G1Spawn05Msg(int id) {
		switch (id) {
			case 3:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// Asmodian warship Invasion.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_05, 0);
						// The Asmodian forces are approaching.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_LF6_Event_G1_Wave_Start_01, 60000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean LF6EventG2Start02Msg(int id) {
		switch (id) {
			case 3:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Asmodian Troopers scout ship will arrive at the Sky Island in one minute.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_LF6_Event_G2_Start_02, 20000);
						// An Asmodian Troopers scout ship will soon arrive at the Sky Island.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_B_G2_Spawn, 60000);
						// The Asmodian Troopers are retreating after the defeat of their officers.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_LF6_Event_G2_Start_01, 1800000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean LF6RvrCountdownMsg(int id) {
		switch (id) {
			case 3:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// The Elyos frigate invasion will end in 10 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_Evett_G1_Time_End_01, 3000000);
						// The Elyos frigate invasion is about to end.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_Evett_G1_Time_End_02, 3300000);
						// The Elyos frigate invasion has ended.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_Evett_G1_Time_End_03, 3540000);
						// The defense against the Elyos warship failed. The Asmodians have attacked Ariel's Sanctuary.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_LF6_Event_G1_Defence_Failed, 3600000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean DF6G1Spawn01Msg(int id) {
		switch (id) {
			case 4:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Elyos warship will invade in 10 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_01);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean DF6G1Spawn02Msg(int id) {
		switch (id) {
			case 4:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Elyos warship will invade in 5 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_02);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean DF6G1Spawn03Msg(int id) {
		switch (id) {
			case 4:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Elyos warship will invade in 3 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_03);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean DF6G1Spawn04Msg(int id) {
		switch (id) {
			case 4:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Elyos warship will invade in 1 minute.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_04, 10000);
						// An Elyos warship will arrive shortly.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G2_Event_Start, 20000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean DF6G1Spawn05Msg(int id) {
		switch (id) {
			case 4:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// Elyos warship Invasion.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_05, 0);
						// The Elyos forces are approaching.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_DF6_Event_G1_Wave_Start_01, 70000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean DF6EventG2Start02Msg(int id) {
		switch (id) {
			case 4:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// An Aetos ship will arrive at the Sky Island in one minute.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_DF6_Event_G2_Start_02, 30000);
						// An Aetos ship will soon arrive at the Sky Island.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_B_G2_Spawn, 70000);
						// The Aetos are retreating after the defeat of their officers.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_DF6_Event_G2_Start_01, 1900000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public boolean DF6RvrCountdownMsg(int id) {
		switch (id) {
			case 4:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						// The Asmodian frigate invasion will end in 10 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_Evett_G1_Time_End_01, 3050000);
						// The Asmodian frigate invasion is about to end.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_Evett_G1_Time_End_02, 3290000);
						// The Asmodian frigate invasion has ended.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_Evett_G1_Time_End_03, 3530000);
						// The defense against the Asmodian warship failed. The Elyos have attacked Azphel's Sanctuary.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_DF6_Event_G1_Defence_Failed, 3590000);
					}
				});
				return true;
			default:
				return false;
		}
	}

	public void despawn(RvrLocation loc) {
		loc.setRvrActive(false);
		for (VisibleObject npc : loc.getSpawned()) {
			((Npc) npc).getController().cancelTask(TaskId.RESPAWN);
			npc.getController().onDelete();
		}
		loc.getSpawned().clear();
	}

	public boolean isRvrInProgress(int id) {
		return activeRvr.containsKey(id);
	}

	public Map<Integer, Rvrlf3df3<?>> getActiveRvr() {
		return activeRvr;
	}

	public int getDuration() {
		return duration;
	}

	public RvrLocation getRvrLocation(int id) {
		return rvr.get(id);
	}

	public Map<Integer, RvrLocation> getRvrLocations() {
		return rvr;
	}

	public static RvrService getInstance() {
		return RvrServiceHolder.INSTANCE;
	}

	private static class RvrServiceHolder {

		private static final RvrService INSTANCE = new RvrService();
	}
}
