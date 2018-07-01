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
package com.aionemu.gameserver.network.aion.iteminfo;

import java.nio.ByteBuffer;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * This blob is sent for weapons. It keeps info about slots that weapon can be equipped to.
 *
 * @author -Nemesiss-
 * @modified Rolandas
 */
public class WeaponInfoBlobEntry extends ItemBlobEntry {

	WeaponInfoBlobEntry() {
		super(ItemBlobType.SLOTS_WEAPON);
	}

	@Override
	public void writeThisBlob(ByteBuffer buf) {
		Item item = ownerItem;

		ItemSlot[] slots = ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot());

		// must occupy two slots
		if (item.getItemTemplate().isTwoHandWeapon()) {
			writeQ(buf, 3);
			writeQ(buf, 0);
		}
		else {
			// primary and secondary slots
			writeQ(buf, slots[0].getSlotIdMask());
			writeQ(buf, slots[1].getSlotIdMask());
		}
	}

	@Override
	public int getSize() {
		return 16;
	}
}
