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

import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_GROUP_DATA_EXCHANGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
public class CM_GROUP_DATA_EXCHANGE extends AionClientPacket {

	private int groupType;
	private int action;
	private int unk2;
	private int dataSize;
	private byte[] data;

	public CM_GROUP_DATA_EXCHANGE(int opcode, AionConnection.State state, AionConnection.State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		action = readC();

		switch (action) {
			case 1:
				dataSize = readD();
				break;
			default:
				groupType = readC();
				unk2 = readC();
				dataSize = readD();
				break;
		}
		if (dataSize > 0 && dataSize <= 5086) {
			data = readB(dataSize);
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null || data == null) {
			return;
		}

		if (action == 1) {
			PacketSendUtility.sendPacket(player, new SM_GROUP_DATA_EXCHANGE(data));
			return;
		}

		Collection<Player> players = null;
		switch (groupType) {
			case 0:
				if (player.isInGroup2()) {
					players = player.getPlayerGroup2().getOnlineMembers();
				}
				break;
			case 1:
				if (player.isInAlliance2()) {
					players = player.getPlayerAllianceGroup2().getOnlineMembers();
				}
				break;
			case 2:
				if (player.isInLeague()) {
					players = player.getPlayerAllianceGroup2().getOnlineMembers();
				}
				break;
		}

		if (players != null) {
			for (Player member : players) {
				if (!member.equals(player)) {
					PacketSendUtility.sendPacket(member, new SM_GROUP_DATA_EXCHANGE(data, action, unk2));
				}
			}
		}
	}
}
