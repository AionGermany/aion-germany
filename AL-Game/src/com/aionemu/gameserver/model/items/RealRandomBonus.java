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
package com.aionemu.gameserver.model.items;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;

public class RealRandomBonus {

	private int itemObjectId;
	private List<RealRandomBonusStat> stats;
	private List<StatFunction> functions;
	private List<StatFunction> fusionFunctions;

	public RealRandomBonus(int itemObjectId, List<RealRandomBonusStat> stats) {
		functions = new ArrayList<StatFunction>();
		fusionFunctions = new ArrayList<StatFunction>();
		this.itemObjectId = itemObjectId;
		this.stats = stats;
		for (RealRandomBonusStat stat : getStats()) {
			switch (stat.getStat()) {
				case ATTACK_SPEED:
				case SPEED: {
					int value = (stat.getStat() == StatEnum.ATTACK_SPEED) ? (-stat.getValue()) : stat.getValue();
					if (stat.isFusion()) {
						fusionFunctions.add(new StatRateFunction(stat.getStat(), value, true));
						continue;
					}
					functions.add(new StatRateFunction(stat.getStat(), value, true));
					continue;
				}
				default: {
					if (stat.isFusion()) {
						fusionFunctions.add(new StatAddFunction(stat.getStat(), stat.getValue(), true));
						continue;
					}
					functions.add(new StatAddFunction(stat.getStat(), stat.getValue(), true));
					continue;
				}
			}
		}
	}

	public void recalcStats() {
		functions.clear();
		for (RealRandomBonusStat stat : getStats()) {
			switch (stat.getStat()) {
				case ATTACK_SPEED:
				case SPEED: {
					int value = (stat.getStat() == StatEnum.ATTACK_SPEED) ? (-stat.getValue()) : stat.getValue();
					functions.add(new StatRateFunction(stat.getStat(), value, true));
					continue;
				}
				default: {
					functions.add(new StatAddFunction(stat.getStat(), stat.getValue(), true));
					continue;
				}
			}
		}
	}

	public List<RealRandomBonusStat> getStats() {
		return stats;
	}

	public void setStats(final List<RealRandomBonusStat> stats) {
		this.stats = stats;
	}

	public int getItemObjectId() {
		return itemObjectId;
	}

	public void setItemObjectId(int itemObjectId) {
		this.itemObjectId = itemObjectId;
	}

	public List<StatFunction> getFunctions() {
		return functions;
	}

	public List<StatFunction> getFusionFunctions() {
		return fusionFunctions;
	}
}
