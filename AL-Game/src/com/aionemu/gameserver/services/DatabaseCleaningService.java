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
package com.aionemu.gameserver.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.CleaningConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Offers the functionality to delete all data about inactive players
 *
 * @author nrg
 */
public class DatabaseCleaningService {

	private Logger log = LoggerFactory.getLogger(DatabaseCleaningService.class);
	private PlayerDAO dao = DAOManager.getDAO(PlayerDAO.class);
	// Singelton
	private static DatabaseCleaningService instance = new DatabaseCleaningService();
	// Workers
	private List<Worker> workers;
	// Starttime of service
	private long startTime;

	/**
	 * Private constructor to avoid extern access and ensure singelton
	 */
	private DatabaseCleaningService() {
		if (CleaningConfig.CLEANING_ENABLE) {
			runCleaning();
		}
	}

	/**
	 * Cleans the databse from inactive player data
	 */
	private void runCleaning() {
		// Execution time
		log.info("DatabaseCleaningService: Executing database cleaning");
		startTime = System.currentTimeMillis();

		// getting period for deletion
		int periodInDays = CleaningConfig.CLEANING_PERIOD;

		// only a security feature
		int SECURITY_MINIMUM_PERIOD = 30;
		if (periodInDays > SECURITY_MINIMUM_PERIOD) {
			delegateToThreads(CleaningConfig.CLEANING_THREADS, dao.getInactiveAccounts(periodInDays, CleaningConfig.CLEANING_LIMIT));
			monitoringProcess();
		}
		else {
			log.warn("The configured days for database cleaning is to low. For security reasons the service will only execute with periods over 30 days!");
		}
	}

	private void monitoringProcess() {
		while (!allWorkersReady()) {
			try {
				int WORKER_CHECK_TIME = 10 * 1000;
				Thread.sleep(WORKER_CHECK_TIME);
				log.info("DatabaseCleaningService: Until now " + currentlyDeletedChars() + " chars deleted in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds!");
			}
			catch (InterruptedException ex) {
				log.error("DatabaseCleaningService: Got Interrupted!");
			}
		}
	}

	private boolean allWorkersReady() {
		for (Worker w : workers) {
			if (!w._READY) {
				return false;
			}
		}
		return true;
	}

	private int currentlyDeletedChars() {
		int deletedChars = 0;
		for (Worker w : workers) {
			deletedChars += w.deletedChars;
		}
		return deletedChars;
	}

	private void delegateToThreads(int numberOfThreads, Set<Integer> idsToDelegate) {
		workers = new ArrayList<Worker>();
		log.info("DatabaseCleaningService: Executing deletion over " + numberOfThreads + " longrunning threads");

		// every id to another worker with maximum of n different workers
		Iterator<Integer> i = idsToDelegate.iterator();
		for (int workerNo = 0; i.hasNext(); workerNo = ++workerNo % numberOfThreads) {
			if (workerNo >= workers.size()) {
				workers.add(new Worker());
			}
			workers.get(workerNo).ids.add(i.next());
		}

		// get them working on our longrunning
		for (Worker w : workers) {
			ThreadPoolManager.getInstance().executeLongRunning(w);
		}
	}

	/**
	 * The only extern access
	 *
	 * @return a singleton DatabaseCleaningService
	 */
	public static DatabaseCleaningService getInstance() {
		GameServer.log.info("[DatabaseCleaningService] started ...");
		return instance;
	}

	private class Worker implements Runnable {

		private List<Integer> ids = new ArrayList<Integer>();
		private int deletedChars = 0;
		private boolean _READY = false;

		@Override
		public void run() {
			for (int id : ids) {
				deletedChars += PlayerService.deleteAccountsCharsFromDB(id);
			}
			_READY = true;
		}
	}
}
