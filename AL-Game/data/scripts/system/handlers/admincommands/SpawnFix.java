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
package admincommands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.ScheduledFuture;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.SpawnsData2;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.Spawn;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author CoolyT
 */
public class SpawnFix extends AdminCommand {

	static Collection<SpawnTemplate> spawns = new ArrayList<SpawnTemplate>();
	static Collection<Npc> walkers = new ArrayList<Npc>();
	static TreeMap<Integer, SpawnGroup2> spawnGroups = new TreeMap<Integer, SpawnGroup2>();
	static volatile boolean canceled = false;
	static volatile boolean isRunning = false;
	static int counter = 1;
	static int worldId = -1;
	static Player runner = null;
	static ScheduledFuture<?> thread = null;
	static boolean wasInvul = true;
	static long startTime = 0;
	static int books = 0;
	static String worldName;

	public SpawnFix() {
		super("spawnfix");
	}

	@Override
	public void execute(final Player admin, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, "Syntax : //spawnfix <start> || <cancel>");
			return;
		}

		if (isRunning && runner != null && !admin.equals(runner)) {
			PacketSendUtility.sendMessage(admin, "Someone is already running this command!");
			return;
		}

		if ("cancel".equals(params[0])) {
			if (isRunning) {
				PacketSendUtility.sendMessage(admin, "Canceled.");
				canceled = true;
				thread.cancel(true);
			}
		}
		else if ("start".equals(params[0])) {
			startTime = System.currentTimeMillis();
			go(admin);
		}
		else
			onFail(admin, "");
	}

	public static void writeXmlFile(SpawnsData2 sd) {

		int spawns = 0;
		int spots = 0;
		StringBuilder template_sb = new StringBuilder();
		// Header
		template_sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<!-- Z-Fixed Spawnfile for AionGer 4.9 - [" + new Date() + "] -->\r\n");
		template_sb.append("<spawns>\r\n");
		template_sb.append("\t<!-- " + worldName + " -->\r\n");
		template_sb.append("\t<spawn_map map_id=\"" + worldId + "\">\r\n");
		for (SpawnGroup2 spawn : sd.getSpawnsByWorldId(worldId)) {
			NpcTemplate npc = null;
			ItemTemplate staticSpawn = null;

			if (spawn.getNpcId() > 999999) // Static Objects
				staticSpawn = DataManager.ITEM_DATA.getItemTemplate(spawn.getNpcId());
			else
				npc = DataManager.NPC_DATA.getNpcTemplate(spawn.getNpcId());

			if (npc == null && staticSpawn == null) {
				GameServer.log.info("missing Template for Npc :" + spawn.getNpcId());
				continue;
			}
			if (npc != null)
				template_sb.append("\t\t<!-- " + npc.getName() + " ||| " + npc.getDesc() + " -->\r\n");
			if (staticSpawn != null)
				template_sb.append("\t\t<!-- StaticObject: " + staticSpawn.getName() + " ||| " + staticSpawn.getNamedesc() + " -->\r\n");
			template_sb.append("\t\t<spawn npc_id=\"" + spawn.getNpcId() + "\" respawn_time=\"" + (spawn.getRespawnTime() + "\""));
			template_sb.append((spawn.getHandlerType() != null) ? " handler=\"" + spawn.getHandlerType().name().toUpperCase() + "\"" : "");
			template_sb.append((spawn.getDifficultId() > 0) ? " difficult_id=\"" + spawn.getDifficultId() + "\"" : "");
			template_sb.append(">\r\n");
			if (spawn.getSpawnTemplates().size() > 1)
				template_sb.append("\t\t\t<!-- " + spawn.getSpawnTemplates().size() + " Spots -->\r\n");

			for (SpawnTemplate st : spawn.getSpawnTemplates()) {
				spots++;
				template_sb.append("\t\t\t<spot x=\"" + st.getX() + "\" y=\"" + st.getY() + "\" z=\"" + st.getZ() + "\" h=\"" + st.getHeading() + "\"");
				if (st.getWalkerId() != null && !st.getWalkerId().isEmpty())
					template_sb.append(" walker_id=\"" + st.getWalkerId() + "\"");
				if (st.hasRandomWalk())
					template_sb.append(" random_walk=\"" + st.getRandomWalk() + "\"");
				if (st.getStaticId() > 0)
					template_sb.append(" static_id=\"" + st.getStaticId() + "\"");
				if (st.getWalkerIndex() > 0)
					template_sb.append(" walker_index=\"" + st.getWalkerIndex() + "\"");
				if (st.getFly() > 0)
					template_sb.append(" fly=\"" + st.getFly() + "\"");
				if (st.isTemporarySpawn()) {
					template_sb.append(">\r\n\t\t\t\t<temporary_spawn spawn_time=\"" + st.getTemporarySpawn().getSpawnTime() + "\" despawn_time=\"" + st.getTemporarySpawn().getDespawnTime() + "\"/>\r\n");
					template_sb.append("\t\t\t</spot>\r\n");
				}
				else
					template_sb.append("/>\r\n");
			}
			template_sb.append("\t\t</spawn>\r\n");
			spawns++;
		}
		template_sb.append("\t</spawn_map>\r\n");
		template_sb.append("</spawns>\r\n");

		String filename = worldId + "_" + worldName + ".xml";
		try {
			File directory;
			if (!(directory = new File("./data/static_data/spawns/Npcs/New/")).exists())
				directory.mkdirs();
			BufferedWriter out = new BufferedWriter(new FileWriter("./data/static_data/spawns/Npcs/New/" + filename));
			out.write(template_sb.toString());
			out.close();
		}
		catch (IOException e) {
			GameServer.log.info("[SpawnFix] Error: " + e.toString());
		}
		GameServer.log.info("[SpawnFix] Written " + spawns + "Npc's with " + spots + " Spots to File: ./New/" + filename);
	}

	public static void go(final Player admin) {
		final SpawnsData2 spawnData = DataManager.SPAWNS_DATA2;// .clone();
		worldId = admin.getWorldId();
		worldName = World.getInstance().getWorldMap(worldId).getName();
		final String mapName = worldName;
		if (World.getInstance().getWorldMap(admin.getWorldId()).getWorldMapInstanceById(admin.getInstanceId()).getNpcs() == null) {
			GameServer.log.info("[SpawnFix] Error: Cannot get WorldMap for WorldId : " + worldId);
		}
		final Collection<Npc> npcs = World.getInstance().getWorldMap(admin.getWorldId()).getWorldMapInstanceById(admin.getInstanceId()).getNpcs();
		final float jumpHeight = 0.5F;
		thread = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				wasInvul = admin.isInvul();
				admin.setInvul(true);
				isRunning = true;
				runner = admin;

				try {
					for (Npc npc : npcs) {
						if (canceled || admin.isInState(CreatureState.DEAD)) {
							reset();
							thread.cancel(true);
							return;
						}

						if (npc == null) {
							GameServer.log.info("Npc is null .. Counter: " + counter);
							counter++;
							continue;
						}

						SpawnTemplate spawn = npc.getSpawn();
						if (spawn == null || !npc.isSpawned()) {
							GameServer.log.info("SpawnTemplate is null or Npc not spawned .. NpcId: " + npc.getNpcId());
							counter++;
							continue;
						}
						if (spawn.getWalkerId() != null && !spawn.getWalkerId().isEmpty()) {
							walkers.add(npc);
							// PacketSendUtility.sendMessage(admin, "Skipping Npc with Route: "+spawn.getWalkerId());
							// counter++;
							// continue;
						}
						if (npc.getObjectTemplate() == null) {
							GameServer.log.info("NpcTemplate is missing for .. NpcId: " + npc.getNpcId());
						}
						if (npc.getObjectTemplate().getDesc().toLowerCase().contains("book")) {
							PacketSendUtility.sendMessage(admin, "Skipping Book: " + npc.getName());
							counter++;
							books++;
							continue;
						}

						WorldPosition pos = npc.getPosition();
						if (pos == null) {
							GameServer.log.info("WorldPos is missing for .. NpcId: " + npc.getNpcId());
							counter++;
							continue;
						}
						if (pos.getX() == 0 && pos.getY() == 0) {
							counter++;
							continue;
						}

						GameServer.log.info("Teleporting to " + npc.getName() + " [" + (counter) + "/" + npcs.size() + "] ");
						PacketSendUtility.sendMessage(admin, "Teleporting to Npc (id: " + spawn.getNpcId() + ") [" + (counter++) + "/" + npcs.size() + "]");

						npc.getController().delete();
						while (npc.isSpawned()) {
							if (canceled)
								break;
							Thread.sleep(500);
							npc.getController().delete();
						}

						pos.setZ(spawn.getZ() + jumpHeight);
						TeleportService2.teleportTo(admin, pos);
						TeleportService2.teleportTo(admin, worldId, pos.getX(), pos.getY(), pos.getZ());
						admin.getController().stopProtectionActiveTask();
						int retryCount = 1;
						float oldAdminZ = admin.getZ();
						float updatedZ = admin.getZ() + 3F;
						float tempJumpHeight = jumpHeight;
						// GameServer.log.info("passed point 1");
						boolean skip = false;
						while (updatedZ > admin.getZ()) // Falling
						{
							updatedZ = admin.getZ();
							Thread.sleep(800);
							// secCount++;
							// GameServer.log.info("passed point 2");

							while (!skip && retryCount < 9 && (oldAdminZ - admin.getZ()) > 20 || admin.getZ() <= (World.getInstance().getWorldMap(admin.getWorldId()).getDeathLevel() + 10)) {
								if (retryCount == 8) {
									PacketSendUtility.sendMessage(admin, " Skipping step: " + counter + " - because there is an black hole ?!");
									skip = true;
								}
								tempJumpHeight += 3;
								PacketSendUtility.sendMessage(admin, "ReTry (" + retryCount + "/8) " + spawn.getNpcId() + " with new jumpheight: " + tempJumpHeight);
								pos.setZ(spawn.getZ() + tempJumpHeight);
								admin.setPortAnimation(0);
								TeleportService2.teleportTo(admin, worldId, pos.getX(), pos.getY(), pos.getZ());
								admin.getController().stopProtectionActiveTask();
								updatedZ = pos.getZ() + 3F;
								oldAdminZ = pos.getZ() + 3F;
								retryCount++;
							}
							if (updatedZ == admin.getZ())
								tempJumpHeight = jumpHeight;
						}
						// GameServer.log.info("passed point 3");
						Spawn sp = spawnData.getSpawnsForNpc(spawn.getWorldId(), spawn.getNpcId());
						if (sp == null || sp.getSpawnSpotTemplates() == null) {
							GameServer.log.info("Spawn is null .. NpcId: " + npc.getNpcId());
							continue;
						}
						for (SpawnSpotTemplate sst : sp.getSpawnSpotTemplates()) {
							if (sst.getX() == spawn.getX() && sst.getY() == spawn.getY())
								sst.setZ(admin.getZ());
						}
						SpawnEngine.spawnObject(spawn, admin.getInstanceId());
					}

				}
				catch (Exception e) {
					GameServer.log.info("Error: " + e.getCause());
				}
				finally {
					if (canceled)
						return;
					long minutes = ((System.currentTimeMillis() - startTime) / 1000) / 60;
					String timeString = minutes / 60 % 24 + " hours and " + minutes % 60 + " minutes";
					PacketSendUtility.sendMessage(admin, "Done " + mapName + " in " + timeString + ". Skipped Books: " + books);
					GameServer.log.info("[SpawnFix] Done " + mapName + " in " + timeString + ". Skipped Books: " + books + " / WalkerRoutes: " + walkers.size());

					if (worldId > 0) {
						writeXmlFile(spawnData);
					}
					reset();
				}
			}
		}, 2000);

	}

	public static void reset() {
		isRunning = false;
		canceled = false;
		if (!wasInvul)
			runner.setInvul(false);
		runner = null;
		counter = 1;
		spawns.clear();
		walkers.clear();
		worldId = -1;
		thread = null;
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax : //fixpath <start> || <cancel>");
	}
}
