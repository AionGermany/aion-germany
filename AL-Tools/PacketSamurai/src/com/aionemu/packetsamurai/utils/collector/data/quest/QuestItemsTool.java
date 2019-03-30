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
package com.aionemu.packetsamurai.utils.collector.data.quest;

import java.io.File;

import javax.xml.bind.JAXBException;

import javolution.util.FastList;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.utils.collector.DataLoader;
import com.aionemu.packetsamurai.utils.collector.DataManager;

/**
 * @author CoolyT
 */
public class QuestItemsTool {

    public static QuestData qd = new QuestData();
	public static FastList<Integer> questitems = FastList.newInstance();
  
    public static void load() {    	
		if (!(new File(DataManager.getPathQuests())).exists())
			return;
		
		DataLoader quest_loader;
		try 
		{
			quest_loader = new DataLoader(DataManager.getPathQuests()+"quest_data.xml", new QuestData());
			qd = (QuestData) quest_loader.getData();			
		} 
		catch (JAXBException e1) 
		{
			PacketSamurai.getUserInterface().log(e1.toString());
		}		
		PacketSamurai.getUserInterface().log("Template [Quest] - Loaded: " + qd.size() + " QuestTemplates with "+questitems.size()+" QuestItems"+(DataManager.directToServer ? " (from GameServer Data)" : " (from Local Data)"));
    }
    
    public static boolean isQuestItem(int id)
    {
    	return questitems.contains(id);
    }
}
