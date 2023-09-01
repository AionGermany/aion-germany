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
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.ride.RideInfo;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author Rolandas, ginho1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RideAction")
public class RideAction extends AbstractItemAction {

	@XmlAttribute(name = "npc_id")
	protected int npcId;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (parentItem == null) {
			return false;
		}

		if (CustomConfig.ENABLE_RIDE_RESTRICTION) {
			for (ZoneInstance zone : player.getPosition().getMapRegion().getZones(player)) {
				if (!zone.canRide()) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401099));
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, Item targetItem) {
		player.getController().cancelUseItem();
		if (player.isInPlayerMode(PlayerMode.RIDE)) {
			player.unsetPlayerMode(PlayerMode.RIDE);
			return;
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 3000, 0), true);
		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300427)); // Item use cancel
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 2), true);
				player.getObserveController().removeObserver(this);
			}
		};

		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.unsetState(CreatureState.ACTIVE);
				player.setState(CreatureState.RESTING);
				player.getObserveController().removeObserver(observer);
				ItemTemplate itemTemplate = parentItem.getItemTemplate();
				player.setPlayerMode(PlayerMode.RIDE, getRideInfo());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_USE_ITEM(new DescriptionId(itemTemplate.getNameId())));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_EMOTE2, 0, 0), true);
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RIDE, 0, getRideInfo().getNpcId()), true);
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 0, 1), true);
				player.getController().cancelTask(TaskId.ITEM_USE);
				QuestEngine.getInstance().rideAction(new QuestEnv(null, player, 0, 0), itemTemplate.getTemplateId());
			}
		}, 3000));

		ActionObserver rideObserver = new ActionObserver(ObserverType.ABNORMALSETTED) {

			@Override
			public void abnormalsetted(AbnormalState state) {
				if ((state.getId() & AbnormalState.DISMOUT_RIDE.getId()) > 0) {
					player.unsetPlayerMode(PlayerMode.RIDE);
				}
			}
		};
		player.getObserveController().addObserver(rideObserver);
		player.setRideObservers(rideObserver);

		// TODO some mounts have lower change of dismounting
		ActionObserver attackedObserver = new ActionObserver(ObserverType.ATTACKED) {

			@Override
			public void attacked(Creature creature) {
				if (Rnd.get(1000) < 200)// 20% from client action file
				{
					player.unsetPlayerMode(PlayerMode.RIDE);
				}
			}
		};
		player.getObserveController().addObserver(attackedObserver);
		player.setRideObservers(attackedObserver);

		ActionObserver dotAttackedObserver = new ActionObserver(ObserverType.DOT_ATTACKED) {

			@Override
			public void dotattacked(Creature creature, Effect dotEffect) {
				if (Rnd.get(1000) < 200)// 20% from client action file
				{
					player.unsetPlayerMode(PlayerMode.RIDE);
				}
			}
		};
		player.getObserveController().addObserver(dotAttackedObserver);
		player.setRideObservers(dotAttackedObserver);
	}

	public RideInfo getRideInfo() {
		return DataManager.RIDE_DATA.getRideInfo(npcId);
	}
}
