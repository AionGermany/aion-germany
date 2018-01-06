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
package com.aionemu.gameserver.model.monsterbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerMonsterbookDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public final class PlayerMonsterbookList implements MonsterbookList<Player> {

	private final Map<Integer, PlayerMonsterbookEntry> entry;

	public PlayerMonsterbookList() {
		this.entry = new HashMap<Integer, PlayerMonsterbookEntry>(0);
	}

	public PlayerMonsterbookList(List<PlayerMonsterbookEntry> list) {
		this();
		for (PlayerMonsterbookEntry playerMonsterbookEntry : list) {
			entry.put(playerMonsterbookEntry.getId(), playerMonsterbookEntry);
		}
	}

	public PlayerMonsterbookEntry[] getAllMonsterbook() {
		ArrayList<PlayerMonsterbookEntry> list = new ArrayList<PlayerMonsterbookEntry>();
		list.addAll(entry.values());
		return (PlayerMonsterbookEntry[]) list.toArray(new PlayerMonsterbookEntry[list.size()]);
	}

	public PlayerMonsterbookEntry[] getBasicMonsterbook() {
		return entry.values().toArray(new PlayerMonsterbookEntry[entry.size()]);
	}

	@Override
	public boolean add(Player creature, int id, int killCount, int level, int claimReward) {
		return add(creature, id, killCount, level, claimReward, PersistentState.NEW);
	}

	private synchronized boolean add(Player player, int id, int killCount, int level, int claimReward, PersistentState persistentState) {
		entry.put(id, new PlayerMonsterbookEntry(id, killCount, level, claimReward, persistentState));
		DAOManager.getDAO(PlayerMonsterbookDAO.class).store(player.getObjectId(), id, killCount, level, claimReward);
		return true;
	}

	@Override
	public synchronized boolean remove(Player player, int id) {
		PlayerMonsterbookEntry playerMonsterbookEntry = entry.get(id);
		if (playerMonsterbookEntry != null) {
			playerMonsterbookEntry.setPersistentState(PersistentState.DELETED);
			entry.remove(id);
			DAOManager.getDAO(PlayerMonsterbookDAO.class).delete(player.getObjectId(), id);
		}
		return entry != null;
	}

	@Override
	public int size() {
		return entry.size();
	}
}
