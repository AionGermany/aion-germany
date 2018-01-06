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
package com.aionemu.gameserver.services.player.CreativityPanel;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CREATIVITY_POINTS_APPLY;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Agility;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Health;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Knowledge;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Power;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Precision;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Will;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CreativityStatsService {

	public void onEssenceApply(Player player, int type, int size, int id, int point) {
		if (player.isHighDaeva()) {
			player.getCP().addPoint(player, id, point);
			switch (id) {
				case 1:
					player.setCPSlot1(point);
					Power.getInstance().onChange(player, point);
					break;
				case 2:
					player.setCPSlot2(point);
					Health.getInstance().onChange(player, point);
					break;
				case 3:
					player.setCPSlot3(point);
					Agility.getInstance().onChange(player, point);
					break;
				case 4:
					player.setCPSlot4(point);
					Precision.getInstance().onChange(player, point);
					break;
				case 5:
					player.setCPSlot5(point);
					Knowledge.getInstance().onChange(player, point);
					break;
				case 6:
					player.setCPSlot6(point);
					Will.getInstance().onChange(player, point);
					break;
			}
			PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(type, size, id, point));
		}
	}

	public static CreativityStatsService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final CreativityStatsService INSTANCE = new CreativityStatsService();
	}
}
