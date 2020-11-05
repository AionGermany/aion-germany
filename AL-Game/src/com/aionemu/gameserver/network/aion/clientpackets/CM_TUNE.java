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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.actions.TuningAction;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.RealRandomBonusService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author xTz
 */
public class CM_TUNE extends AionClientPacket {

	private int itemObjectId, tuningScrollId;

	public CM_TUNE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		itemObjectId = readD();
		tuningScrollId = readD();
	}

	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
		Storage inventory = player.getInventory();
		final Item item = inventory.getItemByObjId(itemObjectId);
		if (item == null) {
			return;
		}
		final int tunePrice = getTunePrices(item);
		if (tuningScrollId != 0) {
			Item tuningItem = inventory.getItemByObjId(tuningScrollId);
			if (tuningItem == null) {
				return;
			}
			TuningAction action = tuningItem.getItemSkinTemplate().getActions().getTuningAction();
			if (action != null && action.canAct(player, tuningItem, item)) {
				action.act(player, tuningItem, item);
            }
        }
        else {
            if (item.getOptionalSocket() != -1 && item.getItemTemplate().getRandomBonusId() == 0 && item.getItemTemplate().getRealRndBonus() == 0) {
                return;
            }
            
            final ItemTemplate template = item.getItemTemplate();
            final int nameId = template.getNameId();
            PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, item.getObjectId(), item.getItemId(), 5000, 9));
            final ItemUseObserver observer = new ItemUseObserver() {

                @Override
                public void abort() {
                    player.getController().cancelTask(TaskId.ITEM_USE);
                    player.removeItemCoolDown(template.getUseLimits().getDelayId());
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(nameId)));
                    PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0 , item.getObjectId(), item.getItemId(), 0, 11));
                    player.getObserveController().removeObserver(this);
                }
            };
            player.getObserveController().attach(observer);
            player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (item.getOptionalSocket() != -1 && item.getItemTemplate().getRandomBonusId() == 0 && item.getItemTemplate().getRealRndBonus() == 0) {
                        return;
                    }
                    if ((item.getRealRndBonus() != null || item.getRandomStats() != null) && !player.getInventory().tryDecreaseKinah(tunePrice)) {
                        return;
                    }
                    
                    item.setRandomStats(null);
                    item.setBonusNumber(0);
                    item.setRndBonus();
                    
                    if (item.getItemTemplate().getOptionSlotBonus() != 0) {
                        item.setOptionalSocket(Rnd.get(0, item.getItemTemplate().getOptionSlotBonus()));
                    }
                    
                    if (item.getRealRndBonus() == null) {
                        RealRandomBonusService.setBonus(item);
                    } else {
                        RealRandomBonusService.rerollAllBonuses(player, item);
                    }
                    
                    player.removeItemCoolDown(template.getUseLimits().getDelayId());
                    item.setPersistentState(PersistentState.UPDATE_REQUIRED);
					player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
					ItemPacketService.updateItemAfterInfoChange(player, item);
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401626, new Object[] { new DescriptionId(nameId) }));
    				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0 , item.getObjectId(), item.getItemId(), 0, 10));
                }
            }, 5000));
        }
    }

    private int getTunePrices(Item item) {
        switch (item.getItemTemplate().getItemQuality()) {
            case FINALITY:
                return 532364;
            case RELIC:
                return 133090;
            case ANCIENT:
                return 36616;
            default:
                return 36616;
        }
    }
}
