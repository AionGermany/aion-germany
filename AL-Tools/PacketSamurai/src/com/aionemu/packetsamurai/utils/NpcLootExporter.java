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

import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.SortedMap;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.utils.collector.data.quest.QuestItemsTool;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnsTool;

/**
 * @author Magenik
 */

public class NpcLootExporter {
	private List<DataPacket> packets;
	private String sessionName;
	private SortedMap<String, String> npcIdMap = new TreeMap<String, String>();
   	private FastMap<String, FastMap<Integer, Integer>> loots = new FastMap<String, FastMap<Integer, Integer>>();
   	private int worldId = -1;
   	private int questItemCounter = 0;

	public NpcLootExporter(List<DataPacket> packets, String sessionName) {
      	this.packets = new FastList<DataPacket>(packets);
		this.sessionName = sessionName;
	}

	public void parse() {
		String fileName = "5.0_loot_" + sessionName + ".xml";

		try {
			//Get NpcIDs
			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				
				if ("SM_PLAYER_SPAWN".equals(name))
					this.worldId = Integer.parseInt(packet.getValuePartList().get(1).readValue());
				else if("SM_NPC_INFO".equals(name))
				{
					String objectId = "";
					String npcId = "";

               		FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							npcId = valuePart.readValue();
						}
						else if("objectId".equals(partName))
						{
							objectId = valuePart.readValue();
						}
						
					}
					if(!"0".equals(objectId))
					{
						npcIdMap.put(objectId, npcId);
					}
				}
			}


			for (DataPacket packet : packets) {
				String packetName = packet.getName();

				if ("SM_LOOT_ITEMLIST".equals(packetName)) {
					String objectId = "";
               		int itemId = 0, itemCount = 0;
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
               		FastMap<Integer, Integer> tempData = new FastMap<Integer, Integer>();
               
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("itemId".equals(partName))
                     		itemId = Integer.parseInt(valuePart.readValue());
						else if ("itemCount".equals(partName))
                     		itemCount = Integer.parseInt(valuePart.readValue());
						else if ("objectId".equals(partName))
							objectId = valuePart.readValue();
                  		if(itemId != 0 && itemCount != 0){
                      		tempData.put(itemId, itemCount);
						}
               		}
					
					if (QuestItemsTool.isQuestItem(itemId))
					{
						questItemCounter++;
						continue;
					}
               
               		if(!loots.containsKey(objectId) && !objectId.equals("")){
                   		loots.put(objectId, tempData);
					}
				}
         	}
			String mapString;
			if (worldId > 0)
				mapString = (SpawnsTool.mapNameById.containsKey(worldId) ? SpawnsTool.mapNameById.get(worldId) : worldId+"")+"_";
			else mapString = "npc_";
			
			String file = "output/Npc_Loot/"+mapString+fileName;
			BufferedWriter out = new BufferedWriter(new FileWriter(file));

         	for (FastMap.Entry<String, FastMap<Integer, Integer>> e = loots.head(), end = loots.tail(); (e = e.getNext()) != end;)
			{
				String npcId = "";
				String objectId = e.getKey();

				for(Entry<String, String> entry : npcIdMap.entrySet())
				{
					if(entry.getKey().equals(objectId))
						npcId = entry.getValue();
				}

				if(!npcId.equals(""))
				{
					StringBuilder sb = new StringBuilder();
               		int i=1;
					
               		sb.append("   <!-- npcid= ").append(npcId).append(" -->\n");
               		sb.append("   <npc_drops npcid=\"").append(npcId).append("\" objectId=\"").append(e.getKey()).append("\">\n");
               		sb.append("      <drop_group>\n");

               		for (Map.Entry<Integer, Integer> lister : e.getValue().entrySet())
					{
                  		sb.append("         <drop count=\"").append(i++).append("\" item_id=\"").append(lister.getKey()).append("\" item_quantity=\"").append(lister.getValue()).append("\" />\n");
					}

					sb.append("		</drop_group>\n");
					sb.append("	</npc_drops>\n");

					out.write(sb.toString());
				}

			}

			out.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (!loots.isEmpty())
			PacketSamurai.getUserInterface().log("Export [NpcLoot] - NPC Loot Has Been Written Successful. Filtered "+questItemCounter+" QuestItems.");
	}

	class Loot {
		int itemId;
		int itemCount;
	}
}