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

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.trade.TradePSItem;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.PrivateStoreService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Simple
 */
public class CM_PRIVATE_STORE extends AionClientPacket {

	/**
	 * Private store information
	 */
	private Player activePlayer;
	private TradePSItem[] tradePSItems;
	private int itemCount;
	private boolean cancelStore;

	public CM_PRIVATE_STORE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		/**
		 * Define who wants to create a private store
		 */
		activePlayer = getConnection().getActivePlayer();
		if (activePlayer == null) {
			return;
		}
		if (activePlayer.isInPrison()) {
			cancelStore = true;
			PacketSendUtility.sendMessage(activePlayer, "You can't open Private Shop in prison!");
			return;
		}

		/**
		 * Read the amount of items that need to be put into the player's store
		 */
		itemCount = readH();
		tradePSItems = new TradePSItem[itemCount];

		if (activePlayer.getMoveController().isInMove()) {
			PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.STR_PERSONAL_SHOP_DISABLED_IN_MOVING_OBJECT);
			cancelStore = true;
			return;
		}

		for (int i = 0; i < itemCount; i++) {
			int itemObjId = readD();
			int itemId = readD();
			int count = readH();
			long price = readD();
			readD();// unk 4.7
			Item item = activePlayer.getInventory().getItemByObjId(itemObjId);
			if ((price < 0 || item == null || item.getItemId() != itemId || item.getItemCount() < count) && !cancelStore) {
				PacketSendUtility.sendMessage(activePlayer, "Invalid item.");
				cancelStore = true;
			}
			else if (!item.isTradeable(activePlayer)) {
				PacketSendUtility.sendPacket(activePlayer, new SM_SYSTEM_MESSAGE(1300344, new DescriptionId(item.getNameId())));
				cancelStore = true;
			}

			tradePSItems[i] = new TradePSItem(itemObjId, itemId, count, price);
		}
	}

	@Override
	protected void runImpl() {
		if (activePlayer == null) {
			return;
		}
		if (activePlayer.getLifeStats().isAlreadyDead()) {
			return;
		}
		if (!cancelStore && itemCount > 0) {
			PrivateStoreService.addItems(activePlayer, tradePSItems);
		}
		else {
			PrivateStoreService.closePrivateStore(activePlayer);
		}
	}
}
