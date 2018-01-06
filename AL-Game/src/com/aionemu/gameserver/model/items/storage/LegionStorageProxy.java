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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemDeleteType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;

import javolution.util.FastList;

/**
 * @author ATracer
 */
public class LegionStorageProxy extends Storage {

	private final Player actor;
	private final Storage storage;

	public LegionStorageProxy(Storage storage, Player actor) {
		super(storage.getStorageType(), false);
		this.actor = actor;
		this.storage = storage;
	}

	@Override
	public void increaseKinah(long amount) {
		storage.increaseKinah(amount, actor);
	}

	@Override
	public void increaseKinah(long amount, ItemUpdateType updateType) {
		storage.increaseKinah(amount, updateType, actor);
	}

	@Override
	public boolean tryDecreaseKinah(long amount) {
		return storage.tryDecreaseKinah(amount, actor);
	}

	@Override
	public boolean tryDecreaseKinah(long amount, ItemUpdateType updateType) {
		return storage.tryDecreaseKinah(amount, updateType, actor);
	}

	@Override
	public void decreaseKinah(long amount) {
		storage.decreaseKinah(amount, actor);
	}

	@Override
	public void decreaseKinah(long amount, ItemUpdateType updateType) {
		storage.decreaseKinah(amount, updateType, actor);
	}

	@Override
	public long increaseItemCount(Item item, long count) {
		return storage.increaseItemCount(item, count, actor);
	}

	@Override
	public long increaseItemCount(Item item, long count, ItemUpdateType updateType) {
		return storage.increaseItemCount(item, count, updateType, actor);
	}

	@Override
	public long decreaseItemCount(Item item, long count) {
		return storage.decreaseItemCount(item, count, actor);
	}

	@Override
	public long decreaseItemCount(Item item, long count, ItemUpdateType updateType) {
		return storage.decreaseItemCount(item, count, updateType, actor);
	}

	@Override
	public long decreaseItemCount(Item item, long count, ItemUpdateType updateType, QuestStatus questStatus) {
		throw new UnsupportedOperationException("Quests should not update LWH!");
	}

	@Override
	public Item add(Item item) {
		return storage.add(item, actor);
	}

	@Override
	public Item add(Item item, ItemAddType addType) {
		return storage.add(item, addType, actor);
	}

	@Override
	public Item put(Item item) {
		return storage.put(item, actor);
	}

	@Override
	public Item delete(Item item) {
		return storage.delete(item, actor);
	}

	@Override
	public Item delete(Item item, ItemDeleteType deleteType) {
		return storage.delete(item, deleteType, actor);
	}

	@Override
	public boolean decreaseByItemId(int itemId, long count) {
		return storage.decreaseByItemId(itemId, count, actor);
	}

	@Override
	public boolean decreaseByItemId(int itemId, long count, QuestStatus questStatus) {
		throw new UnsupportedOperationException("Quests should not update LWH!");
	}

	@Override
	public boolean decreaseByObjectId(int itemObjId, long count) {
		return storage.decreaseByObjectId(itemObjId, count, actor);
	}

	@Override
	public boolean decreaseByObjectId(int itemObjId, long count, ItemUpdateType updateType) {
		return storage.decreaseByObjectId(itemObjId, count, updateType, actor);
	}

	@Override
	public boolean decreaseByObjectId(int itemObjId, long count, QuestStatus questStatus) {
		throw new UnsupportedOperationException("Quests should not update LWH!");
	}

	@Override
	public long getKinah() {
		return storage.getKinah();
	}

	@Override
	public Item getKinahItem() {
		return storage.getKinahItem();
	}

	@Override
	public StorageType getStorageType() {
		return storage.getStorageType();
	}

	@Override
	public void onLoadHandler(Item item) {
		storage.onLoadHandler(item);
	}

	@Override
	public Item remove(Item item) {
		return storage.remove(item);
	}

	@Override
	public Item getFirstItemByItemId(int itemId) {
		return storage.getFirstItemByItemId(itemId);
	}

	@Override
	public FastList<Item> getItemsWithKinah() {
		return storage.getItemsWithKinah();
	}

	@Override
	public List<Item> getItems() {
		return storage.getItems();
	}

	@Override
	public List<Item> getItemsByItemId(int itemId) {
		return storage.getItemsByItemId(itemId);
	}

	@Override
	public Queue<Item> getDeletedItems() {
		return storage.getDeletedItems();
	}

	@Override
	public Item getItemByObjId(int itemObjId) {
		return storage.getItemByObjId(itemObjId);
	}

	@Override
	public boolean isFull() {
		return storage.isFull();
	}

	@Override
	public int getFreeSlots() {
		return storage.getFreeSlots();
	}

	@Override
	public boolean setLimit(int limit) {
		return storage.setLimit(limit);
	}

	@Override
	public int getLimit() {
		return storage.getLimit();
	}

	@Override
	public int size() {
		return storage.size();
	}

	@Override
	public void setOwner(Player player) {
		throw new UnsupportedOperationException("LWH doesnt have owner");
	}
}
