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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

public class MoltenusService {

	private static final Logger log = LoggerFactory.getLogger("ABYSS_BOSS_LOG");

	/**
	 * Balaurea race protector spawn schedule.
	 */
	private static final String MOLTENUS_SPAWN_SCHEDULE = AbyssBossesConfig.MOLTENUS_SPAWN_SCHEDULE;

	private FastMap<Integer, VisibleObject> moltenusAbyssBoss = new FastMap<Integer, VisibleObject>();

	/**
	 * Singleton that is loaded on the class initialization.
	 */
	private static final MoltenusService instance = new MoltenusService();

	public static MoltenusService getInstance() {
		return instance;
	}

	public void initMoltenus() {
		if (!AbyssBossesConfig.MOLTENUS_ENABLE) {
			log.info("[MoltenusService] Moltenus disabled...");
		}
		else {
			log.info("[MoltenusService] Moltenus actived...");
			startMoltenus();
		}
	}

	public void startMoltenus() {
		// Abyss Moltenus start...
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (moltenusAbyssBoss.containsKey(251045) && moltenusAbyssBoss.get(251045).isSpawned()) {
					log.warn("Moltenus was already spawned...");
				}
				else {
					int randomPos = Rnd.get(1, 3);
					switch (randomPos) {
						case 1:
							moltenusAbyssBoss.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2464.9199f, 1689f, 2882.221f, (byte) 0), 1));
							break;
						case 2:
							moltenusAbyssBoss.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2263.4812f, 2587.1633f, 2879.5447f, (byte) 0), 1));
							break;
						case 3:
							moltenusAbyssBoss.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 1692.96f, 1809.04f, 2886.027f, (byte) 0), 1));
							break;
					}
					log.info("Moltenus spawned in the Abyss");
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player player) {
							PacketSendUtility.sendBrightYellowMessage(player, "Menotios: Fragment of the anger has been sighted in the Abyss");
						}
					});
					// Moltenus despawned after 1 hr if not killed
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							for (VisibleObject vo : moltenusAbyssBoss.values()) {
								if (vo != null) {
									Npc npc = (Npc) vo;
									if (!npc.getLifeStats().isAlreadyDead()) {
										npc.getController().onDelete();
									}
								}
								moltenusAbyssBoss.clear();
								log.info("Moltenus dissapeared");
								World.getInstance().doOnAllPlayers(new Visitor<Player>() {

									@Override
									public void visit(Player player) {
										PacketSendUtility.sendBrightYellowMessage(player, "Menotios: Fragment of anger gone");
									}
								});
							}
						}
					}, 3600 * 1000);
				}
			}
		}, MOLTENUS_SPAWN_SCHEDULE);
	}
}
