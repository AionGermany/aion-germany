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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author Falke_34
 */
public class CM_NEVIWIND_CANYON extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_NEVIWIND_CANYON.class);

	private int action;

	public CM_NEVIWIND_CANYON(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		action = readH();

		switch (action) {
			case 0:
                readH();
                readD();
                readD();
                readD();
                readD();
                readH();
				break;
			case 3:
                readH();
                readD();
                readD();
                readD();
                readD();
                readH();
				break;
			case 4: // send Offers
                readH();
                readD();
                readD();
                readD();
                readD(); // auto accept on/off
                readS(); // message
				break;
			case 5: // apply Group
                readH();
                readD();
                readD();
                readD();
                readD();
                readH();
				break;
			case 6: // accept Group Entry
                readH();
                readD();
                readD();
                readD();
                readD();
                readH();
				break;
			case 8: // cancel
                readH();
                readD();
                readD();
                readD();
                readD();
                readH();
				break;
			case 9:
                readH();
                readD();
                readD();
                readD();
                readD();
                readH();
				break;
			case 10:
                readH();
                readD();
                readD();
                readD();
                readD();
                readH();
				break;
			default:
				log.error("Unknown find neviwind packet? 0x" + Integer.toHexString(action).toUpperCase());
				break;
		}
	}

	@Override
	protected void runImpl() {
		final Player player = this.getConnection().getActivePlayer();
		switch (action) {
			case 0:
		}
	}
}
