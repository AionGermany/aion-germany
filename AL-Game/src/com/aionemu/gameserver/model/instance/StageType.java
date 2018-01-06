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
package com.aionemu.gameserver.model.instance;

/**
 * @author xTz
 */
public enum StageType {

	DEFAULT(0, 0), // 34464
	START_STAGE_1_ELEVATOR(35464, 1),
	START_STAGE_1_ROUND_1(35465, 1),
	START_STAGE_1_ROUND_2(35466, 1),
	START_STAGE_1_ROUND_3(35467, 1),
	START_STAGE_1_ROUND_4(35468, 1),
	START_STAGE_1_ROUND_5(35469, 1),
	START_STAGE_2_ELEVATOR(36464, 1),
	START_STAGE_2_ROUND_1(36465, 1),
	START_STAGE_2_ROUND_2(36466, 1),
	START_STAGE_2_ROUND_3(36467, 1),
	START_STAGE_2_ROUND_4(36468, 1),
	START_STAGE_2_ROUND_5(36469, 1),
	START_STAGE_3_ELEVATOR(37464, 1),
	START_STAGE_3_ROUND_1(37465, 1),
	START_STAGE_3_ROUND_2(37466, 1),
	START_STAGE_3_ROUND_3(37467, 1),
	START_STAGE_3_ROUND_4(37468, 1),
	START_STAGE_3_ROUND_5(37469, 1),
	START_STAGE_4_ELEVATOR(38464, 1),
	START_STAGE_4_ROUND_1(38465, 1),
	START_ALTERNATIVE_STAGE_4_ROUND_1(38465, 1),
	START_STAGE_4_ROUND_2(38466, 1),
	START_STAGE_4_ROUND_3(38467, 1),
	START_STAGE_4_ROUND_4(38468, 1),
	START_STAGE_4_ROUND_5(38469, 1),
	START_STAGE_5(8392, 3),
	START_STAGE_5_ROUND_1(8393, 3),
	START_STAGE_5_ROUND_2(8394, 3),
	START_STAGE_5_ROUND_3(8395, 3),
	START_STAGE_5_ROUND_4(8396, 3),
	START_STAGE_5_ROUND_5(8397, 3),
	START_STAGE_6(43856, 4),
	START_STAGE_6_ROUND_1(43857, 4),
	START_STAGE_6_ROUND_2(43858, 4),
	START_STAGE_6_ROUND_3(43859, 4),
	START_STAGE_6_ROUND_4(43860, 4),
	START_STAGE_6_ROUND_5(43861, 4),
	START_STAGE_7(13784, 6),
	START_STAGE_7_ROUND_1(13785, 6),
	START_STAGE_7_ROUND_2(13786, 6),
	START_STAGE_7_ROUND_3(13787, 6),
	START_STAGE_7_ROUND_4(13788, 6),
	START_STAGE_7_ROUND_5(13789, 6),
	START_STAGE_8(49248, 7),
	START_STAGE_8_ROUND_1(49249, 7),
	START_STAGE_8_ROUND_2(49250, 7),
	START_STAGE_8_ROUND_3(49251, 7),
	START_STAGE_8_ROUND_4(49252, 7),
	START_STAGE_8_ROUND_5(49253, 7),
	START_STAGE_9(19176, 9),
	START_STAGE_9_ROUND_1(19177, 9),
	START_STAGE_9_ROUND_2(19178, 9),
	START_STAGE_9_ROUND_3(19179, 9),
	START_STAGE_9_ROUND_4(19180, 9),
	START_STAGE_9_ROUND_5(19181, 9),
	START_STAGE_10(54640, 10),
	START_STAGE_10_ROUND_1(54641, 10),
	START_STAGE_10_ROUND_2(54642, 10),
	START_STAGE_10_ROUND_3(54643, 10),
	START_STAGE_10_ROUND_4(54644, 10),
	START_STAGE_10_ROUND_5(54645, 10),
	PASS_STAGE_1(35566, 1),
	PASS_STAGE_2(36565, 1),
	PASS_STAGE_4(38566, 1),
	PASS_STAGE_5(39566, 1),
	PASS_STAGE_6(40565, 1),
	PASS_GROUP_STAGE_1(35569, 1),
	PASS_GROUP_STAGE_2(36569, 1),
	PASS_GROUP_STAGE_3(37569, 1),
	PASS_GROUP_STAGE_4(38569, 1),
	PASS_GROUP_STAGE_5(8497, 3),
	PASS_GROUP_STAGE_6(43961, 4),
	PASS_GROUP_STAGE_7(13789, 6),
	PASS_GROUP_STAGE_8(49253, 7),
	PASS_GROUP_STAGE_9(19181, 9),
	PASS_GROUP_STAGE_10(54645, 10),
	START_BONUS_STAGE_2(36470, 1),
	START_BONUS_STAGE_3(37470, 1),
	START_BONUS_STAGE_6(43862, 4),
	START_BONUS_STAGE_4(38470, 1),
	PVP_STAGE_1(1, 0),
	PVP_STAGE_2(2, 0),
	PVP_STAGE_3(3, 0),
	PVP_STAGE_4(4, 0),
	PVP_STAGE_5(5, 0),
	PVP_STAGE_6(6, 0),
	PVP_STAGE_OVER(0, 0);

	private int id;
	private int type;

	private StageType(int id, int type) {
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}
}
