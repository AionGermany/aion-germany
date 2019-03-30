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
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;

/**
 * @author Ares - Magenik
 */

public class NpcShoutExporter
{
	private List<DataPacket> packets;
	private String sessionName;
	private SortedMap<String, String> npcIdMap = new TreeMap<String, String>();
	private SortedMap<String, String> npcShoutIdMap = new TreeMap<String, String>();

	public NpcShoutExporter(List<DataPacket> packets, String sessionName)
	{
		super();
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse()
	{
		String fileName = "npc_shouts_" + sessionName + ".sql";

		try
		{
			String file = "output/Npc_Shout/"+fileName;
			BufferedWriter out = new BufferedWriter(new FileWriter(file));

			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_NPC_INFO".equals(name))
				{
					String objId = "";
					String npcId = "";

					List<ValuePart> valuePartList = packet.getValuePartList();

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							npcId = valuePart.readValue();
						}
						else if("objId".equals(partName))
						{
							objId = valuePart.readValue();
						}
					}
					if(!"0".equals(objId))
					{
						npcIdMap.put(objId, npcId);
					}
				}
			}

			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_SYSTEM_MESSAGE".equals(name))
				{
					String npcObjId = "";
					String msgId = "";

					List<ValuePart> valuePartList = packet.getValuePartList();

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcObjId".equals(partName))
						{
							npcObjId = valuePart.readValue();
						}
						else if("msgId".equals(partName))
						{
							msgId = valuePart.readValue();
						}
					}
					if(!"0".equals(msgId))
					{
						String npcId = "";

						for(Entry<String, String> entry : npcIdMap.entrySet())
						{
							if(entry.getKey().equals(npcObjId))
								npcId = entry.getValue();
						}

						StringBuilder sb = new StringBuilder();

						if(!npcId.equals(""))
						{
						    npcShoutIdMap.put(npcId, msgId);

						    sb.append("INSERT INTO `npc_shouts`(`npc_id`, `message_id`, `_interval`) VALUES (");
						    sb.append(npcId);
						    sb.append(", ");
						    sb.append(msgId);
						    sb.append(", 0);\n");
						}
						out.write(sb.toString());
					}
				}
			}
			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if (!npcShoutIdMap.isEmpty())
			PacketSamurai.getUserInterface().log("Export [NpcShouts] - NPC Shouts Has Been Written Successful");
	}
}