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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Collection;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MINIONS;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

public class MinionList {
	private final Player player;
	private int lastUsedObjId;
	private FastMap<Integer, MinionCommonData> minions = new FastMap<Integer, MinionCommonData>();

	public MinionList(Player player) {
		this.player = player;
		loadMinions();
	}

	public void loadMinions() {
		for (MinionCommonData minionCommonData : DAOManager.getDAO(PlayerMinionsDAO.class).getPlayerMinions(player)) {
			if (minionCommonData.getExpireTime() > 0) {
				ExpireTimerTask.getInstance().addTask(minionCommonData, player);
			}
			minions.put(minionCommonData.getObjectId(), minionCommonData);
		}
	}

	public Collection<MinionCommonData> getMinions() {
		return (Collection<MinionCommonData>) minions.values();
	}
	
	public void updateMinionsList() {
		minions.clear();
		for (MinionCommonData minionCommonData : DAOManager.getDAO(PlayerMinionsDAO.class).getPlayerMinions(player)) {
				minions.put(minionCommonData.getObjectId(), minionCommonData);
		}
		if(minions !=null) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(0, player.getMinionList().getMinions()));
		}
		return;
	}

	public MinionCommonData getMinion(int minionObjId) {
		return minions.get(minionObjId);
	}

	public MinionCommonData addNewMinion(Player player, int minionId, String name, String grade, int level) {
		MinionCommonData minionCommonData = new MinionCommonData(minionId, player.getObjectId(), name, grade, level, 0);
		DAOManager.getDAO(PlayerMinionsDAO.class).insertPlayerMinion(minionCommonData);
		DAOManager.getDAO(PlayerMinionsDAO.class).saveBirthday(minionCommonData);
		minions.put(minionId, minionCommonData);
		return minionCommonData;
	}

	public boolean hasMinion(int n) {
		return minions.containsKey(n);
	}

	public void deleteMinion(int minionObjId) {
		if (hasMinion(minionObjId)) {
            DAOManager.getDAO(PlayerMinionsDAO.class).removePlayerMinion(player, minionObjId);
			minions.remove(minionObjId);
		}
	}

	public void setLastUsed(int lastUsedObjId) {
		this.lastUsedObjId = lastUsedObjId;
	}

	public int getLastUsed() {
		return lastUsedObjId;
	}
}
