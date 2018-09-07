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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ACCOUNT_ACCESS_PROPERTIES;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHARACTER_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UNK_14F;

/**
 * In this packets aion client is requesting character list.
 *
 * @author -Nemesiss-
 */
public class CM_CHARACTER_LIST extends AionClientPacket {

	/**
	 * PlayOk2 - we dont care...
	 */
	private int playOk2;

	/**
	 * Constructs new instance of <tt>CM_CHARACTER_LIST </tt> packet.
	 *
	 * @param opcode
	 */
	public CM_CHARACTER_LIST(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		playOk2 = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		// NA Server sends SM_CHARACTER_LIST 2 times first with 0 and then with 2
		boolean isGM = (getConnection()).getAccount().getAccessLevel() >= AdminConfig.GM_PANEL;
		sendPacket(new SM_ACCOUNT_ACCESS_PROPERTIES(isGM));
		sendPacket(new SM_ACCOUNT_ACCESS_PROPERTIES(isGM));
		sendPacket(new SM_ACCOUNT_ACCESS_PROPERTIES(isGM));
		sendPacket(new SM_UNK_14F());
		sendPacket(new SM_CHARACTER_LIST(0, playOk2)); // Clean Character_List (0)
		sendPacket(new SM_CHARACTER_LIST(2, playOk2)); // Send Character_List (2)
	}
}
