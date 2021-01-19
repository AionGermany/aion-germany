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

import java.util.List;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Falke_34
 */
public abstract class PlayerMinionsDAO implements DAO {

	@Override
	public final String getClassName() {
		return PlayerMinionsDAO.class.getName();
	}

	public abstract void insertPlayerMinion(MinionCommonData minionCommonData);

	public abstract void removePlayerMinion(Player player, int minionObjId);

	public abstract List<MinionCommonData> getPlayerMinions(Player player);

	public abstract void setTime(Player player, int minionId, long time);

	public abstract void updateName(MinionCommonData minionCommonData);

	public abstract void updateMinionGrowth(MinionCommonData minionCommonData);

	public abstract void updateMinionLock(MinionCommonData minionCommonData);

	public abstract boolean isNameUsed(int playerId, final String name);
}
