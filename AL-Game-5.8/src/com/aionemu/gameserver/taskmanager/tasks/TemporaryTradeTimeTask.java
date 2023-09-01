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
package com.aionemu.gameserver.taskmanager.tasks;

import java.util.Collection;
import java.util.Map;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.taskmanager.AbstractPeriodicTaskManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Mr. Poke
 */
public class TemporaryTradeTimeTask extends AbstractPeriodicTaskManager {

	private final FastMap<Item, Collection<Integer>> items = new FastMap<Item, Collection<Integer>>();
	private final FastMap<Integer, Item> itemById = new FastMap<Integer, Item>();

	/**
	 * @param period
	 */
	public TemporaryTradeTimeTask() {
		super(1000);
	}

	public static TemporaryTradeTimeTask getInstance() {
		return SingletonHolder._instance;
	}

	public void addTask(Item item, Collection<Integer> players) {
		writeLock();
		try {
			items.put(item, players);
			itemById.put(item.getObjectId(), item);
		}
		finally {
			writeUnlock();
		}
	}

	public boolean canTrade(Item item, int playerObjectId) {
		Collection<Integer> players = items.get(item);
		if (players == null) {
			return false;
		}
		return players.contains(playerObjectId);
	}

	public boolean hasItem(Item item) {
		readLock();
		try {
			return items.containsKey(item);
		}
		finally {
			readUnlock();
		}
	}

	public Item getItem(int objectId) {
		readLock();
		try {
			return itemById.get(objectId);
		}
		finally {
			readUnlock();
		}
	}

	@Override
	public void run() {
		writeLock();
		try {
			for (Map.Entry<Item, Collection<Integer>> entry : items.entrySet()) {
				Item item = entry.getKey();
				int time = (item.getTemporaryExchangeTime() - (int) (System.currentTimeMillis() / 1000));
				if (time == 60) {
					for (int playerId : entry.getValue()) {
						Player player = World.getInstance().findPlayer(playerId);
						if (player != null) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_END_OF_EXCHANGE_TIME(item.getNameId(), time));
						}
					}
				}
				else if (time <= 0) {
					for (int playerId : entry.getValue()) {
						Player player = World.getInstance().findPlayer(playerId);
						if (player != null) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EXCHANGE_TIME_OVER(item.getNameId()));
						}
					}
					item.setTemporaryExchangeTime(0);
					items.remove(item);
					itemById.remove(item.getObjectId());
				}
			}
		}
		finally {
			writeUnlock();
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final TemporaryTradeTimeTask _instance = new TemporaryTradeTimeTask();
	}
}
