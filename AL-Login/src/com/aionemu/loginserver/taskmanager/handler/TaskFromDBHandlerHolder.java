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

import com.aionemu.loginserver.taskmanager.handler.implementations.CleanAccountsHandler;
import com.aionemu.loginserver.taskmanager.handler.implementations.RestartHandler;
import com.aionemu.loginserver.taskmanager.handler.implementations.ShutdownHandler;

/**
 *
 * @author nrg
 */
public enum TaskFromDBHandlerHolder {

    SHUTDOWN(ShutdownHandler.class),
    RESTART(RestartHandler.class),
    CLEAN_ACCOUNTS(CleanAccountsHandler.class);
    private Class<? extends TaskFromDBHandler> taskClass;

    private TaskFromDBHandlerHolder(Class<? extends TaskFromDBHandler> taskClass) {
        this.taskClass = taskClass;
    }

    public Class<? extends TaskFromDBHandler> getTaskClass() {
        return taskClass;
    }
}
