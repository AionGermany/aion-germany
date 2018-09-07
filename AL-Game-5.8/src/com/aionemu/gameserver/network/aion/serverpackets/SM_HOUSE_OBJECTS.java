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

import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import javolution.util.FastList;

/**
 * @author Rolandas
 */
public class SM_HOUSE_OBJECTS extends AionServerPacket {

	Player player;

	public SM_HOUSE_OBJECTS(Player player) {
		this.player = player;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		FastList<HouseObject<?>> objects = player.getHouseRegistry().getSpawnedObjects();
		writeH(objects.size());
		for (HouseObject<?> obj : objects) {
			writeD(obj.getObjectTemplate().getTemplateId());
			writeF(obj.getX());
			writeF(obj.getY());
			writeF(obj.getZ());
		}
	}
}
