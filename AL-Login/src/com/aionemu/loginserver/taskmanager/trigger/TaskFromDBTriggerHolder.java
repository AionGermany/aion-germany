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

import com.aionemu.loginserver.taskmanager.trigger.implementations.AfterRestartTrigger;
import com.aionemu.loginserver.taskmanager.trigger.implementations.FixedInTimeTrigger;

/**
 *
 * @author nrg
 */
public enum TaskFromDBTriggerHolder {

    FIXED_IN_TIME(FixedInTimeTrigger.class),
    AFTER_RESTART(AfterRestartTrigger.class);
    private Class<? extends TaskFromDBTrigger> triggerClass;

    private TaskFromDBTriggerHolder(Class<? extends TaskFromDBTrigger> triggerClass) {
        this.triggerClass = triggerClass;
    }

    public Class<? extends TaskFromDBTrigger> getTriggerClass() {
        return triggerClass;
    }
}
