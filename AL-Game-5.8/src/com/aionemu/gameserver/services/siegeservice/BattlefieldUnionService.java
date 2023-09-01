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
package com.aionemu.gameserver.services.siegeservice;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BATTLEFIELD_UNION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BATTLEFIELD_UNION_REGISTER;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapType;

public class BattlefieldUnionService {

	public void onEnterWorld(Player player) {
		if (player.getWorldId() == WorldMapType.RESHANTA.getId()) {
			PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION(1132, true, 0));
		}
	}

	@SuppressWarnings("unused")
	public void onRegister(Player player, int requestId) {
		if (!false) {
			PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION_REGISTER(requestId, true));
			PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION(1132, true, 2));
		} 
		else {
			PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION_REGISTER(requestId, false));
			PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION(1132, true, 0));
		}
	}

	public static BattlefieldUnionService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final BattlefieldUnionService INSTANCE = new BattlefieldUnionService();
	}
}
