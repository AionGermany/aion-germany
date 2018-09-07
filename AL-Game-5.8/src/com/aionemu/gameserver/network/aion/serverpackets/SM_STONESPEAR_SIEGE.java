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

import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author CoolyT / Falke34
 */
public class SM_STONESPEAR_SIEGE extends AionServerPacket {

	Legion legion;
	int type = 0;

	public SM_STONESPEAR_SIEGE(Legion legion, int type) {
		this.legion = legion;
		this.type = type;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(legion.getTerritory().getId()); // TerritoryId
		writeC(type); // type
		writeH(0); // unk
	}
}
