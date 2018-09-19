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
 * @author Falke_34
 */
public class SM_TRANSFORMATION extends AionServerPacket {

	private int actionId;

	public SM_TRANSFORMATION(int actionId) {
		this.actionId = actionId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(actionId);
		switch (actionId) {
		case 0:
			writeC(0);
			writeH(0); // Count
		case 1:
			writeH(0);
			writeD(0); // Transformation ID
		case 2:
			writeH(0);
			writeD(0); // Transformation ID
			writeD(0);
		}
	}
}