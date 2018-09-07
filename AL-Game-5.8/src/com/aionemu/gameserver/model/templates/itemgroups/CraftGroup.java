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
package com.aionemu.gameserver.model.templates.itemgroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.math.IntRange;

import com.aionemu.gameserver.model.templates.rewards.CraftReward;

import javolution.util.FastMap;

/**
 * @author Rolandas
 */
public abstract class CraftGroup extends BonusItemGroup {

	private FastMap<Integer, FastMap<IntRange, List<CraftReward>>> dataHolder;

	public ItemRaceEntry[] getRewards(Integer skillId) {
		if (!dataHolder.containsKey(skillId)) {
			return new ItemRaceEntry[0];
		}
		List<ItemRaceEntry> result = new ArrayList<ItemRaceEntry>();
		for (List<CraftReward> items : dataHolder.get(skillId).values()) {
			result.addAll(items);
		}
		return result.toArray(new ItemRaceEntry[0]);
	}

	public ItemRaceEntry[] getRewards(Integer skillId, Integer skillPoints) {
		if (!dataHolder.containsKey(skillId)) {
			return new ItemRaceEntry[0];
		}
		List<ItemRaceEntry> result = new ArrayList<ItemRaceEntry>();
		for (Entry<IntRange, List<CraftReward>> entry : dataHolder.get(skillId).entrySet()) {
			if (!entry.getKey().containsInteger(skillPoints)) {
				continue;
			}
			result.addAll(entry.getValue());
		}
		return result.toArray(new ItemRaceEntry[0]);
	}

	/**
	 * @return the dataHolder
	 */
	public FastMap<Integer, FastMap<IntRange, List<CraftReward>>> getDataHolder() {
		return dataHolder;
	}

	/**
	 * @param dataHolder
	 *            the dataHolder to set
	 */
	public void setDataHolder(FastMap<Integer, FastMap<IntRange, List<CraftReward>>> dataHolder) {
		this.dataHolder = dataHolder;
	}
}
