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
import java.util.ArrayList;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * This blob is sending info about the item that were fused with current item.
 *
 * @author -Nemesiss-
 * @modified Rolandas
 */
public class CompositeItemBlobEntry extends ItemBlobEntry {

	CompositeItemBlobEntry() {
		super(ItemBlobType.COMPOSITE_ITEM);
	}

	@Override
	public void writeThisBlob(ByteBuffer buf) {
		Item item = ownerItem;

		writeD(buf, item.getFusionedItemId());
		writeFusionStones(buf);
		writeH(buf, 0);
		writeB(buf, new byte[40]); // TODO
	}

	private void writeFusionStones(ByteBuffer buf) {
		Item item = ownerItem;
		int count = 0;

		if (item.hasFusionStones()) {
			Set<ManaStone> itemStones = item.getFusionStones();
			ArrayList<ManaStone> basicStones = new ArrayList<ManaStone>();

			for (ManaStone itemStone : itemStones) {
				basicStones.add(itemStone);
			}

			for (ManaStone basicFusionStone : basicStones) {
				if (count == 6) {
					break;
				}
				writeD(buf, basicFusionStone.getItemId());
				count++;
			}
			skip(buf, (6 - count) * 4);
		}
		else {
			skip(buf, 24);
		}
	}

	@Override
	public int getSize() {
		return 70;
		// return 12 * 2 + 6;
	}
}
