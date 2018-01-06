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
package com.aionemu.gameserver.taskmanager;

import java.util.Collection;

import javolution.util.FastList;

/**
 * @author NB4L1
 */
public abstract class FIFOSimpleExecutableQueue<T> extends FIFOExecutableQueue {

	private final FastList<T> queue = new FastList<T>();

	public final void execute(T t) {
		synchronized (queue) {
			queue.addLast(t);
		}

		execute();
	}

	public final void executeAll(Collection<T> c) {
		synchronized (queue) {
			queue.addAll(c);
		}

		execute();
	}

	public final void remove(T t) {
		synchronized (queue) {
			queue.remove(t);
		}
	}

	@Override
	protected final boolean isEmpty() {
		synchronized (queue) {
			return queue.isEmpty();
		}
	}

	protected final T removeFirst() {
		synchronized (queue) {
			return queue.removeFirst();
		}
	}

	@Override
	protected abstract void removeAndExecuteFirst();
}
