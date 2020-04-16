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

import com.aionemu.gameserver.model.templates.lugbug.LugbugEventTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 */
public class SM_LUGBUG_EVENT extends AionServerPacket {

	private HashMap<Integer, LugbugEventTemplate> activeLugbugEventsForPlayer;

	public SM_LUGBUG_EVENT(HashMap<Integer, LugbugEventTemplate> activeLugbugEventsForPlayer) {
		this.activeLugbugEventsForPlayer = activeLugbugEventsForPlayer;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(activeLugbugEventsForPlayer.size());
		System.out.println("Active Player Events: " + activeLugbugEventsForPlayer.size());
		for (LugbugEventTemplate lugbugEventTemplate : activeLugbugEventsForPlayer.values()) {
			writeC(1);
			writeD(42647792);
			writeD(0);
			writeD(lugbugEventTemplate.getId());
			writeD(0);
			writeD(0);
			writeC(5);
			writeC(4);
			writeC(1); // Active
			writeH(0); // count / killcount
			writeH(0);
			writeC(4);
			writeB(new byte[20]);
			writeD((int) (lugbugEventTemplate.getStartday().getMillis() / 1000));
			writeD(0);
			writeD((int) (lugbugEventTemplate.getEndday().getMillis() / 1000));
			writeD(0);
		}
	}
}