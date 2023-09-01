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
 */
public class SM_ATREIAN_PASSPORT extends AionServerPacket {

	private int month;
	private int year;
	private int passportId;
	private int countCollected;
	private int lastStampRecived;
	private boolean hasCollected;

	public SM_ATREIAN_PASSPORT(int passportId, int countCollected, int lastStampRecived, boolean hasCollected, int month, int year) {
		this.month = month;
		this.year = year;
		this.passportId = passportId;
		this.countCollected = countCollected;
		this.lastStampRecived = lastStampRecived;
		this.hasCollected = hasCollected;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(year);
		writeH(month);
		writeH(8);// can be variable
		//TODO
		writeH(2); // TODO PassportCount
		//TODO
		writeD(passportId);
		writeD(lastStampRecived);
		writeD(countCollected);
		writeC(hasCollected ? 0 : 1);
		//TODO Aniversity (9 0 0 1 = Get Aniversity)
		writeD(9);
		writeD(0);
		writeD(0);
		writeC(0);
	}
}
