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

import java.util.HashMap;

import com.aionemu.gameserver.model.templates.event.BoostEvents;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34, FrozenKiller
 */
public class SM_BOOST_EVENTS extends AionServerPacket {

	private HashMap<Integer, BoostEvents> activeEvents;
	private int count = 0;

	public SM_BOOST_EVENTS(HashMap<Integer, BoostEvents> activeEvents) {
		 this.activeEvents = activeEvents;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(9);
		for (BoostEvents event : activeEvents.values()) {
			while (count != event.getBuffId()) {
				writeC(0);
				writeC(0);
				count++;
				continue;
			} 
			writeC(event.getBuffId());
			writeC(1);
			writeD((int) (event.getStartDate().getMillis() / 1000));
			writeD(0); // unk
			writeD((int) event.getEndDate().getMillis() / 1000);
			writeD(0); // unk
			writeD(event.getBuffValue());
			writeQ(-1);
			writeD(0); // unk
			writeD(0); // Map ID?
			count++;
		}
	}
}