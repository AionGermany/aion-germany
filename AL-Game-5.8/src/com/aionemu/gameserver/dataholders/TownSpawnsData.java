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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.spawns.Spawn;
import com.aionemu.gameserver.model.templates.towns.TownLevel;
import com.aionemu.gameserver.model.templates.towns.TownSpawn;
import com.aionemu.gameserver.model.templates.towns.TownSpawnMap;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ViAl
 */
@XmlRootElement(name = "town_spawns_data")
public class TownSpawnsData {

	@XmlElement(name = "spawn_map")
	private List<TownSpawnMap> spawnMap;
	private TIntObjectHashMap<TownSpawnMap> spawnMapsData = new TIntObjectHashMap<TownSpawnMap>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		spawnMapsData.clear();

		for (TownSpawnMap map : spawnMap) {
			spawnMapsData.put(map.getMapId(), map);
		}
		spawnMap.clear();
		spawnMap = null;
	}

	/**
	 * @return
	 */
	public int getSpawnsCount() {
		int counter = 0;
		for (TownSpawnMap spawnMap : spawnMapsData.valueCollection()) {
			for (TownSpawn townSpawn : spawnMap.getTownSpawns()) {
				for (TownLevel townLevel : townSpawn.getTownLevels()) {
					counter += townLevel.getSpawns().size();
				}
			}
		}
		return counter;
	}

	/**
	 * @param townId
	 * @param townLevel
	 * @return
	 */
	public List<Spawn> getSpawns(int townId, int townLevel) {
		for (TownSpawnMap spawnMap : spawnMapsData.valueCollection()) {
			if (spawnMap.getTownSpawn(townId) != null) {
				TownSpawn townSpawn = spawnMap.getTownSpawn(townId);
				return townSpawn.getSpawnsForLevel(townLevel).getSpawns();
			}
		}
		return null;
	}

	public int getWorldIdForTown(int townId) {
		for (TownSpawnMap spawnMap : spawnMapsData.valueCollection()) {
			if (spawnMap.getTownSpawn(townId) != null) {
				return spawnMap.getMapId();
			}
		}
		return 0;
	}
}
