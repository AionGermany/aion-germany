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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.model.items.RandomBonusResult;
import com.aionemu.gameserver.model.templates.item.bonuses.StatBonusType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Rolandas
 * @author GiGatR00n
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolishAction")
public class PolishAction extends AbstractItemAction {

	@XmlAttribute(name = "set_id")
	protected int polishSetId;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (parentItem.getItemTemplate().getLevel() > targetItem.getItemTemplate().getLevel()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401649));
			return false;
		}
		// to do You need to tune your equipment before socketing Idian.
		if (targetItem.hasTune()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401750));
			return false;
		}
		return !player.isAttackMode() && targetItem.getItemTemplate().isWeapon() && targetItem.getItemTemplate().isCanPolish();
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		final int parentItemId = parentItem.getItemId();
		final int parentObjectId = parentItem.getObjectId();
		final int parentNameId = parentItem.getNameId();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItemId, 5000, 0), true);
		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300427)); // Item use cancel
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentObjectId, parentItemId, 0, 2), true);
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);

				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentObjectId, parentItemId, 0, 1), true);
				if (!player.getInventory().decreaseByObjectId(parentObjectId, 1)) {
					return;
				}
				RandomBonusResult bonus = DataManager.ITEM_RANDOM_BONUSES.getRandomModifiers(StatBonusType.POLISH, polishSetId);
				if (bonus == null) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(new DescriptionId(parentNameId)));
					return;
				}
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401650, "[item_ex:" + targetItem.getItemId() + ";" + targetItem.getItemName() + "]"));
				IdianStone idianStone = targetItem.getIdianStone();
				if (idianStone != null) {
					idianStone.onUnEquip(player);
					targetItem.setIdianStone(null);
					idianStone.setPersistentState(PersistentState.DELETED);
					DAOManager.getDAO(ItemStoneListDAO.class).storeIdianStones(idianStone);
				}
				idianStone = new IdianStone(parentItemId, PersistentState.NEW, targetItem, bonus.getTemplateNumber(), 1000000);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_POLISH_SUCCEED(targetItem.getItemTemplate().getNameId()));
				targetItem.setIdianStone(idianStone);
				if (targetItem.isEquipped()) {
					idianStone.onEquip(player);
					player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
				}
				PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
			}
		}, 5000));

	}

	public int getPolishSetId() {
		return polishSetId;
	}
}
