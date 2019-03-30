package com.aionemu.packetsamurai.utils.collector.data.conquestPortal;
/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;
import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.utils.collector.DataLoader;
import com.aionemu.packetsamurai.utils.collector.DataManager;

/**
 * @author CoolyT
 */

public class ConquestPortalsTool
{
	public static ConquestPortalData portals = new ConquestPortalData();
	public static TreeMap<Integer, TreeMap<Float,ConquestPortalLoc>> portalLocsByNpcId = new TreeMap<Integer,TreeMap<Float,ConquestPortalLoc>>();
	private static boolean reload = false;
	
	public static void load()
	{
		if (!(new File(DataManager.getPathConquestPortals()+"conquest_portals.xml")).exists())
			return;
		
		DataLoader portals_loader;
		try 
		{
			portals_loader = new DataLoader( DataManager.getPathConquestPortals()+"conquest_portals.xml", new ConquestPortalData());
			portals = (ConquestPortalData) portals_loader.getData();
		} 
		catch (JAXBException e1) 
		{
			PacketSamurai.getUserInterface().log(e1.toString());
		}
		
		for (ConquestPortal portal : portals.portals)
		{
			TreeMap<Float, ConquestPortalLoc> locs = new TreeMap<Float, ConquestPortalLoc>();
			for (ConquestPortalLoc loc: portal.locs)
			{
				locs.put(loc.x, loc);
			}
			portalLocsByNpcId.put(portal.npcId, locs);
		}
		PacketSamurai.getUserInterface().log("Template [ConquestPortal] - "+(reload ? "Re":"")+"Loaded: " + portalLocsByNpcId.size() + " ConquestPortals."+(DataManager.directToServer ? " (from GameServer Data)" : " (from Local Data)"));
		reload = false;
	}
	
	public static void reload()
	{
		reload  = true;
		portals = new ConquestPortalData();
		load();
	}
}

