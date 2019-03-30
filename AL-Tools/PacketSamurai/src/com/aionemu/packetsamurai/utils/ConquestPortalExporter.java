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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.utils.collector.DataManager;
import com.aionemu.packetsamurai.utils.collector.data.conquestPortal.ConquestPortal;
import com.aionemu.packetsamurai.utils.collector.data.conquestPortal.ConquestPortalLoc;
import com.aionemu.packetsamurai.utils.collector.data.conquestPortal.ConquestPortalsTool;
import com.aionemu.packetsamurai.utils.collector.data.npcTemplates.NpcTemplate;
import com.aionemu.packetsamurai.utils.collector.data.npcTemplates.NpcsTool;

/**
 * @author CoolyT
 */
public class ConquestPortalExporter
{
	private List<DataPacket> packets;
	private FastMap<Long, Integer> npcIdByObjId = new FastMap<Long, Integer>();
	private static TreeMap<Integer, TreeMap<Float, ConquestPortalLoc>> portals = ConquestPortalsTool.portalLocsByNpcId;
	static int spawns = 0;
	static int spots = 0;
	static FastList<Integer> npcs = new FastList<Integer>();

	public ConquestPortalExporter(List<DataPacket> packets, String sessionName)
	{
		this.packets = new FastList<DataPacket>(packets);
	}

	public void parse()
	{
		System.currentTimeMillis();
		
		//Fill Npcs List
		npcs.add(833018);
		npcs.add(833019);
		npcs.add(833021);
		npcs.add(833022);

		try
		{			
			// Collect info about all seen NPCs
			ConquestPortal portal = new ConquestPortal();
			for (DataPacket packet : packets)
			{
				String packetName = packet.getName();				
				//Only to get the npcid from the portal via objectId
				if ("SM_NPC_INFO".equals(packetName))
				{					
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					
					int npcId = 0;
					long objId = 0;
					for (ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if ("npcId".equals(partName))
							npcId = Integer.parseInt(valuePart.readValue());
						else if ("objectId".equals(partName))
							objId = Long.parseLong(valuePart.readValue());
					}
					
					if (!npcs.contains(npcId))
						continue;
					
					if (!npcIdByObjId.containsKey(objId))
						npcIdByObjId.put(objId, npcId);
				}

				else if ("SM_USE_OBJECT".equals(packetName))
				{
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					int npcId = 0;
					for (ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if ("targetObjId".equals(partName))
						{
							
							if (npcIdByObjId.containsKey(Long.parseLong(valuePart.readValue())))
							{
								//portal.objectId = Integer.parseInt(valuePart.readValue());
								npcId = npcIdByObjId.get(Long.parseLong(valuePart.readValue()));
							}
							else continue;
						}
						if ("actionType".equals(partName))
						{
							if (Integer.parseInt(valuePart.readValue()) == 2)
								portal.npcId = npcId;								
						}
					}					
				}
				
				else if ("SM_TELEPORT_LOC".equals(packetName))
				{
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					ConquestPortalLoc loc = new ConquestPortalLoc();
					loc.isNew = true;
					for (ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
								
						if ("x".equals(partName))
							loc.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName))
							loc.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName))
							loc.z = Float.parseFloat(valuePart.readValue());
						else if ("heading".equals(partName))
							loc.h = Byte.parseByte(valuePart.readValue());
					}
					if (portal.npcId <= 0)
						continue;
					
					if (portals.containsKey(portal.npcId))
					{
						if (!portals.get(portal.npcId).containsKey(loc.x))
						{
							portals.get(portal.npcId).put(loc.x,loc);
							spots++;
						}
					}
					else 
					{
						TreeMap<Float, ConquestPortalLoc> p = new TreeMap<Float,ConquestPortalLoc>();
						p.put(loc.x, loc);
						portals.put(portal.npcId,p);
						spawns++;
						spots++;
					}
				}
				
			}
			if (spots > 0)
				writeXmlFile();
			else PacketSamurai.getUserInterface().log("Export [ConquestPortals] - Nothing to do ! - The Templates are already up-to-date");
		}

		catch (Exception e)
		{
			PacketSamurai.getUserInterface().log(e.getMessage());
		}		
	}
	
	public static void writeXmlFile()
	{	
		StringBuilder template_sb = new StringBuilder();
		//Header
		template_sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<!-- Parsed via PacketSamurai AionGer-Edtion - ["+new Date()+"] -->\r\n");
		template_sb.append("<portals_conquest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"conquest_portals.xsd\">\r\n");
		
		for (Entry<Integer, TreeMap<Float, ConquestPortalLoc>> portal : portals.entrySet())
		{			
			NpcTemplate npc = NpcsTool.getNpcTemplate(portal.getKey());

			template_sb.append("\t<!-- "+(npc.getNameDesc().toLowerCase().startsWith("lf4") ? "Inngison " : "Gelkmaros ")+npc.getName()+" ||| "+npc.getNameDesc()+" -->\r\n");
			template_sb.append("\t<portal npc_id=\""+npc.getNpcId()+"\">\r\n");

			template_sb.append("\t\t<!-- "+portal.getValue().size()+" Spots -->\r\n");
			for (ConquestPortalLoc spot : portal.getValue().values())
			{
				template_sb.append("\t\t<destination x=\""+spot.x+"\" y=\""+spot.y+"\" z=\""+spot.z+"\" h=\""+spot.h+"\"/>"+(spot.isNew ? " <!-- New -->" : "")+"\r\n");				
			}
			template_sb.append("\t</portal>\r\n");			
		}
		template_sb.append("</portals_conquest>\r\n");
		
		try 
		{
			//the FileWritePart :P
			BufferedWriter out = new BufferedWriter(new FileWriter(DataManager.pathConquestPortals+"conquest_portals.xml"));
			out.write(template_sb.toString());
			out.close();			
		} 
		catch (IOException e) 
		{			
			PacketSamurai.getUserInterface().log(e.toString());
		}			
			
		PacketSamurai.getUserInterface().log("Export [ConquestPortals] - Updated sucessfully ["+spawns+" Npcs / "+spots+" Spots] in File (conquest_portals.xml");
	}
}
