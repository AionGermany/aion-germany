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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Created by Ghostfur
 */
public class SM_SHUGO_SWEEP extends AionServerPacket {

	private int tableId;
	private int currentStep;
	private int diceLeft;
	private int diceGolden;
	private int unkButton;
	private int moveStep;

	@SuppressWarnings("unused")
	private int unk;

	// sweep player infos
	public SM_SHUGO_SWEEP(int tableId, int currentStep, int diceLeft, int diceGolden, int unkButton, int moveStep) {
		this.currentStep = currentStep;
		this.diceLeft = diceLeft;
		this.diceGolden = diceGolden;
		this.unkButton = unkButton;
		this.moveStep = moveStep;
		this.tableId = tableId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(tableId); // table id
		writeD(currentStep); // current step
		writeH(0); // reward ??
		writeH(0); // reward ??
		writeD(0); 
		writeD(diceLeft); // dice left
		writeD(diceGolden); // dice golden
		writeD(unkButton); // button near dice left
		writeD(432000);
		writeD(0);
		writeD(432000);
		writeD(0);
		writeD(moveStep); // move step
	}
}
