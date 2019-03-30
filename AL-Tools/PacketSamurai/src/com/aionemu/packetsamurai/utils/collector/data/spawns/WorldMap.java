package com.aionemu.packetsamurai.utils.collector.data.spawns;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType( name="map")
public class WorldMap 
{
	@XmlAttribute( name = "id")
	public int id;
	
	@XmlAttribute( name = "name")
	public String name;	
}
