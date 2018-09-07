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
 * This packet is used to update current exp / recoverable exp / max exp values.
 *
 * @author Luno
 * @updated by alexa026
 */
public class SM_STATUPDATE_EXP extends AionServerPacket {

	private long currentExp;
	private long recoverableExp;
	private long maxExp;
	private long curBoostExp = 0;
	private long maxBoostExp = 0;
	private long goldenStarEnergy = 0;
	private long growthEnergy = 0;

	/**
	 * @param currentExp
	 * @param recoverableExp
	 * @param maxExp
	 * @param curBoostExp
	 * @param maxBoostExp
	 * @param goldenStarEnergy
	 * @param growthEnergy
	 */
	public SM_STATUPDATE_EXP(long currentExp, long recoverableExp, long maxExp, long curBoostExp, long maxBoostExp, long goldenStarEnergy, long growthEnergy) {
		this.currentExp = currentExp; // checked
		this.recoverableExp = recoverableExp; // checked
		this.maxExp = maxExp; // checked
		this.curBoostExp = curBoostExp; // checked
		this.maxBoostExp = maxBoostExp; // checked
		this.goldenStarEnergy = goldenStarEnergy; // checked = Golden Star Energy
		this.growthEnergy = growthEnergy; // checked = Energy of Growth
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeQ(currentExp);
		writeQ(recoverableExp);
		writeQ(maxExp);
		writeQ(curBoostExp);
		writeQ(maxBoostExp);
		writeQ(goldenStarEnergy);
		writeQ(growthEnergy);
	}
}
