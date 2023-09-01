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
package com.aionemu.gameserver.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.commons.utils.Rnd;

/**
 * Created with IntelliJ IDEA. User: pixfid Date: 7/19/13 Time: 7:44 AM
 */
public class RndSelector<E> {

	private class RndNode<T> implements Comparable<RndNode<T>> {

		private final T value;
		private final int weight;

		public RndNode(T value, int weight) {
			this.value = value;
			this.weight = weight;
		}

		@Override
		public int compareTo(RndNode<T> o) {
			return this.weight - weight;
		}
	}

	private int totalWeight = 0;
	private final List<RndNode<E>> nodes;

	public RndSelector() {
		nodes = new ArrayList<RndNode<E>>();
	}

	public RndSelector(int initialCapacity) {
		nodes = new ArrayList<RndNode<E>>(initialCapacity);
	}

	public void add(E value, int weight) {
		if (value == null || weight <= 0) {
			return;
		}
		totalWeight += weight;
		nodes.add(new RndNode<E>(value, weight));
	}

	public E chance(int maxWeight) {
		if (maxWeight <= 0) {
			return null;
		}

		Collections.sort(nodes);

		int r = Rnd.get(maxWeight);
		int weight = 0;
		for (int i = 0; i < nodes.size(); i++) {
			if ((weight += nodes.get(i).weight) > r) {
				return nodes.get(i).value;
			}
		}
		return null;
	}

	public E chance() {
		return chance(100);
	}

	public E select() {
		return chance(totalWeight);
	}

	public void clear() {
		totalWeight = 0;
		nodes.clear();
	}
}
