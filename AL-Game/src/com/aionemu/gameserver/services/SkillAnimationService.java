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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author FrozenKiller
 */
public class SkillAnimationService {

	public static int getFirstSkillId(Player player, int skillId) {
		return firstSkillId(player, skillId);
	}

	public static int getAnimationId(Player player, int firstSkillId) {
		return animationId(player, firstSkillId);
	}

	private static int firstSkillId(Player player, int skillId) {
		switch (player.getPlayerClass()) {
			case GLADIATOR: {
				if (skillId >= 644 && skillId <= 647) {
					return 644;
				}
				else if (skillId >= 710 && skillId <= 719) {
					return 710;
				}
				else if (skillId >= 769 && skillId <= 780) {
					return 769;
				}
				break;
			}
			case TEMPLAR: {
				if (skillId >= 3125 && skillId <= 3126) {
					return 3125;
				}
				else if (skillId == 3123) {
					return 3123;
				}
				else if (skillId >= 3072 && skillId <= 3083) {
					return 3072;
				}
				break;
			}
			case ASSASSIN: {
				if (skillId == 3330 || (skillId >= 4591 && skillId <= 4596)) {// STIGMA
					return 3330;
				}
				else if (skillId >= 3455 && skillId <= 3466) {
					return 3455;
				}
				else if (skillId >= 3470 && skillId <= 3477) {
					return 3470;
				}
				break;
			}
			case RANGER: {
				if (skillId >= 1102 && skillId <= 1110) {
					return 1102;
				}
				else if (skillId >= 896 && skillId <= 905) {
					return 896;
				}
				else if (skillId >= 784 && skillId <= 795) {
					return 784;
				}
				break;
			}
			case SORCERER: {
				if (skillId >= 1486 && skillId <= 1493) {// STIGMA
					return 1486;
				}
				else if (skillId >= 1391 && skillId <= 1393) {
					return 1391;
				}
				else if (skillId >= 1550 && skillId <= 1555) {// STIGMA
					return 1550;
				}
				break;
			}
			case SPIRIT_MASTER: {
				if ((skillId >= 3640 && skillId <= 3642) || (skillId >= 4661 && skillId <= 4665)) {
					return 3640;
				}
				else if (skillId >= 3556 && skillId <= 3561) {// STIGMA
					return 3556;
				}
				else if (skillId >= 3840 && skillId <= 3847) {// STIGMA
					return 3840;
				}
				break;
			}
			case CLERIC: {
				if (skillId >= 4037 && skillId <= 4045) {
					return 4037;
				}
				else if (skillId >= 3960 && skillId <= 3967) {// STIGMA
					return 3960;
				}
				else if (skillId >= 4150 && skillId <= 4151) {
					return 4150;
				}
				break;
			}
			case CHANTER: {
				if (skillId == 1768) {// Chanter
					return 1768;
				}
				else if (skillId >= 1667 && skillId <= 1676) {
					return 1667;
				}
				else if (skillId >= 1640 && skillId <= 1647) {// STIGMA
					return 1640;
				}
				break;
			}
			case RIDER: {
				if (skillId >= 2606 && skillId <= 2641) {
					return 2606;
				}
				else if (skillId >= 2807 && skillId <= 2818) {
					return 2807;
				}
				else if (skillId >= 2437 && skillId <= 2439) {// STIGMA
					return 2437;
				}
				break;
			}
			case GUNNER: {
				if (skillId >= 2066 && skillId <= 2068) {// STIGMA
					return 2066;
				}
				else if (skillId >= 2274 && skillId <= 2297) {// STIGMA
					return 2274;
				}
				else if (skillId >= 1954 && skillId <= 1956) {
					return 1954;
				}
				break;
			}
			case BARD: {
				if (skillId >= 4524 && skillId <= 4529) {// STIGMA
					return 4524;
				}
				else if (skillId >= 4474 && skillId <= 4482) {// STIGMA
					return 4474;
				}
				else if (skillId >= 4471 && skillId <= 4473) {
					return 4471;
				}
				break;
			}
			default:
				break;
		}
		return skillId;
	}

	private static int animationId(Player player, int firstSkillId) {
		if (player.getSkillList().getSkillEntry(firstSkillId) != null && player.getSkillList().getSkillEntry(firstSkillId).getSkillAnimationEnabled() != null) {
			if (player.getSkillList().getSkillEntry(firstSkillId).getSkillAnimationEnabled() == 1) {
				return player.getSkillList().getSkillEntry(firstSkillId).getSkillAnimation();
			}
			else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}
}
