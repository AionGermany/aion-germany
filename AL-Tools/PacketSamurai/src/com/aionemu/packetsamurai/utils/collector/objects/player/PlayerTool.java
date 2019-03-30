package com.aionemu.packetsamurai.utils.collector.objects.player;

import java.io.File;

import javax.xml.bind.JAXBException;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.utils.collector.DataLoader;
import com.aionemu.packetsamurai.utils.collector.DataManager;
import javolution.util.FastMap;

/**
 * @author CoolyT
 */
public class PlayerTool 
{
	public static Players players = new Players();
	public static FastMap<String, Player> playerByName = new FastMap<String,Player>();
	private static boolean reload = true;
	
	public static void load()
	{
		
		if (!(new File(DataManager.pathPlayerAppearances+"player_appearances.xml")).exists())
			return;
		
		players = new Players();
		playerByName = new FastMap<String,Player>();
		DataLoader appearance_loader;
		try 
		{
			appearance_loader = new DataLoader( DataManager.getPathPlayerAppearances()+"player_appearances.xml", new Players());
			players = (Players) appearance_loader.getData();
		} 
		catch (JAXBException e1) 
		{
			PacketSamurai.getUserInterface().log(e1.toString());
		}
		
		for (Player player : players.players)
		{
			playerByName.put(player.name.toLowerCase(), player);
		}
		PacketSamurai.getUserInterface().log("Player [Appearance] - "+(reload ? "Re":"")+"Loaded: " + playerByName.size() + " Player Appearances."+(DataManager.directToServer ? " (from GameServer Data)" : " (from Local Data)"));
		reload = false;
	}
	
	public static void reload()
	{
		reload   = true;
		load();
	}
}
