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
package com.aionemu.gameserver.model.trade;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.RepurchaseService;

/**
 * @author xTz
 */
public class RepurchaseList {

	private final int sellerObjId;
	private List<Item> repurchases = new ArrayList<Item>();

	public RepurchaseList(int sellerObjId) {
		this.sellerObjId = sellerObjId;
	}

	/**
	 * @param player
	 * @param itemObjectId
	 * @param count
	 */
	public void addRepurchaseItem(Player player, int itemObjectId, long count) {
		Item item = RepurchaseService.getInstance().getRepurchaseItem(player, itemObjectId);
		if (item != null) {
			repurchases.add(item);
		}
	}

	/**
	 * @return the tradeItems
	 */
	public List<Item> getRepurchaseItems() {
		return repurchases;
	}

	public int size() {
		return repurchases.size();
	}

	public final int getSellerObjId() {
		return sellerObjId;
	}
}
