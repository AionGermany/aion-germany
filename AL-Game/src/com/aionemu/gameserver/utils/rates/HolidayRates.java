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
package com.aionemu.gameserver.utils.rates;

import java.util.Calendar;
import java.util.Date;

import com.aionemu.gameserver.configs.main.RateConfig;

/**
 * @author Dr2co
 */

public class HolidayRates {

	private static Calendar calendar = Calendar.getInstance();

	public static int getHolidayRates(int accessLevel) {
		if (RateConfig.HOLIDAY_RATE_ENAMBLE) {
			Date date = new Date();
			calendar.setTime(date);
			int rate = 0;
			switch (accessLevel) {
				case 0:
					rate = RateConfig.HOLIDAY_RATE_REGULAR;
					break;
				case 1:
					rate = RateConfig.HOLIDAY_RATE_PREMIUM;
					break;
				case 2:
					rate = RateConfig.HOLIDAY_RATE_VIP;
					break;
			}
			for (String level : RateConfig.HOLIDAY_RATE_DAYS.split(",")) {
				if (calendar.get(Calendar.DAY_OF_WEEK) == Integer.parseInt(level)) {
					return rate;
				}
			}
		}
		return 0;
	}
}
