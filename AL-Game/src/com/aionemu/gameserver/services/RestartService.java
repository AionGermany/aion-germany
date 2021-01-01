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

import java.sql.Timestamp;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.ShutdownHook;
import com.aionemu.gameserver.dao.PlayerAchievementActionDAO;
import com.aionemu.gameserver.dao.PlayerAchievementDAO;
import com.aionemu.gameserver.dao.PlayerLunaShopDAO;
import com.aionemu.gameserver.dao.PlayerShugoSweepDAO;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementType;
import com.aionemu.gameserver.services.player.PlayerFameService;

/**
 * @author nrg
 * @rework Blackfire
 */
public class RestartService {

	private static final Logger log = LoggerFactory.getLogger(RestartService.class);

	public void onStart() {
		Timestamp date = new Timestamp(System.currentTimeMillis());
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		String daily1 = "0 0 9 ? * * *";
		String daily2 = "0 0 0/12 ? * * *";
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (calendar.get(7) == 4) {
					DAOManager.getDAO(PlayerAchievementDAO.class).deleteAchievements(AchievementType.WEEKLY);
					DAOManager.getDAO(PlayerAchievementActionDAO.class).deleteAchievementsActions(AchievementType.WEEKLY);
					DAOManager.getDAO(PlayerAchievementDAO.class).deleteAchievements(AchievementType.DAILY);
					DAOManager.getDAO(PlayerAchievementActionDAO.class).deleteAchievementsActions(AchievementType.DAILY);
					DAOManager.getDAO(PlayerLunaShopDAO.class).delete();
					DAOManager.getDAO(PlayerShugoSweepDAO.class).delete();
				} 
				else {
					DAOManager.getDAO(PlayerAchievementDAO.class).deleteAchievements(AchievementType.DAILY);
					DAOManager.getDAO(PlayerAchievementActionDAO.class).deleteAchievementsActions(AchievementType.DAILY);
					DAOManager.getDAO(PlayerLunaShopDAO.class).delete();
					DAOManager.getDAO(PlayerShugoSweepDAO.class).delete();
				}
				if (calendar.get(7) == 2) {
					PlayerFameService.getInstance().onResetWeekly();
				}
				int delay1 = 300;
				log.info("Restart Achievement + Luna Shop + Shugo Sweep");
				ShutdownHook.getInstance().doShutdown(delay1, 20, ShutdownHook.ShutdownMode.RESTART);
			}
		}, daily1);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				int delay2 = 300;
				log.info("Restart Server Service");
				ShutdownHook.getInstance().doShutdown(delay2, 20, ShutdownHook.ShutdownMode.RESTART);
			}
		}, daily2);
	}

	public static RestartService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final RestartService instance = new RestartService();
	}
}
