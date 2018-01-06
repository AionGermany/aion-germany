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
 * @author nrg
 */
public abstract class PlayerCooldownsDAO implements DAO {

	/**
	 * Returns unique identifier for PlayerCooldownsDAO
	 *
	 * @return unique identifier for PlayerCooldownsDAO
	 */
	@Override
	public final String getClassName() {
		return PlayerCooldownsDAO.class.getName();
	}

	/**
	 * @param player
	 */
	public abstract void loadPlayerCooldowns(Player player);

	/**
	 * @param player
	 */
	public abstract void storePlayerCooldowns(Player player);
}
