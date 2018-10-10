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

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.ExitCode;

/**
 * @author -Nemesiss-, ATracer
 */
public class DeadLockDetector extends Thread {

	private static final Logger log = LoggerFactory.getLogger(DeadLockDetector.class);
	/** What should we do on DeadLock */
	public static final byte NOTHING = 0;
	/** What should we do on DeadLock */
	public static final byte RESTART = 1;

	/** how often check for deadlocks */
	private final int sleepTime;
	/**
	 * ThreadMXBean
	 */
	private final ThreadMXBean tmx;
	/** What should we do on DeadLock */
	private final byte doWhenDL;

	/**
	 * Create new DeadLockDetector with given values.
	 * 
	 * @param sleepTime
	 * @param doWhenDL
	 */
	public DeadLockDetector(final int sleepTime, final byte doWhenDL) {
		super("DeadLockDetector");
		this.sleepTime = sleepTime * 1000;
		this.tmx = ManagementFactory.getThreadMXBean();
		this.doWhenDL = doWhenDL;
	}

	/**
	 * Check if there is a DeadLock.
	 */
	@Override
	public final void run() {
		boolean deadlock = false;
		while (!deadlock)
			try {
				long[] ids = tmx.findDeadlockedThreads();

				if (ids != null) {
					/** deadlock found :/ */
					deadlock = true;
					ThreadInfo[] tis = tmx.getThreadInfo(ids, true, true);
					String info = "DeadLock Found!\n";
					for (ThreadInfo ti : tis)
						info += ti.toString();

					for (ThreadInfo ti : tis) {
						LockInfo[] locks = ti.getLockedSynchronizers();
						MonitorInfo[] monitors = ti.getLockedMonitors();
						if (locks.length == 0 && monitors.length == 0)
							/** this thread is deadlocked but its not guilty */
							continue;

						ThreadInfo dl = ti;
						info += "Java-level deadlock:\n";
						info += createShortLockInfo(dl);
						while ((dl = tmx.getThreadInfo(new long[] { dl.getLockOwnerId() }, true, true)[0])
								.getThreadId() != ti.getThreadId())
							info += createShortLockInfo(dl);

						info += "\nDumping all threads:\n";
						for (ThreadInfo dumpedTI : tmx.dumpAllThreads(true, true)) {
							info += printDumpedThreadInfo(dumpedTI);
						}
					}
					log.warn(info);

					if (doWhenDL == RESTART)
						System.exit(ExitCode.CODE_RESTART);
				}
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				log.warn("DeadLockDetector: " + e, e);
			}
	}

	/**
	 * Example:
	 * <p>
	 * Java-level deadlock:<br>
	 * Thread-0 is waiting to lock java.lang.Object@276af2 which is held by
	 * main. Locked synchronizers:0 monitors:1<br>
	 * main is waiting to lock java.lang.Object@fa3ac1 which is held by
	 * Thread-0. Locked synchronizers:0 monitors:1<br>
	 * </p>
	 */
	private String createShortLockInfo(ThreadInfo threadInfo) {
		StringBuilder sb = new StringBuilder("\t");
		sb.append(threadInfo.getThreadName());
		sb.append(" is waiting to lock ");
		sb.append(threadInfo.getLockInfo().toString());
		sb.append(" which is held by ");
		sb.append(threadInfo.getLockOwnerName());
		sb.append(". Locked synchronizers:");
		sb.append(threadInfo.getLockedSynchronizers().length);
		sb.append(" monitors:");
		sb.append(threadInfo.getLockedMonitors().length);
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Full thread info (short info and stacktrace)<br>
	 * Example:
	 * <p>
	 * "Thread-0" Id=10 BLOCKED <br>
	 * at com.aionemu.gameserver.DeadlockTest$1$1.run(DeadlockTest.java:70)<br>
	 * - locked java.lang.Object@fa3ac1<br>
	 * at java.lang.Thread.run(Thread.java:662)
	 * </p>
	 */
	private String printDumpedThreadInfo(ThreadInfo threadInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\"" + threadInfo.getThreadName() + "\"" + " Id=" + threadInfo.getThreadId() + " "
				+ threadInfo.getThreadState() + "\n");
		StackTraceElement[] stacktrace = threadInfo.getStackTrace();
		for (int i = 0; i < stacktrace.length; i++) {
			StackTraceElement ste = stacktrace[i];
			sb.append("\t" + "at " + ste.toString() + "\n");
			for (MonitorInfo mi : threadInfo.getLockedMonitors()) {
				if (mi.getLockedStackDepth() == i) {
					sb.append("\t-  locked " + mi);
					sb.append('\n');
				}
			}
		}
		return sb.toString();
	}
}
