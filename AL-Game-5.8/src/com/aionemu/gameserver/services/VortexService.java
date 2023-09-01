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
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.vortexspawns.VortexSpawnTemplate;
import com.aionemu.gameserver.model.vortex.VortexLocation;
import com.aionemu.gameserver.model.vortex.VortexStateType;
import com.aionemu.gameserver.services.rift.RiftInformer;
import com.aionemu.gameserver.services.rift.RiftManager;
import com.aionemu.gameserver.services.vortexservice.DimensionalVortex;
import com.aionemu.gameserver.services.vortexservice.Invasion;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Source
 */
public class VortexService {

	private static final int duration = CustomConfig.VORTEX_DURATION;
	private final Map<Integer, DimensionalVortex<?>> activeInvasions = new FastMap<Integer, DimensionalVortex<?>>().shared();
	private Map<Integer, VortexLocation> vortex;
	private static final Logger log = LoggerFactory.getLogger(VortexService.class);

	public void initVortexLocations() {
		if (CustomConfig.VORTEX_ENABLED) {
			vortex = DataManager.VORTEX_DATA.getVortexLocations();

			// Spawn peace
			for (VortexLocation loc : getVortexLocations().values()) {
				spawn(loc, VortexStateType.PEACE);
			}

			log.info("[VortexService] Loaded " + vortex.size() + " vortex locations");

			// Brusthonin schedule
			CronService.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startInvasion(1);
				}
			}, CustomConfig.VORTEX_BRUSTHONIN_SCHEDULE);

			// Theobomos schedule
			CronService.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startInvasion(0);
				}
			}, CustomConfig.VORTEX_THEOBOMOS_SCHEDULE);
		}
		else {
			vortex = Collections.emptyMap();
		}
	}

	public void startInvasion(final int id) {
		final DimensionalVortex<?> invasion;

		synchronized (this) {
			if (activeInvasions.containsKey(id)) {
				return;
			}
			invasion = new Invasion(vortex.get(id));
			activeInvasions.put(id, invasion);
		}

		invasion.start();

		// Scheduled invasion end
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!invasion.isGeneratorDestroyed()) {
					stopInvasion(id);
				}
			}
		}, duration * 3600 * 1000);
	}

	public void stopInvasion(int id) {
		if (!isInvasionInProgress(id)) {
			return;
		}

		DimensionalVortex<?> invasion;
		synchronized (this) {
			invasion = activeInvasions.remove(id);
		}

		if (invasion == null || invasion.isFinished()) {
			return;
		}

		invasion.stop();
	}

	public void spawn(VortexLocation loc, VortexStateType state) {
		// Spawn Dimensional Vortex
		if (state.equals(VortexStateType.INVASION)) {
			RiftManager.getInstance().spawnVortex(loc);
			RiftInformer.sendRiftsInfo(loc.getHomeWorldId());
		}

		// Spawn NPC
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getVortexSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				VortexSpawnTemplate vortextemplate = (VortexSpawnTemplate) st;
				if (vortextemplate.getStateType().equals(state)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(vortextemplate, 1));
				}
			}
		}
	}

	public void despawn(VortexLocation loc) {
		// Unset Vortex controller
		loc.setVortexController(null);

		// Despawn all NPC
		for (VisibleObject npc : loc.getSpawned()) {
			((Npc) npc).getController().cancelTask(TaskId.RESPAWN);
			npc.getController().onDelete();
		}

		loc.getSpawned().clear();
	}

	public boolean isInvasionInProgress(int id) {
		return activeInvasions.containsKey(id);
	}

	public Map<Integer, DimensionalVortex<?>> getActiveInvasions() {
		return activeInvasions;
	}

	public int getDuration() {
		return duration;
	}

	public void removeDefenderPlayer(Player player) {
		for (DimensionalVortex<?> invasion : activeInvasions.values()) {
			if (invasion.getDefenders().containsKey(player.getObjectId())) {
				invasion.kickPlayer(player, false);
				return;
			}
		}
	}

	public void removeInvaderPlayer(Player player) {
		for (DimensionalVortex<?> invasion : activeInvasions.values()) {
			if (invasion.getInvaders().containsKey(player.getObjectId())) {
				invasion.kickPlayer(player, true);
				return;
			}
		}
	}

	public boolean isInvaderPlayer(Player player) {
		for (DimensionalVortex<?> invasion : activeInvasions.values()) {
			if (invasion.getInvaders().containsKey(player.getObjectId())) {
				return true;
			}
		}

		return false;
	}

	public boolean isInsideVortexZone(Player player) {
		int playerWorldId = player.getWorldId();

		if (playerWorldId == 210060000 || playerWorldId == 220050000) {
			VortexLocation loc = getLocationByWorld(playerWorldId);
			if (loc != null) {
				return loc.getPlayers().containsKey(player.getObjectId());
			}
		}

		return false;
	}

	public VortexLocation getLocationByRift(int npcId) {
		return getVortexLocation(npcId == 831141 ? 1 : 0);
	}

	public VortexLocation getLocationByWorld(int worldId) {
		if (worldId == 210060000) {
			return getVortexLocation(0);
		}
		else if (worldId == 220050000) {
			return getVortexLocation(1);
		}
		else {
			return null;
		}
	}

	public VortexLocation getVortexLocation(int id) {
		return vortex.get(id);
	}

	public Map<Integer, VortexLocation> getVortexLocations() {
		return vortex;
	}

	public void validateLoginZone(Player player) {
		VortexLocation loc = getLocationByWorld(player.getWorldId());
		if (loc != null && player.getRace().equals(loc.getInvadersRace())) {
			if (loc.isInsideLocation(player) && loc.isActive() && loc.getVortexController().getPassedPlayers().containsKey(player.getObjectId())) {
				return;
			}

			int mapId = loc.getHomeWorldId();
			float x = loc.getHomePoint().getX();
			float y = loc.getHomePoint().getY();
			float z = loc.getHomePoint().getZ();
			byte h = loc.getHomePoint().getHeading();
			World.getInstance().setPosition(player, mapId, x, y, z, h);
		}
	}

	public static VortexService getInstance() {
		return VortexServiceHolder.INSTANCE;
	}

	private static class VortexServiceHolder {

		private static final VortexService INSTANCE = new VortexService();
	}
}
