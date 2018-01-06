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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.actions.AbstractItemAction;
import com.aionemu.gameserver.model.templates.item.actions.CosmeticItemAction;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.RenameService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
public class CM_APPEARANCE extends AionClientPacket {

	private int type;
	private int itemObjId;
	private String name;

	public CM_APPEARANCE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		type = readC();
		readC();
		readH();
		itemObjId = readD();
		switch (type) {
			case 0:
			case 1:
				name = readS();
				break;
		}

	}

	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();

		switch (type) {
			case 0: // Change Char Name,
				if (RenameService.renamePlayer(player, player.getName(), name, itemObjId)) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400157, name));
				}
				break;
			case 1: // Change Legion Name
				if (RenameService.renameLegion(player, name, itemObjId)) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400158, name));
				}
				break;
			case 2: // cosmetic items
				Item item = player.getInventory().getItemByObjId(itemObjId);
				if (item != null) {
					for (AbstractItemAction action : item.getItemTemplate().getActions().getItemActions()) {
						if (action instanceof CosmeticItemAction) {
							if (!action.canAct(player, null, null)) {
								return;
							}
							action.act(player, null, item);
							break;
						}
					}
				}
				break;
		}
	}
}
