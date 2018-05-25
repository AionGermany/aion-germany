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

import java.util.Calendar;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author FrozenKiller
 */
public class SM_EVENT_WINDOW_ITEMS extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		// Get Second's until Midnight
		long msInDay = 86400000;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		int remainingTime = (int) (msInDay - (System.currentTimeMillis() - c.getTimeInMillis()) + 60000) / 1000; // Second's until Midnight

		writeC(1); // Always 1
		writeC(1); // ShowIcon 0 = False 1 = True
		writeC(0);
		writeD(3565);
		writeD(0);
		writeD(remainingTime); // Remaining Time before MaxCountOfDay Reset (Reset Every Day @ 00:00:00)
		writeD(0);
		writeD(0);
		writeD(1490601600);
		writeC(1);
		writeD(5);
		writeD(1);
		writeD(1401496);
		writeD(90); // Interval ?
		writeD(164002368); // ItemId
		writeQ(1); // ItemCount
		writeD(30); // Level between 45 and 75?
		writeD(1490169600); // Event Start
		writeD(0);
		writeD(1491375599); // Event End
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(1087891456);
		writeD(45); // StartLevel
		writeD(75); // EndLevel
		writeD(-1);
		writeB(new byte[84]);
		writeD(-1);
		writeD(0);
		writeD(0);
		writeD(1);
		writeD(9); // CleanTime
		writeC(2); // MaxCountOfDay
		writeD(0);
		writeD(1);
		writeH(0);
		writeD(-1);
		writeD(0);
	}
}
