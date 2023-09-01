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

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.actions.AbstractItemAction;
import com.aionemu.gameserver.model.templates.item.actions.IHouseObjectDyeAction;
import com.aionemu.gameserver.model.templates.item.actions.InstanceTimeClear;
import com.aionemu.gameserver.model.templates.item.actions.ItemActions;
import com.aionemu.gameserver.model.templates.item.actions.MultiReturnAction;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Avol
 * @author GiGatR00n v4.7.5.x
 * @rework FrozenKiller
 */
public class CM_USE_ITEM extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_USE_ITEM.class);
	public int uniqueItemId;
	public int type, targetItemId, syncId, returnId;

	public CM_USE_ITEM(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		uniqueItemId = readD();
		type = readC();
		if (type == 2) {
			targetItemId = readD();
		}
		else if (type == 5) {
			syncId = readD();
		}
		else if (type == 6) {
			returnId = readD();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		if (player.isProtectionActive()) {
			player.getController().stopProtectionActiveTask();
		}

		Item item = player.getInventory().getItemByObjId(uniqueItemId);
		Item targetItem = player.getInventory().getItemByObjId(targetItemId);
		HouseObject<?> targetHouseObject = null;

		if (item == null) { // Cancel
			player.getController().cancelUseItem();
			player.getController().onMove();
			return;
		}

		if (targetItem == null) {
			targetItem = player.getEquipment().getEquippedItemByObjId(targetItemId);
		}
		if (targetItem == null && player.getHouseRegistry() != null) {
			targetHouseObject = player.getHouseRegistry().getObjectByObjId(targetItemId);
		}

		if (item.getItemTemplate().getTemplateId() == 165000001 && targetItem.getItemTemplate().canExtract()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return;
		}

		// check use item multicast delay exploit cast (spam)
		if (player.isCasting()) {
			// PacketSendUtility.sendMessage(this.getOwner(),
			// "You must wait until cast time finished to use skill again.");
			player.getController().cancelCurrentSkill();
			// On retail the item is cancelling the current skill and then procs normally
			// return;
		}

		if (!RestrictionsManager.canUseItem(player, item)) {
			return;
		}

		if (item.getItemTemplate().getRace() != Race.PC_ALL && item.getItemTemplate().getRace() != player.getRace()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_INVALID_RACE);
			return;
		}

		int requiredLevel = item.getItemTemplate().getRequiredLevel(player.getCommonData().getPlayerClass());
		if (requiredLevel == -1) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_INVALID_CLASS);
			return;
		}

		if (requiredLevel > player.getLevel()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_LOW_LEVEL_MUST_BE_THIS_LEVEL(item.getNameId(), requiredLevel));
			return;
		}

		HandlerResult result = QuestEngine.getInstance().onItemUseEvent(new QuestEnv(null, player, 0, 0), item);
		if (result == HandlerResult.FAILED) {
			return; // don't remove item
		}
		ItemActions itemActions = item.getItemTemplate().getActions();
		ArrayList<AbstractItemAction> actions = new ArrayList<AbstractItemAction>();

		if (itemActions == null) {
			log.warn(String.format("CHECKPOINT: No Item use Action: %d %d", player.getObjectId(), item.getItemTemplate().getTemplateId()));
			return;
		}

		for (AbstractItemAction itemAction : itemActions.getItemActions()) {
			// check if the item can be used before placing it on the cooldown list.
			if (targetHouseObject != null && itemAction instanceof IHouseObjectDyeAction) {
				IHouseObjectDyeAction action = (IHouseObjectDyeAction) itemAction;
				if (action != null && action.canAct(player, item, targetHouseObject)) {
					actions.add(itemAction);
				}
			}
			else if (itemAction.canAct(player, item, targetItem)) {
				actions.add(itemAction);
			}
		}

		if (actions.size() == 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_IS_NOT_USABLE);
			return;
		}

		// Store Item CD in server Player variable.
		// Prevents potion spamming, and relogging to use kisks/aether jelly/long CD items.
		if (player.isItemUseDisabled(item.getItemTemplate().getUseLimits())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANT_USE_UNTIL_DELAY_TIME);
			return;
		}

		int useDelay = player.getItemCooldown(item.getItemTemplate());
		if (useDelay > 0) {
			player.addItemCoolDown(item.getItemTemplate().getUseLimits().getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);
		}

		// notify item use observer
		player.getObserveController().notifyItemuseObservers(item);

		for (AbstractItemAction itemAction : actions) {
			if (targetHouseObject != null && itemAction instanceof IHouseObjectDyeAction) {
				IHouseObjectDyeAction action = (IHouseObjectDyeAction) itemAction;
				action.act(player, item, targetHouseObject);
			}
			else if (type == 5) {
				// Instance Reset Scroll's
				if (itemAction instanceof InstanceTimeClear) {
					InstanceTimeClear action = (InstanceTimeClear) itemAction;
					int SelectedSyncId = syncId;
					action.act(player, item, SelectedSyncId);
				}
			}
			else if (type == 6) {
				// Multi Returns Items (Scroll Teleporter)
				if (itemAction instanceof MultiReturnAction) {
					MultiReturnAction action = (MultiReturnAction) itemAction;
					int SelectedMapIndex = returnId;
					action.act(player, item, SelectedMapIndex);
				}
			}
			else {
				itemAction.act(player, item, targetItem);
			}
		}
	}
}
