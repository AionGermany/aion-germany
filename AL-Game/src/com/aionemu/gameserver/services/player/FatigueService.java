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
package com.aionemu.gameserver.services.player;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FATIGUE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldType;

/**
 * @author Alcapwnd
 * @see service isnt fully implemented in ncsoft client much things are mystery and not really tested (so it looks like) things like recover fatigue arent complete implemented! this service is only
 *      testy and shouldnt be enabled on a working machine
 * @see implemented the service so good as its possible need to wait until it will be released on ncsoft to do it retail like but so its until yet done its retail like :)
 */
public class FatigueService {

	private int isFull = 0;
	private int fatigueRecover = 0;
	private int effectEnabled = 0;
	private int iconSet = 256;
	private String message = null;
	private List<Future<?>> delays = new ArrayList<Future<?>>();
	private List<Player> players = new ArrayList<Player>();
	private static final Logger log = LoggerFactory.getLogger(FatigueService.class);
	private Calendar calendar = Calendar.getInstance();

	private FatigueService() {
		GameServer.log.info("[FatigueService] started ...");
	}

	public void onPlayerLogin(Player player) {
		if (player == null)
			return;
		if (player.getLevel() < 10)
			return;

		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.WEDNESDAY:
				if (player.getCommonData().getFatigueReset() == 0 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
					player.getCommonData().setFatigue(0);
					player.getCommonData().setFatigueRecover(1);
					player.getCommonData().setFatigueReset(1);
				}
				break;
			case Calendar.MONDAY:
			case Calendar.TUESDAY:
			case Calendar.THURSDAY:
			case Calendar.FRIDAY:
			case Calendar.SATURDAY:
			case Calendar.SUNDAY:
				if (player.getCommonData().getFatigueReset() == 1)
					player.getCommonData().setFatigueReset(0);
				break;
		}

		checkFatigueLost(player);

		if (player.getCommonData().getFatigue() == 100) {
			isFull = 1;
		}
		else {
			message = fatigueMessage(player.getCommonData().getFatigue());
		}

		if (isFull == 1) {
			fatigueRecover = 0/* player.getCommonData().getFatigueRecover() */;
			effectEnabled = 1;
		}
		else {
			fatigueRecover = 0; // only send if fatigue isFull
			effectEnabled = 0;
			PacketSendUtility.sendBrightYellowMessage(player, message);
		}

		if (CustomConfig.FATIGUE_SYSTEM_ENABLED)
			iconSet = 256;
		else
			iconSet = 0;

		PacketSendUtility.sendPacket(player, new SM_FATIGUE(effectEnabled, isFull, fatigueRecover, iconSet));
		players.add(player);
		log.info("[FatigueService] Added player " + player.getName() + " to fatigue update pool");
		load();

	}

	public void onPlayerLogout(Player player) {
		players.remove(player);
		log.info("[FatigueService] Removed player " + player.getName() + " from fatigue update pool");
	}

	public void checkFatigueLost(Player player) {
		long lastOnline = player.getCommonData().getLastOnline().getTime();
		long secondsOffline = (System.currentTimeMillis() / 1000) - lastOnline / 1000;
		double hours = secondsOffline / 3600d;

		int currentFatigue = player.getCommonData().getFatigue();

		// TODO check this calculation! we need to figure out if this is ok
		if (hours > 1 && hours < 3) {
			player.getCommonData().setFatigue(currentFatigue - 5);
		}
		else if (hours > 3 && hours < 5) {
			player.getCommonData().setFatigue(currentFatigue - 10);
		}
		else if (hours > 5 && hours < 6) {
			player.getCommonData().setFatigue(currentFatigue - 20);
		}
		else if (hours > 6 && hours < 10) {
			player.getCommonData().setFatigue(currentFatigue - 40);
		}
		else if (hours > 10 && hours < 24) {
			player.getCommonData().setFatigue(currentFatigue - 50);
		}
		else if (hours > 24) {
			player.getCommonData().setFatigue(currentFatigue - 100);
		}

		if (player.getCommonData().getFatigue() < 0) {
			player.getCommonData().setFatigue(0);
		}
	}

	public void removeRecoverCount(Player player, int count) {
		if (player == null)
			return;
		if (player.getLevel() < 10)
			return;

		if (player.getCommonData().getFatigueRecover() < count)
			return;

		player.getCommonData().setFatigueRecover(-count);
		if (player.getCommonData().getFatigueRecover() < 0) {
			player.getCommonData().setFatigueRecover(0);
		}
		player.getCommonData().setFatigue(0);
		checkFatigue(player);
	}

	public void checkFatigue(Player player) {
		if (player == null)
			return;
		if (player.getLevel() < 10)
			return;

		if (player.getCommonData().getFatigue() == 100) {
			isFull = 1;
		}
		else {
			message = fatigueMessage(player.getCommonData().getFatigue());
		}

		if (isFull == 1) {
			fatigueRecover = 0/* player.getCommonData().getFatigueRecover() */;
			effectEnabled = 1;
		}
		else {
			fatigueRecover = 0; // only send if fatigue isFull
			effectEnabled = 0;
			PacketSendUtility.sendBrightYellowMessage(player, message);
		}

		if (CustomConfig.FATIGUE_SYSTEM_ENABLED)
			iconSet = 256;
		else
			iconSet = 0;

		PacketSendUtility.sendPacket(player, new SM_FATIGUE(effectEnabled, isFull, fatigueRecover, iconSet));
	}

	private void load() {
		for (final Player player : players) {
			delays.add(ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					if (player == null)
						return;
					if (player.getLevel() < 10)
						return;
					if (player.getWorldId() == 110010000 || player.getWorldId() == 120010000) {// SANCTUM, PANDAEMONIUM
						return;
					}
					if (player.getWorldType() == WorldType.ABYSS) {
						return;
					}
					if (player.getStore() != null) {
						return;
					}
					int currentFatigue = player.getCommonData().getFatigue();
					player.getCommonData().setFatigue(currentFatigue + 1);
					if (player.getCommonData().getFatigue() > 100)
						player.getCommonData().setFatigue(100);
					checkFatigue(player);

				}

			}, 180 * 1000, 180 * 1000));// every 3 minutes check this
		}
	}

	public String fatigueMessage(int count) {
		if (count == 0)
			return "Your fatigue is empty";
		return "Your fatigue reached " + count + " %";
	}

	public void resetFatigue() {

		players.clear();// need to clear it before start

		// Reset fatigue
		Iterator<Player> onlinePlayers;
		onlinePlayers = World.getInstance().getPlayersIterator();
		while (onlinePlayers.hasNext()) {
			Player activePlayer = onlinePlayers.next();
			try {
				activePlayer.getCommonData().setFatigue(0);
				activePlayer.getCommonData().setFatigueRecover(1);
				activePlayer.getCommonData().setFatigueReset(1);
				players.add(activePlayer);
			}
			catch (Exception e) {
				log.error("[FatigueService] Error while reset player fatigue " + e.getMessage());
			}
		}
		log.info("[FatigueService] All players fatigue are reseted...");
		load();
		log.info("[FatigueService] Fatigue got reseted...");
	}

	public static FatigueService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final FatigueService instance = new FatigueService();
	}
}
