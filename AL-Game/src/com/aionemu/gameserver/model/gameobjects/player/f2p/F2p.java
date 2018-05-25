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
package com.aionemu.gameserver.model.gameobjects.player.f2p;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.Free2PlayDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;

/**
 * edited by teenwolf
 */
public class F2p {

	private Player owner;
	private F2pAccount f2pAccount;

	public F2p(Player owner) {
		this.owner = owner;
	}

	public void add(F2pAccount f2pacc, boolean isNew) {
		f2pAccount = f2pacc;
		f2pacc.setActive(true);
		if (isNew) {
			if (f2pacc.getExpireTime() != 0) {
				ExpireTimerTask.getInstance().addTask(f2pacc, owner);
			}
			DAOManager.getDAO(Free2PlayDAO.class).storeF2p(owner.getPlayerAccount().getId(), f2pacc.getExpireTime());
		}
	}

	public void update(F2pAccount f2pacc, boolean isNew) {
		f2pAccount = f2pacc;
		f2pacc.setActive(true);
		if (isNew) {
			if (f2pacc.getExpireTime() != 0) {
				ExpireTimerTask.getInstance().addTask(f2pacc, owner);
			}
			DAOManager.getDAO(Free2PlayDAO.class).updateF2p(owner.getPlayerAccount().getId(), f2pacc.getExpireTime());
		}
	}

	public F2pAccount getF2pAccount() {
		return f2pAccount;
	}

	public boolean remove() {
		if (f2pAccount != null) {
			f2pAccount.setActive(false);
			DAOManager.getDAO(Free2PlayDAO.class).deleteF2p(owner.getPlayerAccount().getId());
			owner.getEquipment().checkRankLimitItems();
			return true;
		}
		return false;
	}
}
