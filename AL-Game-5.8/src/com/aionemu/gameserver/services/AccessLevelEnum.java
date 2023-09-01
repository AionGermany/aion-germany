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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public enum AccessLevelEnum {
	AccessLevel1(1, AdminConfig.CUSTOMTAG_ACCESS1, "\ue042Supporter\ue043", new int[] { 240, 241, 277 }),
	AccessLevel2(2, AdminConfig.CUSTOMTAG_ACCESS2, "\ue042Junior-GM\ue043", new int[] { 240, 241, 277 }),
	AccessLevel3(3, AdminConfig.CUSTOMTAG_ACCESS3, "\ue042Senior-GM\ue043", new int[] { 240, 241, 277 }),
	AccessLevel4(4, AdminConfig.CUSTOMTAG_ACCESS4, "\ue042Head-GM\ue043", new int[] { 240, 241, 277 }),
	AccessLevel5(5, AdminConfig.CUSTOMTAG_ACCESS5, "\ue042Admin\ue043", new int[] { 240, 241, 277, 282, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 395, 396 }),
	AccessLevel6(6, AdminConfig.CUSTOMTAG_ACCESS6, "\ue042Developer\ue043", new int[] { 240, 241, 277, 282, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 395, 396 }),
	AccessLevel7(7, AdminConfig.CUSTOMTAG_ACCESS7, "\ue042S-Admin L1\ue043", new int[] { 240, 241, 277, 282, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 395, 396 }),
	AccessLevel8(8, AdminConfig.CUSTOMTAG_ACCESS8, "\ue042S-Admin L2\ue043", new int[] { 240, 241, 277, 282, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 395, 396 }),
	AccessLevel9(9, AdminConfig.CUSTOMTAG_ACCESS9, "\ue042Co-Owner\ue043", new int[] { 240, 241, 277, 282, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 395, 396 }),
	AccessLevel10(10, AdminConfig.CUSTOMTAG_ACCESS10, "\ue042S-Owner\ue043", new int[] { 240, 241, 277, 282, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 395, 396 });

	private final int level;
	private final String nameLevel;
	private String status;
	private int[] skills;

	AccessLevelEnum(int id, String name, String status, int[] skills) {
		this.level = id;
		this.nameLevel = name;
		this.status = status;
		this.skills = skills;
	}

	public String getName() {
		return nameLevel;
	}

	public int getLevel() {
		return level;
	}

	public String getStatusName() {
		return status;
	}

	public int[] getSkills() {
		return skills;
	}

	public static AccessLevelEnum getAlType(int level) {
		for (AccessLevelEnum al : AccessLevelEnum.values()) {
			if (level == al.getLevel()) {
				return al;
			}
		}
		return null;
	}

	public static String getAlName(int level) {
		for (AccessLevelEnum al : AccessLevelEnum.values()) {
			if (level == al.getLevel()) {
				return al.getName();
			}
		}
		return "%s";
	}

	public static String getStatusName(Player player) {
		return player.getAccessLevel() > 0 ? AccessLevelEnum.getAlType(player.getAccessLevel()).getStatusName() : player.getLegion().getLegionName();
	}
}
