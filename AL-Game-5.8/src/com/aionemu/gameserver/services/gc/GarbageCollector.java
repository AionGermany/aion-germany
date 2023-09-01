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
package com.aionemu.gameserver.services.gc;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.GSConfig;

/**
 * @author GiGatR00n v4.7.5.x
 */
public class GarbageCollector extends Thread {

	private static final Logger log = LoggerFactory.getLogger(GarbageCollector.class);

	private static long g_Period = (30 * 60 * 1000); // 30 minutes

	public GarbageCollector() {
		g_Period = (GSConfig.GC_OPTIMIZATION_TIME < 1) ? 30 : GSConfig.GC_OPTIMIZATION_TIME;
		g_Period = g_Period * 60 * 1000;
	}

	/**
	 * instantiate class
	 */
	private static class SingletonHolder {

		protected static final GarbageCollector instance = new GarbageCollector();
	}

	public static GarbageCollector getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public void run() {
		if (GSConfig.ENABLE_MEMORY_GC) {
			GameServer.log.info("[GarbageCollector] Garbage Collector is scheduled to start in " + String.valueOf((g_Period / 1000) / 60) + " minutes.");
			StartMemoryOptimization();
		}
		else {
			GameServer.log.info("[GarbageCollector] Garbage Collector is turned off by administrator.");
		}
	}

	private void StartMemoryOptimization() {

		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					// When we reload configs, it need to initialized again.
					g_Period = (GSConfig.GC_OPTIMIZATION_TIME < 1) ? 30 : GSConfig.GC_OPTIMIZATION_TIME;
					g_Period = g_Period * 60 * 1000;

					if (GSConfig.ENABLE_MEMORY_GC) {
						log.info("[GarbageCollector] Garbage Collector is optimizing memory to free unused heap memory.");
						System.gc();
						System.runFinalization();
						log.info("[GarbageCollector] Garbage Collector has finished optimizing memory.");
					}
				}
				catch (Exception e) {
					log.error("[GarbageCollector] Error on optimizing memory: " + e.getMessage());
				}
			}
		}, g_Period);
	}
}
