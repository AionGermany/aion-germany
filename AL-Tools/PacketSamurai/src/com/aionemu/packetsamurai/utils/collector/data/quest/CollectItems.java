/**
 * 
 */
package com.aionemu.packetsamurai.utils.collector.data.quest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CoolyT
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "collect_items")
public class CollectItems 
{
    @XmlElement(name = "collect_item")
    protected List<CollectItem> collectItem;

	public List<CollectItem> getCollectItem() {
		return collectItem;
	}    
}
