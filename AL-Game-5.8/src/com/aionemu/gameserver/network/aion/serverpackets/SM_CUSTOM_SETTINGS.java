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
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr
 */
public class SM_CUSTOM_SETTINGS extends AionServerPacket {

	private Integer obj;
	private int unk = 0;
	private int display;
	private int deny;

	public SM_CUSTOM_SETTINGS(Player player) {
		this(player.getObjectId(), 1, player.getPlayerSettings().getDisplay(), player.getPlayerSettings().getDeny());
	}

	public SM_CUSTOM_SETTINGS(int objectId, int unk, int display, int deny) {
		obj = objectId;
		this.display = display;
		this.deny = deny;
		this.unk = unk;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(obj);
		writeC(unk); // unk
		writeH(display);
		writeH(deny);
	}
}
