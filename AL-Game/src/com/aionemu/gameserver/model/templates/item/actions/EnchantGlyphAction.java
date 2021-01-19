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

import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.achievement.AchievementActionType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.enchant.EnchantService;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.player.AchievementService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "EnchantDestructionAction")
public class EnchantGlyphAction extends AbstractItemAction {

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (parentItem == null || targetItem == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		if (parentItem.getItemTemplate().isGlyphEnchant() && player.getInventory().getKinah() < (long) EnchantService.EnchantKinah(targetItem)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return false;
		}
		if (targetItem.getEnchantOrAuthorizeLevel() >= targetItem.getItemTemplate().getMaxEnchantLevel()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_IT_CAN_NOT_BE_ENCHANTED_MORE_TIME(targetItem.getNameId()));
			return false;
		}
		if (!targetItem.getItemTemplate().isGlyph()) {
			return false;
		}
		AchievementService.getInstance().onUpdateAchievementAction(player, parentItem.getItemId(), 1, AchievementActionType.ITEM_PLAY);
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		if (player.getInventory().getKinah() < EnchantService.EnchantKinah(targetItem)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return;
		}
		int enchantCast = 0;
		enchantCast = player.getGameStats().getStat(StatEnum.ENCHANT_BOOST, 0).getCurrent() != 0 ? EnchantsConfig.ENCHANT_SPEED / 2 - EnchantsConfig.ENCHANT_SPEED * player.getGameStats().getStat(StatEnum.ENCHANT_BOOST, 0).getCurrent() / 100 : EnchantsConfig.ENCHANT_SPEED;

		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), enchantCast, 0, 0));
		final ItemUseObserver moveObserver = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), targetItem.getObjectId(), targetItem.getItemTemplate().getTemplateId(), 0, 3, 0));
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_CANCELED(targetItem.getItemTemplate().getNameId()));
			}
		};
		player.getObserveController().attach(moveObserver);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(moveObserver);

				EnchantService.enchantGlyphItemAct(player, parentItem, targetItem, targetItem.getEnchantOrAuthorizeLevel(), true);
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 1, 384));
			}
		}, enchantCast));
	}
}
