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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 */
public class SM_SPLIT_ITEM extends AionServerPacket {

	// sobald der Item Stabel voll ist wird ein neuer begonnen
	// getestet mit Machtscherben und Verzauberungsstaub

	private int itemId;
	private long itemCount;

	public SM_SPLIT_ITEM(int itemId, int itemCount) {
		this.itemId = itemId;
		this.itemCount = itemCount;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(0); // TODO
		writeD(itemId); // Item Id
		writeQ(itemCount); // count
	}
}
