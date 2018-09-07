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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.FastTrackService;

/**
 * @author Eloann - Enomine, Alcapwnd
 */
public class CM_FAST_TRACK_MOVE extends AionClientPacket {

	private int action;

	public CM_FAST_TRACK_MOVE(int opcode, State state, State... states) {
		super(opcode, state, states);
	}

	@Override
	protected void readImpl() {
		action = readH();
		readH();// unk
		readD();// unk
		readD();// unk
		readD();// unk
	}

	@Override
	protected void runImpl() {
		Player requested = getConnection().getActivePlayer();
		if (requested == null)
			return;
		switch (action) {
			case 1:
				FastTrackService.getInstance().handleMoveThere(requested);
				break;
			case 2:
				FastTrackService.getInstance().handleMoveBack(requested);
				break;
		}
	}
}
