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

package com.aionemu.packetsamurai.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.utils.collector.data.npcTemplates.NpcTemplate;
import com.aionemu.packetsamurai.utils.collector.data.npcTemplates.NpcsTool;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnMap;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnSpot;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnTemplate;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnTemplates;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnsTool;

/**
 * @author CoolyT
 */
public class ConquestNpcExporter {
	private FastList<DataPacket> packets;
	private FastList<NpcSpawn> spawns;

	public static SpawnTemplates template;
	private static FastMap<Integer, Stat> statMap = new FastMap<Integer,Stat>();
	public static Long start = System.currentTimeMillis();
	static boolean conquest;

	private int worldId = -1;
	private static String filename = "";
	private static FastMap<Integer, FastMap<Integer, SpawnTemplate>> conquestSpawnsByMap = SpawnsTool.conquestSpawnsByWorldId;
	private static TreeMap<Integer, TreeMap<Float, SpawnSpot>> spotsByMap = new TreeMap<Integer,TreeMap<Float,SpawnSpot>>();

	public ConquestNpcExporter(List<DataPacket> packets, String sessionName) {
		this.packets = new FastList<DataPacket>(packets);
		this.spawns = new FastList<NpcSpawn>();
	}

	public void parse()	
	{		
		try
		{
			// Collect info about all seen NPCs
			for (DataPacket packet : packets)
			{
				String packetName = packet.getName();

				if ("SM_PLAYER_SPAWN".equals(packetName))
					this.worldId = Integer.parseInt(packet.getValuePartList().get(1).readValue());
				else if ("SM_NPC_INFO".equals(packetName))
				{
					NpcSpawn spawn = new NpcSpawn();
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					for (ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if ("x".equals(partName) && spawn.x <= 0)
							spawn.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName) && spawn.y <= 0)
							spawn.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName) && spawn.z <= 0)
							spawn.z = Float.parseFloat(valuePart.readValue());
						else if ("npcHeading".equals(partName) && spawn.npcHeading <= 0)
							spawn.npcHeading = Byte.parseByte(valuePart.readValue());
						else if ("objectId".equals(partName))
							spawn.objectId = Long.parseLong(valuePart.readValue());
						else if ("npcId".equals(partName))
							spawn.npcId = Integer.parseInt(valuePart.readValue());
						else if ("spawnStaticId".equals(partName))
							spawn.spawnStaticId = Integer.parseInt(valuePart.readValue());
						spawn.worldId = this.worldId;
					}
					boolean exists = false;
					for (NpcSpawn n : spawns)
						if (n.objectId == spawn.objectId)
							exists = true;
					
					if(!SpawnsTool.isConquestNpc(spawn.npcId))
						continue;
						
					if (!exists)
						spawns.add(spawn);
				}
			}
			
			//fill spotsByMap 
			for (Entry<Integer, FastMap<Integer, SpawnTemplate>> map : conquestSpawnsByMap.entrySet())
			{
				if (!spotsByMap.containsKey(map.getKey()))
					spotsByMap.put(map.getKey(), new TreeMap<Float,SpawnSpot>());
				
				for (SpawnTemplate st : map.getValue().values())
				{
					for (SpawnSpot sp : st.spots)
					{
						if (!spotsByMap.get(map.getKey()).containsKey(sp.x))
							spotsByMap.get(map.getKey()).put(sp.x, sp);						
					}
				}
			}
			
			//Everything needed is parsed, so let's fill the NpcSpawnTemplate :)
			for (NpcSpawn n : spawns) 
			{				
				if (!spotsByMap.containsKey(n.worldId))
					continue;
				
				if (!statMap.containsKey(n.worldId))
					statMap.put(n.worldId, new Stat());
				
				NpcTemplate npc = NpcsTool.getNpcTemplate(n.npcId);
				if (npc == null)
					continue;
				
				//prevent adding Npcs with Name of "None"
				if (npc.getName().toLowerCase().equals("none"))
					continue;
				
				//prevent adding Buff & Teleport Npcs
				if (npc.getNameDesc().toLowerCase().contains("_buff" ) 
						|| npc.getNameDesc().toLowerCase().contains("_teleport") 
						|| npc.getNameDesc().toLowerCase().contains("_portal" ))
					continue;
				
				//look if WorldId already exists ..
				if (conquestSpawnsByMap.containsKey(n.worldId))
				{	//Only add spot				
					FastMap<Integer, SpawnTemplate> mapSpawns = conquestSpawnsByMap.get(n.worldId);
					if (mapSpawns.containsKey(n.npcId))
					{
						SpawnSpot spot = new SpawnSpot(n.x, n.y, n.z, n.npcHeading);
						spot.isNew = true;
						if (!spotsByMap.get(n.worldId).containsKey(n.x))
						{
							spotsByMap.get(n.worldId).put(n.x, spot );
							statMap.get(n.worldId).spots++;
						}
						
						SpawnTemplate spawn = mapSpawns.get(n.npcId);
						if(!spawn.addStaticSpot(n.x, n.y, n.z, n.npcHeading))
								continue;
						
						
						//PacketSamurai.getUserInterface().log("NpcId: "+spawn.npc_id+" Added Spot");
						conquestSpawnsByMap.get(n.worldId).remove(n.npcId);
						conquestSpawnsByMap.get(n.worldId).put(n.npcId, spawn);
					}
					else //Add Spawn & Spot
					{
						SpawnTemplate st = new SpawnTemplate();
						st.npc_id = n.npcId;
						st.isNew = true;
						
						SpawnSpot spot = new SpawnSpot(n.x, n.y, n.z, n.npcHeading);
						spot.isNew = true;
						if (!spotsByMap.get(n.worldId).containsKey(n.x))
						{
							spotsByMap.get(n.worldId).put(n.x, spot );
							statMap.get(n.worldId).spots++;
						}
						if (!st.addStaticSpot(n.x,n.y,n.z,n.npcHeading))
							continue;
						mapSpawns.put(st.npc_id, st);
						statMap.get(n.worldId).spawns++;
						//PacketSamurai.getUserInterface().log("NpcId: "+st.npc_id+" Added Spot & Spawn");
						conquestSpawnsByMap.get(n.worldId).put(n.npcId, st);
					}
				}
				else //Add Map, Spawn & Spot
				{
					SpawnMap map = new SpawnMap();
					map.worldId = worldId;
					map.isNew = true;
					
					SpawnTemplate st = new SpawnTemplate();
					st.npc_id = n.npcId;						
					
					SpawnSpot spot = new SpawnSpot(n.x, n.y, n.z, n.npcHeading);
					spot.isNew = true;
					if (!spotsByMap.get(n.worldId).containsKey(n.x))
					{
						spotsByMap.get(n.worldId).put(n.x, spot );
						statMap.get(n.worldId).spots++;
					}	
					
					if(!st.addStaticSpot(n.x,n.y,n.z,n.npcHeading))
						continue;
					map.addSpawn(st.npc_id,st);
					FastMap<Integer, SpawnTemplate> fm = new FastMap<Integer,SpawnTemplate>();
					fm.put(st.npc_id, st);
					conquestSpawnsByMap.put(map.worldId, fm);
					
					statMap.get(n.worldId).spawns++;
					
					//PacketSamurai.getUserInterface().log("NpcId: "+st.npc_id+" Added Spot & new Map & Spawn");					
				}					
			}
			
			//fill Templates with spots from spotsByMap
			for (Entry<Integer, FastMap<Integer, SpawnTemplate>> map : conquestSpawnsByMap.entrySet())
			{
				for (SpawnTemplate st : map.getValue().values())
				{
					st.spots.clear();
					st.spots.addAll(spotsByMap.get(map.getKey()).values());
				}
			}
			
			if(!statMap.isEmpty())
			{
				writeXmlFile();
			}					
			else 			
			{				
				PacketSamurai.getUserInterface().log("Update [GatherSpawn] - nothing to do ! Your Templates are up to date");
			}
		} 
		catch (Exception e) 
		{
			PacketSamurai.getUserInterface().log("Spawn [Conquest] - Parse Error: "+e.getMessage());
		}
	} 
	
	public static String getFileNamebyWorldId(int worldId)
	{
		File dir = new File("./data/spawns/Conquest/");

		for (File f : dir.listFiles())
		{
			if(f.getName().contains(""+worldId))
			return f.getName();
		}
		return worldId+"_"+SpawnsTool.mapNameById.get(worldId)+".xml";
		
	}
	
	//This is the noob-way but it works :P
	public static void writeXmlFile()
	{		
		FastMap<Integer, FastMap<String, String>> map_list = new FastMap<Integer, FastMap<String, String>>();
		FastMap<Integer, String> stat_list = new FastMap<Integer, String>();

		
		for (Entry<Integer, FastMap<Integer, SpawnTemplate>> smap : conquestSpawnsByMap.entrySet())
		{
			
			if (!statMap.containsKey(smap.getKey()))
				continue;

			StringBuilder template_sb = new StringBuilder();
			//Header
			template_sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<!-- Parsed via PacketSamurai AionGer-Edtion - ["+new Date()+"] -->\r\n");
			template_sb.append("<spawns>\r\n");
			template_sb.append("\t<!-- "+SpawnsTool.mapNameById.get(smap.getKey())+" -->\r\n");
			template_sb.append("\t<spawn_map map_id=\""+smap.getKey()+"\">\r\n");
			Map<Integer, SpawnTemplate> map = new TreeMap<Integer, SpawnTemplate>();
			map.putAll( smap.getValue());
			for (SpawnTemplate spawn : map.values())
			{
				if (conquest)
					spawn.handler ="CONQUEST";
				

				NpcTemplate npc = NpcsTool.getNpcTemplate(spawn.npc_id);
								
				template_sb.append("\t\t<!-- "+npc.getName()+" ||| "+npc.getNameDesc()+" -->\r\n");
				template_sb.append("\t\t<spawn npc_id=\""+npc.getNpcId()+"\" respawn_time=\""+(spawn.respawntime > 0 ? spawn.respawntime : "105")+"\" handler=\"CONQUEST\">"+(spawn.isNew ? " <!-- New -->" : "")+"\r\n");
				Map<Float, SpawnSpot> sp = new TreeMap<Float, SpawnSpot>();
				for (SpawnSpot s :spawn.spots)
				{
					if (!sp.containsKey(s.x))
						sp.put(s.x,s);
				}
				template_sb.append("\t\t\t<!-- "+sp.size()+" Spots -->\r\n");
				for (SpawnSpot spot : sp.values())
				{
					
					//prevent adding zero spawnpoints
					//if (spot.x == 0 && spot.y == 0)
						//continue;
					
					template_sb.append("\t\t\t<spot x=\""+spot.x+"\" y=\""+spot.y+"\" z=\""+spot.z+"\" h=\""+spot.h+"\""+ (spot.spawnStaticId != 0 ?  " walker_id=\""+spot.objectId+"\"" : "")+"/>"+(spot.isNew ? " <!-- New -->" : "")+"\r\n");
				}
				template_sb.append("\t\t</spawn>\r\n");
			}
			template_sb.append("\t</spawn_map>\r\n");
			template_sb.append("</spawns>\r\n");
			FastMap<String, String> strings = new FastMap<String, String>();
			filename = getFileNamebyWorldId(smap.getKey());
			strings.put(template_sb.toString(), filename);
			map_list.put(smap.getKey(), strings);
			stat_list.put(smap.getKey(), "Update [ConquestSpawn] - Written sucessfully ["+statMap.get(smap.getKey()).spawns+" Npcs / "+statMap.get(smap.getKey()).spots+" Spots] to File ("+filename+")");
		}

		//Write a File for each Map
		for (Entry<Integer, FastMap<String, String>> maps : map_list.entrySet())
		{
			if (!statMap.containsKey(maps.getKey()))
				continue;
			
			for (Entry<String, String> strings : maps.getValue().entrySet())
			{			
				try 
				{
					//the FileWritePart :P
					BufferedWriter out = new BufferedWriter(new FileWriter("./data/Spawns/Conquest/"+filename));
					out.write(strings.getKey());
					out.close();			
				} 
				catch (IOException e) 
				{			
					PacketSamurai.getUserInterface().log(e.toString());
				}
				if (!strings.getKey().isEmpty())
					PacketSamurai.getUserInterface().log(stat_list.get(maps.getKey())+") in "+ ((float) (System.currentTimeMillis() - start) / 1000) + "s");
			}			
		}
		SpawnsTool.reload("Conquest");
	}
	
	static class NpcSpawn
	{
		float x;
		float y;
		float z;
		byte npcHeading;
		long objectId;
		int npcId;
		int worldId;
		int spawnStaticId;
	}
	
	class Stat
	{
		int spawns = 0;
		int spots = 0;
	}
}
