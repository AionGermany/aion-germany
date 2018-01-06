package com.aionemu.gameserver.model.templates.appearances;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import javolution.util.FastList;

/**
 * @author CoolyT
 */
@XmlType(name = "player")
public class PlayerApp {

	@XmlAttribute(name = "name")
	public String name;

	@XmlAttribute(name = "gender")
	public String gender;

	@XmlAttribute(name = "race")
	public String race;

	@XmlAttribute(name = "level")
	public int level;

	@XmlAttribute(name = "class")
	public String playerClass;

	@XmlElement(name = "appearance")
	public PlayerAppearanceTemplate appearance = new PlayerAppearanceTemplate();

	@XmlElement(name = "items")
	public FastList<PlayerItem> items = new FastList<PlayerItem>();
}
