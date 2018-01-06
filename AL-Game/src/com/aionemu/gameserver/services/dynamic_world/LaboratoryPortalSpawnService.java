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
public class LaboratoryPortalSpawnService {

	private static final LaboratoryPortalSpawnService instance = new LaboratoryPortalSpawnService();
	private static final Logger log = LoggerFactory.getLogger(LaboratoryPortalSpawnService.class);
	private boolean running = false;

	public void startLaboratory() {
		cron();
	}

	private void cron() {
		if (!running) {
			checkDay();
			running = true;
		}
		log.info("Started Cron for Elemental Lord's Laboratory");
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
			case 3: // Tue
			case 5: // Thur
			case 7: // Sat
				startLaboratorySpawn();
				break;
			default:
				despawn();
				break;
		}
	}

	/**
	 * Spawn Portal's / Npc's
	 */
	private void startLaboratorySpawn() {
		SpawnTemplate portalelyos = SpawnEngine.addNewSpawn(210100000, 806202, 1369.0925f, 1581.8915f, 334.64307f, (byte) 101, 0); // IDF6_Lap_Entrance_L
		portalelyos.setStaticId(2360);
		SpawnEngine.spawnObject(portalelyos, 1);
		SpawnTemplate npcelyos = SpawnEngine.addNewSpawn(210100000, 806215, 1372.0094f, 1573.129f, 334.5f, (byte) 101, 0); // LF6_Stochio_E
		SpawnEngine.spawnObject(npcelyos, 1);
		log.info("Entrance to the Elemental Lord's Laboratory in Esterra spawned");

		SpawnTemplate portalasmos = SpawnEngine.addNewSpawn(220110000, 806204, 1817.985f, 2208.279f, 214.63792f, (byte) 30, 0); // IDF6_Lap_Entrance_D
		portalasmos.setStaticId(2878);
		SpawnEngine.spawnObject(portalasmos, 1);
		SpawnTemplate npcasmos = SpawnEngine.addNewSpawn(220110000, 806217, 1825.169f, 2213.4346f, 212.625f, (byte) 30, 0); // DF6_Guranka_E
		SpawnEngine.spawnObject(npcasmos, 1);
		log.info("Entrance to the Elemental Lord's Laboratory in Nosra spawned");
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
					case 806202:
					case 806215:
					case 806204:
					case 806217:
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
			log.info("Entrance to the Elemental Lord's Laboratory in Esterra & Nosra despawned ");
		}
	}

	public static final LaboratoryPortalSpawnService getInstance() {
		return instance;
	}
}
