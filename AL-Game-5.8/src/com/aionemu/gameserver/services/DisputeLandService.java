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
package com.aionemu.gameserver.services;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DISPUTE_LAND;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneAttributes;

import javolution.util.FastList;

/**
 * @author Source
 * @rework Eloann
 */
public class DisputeLandService {

	private boolean active;
	private FastList<Integer> worlds = new FastList<Integer>();

	private DisputeLandService() {
	}

	public static DisputeLandService getInstance() {
		return DisputeLandServiceHolder.INSTANCE;
	}

	public void initDisputeLand() {
		if (CustomConfig.DISPUTE_LAND_ENABLED) {
			CronService.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (isActive()) {
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								setActive(false);
							}
						}, CustomConfig.DISPUTE_LAND_TIME * 3600 * 1000);// 5 hours
					}
				}
			}, CustomConfig.DISPUTE_LAND_SCHEDULE);
		}
		worlds.add(210020000); // Eltnen.
		worlds.add(210040000); // Heiron.
		worlds.add(210050000); // Inggison.
		worlds.add(210060000); // Theobomos.
		worlds.add(220020000); // Morheim.
		worlds.add(220040000); // Beluslan.
		worlds.add(220050000); // Brusthonin.
		worlds.add(220070000); // Gelkmaros.
		// 4.7
		worlds.add(600090000); // Kaldor.
		worlds.add(600100000); // Levinshor.
		// 4.8
		worlds.add(210070000); // Cygnea.
		worlds.add(220080000); // Enshar.
		// 5.0
		worlds.add(210100000); // Iluma.
		worlds.add(220110000); // Norsvold.
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean value) {
		active = value;
		syncState();
		broadcast();
	}

	private void syncState() {
		for (int world : worlds) {
			if (world == 210020000 || // Eltnen.
				world == 210040000 || // Heiron.
				world == 210050000 || // Inggison.
				world == 210060000 || // Theobomos.
				world == 210070000 || // Cygnea.
				world == 220020000 || // Morheim.
				world == 220040000 || // Beluslan.
				world == 220050000 || // Brusthonin.
				world == 220070000 || // Gelkmaros.
				world == 220080000 || // Enshar.
				world == 210100000 || // Iluma.
				world == 220110000 || // Norsvold.
				world == 600090000 || // Kaldor.
				world == 600100000) { // Levinshor.
				continue;
			}
			if (active) {
				World.getInstance().getWorldMap(world).setWorldOption(ZoneAttributes.PVP_ENABLED);
			}
			else {
				World.getInstance().getWorldMap(world).removeWorldOption(ZoneAttributes.PVP_ENABLED);
			}
		}
	}

	private void broadcast(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DISPUTE_LAND(worlds, active));
	}

	private void broadcast() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				broadcast(player);
			}
		});
	}

	public void onLogin(Player player) {
		broadcast(player);
	}

	private static class DisputeLandServiceHolder {

		private static final DisputeLandService INSTANCE = new DisputeLandService();
	}
}
