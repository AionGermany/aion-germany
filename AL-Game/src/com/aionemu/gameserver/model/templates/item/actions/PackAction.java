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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ever'
 * @rework FrozenKiller
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PackAction")
public class PackAction extends AbstractItemAction {

	@XmlAttribute
	protected UseTarget target;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (target.equals(UseTarget.WEAPON) && !targetItem.getItemTemplate().isWeapon()) {
			return false;
		}

		if (target.equals(UseTarget.ARMOR) && !targetItem.getItemTemplate().isArmor()) {
			return false;
		}

		if (targetItem.getOptionalSocket() == -1) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402030));
			return false;
		}

		if (targetItem.isEquipped()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402020)); // You cannot wrap equipped items
			return false;
		}

		if (targetItem.isTradeable(player)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402022)); // A tradeable item cannot be wrapped
			return false;
		}

		if (parentItem.getItemTemplate().getItemQuality() != targetItem.getItemTemplate().getItemQuality()) {
			if (parentItem.getItemTemplate().getItemQuality().getQualityId() < targetItem.getItemTemplate().getItemQuality().getQualityId()) { // Allow's Pack with greater Scrolls (Myth Scroll + Epic
																																				// Item)
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402018, new DescriptionId(parentItem.getNameId()), new DescriptionId(targetItem.getNameId())));// %1 cannot be wrapped with
																																											// %0
				return false;
			}
		}

		int enchantPack = 0; // Added with 4.9 Enchant > 10 = PackCount +1 , Enchant > 20 = PackCount +2

		if (targetItem.getEnchantLevel() > 10 && targetItem.getEnchantLevel() < 20 && targetItem.getPackCount() < targetItem.getItemTemplate().getPackCount() + 1) {
			enchantPack += 1;
		}
		else if (targetItem.getEnchantLevel() > 20 && targetItem.getPackCount() < targetItem.getItemTemplate().getPackCount() + 2) {
			enchantPack += 2;
		}
		else if (targetItem.getEnchantLevel() <= 10 && targetItem.getPackCount() > targetItem.getItemTemplate().getPackCount()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402015, new Object[] { new DescriptionId(targetItem.getNameId()) }));
			return false;
		}
		return targetItem.getPackCount() < (targetItem.getItemTemplate().getPackCount() + enchantPack) && !targetItem.isEquipped();
	}

	public UseTarget getTarget() {
		return target;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		final int parentItemId = parentItem.getItemId();
		final int parentObjectId = parentItem.getObjectId().intValue();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), 0, parentItem.getObjectId().intValue(), parentItemId, 5000, 0), true);

		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300427)); // Item use cancel
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), 0, parentObjectId, parentItemId, 0, 2), true);

				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);

		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), 0, parentObjectId, parentItemId, 0, 1), true);
				if (!player.getInventory().decreaseByObjectId(parentObjectId, 1)) {
					return;
				}
				int _packCount = targetItem.getPackCount();
				targetItem.setPacked(true);
				targetItem.setPackCount(++_packCount);
				targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
				PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402031, new Object[] { new DescriptionId(targetItem.getNameId()) })); // Wrapping of %0 is complete
			}
		}, 5000));
	}
}
