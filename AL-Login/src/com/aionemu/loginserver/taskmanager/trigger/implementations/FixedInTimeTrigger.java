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


package com.aionemu.loginserver.taskmanager.trigger.implementations;

import com.aionemu.loginserver.taskmanager.trigger.TaskFromDBTrigger;
import com.aionemu.loginserver.utils.ThreadPoolManager;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nrg
 */
public class FixedInTimeTrigger extends TaskFromDBTrigger {

    private static Logger log = LoggerFactory.getLogger(FixedInTimeTrigger.class);
    private final int DAY_IN_MSEC = 24 * 60 * 60 * 1000;
    private int hour, minute, second;

    @Override
    public boolean isValidTrigger() {
        if (params.length == 1) {
            try {
                String time[] = params[0].split(":");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                second = Integer.parseInt(time[2]);
                return true;
            } catch (NumberFormatException e) {
                log.warn("Could not parse the time for a FixedInTimeTrigger from DB", e);
            } catch (Exception e) {
                log.warn("A time for FixedInTimeTrigger is missing or invalid", e);
            }
        }
        log.warn("Not exact 1 parameter for FixedInTimeTrigger received, task is not registered");
        return false;
    }

    /**
     * Run a fixed in the time (HH:MM:SS) task
     */
    @Override
    public void initTrigger() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

        if (delay < 0) {
            delay += DAY_IN_MSEC;
        }

        ThreadPoolManager.getInstance().scheduleAtFixedRate(this, delay, DAY_IN_MSEC);
    }
}
