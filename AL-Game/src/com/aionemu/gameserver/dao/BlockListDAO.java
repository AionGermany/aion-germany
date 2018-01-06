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
import com.aionemu.gameserver.model.gameobjects.player.BlockList;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * Responsible for saving and loading data on players' block lists
 *
 * @author Ben
 */
public abstract class BlockListDAO implements DAO {

	/**
	 * Loads the blocklist for the player given
	 *
	 * @param player
	 * @return BlockList
	 */
	public abstract BlockList load(Player player);

	/**
	 * Adds the given object id to the list of blocked players for the given player
	 *
	 * @param playerObjId
	 *            ID of player to edit the blocklist of
	 * @param objIdToBlock
	 *            ID of player to add to the blocklist
	 * @return Success
	 */
	public abstract boolean addBlockedUser(int playerObjId, int objIdToBlock, String reason);

	/**
	 * Deletes the given object id from the list of blocked players for the given player
	 *
	 * @param playerObjId
	 *            ID of player to edit the blocklist of
	 * @param objIdToDelete
	 *            ID of player to remove from the blocklist
	 * @return Success
	 */
	public abstract boolean delBlockedUser(int playerObjId, int objIdToDelete);

	/**
	 * Sets the reason for blocking a player
	 *
	 * @param playerObjId
	 *            Object ID of the player whos list is being edited
	 * @param blockedObjId
	 *            Object ID of the player whos reason is being edited
	 * @param reason
	 *            The reason to be set
	 * @return true or false
	 */
	public abstract boolean setReason(int playerObjId, int blockedObjId, String reason);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClassName() {
		return BlockListDAO.class.getName();
	}
}
