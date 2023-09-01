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

import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ever'
 */
public class SM_FAST_TRACK extends AionServerPacket {

	private boolean isFirst = false;
	private int currentServer = 0;
	private int newServerId = 0;

	public SM_FAST_TRACK(int currentServer, int newServerId, boolean first) {
		this.currentServer = currentServer;
		this.newServerId = newServerId;
		this.isFirst = first;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		writeD(newServerId);
		writeD(currentServer);
		writeD(player.getObjectId());
		if (isFirst) {
			writeD(NetworkConfig.GAMESERVER_ID);
		}
		else {
			writeD(newServerId);
		}
		writeD(0); // unk
		writeD(0); // unk
	}
}
