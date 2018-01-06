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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthorizeAction")
public class AuthorizeAction extends AbstractItemAction {

	@XmlAttribute(name = "count")
	private int count;
	private boolean success;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (targetItem.getItemTemplate().getAuthorize() == 0) {
			return false;
		}
		if (targetItem.getAuthorize() >= targetItem.getItemTemplate().getAuthorize()) {
			return false;
		}
		if (targetItem.getItemTemplate().isBracelet() && targetItem.getAuthorize() >= 10) {
			return false;
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), targetItem.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 5000, 0));

		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_CANCEL(targetItem.getNameId()));
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 0, 3));
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		final boolean isSuccess = isSuccess(player);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				if (player.getInventory().decreaseByItemId(parentItem.getItemId(), 1)) {
					if (!isSuccess) {
						success = false;
						targetItem.setAuthorize(0);
						if (targetItem.getItemTemplate().isBracelet()) {
							targetItem.setOptionalSocket(0);
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_FAILED(targetItem.getNameId()));
						}
						if (targetItem.getItemTemplate().isPlume()) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_STR_MSG_ITEM_AUTHORIZE_FAILED_TSHIRT(targetItem.getNameId()));
						}
						else {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_FAILED(targetItem.getNameId()));
						}
					}
					else {
						success = true;
						targetItem.setAuthorize(targetItem.getAuthorize() + 1);
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_SUCCEEDED(targetItem.getNameId(), targetItem.getAuthorize()));
					}
				}

				// Add ManastoneSocket to Bracelet.
				if (targetItem.getItemTemplate().isBracelet()) {
					switch (targetItem.getAuthorize()) {
						case 5:
							targetItem.setOptionalSocket(1);
							break;
						case 7:
							targetItem.setOptionalSocket(2);
							break;
						case 10:
							targetItem.setOptionalSocket(3);
							break;
						default:
							break;
					}
				}

				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getGameStats().updateStatsVisually();

				if (targetItem.isEquipped()) {
					player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
				}
				else {
					player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
				}

				ItemPacketService.updateItemAfterInfoChange(player, targetItem);

				if (!success) {
					PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 0, 2));
				}
				else {
					PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 0, 1));
				}
			}
		}, 5000));
	}

	public boolean isSuccess(Player player) {
		int rnd = Rnd.get(0, 1000);
		if (rnd < 700) {
			if (player.getAccessLevel() > 2) {
				PacketSendUtility.sendMessage(player, "Success! Rnd: " + rnd);
			}
			return true;
		}
		else {
			if(player.getAccessLevel() > 2) {
				PacketSendUtility.sendMessage(player, "Fail! Rnd: " + rnd);
			}
			return false;
		}
	}
}
