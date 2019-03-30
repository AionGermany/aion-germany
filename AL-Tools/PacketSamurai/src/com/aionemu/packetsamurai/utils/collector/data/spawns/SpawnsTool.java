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

import java.io.File;
import javax.xml.bind.JAXBException;
import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.utils.collector.DataLoader;
import com.aionemu.packetsamurai.utils.collector.DataManager;
import com.aionemu.packetsamurai.utils.collector.data.npcTemplates.NpcsTool;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author CoolyT
 */
public class SpawnsTool {
	
	public static SpawnTemplates temp_spawns = new SpawnTemplates();
	// WorldMap
	public static WorldMaps maps = new WorldMaps();
	public static FastMap<Integer, String> mapNameById = new FastMap<Integer, String>();
	
	//Full Spawn Templates
	public static FastList<SpawnTemplates> npc = new FastList<SpawnTemplates>();
	public static FastList<SpawnTemplates> base = new FastList<SpawnTemplates>();
	public static FastList<SpawnTemplates>siege = new FastList<SpawnTemplates>();
	public static FastList<SpawnTemplates> conquest = new FastList<SpawnTemplates>();
	public static FastList<SpawnTemplates> gather = new FastList<SpawnTemplates>();
	
	//Only Spawned Npc IDs
	public static FastList<Integer> ids_conquest = new FastList<Integer>();
	public static FastList<Integer> ids_base = new FastList<Integer>();
	public static FastList<Integer> ids_siege = new FastList<Integer>();
	public static FastList<Integer> ids_npc = new FastList<Integer>();
	public static FastList<Integer> ids_gather = new FastList<Integer>();
	
	public static FastMap<Integer, FastMap<Integer, SpawnTemplate>> gatherSpawnsByWorldId = new FastMap<Integer, FastMap<Integer, SpawnTemplate>>();
	public static FastMap<Integer, FastMap<Integer, SpawnTemplate>> conquestSpawnsByWorldId = new FastMap<Integer, FastMap<Integer, SpawnTemplate>>();
	private static boolean reload = false;
			
	public static void load(String function) 
	{		
		//Loading WorldMaps
		if (function.equals("WorldMap"))
		{
			DataLoader worldmap_loader;
			try 
			{
				worldmap_loader = new DataLoader(DataManager.getPathWorldMaps()+"world_maps.xml", new WorldMaps());
				maps = (WorldMaps) worldmap_loader.getData();
			} 
			catch (JAXBException e1) 
			{
				PacketSamurai.getUserInterface().log(e1.toString());
			}
			
			for (WorldMap map : maps.maps)
			{
				mapNameById.put(map.id, map.name);
			}
			PacketSamurai.getUserInterface().log("Template ["+function+"] - Loaded: " + mapNameById.size() + " World Maps."+(DataManager.directToServer ? " (from GameServer Data)" : " (from Local Data)"));
		}
		
		//Loading Spawns
		File directory = new File(DataManager.getPathSpawns()+function+"/");
		if (directory.isDirectory())
		{
			FastList<SpawnTemplates> list = new FastList<SpawnTemplates>();
			
			String dir = directory.getName();
			for (String filename : (directory.list()))
			{
				if (isXml(filename))
				{
					try
					{
						DataLoader loader = new DataLoader(DataManager.getPathSpawns()+dir+"/"+filename, new SpawnTemplates());						
						list.add((SpawnTemplates) loader.getData());
					}
					catch (JAXBException e) 
					{
						PacketSamurai.getUserInterface().log("SpawnLoader: ["+function+"]"+e.getMessage());
					}
				}		
			}
			int totalmaps = 0;
			int mapcount = 0; 
			
			try 
			{
					for (SpawnTemplates temp : list)
					{
						//Fill Spawn NpcId Lists
						if (function.toLowerCase().equals("npcs"))
							addNpcIds(temp.getAllSpawnNpcs());
						if (function.toLowerCase().equals("conquest"))
							addConquestNpcIds(temp.getAllSpawnNpcs());
						if (function.toLowerCase().equals("bases"))
							addBaseNpcIds(temp.getAllSpawnNpcs());
						if (function.toLowerCase().equals("sieges"))
							addSiegeNpcIds(temp.getAllSpawnNpcs());
						if (function.toLowerCase().equals("gather"))
							addGatherIds(temp.getAllSpawnNpcs());	
						
						//Fill SpawnTemplate Lists
						mapcount += temp.getSpawnMapsCount();
						for (SpawnMap map : temp.spawnmaps)
						{
							if (function.toLowerCase().equals("gather"))
								gatherSpawnsByWorldId.put(map.worldId, map.spawnsMap);
							if (function.toLowerCase().equals("conquest"))
								conquestSpawnsByWorldId.put(map.worldId, map.spawnsMap);
							
							temp_spawns.spawnmaps.add(map);
							
							
							if (function.equals("Bases"))
								totalmaps += map.getBaseSpawnCount();
							else if (function.equals("Sieges"))
								totalmaps += map.getSiegeSpawnCount();
							else totalmaps += map.getSpawnsCount();
						}
					}
					PacketSamurai.getUserInterface().log("Spawns ["+function+"] - "+(reload ? "Re" : "")+"Loaded: " + totalmaps + " Spawns from "+mapcount+" Maps."+(DataManager.directToServer ? " (from GameServer Data)" : " (from Local Data)"));
					totalmaps = 0;
					mapcount = 0;
					reload = false;
			}
			catch (Exception e) 
			{
				PacketSamurai.getUserInterface().log("SpawnLoad ["+function+"]: "+e.toString());
			}
			
			switch (function)
			{
				case "Npcs":
					npc = list;
					//PacketSamurai.getUserInterface().log("Spawns ["+function+"] - Loaded: " + ids_npc.size() + " Spawns IDs.");
					break;
					
				case "Bases":
					base = list;
					//PacketSamurai.getUserInterface().log("Spawns ["+function+"] - Loaded: " + ids_base.size() + " Spawns IDs.");
					break;
					
				case "Sieges":
					siege = list;
					//PacketSamurai.getUserInterface().log("Spawns ["+function+"] - Loaded: " + ids_siege.size() + " Spawns IDs.");
					break;
					
				case "Conquest":
					conquest = list;
					//PacketSamurai.getUserInterface().log("Spawns ["+function+"] - Loaded: " + ids_conquest.size() + " Spawns IDs.");
					break;
					
				case "Gather":
					gather = list;
					//PacketSamurai.getUserInterface().log("Spawns ["+function+"] - Loaded: " + ids_gather.size() + " Spawns IDs.");
					break;
			}
		}
	}
	
	public static void load()
	{
		maps = new WorldMaps();
		mapNameById = new FastMap<Integer, String>();
		npc = new FastList<SpawnTemplates>();
		base = new FastList<SpawnTemplates>();
		siege = new FastList<SpawnTemplates>();
		conquest = new FastList<SpawnTemplates>();
		gather = new FastList<SpawnTemplates>();
		ids_conquest = new FastList<Integer>();
		ids_base = new FastList<Integer>();
		ids_siege = new FastList<Integer>();
		ids_npc = new FastList<Integer>();
		ids_gather = new FastList<Integer>();
		gatherSpawnsByWorldId = new FastMap<Integer, FastMap<Integer, SpawnTemplate>>();
		conquestSpawnsByWorldId = new FastMap<Integer, FastMap<Integer, SpawnTemplate>>();
		
		
		load("WorldMap");
		load("Npcs");
		load("Gather");
		load("Sieges");
		load("Bases");
		load("Conquest");
	}
	
	public static void reload(String func)
	{
		reload  = true;
		gather.clear();
		ids_gather.clear();
		load(func);
	}
		
	public static boolean isNormalNpc(int id)
	{
		if (ids_npc.contains(id))
			return true;
		return false;
	}
	
	public static boolean isConquestNpc(int id)
	{
		if (ids_conquest.contains(id) || NpcsTool.getNpcTemplate(id).getNameDesc().toLowerCase().contains("f4_rotation"))
			return true;
		return false;
	}
	
	public static boolean isBaseNpc(int id)
	{
		if (ids_base.contains(id))
			return true;
		return false;
	}
	
	public static boolean isSiegeNpc(int id)
	{
		if (ids_siege.contains(id))
			return true;
		return false;
	}
	
	public static boolean isGatherNpc(int id)
	{
		if (ids_gather.contains(id))
			return true;
		return false;
	}
	
	private static void addNpcIds(FastList<Integer> list)
	{
		for (Integer id : list)
		{
			if (!ids_npc.contains(id))
				ids_npc.add(id);
		}
	}

	private static void addGatherIds(FastList<Integer> list)
	{
		for (Integer id : list)
		{
			if (!ids_gather.contains(id))
				ids_gather.add(id);
		}
	}
	
	private static void addConquestNpcIds(FastList<Integer> list)
	{
		for (Integer id : list)
		{
			if (!ids_conquest.contains(id))
				ids_conquest.add(id);
		}
	}
	
	private static void addBaseNpcIds(FastList<Integer> list)
	{
		for (Integer id : list)
		{
			if (!ids_base.contains(id))
				ids_base.add(id);
		}
	}
	
	private static void addSiegeNpcIds(FastList<Integer> list)
	{
		for (Integer id : list)
		{
			if (!ids_siege.contains(id))
				ids_siege.add(id);
		}
	}
	
	private static Boolean isXml(String file)
	{
		if (file.toLowerCase().endsWith(".xml"))
			return true;
		return false;
	}
}
