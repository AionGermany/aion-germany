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
import com.aionemu.gameserver.configs.main.BeritraConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.beritra.BeritraStateType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.beritraspawns.BeritraSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.beritraservice.BeritraInvasion;
import com.aionemu.gameserver.services.beritraservice.Invade;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Cx3
 */
public class BeritraService {

	private static final Logger log = LoggerFactory.getLogger(BeritraService.class);
	private static final int duration = BeritraConfig.BERITRA_DURATION;
	private final Map<Integer, BeritraInvasion<?>> activeInvasions = new FastMap<Integer, BeritraInvasion<?>>().shared();
	private Map<Integer, BeritraLocation> beritra;

	public static BeritraService getInstance() {
		return BeritraServiceHolder.INSTANCE;
	}

	private static class BeritraServiceHolder {

		private static final BeritraService INSTANCE = new BeritraService();
	}

	public void startBeritraInvasion(final int id) {
		final BeritraInvasion<?> invade;
		synchronized (this) {
			if (activeInvasions.containsKey(id)) {
				return;
			}
			invade = new Invade(beritra.get(id));
			activeInvasions.put(id, invade);
		}
		invade.start();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopBeritraInvasion(id);
			}
		}, duration * 3600 * 1000);
	}

	public void stopBeritraInvasion(int id) {
		if (!isInvasionInProgress(id))
			return;
		BeritraInvasion<?> invade;
		synchronized (this) {
			invade = activeInvasions.remove(id);
		}
		if (invade == null || invade.isFinished())
			return;
		invade.stop();
	}

	public void initBeritraLocations() {
		if (!BeritraConfig.BERITRA_ENABLED) {
			beritra = Collections.emptyMap();
		}
		else {
			beritra = DataManager.BERITRA_DATA.getBeritraLocations();
			for (BeritraLocation loc : getBeritraLocations().values()) {
				spawn(loc, BeritraStateType.PEACE);
			}
			log.info("[BeritaService] Loaded " + beritra.size() + " Beritra Invasions");
			CronService.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					for (BeritraLocation loc : getBeritraLocations().values()) {
						startBeritraInvasion(loc.getId());
					}
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player player) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_INVADE_VRITRA_SPECIAL);
						}
					});
				}
			}, BeritraConfig.BERITRA_SCHEDULE);
		}
	}

	public void spawn(BeritraLocation loc, BeritraStateType bstate) {
		if (bstate.equals(BeritraStateType.INVASION)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getBeritraSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				BeritraSpawnTemplate beritratemplate = (BeritraSpawnTemplate) st;
				if (beritratemplate.getBStateType().equals(bstate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(beritratemplate, 1));
				}
			}
		}
	}

	public int getDuration() {
		return duration;
	}

	public void despawn(BeritraLocation loc) {
		for (VisibleObject npc : loc.getSpawned()) {
			((Npc) npc).getController().cancelTask(TaskId.RESPAWN);
			npc.getController().onDelete();
		}
		loc.getSpawned().clear();
	}

	public Map<Integer, BeritraLocation> getBeritraLocations() {
		return beritra;
	}

	public BeritraLocation getBeritraLocation(int id) {
		return beritra.get(id);
	}

	public boolean isInvasionInProgress(int id) {
		return activeInvasions.containsKey(id);
	}

	public Map<Integer, BeritraInvasion<?>> getActiveInvasions() {
		return activeInvasions;
	}
}
