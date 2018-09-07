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
package com.aionemu.gameserver.spawnengine;

import java.util.HashSet;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.TemporarySpawn;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author xTz
 */
public class TemporarySpawnEngine {

	private static final FastMap<SpawnGroup2, HashSet<Integer>> temporarySpawns = new FastMap<SpawnGroup2, HashSet<Integer>>();

	public static void spawnAll() {
		spawn(true);
	}

	public static void onHourChange() {
		despawn();
		spawn(false);
	}

	private static void despawn() {
		for (SpawnGroup2 spawn : temporarySpawns.keySet()) {
			for (SpawnTemplate template : spawn.getSpawnTemplates()) {
				if (!template.getTemporarySpawn().isInSpawnTime()) {
					FastList<VisibleObject> objects = template.getVisibleObjects();
					if (objects == null) {
						continue;
					}
					for (VisibleObject object : objects) {
						if (object instanceof Npc) {
							Npc npc = (Npc) object;
							npc.getController().cancelTask(TaskId.RESPAWN);
						}
						if (object.isSpawned()) {
							object.getController().onDelete();
						}
						spawn.setTemplateUse(object.getInstanceId(), template, false);
					}
					objects.clear();
				}
			}
		}
	}

	private static void spawn(boolean startCheck) {
		for (SpawnGroup2 spawn : temporarySpawns.keySet()) {
			HashSet<Integer> instances = temporarySpawns.get(spawn);
			if (spawn.hasPool()) {
				TemporarySpawn temporarySpawn = spawn.geTemporarySpawn();
				if ((temporarySpawn.canSpawn() || (startCheck && spawn.getRespawnTime() != 0 && temporarySpawn.isInSpawnTime()))) {
					for (Integer instanceId : instances) {
						spawn.resetTemplates(instanceId);
						for (int pool = 0; pool < spawn.getPool(); pool++) {
							SpawnTemplate template = spawn.getRndTemplate(instanceId);
							SpawnEngine.spawnObject(template, instanceId);
						}
					}
				}
			}
			else {
				for (SpawnTemplate template : spawn.getSpawnTemplates()) {
					TemporarySpawn temporarySpawn = template.getTemporarySpawn();
					if ((temporarySpawn.isInSpawnTime() || (startCheck && !template.isNoRespawn() && temporarySpawn.isInSpawnTime()))) {
						for (Integer instanceId : instances) {
							if (!spawn.isTemplateUsed(instanceId, template)) {
								SpawnEngine.spawnObject(template, instanceId);
								spawn.setTemplateUse(instanceId, template, true);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param spawnTemplate
	 */
	public static void addSpawnGroup(SpawnGroup2 spawn, int instanceId) {
		HashSet<Integer> instances = temporarySpawns.get(spawn);
		if (instances == null) {
			instances = new HashSet<Integer>();
			temporarySpawns.put(spawn, instances);
		}
		instances.add(instanceId);
	}
}
