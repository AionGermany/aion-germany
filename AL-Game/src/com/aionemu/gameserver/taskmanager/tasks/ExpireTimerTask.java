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

import java.util.Iterator;
import java.util.Map;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.taskmanager.AbstractPeriodicTaskManager;

import javolution.util.FastMap;

/**
 * @author Mr. Poke
 */
public class ExpireTimerTask extends AbstractPeriodicTaskManager {

	private FastMap<IExpirable, Player> expirables = new FastMap<IExpirable, Player>();

	/**
	 * @param period
	 */
	public ExpireTimerTask() {
		super(1000);
	}

	public static ExpireTimerTask getInstance() {
		return SingletonHolder._instance;
	}

	public void addTask(IExpirable expirable, Player player) {
		writeLock();
		try {
			expirables.put(expirable, player);
		}
		finally {
			writeUnlock();
		}
	}

	public void removePlayer(Player player) {
		writeLock();
		try {
			for (Map.Entry<IExpirable, Player> entry : expirables.entrySet()) {
				if (entry.getValue() == player) {
					expirables.remove(entry.getKey());
				}
			}
		}
		finally {
			writeUnlock();
		}
	}

	@Override
	public void run() {
		writeLock();
		try {
			int timeNow = (int) (System.currentTimeMillis() / 1000);
			for (Iterator<Map.Entry<IExpirable, Player>> i = expirables.entrySet().iterator(); i.hasNext();) {
				Map.Entry<IExpirable, Player> entry = i.next();
				IExpirable expirable = entry.getKey();
				Player player = entry.getValue();
				int min = (expirable.getExpireTime() - timeNow);
				if (min < 0 && expirable.canExpireNow()) {
					expirable.expireEnd(player);
					i.remove();
					continue;
				}
				switch (min) {
					case 1800:
					case 900:
					case 600:
					case 300:
					case 60:
						expirable.expireMessage(player, min / 60);
						break;
				}
			}
		}
		finally {
			writeUnlock();
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final ExpireTimerTask _instance = new ExpireTimerTask();
	}
}
