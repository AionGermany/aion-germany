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

import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 */
public class SM_ATREIAN_PASSPORT extends AionServerPacket {

	private int passportId;
	private int countCollected;
	private int lastStampRecived;
	private boolean hasCollected;

	public SM_ATREIAN_PASSPORT(int passportId, int countCollected, int lastStampRecived, boolean hasCollected) {
		this.passportId = passportId;
		this.countCollected = countCollected;
		this.lastStampRecived = lastStampRecived;
		this.hasCollected = hasCollected;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(2011); // Year?
		writeH(4); // Month?
		writeH(14); // Day?
		writeH(EventsConfig.ENABLE_ATREIAN_PASSPORT); // 1=on, 0=off
		writeD(passportId); // Id
		writeD(lastStampRecived);
		writeD(countCollected);
		writeC(hasCollected ? 0 : 1);
	}
}
