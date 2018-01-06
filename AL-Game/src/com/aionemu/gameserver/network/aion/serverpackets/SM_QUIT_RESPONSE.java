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
 * This packet is response for CM_QUIT
 *
 * @author -Nemesiss-
 */
public class SM_QUIT_RESPONSE extends AionServerPacket {

	private boolean edit_mode = false;

	public SM_QUIT_RESPONSE() {
	}

	public SM_QUIT_RESPONSE(boolean edit_mode) {
		this.edit_mode = edit_mode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(edit_mode ? 2 : 1);// 1 normal, 2 plastic surgery/gender switch
		writeC(0x00);// unk
		writeC(0xFF);// unk 3.0
		writeC(0xFF);// unk 3.0
		writeC(0xFF);// unk 3.0
		writeC(0xFF);// unk 3.0
	}
}
