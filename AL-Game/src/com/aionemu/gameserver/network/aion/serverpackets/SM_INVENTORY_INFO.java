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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob;

/**
 * In this packet Server is sending Inventory Info
 *
 * @author -Nemesiss-, alexa026, Avol ;d modified by ATracer, Rolandas
 */
public class SM_INVENTORY_INFO extends AionServerPacket {

	private boolean isFirstPacket;
	private boolean isPopUp;
	private int cubeExpandsSize = 0;
	private List<Item> items;
	private Player player;

	public SM_INVENTORY_INFO(boolean isFirstPacket, List<Item> items, int cubeExpandsSize, boolean isPopUp, Player player) {
		// this should prevent client crashes but need to discover when item is null
		items.removeAll(Collections.singletonList(null));
		this.isFirstPacket = isFirstPacket;
		this.items = items;
		this.cubeExpandsSize = cubeExpandsSize;
		this.isPopUp = isPopUp;
		this.player = player;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		// something wrong with cube part.
		writeC(isFirstPacket ? 1 : 0);
		writeC(cubeExpandsSize); // cube size from npc (so max 5 for now)
		writeC(0);
		writeC(isPopUp ? 1 : 0); // popup cube on login
		writeH(items.size()); // number of entries
		for (Item item : items) {
			writeItemInfo(item);
		}
	}

	private void writeItemInfo(Item item) {
		ItemTemplate itemTemplate = item.getItemTemplate();

		writeD(item.getObjectId());
		writeD(itemTemplate.getTemplateId());
		writeNameId(itemTemplate.getNameId());

		ItemInfoBlob itemInfoBlob = ItemInfoBlob.getFullBlob(player, item);
		itemInfoBlob.writeMe(getBuf());

		// invisible -1, visible is a slot
		writeH((int) (item.getEquipmentSlot() & 0xFFFF));

		// probably a right to equip the item, related to passive skill learn
		writeC(itemTemplate.isCloth() ? 1 : 0);
	}
}
