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
package com.aionemu.gameserver.services.abyss;

import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

enum AbyssSkills {

	SUPREME_COMMANDER(Race.ELYOS, AbyssRankEnum.SUPREME_COMMANDER, new int[] { 11889, 11898, 11900, 11903, 11904, 11905, 11906 }),
	COMMANDER(Race.ELYOS, AbyssRankEnum.COMMANDER, new int[] { 11888, 11898, 11900, 11903, 11904 }),
	GREAT_GENERAL(Race.ELYOS, AbyssRankEnum.GREAT_GENERAL, new int[] { 11887, 11897, 11899, 11903 }),
	GENERAL(Race.ELYOS, AbyssRankEnum.GENERAL, new int[] { 11886, 11896, 11899 }),
	STAR5_OFFICER(Race.ELYOS, AbyssRankEnum.STAR5_OFFICER, new int[] { 11885, 11895 }),
	SUPREME_COMMANDER_A(Race.ASMODIANS, AbyssRankEnum.SUPREME_COMMANDER, new int[] { 11894, 11898, 11902, 11903, 11904, 11905, 11906 }),
	COMMANDER_A(Race.ASMODIANS, AbyssRankEnum.COMMANDER, new int[] { 11893, 11898, 11902, 11903, 11904 }),
	GREAT_GENERAL_A(Race.ASMODIANS, AbyssRankEnum.GREAT_GENERAL, new int[] { 11892, 11897, 11901, 11903 }),
	GENERAL_A(Race.ASMODIANS, AbyssRankEnum.GENERAL, new int[] { 11891, 11896, 11901 }),
	STAR5_OFFICER_A(Race.ASMODIANS, AbyssRankEnum.STAR5_OFFICER, new int[] { 11890, 11895 });

	private int[] skills;
	private AbyssRankEnum rankenum;
	private Race race;

	private AbyssSkills(Race race, AbyssRankEnum rankEnum, int[] skills) {
		this.race = race;
		rankenum = rankEnum;
		this.skills = skills;
	}

	public Race getRace() {
		return race;
	}

	public int[] getSkills() {
		return skills;
	}

	public static int[] getSkills(Race race, AbyssRankEnum rank) {
		for (AbyssSkills aSkills : values()) {
			if ((aSkills.race == race) && (aSkills.rankenum == rank)) {
				return aSkills.skills;
			}
		}
		LoggerFactory.getLogger(AbyssSkills.class).warn("No abyss skills for: " + race + " " + rank);
		return new int[0];
	}
}
