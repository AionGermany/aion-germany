package com.aionemu.packetsamurai.utils.collector.objects.player;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javolution.util.FastList;

/**
 * @author CoolyT
 */
@XmlRootElement(name="players")
public class Players 
{
	@XmlElement(name="player")
	public FastList<Player> players = new FastList<Player>();
	
	public void addPlayer(Player player)
	{
		if (players.contains(player))
			players.add(player);
	}
}
