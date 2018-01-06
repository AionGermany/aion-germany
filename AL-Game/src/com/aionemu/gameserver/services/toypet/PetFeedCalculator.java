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
package com.aionemu.gameserver.services.toypet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import org.apache.commons.lang.ArrayUtils;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.pet.PetFeedResult;
import com.aionemu.gameserver.model.templates.pet.PetFlavour;
import com.aionemu.gameserver.model.templates.pet.PetRewards;

/**
 * @author Rolandas
 */

/**
 * <b>Current pre-calculated values multiplied by 4; in packet 14 bits. Max value: 17600 / 4 is 13 bits; feed points as in retail packets.</b><br>
 * static final byte[][] pointValues = new byte[][] {<br>
 * // 10 25 40 50 100 200 -- feed max count<br>
 * { 0, 0, 0, 0, 0, 0 }, // level 1~5 items (feed points 0)<br>
 * { 80, 200, 320, 400, 800, 1600 }, // level 6~10 items (feed points 8)<br>
 * { 160, 400, 640, 800, 1600, 3200 }, // level 11~15 items (feed points 16)<br>
 * { 240, 600, 960, 1200, 2400, 4800 }, // level 16~20 items (feed points 24)<br>
 * { 320, 800, 1280, 1600, 3200, 6400 }, // level 21~25 items (feed points 32)<br>
 * { 400, 1000, 1600, 2000, 4000, 8000 }, // level 26~30 items (feed points 40)<br>
 * { 480, 1200, 1920, 2400, 4800, 9600 }, // level 31~35 items (feed points 48)<br>
 * { 560, 1400, 2240, 2800, 5600, 11200 }, // level 36~40 items (feed points 56)<br>
 * { 640, 1600, 2560, 3200, 6400, 12800 }, // level 41~45 items (feed points 64)<br>
 * { 720, 1800, 2880, 3600, 7200, 14400 }, // level 46~50 items (feed points 72)<br>
 * { 800, 2000, 3200, 4000, 8000, 16000 }, // level 51~55 items (feed points 80)<br>
 * { 880, 2200, 3520, 4400, 8800, 17600 } // level 56~60 items (feed points 88)<br>
 * };
 */
public final class PetFeedCalculator {

	static byte ITEM_MAX_LEVEL = 60;
	static final short[] fullCounts;
	static final byte[] itemLevels;
	static final int[][] pointValues;

	static {
		TreeSet<Short> counts = new TreeSet<Short>();
		for (PetFlavour flavour : DataManager.PET_FEED_DATA.getPetFlavours()) {
			if (flavour.getFullCount() > 0) {
				counts.add((short) (flavour.getFullCount() & 0xFFFF));
			}
		}

		fullCounts = new short[counts.size()];
		int i = 0;
		Iterator<Short> countIter = counts.iterator();
		while (countIter.hasNext()) {
			fullCounts[i++] = countIter.next();
		}

		itemLevels = new byte[ITEM_MAX_LEVEL / 5];
		itemLevels[0] = 5;
		for (int j = 1; j < itemLevels.length; j++) {
			itemLevels[j] = (byte) (itemLevels[j - 1] + 5);
		}

		pointValues = new int[itemLevels.length][fullCounts.length];
		calculate();
	}

	/**
	 * Calculate point values for each item levels and each max feed count
	 */
	static void calculate() {
		for (byte levelByte : itemLevels) {
			short level = (short) (levelByte & 0xFF);
			if (level < 10) {
				continue;
			}
			int countIndex = 0;
			for (short countByte : fullCounts) {
				short count = (short) (countByte & 0xFF);
				int finalLevel = level;
				if (finalLevel % 5 == 0) {
					finalLevel--;
				}
				int pointLevel = itemLevels[(finalLevel / 5)];
				int feedPoints = Math.max(0, pointLevel - 5) / 5 * 8;
				// System.out.println("ITEM LEVEL: " + level + ", COUNT: " + count + ", STEP: " + feedPoints);
				pointValues[finalLevel / 5][countIndex++] = getPoints(feedPoints, count);
			}
		}
	}

	/**
	 * Formula to calculate pointValues array
	 *
	 * @param feedPoints
	 *            - feed points for item
	 * @param maxFeedCount
	 *            - max feeding count
	 * @return byte increment count after all items are fed
	 */
	static int getPoints(int feedPoints, int maxFeedCount) {
		int points = 0;
		int state = 0;
		int consumed = 0;
		while (consumed < maxFeedCount) {
			boolean needSwitch = false;
			int oldPoints = points;
			if ((state == 0 && consumed > maxFeedCount * 0.5f) || (state == 1 && consumed > maxFeedCount * 0.8f) || (state == 2 && consumed > maxFeedCount * 1.05)) {
				needSwitch = true;
			}
			points += feedPoints;
			if (needSwitch) {
				state++;
				if (state == 1 && consumed <= 0.487f * maxFeedCount || state == 2 && consumed <= 0.78f * maxFeedCount) {
					state--;
					points = oldPoints;
				}
			}
			consumed++;
		}
		return points;
	}

	public static void updatePetFeedProgress(@Nonnull PetFeedProgress progress, int itemLevel, int maxFeedCount) {
		PetHungryLevel currHungryLevel = progress.getHungryLevel();
		if (progress.isLovedFeeded()) { // loved food
			if (progress.getLovedFoodRemaining() == 0) {
				return;
			}
			progress.setHungryLevel(PetHungryLevel.FULL);
			progress.incrementCount(true);
			return;
		}

		int oldPoints = progress.getTotalPoints();
		boolean needSwitch = false;

		if ((currHungryLevel == PetHungryLevel.HUNGRY && progress.getRegularCount() > maxFeedCount * 0.5f) || (currHungryLevel == PetHungryLevel.CONTENT && progress.getRegularCount() > maxFeedCount * 0.8f) || (currHungryLevel == PetHungryLevel.SEMIFULL && progress.getRegularCount() > maxFeedCount * 1.05)) {
			// forcefully switch level
			needSwitch = true;
		}
		else {
			int finalLevel = itemLevel;
			if (finalLevel % 5 == 0) {
				finalLevel--;
			}

			byte pointLevel = itemLevels[(finalLevel / 5)];
			byte pointsEarned = (byte) (Math.max(0, pointLevel - 5) / 5 * 8);
			int feedProgress = progress.getTotalPoints() + pointsEarned;
			progress.setTotalPoints(feedProgress);
		}

		if (needSwitch) {
			// just a prevention to not switch level
			PetHungryLevel nextLevel = progress.getHungryLevel().getNextValue();
			if (nextLevel == PetHungryLevel.CONTENT && progress.getRegularCount() <= 0.487f * maxFeedCount || nextLevel == PetHungryLevel.SEMIFULL && progress.getRegularCount() <= 0.78f * maxFeedCount) {
				progress.setTotalPoints(oldPoints);
			}
			else {
				progress.setHungryLevel(nextLevel);
			}
		}
		progress.incrementCount(false);
	}

	public static PetFeedResult getReward(int fullCount, PetRewards rewardGroup, PetFeedProgress progress, int playerLevel) {
		if (progress.getHungryLevel() != PetHungryLevel.FULL || rewardGroup.getResults().size() == 0) {
			return null;
		}

		int pointsIndex = ArrayUtils.indexOf(fullCounts, (short) fullCount);
		if (pointsIndex == ArrayUtils.INDEX_NOT_FOUND) {
			return null;
		}

		if (progress.isLovedFeeded()) { // for cash feed
			if (rewardGroup.getResults().size() == 1) {
				return rewardGroup.getResults().get(0);
			}
			List<PetFeedResult> validRewards = new ArrayList<PetFeedResult>();
			int maxLevel = 0;
			for (PetFeedResult result : rewardGroup.getResults()) {
				int resultLevel = DataManager.ITEM_DATA.getItemTemplate(result.getItem()).getLevel();
				if (resultLevel > playerLevel) {
					continue;
				}
				if (resultLevel > maxLevel) {
					maxLevel = resultLevel;
					validRewards.clear();
				}
				validRewards.add(result);
			}
			if (validRewards.size() == 0) {
				return null;
			}
			if (validRewards.size() == 1) {
				return validRewards.get(0);
			}
			return validRewards.get(Rnd.get(validRewards.size()));
		}

		int rewardIndex = 0;
		int totalRewards = rewardGroup.getResults().size();
		for (int row = 1; row < pointValues.length; row++) {
			int[] points = pointValues[row];
			if (points[pointsIndex] <= progress.getTotalPoints()) {
				rewardIndex = Math.round((float) totalRewards / (pointValues.length - 1) * row) - 1;
			}
		}

		// Fix rounding discrepancy
		if (rewardIndex < 0) {
			rewardIndex = 0;
		}
		else if (rewardIndex > rewardGroup.getResults().size() - 1) {
			rewardIndex = rewardGroup.getResults().size() - 1;
		}

		return rewardGroup.getResults().get(rewardIndex);
	}
}
