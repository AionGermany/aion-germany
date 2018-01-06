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


package com.aionemu.loginserver.taskmanager;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.dao.TaskFromDBDAO;
import com.aionemu.loginserver.taskmanager.trigger.TaskFromDBTrigger;

/**
 * @author nrg
 */
public class TaskFromDBManager {

    private static final Logger log = LoggerFactory.getLogger(TaskFromDBManager.class);
    private ArrayList<TaskFromDBTrigger> tasksList;

    private TaskFromDBManager() {
        tasksList = getDAO().getAllTasks();
        log.info("Loaded " + tasksList.size() + " task" + (tasksList.size() > 1 ? "s" : "") + " from the database");

        registerTaskInstances();
    }

    /**
     * Launching & checking task process
     */
    private void registerTaskInstances() {
        // For all tasks from DB
        for (TaskFromDBTrigger trigger : tasksList) {
            if (trigger.isValid()) {
                trigger.initTrigger();
            } else {
                log.error("Invalid task from db with ID: " + trigger.getTaskId());
            }
        }
    }

    /**
     * Retuns {@link com.aionemu.gameserver.dao.TaskFromDBDAO} , just a shortcut
     *
     * @return {@link com.aionemu.gameserver.dao.TaskFromDBDAO}
     */
    private static TaskFromDBDAO getDAO() {
        return DAOManager.getDAO(TaskFromDBDAO.class);
    }

    /**
     * Get the instance
     *
     * @return
     */
    public static TaskFromDBManager getInstance() {
        return TaskFromDBManager.SingletonHolder.instance;
    }

    /**
     * SingletonHolder
     */
    private static class SingletonHolder {

        protected static final TaskFromDBManager instance = new TaskFromDBManager();
    }
}
