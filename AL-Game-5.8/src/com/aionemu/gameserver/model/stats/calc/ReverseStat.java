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
package com.aionemu.gameserver.model.stats.calc;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.stats.container.StatEnum;

/**
 * @author ATracer
 */
public class ReverseStat extends Stat2 {

	public ReverseStat(StatEnum stat, int base, Creature owner) {
		super(stat, base, owner);
	}

	public ReverseStat(StatEnum stat, int base, Creature owner, float bonusRate) {
		super(stat, base, owner, bonusRate);
	}

	@Override
	public void addToBase(int base) {
		this.base -= base;
		if (this.base < 0) {
			this.base = 0;
		}
	}

	@Override
	public void addToBonus(int bonus) {
		this.bonus -= bonusRate * bonus;
	}

	@Override
	public float calculatePercent(int delta) {
		float percent = (100 - delta) / 100f;
		// TODO need double check here for negatives
		return percent < 0 ? 0 : percent;
	}
}
