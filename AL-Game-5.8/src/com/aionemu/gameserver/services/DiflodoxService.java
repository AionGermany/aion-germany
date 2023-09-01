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
public class DiflodoxService {

	private static final Logger log = LoggerFactory.getLogger("ABYSS_BOSS_LOG");

	/**
	 * Balaurea race protector spawn schedule.
	 */
	private static final String DIFLODOX_SPAWN_SCHEDULE = AbyssBossesConfig.DIFLODOX_SPAWN_SCHEDULE;

	private FastMap<Integer, VisibleObject> diflodoxAbyssBoss = new FastMap<Integer, VisibleObject>();

	/**
	 * Singleton that is loaded on the class initialization.
	 */
	private static final DiflodoxService instance = new DiflodoxService();

	public static DiflodoxService getInstance() {
		return instance;
	}

	public void initDiflodox() {
		if (!AbyssBossesConfig.DIFLODOX_ENABLE) {
			log.info("[DiflodoxService] Diflodox disabled...");
		}
		else {
			log.info("[DiflodoxService] Diflodox actived...");
			startDiflodox();
		}
	}

	public void startDiflodox() {
		// Abyss Diflodox start... Diflodox
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (diflodoxAbyssBoss.containsKey(883659) && diflodoxAbyssBoss.get(883659).isSpawned()) {
					log.warn("Diflodox was already spawned...");
				}
				else {
					int randomPos = Rnd.get(1, 3);
					switch (randomPos) {
						case 1:
							diflodoxAbyssBoss.put(883659, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 883659, 1747.0961f, 1730.6047f, 2886.4045f, (byte) 0), 1));
							break;
						case 2:
							diflodoxAbyssBoss.put(883659, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 883659, 2161.5806f, 2213.3289f, 2877.2397f, (byte) 40), 1));
							break;
						case 3:
							diflodoxAbyssBoss.put(883659, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 883659, 1042.2611f, 2778.6042f, 2925.89f, (byte) 48), 1));
							break;
					}
					log.info("Diflodox spawned in the Abyss");
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player player) {
							PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Diflodox spawned in the Abyss", ChatType.BRIGHT_YELLOW_CENTER));
						}
					});
					// Diflodox despawned after 1 hr if not killed
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							for (VisibleObject vo : diflodoxAbyssBoss.values()) {
								if (vo != null) {
									Npc npc = (Npc) vo;
									if (!npc.getLifeStats().isAlreadyDead()) {
										npc.getController().onDelete();
									}
								}
								diflodoxAbyssBoss.clear();
								log.info("Diflodox dissapeared");
								World.getInstance().doOnAllPlayers(new Visitor<Player>() {

									@Override
									public void visit(Player player) {
										PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Diflodox dissapeared", ChatType.BRIGHT_YELLOW_CENTER));
									}
								});
							}
						}
					}, 3600 * 1000);
				}
			}
		}, DIFLODOX_SPAWN_SCHEDULE);
	}
}
