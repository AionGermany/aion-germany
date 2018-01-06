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
package com.aionemu.gameserver.network;

import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.NetworkBannedDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Alex
 */
public class NetworkBannedManager {

	private Map<String, NetworkBanEntry> bannedList = new FastMap<>();
	private final Logger log = LoggerFactory.getLogger(NetworkBannedManager.class);

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final NetworkBannedManager networkban = new NetworkBannedManager();
	}

	public static NetworkBannedManager getInstance() {
		return SingletonHolder.networkban;
	}

	private final NetworkBannedDAO dao = DAOManager.getDAO(NetworkBannedDAO.class);

	public NetworkBannedManager() {
		dao.cleanExpiredBans();
		bannedList = dao.load();
		log.info("Loaded " + bannedList.size() + " banned ip list.");
	}

	public final void banAddress(String address, long newTime, String details) {
		for (Player player : World.getInstance().getAllPlayers()) {
			if (player.getClientConnection().getIP().startsWith(address)) {
				player.getClientConnection().closeNow();
			}
		}
		NetworkBanEntry entry;
		if (bannedList.containsKey(address)) {
			if (bannedList.get(address).isActiveTill(newTime)) {
				return;
			}
			else {
				entry = bannedList.get(address);
				entry.updateTime(newTime);
			}
		}
		else {
			entry = new NetworkBanEntry(address, newTime);
		}

		entry.setDetails(details);

		bannedList.put(address, entry);
		this.dao.update(entry);
		log.info("[NetworkBannedManager] banned " + address + " to " + entry.getTime().toString() + " for " + details);
	}

	public final boolean unbanAddress(String address, String details) {
		if (bannedList.containsKey(address)) {
			bannedList.remove(address);
			this.dao.remove(address);
			log.info("[NetworkBannedManager] unbanned " + address + " for " + details);
			// LoginServer.getInstance().sendPacket(new SM_MACBAN_CONTROL((byte) 0, address, 0, details));
			return true;
		}
		else {
			return false;
		}
	}

	public final boolean isBanned(String address) {
		if (bannedList.containsKey(address)) {
			log.info("IP: " + address + " is such banned list!");
			return this.bannedList.get(address).isActive();
		}
		else {
			return false;
		}
	}

	public final void dbLoad(String address, long time, String details) {
		this.bannedList.put(address, new NetworkBanEntry(address, new Timestamp(time), details));
	}
}
