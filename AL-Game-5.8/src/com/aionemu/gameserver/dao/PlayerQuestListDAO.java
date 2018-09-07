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
import com.aionemu.gameserver.model.gameobjects.player.QuestStateList;

/**
 * @author MrPoke
 * @modified vlog
 */
public abstract class PlayerQuestListDAO implements DAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClassName() {
		return PlayerQuestListDAO.class.getName();
	}

	/**
	 * @param playerId
	 * @return QuestStateList
	 */
	public abstract QuestStateList load(final Player player);

	/**
	 * @param Player
	 * @param QuestStateList
	 */
	public abstract void store(final Player player);
}
