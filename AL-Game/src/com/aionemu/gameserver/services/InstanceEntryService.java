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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.InstanceEntryCostEnum;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.InstanceCooltime;

public class InstanceEntryService {

	public void onResetInstanceEntry(Player player, int worldId, InstanceEntryCostEnum type) {
		InstanceCooltime time = DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(worldId);
		if (type == InstanceEntryCostEnum.KINAH && player.getInventory().getKinah() >= time.getPrice()) {
			player.getInventory().decreaseKinah(time.getPrice());
		} else if (type == InstanceEntryCostEnum.PC_COIN && player.getInventory().getItemCountByItemId(time.getComponent()) >= time.getComponentCount()) {
			player.getInventory().decreaseByItemId(time.getComponent(), time.getComponentCount());
		} else {
			if (type != InstanceEntryCostEnum.LUNA || player.getLunaAccount() < time.getLuna()) {
				return;
			}
			player.setLunaAccount(player.getLunaAccount() - time.getLuna());
		}
		player.getPortalCooldownList().reduceEntry(worldId);
	}

	public static InstanceEntryService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final InstanceEntryService instance = new InstanceEntryService();
	}
}
