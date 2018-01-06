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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LUCKY_DICE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34
 */
public class CM_LUCKY_DICE extends AionClientPacket {

	private int type;
	@SuppressWarnings("unused")
	private int dices;

	public CM_LUCKY_DICE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		type = readC();
		switch (type) { // Reset
			case 0: {
				break;
			}
			case 1: { // Play
				dices = readC(); // ?
				break;
			}
		}
	}

	@Override
	protected void runImpl() {
		if (type == 1) {
			PacketSendUtility.sendPacket(getConnection().getActivePlayer(), new SM_LUCKY_DICE(0));
		}
		else {
			// TODO BONUS RESET ?
			return;
		}
	}
}
