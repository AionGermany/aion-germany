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

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;

/**
 * @Original author ATracer - Kamui
 */

/**
 * @author editor Magenik
 */

public class NpcInfoExporter
{
	private List<DataPacket> packets;
	private String sessionName;
	private FastList<NpcInfo> npcInfoList = new FastList<NpcInfo>();
	private FastMap<Integer, NpcStats> npcStatMap = new FastMap<Integer, NpcStats>();
	private FastMap<Integer, Integer> objectIds = new FastMap<Integer, Integer>();
	private FastMap<Integer, NpcStats> notProcessed = new FastMap<Integer, NpcStats>();
	
	public NpcInfoExporter(List<DataPacket> packets, String sessionName) 
	{
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse()
	{
		String fileName = "npc_info_" + sessionName + ".xml";

		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter("output/Npc_Info/"+fileName));

			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_NPC_INFO".equals(name))
				{
					List<ValuePart> valuePartList = packet.getValuePartList();
					NpcInfo npc = new NpcInfo();
					NpcStats stats = new NpcStats();
					
					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							npc.npcId = Integer.parseInt(valuePart.readValue());
						}
						else if("spawnStaticId".equals(partName))
						{
							npc.spawnStaticId = Integer.parseInt(valuePart.readValue());
						}
						else if("npcTemplateNameId".equals(partName))
						{
							try 
							{
								npc.npcTemplateNameId = Integer.parseInt(valuePart.getValueAsString());
								npc.npcName = valuePart.readValue();
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
						else if("npcTemplateTitleId".equals(partName))
						{
							npc.npcTemplateTitleId = Integer.parseInt(valuePart.getValueAsString());
							npc.npcTitle = valuePart.readValue();
						}
						else if("npcVisualState".equals(partName))
						{
							npc.npcVisualState = Integer.parseInt(valuePart.getValueAsString());
						}
						else if("moveType".equals(partName))
						{
							npc.moveType = Integer.parseInt(valuePart.readValue());
						}
						else if("x".equals(partName)) 
						{
							npc.x = Float.parseFloat(valuePart.readValue());
						}
						else if("y".equals(partName)) 
						{
							npc.y = Float.parseFloat(valuePart.readValue());
						}
						else if("z".equals(partName)) 
						{
							npc.z = Float.parseFloat(valuePart.readValue());
						}
						else if("npcHeading".equals(partName))
						{
							npc.npcHeading = Integer.parseInt(valuePart.readValue());
						}
						else if("level".equals(partName)) 
						{
							npc.level = Byte.parseByte(valuePart.readValue());
						}
						else if ("objectId".equals(partName))
						{
							npc.objectId = Integer.parseInt(valuePart.readValue());
						}						
						else if ("boundRadiusFront".equals(partName))
						{
							npc.boundRadiusFront = Float.parseFloat(valuePart.readValue());
						}
						else if ("boundRadiusUpper".equals(partName))
						{
							npc.boundRadiusUpper = Float.parseFloat(valuePart.readValue());
						}
						else if("npcType".equals(partName)) 
						{
							switch(Integer.parseInt(valuePart.readValue()))
							{
								case 0:
									npc.npcType = "ATTACKABLE";
								break;
								case 2:
									npc.npcType = "NEUTRAL";
								break;
								case 8:
									npc.npcType = "AGGRESSIVE";
								break;
								case 10:
									npc.npcType = "CONSTRUCT";
								break;
								case 38:
									npc.npcType = "NON_ATTACKABLE";
								break;
								default:
									npc.npcType = valuePart.readValue();
								break;
							}
						}
						else if ("%hp".equals(partName))
						{
							stats.hpPercent = Integer.parseInt(valuePart.readValue());
						}
						else if ("maxHp".equals(partName))
						{
							stats.maxHp = Integer.parseInt(valuePart.readValue());
						}	
					}
					
					if (!objectIds.containsKey(npc.objectId))
						objectIds.put(npc.objectId, npc.npcId);
					
					if (!npcStatMap.containsKey(npc.npcId))
					{
						if (notProcessed.containsKey(npc.objectId))
						{
							NpcStats oldStats = notProcessed.get(npc.objectId);
							notProcessed.remove(npc.objectId);
							oldStats.assign(stats);
						}
						npcStatMap.put(npc.npcId, stats);
					} 
					else if (notProcessed.containsKey(npc.objectId))
					{
						NpcStats oldStats = notProcessed.get(npc.objectId);
						notProcessed.remove(npc.objectId);
						oldStats.assign(npcStatMap.get(npc.npcId));						
					}
					stats.assign(npcStatMap.get(npc.npcId));
					
					npcInfoList.add(npc);
				}
				else if("SM_ATTACK".equals(name))
				{
					List<ValuePart> valuePartList = packet.getValuePartList();
					int attackerObjectId = 0;
					int time = 0;
					
					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						
						if ("time".equals(partName))
						{
							time = Integer.parseInt(valuePart.readValue());
						}
						else if ("attackerObjectId".equals(partName))
						{
							attackerObjectId = Integer.parseInt(valuePart.readValue());
						}
						if (time != 0 && attackerObjectId != 0)
							break;
					}
					
					NpcStats stats = null;
					if (!objectIds.containsKey(attackerObjectId))
					{
						stats = new NpcStats();
						notProcessed.put(attackerObjectId, stats);
					}
					else
					{
						int npcId = objectIds.get(attackerObjectId);
						stats = npcStatMap.get(npcId);
					}
					
					stats.attack_speed = time;					
				}
			}

			for(NpcInfo npc : npcInfoList)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("<npc_template npc_id=\"");
				sb.append(npc.npcId);
				sb.append("\" level=\"");
				sb.append(npc.level);
				sb.append("\" name=\"");
				sb.append(npc.npcName);
				sb.append("\" name_id=\"");
				sb.append(npc.npcTemplateNameId);
				sb.append("\" height=\"");
				sb.append(npc.boundRadiusUpper);
				if (npc.npcTemplateTitleId > 0)
				{
					sb.append("\" title_id=\"");
					sb.append(npc.npcTemplateTitleId);
					sb.append("\" title=\"");
					sb.append(npc.npcTitle);
				}
				sb.append("\" ai=\"");
				sb.append(npc.npcType);
				if (npc.npcVisualState > 0)
				{
					sb.append("\" state=\"");
					sb.append(npc.npcVisualState);
				}
				if (npc.moveType > 0)
				{
					sb.append("\" movetype=\"");
					sb.append(npc.moveType);
				}
				sb.append("\" x=\"");
				sb.append(npc.x);
				sb.append("\" y=\"");
				sb.append(npc.y);
				sb.append("\" z=\"");
				sb.append(npc.z);
				sb.append("\" heading=\"");
				sb.append(npc.npcHeading);
				sb.append("\" front=\"");
				sb.append(npc.boundRadiusFront);
				
				if (npcStatMap.containsKey(npc.npcId))
				{
					sb.append("\">\n");
					NpcStats stats = npcStatMap.get(npc.npcId);
					sb.append("\t<stats maxHp=\"");
					sb.append(stats.maxHp);
					if (stats.attack_speed > 0)
					{
						sb.append("\" attack_speed=\"");
						sb.append(stats.attack_speed);
					}
					sb.append("\" />\n</npc_template>\n");					
				}
				else
					sb.append("\" />\n");
				
				out.write(sb.toString());
			}
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (!npcInfoList.isEmpty())
			PacketSamurai.getUserInterface().log("Export [NpcInfo] - NPC Infos Has Been Written Successful");
	}

	class NpcInfo
	{
		int npcId;
		int spawnStaticId;
		int npcTemplateNameId;
		int npcTemplateTitleId;
		int npcVisualState;
		String npcName;
		String npcTitle;
		int npcMode;
		int moveType;
		String npcType;
		float x;
		float y;
		float z;
		int npcHeading;
		byte level;
		float boundRadiusFront;
		float boundRadiusUpper;
		int objectId;
    	NpcInfoExporter.NpcStats stats;
    
    	NpcInfo() {}
	}
	
	class NpcStats
	{
		int hpPercent;
		int Hp;
		int maxHp;
		int attack_speed;
		
    	NpcStats() {}
    
		public void assign(NpcStats assignTo)
		{
			assignTo.Hp = Hp;
			int realHp = (int)((float)Hp * 100f / hpPercent);
			if (assignTo.maxHp < realHp)
				assignTo.maxHp = realHp;
			if (assignTo.attack_speed < attack_speed)
				assignTo.attack_speed = attack_speed;
		}
	}
}
