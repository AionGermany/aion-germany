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

import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.UseableItemObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Rolandas
 */
public class SM_HOUSE_REGISTRY extends AionServerPacket {

	int action;

	public SM_HOUSE_REGISTRY(int action) {
		this.action = action;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		if (player == null) {
			return;
		}

		writeC(action);
		if (action == 1) { // Display registered objects
			if (player.getHouseRegistry() == null) {
				writeH(0);
				return;
			}
			writeH(player.getHouseRegistry().getNotSpawnedObjects().size());
			for (HouseObject<?> obj : player.getHouseRegistry().getNotSpawnedObjects()) {
				writeD(obj.getObjectId());
				int templateId = obj.getObjectTemplate().getTemplateId();
				writeD(templateId);
				writeD(player.getHouseObjectCooldownList().getReuseDelay(obj.getObjectId()));
				if (obj.getUseSecondsLeft() > 0) {
					writeD(obj.getUseSecondsLeft());
				}
				else {
					writeD(0);
				}

				Integer color = null;
				if (obj != null) {
					color = obj.getColor();
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

				writeC(obj.getObjectTemplate().getTypeId());
				if (obj instanceof UseableItemObject) {
					((UseableItemObject) obj).writeUsageData(getBuf());
				}
			}
		}
		else if (action == 2) { // Display default and registered decoration items
			writeH(player.getHouseRegistry().getDefaultParts().size() + player.getHouseRegistry().getCustomParts().size());
			for (HouseDecoration deco : player.getHouseRegistry().getDefaultParts()) {
				writeD(0);
				writeD(deco.getTemplate().getId());
			}
			for (HouseDecoration houseDecor : player.getHouseRegistry().getCustomParts()) {
				writeD(houseDecor.getObjectId());
				writeD(houseDecor.getTemplate().getId());
			}
		}
	}
}
