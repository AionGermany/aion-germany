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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.AbyssBossesConfig;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * author Phantom_KNA
 */
public class DiflonaxService {

	private static final Logger log = LoggerFactory.getLogger("ABYSS_BOSS_LOG");

	/**
	 * Balaurea race protector spawn schedule.
	 */
	private static final String DIFLONAX_SPAWN_SCHEDULE = AbyssBossesConfig.DIFLONAX_SPAWN_SCHEDULE;

	private FastMap<Integer, VisibleObject> diflonaxAbyssBoss = new FastMap<Integer, VisibleObject>();

	/**
	 * Singleton that is loaded on the class initialization.
	 */
	private static final DiflonaxService instance = new DiflonaxService();

	public static DiflonaxService getInstance() {
		return instance;
	}

	public void initDiflonax() {
		if (!AbyssBossesConfig.DIFLONAX_ENABLE) {
			log.info("[DiflonaxService] Diflonax disabled...");
		}
		else {
			log.info("[DiflonaxService] Diflonax actived...");
			startDiflonax();
		}
	}

	public void startDiflonax() {
		// Abyss Diflonax start... Diflonax
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (diflonaxAbyssBoss.containsKey(883322) && diflonaxAbyssBoss.get(883322).isSpawned()) {
					log.warn("Diflonax was already spawned...");
				}
				else {
					int randomPos = Rnd.get(1, 3);
					switch (randomPos) {
						case 1:
							diflonaxAbyssBoss.put(883322, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 883322, 2464.9199f, 1689f, 2882.221f, (byte) 0), 1));
							break;
						case 2:
							diflonaxAbyssBoss.put(883322, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 883322, 3083.1191f, 925.84436f, 2844.6846f, (byte) 74), 1));
							break;
						case 3:
							diflonaxAbyssBoss.put(883322, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 883322, 1692.96f, 1809.04f, 2886.027f, (byte) 0), 1));
							break;
					}
					log.info("Diflonax spawned in the Abyss");
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player player) {
							PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Diflonax spawned in the Abyss", ChatType.BRIGHT_YELLOW_CENTER));
						}
					});
					// Diflonax despawned after 1 hr if not killed
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							for (VisibleObject vo : diflonaxAbyssBoss.values()) {
								if (vo != null) {
									Npc npc = (Npc) vo;
									if (!npc.getLifeStats().isAlreadyDead()) {
										npc.getController().onDelete();
									}
								}
								diflonaxAbyssBoss.clear();
								log.info("Diflonax dissapeared");
								World.getInstance().doOnAllPlayers(new Visitor<Player>() {

									@Override
									public void visit(Player player) {
										PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Diflonax dissapeared", ChatType.BRIGHT_YELLOW_CENTER));
									}
								});
							}
						}
					}, 3600 * 1000);
				}
			}
		}, DIFLONAX_SPAWN_SCHEDULE);
	}
}
