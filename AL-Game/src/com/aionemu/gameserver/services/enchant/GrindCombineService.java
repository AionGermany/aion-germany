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
package com.aionemu.gameserver.services.enchant;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.grind.GrindCombine;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class GrindCombineService {

	public static void combineGrind(Player player, Item mat, Item mat2) {
		int index;
		GrindCombine combine = DataManager.GRIND_COMBINE_DATA.getCombine(player, mat.getItemTemplate().getGrindColor(), mat2.getItemTemplate().getGrindColor());
		if (combine == null) {
			return;
		}
		if (player.getInventory().getKinah() >= (long) combine.getPrice()) {
			player.getInventory().decreaseKinah(combine.getPrice());
		}
		if (player.getInventory().getKinah() < (long) combine.getPrice()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return;
		}
		if (mat == null || mat2 == null) {
			return;
		}
		if (player.getInventory().decreaseByObjectId(mat.getObjectId(), 1) && player.getInventory().decreaseByObjectId(mat2.getObjectId(), 1) && (index = Rnd.get((int) 0, (int) (combine.getRewards().size() - 1))) != -1) {
			int itemId = combine.getRewards().get(index).getItemId();
			ItemService.addItem(player, itemId, 1);
		}
	}
}
