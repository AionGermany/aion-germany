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

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author CoolyT
 */
@XmlRootElement(name = "spawns")
public class SpawnTemplates {

	@XmlElement(name = "spawn_map")
	public FastList<SpawnMap> spawnmaps = new FastList<SpawnMap>();

	@XmlTransient
	public FastMap<Integer, SpawnMap> mapsByWorldId = new FastMap<Integer, SpawnMap>();
	
	public SpawnTemplates() {
	}
	
	public int getSpawnMapsCount() {
		return spawnmaps.size();		
	}
	
    void afterUnmarshal(Unmarshaller u, Object parent) 
    {
    	for (SpawnMap map : spawnmaps)
    	{
    		if (!mapsByWorldId.containsKey(map.worldId))
    			mapsByWorldId.put(map.worldId, map);
    	}
    }
    
	public FastList<Integer> getAllSpawnNpcs()
	{
		FastList<Integer> list = new FastList<Integer>();
		
		for (SpawnMap map : spawnmaps)
		{
			if (!map.base_spawns.isEmpty())
			{
				for (BaseSpawnTemplate bst : map.base_spawns)
				{
					for (BaseRaceTemplate brt : bst.races)
					{
						for (SpawnTemplate st : brt.spawns)
						{
							if (!list.contains(st.npc_id))
								list.add(st.npc_id);
						}
					}
				}
			}
			else if (!map.siege_spawns.isEmpty())
			{
				for (SiegeSpawnTemplate sst : map.siege_spawns)
				{
					for (SiegeRaceTemplate srt : sst.races)
					{
						for (SiegeModTemplate smt : srt.mods)
						{
							for (SpawnTemplate st : smt.spawns)
							if (!list.contains(st.npc_id))
								list.add(st.npc_id);
						}
					}
				}
			} else
			{
				for (SpawnTemplate st : map.spawns)
				if (!list.contains(st.npc_id))
					list.add(st.npc_id);
			}
		}
		return list;
	}

	public boolean addMap(int worldId, SpawnMap map) 
	{
		if (!mapsByWorldId.containsKey(worldId))
		{
			mapsByWorldId.put(worldId, map);
			spawnmaps.add(map);
			return true;
		}
		return false;
	}
}
