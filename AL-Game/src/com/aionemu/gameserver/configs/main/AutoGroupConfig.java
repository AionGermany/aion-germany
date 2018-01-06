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
 * @author xTz
 * @author GiGatR00n v4.7.5.x
 */
public class AutoGroupConfig {

	/**
	 * Dredgion
	 */
	@Property(key = "gameserver.autogroup.enable", defaultValue = "true")
	public static boolean AUTO_GROUP_ENABLE;
	@Property(key = "gameserver.startTime.enable", defaultValue = "true")
	public static boolean START_TIME_ENABLE;
	@Property(key = "gameserver.dredgion.timer", defaultValue = "120")
	public static long DREDGION_TIMER;
	@Property(key = "gameserver.dredgion2.enable", defaultValue = "true")
	public static boolean DREDGION2_ENABLE;
	@Property(key = "gameserver.dredgion.time", defaultValue = "0 0 0,12,20 ? * *")
	public static String DREDGION_TIMES;
	/**
	 * Kamar Battlefield v4.0 (PvPvE - 12v12)
	 */
	@Property(key = "gameserver.kamar.timer", defaultValue = "120")
	public static long KAMAR_TIMER;
	@Property(key = "gameserver.kamar.enable", defaultValue = "true")
	public static boolean KAMAR_ENABLE;
	@Property(key = "gameserver.kamar.time", defaultValue = "0 0 0,21 ? * *")
	public static String KAMAR_TIMES;
	/**
	 * Jormungand Marching Route v4.5 (PvPvE - 6v6)
	 */
	@Property(key = "gameserver.jormungand.timer", defaultValue = "120")
	public static long JORMUNGAND_TIMER;
	@Property(key = "gameserver.jormungand.enable", defaultValue = "true")
	public static boolean JORMUNGAND_ENABLE;
	@Property(key = "gameserver.jormungand.time", defaultValue = "0 0 0,20 ? * *")
	public static String JORMUNGAND_TIMES;
	/**
	 * Steel Wall Bastion Battlefield v4.5 (PvPvE - 24v24)
	 */
	@Property(key = "gameserver.stellwall.timer", defaultValue = "120")
	public static long STEELWALL_TIMER;
	@Property(key = "gameserver.stellwall.enable", defaultValue = "true")
	public static boolean STEELWALL_ENABLE;
	@Property(key = "gameserver.stellwall.time", defaultValue = "0 0 0,19 ? * *")
	public static String STEELWALL_TIMES;
	/**
	 * Runatorium v4.7 (PvPvE - 6v6)
	 */
	@Property(key = "gameserver.runatorium.enable", defaultValue = "true")
	public static boolean RUNATORIUM_ENABLE;
	@Property(key = "gameserver.runatorium.timer", defaultValue = "120")
	public static long RUNATORIUM_TIMER;
	@Property(key = "gameserver.runatorium.time", defaultValue = "0 0 22 1/1 * ? *")
	public static String RUNATORIUM_TIMES;
	/**
	 * Balaur Marching Route v5.x (PvPvE - 6v6)
	 */
	@Property(key = "gameserver.balaurmarching.timer", defaultValue = "120")
	public static long BALAURMARCHING_TIMER;
	@Property(key = "gameserver.balaurmarching.enable", defaultValue = "true")
	public static boolean BALAURMARCHING_ENABLE;
	@Property(key = "gameserver.balaurmarching.time", defaultValue = "0 0 0,20 ? * *")
	public static String BALAURMARCHING_TIMES;
	/**
	 * Runatorium Ruins v5.x (PvPvE - 6v6)
	 */
	@Property(key = "gameserver.runatoriumruins.enable", defaultValue = "true")
	public static boolean RUNATORIUMRUINS_ENABLE;
	@Property(key = "gameserver.runatoriumruins.timer", defaultValue = "120")
	public static long RUNATORIUMRUINS_TIMER;
	@Property(key = "gameserver.runatoriumruins.time", defaultValue = "0 0 22 1/1 * ? *")
	public static String RUNATORIUMRUINS_TIMES;
	/**
	 * Golden Arena v5.3
	 */
	@Property(key = "gameserver.goldencrucible.enable", defaultValue = "true")
	public static boolean GOLDENCRUCIBLE_ENABLE;
	@Property(key = "gameserver.goldencrucible.timer", defaultValue = "60")
	public static long GOLDENCRUCIBLE_TIMER;
	@Property(key = "gameserver.goldencrucible.time", defaultValue = "0 0 0 ? * SAT,SUN *")
	public static String GOLDENCRUCIBLE_TIMES;
	/**
	 * Sanctum Battlefield v5.4
	 */
	@Property(key = "gameserver.sanctumbattlefield.enable", defaultValue = "true")
	public static boolean SANCTUMBATTLEFIELD_ENABLE;
	@Property(key = "gameserver.sanctumbattlefield.timer", defaultValue = "60")
	public static long SANCTUMBATTLEFIELD_TIMER;
	@Property(key = "gameserver.sanctumbattlefield.time", defaultValue = "0 0 0 ? * SAT,SUN *")
	public static String SANCTUMBATTLEFIELD_TIMES;
	/**
	 * Pandaemonium Battlefield v5.4
	 */
	@Property(key = "gameserver.pandaemoniumbattlefield.enable", defaultValue = "true")
	public static boolean PANDAEMONIUMBATTLEFIELD_ENABLE;
	@Property(key = "gameserver.pandaemoniumbattlefield.timer", defaultValue = "60")
	public static long PANDAEMONIUMBATTLEFIELD_TIMER;
	@Property(key = "gameserver.pandaemoniumbattlefield.time", defaultValue = "0 0 0 ? * SAT,SUN *")
	public static String PANDAEMONIUMBATTLEFIELD_TIMES;
}
