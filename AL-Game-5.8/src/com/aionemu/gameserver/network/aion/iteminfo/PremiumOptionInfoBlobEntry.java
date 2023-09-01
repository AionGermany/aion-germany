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

import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * @author Rolandas
 */
public class PremiumOptionInfoBlobEntry extends ItemBlobEntry {

	public PremiumOptionInfoBlobEntry() {
		super(ItemBlobType.PREMIUM_OPTION);
	}

	@Override
	public void writeThisBlob(ByteBuffer buf) {
		writeH(buf, ownerItem.getBonusNumber());
		// writeC(buf, ownerItem.getRandomCount());
		writeC(buf, 0);
	}

	@Override
	public int getSize() {
		return 1 + 2;
	}
}
