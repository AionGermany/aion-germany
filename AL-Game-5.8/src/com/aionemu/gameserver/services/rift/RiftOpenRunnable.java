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
package com.aionemu.gameserver.services.rift;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.rift.RiftLocation;
import com.aionemu.gameserver.services.RiftService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Source
 * @modified CoolyT
 */
public class RiftOpenRunnable implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(RiftOpenRunnable.class);
	private final int worldId;
	private final boolean guards;

	public RiftOpenRunnable(int worldId, boolean guards) {
		this.worldId = worldId;
		this.guards = guards;
	}

	@Override
	public void run() {
		log.debug("RiftOpenRunnable started.. WorldId: " + worldId);
		Map<Integer, RiftLocation> locations = RiftService.getInstance().getRiftLocations();
		String mapName = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getName();
		RiftStatistics riftStat = new RiftStatistics();
		RiftStatistics vortexStat = new RiftStatistics();
		riftStat.setWorldMap(worldId);
		vortexStat.setWorldMap(worldId);
		int skipped = 0;

		for (RiftLocation loc : locations.values()) {

			if (loc.getWorldId() == worldId) {
				RiftEnum rift = RiftEnum.getRift(loc.getId());

				boolean spawn = Rnd.chance(CustomConfig.RIFT_SPAWNCHANCE);
				log.debug("[RiftOpenRunnable] WorldId : " + worldId + " Name: " + mapName + "try to Spawn Rift " + rift.name() + " canSpawnByChance : " + spawn);

				if (!spawn && !rift.isVortex()) {
					skipped++;
					continue;
				}

				RiftStatistics stat = RiftService.getInstance().openRifts(loc, guards);

				if (stat.isVortex) {
					vortexStat.addSpawnedNpcs(stat.getSpawnedNpcs());
					vortexStat.addSpawnedRifts(stat.getSpawnedRifts());
				}
				else {
					riftStat.addSpawnedNpcs(stat.getSpawnedNpcs());
					riftStat.addSpawnedRifts(stat.getSpawnedRifts());
				}
			}
		}

		if (vortexStat.getSpawnedRifts() > 0)
			log.info("[VortexService] Spawned " + vortexStat.spawnedRifts + " Vortex" + (vortexStat.getSpawnedNpcs() > 0 ? " with " + vortexStat.getSpawnedNpcs() + " Guard Npcs" : "") + " in Map: " + mapName + " (id:" + worldId + ")");

		if (riftStat.getSpawnedRifts() > 0)
			log.info("[RiftService] Spawned randomized " + riftStat.spawnedRifts + " of " + (riftStat.spawnedRifts + skipped) + (riftStat.getSpawnedRifts() == 1 ? " Rift" : " Rifts") + (riftStat.getSpawnedNpcs() > 0 ? " with " + riftStat.getSpawnedNpcs() + " Guard Npcs" : "") + " in " + mapName + " (id:" + worldId + ")");

		// Scheduled rifts close
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				RiftService.getInstance().closeRifts();
			}
		}, RiftService.getInstance().getDuration() * 3540 * 1000);
		// Broadcast rift spawn on map
		RiftInformer.sendRiftsInfo(worldId);

	}
}
