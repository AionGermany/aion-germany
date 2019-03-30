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

public class NpcSkillExporter 
{
	private List<DataPacket> packets;
	private String sessionName;
	private FastMap<Integer, NpcSkillUse> npcInfoList = new FastMap<Integer, NpcSkillUse>();
	private FastMap<Integer, Integer> objectIds = new FastMap<Integer, Integer>();
	
	public NpcSkillExporter(List<DataPacket> packets, String sessionName) 
	{
		super();
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse()
	{
		String fileName = "npc_skills_" + sessionName + ".xml";

		try
		{
			String file = "output/Npc_Skill/"+fileName;
			BufferedWriter out = new BufferedWriter(new FileWriter(file));

			for (DataPacket packet : packets)
			{
				String name = packet.getName();
				if ("SM_NPC_INFO".equals(name))
				{
					List<ValuePart> valuePartList = packet.getValuePartList();
					NpcSkillUse skillUse = new NpcSkillUse();
					int objectId = 0;
					
					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							skillUse.npcId = Integer.parseInt(valuePart.readValue());
						}
						else if ("objectId".equals(partName))
						{
							objectId = Integer.parseInt(valuePart.readValue());
						}
					}
					
					if (!objectIds.containsKey(objectId))
						objectIds.put(objectId, skillUse.npcId);
				}
				else if ("SM_CASTSPELL_END".equals(name))
				{
					List<ValuePart> valuePartList = packet.getValuePartList();
					int effectorId = 0;
					int skillId = 0;
					short level = 0;
					short hpAtUse = 0;
					
					for (ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if ("effectorId".equals(partName))
						{
							effectorId = Integer.parseInt(valuePart.readValue());
						}
						else if ("spellId".equals(partName))
						{
							skillId = Integer.parseInt(valuePart.getValueAsString());
						}
						else if ("level".equals(partName))
						{
							level = Short.parseShort(valuePart.readValue());
						}
						else if ("attackerHp".equals(partName))
						{
							hpAtUse = Short.parseShort(valuePart.readValue());
						}
						if (effectorId != 0 && skillId != 0 && level != 0 && hpAtUse != 0)
							break;
					}
					
					if (!objectIds.containsKey(effectorId))
						continue;
					
					int npcId = objectIds.get(effectorId);
					NpcSkillUse skillUse = null;
					
					if (npcInfoList.containsKey(npcId))
						skillUse = npcInfoList.get(npcId);
					else
					{
						skillUse = new NpcSkillUse();
						skillUse.npcId = npcId;
						npcInfoList.put(npcId, skillUse);
					}
					
					SkillData skillData = null;
					if (skillUse.skillsUsed.containsKey(skillId))
						skillData = skillUse.skillsUsed.get(skillId);
					else
					{
						skillData = new SkillData();
						skillData.skillId = skillId;
						skillUse.skillsUsed.put(skillId, skillData);
					}
					
					SkillUseStat stat = null;
					if (skillData.skillsByLevel.containsKey(level))
						stat = skillData.skillsByLevel.get(level);
					else
					{
						stat = new SkillUseStat();
						stat.level = level;
						skillData.skillsByLevel.put(level, stat);
					}
					
					stat.useCount++;
					if (stat.maxHp < hpAtUse)
						stat.maxHp = hpAtUse;
					if (stat.minHp > hpAtUse)
						stat.minHp = hpAtUse;
				}
			}

			StringBuilder sb = new StringBuilder();
			
			for (NpcSkillUse entry : npcInfoList.values())
			{
				sb.append("\t<npcskills npcid=\"");
				sb.append(entry.npcId);
				sb.append("\">\n");

				int useCount = 0;
				for (SkillData skillData : entry.skillsUsed.values())
				{
					for (SkillUseStat stat : skillData.skillsByLevel.values())
					{
						// add distribution 5% error
						if (stat.maxHp + 5 > 100)
							stat.maxHp = 100;
						else
							stat.maxHp += 5;
						
						if (stat.minHp - 5 < 0)
							stat.minHp = 0;
						else
							stat.minHp -= 5;
						
						useCount += stat.useCount;
					}
					
				}
				
				int i = 0;
				for (SkillData skillData : entry.skillsUsed.values())
				{
					for (SkillUseStat stat : skillData.skillsByLevel.values())
					{
						sb.append("\t\t<skillid=\"");
						sb.append(Integer.toString(skillData.skillId));
						sb.append("\" skilllevel=\"");
						sb.append(Short.toString(stat.level));
						sb.append("\" probability=\"");
						sb.append(Integer.toString(Math.round((float)stat.useCount * 100 / useCount)));
						sb.append("\" maxhp=\"");
						sb.append(Short.toString(stat.maxHp));
						sb.append("\" minhp=\"");
						sb.append(Short.toString(stat.minHp));
						sb.append("\" id=\"");
						sb.append(Integer.toString(++i));
						sb.append("\"/>\n");
					}
				}
				
				sb.append("\t</npcskills>\n");
			}
			
			out.write(sb.toString());
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if (!npcInfoList.isEmpty())
			PacketSamurai.getUserInterface().log("Export [NpcSkill] - NPC Skills Has Been Written Successfull");
	}
	
	class NpcSkillUse
	{
		int npcId;
		
		FastMap<Integer, SkillData> skillsUsed = new FastMap<Integer, SkillData>();
	}
	
	class SkillData
	{
		int skillId;
		
		FastMap<Short, SkillUseStat> skillsByLevel = new FastMap<Short, SkillUseStat>();
	}
	
	class SkillUseStat
	{
		short level;
		short maxHp;
		short minHp;
		int useCount;
		
		float probability;
	}
}
