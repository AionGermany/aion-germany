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
 * @author ATracer
 */
public class SM_SUMMON_USESKILL extends AionServerPacket {

	private int summonId;
	private int skillId;
	private int skillLvl;
	private int targetId;

	/**
	 * @param summonId
	 * @param skillId
	 * @param skillLvl
	 * @param targetId
	 */
	public SM_SUMMON_USESKILL(int summonId, int skillId, int skillLvl, int targetId) {
		this.summonId = summonId;
		this.skillId = skillId;
		this.skillLvl = skillLvl;
		this.targetId = targetId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(summonId);
		writeH(skillId);
		writeC(skillLvl);
		writeD(targetId);
	}
}
