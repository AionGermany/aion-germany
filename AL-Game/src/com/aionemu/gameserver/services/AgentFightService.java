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

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.AgentFightConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

public class AgentFightService {

	private static final Logger log = LoggerFactory.getLogger("AGENT-FIGHT_LOG");
	private static final String AGENT_FIGHT_SPAWN_SCHEDULE = AgentFightConfig.AGENT_FIGHT_SPAWN_SCHEDULE;
	private FastMap<Integer, VisibleObject> agents = new FastMap<Integer, VisibleObject>();
	private Future<?> TimerStop;
	private Future<?> TimerStop2;

	public void initAgentFight() {
		if (!AgentFightConfig.AGENT_ENABLE) {
			log.info("[AgentFightService] Agent Fight are disabled...");
		}
		else {
			log.info("[AgentFightService] Agent Fight are actived...");
			startAgentFight();
		}
	}

	public void startAgentFight() {
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawnAgent();
				announceAgent();
				log.info("[AgentFightService] Veille VS Mastarius Started");
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite);
					}
				});
			}
		}, AGENT_FIGHT_SPAWN_SCHEDULE, true);
	}

	public void announceAgent() { // 10 Min left message
		TimerStop = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				log.info("[AgentFightService] Veille VS Mastarius Announce (10 Min left)"); // 10 Min left
				announceAgent2();
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite_time_01);
					}
				});
			}
		}, AgentFightConfig.AGENT_FIGHT_SPAWN_ANNOUNCE * 60 * 1000);
	}

	public void announceAgent2() { // 5 Min left message
		TimerStop2 = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				log.info("[AgentFightService] Veille VS Mastarius (5 min left)");
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite_time_02);
					}
				});
			}
		}, 300000); // 5 Min (300000)
	}

	public void endAnnounce() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite_time_03);
				log.info("[AgentFightService] Veille VS Mastarius Finish");
			}
		});
	}

	private void despawnTimer() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				despawnNpcs();
				endAnnounce();
			}
		}, AgentFightConfig.AGENT_FIGHT_SPAWN_RUNTIME * 3600 * 1000); // Despawn after in Hour's
	}

	public void spawnAgent() {
		despawnTimer();
		agents.put(235064, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(600100000, 235064, 877.99915f, 684.29407f, 271.8933f, (byte) 15), 1)); // Veille.
		agents.put(235065, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(600100000, 235065, 1042.4966f, 1432.3025f, 260.73108f, (byte) 80), 1)); // Mastarius.
		agents.put(832830, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(600100000, 832830, 877.99915f, 684.29407f, 271.8933f, (byte) 15), 1)); // Veille's Flag.
		agents.put(832831, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(600100000, 832831, 1042.4966f, 1432.3025f, 260.73108f, (byte) 80), 1)); // Mastarius Flag.
	}

	public void onVeilleReward() {
		World.getInstance().getWorldMap(WorldMapType.AKARON.getId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.getRace() == Race.ASMODIANS) {
					AbyssPointsService.addGp(player, AgentFightConfig.AGENT_FIGHT_GP);
					ItemService.addItem(player, AgentFightConfig.AGENT_FIGHT_ITEM, AgentFightConfig.AGENT_FIGHT_AMMOUNT); // Item / AMMOUNT
					BaseService.getInstance().capture(100, Race.ASMODIANS); // Central Base Asmodians
				}
				despawnNpcs();
			}
		});
	}

	public void onMastaReward() {
		World.getInstance().getWorldMap(WorldMapType.AKARON.getId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.getRace() == Race.ELYOS) {
					AbyssPointsService.addGp(player, AgentFightConfig.AGENT_FIGHT_GP);
					ItemService.addItem(player, AgentFightConfig.AGENT_FIGHT_ITEM, AgentFightConfig.AGENT_FIGHT_AMMOUNT); // Item / AMMOUNT
					BaseService.getInstance().capture(100, Race.ELYOS); // Central Base Elyos
				}
				despawnNpcs();
			}
		});
	}

	public void devMod() {
		announceAgent();
		spawnAgent();
		log.info("[AgentFightService] Veille VS Mastarius 4.9 Started");
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite);
			}
		});
	}

	private void despawnNpcs() {
		for (VisibleObject vo : agents.values()) {
			if (vo != null) {
				Npc npc = (Npc) vo;
				if (!npc.getLifeStats().isAlreadyDead()) {
					npc.getController().onDelete();
				}
			}
		}
		agents.clear();
		TimerStop.cancel(true);
		TimerStop2.cancel(true);
	}

	public static AgentFightService getInstance() {
		return AgentFightHolder.INSTANCE;
	}

	private static class AgentFightHolder {

		private static final AgentFightService INSTANCE = new AgentFightService();
	}
}
