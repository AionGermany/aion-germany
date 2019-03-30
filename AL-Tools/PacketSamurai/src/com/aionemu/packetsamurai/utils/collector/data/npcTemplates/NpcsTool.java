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
package com.aionemu.packetsamurai.utils.collector.data.npcTemplates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.valuereader.ClientStringReader;
import com.aionemu.packetsamurai.utils.collector.DataManager;

public class NpcsTool {

    private static Map<Integer, NpcTemplate> data = new HashMap<Integer, NpcTemplate>();
 
    public static void load() {

    	try {
        	JAXBContext jc = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.npcTemplates");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
      
            NpcTemplates collection = (NpcTemplates)unmarshaller.unmarshal(new File(DataManager.getPathNpcTemplate()+"npc_templates.xml"));
            for (NpcTemplate template : collection.getNpcTemplate()) {
                data.put(template.npcId, template);
            }
            PacketSamurai.getUserInterface().log("Template [Npc] - Loaded: " + data.size() + " Npcs"+(DataManager.directToServer ? " (from GameServer Data)" : " (from Local Data)"));
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
    public static NpcTemplate getNpcTemplate(int npcId)
    {
    	NpcTemplate temp = new NpcTemplate();
    	
    	if (data == null)
    		return temp;
    	
    	if (!data.containsKey(npcId))
	    	PacketSamurai.getUserInterface().log("Template [Npc] - Warning ! : There is no NpcTemplate found for NpcId :" +npcId);
    	else temp = data.get(npcId);
    	
    	return temp;
    }
  
    public static void save() {
        ObjectFactory objFactory = new ObjectFactory();
        NpcTemplates collection = objFactory.createNpcTemplates();
        List<NpcTemplate> templateList = collection.getNpcTemplate();
        for (int x = 200000; x < 1000000; x++) {
            NpcTemplate template = (NpcTemplate)data.get(x);
            if (template != null) {
                String npcName = ClientStringReader.getStringById(template.getNameId());
                template.setName(npcName);
                templateList.add(template);
            }
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.npcTemplates");
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            marshaller.marshal(collection, new FileOutputStream(DataManager.pathNpcTemplate+"npc_templates.xml"));
        }
        catch (PropertyException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        PacketSamurai.getUserInterface().log("Template [Npc] - Saved: " + data.size() + " Npc Templates");
    }
  
    public static void update(int npcId, int level, int hp, int titleId, int npcType, int nameId) {
        NpcTemplate template = (NpcTemplate)data.get(Integer.valueOf(npcId));
        if (template != null) {
        	
        	if (template.npcId >= 833287 && template.npcId <= 833450)
        	{
        		//PacketSamurai.getUserInterface().log("Template [Npc] - Skipping template change for Npc : "+template.getName()+" - "+template.getNpcId());
        		return;
        	}
            if ((titleId > 0) && (titleId != template.getTitleId())) {
                PacketSamurai.getUserInterface().log("Template [Npc] -  "+template.getName() + " (id: "+template.getNpcId()+ ") changed title from " + (template.getTitleId() > 0 ? template.getTitleId() : 0) + " to " + titleId);
                template.setTitleId(Integer.valueOf(titleId));
            }
            if(nameId > 0 && template.getNameId() != nameId) {
                PacketSamurai.getUserInterface().log("Template [Npc] -  "+template.getName() + " (id: "+template.getNpcId()+ ") changed nameId from " + (template.getNameId() > 0 ? template.getNameId() : 0) + " to " + nameId);
                template.setNameId(nameId);
            }
            if (template.getStats().getMaxHp() != hp) {
                PacketSamurai.getUserInterface().log("Template [Npc] -  "+template.getName() + " (id: "+template.getNpcId()+ ") changed Hp from " + template.getStats().getMaxHp() + " to " + hp);
                template.getStats().setMaxHp(hp);
            }
            if (template.getLevel() != level) {
                PacketSamurai.getUserInterface().log("Template [Npc] -  "+template.getName() + " (id: "+template.getNpcId()+ ") changed level from " + template.getLevel() + " to " + level);
                template.setLevel(level);
            }
        }
        else {
            PacketSamurai.getUserInterface().log("Template [Npc] - There is o template found for NpcId: " + npcId + ". npc_template reparse is needed.");
        }
    }
}
