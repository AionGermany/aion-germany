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
public class SM_HOUSE_EDIT extends AionServerPacket {

	private int action;
	private int storeId;
	private int itemObjectId;
	private float x, y, z;
	private int rotation;

	public SM_HOUSE_EDIT(int action) {
		this.action = action;
	}

	public SM_HOUSE_EDIT(int action, int storeId, int itemObjectId) {
		this(action);
		this.itemObjectId = itemObjectId;
		this.storeId = storeId;
	}

	public SM_HOUSE_EDIT(int action, int itemObjectId, float x, float y, float z, int rotation) {
		this.action = action;
		this.itemObjectId = itemObjectId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		if (player == null || player.getHouseRegistry() == null) {
			return;
		}

		HouseObject<?> obj = player.getHouseRegistry().getObjectByObjId(itemObjectId);

		if (action == 3) { // Add item
			int templateId = 0;
			int typeId = 0;
			if (obj == null) {
				HouseDecoration deco = player.getHouseRegistry().getCustomPartByObjId(itemObjectId);
				templateId = deco.getTemplate().getId();
			}
			else {
				templateId = obj.getObjectTemplate().getTemplateId();
				typeId = obj.getObjectTemplate().getTypeId();
			}
			writeC(action);
			writeC(storeId);
			writeD(itemObjectId);
			writeD(templateId);
			if (obj != null && obj.getUseSecondsLeft() > 0) {
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

			writeC(typeId);
			// Additional info about the usage
			if (obj != null && obj instanceof UseableItemObject) {
				writeD(player.getObjectId());
				((UseableItemObject) obj).writeUsageData(getBuf());
			}
		}
		else if (action == 4) { // Remove from inventory
			writeC(action);
			writeC(storeId);
			writeD(itemObjectId);
		}
		else if (action == 5) { // Spawn or move object
			writeC(action);
			writeD(player.getHouseOwnerId()); // if painted 0 ?
			writeD(player.getCommonData().getPlayerObjId());
			writeD(itemObjectId);
			writeD(obj.getObjectTemplate().getTemplateId());
			writeF(x);
			writeF(y);
			writeF(z);
			writeH(rotation);
			writeD(player.getHouseObjectCooldownList().getReuseDelay(itemObjectId));
			if (obj.getUseSecondsLeft() > 0) {
				writeD(obj.getUseSecondsLeft());
			}
			else {
				writeD(0);
			}

			Integer color = obj.getColor();
			writeC(color == null ? 0 : 1); // Is dyed
			if (color == null) {
				writeC(0);
				writeC(0);
				writeC(0);
			}
			else {
				writeC((color & 0xFF0000) >> 16);
				writeC((color & 0xFF00) >> 8);
				writeC((color & 0xFF));
			}
			writeD(0); // expiration as for armor ?

			writeC(obj.getObjectTemplate().getTypeId());

			if (obj instanceof UseableItemObject) {
				((UseableItemObject) obj).writeUsageData(getBuf());
			}
		}
		else if (action == 7) { // Despawn object
			writeC(action);
			writeD(itemObjectId);
		}
		else {
			writeC(action);
		}
	}
}
