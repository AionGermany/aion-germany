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
import com.aionemu.gameserver.network.aion.AionServerPacket;
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
		final int parentItemId = parentItem.getItemId();
		final int parentObjectId = parentItem.getObjectId();
		final int parentNameId = parentItem.getNameId();
		PacketSendUtility.broadcastPacket(player, (AionServerPacket) new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItemId, 1500, 0, 0), true);
		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(parentNameId)));
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentObjectId, parentItemId, 0, 14, 0));
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentObjectId, parentItemId, 0, 1, 1), true);
				if (!player.getInventory().decreaseByObjectId(parentObjectId, 1)) {
					return;
				}
				if (AdoptMinionAction.this.minionId > 980000) {
					MinionService.getInstance().adoptMinion(player, targetItem, AdoptMinionAction.this.minionId);
				} else {
					MinionService.getInstance().adoptMinion(player, targetItem, AdoptMinionAction.this.grade);
				}
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), targetItem.getObjectId(), targetItem.getItemId(), 0, 13, 0));
			}
		}, 1500));
	}

	public int getMinionId() {
		return this.minionId;
	}

	public String getGrade() {
		return this.grade;
	}
}
