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
package com.aionemu.gameserver.model.items;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.bonuses.StatBonusType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
public class IdianStone extends ItemStone {

	private ActionObserver actionListener;
	private int polishCharge;
	private final int polishSetId;
	private final int polishNumber;
	private final Item item;
	private final ItemTemplate template;
	private final int burnDefend;
	private final int burnAttack;
	private final RandomBonusEffect rndBonusEffect;

	public IdianStone(int itemId, PersistentState persistentState, Item item, int polishNumber, int polishCharge) {
		super(item.getObjectId(), itemId, 0, persistentState);
		this.item = item;
		burnDefend = item.getItemTemplate().getIdianAction().getBurnDefend();
		burnAttack = item.getItemTemplate().getIdianAction().getBurnAttack();
		this.polishCharge = polishCharge;
		this.template = DataManager.ITEM_DATA.getItemTemplate(itemId);
		this.polishNumber = polishNumber;
		polishSetId = template.getActions().getPolishAction().getPolishSetId();
		rndBonusEffect = new RandomBonusEffect(StatBonusType.POLISH, polishSetId, polishNumber);
	}

	public void onEquip(final Player player) {
		if (polishCharge > 0) {
			actionListener = new ActionObserver(ObserverType.ALL) {

				@Override
				public void attacked(Creature creature) {
					decreasePolishCharge(player, true);
				}

				@Override
				public void attack(Creature creature) {
					decreasePolishCharge(player, false);
				}
			};
			player.getObserveController().addObserver(actionListener);
			rndBonusEffect.applyEffect(player);
		}
	}

	private synchronized void decreasePolishCharge(Player player, boolean isAttacked) {
		decreasePolishCharge(player, isAttacked, 0);
	}

	public synchronized void decreasePolishCharge(Player player, int skillValue) {
		decreasePolishCharge(player, false, skillValue);
	}

	private synchronized void decreasePolishCharge(Player player, boolean isAttacked, int skillValue) {
		int result = 0;
		if (polishCharge <= 0) {
			return;
		}
		if (skillValue == 0) {
			result = isAttacked ? burnDefend : burnAttack;
		}
		else {
			result = skillValue;
		}
		if (polishCharge - result < 0) {
			polishCharge = 0;
		}
		else {
			polishCharge -= result;
		}
		if (polishCharge == 0) {
			onUnEquip(player);
			PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, item));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401652, new DescriptionId(item.getNameId())));
			item.setIdianStone(null);
			setPersistentState(PersistentState.DELETED);
			DAOManager.getDAO(ItemStoneListDAO.class).storeIdianStones(this);
		}
	}

	public int getPolishNumber() {
		return polishNumber;
	}

	public int getPolishSetId() {
		return polishSetId;
	}

	public int getPolishCharge() {
		return polishCharge;
	}

	public void onUnEquip(Player player) {
		if (actionListener != null) {
			rndBonusEffect.endEffect(player);
			player.getObserveController().removeObserver(actionListener);
			actionListener = null;
		}
	}
}
