/**
 * This file is part of aion-lightning <aion-lightning.org>.
 * 
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ShutdownHook;
import com.aionemu.gameserver.ShutdownHook.ShutdownMode;
import com.aionemu.gameserver.configs.main.ShutdownConfig;
import com.aionemu.gameserver.model.RestartFrequency;

/**
 * @author nrg
 * @rework Blackfire
 */
public class RestartService {

	private static final Logger log = LoggerFactory.getLogger(RestartService.class);

	public static final RestartService getInstance() {
		log.info("Checking AutoShutdown Schedule...");
		return SingletonHolder.instance;
	}

	private RestartService() {
		log.info("Starting RestartService ...");
		RestartFrequency rf;
		try {
			rf = RestartFrequency.valueOf(ShutdownConfig.GAMESERVER_SHUTDOWN_FREQUENCY);
		}
		catch (Exception e) {
			log.warn("Could not find stated RestartFrequency. Using NEVER as default value!");
			rf = RestartFrequency.NEVER;
		}
		setTimer(rf);

	}

	private void setTimer(RestartFrequency frequency) {
		// get time to restart
		String[] time = getRestartTime();
		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1]);

		// calculate the correct time based on frequency
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		boolean isMissed = calendar.getTimeInMillis() < System.currentTimeMillis();

		// switch frequency
		switch (frequency) {
			case NEVER:
				log.warn("Automatic restart/shutdown system disable.");
				return;
			case DAILY:
				if (isMissed) // execute next day if we missed the time today (what is mostly the case)
					calendar.add(Calendar.DAY_OF_YEAR, 1);
				break;
			case WEEKLY:
				calendar.add(Calendar.WEEK_OF_YEAR, 1);
				break;
			case MONTHLY:
				calendar.add(Calendar.MONTH, 1);
		}
		// Restart timer
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				log.info("Shutdown task is triggered - shutting down gameserver!");
				if (ShutdownConfig.HOOK_MODE_AUTO == 1) {
					ShutdownHook.getInstance().doShutdown(ShutdownConfig.HOOK_DELAY_AUTO, ShutdownConfig.ANNOUNCE_INTERVAL_AUTO, ShutdownMode.SHUTDOWN);
				}
				else if (ShutdownConfig.HOOK_MODE == 2) {
					ShutdownHook.getInstance().doShutdown(ShutdownConfig.HOOK_DELAY_AUTO, ShutdownConfig.ANNOUNCE_INTERVAL_AUTO, ShutdownMode.RESTART);
				}
			}
		}, calendar.getTime());
		log.info("Auto restart system started!");
		log.info("Scheduled next shutdown for " + calendar.getTime().toString());
	}

	private String[] getRestartTime() {
		String[] time;
		if ((time = ShutdownConfig.GAMESERVER_SHUTDOWN_TIME.split(":")).length != 2) {
			log.warn("You did not state a valid shutdown time. Using 5:00 AM as default value!");
			return new String[] { "5", "0" };
		}
		return time;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final RestartService instance = new RestartService();
	}
}
