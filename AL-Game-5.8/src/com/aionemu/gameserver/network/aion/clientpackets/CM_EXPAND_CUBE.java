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
import com.aionemu.gameserver.services.CubeExpandService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Eloann
 */

public class CM_EXPAND_CUBE extends AionClientPacket {

	int action;

	public CM_EXPAND_CUBE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		action = readC();
	}

	@Override
	protected void runImpl() {
		final Player activePlayer = getConnection().getActivePlayer();

		switch (action) {
			case 0: // Kinah
				// starting inventory = 27 slots (3 rows)
				if (activePlayer.getCubeExpands() < 10) { // max 9 rows open for kinah 117 slots
					if (activePlayer.getCubeExpands() == 0) { // 0 rows open / 27 slots to 36
						if (activePlayer.getInventory().tryDecreaseKinah(1000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 1
						}
					}
					else if (activePlayer.getCubeExpands() == 1) { // 1 rows open / 36 slots to 45
						if (activePlayer.getInventory().tryDecreaseKinah(10000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 2
						}
					}
					else if (activePlayer.getCubeExpands() == 2) { // 2 rows open / 45 slots to 54
						if (activePlayer.getInventory().tryDecreaseKinah(50000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 3
						}
					}
					else if (activePlayer.getCubeExpands() == 3) { // 3 rows open / 54 slots to 63
						if (activePlayer.getInventory().tryDecreaseKinah(150000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 4
						}
					}
					else if (activePlayer.getCubeExpands() == 4) { // 4 rows open / 63 slots to 72
						if (activePlayer.getInventory().tryDecreaseKinah(300000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 5
						}
					}
					else if (activePlayer.getCubeExpands() == 5) { // 5 rows open / 72 slots to 81
						if (activePlayer.getInventory().tryDecreaseKinah(3000000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 6
						}
					}
					else if (activePlayer.getCubeExpands() == 6) { // 6 rows open / 81 slots to 90
						if (activePlayer.getInventory().tryDecreaseKinah(6000000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 7
						}
					}
					else if (activePlayer.getCubeExpands() == 7) { // 7 rows open / 90 slots to 99
						if (activePlayer.getInventory().tryDecreaseKinah(12000000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 8
						}
					}
					else if (activePlayer.getCubeExpands() == 8) { // 8 rows open / 99 slots to 108
						if (activePlayer.getInventory().tryDecreaseKinah(24000000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 9
						}
					}
					else if (activePlayer.getCubeExpands() == 9) { // 9 rows open / 108 slots to 117 slots
						if (activePlayer.getInventory().tryDecreaseKinah(48000000)) {
							CubeExpandService.expand(activePlayer, true); // row goes to 10
						}
					}
					else {
						PacketSendUtility.sendMessage(activePlayer, "You need cube expansion coin to expand your cube now");
					}
				}
				break;
			case 1: // Cube Expansion Coin
				if (activePlayer.getCubeExpands() < 11) { // if less than 10 rows open / 117 slots
					if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { // 117 slots to 126 [EU Always 5-Keys Displayed ]
						CubeExpandService.expand(activePlayer, true); // row goes to 11
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
				}
				else if (activePlayer.getCubeExpands() == 11) { // 11 rows open
					if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { // 126 slots to 135
						CubeExpandService.expand(activePlayer, true); // row goes to 12
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
				}
				else if (activePlayer.getCubeExpands() == 12) { // 12 rows open
					if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { // 135 slots to 144
						CubeExpandService.expand(activePlayer, true); // row goes to 13
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
				}
				else if (activePlayer.getCubeExpands() == 13) { // 13 rows open
					if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { // 144 slots to 153
						CubeExpandService.expand(activePlayer, true); // row goes to 14
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
				}
				else if (activePlayer.getCubeExpands() == 14) { // 14 rows open
					if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { // 153 slots to 162
						CubeExpandService.expand(activePlayer, true); // row goes to 15 (max)
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
					else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) {
						CubeExpandService.expand(activePlayer, true);
					}
				}
				else {
					PacketSendUtility.sendMessage(activePlayer, "No more expansion available");
				}
				break;
		}
	}
}
