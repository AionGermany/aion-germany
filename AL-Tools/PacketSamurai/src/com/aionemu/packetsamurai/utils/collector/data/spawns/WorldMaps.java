package com.aionemu.packetsamurai.utils.collector.data.spawns;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javolution.util.FastList;

@XmlRootElement( name = "world_maps")
public class WorldMaps 
{
	@XmlElement( name = "map")
	public FastList<WorldMap> maps = new FastList<WorldMap>();
}
