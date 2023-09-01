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

import javax.xml.bind.annotation.XmlEnum;

import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 * @author Sarynth
 * @author Imaginary
 * @rework Ever' for 4.5
 */
@XmlEnum
public enum AbyssRankEnum {

	// Abyss Points
	GRADE9_SOLDIER(1, 300, 90, 0, 0, 0, 0, 0, 1802431),
	GRADE8_SOLDIER(2, 414, 103, 1200, 0, 0, 0, 0, 1802433),
	GRADE7_SOLDIER(3, 475, 118, 4220, 0, 0, 0, 0, 1802435),
	GRADE6_SOLDIER(4, 546, 136, 10990, 0, 0, 0, 0, 1802437),
	GRADE5_SOLDIER(5, 627, 156, 23500, 0, 0, 0, 0, 1802439),
	GRADE4_SOLDIER(6, 721, 180, 42780, 0, 0, 0, 0, 1802441),
	GRADE3_SOLDIER(7, 865, 216, 69700, 0, 0, 0, 0, 1802443),
	GRADE2_SOLDIER(8, 1038, 259, 105600, 0, 0, 0, 0, 1802445),
	GRADE1_SOLDIER(9, 1245, 311, 150800, 0, 0, 0, 0, 1802447),
	// Glory Points
	STAR1_OFFICER(10, 1868, 467, 0, 1244, 1000, 7, 49, 1802449),
	STAR2_OFFICER(11, 2241, 560, 0, 1368, 700, 14, 98, 1802451),
	STAR3_OFFICER(12, 2577, 644, 0, 1915, 500, 28, 196, 1802453),
	STAR4_OFFICER(13, 2964, 741, 0, 3064, 300, 49, 343, 1802455),
	STAR5_OFFICER(14, 4446, 1511, 0, 5210, 100, 107, 749, 1802457),
	GENERAL(15, 4890, 1662, 0, 8335, 30, 119, 833, 1802459),
	GREAT_GENERAL(16, 5378, 1828, 0, 10002, 10, 122, 854, 1802461),
	COMMANDER(17, 5916, 2011, 0, 11503, 3, 127, 889, 1802463),
	SUPREME_COMMANDER(18, 7099, 2413, 0, 12437, 1, 147, 1029, 1802465);

	private int id;
	private int pointsGained;
	private int pointsLost;
	private int requiredAp;
	private int requiredGp;
	private int quota;
	private int dailyReduceGp;
	private int weeklyReduceGp;
	private int descriptionId;

	/**
	 * @param id
	 * @param pointsGained
	 * @param pointsLost
	 * @param requiredAp
	 * @param quota
	 */
	private AbyssRankEnum(int id, int pointsGained, int pointsLost, int requiredAp, int requiredGp, int quota, int dailyReduceGp, int weeklyReduceGp, int descriptionId) {
		this.id = id;
		this.pointsGained = pointsGained;
		this.pointsLost = pointsLost;
		this.requiredAp = requiredAp * RateConfig.ABYSS_RANK_RATE;
		this.requiredGp = requiredGp * RateConfig.ABYSS_RANK_RATE;
		this.quota = quota;
		this.dailyReduceGp = dailyReduceGp;
		this.weeklyReduceGp = weeklyReduceGp;
		this.descriptionId = descriptionId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the pointsLost
	 */
	public int getPointsLost() {
		return pointsLost;
	}

	/**
	 * @return the pointsGained
	 */
	public int getPointsGained() {
		return pointsGained;
	}

	/**
	 * @return AP required for Rank
	 */
	public int getRequiredAp() {
		return requiredAp == 0 ? 1 : requiredAp;
	}

	/**
	 * @return AP required for Rank
	 */
	public int getRequiredGp() {
		return requiredGp == 0 ? 1 : requiredGp;
	}

	/**
	 * @return The quota is the maximum number of allowed player to have the rank
	 */
	public int getQuota() {
		return quota;
	}

	public int getDailyReduceGp() {
		return dailyReduceGp;
	}

	public int getWeeklyReduceGp() {
		return weeklyReduceGp;
	}

	public int getDescriptionId() {
		return descriptionId;
	}

	public static DescriptionId getRankDescriptionId(Player player) {
		int pRankId = player.getAbyssRank().getRank().getId();
		for (AbyssRankEnum rank : values()) {
			if (rank.getId() == pRankId) {
				int descId = rank.getDescriptionId();
				return (player.getRace() == Race.ELYOS) ? new DescriptionId(descId) : new DescriptionId(descId + 36);
			}
		}
		throw new IllegalArgumentException("No rank Description Id found for player: " + player);
	}

	/**
	 * @param id
	 * @return The abyss rank enum by his id
	 */
	public static AbyssRankEnum getRankById(int id) {
		for (AbyssRankEnum rank : values()) {
			if (rank.getId() == id) {
				return rank;
			}
		}
		throw new IllegalArgumentException("Invalid abyss rank provided" + id);
	}

	/**
	 * @param ap
	 * @return The abyss rank enum for his needed ap
	 */
	public static AbyssRankEnum getRankForAp(int ap) {
		AbyssRankEnum r = AbyssRankEnum.GRADE9_SOLDIER;
		for (AbyssRankEnum rank : values()) {
			if (rank.getRequiredAp() <= ap) {
				r = rank;
			}
			else {
				break;
			}
		}
		return r;
	}

	/**
	 * @param gp
	 * @return The abyss rankGp enum for his needed gp
	 */
	public static AbyssRankEnum getRankForGp(int gp) {
		AbyssRankEnum rgp = null;
		for (AbyssRankEnum rank : values()) {
			if (rank.getRequiredGp() <= gp) {
				rgp = rank;
			}
			else {
				break;
			}
		}
		return rgp;
	}

	public static AbyssRankEnum getRank(int ap, int gp) {
		AbyssRankEnum rap = getRankForAp(ap);
		AbyssRankEnum rgp = getRankForGp(gp);
		if (rgp != null) {
			return rgp;
		}
		else {
			if (rap.getId() <= 9)
				return rap;
			else
				return getRankById(9);
		}
	}
}
