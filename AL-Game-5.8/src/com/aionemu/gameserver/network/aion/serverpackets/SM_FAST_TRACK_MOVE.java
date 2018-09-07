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
 * @author Alcapwnd
 * @see need to implement this packet fully, its only dummy actual
 */
public class SM_FAST_TRACK_MOVE extends AionServerPacket {

	private int currentServerId;
	private int newServerId;
	private int mapId;

	public SM_FAST_TRACK_MOVE(int currentServer, int newServerId, int mapId) {
		this.currentServerId = currentServer;
		this.newServerId = newServerId;
		this.mapId = mapId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(newServerId);
		writeD(currentServerId);
		writeC(0);
		writeD(mapId);
	}
}
