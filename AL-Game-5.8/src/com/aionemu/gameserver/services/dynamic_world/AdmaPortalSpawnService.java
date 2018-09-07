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
package com.aionemu.gameserver.services.dynamic_world;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.world.World;

/**
 * @author Falke34, FrozenKiller
 */
public class AdmaPortalSpawnService {

	private static final AdmaPortalSpawnService instance = new AdmaPortalSpawnService();
	private static final Logger log = LoggerFactory.getLogger(AdmaPortalSpawnService.class);
	private boolean running = false;

	public void startAdma() {
		cron();
	}

	private void cron() {
		if (!running) {
			checkDay();
			running = true;
		}
		log.info("Started Cron for Adma Ruins");
		String crontime = new String("1 0 * * * ?"); // check @ 00:01 every day of every month
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				checkDay();
			}
		}, crontime);
	}

	/**
	 * Check Day's
	 */
	private void checkDay() {
		int calendar = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		switch (calendar) {
			case 2: // Mon
			case 4: // Wed
			case 6: // Fri
				startAdmaSpawn();
				break;
			default:
				despawn();
				break;
		}
	}

	/**
	 * Spawn Portal's / Npc's
	 */
	private void startAdmaSpawn() {
		SpawnTemplate portalelyos = SpawnEngine.addNewSpawn(210100000, 806201, 1634.4988f, 1197.7968f, 335.10535f, (byte) 75, 0); // IDF6_Adma_Entrance_L
		portalelyos.setStaticId(2336);
		SpawnEngine.spawnObject(portalelyos, 1);
		SpawnTemplate npcelyos = SpawnEngine.addNewSpawn(210100000, 806214, 1631.2228f, 1190.2537f, 333.375f, (byte) 30, 0); // LF6_Enosi_E
		SpawnEngine.spawnObject(npcelyos, 1);
		log.info("Entrance to the Adma Ruins in Esterra spawned");

		SpawnTemplate portalasmos = SpawnEngine.addNewSpawn(220110000, 806203, 1603.1609f, 1838.8416f, 195.94737f, (byte) 113, 0); // IDF6_Adma_Entrance_D
		portalasmos.setStaticId(2877);
		SpawnEngine.spawnObject(portalasmos, 1);
		SpawnTemplate npcasmos = SpawnEngine.addNewSpawn(220110000, 806216, 1612.5476f, 1830.2737f, 193.99532f, (byte) 11, 0); // DF6_Petur_E
		SpawnEngine.spawnObject(npcasmos, 1);
		log.info("Entrance to the Adma Ruins in Nosra spawned");
	}

	/**
	 * Despawn Portal's / Npc's
	 */
	private void despawn() {
		int npcCount = 0;
		int[] Worlds = { 210100000, 220110000 };
		for (int WorldId : Worlds) {
			World.getInstance().getWorldMap(WorldId).getMainWorldMapInstance().getNpcs();
			for (Npc npc : World.getInstance().getNpcs()) {
				switch (npc.getNpcId()) {
					case 806201:
					case 806214:
					case 806203:
					case 806216:
						if (npc.isSpawned()) {
							npc.getController().onDelete();
							npcCount++;
							break;
						}
					default:
						break;
				}
			}
		}
		if (running && npcCount == 4) {
			log.info("Entrance to the Adma Ruins in Esterra & Nosra despawned ");
		}
	}

	public static final AdmaPortalSpawnService getInstance() {
		return instance;
	}
}
