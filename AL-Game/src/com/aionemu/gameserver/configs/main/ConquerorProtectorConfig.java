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
 * @Author Elo
 */
public class ConquerorProtectorConfig {

	/**
	 * Conqueror's / Protector's Buff System
	 */
	@Property(key = "gameserver.gaurdian.enable", defaultValue = "true")
	public static boolean ENABLE_GUARDIAN_PVP;

	@Property(key = "gameserver.guardian.maps", defaultValue = "220080000,210070000")
	public static String ENABLED_MAPS_GUARDIAN;

	@Property(key = "gameserver.guardian.ignore.maps", defaultValue = "false")
	public static boolean IGNORE_MAPS;

	@Property(key = "gameserver.guardian.debugMode", defaultValue = "false")
	public static boolean ENABLE_CONQUEROR_DEBUGMODE;

	@Property(key = "gameserver.guardian.duration.pBuffLvl1", defaultValue = "30") // in Mins
	public static int DURATION_PBUFF1;

	@Property(key = "gameserver.guardian.duration.pBuffLvl2", defaultValue = "40") // in Mins
	public static int DURATION_PBUFF2;

	@Property(key = "gameserver.guardian.duration.pBuffLvl3", defaultValue = "60") // in Mins
	public static int DURATION_PBUFF3;

	@Property(key = "gameserver.guardian.duration.cBuffLvl1", defaultValue = "30") // in Mins
	public static int DURATION_CBUFF1;

	@Property(key = "gameserver.guardian.duration.cBuffLvl2", defaultValue = "40") // in Mins
	public static int DURATION_CBUFF2;

	@Property(key = "gameserver.guardian.duration.cBuffLvl3", defaultValue = "60") // in Mins
	public static int DURATION_CBUFF3;

	@Property(key = "gameserver.guardian.killcount.protector1", defaultValue = "1")
	public static int PROTECTOR_LVL1_KILLCOUNT;

	@Property(key = "gameserver.guardian.killcount.protector2", defaultValue = "10")
	public static int PROTECTOR_LVL2_KILLCOUNT;

	@Property(key = "gameserver.guardian.killcount.protector3", defaultValue = "25")
	public static int PROTECTOR_LVL3_KILLCOUNT;

	@Property(key = "gameserver.guardian.killcount.conqueror1", defaultValue = "1")
	public static int CONQUEROR_LVL1_KILLCOUNT;

	@Property(key = "gameserver.guardian.killcount.conqueror2", defaultValue = "10")
	public static int CONQUEROR_LVL2_KILLCOUNT;

	@Property(key = "gameserver.guardian.killcount.conqueror3", defaultValue = "25")
	public static int CONQUEROR_LVL3_KILLCOUNT;

}
