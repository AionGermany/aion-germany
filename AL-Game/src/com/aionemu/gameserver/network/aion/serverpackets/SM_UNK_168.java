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
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.events.EventService;

/**
 * @author FrozenKiller
 */

public class SM_UNK_168 extends AionServerPacket {
	
	@Override
	protected void writeImpl(AionConnection con) { //59
		writeB(new byte[14]);
		writeC(1);
		writeC(94);
		writeC(1);
		writeC(1);
		writeC(5);
		writeC(15);
		writeC(10);
		writeC(1);
		writeC(1);
		writeC(10);
		writeC(1);
		writeC(2);
		writeC(0);
		writeC(GSConfig.CHARACTER_REENTRY_TIME);
		writeC(EventsConfig.ENABLE_DECOR);
		writeC(EventService.getInstance().getEventType().getId()); // 18 Summer Splash V1 / 20 Summer Splash V2
		writeB(new byte[3]);
		writeC(4);
		writeC(1);
		writeB(new byte[5]);
		writeC(1);
		writeC(1);
		writeC(1);
		writeH(0);
		writeC(-128);
		writeC(63);
		writeC(1);
		writeC(19);
		writeB(new byte[3]);
		writeC(1);
		writeC(-86);
		writeC(5);
		writeH(0);
		writeC(1);
		writeC(8);
		writeH(0);
		writeC(-128);
		writeC(63);
		writeB(new byte[10]);
	}
}
