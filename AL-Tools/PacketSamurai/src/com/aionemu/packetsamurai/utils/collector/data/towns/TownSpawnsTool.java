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
package com.aionemu.packetsamurai.utils.collector.data.towns;

import javax.xml.bind.JAXBException;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.utils.collector.DataLoader;
import com.aionemu.packetsamurai.utils.collector.JAXBExtractor;

/**
 * 
 * @author ViAl
 *
 */
public class TownSpawnsTool {
	
	public static TownSpawnsData TOWN_SPAWNS_DATA;
	
	public static void load() {
		try {
			DataLoader loader = new DataLoader("./data/town_spawns/town_spawns.xml", new TownSpawnsData());
			TOWN_SPAWNS_DATA = (TownSpawnsData) loader.getData();
			PacketSamurai.getUserInterface().log("Spawns [Towns] - Loaded: " +TOWN_SPAWNS_DATA.getSpawnsCount()+" spawns"+ " from "+TOWN_SPAWNS_DATA.towns.size()+" towns");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void save() {
		JAXBExtractor extractor = new JAXBExtractor("./data/town_spawns/town_spawns.xml", TOWN_SPAWNS_DATA);
		extractor.extract();
		PacketSamurai.getUserInterface().log("Spawn [Towns] - Saved: " +TOWN_SPAWNS_DATA.getSpawnsCount()+" spawns"+ " from "+TOWN_SPAWNS_DATA.towns.size()+" towns");
	}
	
	public static void add(int townId, int worldId, int npcId, float x, float y, float z, int h) {
		Spawn spawn = new Spawn(npcId, x, y, z, h);
		for(TownSpawns town : TOWN_SPAWNS_DATA.towns) {
			if(town.id == townId && town.world_id == worldId) {
				for(Spawn s : town.spawns) {
					if(s.npc_id == npcId)
						return;
				}
				town.spawns.add(spawn);
				return;
			}
		}
		TownSpawns town = new TownSpawns();
		town.id = townId;
		town.world_id = worldId;
		town.spawns.add(spawn);
		TOWN_SPAWNS_DATA.towns.add(town);
		PacketSamurai.getUserInterface().log("Spawn [Towns] - Found new Town spawn: town id: " +townId+" , npcId:"+npcId);
	}
}
