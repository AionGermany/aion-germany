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
package com.aionemu.gameserver.utils.gametime;

import java.security.InvalidParameterException;

import com.aionemu.gameserver.services.WeatherService;
import com.aionemu.gameserver.spawnengine.TemporarySpawnEngine;

/**
 * Represents the internal clock for the time in aion world
 *
 * @author Ben, reworked by vlog
 */
public class GameTime implements Cloneable {

	private static final int MINUTES_IN_HOUR = 60;
	private static final int MINUTES_IN_DAY = MINUTES_IN_HOUR * 24;
	private static final int MINUTES_IN_YEAR = (31 * 7 + 30 * 4 + 28 * 1) * MINUTES_IN_DAY;
	private int gameTime = 0;
	private DayTime dayTime;

	private enum Monthes {

		JANUARY(31),
		FEBRUARY(28),
		MARCH(31),
		APRIL(30),
		MAY(31),
		JUNE(30),
		JULY(31),
		AUGUST(31),
		SEPTEMBER(30),
		OCTOBER(31),
		NOVEMBER(30),
		DECEMBER(31);

		private int _days;

		Monthes(int days) {
			_days = days;
		}

		public int getDays() {
			return _days;
		}
	}

	;

	/**
	 * Constructs a GameTime with the given time in minutes since midnight 01.01.0000
	 *
	 * @param time
	 *            Minutes since midnight 01.01.0000
	 */
	public GameTime(int time) {
		if (time < 0) {
			throw new InvalidParameterException("Time must be >= 0");
		}
		gameTime = time;
		calculateDayTime();
	}

	/**
	 * Get the proper amount of minutes in this month
	 *
	 * @param m
	 * @return time in minutes in this month
	 */
	public int getProperMinutesInMonth(Monthes m) {
		return m.getDays() * MINUTES_IN_DAY;
	}

	/**
	 * Gets the ingame time in minutes
	 *
	 * @return The number of minutes since 01.01.0000 00:00:00
	 */
	public int getTime() {
		return gameTime;
	}

	/**
	 * Increases game time by a minute
	 */
	public void increase() {
		gameTime++;
		if (getMinute() == 0) {
			checkDayTimeChange();
		}
	}

	/**
	 * Calculate new day time and send events on change
	 */
	public void checkDayTimeChange() {
		DayTime oldDayTime = this.dayTime;
		calculateDayTime();
		onHourChange();
		if (oldDayTime != this.dayTime) {
			onDayTimeChange();
		}
	}

	/**
	 * Calculate the day time
	 */
	public void calculateDayTime() {
		int hour = getHour();
		if (hour > 21 || hour < 4) {
			dayTime = DayTime.NIGHT;
		}
		else if (hour > 16) {
			dayTime = DayTime.EVENING;
		}
		else if (hour > 8) {
			dayTime = DayTime.AFTERNOON;
		}
		else {
			dayTime = DayTime.MORNING;
		}
	}

	private void onHourChange() {
		TemporarySpawnEngine.onHourChange();
	}

	/**
	 * Perform actions upon day time change
	 */
	private void onDayTimeChange() {
		WeatherService.getInstance().checkWeathersTime();
	}

	/**
	 * Gets the year in the game: 0 - <integer bound>
	 *
	 * @return Year
	 */
	public int getYear() {
		return gameTime / MINUTES_IN_YEAR;
	}

	/**
	 * Gets the month in the game, 1 - 12
	 *
	 * @return Month 1-12
	 */
	public int getMonth() {
		int answer = 1;
		int minutesInYear = gameTime % MINUTES_IN_YEAR;
		for (Monthes m : Monthes.values()) {
			if ((minutesInYear - getProperMinutesInMonth(m)) > 0) {
				minutesInYear = minutesInYear - getProperMinutesInMonth(m);
				answer = answer + 1;
			}
			else if ((minutesInYear - getProperMinutesInMonth(m)) == 0) {
				answer = answer + 1;
				break;
			}
			else {
				break;
			}
		}
		return answer;
	}

	/**
	 * Gets the day in the game, 1 - Monthes.getDays()
	 *
	 * @return Day 1 - Monthes.getDays()
	 */
	public int getDay() {
		int answer = 1;
		int minutesInYear = gameTime % MINUTES_IN_YEAR;
		for (Monthes m : Monthes.values()) {
			if ((minutesInYear - getProperMinutesInMonth(m)) > 0) {
				minutesInYear = minutesInYear - getProperMinutesInMonth(m);
			}
			else if ((minutesInYear - getProperMinutesInMonth(m)) == 0) {
				break;
			}
			else {
				answer = minutesInYear / MINUTES_IN_DAY + 1;
				break;
			}
		}
		return answer;
	}

	/**
	 * Gets the hour in the game, 0-23
	 *
	 * @return Hour 0-23
	 */
	public int getHour() {
		return (gameTime % MINUTES_IN_DAY) / (MINUTES_IN_HOUR);
	}

	/**
	 * Gets the minute in the game, 0-59
	 *
	 * @return Minute 0-59
	 */
	public int getMinute() {
		return (gameTime % MINUTES_IN_HOUR);
	}

	/**
	 * @return the dayTime
	 */
	public DayTime getDayTime() {
		return dayTime;
	}

	/**
	 * Convert from game time into real time
	 *
	 * @author vlog
	 */
	public int convertTime() {
		return this.getTime() / 12;
	}

	/**
	 * Subtract the given game time from this game time
	 *
	 * @param game
	 *            time to subtract
	 * @return new game time
	 */
	public GameTime minus(GameTime gt) {
		return new GameTime(this.getTime() - gt.getTime());
	}

	/**
	 * Add the given game time to this game time
	 *
	 * @param game
	 *            time to add
	 * @return new game time
	 */
	public GameTime plus(GameTime gt) {
		return new GameTime(this.getTime() + gt.getTime());
	}

	/**
	 * Compares this time and the time given
	 *
	 * @param gt
	 * @return true, if this time is greater
	 */
	public boolean isGreaterThan(GameTime gt) {
		return this.getTime() > gt.getTime();
	}

	/**
	 * Compares this time and the time given
	 *
	 * @param gt
	 * @return true, if this time is less
	 */
	public boolean isLessThan(GameTime gt) {
		return this.getTime() < gt.getTime();
	}

	/**
	 * Compare two game times
	 *
	 * @param GameTime
	 *            object
	 * @return true or false
	 * @author vlog
	 */
	@Override
	public boolean equals(Object o) {
		GameTime other = (GameTime) o;
		return this.getTime() == other.getTime();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public Object clone() {
		return new GameTime(gameTime);
	}
}
