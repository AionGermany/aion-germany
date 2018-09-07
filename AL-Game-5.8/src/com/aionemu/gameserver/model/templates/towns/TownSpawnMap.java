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
package com.aionemu.gameserver.model.templates.towns;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ViAl
 */
@XmlType(name = "town_spawn_map")
public class TownSpawnMap {

	@XmlAttribute(name = "map_id")
	private int mapId;
	@XmlElement(name = "town_spawn")
	private List<TownSpawn> townSpawns;
	private TIntObjectHashMap<TownSpawn> townSpawnsData = new TIntObjectHashMap<TownSpawn>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		townSpawnsData.clear();

		for (TownSpawn town : townSpawns) {
			townSpawnsData.put(town.getTownId(), town);
		}
		townSpawns.clear();
		townSpawns = null;
	}

	/**
	 * @return the mapId
	 */
	public int getMapId() {
		return mapId;
	}

	public TownSpawn getTownSpawn(int townId) {
		return townSpawnsData.get(townId);
	}

	public Collection<TownSpawn> getTownSpawns() {
		return townSpawnsData.valueCollection();
	}
}
