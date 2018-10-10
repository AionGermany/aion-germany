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

package com.aionemu.commons.network.util;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.concurrent.PriorityThreadFactory;
import com.aionemu.commons.utils.concurrent.RunnableWrapper;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author -Nemesiss-, Rolandas
 */
public class ThreadPoolManager implements Executor {

	/**
	 * PriorityThreadFactory creating new threads for ThreadPoolManager
	 */

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final ThreadPoolManager instance = new ThreadPoolManager();
	}

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(ThreadPoolManager.class);

	/**
	 * @return ThreadPoolManager instance.
	 */
	public static final ThreadPoolManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * STPE for normal scheduled tasks
	 */
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	private ListeningScheduledExecutorService scheduledThreadPool;
	/**
	 * TPE for execution of gameserver client packets
	 */
	private final ThreadPoolExecutor generalPacketsThreadPoolExecutor;
	private final ListeningExecutorService generalPacketsThreadPool;

	/**
	 * Constructor.
	 */
	private ThreadPoolManager() {
		new DeadLockDetector(60, DeadLockDetector.RESTART).start();

		scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4, new PriorityThreadFactory("ScheduledThreadPool", Thread.NORM_PRIORITY));
		scheduledThreadPool = MoreExecutors.listeningDecorator(scheduledThreadPoolExecutor);

		generalPacketsThreadPoolExecutor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		generalPacketsThreadPool = MoreExecutors.listeningDecorator(generalPacketsThreadPoolExecutor);
	}

	/**
	 * Executes Runnable - GameServer Client packet.
	 * 
	 * @param pkt
	 */
	@Override
	public void execute(final Runnable pkt) {
		generalPacketsThreadPool.execute(new RunnableWrapper(pkt));
	}

	/**
	 * @return the packetsThreadPool
	 */
	public ListeningExecutorService getPacketsThreadPool() {
		return generalPacketsThreadPool;
	}

	/**
	 * Schedule
	 * 
	 * @param <T>
	 * @param r
	 * @param delay
	 * @return ScheduledFuture
	 */

	@SuppressWarnings("unchecked")
	public <T extends Runnable> ListenableFuture<T> schedule(final T r, long delay) {
		try {
			if (delay < 0)
				delay = 0;
			return (ListenableFuture<T>) JdkFutureAdapters.listenInPoolThread(scheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS));
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}

	/**
	 * Schedule at fixed rate
	 * 
	 * @param <T>
	 * @param r
	 * @param initial
	 * @param delay
	 * @return ScheduledFuture
	 */
	@SuppressWarnings("unchecked")
	public <T extends Runnable> ListenableFuture<T> scheduleAtFixedRate(final T r, long initial, long delay) {
		try {
			if (delay < 0)
				delay = 0;
			if (initial < 0)
				initial = 0;
			return (ListenableFuture<T>) JdkFutureAdapters.listenInPoolThread(scheduledThreadPool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS));
		} catch (RejectedExecutionException e) {
			return null;
		}
	}

	/**
	 * Shutdown all thread pools.
	 */
	public void shutdown() {
		try {
			scheduledThreadPool.shutdown();
			generalPacketsThreadPool.shutdown();
			scheduledThreadPool.awaitTermination(2, TimeUnit.SECONDS);
			generalPacketsThreadPool.awaitTermination(2, TimeUnit.SECONDS);
			log.info("All ThreadPools are now stopped.");
		} catch (InterruptedException e) {
			log.error("Can't shutdown ThreadPoolManager", e);
		}
	}
}
