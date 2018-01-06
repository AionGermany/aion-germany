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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Alcapwnd
 */
public class SM_HOTSPOT_TELEPORT extends AionServerPacket {

	int playerObjId;
	int action;
	int teleportId;
	int cooldown;

	public SM_HOTSPOT_TELEPORT(int action, int playerObjId) {
		this.action = action;
		this.playerObjId = playerObjId;
	}

	public SM_HOTSPOT_TELEPORT(int action, int playerObjId, int teleportId) {
		this.action = action;
		this.playerObjId = playerObjId;
		this.teleportId = teleportId;
	}

	public SM_HOTSPOT_TELEPORT(Player player, int action, int teleportId, int cooldown) {
		this.playerObjId = player.getObjectId();
		this.teleportId = teleportId;
		this.action = action;
		this.cooldown = cooldown;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(action);
		switch (action) {
			case 0:
				writeD(playerObjId);
				break;
			case 1:
				writeD(playerObjId);
				writeD(teleportId);
				break;
			case 2:
				writeD(playerObjId);
				break;
			case 3:
				writeD(playerObjId);
				writeD(teleportId);
				writeD(cooldown);
				break;
		}
	}
}
