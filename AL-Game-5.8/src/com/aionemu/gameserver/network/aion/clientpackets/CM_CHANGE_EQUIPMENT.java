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
package com.aionemu.gameserver.network.aion.clientpackets;

import java.util.HashMap;
import java.util.Map.Entry;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author FrozenKiller
 */

public class CM_CHANGE_EQUIPMENT extends AionClientPacket {

	private int size;
	private int action;
	private int itemObjId;
	private static HashMap<Integer, Integer> itemList = new HashMap<Integer, Integer>();

	public CM_CHANGE_EQUIPMENT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		size = readH();
		for (int i = 0; i < size; i++) {
			action = readD();
			readD();
			readD();
			itemObjId = readD();
			itemList.put(itemObjId, action);
		}
	}

	@Override
	protected void runImpl() {
		final Player activePlayer = getConnection().getActivePlayer();
		Equipment equipment = activePlayer.getEquipment();

		if (itemList.size() != 0) {
			for (Entry<Integer, Integer> entry : itemList.entrySet()) {
				int itemObjectId = entry.getKey();
				int action = entry.getValue();
				if (action == 1) {
					equipment.unEquipItem(itemObjectId, equipment.getEquippedItemByObjId(itemObjectId).getEquipmentSlot());
				}
				else {
					Item item = activePlayer.getInventory().getItemByObjId(itemObjectId);
					equipment.equipItem(item.getObjectId(), item.getItemTemplate().getItemSlot());
				}
			}
			itemList.clear();
		}
	}
}
