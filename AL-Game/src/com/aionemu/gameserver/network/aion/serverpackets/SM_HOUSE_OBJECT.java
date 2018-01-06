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
import com.aionemu.gameserver.model.gameobjects.NpcObject;
import com.aionemu.gameserver.model.gameobjects.UseableItemObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Rolandas
 */
public class SM_HOUSE_OBJECT extends AionServerPacket {

	HouseObject<?> houseObject;

	public SM_HOUSE_OBJECT(HouseObject<?> owner) {
		this.houseObject = owner;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		if (player == null) {
			return;
		}

		House house = houseObject.getOwnerHouse();
		int templateId = houseObject.getObjectTemplate().getTemplateId();

		writeD(house.getAddress().getId()); // if painted 0 ?
		writeD(house.getOwnerId()); // player which owns house
		writeD(houseObject.getObjectId()); // <outlet[X]> data in house scripts
		writeD(houseObject.getObjectId()); // <outDB[X]> data in house scripts (probably DB id), where [X] is number

		writeD(templateId);
		writeF(houseObject.getX());
		writeF(houseObject.getY());
		writeF(houseObject.getZ());
		writeH(houseObject.getRotation());

		writeD(player.getHouseObjectCooldownList().getReuseDelay(houseObject.getObjectId()));
		if (houseObject.getUseSecondsLeft() > 0) {
			writeD(houseObject.getUseSecondsLeft());
		}
		else {
			writeD(0);
		}

		Integer color = null;
		if (houseObject != null) {
			color = houseObject.getColor();
		}

		if (color != null && color > 0) {
			writeC(1); // Is dyed (True)
			writeC((color & 0xFF0000) >> 16);
			writeC((color & 0xFF00) >> 8);
			writeC((color & 0xFF));
		}
		else {
			writeC(0); // Is dyed (False)
			writeC(0);
			writeC(0);
			writeC(0);
		}
		writeD(0); // expiration as for armor ?

		byte typeId = houseObject.getObjectTemplate().getTypeId();
		writeC(typeId);

		switch (typeId) {
			case 1: // Use item
				((UseableItemObject) houseObject).writeUsageData(getBuf());
				break;
			case 7: // Npc type
				NpcObject npcObj = (NpcObject) houseObject;
				writeD(npcObj.getNpcObjectId());
			default:
				break;
		}
	}
}
