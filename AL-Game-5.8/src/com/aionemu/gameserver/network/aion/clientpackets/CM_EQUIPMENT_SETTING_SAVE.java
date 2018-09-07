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

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author Falke_34
 */
public class CM_EQUIPMENT_SETTING_SAVE extends AionClientPacket {

	private int slot;
	private int displayType;
	private int mHand;
	private int sHand;
	private int helmet;
	private int torso;
	private int glove;
	private int boots;
	private int earringsLeft;
	private int earringsRight;
	private int ringLeft;
	private int ringRight;
	private int necklace;
	private int shoulder;
	private int pants;
	private int powershardLeft;
	private int powershardRight;
	private int wings;
	private int waist;
	private int mOffHand;
	private int sOffHand;
	private int plume;
	private int bracelet;
	private int unk1;
	private int unk2;

	public CM_EQUIPMENT_SETTING_SAVE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	protected void readImpl() {
		slot = readD();
		displayType = readD();
		mHand = readD();
		sHand = readD();
		helmet = readD();
		torso = readD();
		glove = readD();
		boots = readD();
		earringsLeft = readD();
		earringsRight = readD();
		ringLeft = readD();
		ringRight = readD();
		necklace = readD();
		shoulder = readD();
		pants = readD();
		powershardLeft = readD();
		powershardRight = readD();
		wings = readD();
		waist = readD();
		mOffHand = readD();
		sOffHand = readD();
		plume = readD();
		unk1 = readD();
		bracelet = readD();
		unk2 = readD();

		GameServer.log.info("displayType: " + displayType + " MHand: " + mHand + " sHand: " + sHand + " helmet: "
			+ helmet + " torso: " + torso + " glove: " + glove + " boots: " + boots + " earringsLeft: "
			+ earringsLeft + " earringsRight: " + earringsRight + " ringLeft: " + ringLeft + " ringRight: " + ringRight
			+ " necklace: " + necklace + " shoulder: " + shoulder + " pants: " + pants + " powershardLeft: "
			+ powershardLeft + " powershardRight: " + powershardRight + " wings: " + wings + " waist: " + waist
			+ " mOffHand: " + mOffHand + " sOffHand: " + sOffHand + " plume: " + plume + " unk1: " + unk1
			+ " bracelet: " + bracelet + " unk2: " + unk2);
	}

	protected void runImpl() {
        final Player player = getConnection().getActivePlayer();
        if (player == null || !player.isSpawned()) {
            return;
        }
        
        player.getEquipmentSettingList().add(slot, displayType, mHand, sHand, helmet, torso, glove, boots, earringsLeft, earringsRight, ringLeft, ringRight, necklace, shoulder, pants, powershardLeft, powershardRight, wings, waist, mOffHand, sOffHand, plume, bracelet, true);
    }
}
