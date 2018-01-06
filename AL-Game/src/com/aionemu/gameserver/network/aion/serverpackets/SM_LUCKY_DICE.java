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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34 & FrozenKiller
 */
public class SM_LUCKY_DICE extends AionServerPacket {

	private int login;
	@SuppressWarnings("unused")
	private int trys;
	private int roll = Rnd.get(1, 6);
	@SuppressWarnings("unused")
	private int test;
	@SuppressWarnings("unused")
	private Player player;

	public SM_LUCKY_DICE(int login) {
		this.login = login;
	}

	public SM_LUCKY_DICE(int login, int trys) {
		this.login = login;
		this.trys = trys;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		switch (login) {
			case 0: {
				writeD(2); // unk
				writeD(0 + roll); // only for test
				writeD(15); // Makierung ueber den zuletzt erhaltenen Items ka wie das berechnet wird ^^ (15 = die ersten 3 Items 16 = nur das 4te Item)
				writeD(0);
				writeD(3); // Anzahl an kostenlosen Wuerfeln -> muss evtl in die DB
				writeD(6); // Anzahl an Extra Wuerfel -> muss evtl in die DB
				writeD(1); // Reset Bonus ? (Zuruecksetzen * x)
				writeD(594037); // Zeit bis zum zuruecksetzen
				writeD(0);
				writeD(594037); // Zeit bis zum zuruecksetzen
				writeD(0);
				writeD(roll); // Augen auf dem Wuerfel
			}
			case 1: {
				writeD(2);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(3); // Anzahl an kostenlosen Wuerfeln -> muss evtl in die DB
				writeD(6); // Anzahl an Extra Wuerfel -> muss evtl in die DB
				writeD(1); // Reset Bonus ? (Zuruecksetzen * x)
				writeD(594037); // Zeit bis zum zuruecksetzen
				writeD(0);
				writeD(594037); // Zeit bis zum zuruecksetzen
				writeD(0);
				writeD(0);
			}
		}
	}
}
