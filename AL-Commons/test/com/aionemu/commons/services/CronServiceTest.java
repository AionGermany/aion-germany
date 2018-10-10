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

package com.aionemu.commons.services;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.commons.lang.mutable.MutableInt;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aionemu.commons.services.cron.CurrentThreadRunnableRunner;

/**
 * @author SoulKeeper
 */
@Test(singleThreaded = false)
public class CronServiceTest extends Assert {

	private CronService cronService;

	@BeforeClass
	public void init() throws Exception {
		Constructor<CronService> constructor = CronService.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		cronService = constructor.newInstance();
		cronService.init(CurrentThreadRunnableRunner.class);
	}

	@Test
	public void testCronTriggerExecutionTime() throws Exception {

		MutableInt ref = new MutableInt();
		Runnable test = newIncrementingRunnable(ref);

		// should run on second # 0 and every 2 seconds
		// execute on 0, 2, 4...

		cronService.schedule(test, "0/2 * * * * ?");
		sleep(5);
		int count = ref.intValue();
		assertEquals(count, 3);
	}

	@Test
	public void testGetRunnables() {
		Runnable test = newRunnable();
		cronService.schedule(test, "* 5 * * * ?");
		Collection<Runnable> col = cronService.getRunnables().keySet();
		assertTrue(col.contains(test));
	}

	@Test
	public void testCancelRunnableUsingRunnableReference() throws Exception {
		final MutableInt val = new MutableInt();
		Runnable test = new Runnable() {
			@Override
			public void run() {
				val.increment();
				cronService.cancel(this);
			}
		};
		cronService.schedule(test, "0/2 * * * * ?");
		sleep(5);
		assertEquals(val.intValue(), 1);
	}

	@Test
	public void testCancelRunnableUsingJobDetails() throws Exception {
		final MutableInt val = new MutableInt();
		Runnable test = new Runnable() {
			@Override
			public void run() {
				val.increment();
				cronService.cancel(cronService.getRunnables().get(this));
			}
		};
		cronService.schedule(test, "0/2 * * * * ?");
		sleep(5);
		assertEquals(val.intValue(), 1);
	}

	@Test
	public void testCancelledRunableGC() throws Exception {
		final MutableBoolean collected = new MutableBoolean();
		Runnable r = new Runnable() {

			@Override
			public void run() {
				cronService.cancel(this);
			}

			@Override
			public void finalize() throws Throwable {
				collected.setValue(true);
				super.finalize();
			}
		};

		cronService.schedule(r, "0/2 * * * * ?");
		r = null;
		sleep(5);
		for (int i = 0; i < 100; i++) {
			System.gc();
		}
		assertEquals(collected.booleanValue(), true);
	}

	@Test
	public void testGetJobTriggers() {
		Runnable r = newRunnable();
		cronService.schedule(r, "0 15 * * * ?");
		JobDetail jd = cronService.getRunnables().get(r);
		List<? extends Trigger> triggers = cronService.getJobTriggers(jd);
		assertEquals(triggers.size(), 1);
	}

	@AfterClass
	public void shutdown() {
		cronService.shutdown();
	}

	private static Runnable newRunnable() {
		return newIncrementingRunnable(null);
	}

	private static Runnable newIncrementingRunnable(final MutableInt ref) {
		return new Runnable() {
			@Override
			public void run() {
				if (ref != null) {
					ref.increment();
				}
			}
		};
	}

	@Test(enabled = false)
	public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			throw new RuntimeException("Sleep Interrupted", e);
		}
	}
}
