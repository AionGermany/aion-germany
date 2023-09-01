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

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOTSPOT_TELEPORT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.HotspotTeleportService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Alcapwnd
 */
public class CM_HOTSPOT_TELEPORT extends AionClientPacket {

	private int action;
	private int teleportId;
	private int price;
	@SuppressWarnings("unused")
	private int unk;

	public CM_HOTSPOT_TELEPORT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {

		action = readC();
		if (action == 1) {
			teleportId = readD();
			price = readD();
			unk = readD();
		}
		else if (action == 2) {
		}
	}

	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();
		if (player.getInventory().getKinah() < price) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOTSPOT_NOT_ENOUGH_COST);
			return;
		}
		if (action == 1) {
			HotspotTeleportService.getInstance().doTeleport(player, teleportId, price);
		}
		else if (action == 2) {
			player.getController().cancelTask(TaskId.HOTSPOT_TELEPORT);
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_HOTSPOT_TELEPORT(2, player.getObjectId()));
		}
	}
}
