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
 * @author Alcapwnd this fatigue system isnt implemented on official servers its inside but not enabled
 */
public class SM_FATIGUE extends AionServerPacket {

	private int effectEnabled;
	private int isFull;
	private int fatigueRecover;
	private int iconSet;

	/**
	 * @param fatigueRecover
	 */
	public SM_FATIGUE(int effectEnabled, int isFull, int fatigueRecover, int iconSet) {
		this.effectEnabled = effectEnabled;
		this.isFull = isFull;
		this.fatigueRecover = 0;// fatigueRecover
		this.iconSet = iconSet;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(0);// unk
		writeC(0);// unk
		writeC(effectEnabled);// 1=effect enabled | 0=effect disabled //VERIFIED!
		writeH(iconSet);// icon
		writeC(isFull);// isFull 1=100% | 0=0% //VERIFIED!
		writeC(fatigueRecover);// fatigue recovery //VERIFIED! //seems it isnt implemented
	}
}
