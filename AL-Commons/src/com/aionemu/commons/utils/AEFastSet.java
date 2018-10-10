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

package com.aionemu.commons.utils;

import java.util.Iterator;
import java.util.Set;

import javolution.util.FastCollection.Record;
import javolution.util.FastMap;

/**
 * @author NB4L1
 */
@SuppressWarnings("unchecked")
public class AEFastSet<E> extends AEFastCollection<E> implements Set<E> {

	private static final Object NULL = new Object();

	private final FastMap<E, Object> map;

	public AEFastSet() {
		map = new FastMap<E, Object>();
	}

	public AEFastSet(int capacity) {
		map = new FastMap<E, Object>(capacity);
	}

	public AEFastSet(Set<? extends E> elements) {
		map = new FastMap<E, Object>(elements.size());

		addAll(elements);
	}

	/*
	 * public AEFastSet<E> setShared(boolean isShared) {
	 * map.setShared(isShared); return this; }
	 */

	public boolean isShared() {
		return map.isShared();
	}

	@Override
	public Record head() {
		return map.head();
	}

	@Override
	public Record tail() {
		return map.tail();
	}

	@Override
	public E valueOf(Record record) {
		return ((FastMap.Entry<E, Object>) record).getKey();
	}

	@Override
	public void delete(Record record) {
		map.remove(((FastMap.Entry<E, Object>) record).getKey());
	}

	@Override
	public void delete(Record record, E value) {
		map.remove(value);
	}

	@Override
	public boolean add(E value) {
		return map.put(value, NULL) == null;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

	@Override
	public boolean remove(Object o) {
		return map.remove(o) != null;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public String toString() {
		return super.toString() + "-" + map.keySet().toString();
	}
}
