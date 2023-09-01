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
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.aionemu.gameserver.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.schedule.RiftSchedule;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.rift.RiftLocation;
import com.aionemu.gameserver.model.templates.rift.OpenRift;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.riftspawns.RiftSpawnTemplate;
import com.aionemu.gameserver.services.rift.RiftInformer;
import com.aionemu.gameserver.services.rift.RiftManager;
import com.aionemu.gameserver.services.rift.RiftOpenRunnable;
import com.aionemu.gameserver.services.rift.RiftStatistics;
import com.aionemu.gameserver.spawnengine.SpawnEngine;

import javolution.util.FastMap;

/**
 * @author Source
 * @modified CoolyT
 */
public class RiftService {

	private static final Logger log = LoggerFactory.getLogger(RiftService.class);
	private static final int duration = CustomConfig.RIFT_DURATION;

	private Map<Integer, RiftLocation> locations;
	private final Lock closing = new ReentrantLock();
	private FastMap<Integer, RiftLocation> activeRifts = new FastMap<Integer, RiftLocation>();

	public static RiftService getInstance() {
		return RiftServiceHolder.INSTANCE;
	}

	private static class RiftServiceHolder {

		private static final RiftService INSTANCE = new RiftService();
	}

	public RiftStatistics openRifts(RiftLocation location, boolean guards) {
		location.setOpened(true);

		// Spawn NPC guards
		if (guards) {
			List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getRiftSpawnsByLocId(location.getId());
			for (SpawnGroup2 group : locSpawns) {
				for (SpawnTemplate st : group.getSpawnTemplates()) {
					RiftSpawnTemplate template = (RiftSpawnTemplate) st;
					location.getSpawned().add(SpawnEngine.spawnObject(template, 1));
				}
			}
		}

		// Spawn rifts
		activeRifts.putEntry(location.getId(), location);
		return RiftManager.getInstance().spawnRift(location);
	}

	public void closeRift(RiftLocation location) {
		location.setOpened(false);

		if (location.getSpawned() == null)
			return;

		// Despawn NPC
		for (VisibleObject obj : location.getSpawned()) {
			Npc spawned = (Npc) obj;
			spawned.setDespawnDelayed(true);
			if (spawned.getAggroList().getList().isEmpty()) {
				spawned.getController().cancelTask(TaskId.RESPAWN);
				obj.getController().onDelete();
			}
		}

		// Clear spawned list
		location.getSpawned().clear();
	}

	public void closeRifts() {
		closing.lock();

		try {
			for (RiftLocation rift : activeRifts.values()) {
				closeRift(rift);
			}

			activeRifts.clear();
		}
		finally {
			closing.unlock();
		}
	}

	public int getDuration() {
		return duration;
	}

	public RiftLocation getRiftLocation(int id) {
		return locations.get(id);
	}

	public Map<Integer, RiftLocation> getRiftLocations() {
		return locations;
	}

	public void initRiftLocations() {
		if (CustomConfig.RIFT_ENABLED) {
			locations = DataManager.RIFT_DATA.getRiftLocations();
			log.info("[RiftService] Loaded " + locations.size() + " rift locations");
		}
		else {
			locations = Collections.emptyMap();
		}
	}

	public void initRifts() {
		if (CustomConfig.RIFT_ENABLED) {
			log.debug("[RiftService] Init Rifts...");
			RiftSchedule schedule = RiftSchedule.load();
			for (RiftSchedule.Rift rift : schedule.getRiftsList()) {
				for (OpenRift open : rift.getRift()) {
					log.debug("[RiftService] Sheduling rift by cron : " + open.getSchedule());
					CronService.getInstance().schedule(new RiftOpenRunnable(rift.getWorldId(), open.spawnGuards()), open.getSchedule());
				}
			}
		}
	}

	public boolean openRifts(int id, boolean guards) {
		if (isValidId(id)) {
			if (isRift(id)) {
				RiftLocation rift = getRiftLocation(id);
				if (rift.getSpawned().isEmpty()) {
					openRifts(rift, guards);

					// Broadcast rift spawn on map
					RiftInformer.sendRiftsInfo(rift.getWorldId());
					return true;
				}
			}
			else {
				boolean opened = false;
				for (RiftLocation rift : getRiftLocations().values()) {
					if (rift.getWorldId() == id && rift.getSpawned().isEmpty()) {
						openRifts(rift, guards);
						opened = true;
					}
				}

				// Broadcast rift spawn on map
				RiftInformer.sendRiftsInfo(id);
				return opened;
			}
		}
		return false;
	}

	public boolean closeRifts(int id) {
		if (isValidId(id)) {
			if (isRift(id)) {
				RiftLocation rift = getRiftLocation(id);
				if (!rift.getSpawned().isEmpty()) {
					closeRift(rift);
					return true;
				}
			}
			else {
				boolean opened = false;
				for (RiftLocation rift : getRiftLocations().values()) {
					if (rift.getWorldId() == id && !rift.getSpawned().isEmpty()) {
						closeRift(rift);
						opened = true;
					}
				}
				return opened;
			}
		}
		return false;
	}

	public boolean isValidId(int id) {
		if (isRift(id)) {
			return RiftService.getInstance().getRiftLocations().keySet().contains(id);
		}
		else {
			for (RiftLocation loc : RiftService.getInstance().getRiftLocations().values()) {
				if (loc.getWorldId() == id) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isRift(int id) {
		return id < 10000;
	}
}
