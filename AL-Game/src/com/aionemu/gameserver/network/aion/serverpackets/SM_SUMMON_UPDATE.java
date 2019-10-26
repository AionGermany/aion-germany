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
import com.aionemu.gameserver.model.stats.container.SummonGameStats;
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
		
		SummonGameStats stats = summon.getGameStats();
		
		writeC(summon.getLevel());
		writeH(summon.getMode().getId());
		writeD(0);
		writeD(0);
		//Current
		writeD(summon.getLifeStats().getCurrentHp());
		writeD(stats.getMaxHp().getCurrent());
		writeD(stats.getMainHandPAttack().getCurrent()); // TODO Weapon Attack
		writeD(stats.getPDef().getBonus()); // TODO
		writeD(stats.getMResist().getCurrent());
		writeD(0); // TODO
		writeD(stats.getAccuracy().getCurrent());
		writeH(stats.getMainHandPCritical().getCurrent()); // TODO CritStrike
		writeD(0); // TODO
		writeD(0); // TODO
		writeD(stats.getMAccuracy().getCurrent());
		writeH(stats.getMCritical().getCurrent()); // TODO ? Critspell
		writeD(stats.getParry().getCurrent());
		writeD(stats.getEvasion().getCurrent());
		writeD(stats.getMainHandPAttack().getCurrent());
		writeD(stats.getPDef().getCurrent());
		writeD(stats.getMAttack().getCurrent());
		writeD(stats.getMDef().getCurrent());
		//Base
		writeD(stats.getMaxHp().getBase());
		writeD(stats.getMainHandPAttack().getBase()); // TODO Weapon Attack
		writeD(0); // TODO
		writeD(stats.getMResist().getBase());
		writeD(0); // TODO
		writeD(stats.getAccuracy().getBase());
		writeH(stats.getMainHandPCritical().getBase()); // TODO CritStrike
		writeD(0); // TODO
		writeD(0); // TODO
		writeD(stats.getMAccuracy().getBase());
		writeH(stats.getMCritical().getBase()); // TODO ? Critspell
		writeD(stats.getParry().getBase());
		writeD(stats.getEvasion().getBase());
		writeD(stats.getMainHandPAttack().getBase());
		writeD(stats.getPDef().getBase());
		writeD(stats.getMAttack().getBase());
		writeD(stats.getMDef().getBase());
	}
}
