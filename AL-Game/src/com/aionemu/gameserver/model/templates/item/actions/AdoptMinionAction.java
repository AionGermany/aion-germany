/*
 * Decompiled with CFR 0_123.
 */
package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.toypet.MinionService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class AdoptMinionAction extends AbstractItemAction {
	
	@XmlAttribute(name = "minion_id")
	private int minionId;
	@XmlAttribute(name = "tier_grade")
	private String grade;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0 , parentItem.getObjectId(), parentItem.getItemId(), 1500, 0), true);
		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(parentItem.getNameId())));
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0 , parentItem.getObjectId(), parentItem.getItemId(), 0, 14));
				player.getObserveController().removeObserver(this);
			}
		};
		
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
					return;
				}
				if (minionId != 0) {
					MinionService.getInstance().adoptMinion(player, targetItem, minionId);
				} else {
					MinionService.getInstance().adoptMinion(player, targetItem, getGrade());
				}
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0 , parentItem.getObjectId(), parentItem.getItemId(), 0, 1));
			}
		}, 1500));
	}

	public int getMinionId() {
		return minionId;
	}

	public String getGrade() {
		return grade;
	}
}
