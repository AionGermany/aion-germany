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
package com.aionemu.gameserver.services.conquerer_protector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.ConquerorProtectorConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerConquererProtectorData;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CONQUEROR_PROTECTOR;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Kill3r
 * @modify Elo
 */
public class ConquerorsService {

	private FastMap<Integer, MapTypes> usedWorldMaps = new FastMap<Integer, MapTypes>();
	private FastMap<Integer, PlayerConquererProtectorData> players = new FastMap<Integer, PlayerConquererProtectorData>();
	private FastMap<Integer, Future<?>> pduration = new FastMap<Integer, Future<?>>();
	private FastMap<Integer, Future<?>> cduration = new FastMap<Integer, Future<?>>();
	private ProtectorBuff protectorBuff;
	private ConquerorBuff conquerorBuff;

	private static final Logger log = LoggerFactory.getLogger(ConquerorsService.class);

	public enum MapTypes {
		ELYOS,
		ASMODIANS;
	}

	public void initConquerorPvPSystem() {
		if (!ConquerorProtectorConfig.ENABLE_GUARDIAN_PVP)
			return;

		GameServer.log.info("[ConquerorsService] Initializing Conqueror/Protector Buff System...");

		protectorBuff = new ProtectorBuff();
		conquerorBuff = new ConquerorBuff();
		if (!ConquerorProtectorConfig.IGNORE_MAPS) {
			for (String worldids : ConquerorProtectorConfig.ENABLED_MAPS_GUARDIAN.split(",")) {
				if (worldids.equals(""))
					break;

				int worldId = Integer.parseInt(worldids); // world Id from Config
				int type = Integer.parseInt(String.valueOf(worldids.charAt(1))); // 220000000 , second number from worldId , to get Wat Type of World

				if (!(type == 1 || type == 2)) {
					type = 3;
					log.info("[ConquerorsService] [CONQUEROR NOTE] Please Verify the Map Id's Given in conqueror.properties!!");
				}
				MapTypes mType = type == 1 ? MapTypes.ELYOS : MapTypes.ASMODIANS;
				usedWorldMaps.put(worldId, mType);
			}
		}
	}

	public boolean isOnConquerorPvPMap(int worldid) {
		if (ConquerorProtectorConfig.IGNORE_MAPS)
			return true;

		return usedWorldMaps.containsKey(worldid);
	}

	public void setKills(Player player, int kills) {
		if (isOnEnemyMap(player))
			player.getConquerorProtectorData().setKillCountAsConquerer(kills);
		else
			player.getConquerorProtectorData().setKillCountAsProtector(kills);
	}

	public void scanForIntruders(Player player) {
		if (!isOnConquerorPvPMap(player.getWorldId()))
			return;

		int protectorLevel = player.getConquerorProtectorData().getProtectorBuffLevel();
		Collection<Player> players = new ArrayList<Player>();
		Iterator<Player> ita = World.getInstance().getPlayersIterator();
		while (ita.hasNext()) {
			Player p1 = ita.next();
			if (player.getWorldId() == p1.getWorldId() && player.getRace() != p1.getRace() && protectorLevel >= p1.getConquerorProtectorData().getConquerorBuffLevel() && MathUtil.getDistance(player, p1) <= 500) {
				players.add(p1);
			}
		}
		msgLog("Sending SM_SERIAL_KILLER with " + players.size() + " Players");
		PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(players, false));
		PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(players, true));
	}

	public int getKills(Player player) {
		PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();
		// msgLog("GetKills for "+player.getName()+": KillsAsProtector: "+pcdd.getKillCountAsProtector()+" KillsAsConquerer: "+pcdd.getKillCountAsConquerer()+" isProtector: "+pcdd.isProtector());
		if (pcdd.isProtector())
			return pcdd.getKillCountAsProtector();
		else
			return pcdd.getKillCountAsConquerer();
	}

	public void addKills(Player player, int kills) {
		PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();
		int oldKills = getKills(player);
		if (isOnEnemyMap(player))
			pcdd.setKillCountAsConquerer(pcdd.getKillCountAsConquerer() + kills);
		else
			pcdd.setKillCountAsProtector(pcdd.getKillCountAsProtector() + kills);

		int newKills = getKills(player);
		msgLog((oldKills > newKills ? "De" : "In") + "creased Kills for " + player.getName() + " from: " + oldKills + " to: " + newKills);
	}

	public boolean isOnEnemyMap(Player player) {
		if (ConquerorProtectorConfig.IGNORE_MAPS) {
			String worldidsAsString = String.valueOf(player.getWorldId());
			int type = worldidsAsString.charAt(1);

			if (!(type == 1 || type == 2)) {
				type = 3;
				log.info("[ConquerorsService] [CONQUEROR NOTE] Please Verify the Map Id's Given in conqueror.properties OR In an Instance?");
			}

			MapTypes mType = type == 1 ? MapTypes.ELYOS : MapTypes.ASMODIANS;
			MapTypes tt = player.getRace().equals(Race.ASMODIANS) ? MapTypes.ASMODIANS : MapTypes.ELYOS;

			return !mType.equals(tt);
		}
		if (usedWorldMaps.containsKey(player.getWorldId())) {
			MapTypes mType = player.getRace().equals(Race.ASMODIANS) ? MapTypes.ASMODIANS : MapTypes.ELYOS;
			return !usedWorldMaps.get(player.getWorldId()).equals(mType);
		}
		return false;
	}

	public void onKill(Player player, Player diedPlayer) {
		if (!isOnConquerorPvPMap(player.getWorldId()))
			return;

		PlayerConquererProtectorData pcdd_killer = player.getConquerorProtectorData();
		// Add new player if he's not in players and give first buff lvl
		if (!players.containsKey(player.getObjectId())) {
			players.put(player.getObjectId(), pcdd_killer);
			msgLog("Added New Player : " + player.getName() + " to playerList of PvP.");
			msgLog(player.getName() + "'s Buff Lvl's " + players.get(player.getObjectId()) + " {ProtectorBuffLvl = ConquerorBuffLvl}.");
		}
		msgLog("Current Kill Count : " + getKills(player));
		addKills(player, 1);
		msgLog("New Kill Count : " + getKills(player));

		// check if player got enough kills for next bufflevel and update it in case.
		checkKillCountAndUpdateBuffLvls(player);
		// set timer kiilDownCount
		sheduleCountDownKills(player);

		// only for Msg
		checkIfTargetIsHighestRankIntruder(player, diedPlayer);

		if (getKills(diedPlayer) > 0) {
			addKills(diedPlayer, -1);
			checkKillCountAndUpdateBuffLvls(diedPlayer);
			sheduleCountDownKills(diedPlayer);
		}
	}

	public void updateTagPacketToNearby(final Player player) // NEED TO RE-CHECK
	{
		final PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();
		World.getInstance().getWorldMap(player.getWorldId()).getWorldMapInstanceById(player.getInstanceId()).doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player p) {
				if (player != p) {
					if (pcdd.isProtector()) // Protector
						PacketSendUtility.sendPacket(p, new SM_CONQUEROR_PROTECTOR(player, true, true, pcdd.getProtectorBuffLevel()));
					else // Conqueror
						PacketSendUtility.sendPacket(p, new SM_CONQUEROR_PROTECTOR(player, false, true, pcdd.getConquerorBuffLevel()));
				}
			}
		});
	}

	public void sendPacketToEveryoneInMap(Player player, Player diedPlayer) {
		Iterator<Player> ita = World.getInstance().getPlayersIterator();
		while (ita.hasNext()) {
			Player p1 = ita.next();
			if (player.getWorldId() == p1.getWorldId()) {
				if (diedPlayer.getRace() == Race.ELYOS && !isOnEnemyMap(player))
					// Hero of Asmodian %0 killed the Divinely Punished Intruder %1.
					PacketSendUtility.sendPacket(p1, new SM_SYSTEM_MESSAGE(1400141, player.getName(), diedPlayer.getName()));
				else if (diedPlayer.getRace() == Race.ASMODIANS && !isOnEnemyMap(player))
					// Hero of Elyos %0 killed the Divinely Punished Intruder %1.
					PacketSendUtility.sendPacket(p1, new SM_SYSTEM_MESSAGE(1400142, player.getName(), diedPlayer.getName()));
			}
		}
	}

	public void checkIfTargetIsHighestRankIntruder(Player player, Player diedPlayer) {
		if (diedPlayer.getConquerorProtectorData().getProtectorBuffLevel() == 3 || diedPlayer.getConquerorProtectorData().getConquerorBuffLevel() == 3)
			sendPacketToEveryoneInMap(player, diedPlayer);
	}

	public void checkKillCountAndUpdateBuffLvls(Player player) {
		PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();
		int kills = getKills(player);
		boolean isProtector = pcdd.isProtector();
		int buffLevel = 0;
		int oldBuffLevel = isProtector ? pcdd.getProtectorBuffLevel() : pcdd.getConquerorBuffLevel();

		if (isProtector) {
			if ((kills >= ConquerorProtectorConfig.PROTECTOR_LVL1_KILLCOUNT && kills < ConquerorProtectorConfig.PROTECTOR_LVL2_KILLCOUNT))
				buffLevel = 1; // sets Protector lvl 1 buff
			else if ((kills >= ConquerorProtectorConfig.PROTECTOR_LVL2_KILLCOUNT && kills < ConquerorProtectorConfig.PROTECTOR_LVL3_KILLCOUNT))
				buffLevel = 2; // sets Protector lvl 2 Buff
			else if (kills >= ConquerorProtectorConfig.PROTECTOR_LVL3_KILLCOUNT)
				buffLevel = 3; // sets Protector lvl 3 Buff
		}
		else {
			if ((kills >= ConquerorProtectorConfig.CONQUEROR_LVL1_KILLCOUNT && kills < ConquerorProtectorConfig.CONQUEROR_LVL2_KILLCOUNT))
				buffLevel = 1; // sets Conquerur lvl 1 buff
			else if ((kills >= ConquerorProtectorConfig.CONQUEROR_LVL2_KILLCOUNT && kills < ConquerorProtectorConfig.CONQUEROR_LVL3_KILLCOUNT))
				buffLevel = 2; // sets Conquerur lvl 2 buff
			else if (kills >= ConquerorProtectorConfig.CONQUEROR_LVL3_KILLCOUNT)
				buffLevel = 3; // sets Conquerur lvl 3 buff
		}

		if (buffLevel != oldBuffLevel) {
			int protectorBuffLevel = 0;
			int conquerorBuffLevel = 0;

			if (isProtector) {
				protectorBuffLevel = buffLevel;
				conquerorBuffLevel = pcdd.getConquerorBuffLevel();
			}
			else {
				conquerorBuffLevel = buffLevel;
				protectorBuffLevel = pcdd.getProtectorBuffLevel();
			}

			msgLog("---------------------------------- [ setBuffLevels ] -----------------------------------");
			msgLog("Player : " + player.getName() + " Setting ProtectorBuffLvl : " + protectorBuffLevel + " , Setting ConquerorBuffLvl : " + conquerorBuffLevel);
			msgLog("Player : " + player.getName() + " KILL COUNT : " + getKills(player) + " on Enemy Map : " + isOnEnemyMap(player) + " is Protector: " + pcdd.isProtector());
			msgLog("---------------------------------- [ endBuffLevels ] -----------------------------------");
			setBuffLvls(player, protectorBuffLevel, conquerorBuffLevel);
		}
	}

	public void onLogOut(Player player) {
		if (!ConquerorProtectorConfig.ENABLE_GUARDIAN_PVP)
			return;

		if (players.containsKey(player.getObjectId()))
			players.remove(player.getObjectId());

		if (pduration.containsKey(player.getObjectId()) || cduration.containsKey(player.getObjectId()))
			players.put(player.getObjectId(), player.getConquerorProtectorData());
	}

	public void onEnterWorld(Player player) {
		if (!ConquerorProtectorConfig.ENABLE_GUARDIAN_PVP)
			return;
		// if already in list - update players data with the saved one.
		if (players.containsKey(player.getObjectId())) {
			PlayerConquererProtectorData savedData = players.get(player.getObjectId());
			msgLog("Found saved Data for Player : " + player.getName() + "Kills: " + getKills(player) + " ConquererBuffLevel: " + savedData.getConquerorBuffLevel() + " ProtectorBuffLevel: " + savedData.getProtectorBuffLevel());
			player.setConquerorDefenderData(savedData);
		}
		PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();
		checkKillCountAndUpdateBuffLvls(player);

		sendPacket(player, pcdd.getProtectorBuffLevel(), pcdd.getConquerorBuffLevel());
		msgLog("==You're Last Activity [" + player.getName() + "]==");
		msgLog("Kills : " + getKills(player));
		msgLog("Buffs Protector : " + pcdd.getProtectorBuffLevel());
		msgLog("Buffs Conqueror : " + pcdd.getConquerorBuffLevel());
	}

	public void sheduleCountDownKills(final Player player) {
		Future<?> timer = null;
		int time = 0;
		PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();

		if (pcdd.isProtector() && pduration.containsKey(player.getObjectId())) {
			timer = pduration.get(player.getObjectId());
			pduration.remove(player.getObjectId());
		}
		else if (cduration.containsKey(player.getObjectId())) {
			timer = cduration.get(player.getObjectId());
			cduration.remove(player.getObjectId());
		}

		if (pcdd.isProtector()) {
			switch (pcdd.getProtectorBuffLevel()) {
				case 1:
					time = ConquerorProtectorConfig.DURATION_PBUFF1;
					break;
				case 2:
					time = ConquerorProtectorConfig.DURATION_PBUFF2;
					break;
				case 3:
					time = ConquerorProtectorConfig.DURATION_PBUFF3;
					break;
			}
		}
		else {
			switch (pcdd.getConquerorBuffLevel()) {
				case 1:
					time = ConquerorProtectorConfig.DURATION_CBUFF1;
					break;
				case 2:
					time = ConquerorProtectorConfig.DURATION_CBUFF2;
					break;
				case 3:
					time = ConquerorProtectorConfig.DURATION_CBUFF3;
					break;
			}
		}

		if (timer != null)
			timer.cancel(true);

		timer = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (getKills(player) <= 0)
					return;

				// Decrease kills by one
				if (getKills(player) > 1) {
					msgLog("Runnable : decreasing " + player.getName() + "'s KillCount by 1");
					addKills(player, -1);
				}
				else if (getKills(player) == 1) {
					addKills(player, -1);
					// Kills should be 0 now .. so we can remove te player from players list ...
					msgLog("Removed Player: " + player.getName() + " from PVPList... because KillCount is : " + getKills(player));
					players.remove(player.getObjectId());
				}
				// Call CheckRoutine
				checkKillCountAndUpdateBuffLvls(player);
				// Shedule next downCount
				sheduleCountDownKills(player);
			}
		}, time * 1000 * 60); // Need to find retail time , for how long it takes for kill countdown each bufflevel ...

		if (pcdd.isProtector())
			pduration.put(player.getObjectId(), timer);
		else
			cduration.put(player.getObjectId(), timer);
	}

	public void setBuffLvls(Player player, int ProtectorBuffLvl, int ConquerorBuffLvl) {
		PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();
		pcdd.setConquerorBuffId(ConquerorBuffLvl);
		pcdd.setProtectorBuffId(ProtectorBuffLvl);
		updateBuffLvls(player);
		updateTagPacketToNearby(player);
		msgLog("Set Player " + player.getName() + "'s Buffs to Protector Lvl: " + pcdd.getProtectorBuffLevel() + " | Conquerors Lvl: " + pcdd.getConquerorBuffLevel());
	}

	public void updateBuffLvls(Player player) {
		PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();
		sendPacket(player, pcdd.getProtectorBuffLevel(), pcdd.getConquerorBuffLevel());
		protectorBuff.applyEffect(player, pcdd.getProtectorBuffLevel());
		conquerorBuff.applyEffect(player, pcdd.getConquerorBuffLevel());

		msgLog("Updated Player " + player.getName() + "'s Buffs to Protector Lvl: " + pcdd.getProtectorBuffLevel() + " | Conquerors Lvl: " + pcdd.getConquerorBuffLevel());
		player.getGameStats().updateStatsAndSpeedVisually();
	}

	public void sendPacket(Player player, int ProtectorLvl, int ConquerorLvl) {
		// Packet for the Conqueror
		PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(player, false, false, ConquerorLvl));
		// Packet for the Protector
		PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(player, true, false, ProtectorLvl));
	}

	public void msgLog(String msg) {
		if (ConquerorProtectorConfig.ENABLE_CONQUEROR_DEBUGMODE) {
			log.info("[ConquerorsService] " + msg);
		}
	}

	public void sendNormalLogMsg(String msg) {
		log.info("[ConquerorsService] " + msg);
	}

	public static ConquerorsService getInstance() {
		return ConquerorsService.SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final ConquerorsService instance = new ConquerorsService();
	}

}
