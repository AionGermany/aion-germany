/** * This file is part of Aion-Lightning <aion-lightning.org>.
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
package com.aionemu.packetsamurai.utils.collector.data.gather;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.utils.collector.DataLoader;
import com.aionemu.packetsamurai.utils.collector.data.gather.GatherableData;
import com.aionemu.packetsamurai.utils.collector.data.gather.GatherableTemplate;

/**
 * @author CoolyT
 */
public class GathersTool {

    public static GatherableData gathers = new GatherableData();
	public static Map<Integer, GatherableTemplate> gather_data = new HashMap<Integer, GatherableTemplate>();
	private static boolean reload = false;
  
    public static void load() {    	
		if (!(new File("data/gatherable_templates/gatherable_templates.xml")).exists())
			return;
		
		DataLoader appearance_loader;
		try 
		{
			appearance_loader = new DataLoader("data/gatherable_templates/gatherable_templates.xml", new GatherableData());
			gathers = (GatherableData) appearance_loader.getData();
			
			for (GatherableTemplate g : gathers.gatherables)
			{
				gather_data.put(g.id, g);
			}
			PacketSamurai.getUserInterface().log("Template [Gather] - "+(reload ? "Re":"")+"Loaded: " + gather_data.size() + " Gatherables");
			reload = false;
		} 
		catch (JAXBException e1) 
		{
			PacketSamurai.getUserInterface().log(e1.toString());
		}
    }
    
    public static GatherableTemplate getGatherTemplate(int npcId)
    {
    	GatherableTemplate temp = new GatherableTemplate();
    	
    	if (gather_data == null)
    		return temp;
    	
    	if (!gather_data.containsKey(npcId))
	    	PacketSamurai.getUserInterface().log("Template [Gather] - Warning ! : There is no GatherableTemplate found for NpcId :" +npcId);
    	else temp = gather_data.get(npcId);
    	
    	return temp;
    }
  
	public static void reload()
	{
		reload  = true;
		gathers = new GatherableData();
		load();
	}
    
    public static void save() 
    {
    	//nothing to do :)
    }
}
