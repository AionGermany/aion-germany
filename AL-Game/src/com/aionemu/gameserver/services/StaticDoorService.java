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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.staticdoor.StaticDoorState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Wakizashi
 */
public class StaticDoorService {

	private static final Logger log = LoggerFactory.getLogger(StaticDoorService.class);

	public static StaticDoorService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final StaticDoorService instance = new StaticDoorService();
	}

	public void openStaticDoor(final Player player, int doorId) {
		if (player.getAccessLevel() >= 1) {
			PacketSendUtility.sendMessage(player, "door id : " + doorId);
		}

		StaticDoor door = player.getPosition().getWorldMapInstance().getDoors().get(doorId);
		if (door == null) {
			log.warn("Not spawned door worldId: " + player.getWorldId() + " doorId: " + doorId);
			return;
		}
		int keyId = door.getObjectTemplate().getKeyId();

		if (player.getAccessLevel() >= 1) {
			PacketSendUtility.sendMessage(player, "key id : " + keyId);
		}

		if (checkStaticDoorKey(player, keyId)) {
			door.setOpen(true);
		}
		else {
			log.info("Opening door without key ...");
		}
	}

	public void changeStaticDoorState(final Player player, int doorId, boolean open, int state) {
		StaticDoor door = player.getPosition().getWorldMapInstance().getDoors().get(doorId);
		if (door == null) {
			PacketSendUtility.sendMessage(player, "Door is not spawned!");
			return;
		}
		door.changeState(open, state);
		String currentStates = "";
		for (StaticDoorState st : StaticDoorState.values()) {
			if (st == StaticDoorState.NONE) {
				continue;
			}
			if (door.getStates().contains(st)) {
				currentStates += st.toString() + ", ";
			}
		}
		if ("".equals(currentStates)) {
			currentStates = "NONE";
		}
		else {
			currentStates = currentStates.substring(0, currentStates.length() - 2);
		}
		PacketSendUtility.sendMessage(player, "Door states now are: " + currentStates);
	}

	public boolean checkStaticDoorKey(Player player, int keyId) {
		if (player.getAccessLevel() >= AdminConfig.DOORS_OPEN) {
			return true;
		}

		if (keyId == 0) {
			return true;
		}

		if (keyId == 1) {
			return false;
		}

		if (!player.getInventory().decreaseByItemId(keyId, 1)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_OPEN_DOOR_NEED_KEY_ITEM);
			return false;
		}

		return true;
	}
}
