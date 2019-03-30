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


package com.aionemu.packetsamurai.utils.collector;

import java.io.File;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.Util;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.utils.collector.data.conquestPortal.ConquestPortalsTool;
import com.aionemu.packetsamurai.utils.collector.data.gather.GathersTool;
import com.aionemu.packetsamurai.utils.collector.data.houses.CollectedHouseDataLoader;
import com.aionemu.packetsamurai.utils.collector.data.npcData.CollectedNpcDataLoader;
import com.aionemu.packetsamurai.utils.collector.data.npcMoves.NpcMoveDataSaver;
import com.aionemu.packetsamurai.utils.collector.data.npcTemplates.NpcsTool;
import com.aionemu.packetsamurai.utils.collector.data.npcskills.NpcSkillsTool;
import com.aionemu.packetsamurai.utils.collector.data.quest.QuestItemsTool;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnsTool;
import com.aionemu.packetsamurai.utils.collector.data.towns.TownSpawnsTool;
import com.aionemu.packetsamurai.utils.collector.data.windstreams.WindstreamsTool;
import com.aionemu.packetsamurai.utils.collector.objects.player.PlayerTool;

public class DataManager {

	public static boolean directToServer = ((Main)PacketSamurai.getUserInterface()).getDataFromGameServer();
	public static String pathNpcTemplate = directToServer ? "../../../../AL-Game/data/static_data/npcs/" : "./data/npc_templates/";
	public static String pathNpcDrops = directToServer ? "../../../../AL-Game/data/static_data/npc_drops/" : "./data/npc_drops/";
	public static String pathConquestPortals = directToServer ? "../../../../AL-Game/data/static_data/portals/" : "./data/conquest_portals/";
	public static String pathGatherSpawns = directToServer ? "../../../../AL-Game/data/static_data/spawns/gather/" : "./data/spawns/gather/";
	public static String pathSpawns = directToServer ? "../../../../AL-Game/data/static_data/spawns/" : "./data/spawns/";
	public static String pathQuests = directToServer ? "../../../../AL-Game/data/static_data/quest_data/" : "./data/quest/";
	public static String pathPlayerAppearances = directToServer ? "../../../../AL-Game/data/static_data/" : "./data/appearances/";
	public static String pathWorldMaps = directToServer ? "../../../../AL-Game/data/static_data/" : "./data/";
	public static String pathWindstreams = directToServer ? "../../../../AL-Game/data/static_data/windstreams/" : "./data/windstreams/";
	
	public static void load() {

		directToServer = ((Main)PacketSamurai.getUserInterface()).getDataFromGameServer();

		Util.drawTitle("StaticData Templates");
		NpcsTool.load();
		GathersTool.load();
		ConquestPortalsTool.load();
		SpawnsTool.load();
		CollectedNpcDataLoader.load();
		NpcSkillsTool.load();
		TownSpawnsTool.load();
		CollectedHouseDataLoader.load();
		PlayerTool.load();
		QuestItemsTool.load();
		WindstreamsTool.load();

		//Create output directories, if they don't exists
		File x;
		//if ((x = new File("output/Portals_Conquest")).mkdirs())
			//PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Spawn")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Loot")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_ObjId")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Position")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Shout")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Static")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");		
		if ((x = new File("output/Npc_Skill")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_State")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Test")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Title")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Walker")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Rift_spawn")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Pet_Feed")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		if ((x = new File("output/Npc_Info")).mkdirs())
			PacketSamurai.getUserInterface().log("System [FirstUse] - Created Directory : ["+x.getAbsolutePath()+"]");
		Util.drawTitle("");
	}

	public static String getPathNpcTemplate() {
		return directToServer ? "../../../../AL-Game/data/static_data/npcs/" : "./data/npc_templates/";
	}

	public static String getPathNpcDrops() {
		return pathNpcDrops = directToServer ? "../../../../AL-Game/data/static_data/npc_drops/" : "./data/npc_drops/";
	}

	public static String getPathConquestPortals() {
		return pathConquestPortals = directToServer ? "../../../../AL-Game/data/static_data/portals/" : "./data/conquest_portals/";
	}

	public static String getPathGatherSpawns() {
		return pathGatherSpawns = directToServer ? "../../../../AL-Game/data/static_data/spawns/gather/" : "./data/spawns/gather/";

	}

	public static String getPathSpawns() {
		return pathSpawns = directToServer ? "../../../../AL-Game/data/static_data/spawns/" : "./data/spawns/";
	}

	public static String getPathPlayerAppearances() {
		return pathPlayerAppearances = directToServer ? "../../../../AL-Game/data/static_data/" : "./data/appearances/";
	}

	public static String getPathWorldMaps() {
		return pathWorldMaps = directToServer ? "../../../../AL-Game/data/static_data/" : "./data/";
	}
	public static String getPathQuests() {
		return pathQuests = directToServer ? "../../../../AL-Game/data/static_data/quest_data/" : "./data/quest/";
	}

	public static String getPathWindstreams() {
		return directToServer ? "../../../../AL-Game/data/static_data/windstreams/" : "./data/windstreams/";
	}
	public static void save() {
		NpcSkillsTool.save();
		CollectedNpcDataLoader.save();
		NpcMoveDataSaver.save();
		CollectedHouseDataLoader.save();
		TownSpawnsTool.save();
    	NpcsTool.save();
		WindstreamsTool.save();
	}

}
