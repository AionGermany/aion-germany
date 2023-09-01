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
package com.aionemu.gameserver.network.aion.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.PetAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.services.NameRestrictionService;
import com.aionemu.gameserver.services.toypet.MinionService;
import com.aionemu.gameserver.services.toypet.PetAdoptionService;
import com.aionemu.gameserver.services.toypet.PetMoodService;
import com.aionemu.gameserver.services.toypet.PetService;
import com.aionemu.gameserver.services.toypet.PetSpawnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author M@xx, xTz
 */
public class CM_PET extends AionClientPacket {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CM_PET.class);
	private int actionId;
	private PetAction action;
	private int petId;
	private String petName;
	private int decorationId;
	private int eggObjId;
	private int objectId;
	private int count;
	private int subType;
	private int emotionId;
	private int actionType;
	private int dopingItemId;
	private int dopingAction;
	private int dopingSlot1;
	private int dopingSlot2;
	private int activateLoot;
	@SuppressWarnings("unused")
	private int unk2;
	@SuppressWarnings("unused")
	private int unk3;
	@SuppressWarnings("unused")
	private int unk5;
	@SuppressWarnings("unused")
	private int unk6;

	// Buff
	private int activateCheering;
	@SuppressWarnings("unused")
	private int unkCheer2;
	@SuppressWarnings("unused")
	private int unkCheer3;

	// Merchand
	private int activateAutoSell;
	@SuppressWarnings("unused")
	private int unkMerchand2;
	@SuppressWarnings("unused")
	private int unkMerchand3;

	public CM_PET(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionId = readH();
		action = PetAction.getActionById(actionId);
		switch (action) {
			case ADOPT:
				eggObjId = readD();
				petId = readD();
				unk2 = readC();
				unk3 = readD();
				decorationId = readD();
				unk5 = readD();
				unk6 = readD();
				petName = readS();
				break;
			case SURRENDER:
			case SPAWN:
			case DISMISS:
				petId = readD();
				break;
			case FOOD:
				actionType = readD();
				if (actionType == 3) {
					activateLoot = readD();
				}
				else if (actionType == 2) {
					dopingAction = readD();
					if (dopingAction == 0) { // add item
						dopingItemId = readD();
						dopingSlot1 = readD();
					}
					else if (dopingAction == 1) { // remove item
						dopingSlot1 = readD();
						dopingItemId = readD();
					}
					else if (dopingAction == 2) { // move item
						dopingSlot1 = readD();
						dopingSlot2 = readD();
					}
					else if (dopingAction == 3) { // use doping
						dopingItemId = readD();
						dopingSlot1 = readD();
					}
				}
				else if (actionType == 4) {
					activateAutoSell = readD();
					unkMerchand2 = readD();
					unkMerchand3 = readD();
				}
				else if (actionType == 5) {
					activateCheering = readD();
					unkCheer2 = readD();
					unkCheer3 = readD();
				}
				else {
					objectId = readD();
					count = readD();
					unk2 = readD();
				}
				break;
			case RENAME:
				petId = readD();
				petName = readS();
				break;
			case MOOD:
				subType = readD();
				emotionId = readD();
				break;
			default:
				break;
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
		Pet pet = player.getPet();
		switch (action) {
			case ADOPT:
				if (NameRestrictionService.isForbiddenWord(petName)) {
					PacketSendUtility.sendMessage(player, "You are trying to use a forbidden name. Choose another one!");
				}
				else {
					PetAdoptionService.adoptPet(player, eggObjId, petId, petName, decorationId);
				}
				break;
			case SURRENDER:
				PetAdoptionService.surrenderPet(player, petId);
				break;
			case SPAWN:
                if (player.getMinion() != null) {
                    MinionService.getInstance().despawnMinion(player, 0);
                }
				PetSpawnService.summonPet(player, petId, true);
				break;
			case DISMISS:
				PetSpawnService.dismissPet(player, true);
				break;
			case FOOD:
				if (actionType == 2) {
					// Pet doping
					if (dopingAction == 2) {
						PetService.getInstance().relocateDoping(player, dopingSlot1, dopingSlot2);
					}
					else {
						PetService.getInstance().useDoping(player, dopingAction, dopingItemId, dopingSlot1);
					}
				}
				else if (actionType == 3) {
					// Pet looting
					PetService.getInstance().activateLoot(player, activateLoot != 0);
				}
				else if (actionType == 4) {
					if (activateAutoSell == 1) {
						PetService.getInstance().activeAutoSell(player, true);
					}
					else if (activateAutoSell == 0) {
						PetService.getInstance().activeAutoSell(player, false);
					}
				}
				else if (actionType == 5) {
					if (activateCheering == 1) {
						PetService.getInstance().activateBuff(player, true);
					}
					else if (activateCheering == 0) {
						PetService.getInstance().activateBuff(player, false);
					}
				}
				else if (pet != null) {
					if (objectId == 0) {
						pet.getCommonData().setCancelFeed(true);
						PacketSendUtility.sendPacket(player, new SM_PET(4, actionId, 0, 0, player.getPet()));
						PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.END_FEEDING, 0, player.getObjectId()));
					}
					else if (!pet.getCommonData().isFeedingTime()) {
						PacketSendUtility.sendPacket(player, new SM_PET(8, actionId, objectId, count, player.getPet()));
					}
					else {
						PetService.getInstance().removeObject(objectId, count, actionId, player);
					}
				}
				break;
			case RENAME:
				if (NameRestrictionService.isForbiddenWord(petName)) {
					PacketSendUtility.sendMessage(player, "You are trying to use a forbidden name. Choose another one!");
				}
				else {
					PetService.getInstance().renamePet(player, petName);
				}
				break;
			case MOOD:
				if (pet != null && (subType == 0 && pet.getCommonData().getMoodRemainingTime() == 0 || (subType == 3 && pet.getCommonData().getGiftRemainingTime() == 0) || emotionId != 0)) {
					PetMoodService.checkMood(pet, subType, emotionId);
				}
			default:
				break;
		}
	}
}
