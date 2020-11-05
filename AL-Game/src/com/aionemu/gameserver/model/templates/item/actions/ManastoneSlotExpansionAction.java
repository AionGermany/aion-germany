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
package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "ManastoneSlotExpansionAction")
public class ManastoneSlotExpansionAction extends AbstractItemAction {

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (targetItem.getOptionalSocket() >= targetItem.getItemTemplate().getMaxSlot() && targetItem.getOptionalFusionSocket() >= targetItem.getFusionedItemTemplate().getMaxSlot()) {
			return false;
		}
		if (parentItem == null || targetItem == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		final int manaSlot = targetItem.getOptionalSocket();
		final int manaSlot2 = targetItem.getOptionalFusionSocket();
		final boolean isSlotSuccess = Rnd.chance(65);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), targetItem.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 2000, 12));

		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				player.getObserveController().removeObserver(this);
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), targetItem.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 14));
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTIONSLOT_CANCELED(targetItem.getNameId()));
			}
		};
		
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), targetItem.getItemTemplate().getOptionSlotAddCount())) {
					return;
				}
				if (isSlotSuccess) {
					if (targetItem.getOptionalSocket() >= targetItem.getItemTemplate().getMaxSlot()) {
						targetItem.setOptionalFusionSocket(manaSlot2 + 1);
					} else {
						targetItem.setOptionalSocket(manaSlot + 1);
					}
					targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTIONSLOT_SUCCEED(new DescriptionId(targetItem.getNameId())));
				} else {
					player.getController().cancelTask(TaskId.ITEM_USE);
					player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
					player.getObserveController().removeObserver(observer);
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTIONSLOT_FAILED(new DescriptionId(targetItem.getNameId())));
				}
				player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), targetItem.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, isSlotSuccess ? 13 : 14));
			}
		}, 2000));
	}
}
