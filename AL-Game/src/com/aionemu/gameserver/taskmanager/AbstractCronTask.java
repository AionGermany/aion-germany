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
package com.aionemu.gameserver.taskmanager;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Rolandas
 */
public abstract class AbstractCronTask implements Runnable {

	private String cronExpressionString;
	private CronExpression runExpression;
	private int runTime;
	private long period;

	/**
	 * Timestamp in Seconds of the task run start, based on DB server variable
	 */
	public final int getRunTime() {
		return runTime;
	}

	/**
	 * The same as a milliseconds left, but any extended class may specify a little delay. if delay is not needed then it's simple "runTime" minus "now" function
	 */
	abstract protected long getRunDelay();

	/**
	 * Any pre-init, pre-load tasks when the instance is created
	 */
	protected void preInit() {
	}

	/**
	 * Any post-init, post-load tasks when the instance is created
	 */
	protected void postInit() {
	}

	public final String getCronExpressionString() {
		return cronExpressionString;
	}

	/**
	 * Variable name of the task start time stored in the server_variables DB table
	 */
	abstract protected String getServerTimeVariable();

	public long getPeriod() {
		return period;
	}

	/**
	 * Tasks which have to be run before the actual scheduled task
	 */
	protected void preRun() {
	}

	/**
	 * The main execution code goes here
	 */
	abstract protected void executeTask();

	/**
	 * Is the task allowed to run on its initialization (if runDelay = 0) or only at times defined by cron
	 */
	abstract protected boolean canRunOnInit();

	/**
	 * Tasks which have to be run after the task is complete and saved to DB
	 */
	protected void postRun() {
	}

	public AbstractCronTask(String cronExpression) throws ParseException {
		if (cronExpression == null) {
			throw new NullPointerException("cronExpressionString");
		}

		cronExpressionString = cronExpression;

		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		runTime = dao.load(getServerTimeVariable());

		preInit();
		runExpression = new CronExpression(cronExpressionString);
		Date nextDate = runExpression.getTimeAfter(new Date());
		Date nextAfterDate = runExpression.getTimeAfter(nextDate);
		period = nextAfterDate.getTime() - nextDate.getTime();
		postInit();

		if (getRunDelay() == 0) {
			if (canRunOnInit()) {
				ThreadPoolManager.getInstance().schedule(this, 0);
			}
			else {
				saveNextRunTime();
			}
		}
		scheduleNextRun();
	}

	private void scheduleNextRun() {
		CronService.getInstance().schedule(this, cronExpressionString, true);
	}

	private void saveNextRunTime() {
		Date nextDate = runExpression.getTimeAfter(new Date());
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		runTime = (int) (nextDate.getTime() / 1000);
		dao.store(getServerTimeVariable(), runTime);
	}

	@Override
	public final void run() {
		if (getRunDelay() > 0) {
			ThreadPoolManager.getInstance().schedule(this, getRunDelay());
		}
		else {
			preRun();

			executeTask();
			saveNextRunTime();

			postRun();
		}
	}
}
