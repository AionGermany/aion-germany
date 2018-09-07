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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.PetCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class PetAdoptionService {

	private static final Logger log = LoggerFactory.getLogger(PetAdoptionService.class);

	/**
	 * Create a pet for player (with validation)
	 *
	 * @param player
	 * @param eggObjId
	 * @param petId
	 * @param name
	 * @param decorationId
	 */
	public static void adoptPet(Player player, int eggObjId, int petId, String name, int decorationId) {

		int eggId = player.getInventory().getItemByObjId(eggObjId).getItemId();
		ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(eggId);

		if (!validateAdoption(player, template, petId)) {
			return;
		}

		if (!player.getInventory().decreaseByObjectId(eggObjId, 1)) {
			return;
		}

		int expireTime = template.getActions().getAdoptPetAction().getExpireMinutes() != 0 ? (int) ((System.currentTimeMillis() / 1000) + template.getActions().getAdoptPetAction().getExpireMinutes() * 60) : 0;

		addPet(player, petId, name, decorationId, expireTime);
	}

	/**
	 * Add pet to player
	 *
	 * @param player
	 * @param petId
	 * @param name
	 * @param decorationId
	 */
	public static void addPet(Player player, int petId, String name, int decorationId, int expireTime) {
		PetCommonData petCommonData = player.getPetList().addPet(player, petId, decorationId, name, expireTime);
		if (petCommonData != null) {
			PacketSendUtility.sendPacket(player, new SM_PET(1, petCommonData));
			if (expireTime > 0) {
				ExpireTimerTask.getInstance().addTask(petCommonData, player);
			}
		}
	}

	private static boolean validateAdoption(Player player, ItemTemplate template, int petId) {
		if (template == null || template.getActions() == null || template.getActions().getAdoptPetAction() == null || template.getActions().getAdoptPetAction().getPetId() != petId) {
			return false;
		}
		if (player.getPetList().hasPet(petId)) {
			log.warn("Duplicate pet adoption");
			return false;
		}
		if (DataManager.PET_DATA.getPetTemplate(petId) == null) {
			log.warn("Trying adopt pet without template. PetId:" + petId);
			return false;
		}
		return true;
	}

	/**
	 * Delete pet
	 *
	 * @param player
	 * @param petId
	 */
	public static void surrenderPet(Player player, int petId) {
		PetCommonData petCommonData = player.getPetList().getPet(petId);
		if (player.getPet() != null && player.getPet().getPetId() == petCommonData.getPetId()) {
			if (petCommonData.getFeedProgress() != null) {
				petCommonData.setCancelFeed(true);
			}
			PetSpawnService.dismissPet(player, false);
		}
		player.getPetList().deletePet(petCommonData.getPetId());
		PacketSendUtility.sendPacket(player, new SM_PET(2, petCommonData));
	}
}
