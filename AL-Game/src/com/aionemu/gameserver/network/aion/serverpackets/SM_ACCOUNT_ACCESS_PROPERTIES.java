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
 * @author pixfid
 * @modified Magenik , Kev
 */
public class SM_ACCOUNT_ACCESS_PROPERTIES extends AionServerPacket {

	public SM_ACCOUNT_ACCESS_PROPERTIES() {
	}

	private boolean isGM;
	private int accountType;
	private int purchaseType;
	private int time;
	private boolean active;

	public SM_ACCOUNT_ACCESS_PROPERTIES(boolean isGM) {
		this.isGM = isGM;
	}

	public SM_ACCOUNT_ACCESS_PROPERTIES(boolean isGM, int accountType, int purchaseType, int time, boolean active) {
		this.isGM = isGM;
		this.accountType = accountType;
		this.purchaseType = purchaseType;
		this.time = time;
		this.active = active;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(this.isGM ? 3 : 0); // 3 = GM-Panel(Shift+F1)
		writeH(0);
		writeD(0);
		writeD(0);
		writeD(this.isGM ? 32768 : 0); // unk
		writeD(0);
		writeC(0);
		writeD(this.active ? 31 : 0); // 31 with Active GoldPaket
		writeD(0); // should be always 0
		writeD(purchaseType); // GoldPaket active 8 else 0
		writeD(accountType); // 2 = Starter 4 = Veteran
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(time); // GoldPaket Time
		writeB(new byte[32]); // unk 4.9
	}
}
