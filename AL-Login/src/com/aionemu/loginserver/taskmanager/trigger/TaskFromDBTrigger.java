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


package com.aionemu.loginserver.taskmanager.trigger;

import com.aionemu.loginserver.taskmanager.handler.TaskFromDBHandler;

/**
 *
 * @author nrg
 */
public abstract class TaskFromDBTrigger implements Runnable {

    protected TaskFromDBHandler handlerToTrigger;
    protected String[] params = {""};

    public int getTaskId() {
        return handlerToTrigger.getTaskId();
    }

    public TaskFromDBHandler getHandlerToTrigger() {
        return handlerToTrigger;
    }

    public void setHandlerToTrigger(TaskFromDBHandler handlerToTrigger) {
        this.handlerToTrigger = handlerToTrigger;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public final boolean isValid() {
        return handlerToTrigger != null && this.isValidTrigger() && handlerToTrigger.isValid();
    }

    public abstract boolean isValidTrigger();

    public abstract void initTrigger();

    @Override
    public void run() {
        handlerToTrigger.trigger();
    }
}
