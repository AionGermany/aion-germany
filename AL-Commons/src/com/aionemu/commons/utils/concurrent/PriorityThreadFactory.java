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

package com.aionemu.commons.utils.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.aionemu.commons.network.util.ThreadUncaughtExceptionHandler;

/**
 * @author -Nemesiss-
 */
public class PriorityThreadFactory implements ThreadFactory {

	/**
	 * Priority of new threads
	 */
	private int prio;
	/**
	 * Thread group name
	 */
	private String name;

	/*
	 * Default pool for the thread group, can be null for default
	 */
	private ExecutorService threadPool;

	/**
	 * Number of created threads
	 */
	private AtomicInteger threadNumber = new AtomicInteger(1);
	/**
	 * ThreadGroup for created threads
	 */
	private ThreadGroup group;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param prio
	 */
	public PriorityThreadFactory(final String name, final int prio) {
		this.prio = prio;
		this.name = name;
		group = new ThreadGroup(this.name);
	}

	public PriorityThreadFactory(final String name, ExecutorService defaultPool) {
		this(name, Thread.NORM_PRIORITY);
		setDefaultPool(defaultPool);
	}

	protected void setDefaultPool(ExecutorService pool) {
		threadPool = pool;
	}

	protected ExecutorService getDefaultPool() {
		return threadPool;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Thread newThread(final Runnable r) {
		Thread t = new Thread(group, r);
		t.setName(name + "-" + threadNumber.getAndIncrement());
		t.setPriority(prio);
		t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
		return t;
	}
}
