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
package com.aionemu.gameserver.model.dorinerk_wardrobe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerWardrobeDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public final class PlayerWardrobeList implements WardrobeList<Player> {

	private final Map<Integer, PlayerWardrobeEntry> entry;

	public PlayerWardrobeList() {
		this.entry = new HashMap<Integer, PlayerWardrobeEntry>(0);
	}

	public PlayerWardrobeList(List<PlayerWardrobeEntry> entries) {
		this();
		for (PlayerWardrobeEntry e : entries) {
			entry.put(e.getItemId(), e);
		}
	}

	public PlayerWardrobeEntry[] getAllWardrobe() {
		List<PlayerWardrobeEntry> allWardrobe = new ArrayList<PlayerWardrobeEntry>();
		allWardrobe.addAll(entry.values());
		return allWardrobe.toArray(new PlayerWardrobeEntry[allWardrobe.size()]);
	}

	public PlayerWardrobeEntry[] getBasicWardrobe() {
		return entry.values().toArray(new PlayerWardrobeEntry[entry.size()]);
	}

	@Override
	public boolean addItem(Player player, int itemId, int slot, int reskin_count) {
		return addItem(player, itemId, slot, reskin_count, PersistentState.NEW);
	}

	private synchronized boolean addItem(Player player, int itemId, int slot, int reskin_count, PersistentState state) {
		entry.put(itemId, new PlayerWardrobeEntry(itemId, slot, reskin_count, state));
		DAOManager.getDAO(PlayerWardrobeDAO.class).store(player.getObjectId(), itemId, slot, reskin_count);
		return true;
	}

	@Override
	public synchronized boolean removeItem(Player player, int itemId) {
		PlayerWardrobeEntry entries = entry.get(itemId);
		if (entries != null) {
			entries.setPersistentState(PersistentState.DELETED);
			entry.remove(itemId);
			DAOManager.getDAO(PlayerWardrobeDAO.class).delete(player.getObjectId(), itemId);
		}
		return entry != null;
	}

	@Override
	public int size() {
		return entry.size();
	}

}
