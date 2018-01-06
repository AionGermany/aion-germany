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

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Alex
 */
public abstract class EventItemsDAO implements DAO {

	/**
	 * @return unique identifier for EventItemsDAO
	 */
	@Override
	public final String getClassName() {
		return EventItemsDAO.class.getName();
	}

	/**
	 * @param player
	 */
	public abstract void loadItems(Player player);

	/**
	 * @param player
	 */
	public abstract void storeItems(Player player);

	/**
	 * @param itemId
	 */
	public abstract void deleteItems(final int itemId);
}
