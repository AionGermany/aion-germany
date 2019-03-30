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
import java.util.Map.Entry;

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

public class PetFeedExporter
{
	private FastList<DataPacket> packets;
	private FastMap<Long, String> items = new FastMap<Long, String>();
	private FastMap<Long, FastList<FeedStat>> feedStats = new FastMap<Long, FastList<FeedStat>>();
	private String sessionName;
	
	public PetFeedExporter(FastList<DataPacket> packets, String sessionName) {
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse() {
		String filename = "pet_feed_" + sessionName + ".xml";
		try {
			String file = "output/Pet_Feed/"+filename;
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			
			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_UPDATE_ITEM".equals(name))
				{
					Long uniqueId = 0L;
					String itemNameId = "";
					
					List<ValuePart> valuePartList = packet.getValuePartList();
					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("uniqueId".equals(partName))
						{
							uniqueId = Long.parseLong(valuePart.readValue());
							if (items.containsKey(uniqueId)) {
								uniqueId = 0L;
								continue;
							}
						}
						else if("itemNameId".equals(partName))
						{
							itemNameId = valuePart.readValue();
						}
					}
					if (uniqueId != 0)
						items.put(uniqueId, itemNameId);
				}
			}
			
			out.write("<feeds>\n");

			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_PET".equals(name))
				{
					String actionId = "";
					String feedActionId = "";
					int feedExp = 0;
					int feedLove = 0;
					int feedCount = 0;
					int foodLeft = 0;
					Long uniqueId = 0L;
					
					List<ValuePart> valuePartList = packet.getValuePartList();
					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("actionId".equals(partName))
						{
							actionId = valuePart.readValue();
							if (!"9".equals(actionId))
							{
								actionId = "";
								continue;
							}
						}
						else if("feedActionId".equals(partName))
						{
							feedActionId = valuePart.readValue();
							if (!"1".equals(feedActionId) && !"2".equals(feedActionId))
							{
								feedActionId = "";
								continue;
							}
						} else if("feedExp".equals(partName)) {
							feedExp = (int)Long.parseLong(valuePart.readValue());
						} else if("feedLove".equals(partName)) {
							feedLove = (int)Long.parseLong(valuePart.readValue());
						} else if("feedCount".equals(partName)) {
							feedCount = (int)Long.parseLong(valuePart.readValue());
						} else if("foodLeft".equals(partName)) {
							foodLeft = (int)Long.parseLong(valuePart.readValue());
						} else if ("objectId".equals(partName)) {
							uniqueId = Long.parseLong(valuePart.readValue());
						}
					}
					if (!actionId.isEmpty() && !feedActionId.isEmpty()) {
						FeedStat feedStat = new FeedStat();
						feedStat.feedExp = (feedExp & 0xFF);
						feedStat.feedLove = (feedLove & 0xFF);
						feedStat.feedCount = (feedCount & 0xFF);
						feedStat.foodLeft = (foodLeft & 0xFF);
						FastList<FeedStat> stats;
						if (feedStats.containsKey(uniqueId))
							stats = feedStats.get(uniqueId);
						else {
							stats = new FastList<FeedStat>();
							feedStats.put(uniqueId, stats);
						}
						stats.add(feedStat);
					}
				}
			}

			StringBuilder sb = new StringBuilder();
			for(Entry<Long, FastList<FeedStat>> entry : feedStats.entrySet())
			{
				for (FeedStat stat : entry.getValue())
				{
					if (items.containsKey(entry.getKey())) {
						sb.append("\t<feed nameId=\"");
						Long value = Long.parseLong(items.get(entry.getKey()));
						sb.append((value - 1) / 2);
					} else {
						sb.append("\t<feed objectId=\"");
						sb.append(entry.getKey());					
					}
					sb.append("\" exp=\"");
					sb.append(stat.feedExp);
					sb.append("\" love=\"");
					sb.append(stat.feedLove);
					sb.append("\" count=\"");
					sb.append(stat.feedCount);
					sb.append("\" itemsLeft=\"");
					sb.append(stat.foodLeft);
					sb.append("\" />\n");
				}
			}
			out.write(sb.toString());
			out.write("</feeds>\n");
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (!feedStats.isEmpty())
			PacketSamurai.getUserInterface().log("Export [PetFeed] - Pet Feed Results Has Been Written Successful");
		
	}
	
	class Item {
		long objectId;
		int nameId;
	}
	
	class FeedStat {
		int feedExp;
		int feedLove;
		int feedCount;
		int foodLeft;
	}

}
