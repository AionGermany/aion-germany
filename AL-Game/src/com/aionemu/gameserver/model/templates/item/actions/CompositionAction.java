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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CompositionConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Created with IntelliJ IDEA. User: pixfid Date: 7/14/13 Time: 5:18 PM Modded by FrozenKiller
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompositionAction")
public class CompositionAction extends AbstractItemAction {

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		return false;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem) {
	}

	public boolean canAct(Player player, Item tools, Item first, Item second) {

		if (!tools.getItemTemplate().isCombinationItem()) {
			return false;
		}

		if (!first.getItemTemplate().isEnchantmentStone()) {
			return false;
		}

		if (!second.getItemTemplate().isEnchantmentStone()) {
			return false;
		}

		if (first.getItemCount() < 1 || second.getItemCount() < 1) {
			return false;
		}

		if (first.getItemTemplate().getLevel() > 95 || second.getItemTemplate().getLevel() > 95) {
			return false;
		}

		return true;
	}

	public void act(final Player player, final Item tools, final Item first, final Item second) {

		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, tools.getObjectId(), tools.getItemTemplate().getTemplateId(), CompositionConfig.COMPOSITION_SPEED, 0));
		player.getController().cancelTask(TaskId.ITEM_USE);

		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, tools.getObjectId(), tools.getItemTemplate().getTemplateId(), 0, 2));
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				boolean result = player.getInventory().decreaseByObjectId(tools.getObjectId(), 1);
				boolean result1 = player.getInventory().decreaseByObjectId(first.getObjectId(), 1);
				boolean result2 = player.getInventory().decreaseByObjectId(second.getObjectId(), 1);
				if (result && result1 && result2) {
					ItemService.addItem(player, getItemId(calcLevel(first.getItemTemplate().getLevel(), second.getItemTemplate().getLevel(), tools.getItemTemplate().getTemplateId())), CompositionConfig.COMPOSITION_STONE_QUANTITY);
				}
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, tools.getObjectId(), tools.getItemTemplate().getTemplateId(), 0, 1));
			}
		}, CompositionConfig.COMPOSITION_SPEED));

	}

	private int calcLevel(int first, int second, int tools) {
		int itemId = tools;
		int value = ((first + second) / 2);
		if (value < 11) {
			value = Rnd.get(1, 20);
		}
		else {
			int random = Rnd.get(CompositionConfig.COMPOSITION_RND_MIN, CompositionConfig.COMPOSITION_RND_MAX);
			int bit = Rnd.get(0, 1);
			if (itemId == 165010001) { // Vindachinerk's Fine Combination Tool should only give +
				value = (bit == 0 ? value + random : value + random);
			}
			else {
				value = (bit == 0 ? value - random : value + random);
			}
		}
		return value;
	}

	public int getItemId(int value) {
		return 166000000 + value;
	}
}
