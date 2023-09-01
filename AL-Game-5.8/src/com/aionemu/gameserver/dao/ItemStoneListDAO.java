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
package com.aionemu.gameserver.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.model.items.ManaStone;

/**
 * @author ATracer modified by Wakizashi
 * @rework FrozenKiller
 */
public abstract class ItemStoneListDAO implements DAO {

	/**
	 * Loads stones of item
	 *
	 * @param items
	 *            list of items to load stones
	 */
	public abstract void load(Collection<Item> items);

	public abstract void storeManaStones(Set<ManaStone> manaStones);

	public abstract void storeFusionStones(Set<ManaStone> fusionStones);

	public abstract void storeIdianStones(IdianStone idianStone);

	/**
	 * Saves stones of player
	 *
	 * @param player
	 *            whos stones we need to save
	 */
	public void save(Player player) {
		save(player.getAllItems());
	}

	public abstract void save(List<Item> items);

	@Override
	public String getClassName() {
		return ItemStoneListDAO.class.getName();
	}
}
