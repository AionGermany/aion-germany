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
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EquipedLevelAdjAction")
public class EquipedLevelAdjAction extends AbstractItemAction {

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (parentItem == null || targetItem == null) {
			// No items for recommended level reduction could be found
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_NO_TARGET_ITEM);
			return false;
		}
		if (targetItem.isPacked()) {
			// You cannot reduce the recommended level of packed items
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_WRONG_PACK);
			return false;
			// } if (targetItem.hasRetuning()) {
			// //You need to use tuning if you want to use the recommended level reduction function
			// PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_NEED_IDENTIFY);
			// return false;
		}
		if (targetItem.getReductionLevel() > 5) {
			// You cannot reduce the recommended level of %0 any further
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_WRONG_MAX(targetItem.getNameId()));
			return false;
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0));
		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), 0, parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), 0, 3));
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				// The reduction of %0‘s recommended level was canceled
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_CANCEL(targetItem.getNameId()));
			}
		};
		player.getObserveController().attach(observer);
		final boolean isReductionSuccess = isReductionSuccess(player);
		final int reductionCount = reductionCount(player);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (player.getInventory().decreaseByItemId(parentItem.getItemId(), 1)) {
					player.getController().cancelTask(TaskId.ITEM_USE);
					player.getObserveController().removeObserver(observer);
					EnchantService.reductItemAct(player, parentItem, targetItem, targetItem.getReductionLevel(), isReductionSuccess, reductionCount);
				}
			}
		}, 5000));
	}

	public boolean isReductionSuccess(Player player) {
		int reduction = Rnd.get(1, 1000);
		if (reduction < 600) {
			if (player.getAccessLevel() > 0) {
				PacketSendUtility.sendMessage(player, "Success! Reduction Level: " + reduction + " Lucky: 600");
			}
			return true;
		}
		else {
			if (player.getAccessLevel() > 0) {
				PacketSendUtility.sendMessage(player, "Fail! Reduction Level: " + reduction + " Lucky: 600");
			}
			return false;
		}
	}

	public int reductionCount(Player player) {
		return Rnd.get(1, 3);
	}
}
