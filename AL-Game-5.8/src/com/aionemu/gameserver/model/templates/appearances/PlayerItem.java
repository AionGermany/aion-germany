package com.aionemu.gameserver.model.templates.appearances;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CoolyT
 */
@XmlType(name = "items")
public class PlayerItem {

	@XmlAttribute()
	public int itemTemplateId;
	@XmlAttribute()
	public int itemColor;
}
