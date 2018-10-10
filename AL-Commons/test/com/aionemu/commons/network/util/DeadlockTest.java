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

import java.util.ArrayList;
import java.util.Collection;

import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * This test is for checking print of deadlock report. Should not be executed
 * during unit test phase
 * 
 * @author ATracer
 */
public class DeadlockTest {

	private final Object lock1 = new Object();
	private final Object lock2 = new Object();

	@Test(enabled = false)
	public void testCommon() {
		DeadLockDetector dd = new DeadLockDetector(2, DeadLockDetector.NOTHING);
		dd.start();
		createDeadlock();
	}

	/**
	 * This "smart" logic is for generating long stacktrace
	 */
	private void createDeadlock() {
		final Collection<String> coll = new ArrayList<String>();
		coll.add("1");
		synchronized (lock1) {
			Collection<Integer> filtered = Collections2
					.filter(Collections2.transform(coll, new Function<String, Integer>() {

						@Override
						public Integer apply(String input) {

							new Thread(new Runnable() {

								@Override
								public void run() {
									System.out.println("Locking lock 2 from thread 2");
									synchronized (lock2) {
										System.out.println("Deadlocking");
										synchronized (lock1) {
											System.out.println("This will not be printed");
										}
									}
								}
							}).start();
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							synchronized (lock2) {
								System.out.println("This will not be printed");
							}
							return Integer.valueOf(input);
						}
					}), new Predicate<Integer>() {

						@Override
						public boolean apply(Integer input) {
							return true;
						}
					});
			System.out.println(filtered.size());
		}
	}
}
