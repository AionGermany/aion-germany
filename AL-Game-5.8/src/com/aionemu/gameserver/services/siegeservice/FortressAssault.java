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
package com.aionemu.gameserver.services.siegeservice;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.npc.AbyssNpcType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Luzien
 */
public class FortressAssault extends Assault<FortressSiege> {

	private final boolean isBalaurea;
	private boolean spawned = false;
	private List<float[]> spawnLocations;

	public FortressAssault(FortressSiege siege) {
		super(siege);
		this.isBalaurea = worldId != 400010000;
	}

	@Override
	protected void scheduleAssault(int delay) {
		dredgionTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				BalaurAssaultService.getInstance().spawnDredgion(getSpawnIdByFortressId());

				spawnTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawnAttackers();
					}
				}, Rnd.get(240, 300) * 1000);

			}
		}, delay * 1000);
	}

	@Override
	protected void onAssaultFinish(boolean captured) {
		spawnLocations.clear();

		if (!spawned) {
			return;
		}

		if (!captured) {
			rewardDefendingPlayers();
		}
		else {
			siegeLocation.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					// The Balaur have killed the Guardian General.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DRAGON_BOSS_KILLED);
				}
			});
		}
	}

	private void spawnAttackers() {

		if (spawned) {
			return;
		}

		spawned = true;
		float x = boss.getX();
		float y = boss.getY();
		float z = boss.getZ();
		byte heading = boss.getSpawn().getHeading();
		int radius1 = isBalaurea ? 5 : Rnd.get(7, 13);
		int radius2 = isBalaurea ? 9 : Rnd.get(15, 20);
		int amount = isBalaurea ? Rnd.get(10, 15) : Rnd.get(10, 15);
		int templateId;
		SiegeSpawnTemplate spawn;

		float minAngle = MathUtil.convertHeadingToDegree(heading) - 90;
		if (minAngle < 0) {
			minAngle += 360;
		}
		double minRadian = Math.toRadians(minAngle);
		float interval = (float) (Math.PI / (amount / 2));
		float x1;
		float y1;

		List<Integer> idList = getSpawnIds();
		int commanderCount = isBalaurea ? 0 : Rnd.get(2);
		spawnRegularBalaurs();

		for (int i = 0; amount > i; i++) {
			if (i < (amount / 2)) {
				x1 = (float) (Math.cos(minRadian + interval * i) * radius1);
				y1 = (float) (Math.sin(minRadian + interval * i) * radius1);
			}
			else {
				x1 = (float) (Math.cos(minRadian + interval * (i - amount / 2)) * radius2);
				y1 = (float) (Math.sin(minRadian + interval * (i - amount / 2)) * radius2);
			}
			templateId = (i <= commanderCount) ? idList.get(0) : idList.get(Rnd.get(1, idList.size() - 1));

			Npc attaker;
			if ((i > Math.round(amount / 3)) && !spawnLocations.isEmpty()) {
				float[] coords = spawnLocations.get(Rnd.get(spawnLocations.size()));
				spawn = SpawnEngine.addNewSiegeSpawn(worldId, templateId, locationId, SiegeRace.BALAUR, SiegeModType.ASSAULT, coords[0], coords[1], coords[2], heading);

				attaker = (Npc) SpawnEngine.spawnObject(spawn, 1);
				attaker.getSpawn().setX(x + x1);
				attaker.getSpawn().setY(y + y1);
				attaker.getSpawn().setZ(z);
			}
			else {
				spawn = SpawnEngine.addNewSiegeSpawn(worldId, templateId, locationId, SiegeRace.BALAUR, SiegeModType.ASSAULT, x + x1, y + y1, z, heading);
				SpawnEngine.spawnObject(spawn, 1);
			}
		}

		siegeLocation.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// The Dredgion has disgorged a horde of Balaur troopers
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_ABYSS_CARRIER_DROP_DRAGON, 0);
				// The Balaur Teleport Raiders appeared
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_ABYSS_WARP_DRAGON, 120000);
			}
		});

		idList.clear();
	}

	private void spawnRegularBalaurs() {
		spawnLocations = new ArrayList<float[]>();
		List<SpawnGroup2> siegeSpawns = DataManager.SPAWNS_DATA2.getSiegeSpawnsByLocId(locationId);
		for (SpawnGroup2 spawnGroup : siegeSpawns) {
			for (SpawnTemplate spawnTemplate : spawnGroup.getSpawnTemplates()) {
				SiegeSpawnTemplate temp = (SiegeSpawnTemplate) spawnTemplate;
				AbyssNpcType type = DataManager.NPC_DATA.getNpcTemplate(temp.getNpcId()).getAbyssNpcType();
				if (temp.getSiegeRace() != SiegeRace.BALAUR || !temp.isPeace() || type.equals(AbyssNpcType.ARTIFACT) || type.equals(AbyssNpcType.TELEPORTER)) {
					continue;
				}

				float[] loc = { spawnTemplate.getX() + 2, spawnTemplate.getY() + 2, spawnTemplate.getZ() };
				SiegeSpawnTemplate spawn = SpawnEngine.addNewSiegeSpawn(spawnTemplate.getWorldId(), spawnTemplate.getNpcId(), locationId, SiegeRace.BALAUR, SiegeModType.ASSAULT, loc[0], loc[1], loc[2], spawnTemplate.getHeading());
				VisibleObject attaker = SpawnEngine.spawnObject(spawn, 1);

				if (MathUtil.isIn3dRange(attaker, boss, isBalaurea ? 100 : 70)) {
					spawnLocations.add(loc);
				}
			}
		}
	}

	private int getSpawnIdByFortressId() {
		switch (locationId) {
			// RESHANTA
			case 1011: // Divine Fortress
				return 10;
			case 1131: // Siel's Western Fortress
				return 10;
			case 1132: // Siel's Eastern Fortress
				return 10;
			case 1141: // Sulfur Fortress
				return 10;
			case 1221: // Krotan Refuge
				return 10;
			case 1231: // Kysis Fortress
				return 10;
			case 1241: // Miren Fortress
				return 10;
			// KALDOR
			case 7011: // Wealhtheow's Keep
				return 10;
			// PANESTERRA
			case 10111: // Arcadian Fortress
				return 10;
			case 10211: // Umbral Fortress
				return 10;
			case 10311: // Eternum Fortress
				return 10;
			case 10411: // Skyclash Fortress
				return 10;
			default:
				return 1;
		}
	}

	private List<Integer> getSpawnIds() {
		List<Integer> Spawns = new ArrayList<Integer>();
		switch (locationId) {
			case 1011: // Divine Fortress
				return Spawns;
			case 1131: // Siel's Western Fortress
				Spawns.add(263027);
				Spawns.add(263042);
				Spawns.add(263057);
				Spawns.add(263072);
				return Spawns;
			case 1132: // Siel's Eastern Fortress
				Spawns.add(263327);
				Spawns.add(263342);
				Spawns.add(263357);
				Spawns.add(263372);
				return Spawns;
			case 1141: // Sulfur Fortress
				Spawns.add(264527);
				Spawns.add(264542);
				Spawns.add(264557);
				Spawns.add(264572);
				return Spawns;
			case 1221: // Krotan Refuge
				Spawns.add(279658);
				Spawns.add(279697);
				Spawns.add(279699);
				Spawns.add(279722);
				return Spawns;
			case 1231: // Kysis Fortress
				Spawns.add(279852);
				Spawns.add(279860);
				Spawns.add(279861);
				Spawns.add(279870);
				Spawns.add(279873);
				Spawns.add(279894);
				Spawns.add(279897);
				Spawns.add(279916);
				Spawns.add(279920);
				Spawns.add(279926);
				Spawns.add(279930);
				return Spawns;
			case 1241: // Miren Fortress
				Spawns.add(279749);
				Spawns.add(279763);
				Spawns.add(279764);
				Spawns.add(279771);
				Spawns.add(279788);
				Spawns.add(279789);
				Spawns.add(279803);
				Spawns.add(279819);
				Spawns.add(279828);
				Spawns.add(279829);
				Spawns.add(279833);
				return Spawns;
			case 7011: // Wealhtheow's Keep
				Spawns.add(252000);
				Spawns.add(252010);
				Spawns.add(252020);
				Spawns.add(252025);
				Spawns.add(252030);
				Spawns.add(252035);
				return Spawns;
			case 10111: // Arcadian Fortress
			case 10211: // Umbral Fortress
			case 10311: // Eternum Fortress
			case 10411: // Skyclash Fortress
				Spawns.add(880813);
				Spawns.add(880814);
				Spawns.add(880817);
				Spawns.add(880818);
				Spawns.add(880819);
				Spawns.add(880820);
				return Spawns;
			default:
				return Spawns;
		}
	}

	private void rewardDefendingPlayers() {
		// TODO: participating Players recieve Mail on Death of Commander
	}
}
