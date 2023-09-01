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
package com.aionemu.gameserver.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.trade.Exchange;
import com.aionemu.gameserver.model.trade.ExchangeItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EXCHANGE_ADD_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EXCHANGE_ADD_KINAH;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EXCHANGE_CONFIRMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EXCHANGE_REQUEST;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.item.ItemFactory;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.taskmanager.AbstractFIFOPeriodicTaskManager;
import com.aionemu.gameserver.taskmanager.tasks.TemporaryTradeTimeTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author ATracer
 */
public class ExchangeService {

	private static final Logger log = LoggerFactory.getLogger("EXCHANGE_LOG");
	private Map<Integer, Exchange> exchanges = new HashMap<Integer, Exchange>();
	private ExchangePeriodicTaskManager saveManager;

	public static final ExchangeService getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Default constructor
	 */
	private ExchangeService() {
		int DELAY_EXCHANGE_SAVE = 5000;
		saveManager = new ExchangePeriodicTaskManager(DELAY_EXCHANGE_SAVE);
	}

	/**
	 * @param objectId
	 * @param objectId2
	 */
	public void registerExchange(Player player1, Player player2) {
		if (!validateParticipants(player1, player2)) {
			return;
		}

		player1.setTrading(true);
		player2.setTrading(true);

		exchanges.put(player1.getObjectId(), new Exchange(player1, player2));
		exchanges.put(player2.getObjectId(), new Exchange(player2, player1));

		PacketSendUtility.sendPacket(player2, new SM_EXCHANGE_REQUEST(player1.getName()));
		PacketSendUtility.sendPacket(player1, new SM_EXCHANGE_REQUEST(player2.getName()));
	}

	/**
	 * @param player1
	 * @param player2
	 */
	private boolean validateParticipants(Player player1, Player player2) {
		return RestrictionsManager.canTrade(player1) && RestrictionsManager.canTrade(player2);
	}

	private Player getCurrentParter(Player player) {
		Exchange exchange = exchanges.get(player.getObjectId());
		return exchange != null ? exchange.getTargetPlayer() : null;
	}

	/**
	 * @param player
	 * @return Exchange
	 */
	private Exchange getCurrentExchange(Player player) {
		return exchanges.get(player.getObjectId());
	}

	/**
	 * @param player
	 * @return Exchange
	 */
	public Exchange getCurrentParnterExchange(Player player) {
		Player partner = getCurrentParter(player);
		return partner != null ? getCurrentExchange(partner) : null;
	}

	/**
	 * @param player
	 */
	public boolean isPlayerInExchange(Player player) {
		return getCurrentExchange(player) != null;
	}

	/**
	 * @param activePlayer
	 * @param itemCount
	 */
	public void addKinah(Player activePlayer, long itemCount) {
		Exchange currentExchange = getCurrentExchange(activePlayer);
		if (currentExchange == null || currentExchange.isLocked()) {
			return;
		}

		if (itemCount < 1) {
			return;
		}

		// count total amount in inventory
		long availableCount = activePlayer.getInventory().getKinah();

		// count amount that was already added to exchange
		availableCount -= currentExchange.getKinahCount();

		long countToAdd = availableCount > itemCount ? itemCount : availableCount;

		if (countToAdd > 0) {
			Player partner = getCurrentParter(activePlayer);
			PacketSendUtility.sendPacket(activePlayer, new SM_EXCHANGE_ADD_KINAH(countToAdd, 0));
			PacketSendUtility.sendPacket(partner, new SM_EXCHANGE_ADD_KINAH(countToAdd, 1));
			currentExchange.addKinah(countToAdd);
			if (LoggingConfig.LOG_PLAYER_EXCHANGE) {
				log.info("[PLAYER EXCHANGE] > [Player: " + activePlayer.getName() + "] exchanged [Item: 182400001" + (LoggingConfig.ENABLE_ADVANCED_LOGGING ? "] [Item Name: Kinah]" : "]") + " [Count: " + countToAdd + "] with [Partner: " + partner.getName() + "]");
			}
		}
	}

	/**
	 * @param activePlayer
	 * @param itemObjId
	 * @param itemCount
	 */
	public void addItem(Player activePlayer, int itemObjId, long itemCount) {
		Item item = activePlayer.getInventory().getItemByObjId(itemObjId);
		if (item == null) {
			return;
		}

		Player partner = getCurrentParter(activePlayer);
		if (partner == null) {
			return;
		}
		if (!TemporaryTradeTimeTask.getInstance().canTrade(item, partner.getObjectId())) {
			if (!item.isTradeable(activePlayer)) {
				return;
			}
			if (!item.isTradeable(activePlayer) && (item.getPackCount() <= item.getItemTemplate().getPackCount() && !item.isPacked())) {
				return;
			}
		}

		if (itemCount < 1) {
			return;
		}

		if (itemCount > item.getItemCount()) {
			return;
		}

		Exchange currentExchange = getCurrentExchange(activePlayer);

		if (currentExchange == null) {
			return;
		}

		if (currentExchange.isLocked()) {
			return;
		}

		if (currentExchange.isExchangeListFull()) {
			return;
		}

		if (!AdminService.getInstance().canOperate(activePlayer, partner, item, "trade")) {
			return;
		}

		ExchangeItem exchangeItem = currentExchange.getItems().get(item.getObjectId());

		long actuallAddCount = 0;
		// item was not added previosly
		if (exchangeItem == null) {
			Item newItem = null;
			if (itemCount < item.getItemCount()) {
				newItem = ItemFactory.newItem(item.getItemId(), itemCount);
			}
			else {
				newItem = item;
			}
			exchangeItem = new ExchangeItem(itemObjId, itemCount, newItem);
			currentExchange.addItem(itemObjId, exchangeItem);
			actuallAddCount = itemCount;
		} // item was already added
		else {
			// if player add item count that is more than possible
			// happens with exploits
			if (item.getItemCount() == exchangeItem.getItemCount()) {
				return;
			}

			long possibleToAdd = item.getItemCount() - exchangeItem.getItemCount();
			actuallAddCount = itemCount > possibleToAdd ? possibleToAdd : itemCount;
			exchangeItem.addCount(actuallAddCount);
		}

		PacketSendUtility.sendPacket(activePlayer, new SM_EXCHANGE_ADD_ITEM(0, exchangeItem.getItem(), activePlayer));
		PacketSendUtility.sendPacket(partner, new SM_EXCHANGE_ADD_ITEM(1, exchangeItem.getItem(), partner));

		Item exchangedItem = exchangeItem.getItem();

		if (LoggingConfig.LOG_PLAYER_EXCHANGE) {
			log.info("[PLAYER EXCHANGE] > [Player: " + activePlayer.getName() + "] exchanged [Item: " + exchangedItem.getItemId() + (LoggingConfig.ENABLE_ADVANCED_LOGGING ? "] [Item Name: " + exchangedItem.getItemName() + "]" : "]") + " [Count: " + exchangeItem.getItemCount() + " with [Partner: " + partner.getName() + "]");
		}
	}

	/**
	 * @param activePlayer
	 */
	public void lockExchange(Player activePlayer) {
		Exchange exchange = getCurrentExchange(activePlayer);
		if (exchange != null) {
			exchange.lock();
			Player currentParter = getCurrentParter(activePlayer);
			PacketSendUtility.sendPacket(currentParter, new SM_EXCHANGE_CONFIRMATION(3));
		}
	}

	/**
	 * @param activePlayer
	 */
	public void cancelExchange(Player activePlayer) {
		Player currentParter = getCurrentParter(activePlayer);
		cleanupExchanges(activePlayer, currentParter);
		if (currentParter != null) {
			PacketSendUtility.sendPacket(currentParter, new SM_EXCHANGE_CONFIRMATION(1));
		}
	}

	/**
	 * @param activePlayer
	 */
	public void confirmExchange(Player activePlayer) {
		if (activePlayer == null || !activePlayer.isOnline()) {
			return;
		}

		Exchange currentExchange = getCurrentExchange(activePlayer);

		// TODO: Why is exchange null =/
		if (currentExchange == null) {
			return;
		}
		currentExchange.confirm();

		Player currentPartner = getCurrentParter(activePlayer);
		PacketSendUtility.sendPacket(currentPartner, new SM_EXCHANGE_CONFIRMATION(2));

		if (getCurrentExchange(currentPartner).isConfirmed()) {
			performTrade(activePlayer, currentPartner);
		}
	}

	/**
	 * @param activePlayer
	 * @param currentPartner
	 */
	private void performTrade(Player activePlayer, Player currentPartner) {
		// TODO message here
		// TODO release item id if return
		if (!validateExchange(activePlayer, currentPartner)) {
			cleanupExchanges(activePlayer, currentPartner);
			return;
		}

		Exchange exchange1 = getCurrentExchange(activePlayer);
		Exchange exchange2 = getCurrentExchange(currentPartner);

		cleanupExchanges(activePlayer, currentPartner);

		if (!removeItemsFromInventory(activePlayer, exchange1) || !removeItemsFromInventory(currentPartner, exchange2)) {
			AuditLogger.info(activePlayer, "Exchange kinah exploit partner: " + currentPartner.getName());
			return;
		}

		PacketSendUtility.sendPacket(activePlayer, new SM_EXCHANGE_CONFIRMATION(0));
		PacketSendUtility.sendPacket(currentPartner, new SM_EXCHANGE_CONFIRMATION(0));

		putItemToInventory(currentPartner, exchange1, exchange2);
		putItemToInventory(activePlayer, exchange2, exchange1);

		saveManager.add(new ExchangeOpSaveTask(exchange1.getActiveplayer().getObjectId(), exchange2.getActiveplayer().getObjectId(), exchange1.getItemsToUpdate(), exchange2.getItemsToUpdate()));
	}

	/**
	 * @param activePlayer
	 * @param currentPartner
	 */
	private void cleanupExchanges(Player activePlayer, Player currentPartner) {
		if (activePlayer != null) {
			exchanges.remove(activePlayer.getObjectId());
			activePlayer.setTrading(false);
		}

		if (currentPartner != null) {
			exchanges.remove(currentPartner.getObjectId());
			currentPartner.setTrading(false);
		}
	}

	/**
	 * @param player
	 * @param exchange
	 */
	private boolean removeItemsFromInventory(Player player, Exchange exchange) {
		Storage inventory = player.getInventory();

		for (ExchangeItem exchangeItem : exchange.getItems().values()) {
			Item item = exchangeItem.getItem();
			Item itemInInventory = inventory.getItemByObjId(exchangeItem.getItemObjId());
			if (itemInInventory == null) {
				AuditLogger.info(player, "Try to trade unexisting item.");
				return false;
			}

			long itemCount = exchangeItem.getItemCount();

			if (itemCount < itemInInventory.getItemCount()) {
				inventory.decreaseItemCount(itemInInventory, itemCount);
				exchange.addItemToUpdate(itemInInventory);
			}
			else {
				// remove from source inventory only
				inventory.remove(itemInInventory);
				exchangeItem.setItem(itemInInventory);
				// release when only part stack was added in the beginning -> full stack in the end
				if (item.getObjectId() != exchangeItem.getItemObjId()) {
					ItemService.releaseItemId(item);
				}
				PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(itemInInventory.getObjectId()));
			}
		}
		if (!player.getInventory().tryDecreaseKinah(exchange.getKinahCount())) {
			return false;
		}
		exchange.addItemToUpdate(player.getInventory().getKinahItem());
		return true;
	}

	/**
	 * @param activePlayer
	 * @param currentPartner
	 * @return
	 */
	private boolean validateExchange(Player activePlayer, Player currentPartner) {
		Exchange exchange1 = getCurrentExchange(activePlayer);
		Exchange exchange2 = getCurrentExchange(currentPartner);

		return validateInventorySize(activePlayer, exchange2) && validateInventorySize(currentPartner, exchange1);
	}

	private boolean validateInventorySize(Player activePlayer, Exchange exchange) {
		int numberOfFreeSlots = activePlayer.getInventory().getFreeSlots();
		return numberOfFreeSlots >= exchange.getItems().size();
	}

	/**
	 * @param player
	 * @param exchange1
	 * @param exchange2
	 */
	private void putItemToInventory(Player player, Exchange exchange1, Exchange exchange2) {
		for (ExchangeItem exchangeItem : exchange1.getItems().values()) {
			Item itemToPut = exchangeItem.getItem();
			itemToPut.setEquipmentSlot(0);
			player.getInventory().add(itemToPut);
			exchange2.addItemToUpdate(itemToPut);
		}
		long kinahToExchange = exchange1.getKinahCount();
		if (kinahToExchange > 0) {
			player.getInventory().increaseKinah(exchange1.getKinahCount());
			exchange2.addItemToUpdate(player.getInventory().getKinahItem());
		}
	}

	/**
	 * Frequent running save task
	 */
	public static final class ExchangePeriodicTaskManager extends AbstractFIFOPeriodicTaskManager<ExchangeOpSaveTask> {

		private static final String CALLED_METHOD_NAME = "exchangeOperation()";

		/**
		 * @param period
		 */
		public ExchangePeriodicTaskManager(int period) {
			super(period);
		}

		@Override
		protected void callTask(ExchangeOpSaveTask task) {
			task.run();
		}

		@Override
		protected String getCalledMethodName() {
			return CALLED_METHOD_NAME;
		}
	}

	/**
	 * This class is used for storing all items in one shot after any exchange operation
	 */
	public static final class ExchangeOpSaveTask implements Runnable {

		private int player1Id;
		private int player2Id;
		private List<Item> player1Items;
		private List<Item> player2Items;

		/**
		 * @param player1Id
		 * @param player2Id
		 * @param player1Items
		 * @param player2Items
		 */
		public ExchangeOpSaveTask(int player1Id, int player2Id, List<Item> player1Items, List<Item> player2Items) {
			this.player1Id = player1Id;
			this.player2Id = player2Id;
			this.player1Items = player1Items;
			this.player2Items = player2Items;
		}

		@Override
		public void run() {
			DAOManager.getDAO(InventoryDAO.class).store(player1Items, player1Id);
			DAOManager.getDAO(InventoryDAO.class).store(player2Items, player2Id);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final ExchangeService instance = new ExchangeService();
	}
}
