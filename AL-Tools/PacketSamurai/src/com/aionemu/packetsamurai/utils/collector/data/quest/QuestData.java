/**
 * 
 */
package com.aionemu.packetsamurai.utils.collector.data.quest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author CoolyT
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "quests")
public class QuestData 
{
    @XmlElement(name = "quest", required = true)
    protected List<QuestTemplate> questData;
    
	public int size()
    {
    	return questData.size();
    }
}
