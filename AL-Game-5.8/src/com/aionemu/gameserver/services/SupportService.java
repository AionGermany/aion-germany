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

import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.Support;
import com.aionemu.gameserver.model.gameobjects.player.FriendList.Status;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author ThunderBolt
 */
public class SupportService {

	private static final Logger log = LoggerFactory.getLogger(SupportService.class);
	Queue<Support> supports = new ConcurrentLinkedQueue<Support>();
	Set<Player> players = Collections.newSetFromMap(new ConcurrentHashMap<Player, Boolean>());

	private SupportService() {
		log.info("SupportService initialized");
	}

	public void addTicket(Support support) {
		if (!players.add(support.getOwner())) {
			return;
		}

		supports.add(support);

		for (Player player : World.getInstance().getAllPlayers()) {
			if (!player.isGM()) {
				continue;
			}

			if (player.getFriendList().getStatus() == Status.ONLINE) {
				PacketSendUtility.sendSys2Message(player, "Ticket", "New support from " + support.getOwner().getName() + "!");
			}
		}
	}

	public Support getTicket() {
		Support support = supports.poll();
		if (support == null) {
			return null;
		}

		players.remove(support.getOwner());

		while (support != null && !support.getOwner().isOnline()) {
			support = supports.poll();
			players.remove(support.getOwner());
		}

		return support;
	}

	public Support peek() {
		return supports.peek();
	}

	public boolean hasTicket(Player player) {
		return players.contains(player);
	}

	public void onPlayerLogout(Player player) {
		// We just need to cleanup our own mess, so we can run this parallel to the logout thread
		ThreadPoolManager.getInstance().schedule(new LogoutWorker(player), 0);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final SupportService instance = new SupportService();
	}

	public static SupportService getInstance() {
		return SingletonHolder.instance;
	}

	private class LogoutWorker implements Runnable {

		private Player player;

		public LogoutWorker(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			players.remove(player);
			Iterator<Support> it = supports.iterator();

			while (it.hasNext()) {
				Support support = it.next();

				if (support.getOwner() == player) {
					it.remove();
				}
			}
		}
	}
}
