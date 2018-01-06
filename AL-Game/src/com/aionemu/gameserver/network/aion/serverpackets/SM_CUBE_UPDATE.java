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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr
 */
public class SM_CUBE_UPDATE extends AionServerPacket {

	private int action;
	/**
	 * for action 0 - its storage type<br>
	 * for action 6 - its advanced stigma count
	 */
	private int actionValue;
	private int itemsCount;
	private int cubeExpands;

	public static SM_CUBE_UPDATE stigmaSlots(int slots) {
		return new SM_CUBE_UPDATE(6, slots);
	}

	public static SM_CUBE_UPDATE cubeSize(StorageType type, Player player) {
		int itemsCount = 0;
		int cubeExpands = 0;
		switch (type) {
			case CUBE:
				itemsCount = player.getInventory().size();
				cubeExpands = player.getCubeExpands();
				break;
			case REGULAR_WAREHOUSE:
				itemsCount = player.getWarehouse().size();
				cubeExpands = player.getWarehouseSize();
				break;
			case LEGION_WAREHOUSE:
				itemsCount = player.getLegion().getLegionWarehouse().size();
				cubeExpands = player.getLegion().getWarehouseLevel();
				break;
			default:
				break;
		}

		return new SM_CUBE_UPDATE(0, type.ordinal(), itemsCount, cubeExpands);
	}

	private SM_CUBE_UPDATE(int action, int actionValue, int itemsCount, int cubeExpands) {
		this(action, actionValue);
		this.itemsCount = itemsCount;
		this.cubeExpands = cubeExpands;
	}

	private SM_CUBE_UPDATE(int action, int actionValue) {
		this.action = action;
		this.actionValue = actionValue;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(action);
		writeC(actionValue);
		switch (action) {
			case 0:
				writeD(itemsCount);
				writeC(cubeExpands); // cube size from npc (so max 5 for now)
				writeC(0);
				writeC(0); // unk - expands from items?
				break;
			case 6:
				break;
			default:
				break;
		}
	}
}
