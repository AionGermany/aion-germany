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
package com.aionemu.gameserver.services.dynamic_world;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLAG_INFO;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34, FrozenKiller
 */
public class TowerEntranceService {

	private static final TowerEntranceService instance = new TowerEntranceService();

	private List<Npc> flags = new ArrayList<Npc>();

	public void startTowerEntrance() {
		startTowerEntranceAsmodians();
		startTowerEntranceElyos();
	}

	private void startTowerEntranceElyos() {
		int randomPos = Rnd.get(1, 5);
		switch (randomPos) {
			case 1:// spawn checked
				SpawnTemplate portal = SpawnEngine.addNewSpawn(210100000, 806081, 1239.3398f, 943.23267f, 330.21225f, (byte) 0, 0); // LF6_LF_Tower_Entrance
				portal.setStaticId(1769);
				SpawnEngine.spawnObject(portal, 1);
				SpawnTemplate flag = SpawnEngine.addNewSpawn(210100000, 833765, 1239.3398f, 943.23267f, 330.21225f, (byte) 0, 0); // LF6_LF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag, 1);
				broadcastUpdate(portal, flag);
				break;
			case 2:
				SpawnTemplate portal1 = SpawnEngine.addNewSpawn(210100000, 806081, 1458.0706f, 1300.4155f, 334.83932f, (byte) 0, 0); // LF6_LF_Tower_Entrance
				portal1.setStaticId(60);
				SpawnEngine.spawnObject(portal1, 1);
				SpawnTemplate flag1 = SpawnEngine.addNewSpawn(210100000, 833765, 1458.0706f, 1300.4155f, 334.83932f, (byte) 0, 0); // LF6_LF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag1, 1);
				broadcastUpdate(portal1, flag1);
				break;
			case 3:// spawn checked
				SpawnTemplate portal2 = SpawnEngine.addNewSpawn(210100000, 806081, 1512.8707f, 1859.1428f, 319.41635f, (byte) 0, 0); // LF6_LF_Tower_Entrance
				portal2.setStaticId(597);
				SpawnEngine.spawnObject(portal2, 1);
				SpawnTemplate flag2 = SpawnEngine.addNewSpawn(210100000, 833765, 1512.8707f, 1859.1428f, 319.41635f, (byte) 0, 0); // LF6_LF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag2, 1);
				broadcastUpdate(portal2, flag2);
				break;
			case 4:// spawn checked
				SpawnTemplate portal3 = SpawnEngine.addNewSpawn(210100000, 806081, 1779.8989f, 820.21204f, 311.21802f, (byte) 0, 0); // LF6_LF_Tower_Entrance
				portal3.setStaticId(1768);
				SpawnEngine.spawnObject(portal3, 1);
				SpawnTemplate flag3 = SpawnEngine.addNewSpawn(210100000, 833765, 1779.8989f, 820.21204f, 311.21802f, (byte) 0, 0); // LF6_LF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag3, 1);
				broadcastUpdate(portal3, flag3);
				break;
			case 5:
				SpawnTemplate portal4 = SpawnEngine.addNewSpawn(210100000, 806081, 1967.001f, 1400.6051f, 293.57266f, (byte) 0, 0); // LF6_LF_Tower_Entrance
				portal4.setStaticId(1486);
				SpawnEngine.spawnObject(portal4, 1);
				SpawnTemplate flag4 = SpawnEngine.addNewSpawn(210100000, 833765, 1967.001f, 1400.6051f, 293.57266f, (byte) 0, 0); // LF6_LF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag4, 1);
				broadcastUpdate(portal4, flag4);
				break;
		}
	}

	private void startTowerEntranceAsmodians() {
		int randomPos = Rnd.get(1, 5);
		switch (randomPos) {
			case 1:
				SpawnTemplate portal = SpawnEngine.addNewSpawn(220110000, 806082, 1246.2782f, 2129.3772f, 187.10168f, (byte) 0, 0); // DF6_DF_Tower_Entrance
				portal.setStaticId(2325);
				SpawnEngine.spawnObject(portal, 1);
				SpawnTemplate flag = SpawnEngine.addNewSpawn(220110000, 703146, 1246.2782f, 2129.3772f, 187.10168f, (byte) 0, 0); // DF6_DF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag, 1);
				broadcastUpdate(portal, flag);
				break;
			case 2: // checked
				SpawnTemplate portal1 = SpawnEngine.addNewSpawn(220110000, 806082, 1515.3073f, 1623.7815f, 208.20427f, (byte) 0, 0); // DF6_DF_Tower_Entrance
				portal1.setStaticId(2326);
				SpawnEngine.spawnObject(portal1, 1);
				SpawnTemplate flag1 = SpawnEngine.addNewSpawn(220110000, 703146, 1515.3073f, 1623.7815f, 208.20427f, (byte) 0, 0); // DF6_DF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag1, 1);
				broadcastUpdate(portal1, flag1);
				break;
			case 3: // checked
				SpawnTemplate portal2 = SpawnEngine.addNewSpawn(220110000, 806082, 1754.4885f, 2011.0245f, 196.33447f, (byte) 0, 0); // DF6_DF_Tower_Entrance
				portal2.setStaticId(2302);
				SpawnEngine.spawnObject(portal2, 1);
				SpawnTemplate flag2 = SpawnEngine.addNewSpawn(220110000, 703146, 1754.4885f, 2011.0245f, 196.33447f, (byte) 0, 0); // DF6_DF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag2, 1);
				broadcastUpdate(portal2, flag2);
				break;
			case 4: // checked
				SpawnTemplate portal3 = SpawnEngine.addNewSpawn(220110000, 806082, 1816.1018f, 2536.0164f, 218.1f, (byte) 0, 0); // DF6_DF_Tower_Entrance
				portal3.setStaticId(2324);
				SpawnEngine.spawnObject(portal3, 1);
				SpawnTemplate flag3 = SpawnEngine.addNewSpawn(220110000, 703146, 1816.1018f, 2536.0164f, 218.1f, (byte) 0, 0); // DF6_DF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag3, 1);
				broadcastUpdate(portal3, flag3);
				break;
			case 5: // checked
				SpawnTemplate portal4 = SpawnEngine.addNewSpawn(220110000, 806082, 1999.5272f, 1749.0195f, 228.1f, (byte) 0, 0); // DF6_DF_Tower_Entrance
				portal4.setStaticId(2309);
				SpawnEngine.spawnObject(portal4, 1);
				SpawnTemplate flag4 = SpawnEngine.addNewSpawn(220110000, 703146, 1999.5272f, 1749.0195f, 228.1f, (byte) 0, 0); // DF6_DF_Tower_Entrance_Flag
				SpawnEngine.spawnObject(flag4, 1);
				broadcastUpdate(portal4, flag4);
				break;
		}
	}

	private void broadcastUpdate(final SpawnTemplate portal, final SpawnTemplate flag) {
		World.getInstance().getWorldMap(flag.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				for (VisibleObject npc : flag.getVisibleObjects()) {
					if (npc.getObjectTemplate().getTemplateId() == 833765 && npc.isSpawned()) {
						if (player.getWorldId() == 210100000 && player.getRace() == Race.ELYOS) {
							flags.add((Npc) npc);
							PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(flags));
						}
					}
					else if (npc.getObjectTemplate().getTemplateId() == 703146 && npc.isSpawned()) {
						if (player.getWorldId() == 220110000 && player.getRace() == Race.ASMODIANS) {
							flags.add((Npc) npc);
							PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(flags));
						}
					}
				}
			}
		});
		stopTower(portal, flag);
	}

	private void stopTower(final SpawnTemplate portal, final SpawnTemplate flag) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (portal.getWorldId() == 210100000) {
					for (VisibleObject npc : portal.getVisibleObjects()) {
						npc.getController().onDelete();
					}
					for (VisibleObject npc : flag.getVisibleObjects()) {
						npc.getController().onDelete();
					}
					startTowerEntranceElyos();
				}
				else if (portal.getWorldId() == 220110000) {
					for (VisibleObject npc : portal.getVisibleObjects()) {
						npc.getController().onDelete();
					}
					for (VisibleObject npc : flag.getVisibleObjects()) {
						npc.getController().onDelete();
					}
					startTowerEntranceAsmodians();
				}
			}
		}, 360 * 1000);
	}

	public void onEnterTowerWorld(Player player) {
		if (player.getWorldId() == 210100000 && player.getRace() == Race.ELYOS || player.getWorldId() == 220110000 && player.getRace() == Race.ASMODIANS) {
			World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					for (VisibleObject npc : World.getInstance().getNpcs()) {
						if (npc.getObjectTemplate().getTemplateId() == 833765 && npc.isSpawned()) {
							if (player.getWorldId() == 210100000 && player.getRace() == Race.ELYOS) {
								flags.add((Npc) npc);
								PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(flags));
							}
						}
						else if (npc.getObjectTemplate().getTemplateId() == 703146 && npc.isSpawned()) {
							if (player.getWorldId() == 220110000 && player.getRace() == Race.ASMODIANS) {
								flags.add((Npc) npc);
								PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(flags));
							}
						}
					}
				}
			});
		}
	}

	public static TowerEntranceService getInstance() {
		return instance;
	}
}
