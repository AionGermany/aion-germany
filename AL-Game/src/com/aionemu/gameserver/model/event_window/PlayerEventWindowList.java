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
package com.aionemu.gameserver.model.event_window;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerEventsWindowDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class PlayerEventWindowList implements EventWindowList<Player> {

	private final Map<Integer, PlayerEventWindowEntry> entry = new HashMap<>(0);

	public PlayerEventWindowList(List<PlayerEventWindowEntry> list) {
		for (PlayerEventWindowEntry playerEventWindowEntry : list) {
			entry.put(playerEventWindowEntry.getId(), playerEventWindowEntry);
		}
	}

	public PlayerEventWindowEntry[] getAll() {
		ArrayList<PlayerEventWindowEntry> arrayList = new ArrayList<PlayerEventWindowEntry>(entry.values());
		return arrayList.toArray(new PlayerEventWindowEntry[arrayList.size()]);
	}

	public PlayerEventWindowEntry[] getBasic() {
		return entry.values().toArray(new PlayerEventWindowEntry[entry.size()]);
	}

	/**
	 * add player event window list
	 */
	private synchronized boolean add(Player player, int remaining, Timestamp timestamp, int Time, PersistentState persistentState) {
		entry.put(remaining, new PlayerEventWindowEntry(remaining, timestamp, Time, persistentState));
		DAOManager.getDAO(PlayerEventsWindowDAO.class).store(player.getPlayerAccount().getId(), remaining, timestamp, Time);
		return true;
	}

	@Override
	public boolean add(Player player, int remaining, Timestamp timestamp, int Time) {
		return add(player, remaining, timestamp, Time, PersistentState.NEW);
	}

	/**
	 * remove player event window list
	 */
	@Override
	public synchronized boolean remove(Player player, int remaining) {
		PlayerEventWindowEntry playerEventWindowEntry = entry.get(remaining);
		if (playerEventWindowEntry != null) {
			playerEventWindowEntry.setPersistentState(PersistentState.DELETED);
			entry.remove(remaining);
			DAOManager.getDAO(PlayerEventsWindowDAO.class).delete(player.getPlayerAccount().getId(), remaining);
		}
		return entry != null;
	}

	/**
	 * size player event window list
	 */
	@Override
	public int size() {
		return entry.size();
	}
}
