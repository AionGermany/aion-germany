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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
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
 * @Original author ATracer - Kamui
 */

/**
 * @author editor Magenik, CoolyT
 */

public class NpcSpawnExporter {
	private FastList<DataPacket> packets;
	private FastList<NpcSpawn> spawns;
	private static String sessionName;
	public static SpawnTemplates template;
	public static Long start = System.currentTimeMillis();

	private int worldId = -1;
	private static String filename = "";

	@SuppressWarnings("static-access")
	public NpcSpawnExporter(List<DataPacket> packets, String sessionName) {
		this.packets = new FastList<DataPacket>(packets);
		this.sessionName = sessionName;
		this.spawns = new FastList<NpcSpawn>();
	}

	@SuppressWarnings("static-access")
	public void parse()	
	{		
		SpawnTemplates template = new SpawnTemplates();
		try
		{
			DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = dFact.newDocumentBuilder();
			Document doc = build.newDocument();
			Element root = doc.createElement("spawns");
			doc.appendChild(root);

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
						if ("x".equals(partName)&& spawn.x <= 0)
							spawn.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName)&& spawn.y <= 0)
							spawn.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName)&& spawn.z <= 0)
							spawn.z = Float.parseFloat(valuePart.readValue());
						else if ("npcHeading".equals(partName)&& spawn.npcHeading <= 0)
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
					
					NpcTemplate npc = NpcsTool.getNpcTemplate(spawn.npcId);
					
					if (((Main)PacketSamurai.getUserInterface()).isSkippingBaseNpcs() && SpawnsTool.isBaseNpc(spawn.npcId))
						continue;
					else if (((Main)PacketSamurai.getUserInterface()).isSkippingSiegeNpcs() && SpawnsTool.isSiegeNpc(spawn.npcId))
						continue;
					else if (((Main)PacketSamurai.getUserInterface()).isSkippingNpcs() && SpawnsTool.isNormalNpc(spawn.npcId))
						continue;
					else if (((Main)PacketSamurai.getUserInterface()).isSkippingConquestNpcs() && SpawnsTool.isConquestNpc(spawn.npcId))
						continue;
					
					//Skip the Npcs which doesn't visually appears
					//if (npc.getNameDesc().toLowerCase().contains("noshow") || npc.getName().toLowerCase().equals("none"))
					//	continue;
					
					//Skip every Summon, Energy, Servant
					if (npc.getNpcId() >= 833287 && npc.getNpcId() <= 833450)
						continue;
					
					// Skip Artifacts
					//if (npc.getNameDesc().toLowerCase().contains("artifact"))
					//	continue;
						
					if (!exists)
						spawns.add(spawn);
				}
			}
			
			//FileName Builder :)
			StringBuilder sb = new StringBuilder();
			sb.append("npc_spawns_");
			if (!((Main)PacketSamurai.getUserInterface()).isSkippingBaseNpcs())
				sb.append("base_");
			if (!((Main)PacketSamurai.getUserInterface()).isSkippingSiegeNpcs())
				sb.append("siege_");
			if (!((Main)PacketSamurai.getUserInterface()).isSkippingNpcs())
				sb.append("normal-npc_");
			if (!((Main)PacketSamurai.getUserInterface()).isSkippingConquestNpcs())
				sb.append("conquest_");
			
				
			this.filename  = sb.toString();
			
			//Everything needed is parsed, so let's fill the NpcSpawnTemplate :)
			for (NpcSpawn n : spawns) 
			{
				//Get the WorldMap, or create if not exists
				SpawnMap map = new SpawnMap();				
				if (!template.mapsByWorldId.containsKey(n.worldId))
					template.addMap(n.worldId, map);				
				map = template.mapsByWorldId.get(n.worldId);
				map.worldId = n.worldId;
				
				//Get the Spawn, or create if not exists
				SpawnTemplate spawn = new SpawnTemplate();
				spawn.npc_id = n.npcId;
				
				if (NpcsTool.getNpcTemplate(n.npcId).getNameDesc().toLowerCase().contains("f4_rotation"))
					spawn.handler ="CONQUEST";
				spawn.respawntime = 295; //4 min Standard RespawnTime
				
				if (!map.spawnsMap.containsKey(n.npcId))
					map.addSpawn(n.npcId, spawn);
				spawn = map.spawnsMap.get(n.npcId);
				
				//Only add Spot if it doesn't already exists
				SpawnSpot pos = new SpawnSpot();
				pos.objectId = n.objectId;
				pos.worldId = n.worldId;
				pos.spawnStaticId = n.spawnStaticId;
				pos.x = n.x;
				pos.y = n.y;
				pos.z = n.z;
				pos.h = n.npcHeading;
				if (!spawn.spotMap.containsKey(n.objectId))
					spawn.addSpot(n.objectId, pos);
			}
			
			this.template = template;
			writeXmlFile();	
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
	} 
	
	//This is the noob-way but it works :P
	public static void writeXmlFile()
	{		
		FastMap<Integer, FastMap<String, String>> map_list = new FastMap<Integer, FastMap<String, String>>();
		FastMap<Integer, String> stat_list = new FastMap<Integer, String>();

		for (SpawnMap smap : template.spawnmaps)
		{
			int spawns = 0;
			int spots = 0;
			StringBuilder template_sb = new StringBuilder();
			//Header
			template_sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<!-- Parsed via PacketSamurai AionGer-Edtion - ["+new Date()+"] -->\r\n");
			template_sb.append("<spawns>\r\n");
			template_sb.append("\t<!-- "+SpawnsTool.mapNameById.get(smap.worldId)+" -->\r\n");
			template_sb.append("\t<spawn_map map_id=\""+smap.worldId+"\">\r\n");
			Map<Integer, SpawnTemplate> map = new TreeMap<Integer, SpawnTemplate>();
			map.putAll( smap.spawnsMap);
			for (SpawnTemplate spawn : map.values())
			{
				spawns++;
				NpcTemplate npc = NpcsTool.getNpcTemplate(spawn.npc_id);
				template_sb.append("\t\t<!-- "+npc.getName()+" ||| "+npc.getNameDesc()+" -->\r\n");
				template_sb.append("\t\t<spawn npc_id=\""+npc.getNpcId()+"\" respawn_time=\""+(spawn.respawntime >= 0 ? spawn.respawntime : "105")+"\""+(SpawnsTool.isConquestNpc(npc.getNpcId()) ? " handler=\"CONQUEST\"" : "") + ">\r\n");
				//template_sb.append("\t\t\t<!-- "+spawn.spots.size()+" Spots -->\r\n");
				for (SpawnSpot spot :spawn.spots)
				{
					spots++;
					template_sb.append("\t\t\t<spot x=\""+spot.x+"\" y=\""+spot.y+"\" z=\""+spot.z+"\" h=\""+spot.h+"\""+ (spot.spawnStaticId != 0 ?  " static_id=\""+spot.spawnStaticId+"\"" : "")+"/>\r\n");
				}
				template_sb.append("\t\t</spawn>\r\n");
			}
			template_sb.append("\t</spawn_map>\r\n");
			template_sb.append("</spawns>\r\n");
			FastMap<String, String> strings = new FastMap<String, String>();
			strings.put(template_sb.toString(), (filename+smap.worldId+"_"+SpawnsTool.mapNameById.get(smap.worldId)+"_"+sessionName+".xml"));
			map_list.put(smap.worldId, strings);
			stat_list.put(smap.worldId, "Export [NpcSpawn] - Written sucessfully ["+spawns+" Npcs / "+spots+" Spots] to File ("+filename+smap.worldId+"_"+SpawnsTool.mapNameById.get(smap.worldId)+"_"+sessionName+".xml)");
		}

		//Write a File for each Map
		for (Entry<Integer, FastMap<String, String>> maps : map_list.entrySet())
		{
			for (Entry<String, String> strings : maps.getValue().entrySet())
			{			
				try 
				{
					//the FileWritePart :P
					BufferedWriter out = new BufferedWriter(new FileWriter("./output/Npc_Spawn/"+strings.getValue()));
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
	}
	
	//This is the Easy Way, works pretty fast, but doesn't allow Comments :(
	@Deprecated
	public static void writeXml()
	{
		try 
		{
	        JAXBContext jaxbContext = JAXBContext.newInstance( SpawnTemplates.class );
	        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	      
	        jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
	        jaxbMarshaller.marshal( template, new File( filename ) );
	        jaxbMarshaller.marshal( template, System.out );	        
		} 
		catch (JAXBException e) 
		{
			PacketSamurai.getUserInterface().log(e.toString());
		}
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
}