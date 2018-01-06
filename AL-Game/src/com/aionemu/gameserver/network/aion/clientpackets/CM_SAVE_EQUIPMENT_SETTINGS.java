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

/**
 * @author FrozenKiller
 */

public class CM_SAVE_EQUIPMENT_SETTINGS extends AionClientPacket {

	private int slotId;
	private int show;
	private int weaponRight;
	private int weaponLeft;
	private int head;
	private int oberteil;
	private int hand;
	private int schuhe;
	private int ohrringL;
	private int ohrringR;
	private int ringL;
	private int ringR;
	private int kette;
	private int schulter;
	private int hose;
	private int machtscherbeR;
	private int machtscherbeL;
	private int fluegel;
	private int guertel;
	private int feder;
	private int armband;
	private int unk1;
	private int unk2;
	private int unk3;

	public CM_SAVE_EQUIPMENT_SETTINGS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		slotId = readD();
		show = readD();
		weaponRight = readD();
		weaponLeft = readD();

		head = readD();
		oberteil = readD(); // Oberteil
		hand = readD(); // Hand
		schuhe = readD(); // Schuhe

		ohrringL = readD(); // Ohrring Links
		ohrringR = readD(); // Ohrring Rechts
		ringL = readD(); // Ring Links
		ringR = readD(); // Ring Rechts

		kette = readD(); // Kette
		schulter = readD(); // Schulter
		hose = readD(); // Hose
		machtscherbeR = readD(); // Machtscherbe Rechts

		machtscherbeL = readD(); // Machtscherbe Links
		fluegel = readD(); // Fluegel
		guertel = readD(); // Guertel
		unk1 = readD();

		unk2 = readD();
		feder = readD(); // Feder
		unk3 = readD();
		armband = readD(); // Armband
	}

	@Override
	protected void runImpl() {
		System.out.println(slotId + " | " + show + " | " + weaponRight + " | " + weaponLeft + " | " + head + " | " + oberteil + " | " + hand + " | " + schuhe + " | " + ohrringL + " | " + ohrringR + " | " + ringL + " | " + ringR + " | " + kette + " | " + schulter + " | " + hose + " | " + machtscherbeR + " | " + machtscherbeL + " | " + fluegel + " | " + guertel + " | " + unk1 + " | " + unk2 + " | " + feder + " | " + unk3 + " | " + armband);
	}
}
