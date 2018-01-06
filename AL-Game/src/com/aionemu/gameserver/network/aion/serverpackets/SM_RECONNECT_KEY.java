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
 * Response for CM_RECONNECT_AUTH with key that will be use for authentication at LoginServer.
 *
 * @author -Nemesiss-
 */
public class SM_RECONNECT_KEY extends AionServerPacket {

	/**
	 * key for reconnection - will be used for authentication
	 */
	private final int key;

	/**
	 * Constructs new <tt>SM_RECONNECT_KEY</tt> packet
	 *
	 * @param key
	 *            key for reconnection
	 */
	public SM_RECONNECT_KEY(int key) {
		this.key = key;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeC(0x00);
		writeD(key);
	}
}
