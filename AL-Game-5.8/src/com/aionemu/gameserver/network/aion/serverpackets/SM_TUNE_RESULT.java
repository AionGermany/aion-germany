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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
//import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author FrozenKiller
 */
public class SM_TUNE_RESULT extends AionServerPacket {

	private final Player player;
	private int itemObjectId;
	private int tuningScrollId;
	private int itemId;

	public SM_TUNE_RESULT(Player player, int itemObjectId, int tuningScrollId, int itemId) {
		this.player = player;
		this.itemObjectId = itemObjectId;
		this.tuningScrollId = tuningScrollId;
		this.itemId = itemId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Item item = player.getInventory().getItemByObjId(itemObjectId);
		writeD(itemObjectId);
		writeD(tuningScrollId);
		switch (item.getEquipmentType()) {
			case ARMOR: {
				writeH(5);
			}
			case WEAPON: {
				writeH(10);
			}
			default:
				break;
		};
		writeC(item.getEnchantLevel());
		writeD(itemId);
		writeH(0); // TODO found value 256,512,1024 (maybe slot ?)
		// int count = 0;
		// for (ManaStone manaStone : item.getItemStones()) {
		// writeD(manaStone.getItemId());
		// count++;
		// }
		//
		// if (count < 6 ) {
		// for (int i = count; i < 6; i++) {
		// writeD(0);
		// count++;
		// }
		// }
		// System.out.println("Count: " + count);
		// System.out.println("ManaStone: " + item.getItemStones());
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);

		if (item.getGodStone() != null) {
			writeD(item.getGodStone().getItemId());
		}
		else {
			writeD(0);
		}

		writeB(new byte[13]); // Spacer

		if (item.getIdianStone() != null) {
			writeD(item.getIdianStone().getItemId());
		}
		else {
			writeD(0);
		}

		writeC(2); // TODO found value 0 and 2
		writeB(new byte[120]); // Garbage
		if (tuningScrollId > 0) {
			writeC(1); // Show Window ?
			writeC(1); // Show Window ?
		}
		else {
			writeC(0);
			writeC(0);
		}
	}
}
