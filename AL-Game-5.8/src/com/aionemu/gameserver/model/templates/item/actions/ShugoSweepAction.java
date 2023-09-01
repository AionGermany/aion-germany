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
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.PlayerSweep;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHUGO_SWEEP;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.services.item.ItemService;

/**
 * Created by Ghostfur
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShugoSweepAction")
public class ShugoSweepAction extends AbstractItemAction {

	@XmlAttribute(name = "type")
	// 1 reset ; 2 gold dice
	protected int type;

	@XmlAttribute(name = "count")
	protected boolean count;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (type == 1) {
			if (getCommonData(player).getResetBoard() != 0) {
				PacketSendUtility.sendMessage(player, "You have already one Reset Board");
				return false;
			}
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		
		if (type == 1) {
				getCommonData(player).setResetBoard(getCommonData(player).getResetBoard() + 1);
				PacketSendUtility.sendMessage(player, "You have received one Reset Board");
		}
		if (type == 2) {
			PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 1000, 0));
			
			final ItemUseObserver observer = new ItemUseObserver() {
				
				@Override
				public void abort() {
					player.getController().cancelTask(TaskId.ITEM_USE);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 0, 2), true);
					player.getObserveController().removeObserver(this);
				}
			};
				
			player.getObserveController().attach(observer);
			player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					boolean succ = player.getInventory().decreaseByObjectId(parentItem.getObjectId().intValue(), 1);
					if (succ) {
						player.getObserveController().removeObserver(observer);
						getCommonData(player).setGoldenDice(getCommonData(player).getGoldenDice() + 1);
						PacketSendUtility.sendMessage(player, "You have received one Golden Dice");
						PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 0, succ ? 1 : 2));
						PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), 0));
					}
				}
			}, 1000));
		}
	}

	public PlayerCommonData getCommonData(Player player) {
		return player.getCommonData();
	}

	public PlayerSweep getPlayerSweep(Player player) {
		return player.getPlayerShugoSweep();
	}
}
