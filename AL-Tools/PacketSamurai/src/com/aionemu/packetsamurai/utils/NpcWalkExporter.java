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
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.SortedMap;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;

/**
 * @author Magenik
 */

public class NpcWalkExporter {
	private List<DataPacket> packets;
	private String sessionName;
	private SortedMap<String, String> npcIdMap = new TreeMap<String, String>();
	private FastMap<String, FastList<Move>> moves = new FastMap<String, FastList<Move>>();

	public NpcWalkExporter(List<DataPacket> packets, String sessionName) {
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse() {
		String fileName = "npc_walker_" + sessionName + ".xml";

		try {
			//Get NpcIDs
			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_NPC_INFO".equals(name))
				{
					String objectId = "";
					String npcId = "";

					List<ValuePart> valuePartList = packet.getValuePartList();

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

				if ("SM_MOVE".equals(packetName)) {
					Move movement = new Move();
					String objectId = "";
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("x".equals(partName))
							movement.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName))
							movement.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName))
							movement.z = Float.parseFloat(valuePart.readValue());
						else if ("objectId".equals(partName))
							objectId = valuePart.readValue();
					}
					if(!moves.containsKey(objectId) && !objectId.equals(""))
						moves.put(objectId, new FastList<Move>());
					moves.get(objectId).add(movement);
				}
			}


			String file = "output/Npc_Walker/"+fileName;
			BufferedWriter out = new BufferedWriter(new FileWriter(file));

			for (FastMap.Entry<String, FastList<Move>> e = moves.head(), end = moves.tail(); (e = e.getNext()) != end;)
			{
				String npcId = "";
				String npcobjectId = e.getKey();

				for(Entry<String, String> entry : npcIdMap.entrySet())
				{
					if(entry.getKey().equals(npcobjectId))
						npcId = entry.getValue();
				}

				if(!npcId.equals(""))
				{
					StringBuilder sb = new StringBuilder();

					sb.append("	<walker_template route_id=\"").append(e.getKey()).append("\" pool=\"1\">\n");
					sb.append("		<!-- NpcID: ").append(npcId).append(" -->\n");

					for (int i = 1; i <= e.getValue().size(); i++)
					{
						Move lsMove = e.getValue().get(i - 1);
						sb.append("			<routestep step=\"").append(i).append("\" x=\"").append(lsMove.x).append("\" y=\"").append(lsMove.y).append("\" z=\"").append(lsMove.z).append("\" rest_time=\"0\"/>\n");
					}
					sb.append("	</walker_template>\n");
					out.write(sb.toString());
				}
			}
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (!npcIdMap.isEmpty())
			PacketSamurai.getUserInterface().log("Export [NpcWalker] - NPC Walker Has Been Written Successful");
	}

	class Move {
		float x;
		float y;
		float z;
	}
}