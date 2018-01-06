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


package com.aionemu.loginserver.taskmanager.handler;

/**
 * @author nrg
 */
public abstract class TaskFromDBHandler {

    protected int taskId;
    protected String[] params = {""};

    /**
     * Task's id
     *
     * @return
     */
    public int getTaskId() {
        return taskId;
    }

    /**
     * Task's id
     *
     * @param int
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Task's execution paramaeters
     *
     * @return parameters
     */
    public String[] getParams() {
        return params;
    }

    /**
     * Task's execution param(s)
     *
     * @param params String[]
     */
    public void setParams(String params[]) {
        this.params = params;
    }

    /**
     * Check if the task's parameters are valid
     *
     * @return true if valid, false otherwise
     */
    public abstract boolean isValid();

    /**
     * Triggers the handlers functions
     */
    public abstract void trigger();
}
