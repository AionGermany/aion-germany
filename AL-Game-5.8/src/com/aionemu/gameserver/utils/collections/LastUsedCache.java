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
package com.aionemu.gameserver.utils.collections;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rolandas
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LastUsedCache<K extends Comparable, V> implements ICache<K, V>, Serializable {

	private static final long serialVersionUID = 3674312987828041877L;
	Map<K, Item> map = new ConcurrentHashMap<K, Item>();
	Item startItem = new Item();
	Item endItem = new Item();
	int maxSize;
	private final Object syncRoot = new Object();

	static class Item {

		public Item(Comparable k, Object v) {
			key = k;
			value = v;
		}

		public Item() {
		}

		public Comparable key;
		public Object value;
		public Item previous;
		public Item next;
	}

	void removeItem(Item item) {
		synchronized (syncRoot) {
			item.previous.next = item.next;
			item.next.previous = item.previous;
		}
	}

	void insertHead(Item item) {
		synchronized (syncRoot) {
			item.previous = startItem;
			item.next = startItem.next;
			startItem.next.previous = item;
			startItem.next = item;
		}
	}

	void moveToHead(Item item) {
		synchronized (syncRoot) {
			item.previous.next = item.next;
			item.next.previous = item.previous;
			item.previous = startItem;
			item.next = startItem.next;
			startItem.next.previous = item;
			startItem.next = item;
		}
	}

	public LastUsedCache(int maxObjects) {
		maxSize = maxObjects;
		startItem.next = endItem;
		endItem.previous = startItem;
	}

	@Override
	public CachePair[] getAll() {
		CachePair p[] = new CachePair[maxSize];
		int count = 0;

		synchronized (syncRoot) {
			Item cur = startItem.next;
			while (cur != endItem) {
				p[count] = new CachePair(cur.key, cur.value);
				count++;
				cur = cur.next;
			}
		}

		CachePair np[] = new CachePair[count];
		System.arraycopy(p, 0, np, 0, count);
		return np;
	}

	/**
	 * Gets a value by key. Returns null if not found
	 */
	@Override
	public V get(K key) {
		Item cur = map.get(key);
		if (cur == null) {
			return null;
		}

		if (cur != startItem.next) {
			moveToHead(cur);
		}
		return (V) cur.value;
	}

	/**
	 * Adds or renews a cache item pair
	 */
	@Override
	public void put(K key, V value) {
		Item cur = map.get(key);
		if (cur != null) {
			cur.value = value;
			moveToHead(cur);
			return;
		}

		if (map.size() >= maxSize && maxSize != 0) {
			cur = endItem.previous;
			map.remove(cur.key);
			removeItem(cur);
		}

		Item item = new Item(key, value);
		insertHead(item);
		map.put(key, item);
	}

	@Override
	public void remove(K key) {
		Item cur = map.get(key);
		if (cur == null) {
			return;
		}
		map.remove(key);
		removeItem(cur);
	}

	@Override
	public int size() {
		return map.size();
	}
}
