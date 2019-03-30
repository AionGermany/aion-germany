package com.aionemu.packetsamurai.utils.collector.objects.player;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import javolution.util.FastList;

/**
 * @author CoolyT
 */
@XmlType(name = "player")
public class Player 
{
	@XmlAttribute(name= "id")
	public int id;	
	
	@XmlAttribute(name= "name")
	public String name;
	
	@XmlAttribute(name= "gender")
	public String gender;
	
	@XmlAttribute(name= "race")
	public String race;
	
	@XmlAttribute(name= "class")
	public String playerClass;
	
	@XmlAttribute(name= "level")
	public int level;
	
	@XmlElement(name = "appearance")
	public PlayerAppearance appearance = new PlayerAppearance();
	
	@XmlElement(name = "items")
	public FastList<PlayerItem> items = new FastList<PlayerItem>();
}
