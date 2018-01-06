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
package com.aionemu.gameserver.model.autogroup;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.commons.taskmanager.AbstractLockManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import javolution.util.FastList;

/**
 * @author xTz
 */
public class LookingForParty extends AbstractLockManager {

	private List<SearchInstance> searchInstances = new ArrayList<SearchInstance>();
	private Player player;
	private long startEnterTime;
	private long penaltyTime;

	public LookingForParty(Player player, int instanceMaskId, EntryRequestType ert) {
		this.player = player;
		searchInstances.add(new SearchInstance(instanceMaskId, ert, ert.isGroupEntry() ? player.getPlayerGroup2().getOnlineMembers() : null));
	}

	public int unregisterInstance(int instanceMaskId) {
		super.writeLock();
		try {
			for (SearchInstance si : searchInstances) {
				if (si.getInstanceMaskId() == instanceMaskId) {
					searchInstances.remove(si);
					return searchInstances.size();
				}
			}
			return searchInstances.size();
		}
		finally {
			super.writeUnlock();
		}
	}

	public List<SearchInstance> getSearchInstances() {
		FastList<SearchInstance> tempList = FastList.newInstance();
		for (SearchInstance si : searchInstances) {
			tempList.add(si);
		}
		return tempList;
	}

	public void addInstanceMaskId(int instanceMaskId, EntryRequestType ert) {
		super.writeLock();
		try {
			searchInstances.add(new SearchInstance(instanceMaskId, ert, ert.isGroupEntry() ? player.getPlayerGroup2().getOnlineMembers() : null));
		}
		finally {
			super.writeUnlock();
		}
	}

	public SearchInstance getSearchInstance(int instanceMaskId) {
		super.readLock();
		try {
			for (SearchInstance si : searchInstances) {
				if (si.getInstanceMaskId() == instanceMaskId) {
					return si;
				}
			}
			return null;
		}
		finally {
			super.readUnlock();
		}
	}

	public boolean isRegistredInstance(int instanceMaskId) {
		for (SearchInstance si : searchInstances) {
			if (si.getInstanceMaskId() == instanceMaskId) {
				return true;
			}
		}
		return false;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setPenaltyTime() {
		penaltyTime = System.currentTimeMillis();
	}

	public boolean hasPenalty() {
		return System.currentTimeMillis() - penaltyTime <= 10000;
	}

	public void setStartEnterTime() {
		startEnterTime = System.currentTimeMillis();
	}

	public boolean isOnStartEnterTask() {
		return System.currentTimeMillis() - startEnterTime <= 120000;
	}
}
