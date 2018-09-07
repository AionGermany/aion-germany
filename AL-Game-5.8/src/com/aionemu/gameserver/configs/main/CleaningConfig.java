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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author nrg
 */
public class CleaningConfig {

	/**
	 * Enable Database Cleaning
	 */
	@Property(key = "gameserver.cleaning.enable", defaultValue = "false")
	public static boolean CLEANING_ENABLE;
	/**
	 * Period after which inactive chars get deleted It is expressed in number of days
	 */
	@Property(key = "gameserver.cleaning.period", defaultValue = "180")
	public static int CLEANING_PERIOD;
	/**
	 * Number of threads executing the cleaning If you have many chars to delete you should use a value between 4 and 6
	 */
	@Property(key = "gameserver.cleaning.threads", defaultValue = "2")
	public static int CLEANING_THREADS;
	/**
	 * Maximum amount of chars deleted at one execution If too many chars are deleted in one run your database will get strongly fragmented which increases runtime dramatically Note: 0 for not
	 * limitation
	 */
	@Property(key = "gameserver.cleaning.limit", defaultValue = "5000")
	public static int CLEANING_LIMIT;
}
