package com.aionemu.packetsamurai.utils.collector.data.quest;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 *  @author CoolyT
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "collect_item")
public class CollectItem {

    @XmlAttribute(name = "item_id")
    protected Integer itemId;
    
    void afterUnmarshal(Unmarshaller u, Object parent) 
    {
    	if (!QuestItemsTool.questitems.contains(itemId))
    		QuestItemsTool.questitems.add(itemId);        
    }
}
