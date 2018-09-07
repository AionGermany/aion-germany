/**
 * 
 */
package com.aionemu.gameserver.services.territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.dao.LegionDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team.legion.LegionTerritory;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CONQUEROR_PROTECTOR;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STONESPEAR_SIEGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TERRITORY_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UNK_12B;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

import javolution.util.FastMap;

/**
 * @author CoolyT
 */
public class TerritoryService {

	private TerritoryBuff territoryBuff;
	private FastMap<Integer, TerritoryBuff> buffs = new FastMap<Integer, TerritoryBuff>();
	private TreeMap<Integer, LegionTerritory> territories = new TreeMap<Integer, LegionTerritory>();
	private TreeMap<Integer, TreeMap<Integer, WorldPosition>> teleporters = new TreeMap<Integer, TreeMap<Integer, WorldPosition>>();

	public void init() {
		LegionService ls = LegionService.getInstance();
		Collection<Legion> legions = new ArrayList<Legion>();
		int counter = 0;

		// Fill Map with dummies, because client wants 6 entries ..
		for (int i = 1; i <= 6; i++) {
			territories.put(i, new LegionTerritory(i));
		}
		// Fill the LegionList
		for (Integer legionId : DAOManager.getDAO(LegionDAO.class).getLegionIdswithTerritories()) {
			legions.add(ls.getLegion(legionId));
		}
		// replace the dummies with realdata, if a legion owns an territory
		for (Legion legion : legions) {
			LegionTerritory territory = legion.getTerritory();
			// Because .replace is only supported @ java 1.8 or higher, we use the old variant :)
			territories.remove(territory.getId());
			territories.put(territory.getId(), territory);
			counter++;
		}
		GameServer.log.info("[TerritoryService] " + counter + " Legions owns a Territory..");

		/*
		 * Teleporters Asmos
		 */
		// Mura
		TreeMap<Integer, WorldPosition> mura = new TreeMap<Integer, WorldPosition>();
		mura.put(805174, new WorldPosition(220080000, 1767.4069F, 2589.9512F, 299.21448F, (byte) 25));
		mura.put(805175, new WorldPosition(220080000, 1767.9563F, 2586.478F, 298.71466F, (byte) 86));
		mura.put(805176, new WorldPosition(220080000, 1803.5879F, 2557.8096F, 299.47995F, (byte) 6));
		mura.put(805177, new WorldPosition(220080000, 1800.6494F, 2554.5647F, 298.75F, (byte) 72));
		teleporters.put(1, mura);
		// Satyr
		TreeMap<Integer, WorldPosition> satyr = new TreeMap<Integer, WorldPosition>();
		satyr.put(805178, new WorldPosition(220080000, 1442.7272F, 1739.9116F, 329.77304F, (byte) 60));
		satyr.put(805179, new WorldPosition(220080000, 1446.1732F, 1741.3677F, 328.875F, (byte) 116));
		satyr.put(805180, new WorldPosition(220080000, 1469.9454F, 1781.2479F, 330.02618F, (byte) 45));
		satyr.put(805181, new WorldPosition(220080000, 1472.8165F, 1777.6108F, 328.82397F, (byte) 101));
		teleporters.put(2, satyr);
		// Velias
		TreeMap<Integer, WorldPosition> velias = new TreeMap<Integer, WorldPosition>();
		velias.put(805182, new WorldPosition(220080000, 764.80646F, 1310.1145F, 252.27219F, (byte) 26));
		velias.put(805183, new WorldPosition(220080000, 765.20404F, 1304.71F, 251.5F, (byte) 86));
		velias.put(805184, new WorldPosition(220080000, 796.8811F, 1272.6748F, 252.29373F, (byte) 3));
		velias.put(805185, new WorldPosition(220080000, 792.2996F, 1270.9819F, 251.5F, (byte) 63));
		teleporters.put(3, velias);

		/*
		 * Teleporters Elyos
		 */
		// Kenoa
		TreeMap<Integer, WorldPosition> kenoa = new TreeMap<Integer, WorldPosition>();
		kenoa.put(805164, new WorldPosition(210070000, 1375.895F, 647.5174F, 581.81555F, (byte) 29));
		kenoa.put(805165, new WorldPosition(210070000, 1376.3457F, 643.75977F, 581.61F, (byte) 88));
		kenoa.put(805162, new WorldPosition(210070000, 1330.3495F, 634.5133F, 582.0176F, (byte) 45));
		kenoa.put(805163, new WorldPosition(210070000, 1333.2931F, 631.8545F, 581.4909F, (byte) 110));
		teleporters.put(4, kenoa);
		// Deluan
		TreeMap<Integer, WorldPosition> deluan = new TreeMap<Integer, WorldPosition>();
		deluan.put(805168, new WorldPosition(210070000, 1211.4873F, 1580.0035F, 467.79865F, (byte) 107));
		deluan.put(805169, new WorldPosition(210070000, 1208.5676F, 1582.7205F, 467.20496F, (byte) 50));
		deluan.put(805167, new WorldPosition(210070000, 1217.7823F, 1626.8406F, 467.2364F, (byte) 75));
		deluan.put(805166, new WorldPosition(210070000, 1220.8087F, 1628.8281F, 467.61154F, (byte) 9));
		teleporters.put(5, deluan);
		// Attika
		TreeMap<Integer, WorldPosition> attika = new TreeMap<Integer, WorldPosition>();
		attika.put(805172, new WorldPosition(210070000, 2381.85F, 1542.4397F, 438.40796F, (byte) 31));
		attika.put(805173, new WorldPosition(210070000, 2378.4214F, 1538.7888F, 437.67432F, (byte) 79));
		attika.put(805171, new WorldPosition(210070000, 2334.0369F, 1525.9147F, 438.49768F, (byte) 112));
		attika.put(805170, new WorldPosition(210070000, 2332.486F, 1529.6007F, 438.5F, (byte) 45));
		teleporters.put(6, attika);
	}

	public void onTeleport(Player player, int npcid) {
		if (player.getLegion() == null || player.getLegion().getTerritory().getId() == 0)
			return;

		int territoryId = player.getLegion().getTerritory().getId();
		TreeMap<Integer, WorldPosition> teleportMap = teleporters.get(territoryId);
		WorldPosition pos = null;
		if (teleportMap.containsKey(npcid))
			pos = teleportMap.get(npcid);

		if (pos != null)
			TeleportService2.teleportTo(player, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
	}

	public void onEnterWorld(Player player) {
		PacketSendUtility.sendPacket(player, new SM_TERRITORY_LIST(territories.values()));
		PacketSendUtility.sendPacket(player, new SM_UNK_12B());
	}

	public void sendStoneSpearPacket(Player player) {
		// PacketSendUtility.sendPacket(player, new SM_STONESPEAR_SIEGE(player.getLegion(),0));
	}

	public void onEnterTerritory(Player player) {
		if (player.getLegion() == null || player.getLegion().getTerritory().getId() == 0)
			return;

		territoryBuff = new TerritoryBuff();
		territoryBuff.applyEffect(player);
		buffs.put(player.getObjectId(), territoryBuff);
	}

	public void onLeaveTerritory(Player player) {
		if (player.getLegion() == null || player.getLegion().getTerritory().getId() == 0)
			return;

		if (buffs.containsKey(player.getObjectId())) {
			buffs.get(player.getObjectId()).endEffect(player);
			buffs.remove(player.getObjectId());
		}
	}

	public void scanForIntruders(Player player) {
		Collection<Player> players = new ArrayList<Player>();
		Iterator<Player> playerIt = World.getInstance().getPlayersIterator();
		while (playerIt.hasNext()) {
			Player enemy = playerIt.next();
			if (player.getWorldId() == enemy.getWorldId() && player.getRace() != enemy.getRace())
				players.add(enemy);
		}
		PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(players, false));
	}

	public void onConquerTerritory(Legion legion, int id) {
		if (legion.ownsTerretory()) {
			onLooseTerritory(legion);
		}

		LegionTerritory territory = new LegionTerritory(id);
		territory.setLegionId(legion.getLegionId());
		territory.setLegionName(legion.getLegionName());
		legion.setTerritory(territory);

		territories.remove(id);
		territories.put(id, territory);
		broadcastTerritoryList(territories);
		broadcastToLegion(legion);
	}

	private void broadcastToLegion(Legion legion) {
		PacketSendUtility.broadcastPacketToLegion(legion, new SM_LEGION_INFO(legion));
		PacketSendUtility.broadcastPacketToLegion(legion, new SM_STONESPEAR_SIEGE(legion, 0));
	}

	public void onLooseTerritory(Legion legion) {
		int oldTerritoryId = legion.getTerritory().getId();
		legion.clearTerritory();

		if (oldTerritoryId == 0)
			GameServer.log.info("[TerritoryService] Error TerritoryId is 0 !!! - Legion : " + legion.getLegionName());

		LegionTerritory fakeTerritory = new LegionTerritory(oldTerritoryId);
		territories.remove(oldTerritoryId);
		territories.put(oldTerritoryId, fakeTerritory);

		TreeMap<Integer, LegionTerritory> lostTerr = new TreeMap<Integer, LegionTerritory>();
		lostTerr.put(oldTerritoryId, fakeTerritory);
		broadcastTerritoryList(lostTerr);
		broadcastToLegion(legion);
	}

	public void broadcastTerritoryList(TreeMap<Integer, LegionTerritory> terr) {
		Collection<Player> players = World.getInstance().getAllPlayers();
		for (Player player : players) {
			if (!player.isOnline())
				return;

			PacketSendUtility.sendPacket(player, new SM_TERRITORY_LIST(terr.values()));
		}
	}

	public Collection<LegionTerritory> getTerritories() {
		return territories.values();
	}

	public static TerritoryService getInstance() {
		return TerritoryService.SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final TerritoryService instance = new TerritoryService();
	}
}
