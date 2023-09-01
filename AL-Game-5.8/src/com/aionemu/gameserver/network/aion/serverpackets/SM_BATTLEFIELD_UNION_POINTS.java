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
public class SM_BATTLEFIELD_UNION_POINTS extends AionServerPacket {

	int fortressId;
    int elyos_points;
    int asmodians_points;
    int balaur_points;

	public SM_BATTLEFIELD_UNION_POINTS(int fortressId, int elyos_points, int asmodians_points, int balaur_points) {
		this.fortressId = fortressId;
		this.elyos_points = elyos_points;
		this.asmodians_points = asmodians_points;
		this.balaur_points = balaur_points;
	}

	@Override
	protected void writeImpl(final AionConnection aionConnection) {
		writeD(fortressId);
		writeD(elyos_points);
		writeD(asmodians_points);
		writeD(balaur_points);
	}
}
