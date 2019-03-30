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
package com.aionemu.packetsamurai.utils.collector.data.spawns;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import javolution.util.FastMap;

/**
 * @author CoolyT
 */
@XmlType(name = "spawn_map")
public class SpawnMap {
	
	@XmlAttribute(name = "map_id")
	public int worldId;
	
	@XmlElement(name="spawn")
	public List<SpawnTemplate> spawns = new ArrayList<SpawnTemplate>();
	
	@XmlTransient
	public FastMap<Integer, SpawnTemplate> spawnsMap = new FastMap<Integer, SpawnTemplate>();
	
	@XmlElement(name="base_spawn")
	List<BaseSpawnTemplate> base_spawns = new ArrayList<BaseSpawnTemplate>();
	
	@XmlElement(name="siege_spawn")
	List<SiegeSpawnTemplate> siege_spawns = new ArrayList<SiegeSpawnTemplate>();
	
	@XmlTransient
	public boolean isNew = false;

	public SpawnMap() {
		this.spawns = new ArrayList<SpawnTemplate>();
		this.base_spawns = new ArrayList<BaseSpawnTemplate>();
		this.siege_spawns = new ArrayList<SiegeSpawnTemplate>();
	}
	
    void afterUnmarshal(Unmarshaller u, Object parent) 
    {
    	for (SpawnTemplate spawn : spawns)
    	{
    		if (!spawnsMap.containsKey(spawn.npc_id))
    			spawnsMap.put(spawn.npc_id, spawn);
    	}
    }

	public int getBaseSpawnCount()
	{
		int i = 0;
		for (BaseSpawnTemplate base : base_spawns)
		{
			for (BaseRaceTemplate race : base.races)
			{
				i += race.spawns.size();
			}
		}
		return i;
	}
	
	public int getSiegeSpawnCount()
	{
		int i = 0;
		for (SiegeSpawnTemplate siege : siege_spawns)
		{
			for (SiegeRaceTemplate race : siege.races)
			{
				for (SiegeModTemplate mod : race.mods)
				i += mod.spawns.size();
			}
		}
		return i;
	}
	
	public int getSpawnsCount() 
	{
		return spawns.size();
	}

	public void addSpawn(int npcId, SpawnTemplate spawn) 
	{
		spawnsMap.put(npcId, spawn);
		spawns.add(spawn);
	}	
}
