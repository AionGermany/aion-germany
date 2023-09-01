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
public class SM_BATTLEFIELD_UNION extends AionServerPacket {

	int fortressId;
	boolean isAvailable;
	int timer;
	int memberSize;
	int maxSize;

	public SM_BATTLEFIELD_UNION(int fortressId, boolean isAvailable, int memberSize) {
		this.fortressId = fortressId;
		this.isAvailable = isAvailable;
		this.memberSize = memberSize;
	}

	@Override
	protected void writeImpl(final AionConnection aionConnection) {
		writeD(fortressId);
		writeC(isAvailable ? 0 : 1);
		writeD(-2080374784);
		writeD(4161);
		writeD(memberSize);
		writeD(maxSize);
	}
}
