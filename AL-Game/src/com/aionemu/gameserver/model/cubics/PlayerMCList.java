/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 * Aion-Lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Aion-Lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.cubics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerCubicsDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Phantom_KNA
 */
public final class PlayerMCList implements MCList<Player> {

	private final Map<Integer, PlayerMCEntry> entry = new HashMap<Integer, PlayerMCEntry>(0);

	public PlayerMCList() {
	}

	public PlayerMCList(List<PlayerMCEntry> list) {
		this();
		for (PlayerMCEntry playerMCEntry : list) {
			entry.put(playerMCEntry.getCubeId(), playerMCEntry);
		}
	}

	public PlayerMCEntry[] getAllMC() {
		ArrayList<PlayerMCEntry> list = new ArrayList<PlayerMCEntry>();
		list.addAll(this.entry.values());
		return (PlayerMCEntry[]) list.toArray(new PlayerMCEntry[list.size()]);
	}

	public PlayerMCEntry[] getBasicMC() {
		return this.entry.values().toArray(new PlayerMCEntry[this.entry.size()]);
	}

	@Override
	public boolean add(Player crature, int cubeid, int rank, int level, int stat_value, int category) {
		return add(crature, cubeid, rank, level, stat_value, category, PersistentState.NEW);
	}

	private synchronized boolean add(Player player, int cubeid, int rank, int level, int stat_value, int category,
			PersistentState paramPersistentState) {
		this.entry.put(cubeid, new PlayerMCEntry(cubeid, rank, level, stat_value, category, paramPersistentState));
		DAOManager.getDAO(PlayerCubicsDAO.class).store(player.getObjectId(), cubeid, rank, level, stat_value, category);
		return true;
	}

	@Override
	public synchronized boolean remove(Player player, int cubeid) {
		PlayerMCEntry playerMCEntry = this.entry.get(cubeid);
		if (playerMCEntry != null) {
			playerMCEntry.setPersistentState(PersistentState.DELETED);
			this.entry.remove(cubeid);
			DAOManager.getDAO(PlayerCubicsDAO.class).delete(player.getObjectId(), cubeid);
		}
		return this.entry != null;
	}

	@Override
	public int size() {
		return this.entry.size();
	}
}
