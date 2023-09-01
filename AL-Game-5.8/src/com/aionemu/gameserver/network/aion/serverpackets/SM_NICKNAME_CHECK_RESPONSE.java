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
 * This packet is response for CM_CHECK_NICKNAME.<br>
 * It sends client information if name can be used or not
 *
 * @author -Nemesiss-
 */
public class SM_NICKNAME_CHECK_RESPONSE extends AionServerPacket {

	/**
	 * Value of response object
	 */
	private final int value;

	/**
	 * Constructs new <tt>SM_NICKNAME_CHECK_RESPONSE</tt> packet
	 *
	 * @param value
	 *            Response value
	 */
	public SM_NICKNAME_CHECK_RESPONSE(int value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		/**
		 * Here is some msg: 0x00 = ok 0x0A = not ok and much more
		 */
		writeC(value);
	}
}
