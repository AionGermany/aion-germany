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
package com.aionemu.gameserver.services.instance;

import org.joda.time.DateTime;

import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
public class PvPArenaService {

	public static boolean isPvPArenaAvailable(Player player, AutoGroupType agt) {
		if (AutoGroupConfig.START_TIME_ENABLE && !checkTime(agt) && player.getAccessLevel() >= 1) {
			return true;
		}
		if (AutoGroupConfig.START_TIME_ENABLE && !checkTime(agt)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401306, agt.getInstanceMapId()));
			return false;
		}
		if (!checkItem(player, agt)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400219, agt.getInstanceMapId()));
			return false;
		}
		// todo check cool down
		return true;
	}

	public static boolean checkItem(Player player, AutoGroupType agt) {
		Storage inventory = player.getInventory();
		if (agt.isPvPFFAArena() || agt.isPvPSoloArena()) {
			return inventory.getItemCountByItemId(186000135) > 0;
		}
		else if (agt.isHarmonyArena()) {
			return inventory.getItemCountByItemId(186000184) > 0;
		}
		else if (agt.isGloryArena()) {
			return inventory.getItemCountByItemId(186000185) >= 3;
		}
		return true;
	}

	private static boolean checkTime(AutoGroupType agt) {
		if (agt.isPvPFFAArena() || agt.isPvPSoloArena()) {
			return isPvPArenaAvailable();
		}
		else if (agt.isHarmonyArena()) {
			return isHarmonyArenaAvailable();
		}
		else if (agt.isGloryArena()) {
			return isGloryArenaAvailable();
		}
		return true;
	}

	private static boolean isPvPArenaAvailable() {
		DateTime now = DateTime.now();
		int hour = now.getHourOfDay();
		int day = now.getDayOfWeek();
		if (day == 6 || day == 7) {
			return hour == 0 || hour == 1 || (hour >= 10 && hour <= 23);
		}
		return hour == 0 || hour == 1 || hour == 12 || hour == 13 || (hour >= 18 && hour <= 23);
	}

	private static boolean isHarmonyArenaAvailable() {
		DateTime now = DateTime.now();
		int hour = now.getHourOfDay();
		int day = now.getDayOfWeek();
		if (day == 6) {
			return hour >= 10 || hour == 1 || hour == 2;
		}
		else if (day == 7) {
			return hour == 0 || hour == 1 || hour >= 10;
		}
		else {
			return (hour >= 10 && hour < 14) || (hour >= 18 && hour <= 23);
		}
	}

	private static boolean isGloryArenaAvailable() {
		DateTime now = DateTime.now();
		int hour = now.getHourOfDay();
		int day = now.getDayOfWeek();
		return (day == 6 || day == 7) && hour >= 20 && hour < 22;
	}
}
