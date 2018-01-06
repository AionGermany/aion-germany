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
package com.aionemu.gameserver.utils.stats;

import java.util.NoSuchElementException;

public enum DropRewardEnum {

	MINUS_20(-20, 0),
	MINUS_19(-19, 39),
	MINUS_18(-18, 79),
	MINUS_17(-17, 100);

	private int dropRewardPercent;
	private int levelDifference;

	private DropRewardEnum(int levelDifference, int dropRewardPercent) {
		this.levelDifference = levelDifference;
		this.dropRewardPercent = dropRewardPercent;
	}

	public int rewardPercent() {
		return dropRewardPercent;
	}

	/**
	 * @param levelDifference
	 *            between two objects
	 * @return Drop reward percentage
	 */
	public static int dropRewardFrom(int levelDifference) {
		if (levelDifference < MINUS_20.levelDifference) {
			return MINUS_20.dropRewardPercent;
		}
		if (levelDifference > MINUS_17.levelDifference) {
			return MINUS_17.dropRewardPercent;
		}

		for (DropRewardEnum dropReward : values()) {
			if (dropReward.levelDifference == levelDifference) {
				return dropReward.dropRewardPercent;
			}
		}

		throw new NoSuchElementException("Drop reward for such level difference was not found");
	}
}
