package com.aionemu.gameserver.spawnengine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldPosition;

import javolution.util.FastList;

/**
 * @author CoolyT
 */
public class ConquestSpawnManager {

	private static final Logger log = LoggerFactory.getLogger(ConquestSpawnManager.class);

	private static Map<WorldPosition, FastList<SpawnTemplate>> spawnsByPosition = new HashMap<WorldPosition, FastList<SpawnTemplate>>();
	private static Map<Integer, FastList<WorldPosition>> portals = new HashMap<Integer, FastList<WorldPosition>>();

	private static FastList<VisibleObject> spawned = new FastList<VisibleObject>();

	public static void spawnAll() {
		int spawns = 0;
		for (Entry<WorldPosition, FastList<SpawnTemplate>> spawnpos : spawnsByPosition.entrySet()) {
			if (spawnpos.getValue().size() == 0)
				continue;

			int index = Rnd.get(0, (spawnpos.getValue().size() - 1));

			SpawnTemplate st = spawnpos.getValue().get(index);
			spawns++;
			spawn(st);
		}
		GameServer.log.info("[ConquestSpawnManager] Spawned " + spawns + " ConquestNpcs");
		// writeTemplate();
	}

	public static void spawnByLoc(WorldPosition pos) {
		// boolean found = false;
		for (Entry<WorldPosition, FastList<SpawnTemplate>> spawnpos : spawnsByPosition.entrySet()) {
			if (spawnpos.getKey().getX() == pos.getX() && spawnpos.getKey().getY() == pos.getY() && spawnpos.getKey().getZ() == pos.getZ()) {
				// found = true;
				int index = Rnd.get(0, (spawnpos.getValue().size() - 1));
				final SpawnTemplate st = spawnpos.getValue().get(index);
				int respawntime = 295;
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(st);
					}
				}, respawntime * 1000);
			}
		}
		// if (!found) log.warn("Didn't found Location for "+pos.toString());
	}

	public static void deSpawnByLoc(WorldPosition pos) {
		for (VisibleObject spawn : spawned) {
			if (spawn.getPosition().getX() == pos.getX() && spawn.getPosition().getY() == pos.getY() && spawn.getPosition().getZ() == pos.getZ()) {
				if (spawn != null && spawn.isSpawned()) {
					spawn.getController().delete();
					spawned.remove(spawn);
					break;
				}
			}
		}
	}

	private static void spawn(SpawnTemplate st) {
		st.setRespawnTime(0);
		VisibleObject visibleObject = SpawnEngine.spawnObject(st, 1);
		spawned.add(visibleObject);
	}

	public static void addConquestSpawnTemplate(SpawnGroup2 sg) {
		for (SpawnTemplate spawn : sg.getSpawnTemplates()) {
			WorldPosition pos = new WorldPosition(spawn.getWorldId());
			for (Entry<WorldPosition, FastList<SpawnTemplate>> set : spawnsByPosition.entrySet()) {
				if (spawn.getX() == set.getKey().getX() && spawn.getY() == set.getKey().getY() && spawn.getZ() == set.getKey().getZ()) {
					pos = set.getKey();
				}
			}

			if (spawnsByPosition.containsKey(pos))
				spawnsByPosition.get(pos).add(spawn);
			else {
				FastList<SpawnTemplate> stList = new FastList<SpawnTemplate>();
				pos.setXYZH(spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getHeading());
				spawnsByPosition.put(pos, stList);
			}
		}
	}

	public static void writeTemplate() {
		StringBuilder spots_gelk = new StringBuilder();
		StringBuilder spots_ingg = new StringBuilder();
		StringBuilder template_gelk = new StringBuilder();
		StringBuilder template_ingg = new StringBuilder();
		FastList<Integer> npc_gelk = new FastList<Integer>();
		FastList<Integer> npc_ingg = new FastList<Integer>();

		for (int i = 236307; i <= 236362; i++) // Npc Inggison
		{
			npc_ingg.add(i);
		}

		for (int i = 236363; i <= 236418; i++) // Npc Gelkmaros
		{
			npc_gelk.add(i);
		}

		for (WorldPosition wpos : spawnsByPosition.keySet()) {
			if (wpos.getX() == 0.0 && wpos.getY() == 0.0)
				continue;

			if (wpos.getMapId() == 220070000) {
				spots_gelk.append("\t\t\t<spot h=\"" + wpos.getHeading() + "\" x=\"" + wpos.getX() + "\" y=\"" + wpos.getY() + "\" z=\"" + wpos.getZ() + "\"/>\r\n");
			}
			else if (wpos.getMapId() == 210050000) {
				spots_ingg.append("\t\t\t<spot h=\"" + wpos.getHeading() + "\" x=\"" + wpos.getX() + "\" y=\"" + wpos.getY() + "\" z=\"" + wpos.getZ() + "\"/>\r\n");
			}
		}
		spots_gelk.append("\t\t</spawn>\r\n");
		spots_ingg.append("\t\t</spawn>\r\n");

		template_gelk.append("\r\n<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n");
		template_gelk.append("<spawns>\r\n");
		template_gelk.append("\t<spawn_map map_id=\"220070000\">\r\n");

		// SpawnTemplate Gelkmaros
		for (int id : npc_gelk) {
			template_gelk.append("\t\t<!-- " + DataManager.NPC_DATA.getNpcTemplate(id).getName() + " -->\r\n");
			template_gelk.append("\t\t<spawn npc_id=\"" + id + "\" respawn_time=\"295\" handler=\"CONQUEST\">\r\n");
			template_gelk.append(spots_gelk);
		}
		template_gelk.append("\t</spawn_map>\r\n");
		template_gelk.append("</spawns>\r\n");

		template_ingg.append("\r\n<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n");
		template_ingg.append("<spawns>\r\n");
		template_ingg.append("\t<spawn_map map_id=\"210050000\">\r\n");

		// SpawnTemplate Inggison
		for (int id : npc_ingg) {
			template_ingg.append("\t\t<!-- " + DataManager.NPC_DATA.getNpcTemplate(id).getName() + " -->\r\n");
			template_ingg.append("\t\t<spawn npc_id=\"" + id + "\" respawn_time=\"295\" handler=\"CONQUEST\">\r\n");
			template_ingg.append(spots_ingg);
		}
		template_ingg.append("\t</spawn_map>\r\n");
		template_ingg.append("</spawns>\r\n");

		log.warn(template_gelk.toString());
		log.warn("------------------------------------------------------------------------------------------");
		log.warn(template_ingg.toString());
	}

	public static FastList<WorldPosition> getLocationsByNpcId(int npcId) {
		if (portals.containsKey(npcId))
			return portals.get(npcId);
		return new FastList<WorldPosition>();
	}
}
