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
import com.aionemu.gameserver.model.dynamicportal.DynamicPortalLocation;
import com.aionemu.gameserver.model.dynamicportal.DynamicPortalStateType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.dynamicportalspawns.DynamicPortalSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.dynamicportal.DynamicPortal;
import com.aionemu.gameserver.services.dynamicportal.Portal;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Falke_34
 */
public class DynamicPortalService {

	private Map<Integer, DynamicPortalLocation> dynamicPortal;
	private static final int duration = CustomConfig.DYNAMIC_PORTAL_DURATION;
	private final Map<Integer, DynamicPortal<?>> activeDynamicPortal = new FastMap<Integer, DynamicPortal<?>>().shared();
	private static final Logger log = LoggerFactory.getLogger(DynamicPortalService.class);

	public void initDynamicPortalLocations() {
		if (CustomConfig.DYNAMIC_PORTAL_ENABLED) {
			dynamicPortal = DataManager.DYNAMIC_PORTAL_DATA.getDynamicPortalLocations();

			for (DynamicPortalLocation loc : getDynamicPortalLocations().values()) {
				spawn(loc, DynamicPortalStateType.CLOSED);
			}

			log.info("[DynamicPortalService] Loaded " + dynamicPortal.size() + " dynamic portal locations");

			CronService.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startDynamicPortal(1);
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendPacket(player,SM_SYSTEM_MESSAGE.STR_MSG_LDF5a_Open_01);
						}
					});
				}
			}, CustomConfig.DYNAMIC_PORTAL_KATALAM_SCHEDULE);
		} 
		else {
			dynamicPortal = Collections.emptyMap();
		}
	}

	public void startDynamicPortal(final int id) {
		final DynamicPortal<?> portal;

		synchronized (this) {
			if (activeDynamicPortal.containsKey(id)) {
				return;
			}
			portal = new Portal(dynamicPortal.get(id));
			activeDynamicPortal.put(id, portal);
		}
		portal.start();

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopDynamicPortal(id);
			}
		}, duration * 3600 * 1000);
	}

	public void stopDynamicPortal(int id) {
		if (!isDynamicPortalInProgress(id)) {
			return;
		}

		DynamicPortal<?> portal;
		synchronized (this) {
			portal = activeDynamicPortal.remove(id);
		}

		if (portal == null || portal.isClosed()) {
			return;
		}

		portal.stop();
	}

	public void spawn(DynamicPortalLocation loc, DynamicPortalStateType dstate) {
		if (dstate.equals(DynamicPortalStateType.OPEN)) {
		}

		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getDynamicPortalSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				DynamicPortalSpawnTemplate dynamicPortaltemplate = (DynamicPortalSpawnTemplate) st;
				if (dynamicPortaltemplate.getDStateType().equals(dstate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(dynamicPortaltemplate, 1));
				}
			}
		}
	}

	public void despawn(DynamicPortalLocation loc) {
		for (VisibleObject npc : loc.getSpawned()) {
			((Npc) npc).getController().cancelTask(TaskId.RESPAWN);
			npc.getController().onDelete();
		}
		loc.getSpawned().clear();
	}

	public boolean isDynamicPortalInProgress(int id) {
		return activeDynamicPortal.containsKey(id);
	}

	public Map<Integer, DynamicPortal<?>> getActiveDynamicPortal() {
		return activeDynamicPortal;
	}

	public int getDuration() {
		return duration;
	}

	public DynamicPortalLocation getDynamicPortalLocation(int id) {
		return dynamicPortal.get(id);
	}

	public Map<Integer, DynamicPortalLocation> getDynamicPortalLocations() {
		return dynamicPortal;
	}

	public static DynamicPortalService getInstance() {
		return DynamicPortalServiceHolder.INSTANCE;
	}

	private static class DynamicPortalServiceHolder {
		private static final DynamicPortalService INSTANCE = new DynamicPortalService();
	}
}
