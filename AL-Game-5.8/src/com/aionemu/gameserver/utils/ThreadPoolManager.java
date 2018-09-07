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
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadUncaughtExceptionHandler;
import com.aionemu.commons.utils.concurrent.AionRejectedExecutionHandler;
import com.aionemu.commons.utils.concurrent.PriorityThreadFactory;
import com.aionemu.commons.utils.concurrent.RunnableWrapper;
import com.aionemu.commons.utils.internal.chmv8.ForkJoinPool;
import com.aionemu.gameserver.configs.main.ThreadConfig;

/**
 * @author -Nemesiss-, NB4L1, MrPoke, lord_rex
 */
public final class ThreadPoolManager {

	private static final Logger log = LoggerFactory.getLogger(ThreadPoolManager.class);
	public static final long MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING = 5000;
	private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;
	private final ScheduledThreadPoolExecutor scheduledPool;
	private final ThreadPoolExecutor instantPool;
	private final ThreadPoolExecutor longRunningPool;
	private final ForkJoinPool workStealingPool;

	private ThreadPoolManager() {
		final int instantPoolSize = Math.max(1, ThreadConfig.BASE_THREAD_POOL_SIZE) * Runtime.getRuntime().availableProcessors();

		instantPool = new ThreadPoolExecutor(instantPoolSize, instantPoolSize, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100000), new PriorityThreadFactory("InstantPool", ThreadConfig.USE_PRIORITIES ? 7 : Thread.NORM_PRIORITY));
		instantPool.setRejectedExecutionHandler(new AionRejectedExecutionHandler());
		instantPool.prestartAllCoreThreads();

		scheduledPool = new ScheduledThreadPoolExecutor(Math.max(1, ThreadConfig.EXTRA_THREAD_PER_CORE) * Runtime.getRuntime().availableProcessors());
		scheduledPool.setRejectedExecutionHandler(new AionRejectedExecutionHandler());
		scheduledPool.prestartAllCoreThreads();

		longRunningPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		longRunningPool.setRejectedExecutionHandler(new AionRejectedExecutionHandler());
		longRunningPool.prestartAllCoreThreads();

		WorkStealThreadFactory forkJoinThreadFactory = new WorkStealThreadFactory("ForkJoinPool");
		workStealingPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), forkJoinThreadFactory, new ThreadUncaughtExceptionHandler(), true);
		forkJoinThreadFactory.setDefaultPool(workStealingPool);

		Thread maintainThread = new Thread(new Runnable() {

			@Override
			public void run() {
				purge();
			}
		}, "ThreadPool Purge Task");

		maintainThread.setDaemon(true);
		scheduleAtFixedRate(maintainThread, 150000, 150000);

		log.info("ThreadPoolManager: Initialized with " + scheduledPool.getPoolSize() + " scheduler, " + instantPool.getPoolSize() + " instant, " + longRunningPool.getPoolSize() + " long running, and forking " + workStealingPool.getPoolSize() + " thread(s).");
	}

	private long validate(long delay) {
		return Math.max(0, Math.min(MAX_DELAY, delay));
	}

	private static final class ThreadPoolRunnableWrapper extends RunnableWrapper {

		private ThreadPoolRunnableWrapper(Runnable runnable) {
			super(runnable, ThreadConfig.MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING);
		}
	}

	public final ScheduledFuture<?> schedule(Runnable r, long delay) {
		r = new ThreadPoolRunnableWrapper(r);
		delay = validate(delay);
		return scheduledPool.schedule(r, delay, TimeUnit.MILLISECONDS);
	}

	public final ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long delay, long period) {
		r = new ThreadPoolRunnableWrapper(r);
		delay = validate(delay);
		period = validate(period);
		return scheduledPool.scheduleAtFixedRate(r, delay, period, TimeUnit.MILLISECONDS);
	}

	public ForkJoinPool getForkingPool() {
		return workStealingPool;
	}

	public final void execute(Runnable r) {
		r = new ThreadPoolRunnableWrapper(r);
		instantPool.execute(r);
	}

	public final void executeLongRunning(Runnable r) {
		r = new RunnableWrapper(r);

		longRunningPool.execute(r);
	}

	public final Future<?> submit(Runnable r) {
		r = new ThreadPoolRunnableWrapper(r);

		return instantPool.submit(r);
	}

	public final Future<?> submitLongRunning(Runnable r) {
		r = new RunnableWrapper(r);

		return longRunningPool.submit(r);
	}

	/**
	 * Executes a loginServer packet task
	 *
	 * @param pkt
	 *            runnable packet for Login Server
	 */
	public void executeLsPacket(Runnable pkt) {
		execute(pkt);
	}

	public void purge() {
		scheduledPool.purge();
		instantPool.purge();
		longRunningPool.purge();
		// workStealingPool is already maintaining needed threads
	}

	/**
	 * Shutdown all thread pools.
	 */
	public void shutdown() {
		final long begin = System.currentTimeMillis();

		log.info("ThreadPoolManager: Shutting down.");
		log.info("\t... executing " + getTaskCount(scheduledPool) + " scheduled tasks.");
		log.info("\t... executing " + getTaskCount(instantPool) + " instant tasks.");
		log.info("\t... executing " + getTaskCount(longRunningPool) + " long running tasks.");
		log.info("\t... " + (workStealingPool.getQueuedTaskCount() + workStealingPool.getQueuedSubmissionCount()) + " forking tasks left.");

		scheduledPool.shutdown();
		instantPool.shutdown();
		longRunningPool.shutdown();
		workStealingPool.shutdown();

		boolean success = false;
		try {
			success |= awaitTermination(5000);

			scheduledPool.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			scheduledPool.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);

			success |= awaitTermination(10000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		log.info("\t... success: " + success + " in " + (System.currentTimeMillis() - begin) + " msec.");
		log.info("\t... " + getTaskCount(scheduledPool) + " scheduled tasks left.");
		log.info("\t... " + getTaskCount(instantPool) + " instant tasks left.");
		log.info("\t... " + getTaskCount(longRunningPool) + " long running tasks left.");
		log.info("\t... " + (workStealingPool.getQueuedTaskCount() + workStealingPool.getQueuedSubmissionCount()) + " forking tasks left.");
		workStealingPool.shutdownNow();
	}

	private int getTaskCount(ThreadPoolExecutor tp) {
		return tp.getQueue().size() + tp.getActiveCount();
	}

	public List<String> getStats() {
		List<String> list = new ArrayList<String>();

		list.add("");
		list.add("Scheduled pool:");
		list.add("=================================================");
		list.add("\tgetActiveCount: ...... " + scheduledPool.getActiveCount());
		list.add("\tgetCorePoolSize: ..... " + scheduledPool.getCorePoolSize());
		list.add("\tgetPoolSize: ......... " + scheduledPool.getPoolSize());
		list.add("\tgetLargestPoolSize: .. " + scheduledPool.getLargestPoolSize());
		list.add("\tgetMaximumPoolSize: .. " + scheduledPool.getMaximumPoolSize());
		list.add("\tgetCompletedTaskCount: " + scheduledPool.getCompletedTaskCount());
		list.add("\tgetQueuedTaskCount: .. " + scheduledPool.getQueue().size());
		list.add("\tgetTaskCount: ........ " + scheduledPool.getTaskCount());
		list.add("");
		list.add("Instant pool:");
		list.add("=================================================");
		list.add("\tgetActiveCount: ...... " + instantPool.getActiveCount());
		list.add("\tgetCorePoolSize: ..... " + instantPool.getCorePoolSize());
		list.add("\tgetPoolSize: ......... " + instantPool.getPoolSize());
		list.add("\tgetLargestPoolSize: .. " + instantPool.getLargestPoolSize());
		list.add("\tgetMaximumPoolSize: .. " + instantPool.getMaximumPoolSize());
		list.add("\tgetCompletedTaskCount: " + instantPool.getCompletedTaskCount());
		list.add("\tgetQueuedTaskCount: .. " + instantPool.getQueue().size());
		list.add("\tgetTaskCount: ........ " + instantPool.getTaskCount());
		list.add("");
		list.add("Long running pool:");
		list.add("=================================================");
		list.add("\tgetActiveCount: ...... " + longRunningPool.getActiveCount());
		list.add("\tgetCorePoolSize: ..... " + longRunningPool.getCorePoolSize());
		list.add("\tgetPoolSize: ......... " + longRunningPool.getPoolSize());
		list.add("\tgetLargestPoolSize: .. " + longRunningPool.getLargestPoolSize());
		list.add("\tgetMaximumPoolSize: .. " + longRunningPool.getMaximumPoolSize());
		list.add("\tgetCompletedTaskCount: " + longRunningPool.getCompletedTaskCount());
		list.add("\tgetQueuedTaskCount: .. " + longRunningPool.getQueue().size());
		list.add("\tgetTaskCount: ........ " + longRunningPool.getTaskCount());
		list.add("");
		list.add("Work forking pool:");
		list.add("=================================================");
		list.add("\tgetActiveCount: ...... " + workStealingPool.getActiveThreadCount());
		list.add("\tgetPoolSize: ......... " + workStealingPool.getPoolSize());
		list.add("\tgetStealCount: ........" + workStealingPool.getStealCount());
		list.add("\tgetQueuedTaskCount: .. " + workStealingPool.getQueuedTaskCount());
		list.add("\tgetRunningThreadCount: " + workStealingPool.getRunningThreadCount());

		return list;
	}

	private boolean awaitTermination(long timeoutInMillisec) throws InterruptedException {
		final long begin = System.currentTimeMillis();

		while (System.currentTimeMillis() - begin < timeoutInMillisec) {
			if (!scheduledPool.awaitTermination(10, TimeUnit.MILLISECONDS) && scheduledPool.getActiveCount() > 0) {
				continue;
			}

			if (!instantPool.awaitTermination(10, TimeUnit.MILLISECONDS) && instantPool.getActiveCount() > 0) {
				continue;
			}

			if (!workStealingPool.awaitTermination(10, TimeUnit.MILLISECONDS) && workStealingPool.getActiveThreadCount() > 0) {
				continue;
			}

			if (!longRunningPool.awaitTermination(10, TimeUnit.MILLISECONDS) && longRunningPool.getActiveCount() > 0) {
				continue;
			}

			return true;
		}

		return false;
	}

	private static final class SingletonHolder {

		private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
	}

	public static ThreadPoolManager getInstance() {
		return SingletonHolder.INSTANCE;
	}
}
