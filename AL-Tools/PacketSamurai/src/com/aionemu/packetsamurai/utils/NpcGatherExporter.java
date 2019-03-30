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
import java.util.TreeMap;
import java.util.Map.Entry;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.utils.collector.DataManager;
import com.aionemu.packetsamurai.utils.collector.data.gather.GatherableTemplate;
import com.aionemu.packetsamurai.utils.collector.data.gather.GathersTool;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnSpot;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnTemplate;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnsTool;

/**
 * @author Magenik, CoolyT
 */

public class NpcGatherExporter {
	private FastList<DataPacket> packets;
	private FastList<GatherSpawn> spawns;
	private static FastMap<Integer, FastMap<Integer, SpawnTemplate>> gatherSpawns = SpawnsTool.gatherSpawnsByWorldId;
			//new FastMap<Integer, FastMap<Integer, SpawnTemplate>>();
	private static FastMap<Integer, Stat> statMap = new FastMap<Integer,Stat>();

	static Long start = System.currentTimeMillis();
	static int _spots = 0;
	static int _spawns = 0;

	private int worldId = -1;

	public NpcGatherExporter(List<DataPacket> packets, String sessionName) {
		this.packets = new FastList<DataPacket>(packets);
		this.spawns = new FastList<GatherSpawn>();
	}

	public static String getFileNamebyWorldId(int worldId)
	{
		File dir = new File(DataManager.getPathGatherSpawns());

		for (File f : dir.listFiles())
		{
			if(f.getName().contains(""+worldId))
			return f.getName();
		}
		return worldId+"_"+SpawnsTool.mapNameById.get(worldId)+".xml";
		
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
				else if ("SM_GATHERABLE_INFO".equals(packetName))
				{
					GatherSpawn spawn = new GatherSpawn();
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					for (ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if ("x".equals(partName))
							spawn.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName))
							spawn.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName))
							spawn.z = Float.parseFloat(valuePart.readValue());
						else if ("templateId".equals(partName))
							spawn.templateId = Integer.parseInt(valuePart.readValue());
						else if ("staticId".equals(partName))
							spawn.staticId = Integer.parseInt(valuePart.readValue());
						else if ("objectId".equals(partName))
							spawn.objectId = Long.parseLong(valuePart.readValue());
						else if ("npcHeading".equals(partName))
							spawn.heading = Byte.parseByte(valuePart.readValue());
						spawn.worldId = this.worldId;
					}
					boolean exists = false;
					for (GatherSpawn n : spawns)
						if (n.objectId == spawn.objectId)
							exists = true;
					if (!exists)
						spawns.add(spawn);
				}
			}

/*			for (int i :gatherSpawns.keySet())
			{
				PacketSamurai.getUserInterface().log("WorldId from Template: "+i);
			}
*/				
			for (GatherSpawn n : spawns) 
			{
				if (!statMap.containsKey(n.worldId))
					statMap.put(n.worldId, new Stat());
				
				if (gatherSpawns.containsKey(n.worldId))
				{					
					FastMap<Integer, SpawnTemplate> mapSpawns = gatherSpawns.get(n.worldId);
					if (mapSpawns.containsKey(n.templateId))
					{
						SpawnTemplate spawn = mapSpawns.get(n.templateId);
						if(spawn.addStaticSpot(n.x, n.y, n.z, n.heading))
						{
							statMap.get(n.worldId).spots++;
							_spots++;
						}
						else continue;
						
						//PacketSamurai.getUserInterface().log("NpcId: "+spawn.npc_id+" Added Spot");
						gatherSpawns.get(n.worldId).remove(n.templateId);
						gatherSpawns.get(n.worldId).put(n.templateId, spawn);
						
					}
					else
					{
						SpawnTemplate st = new SpawnTemplate();
						st.npc_id = n.templateId;
						
						if (st.addStaticSpot(n.x,n.y,n.z,n.heading))
						{
							statMap.get(n.worldId).spots++;
							_spots++;
						}
						else continue;
						statMap.get(n.worldId).spawns++;
						_spawns++;
						mapSpawns.put(st.npc_id, st);
						
						
						//PacketSamurai.getUserInterface().log("NpcId: "+st.npc_id+" Added Spot & Spawn");
						gatherSpawns.get(n.worldId).put(n.templateId, st);
					}
				}
				else 
				{
					if (n.worldId <= 0)
						continue;

					SpawnTemplate st = new SpawnTemplate();
					st.npc_id = n.templateId;
					st.isNew = true;
					statMap.get(n.worldId).spawns++;
					_spawns++;
					if (st.addStaticSpot(n.x,n.y,n.z,n.heading))
					{
						statMap.get(n.worldId).spots++;
						_spots++;
					}
					else continue;
					
					FastMap<Integer, SpawnTemplate> fm = new FastMap<Integer,SpawnTemplate>();
					fm.put(st.npc_id, st);
					gatherSpawns.put(n.worldId, fm);
					//PacketSamurai.getUserInterface().log("NpcId: "+st.npc_id+" Added Spot & new Map & Spawn");					
				}
			}
		}	
		catch (Exception e) 
		{
			PacketSamurai.getUserInterface().log(e.getMessage());
		}
							
		if(_spawns > 0 || _spots > 0)
		{
			PacketSamurai.getUserInterface().log("Found (total) new Spawns : "+_spawns + " Spots : "+ _spots);
			writeXmlFile();
		}					
		else 			
		{				
			PacketSamurai.getUserInterface().log("Update [GatherSpawn] - nothing to do ! Your Templates are up to date");
		}

	}
	public static void writeXmlFile()
	{		
		FastMap<Integer, FastMap<String, String>> map_list = new FastMap<Integer, FastMap<String, String>>();
		FastMap<Integer, String> stat_list = new FastMap<Integer, String>();
		
		
		for (Entry<Integer, FastMap<Integer, SpawnTemplate>> s_map : gatherSpawns.entrySet())
		{
			if (!statMap.containsKey(s_map.getKey()) || statMap.get(s_map.getKey()).spots <= 0)
				continue;
			
			//PacketSamurai.getUserInterface().log("WorldID: "+s_map.getKey());

			//smap. = s_map.getValue();
			int spawns = 0;
			int spots = 0;
			StringBuilder template_sb = new StringBuilder();
			//Header
			template_sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<!-- Updated via PacketSamurai AionGer-Edtion - ["+new Date()+"] -->\r\n");
			template_sb.append("<spawns>\r\n");
			template_sb.append("\t<!-- "+SpawnsTool.mapNameById.get(s_map.getKey())+" -->\r\n");
			template_sb.append("\t<spawn_map map_id=\""+s_map.getKey()+"\">\r\n");
			Map<Integer, SpawnTemplate> map = new TreeMap<Integer, SpawnTemplate>();
			
			map.putAll(s_map.getValue()); //Sort it!!
			for (Entry<Integer, SpawnTemplate> spawn : map.entrySet())
			{
				
				GatherableTemplate npc = GathersTool.getGatherTemplate(spawn.getKey());
				if (npc == null)
				{
					PacketSamurai.getUserInterface().log(" Warning no GatherTemplate for :" +spawn.getKey());
					continue;
				}
					
				template_sb.append("\t\t<!-- "+npc.name+" ||| "+npc.sourceType+" -->\r\n");
				template_sb.append("\t\t<spawn npc_id=\""+npc.id+"\" respawn_time=\""+(spawn.getValue().respawntime > 0 ? spawn.getValue().respawntime : "105")+"\">"+(spawn.getValue().isNew ? " <!-- New -->" : "")+"\r\n");
				Map<Float, SpawnSpot> sp = new TreeMap<Float, SpawnSpot>();
				for (SpawnSpot s :spawn.getValue().spots)
				{
					if (!sp.containsKey(s.x))
						sp.put(s.x,s);
				}
				for (SpawnSpot spot : sp.values())
				{
					template_sb.append("\t\t\t<spot x=\""+spot.x+"\" y=\""+spot.y+"\" z=\""+spot.z+"\" h=\""+spot.h+"\"/>"+(spot.isNew ? " <!-- New -->" : "")+"\r\n");
				}
				template_sb.append("\t\t</spawn>\r\n");
			}
			template_sb.append("\t</spawn_map>\r\n");
			template_sb.append("</spawns>\r\n");
			FastMap<String, String> strings = new FastMap<String, String>();
			String filename = getFileNamebyWorldId(s_map.getKey());
			PacketSamurai.getUserInterface().log("FileName: "+filename);
			strings.put(template_sb.toString(), filename);
			map_list.put(s_map.getKey(), strings);
			spots = statMap.get(s_map.getKey()).spots;
			spawns = statMap.get(s_map.getKey()).spawns;
			stat_list.put(s_map.getKey(), "Update [GatherSpawn] - Written sucessfully ["+spawns+" new Gather Npcs / "+spots+" Spots] to File ("+filename+")");
		}

		//Write a File for each Map
		for (Entry<Integer, FastMap<String, String>> maps : map_list.entrySet())
		{
			for (Entry<String, String> strings : maps.getValue().entrySet())
			{			
				try 
				{
					//the FileWritePart :P					
					BufferedWriter out = new BufferedWriter(new FileWriter(DataManager.getPathGatherSpawns()+strings.getValue()));
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
		SpawnsTool.reload("Gather");
	}

	class GatherSpawn
	{
		float x;
		float y;
		float z;
		byte heading;
		int templateId;
		int worldId;
		long objectId;
		int staticId;
	}
	
	class Stat
	{
		int spawns;
		int spots;
	}
}
