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

import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 */
public class SM_SUMMON_UPDATE extends AionServerPacket {

	private Summon summon;

	public SM_SUMMON_UPDATE(Summon summon) {
		this.summon = summon;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(summon.getLevel());
		writeH(summon.getMode().getId());
		writeD(0);// unk
		writeD(0);// unk
		writeD(summon.getLifeStats().getCurrentHp());

		Stat2 maxHp = summon.getGameStats().getMaxHp();
		writeD(maxHp.getCurrent());

		Stat2 mainHandPAttack = summon.getGameStats().getMainHandPAttack();
		writeD(mainHandPAttack.getCurrent());

		Stat2 pDef = summon.getGameStats().getPDef();
		writeD(pDef.getCurrent());

		Stat2 mResist = summon.getGameStats().getMResist();
		writeH(mResist.getCurrent());

		Stat2 mDef = summon.getGameStats().getMDef();
		writeD(mDef.getCurrent());

		Stat2 accuracy = summon.getGameStats().getMainHandPAccuracy();
		writeH(accuracy.getCurrent());

		Stat2 mainHandPCritical = summon.getGameStats().getMainHandPCritical();
		writeH(mainHandPCritical.getCurrent());

		Stat2 mBoost = summon.getGameStats().getMBoost();
		writeH(mBoost.getCurrent());

		Stat2 suppression = summon.getGameStats().getMBResist();
		writeH(suppression.getCurrent());

		Stat2 mAccuracy = summon.getGameStats().getMAccuracy();
		writeH(mAccuracy.getCurrent());

		Stat2 mCritical = summon.getGameStats().getMCritical();
		writeH(mCritical.getCurrent());

		Stat2 parry = summon.getGameStats().getParry();
		writeH(parry.getCurrent());

		Stat2 evasion = summon.getGameStats().getEvasion();
		writeH(evasion.getCurrent());

		writeD(maxHp.getBase());
		writeD(mainHandPAttack.getBase());
		writeD(pDef.getBase());
		writeH(mResist.getBase());
		writeD(mDef.getBase());
		writeH(accuracy.getBase());
		writeH(mainHandPCritical.getBase());
		writeH(mBoost.getBase());
		writeH(suppression.getBase());
		writeH(mAccuracy.getBase());
		writeH(mCritical.getBase());
		writeH(parry.getBase());
		writeH(evasion.getBase());
	}
}
