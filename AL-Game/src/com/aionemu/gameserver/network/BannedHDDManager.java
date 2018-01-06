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
import com.aionemu.gameserver.dao.BannedHddDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Alex
 */
public class BannedHDDManager {

	private final Logger log = LoggerFactory.getLogger(BannedHDDManager.class);

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final BannedHDDManager hddmanager = new BannedHDDManager();
	}

	public static BannedHDDManager getInstance() {
		return SingletonHolder.hddmanager;
	}

	private final BannedHddDAO dao = DAOManager.getDAO(BannedHddDAO.class);

	public BannedHDDManager() {
		dao.cleanExpiredBans();
		bannedHddList = dao.load();
		log.info("Loaded " + bannedHddList.size() + " banned hdd list.");
	}

	private Map<String, BannedHDDEntry> bannedHddList = new FastMap<>();

	public final void banAddress(String address, long newTime, String details) {
		for (Player player : World.getInstance().getAllPlayers()) {
			if (player.getClientConnection().getHddSerial().equals(address)) {
				player.getClientConnection().closeNow();
			}
		}
		BannedHDDEntry entry;
		if (bannedHddList.containsKey(address)) {
			if (bannedHddList.get(address).isActiveTill(newTime)) {
				return;
			}
			else {
				entry = bannedHddList.get(address);
				entry.updateTime(newTime);
			}
		}
		else {
			entry = new BannedHDDEntry(address, newTime);
		}

		entry.setDetails(details);

		bannedHddList.put(address, entry);
		this.dao.update(entry);
		log.info("[BannedHDDManager] banned " + address + " to " + entry.getTime().toString() + " for " + details);
	}

	public final boolean unbanAddress(String address, String details) {
		if (bannedHddList.containsKey(address)) {
			bannedHddList.remove(address);
			this.dao.remove(address);
			log.info("[BannedHDDManager] unbanned " + address + " for " + details);
			// LoginServer.getInstance().sendPacket(new SM_MACBAN_CONTROL((byte) 0, address, 0, details));
			return true;
		}
		else {
			return false;
		}
	}

	public final boolean isBanned(String address) {
		if (bannedHddList.containsKey(address)) {
			log.info("HDD_SERIAL: " + address + " is such banned list!");
			return this.bannedHddList.get(address).isActive();
		}
		else {
			return false;
		}
	}

	public final void dbLoad(String address, long time, String details) {
		this.bannedHddList.put(address, new BannedHDDEntry(address, new Timestamp(time), details));
	}
}
