/**
 * 
 */
package com.aionemu.packetsamurai.utils.collector.data.quest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CoolyT
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "quest")
public class QuestTemplate 
{
	    @XmlElement(name = "collect_items")
	    protected CollectItems collectItems;
}
