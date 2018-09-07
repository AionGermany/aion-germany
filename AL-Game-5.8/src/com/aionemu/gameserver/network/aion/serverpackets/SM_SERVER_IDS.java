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
import com.aionemu.gameserver.services.transfers.FastTrack;

/**
 * @author Eloann - Enomine
 */
public class SM_SERVER_IDS extends AionServerPacket {

	private FastTrack settings;

	public SM_SERVER_IDS(FastTrack settings) {
		this.settings = settings;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(settings.getServerId());
		writeH(0);// unk
		writeH(settings.getIconSet()); // 257 or 513 the icon available
		writeD(settings.getMaxLevel());
		writeD(settings.getMaxLevel());
		writeD(1);// unk first packet 1, second packet 0
		writeC(0);
	}
}
