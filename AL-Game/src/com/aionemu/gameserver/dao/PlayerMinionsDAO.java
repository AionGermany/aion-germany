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
import com.aionemu.gameserver.model.templates.minion.MinionDopingBag;

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

	public abstract void updateMinionName(MinionCommonData minionCommonData);

	public abstract List<MinionCommonData> getPlayerMinions(Player player);
	
	public abstract void updatePlayerMinionGrowthPoint(Player player, MinionCommonData minionCommonData);
	
	public abstract boolean PlayerMinions(int playerid, int miniona);
	
	public abstract void evolutionMinion(Player player, MinionCommonData minionCommonData);
	
	public abstract void lockMinions(Player player, int minionObjId, int isLocked);
	
	public abstract void saveDopingBag(Player player, MinionCommonData minionCommonData, MinionDopingBag bag);
	
	public abstract void saveBirthday(MinionCommonData minionCommonData);
	
}
