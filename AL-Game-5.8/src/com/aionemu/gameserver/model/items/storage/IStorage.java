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
package com.aionemu.gameserver.model.items.storage;

import java.util.List;
import java.util.Queue;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemDeleteType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;

import javolution.util.FastList;

/**
 * Public interface for Storage, later will rename probably
 *
 * @author ATracer
 */
public interface IStorage {

	/**
	 * @param player
	 */
	void setOwner(Player player);

	/**
	 * @return current kinah count
	 */
	long getKinah();

	/**
	 * @return kinah item or null if storage never had kinah
	 */
	Item getKinahItem();

	/**
	 * @return
	 */
	StorageType getStorageType();

	/**
	 * @param amount
	 */
	void increaseKinah(long amount);

	/**
	 * @param amount
	 * @param updateType
	 */
	void increaseKinah(long amount, ItemUpdateType updateType);

	/**
	 * @param amount
	 * @return
	 */
	boolean tryDecreaseKinah(long amount);

	boolean tryDecreaseKinah(long amount, ItemUpdateType updateType);

	/**
	 * @param amount
	 */
	void decreaseKinah(long amount);

	/**
	 * @param amount
	 * @param updateType
	 */
	void decreaseKinah(long amount, ItemUpdateType updateType);

	/**
	 * @param item
	 * @param count
	 * @return
	 */
	long increaseItemCount(Item item, long count);

	/**
	 * @param item
	 * @param count
	 * @param updateType
	 * @return
	 */
	long increaseItemCount(Item item, long count, ItemUpdateType updateType);

	/**
	 * @param item
	 * @param count
	 * @return
	 */
	long decreaseItemCount(Item item, long count);

	/**
	 * @param item
	 * @param count
	 * @param updateType
	 * @return
	 */
	long decreaseItemCount(Item item, long count, ItemUpdateType updateType);

	long decreaseItemCount(Item item, long count, ItemUpdateType updateType, QuestStatus questStatus);

	/**
	 * Add operation should be used for new items incoming into storage from outside
	 */
	Item add(Item item);

	Item add(Item item, ItemAddType addType);

	/**
	 * Put operation is used in some operations like unequip
	 */
	Item put(Item item);

	/**
	 * @param item
	 * @return
	 */
	Item remove(Item item);

	/**
	 * @param item
	 * @return
	 */
	Item delete(Item item);

	/**
	 * @param item
	 * @param deleteType
	 * @return
	 */
	Item delete(Item item, ItemDeleteType deleteType);

	/**
	 * @param itemId
	 * @param count
	 * @return
	 */
	boolean decreaseByItemId(int itemId, long count);

	boolean decreaseByItemId(int itemId, long count, QuestStatus questStatus);

	/**
	 * @param itemObjId
	 * @param count
	 * @return
	 */
	boolean decreaseByObjectId(int itemObjId, long count);

	/**
	 * @param itemObjId
	 * @param count
	 * @param updateType
	 * @return
	 */
	boolean decreaseByObjectId(int itemObjId, long count, ItemUpdateType updateType);

	boolean decreaseByObjectId(int itemObjId, long count, QuestStatus questStatus);

	/**
	 * @param itemId
	 * @return
	 */
	Item getFirstItemByItemId(int itemId);

	/**
	 * @return
	 */
	FastList<Item> getItemsWithKinah();

	/**
	 * @return
	 */
	List<Item> getItems();

	/**
	 * @param itemId
	 * @return
	 */
	List<Item> getItemsByItemId(int itemId);

	/**
	 * @param itemObjId
	 * @return
	 */
	Item getItemByObjId(int itemObjId);

	/**
	 * @param itemId
	 * @return
	 */
	long getItemCountByItemId(int itemId);

	/**
	 * @return
	 */
	boolean isFull();

	/**
	 * @return
	 */
	int getFreeSlots();

	/**
	 * @return
	 */
	int getLimit();

	int getRowLength();

	/**
	 * @return
	 */
	int size();

	/**
	 * @return
	 */
	PersistentState getPersistentState();

	/**
	 * @param persistentState
	 */
	void setPersistentState(PersistentState persistentState);

	/**
	 * @return
	 */
	Queue<Item> getDeletedItems();

	/**
	 * @param item
	 */
	void onLoadHandler(Item item);
}
