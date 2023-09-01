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
package com.aionemu.gameserver.utils;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author antness
 */
public class ChatUtil {

	public static String position(String label, WorldPosition pos) {
		return position(label, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ());
	}

	public static String position(String label, long worldId, float x, float y, float z) {
		// TODO: need rework for abyss map
		return String.format("[pos:%s;%d %f %f %f -1]", label, worldId, x, y, z);
	}

	public static String item(long itemId) {
		return String.format("[item: %d]", itemId);
	}

	public static String recipe(long recipeId) {
		return String.format("[recipe: %d]", recipeId);
	}

	public static String quest(int questId) {
		return String.format("[quest: %d]", questId);
	}

	public static String removePattern(String PlayerName, String Pattern) {

		int index = Pattern.indexOf("%s");
		if (index == -1)
			return PlayerName;

		String RealName = "";
		RealName = PlayerName.replace(Pattern.substring(0, index), "");
		RealName = RealName.replace(Pattern.substring(index + 2), "");

		return RealName;
	}

	public static String getRealAdminName(String PlayerName) {
		String RealAdminName = "";
		RealAdminName = removePattern(PlayerName, MembershipConfig.TAG_VIP);
		RealAdminName = removePattern(RealAdminName, MembershipConfig.TAG_PREMIUM);
		RealAdminName = removePattern(RealAdminName, WeddingsConfig.TAG_WEDDING);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS1);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS2);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS3);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS4);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS5);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS6);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS7);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS8);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS9);
		RealAdminName = removePattern(RealAdminName, AdminConfig.CUSTOMTAG_ACCESS10);
		return RealAdminName;
	}
}
