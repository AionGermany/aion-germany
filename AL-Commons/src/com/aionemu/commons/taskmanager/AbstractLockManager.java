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

package com.aionemu.commons.taskmanager;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lord_rex and MrPoke
 */
public abstract class AbstractLockManager {

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
	private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock(); // Later
																				// could
																				// be
																				// used.

	public final void writeLock() {
		writeLock.lock();
	}

	public final void writeUnlock() {
		writeLock.unlock();
	}

	public final void readLock() {
		readLock.lock();
	}

	public final void readUnlock() {
		readLock.unlock();
	}
}
