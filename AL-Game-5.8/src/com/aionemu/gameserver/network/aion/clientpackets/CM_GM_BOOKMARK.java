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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gm.GmCommands;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.world.World;

/**
 * @author xTz
 */
public class CM_GM_BOOKMARK extends AionClientPacket {

	private GmCommands command;
	private String playerName;
	private String[] parts;

	public CM_GM_BOOKMARK(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		playerName = readS();
		parts = playerName.split(" ");
		command = GmCommands.getValue(parts[0]);
		playerName = parts[1];
	}

	@Override
	protected void runImpl() {
		Player admin = getConnection().getActivePlayer();
		Player player = World.getInstance().findPlayer(Util.convertName(playerName));
		if (admin == null) {
			return;
		}
		if (admin.getAccessLevel() < AdminConfig.GM_PANEL) {
			return;
		}
		if (player == null) {
			PacketSendUtility.sendMessage(admin, "Could not find an online player with that name.");
			return;
		}
		switch (command) {
			case GM_MAIL_LIST:
				// TODO Show mail box
				break;
			case INVENTORY:
				break;
			case TELEPORTTO:
				TeleportService2.teleportTo(admin, player.getWorldId(), player.getX(), player.getY(), player.getZ());
				break;
			case STATUS:
				// TODO Player Status
				break;
			case SEARCH:
				// TODO Target selected
				break;
			case GM_GUILDHISTORY:
				// TODO Player Legion Info
				break;
			case GM_BUDDY_LIST:
				// TODO FRIEND LIST
				break;
			case RECALL:
				TeleportService2.teleportTo(player, admin.getWorldId(), admin.getX(), admin.getY(), admin.getZ());
			default:
				PacketSendUtility.sendMessage(admin, "Invalid command: " + command.name());
				break;
		}
	}
}
