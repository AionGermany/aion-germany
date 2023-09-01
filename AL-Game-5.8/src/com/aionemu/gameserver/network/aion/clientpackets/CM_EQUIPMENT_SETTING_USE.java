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

import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_EQUIPMENT_SETTING_USE extends AionClientPacket {

	private int slotRead;
	private int itemObjtId;
	private int action;
	private int size;

	public CM_EQUIPMENT_SETTING_USE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
		size = 0;
	}

	protected void readImpl() {
        size = readH();
        for (int i = 0; i < size; ++i) {
            action = readD();
            slotRead = readD();
            readD();
            itemObjtId = readD();
        }
    }

	protected void runImpl() {
		final Player activePlayer = getConnection().getActivePlayer();
		Equipment equipment = activePlayer.getEquipment();
        activePlayer.getController().cancelUseItem();
        
        if (!activePlayer.isSpawned() || activePlayer.getController().isInShutdownProgress()) {
            return;
        }
        
        if (activePlayer.getInventory().isFull() || activePlayer.getInventory().getFreeSlots() < size) {
            return;
        }
        
        if (action == 0) {
            equipment.equipItem(itemObjtId, slotRead);
        }
        else {
            equipment.unEquipItem(itemObjtId, slotRead);
        }
        
        PacketSendUtility.sendPacket(activePlayer, new SM_SYSTEM_MESSAGE(1404124, new Object[0]));
        PacketSendUtility.broadcastPacket(activePlayer, new SM_UPDATE_PLAYER_APPEARANCE(activePlayer.getObjectId(), equipment.getEquippedForApparence()), true);
    }
}
