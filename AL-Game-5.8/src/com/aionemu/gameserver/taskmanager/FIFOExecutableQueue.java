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

import java.util.concurrent.locks.ReentrantLock;

import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author NB4L1
 */
public abstract class FIFOExecutableQueue implements Runnable {

	private static final byte NONE = 0;
	private static final byte QUEUED = 1;
	private static final byte RUNNING = 2;
	private final ReentrantLock lock = new ReentrantLock();
	private volatile byte state = NONE;

	protected final void execute() {
		lock();
		try {
			if (state != NONE) {
				return;
			}

			state = QUEUED;
		}
		finally {
			unlock();
		}

		ThreadPoolManager.getInstance().execute(this);
	}

	public final void lock() {
		lock.lock();
	}

	public final void unlock() {
		lock.unlock();
	}

	@Override
	public final void run() {
		try {
			while (!isEmpty()) {
				setState(QUEUED, RUNNING);

				try {
					while (!isEmpty()) {
						removeAndExecuteFirst();
					}
				}
				finally {
					setState(RUNNING, QUEUED);
				}
			}
		}
		finally {
			setState(QUEUED, NONE);
		}
	}

	private void setState(byte expected, byte value) {
		lock();
		try {
			if (state != expected) {
				throw new IllegalStateException("state: " + state + ", expected: " + expected);
			}
		}
		finally {
			state = value;

			unlock();
		}
	}

	protected abstract boolean isEmpty();

	protected abstract void removeAndExecuteFirst();
}
