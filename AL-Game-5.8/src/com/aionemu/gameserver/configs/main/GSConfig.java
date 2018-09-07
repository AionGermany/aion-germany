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

import java.util.Calendar;

import com.aionemu.commons.configuration.Property;

public class GSConfig {

	/**
	 * Gameserver
	 */

	/* Server Country Code */
	@Property(key = "gameserver.country.code", defaultValue = "1")
	public static int SERVER_COUNTRY_CODE;
	/* Server Credits Name */
	@Property(key = "gameserver.name", defaultValue = "Aion Lightning")
	public static String SERVER_NAME;
	/* Server Credits Name */
	@Property(key = "loginserver.database.name", defaultValue = "al_login_ls")
	public static String LOGINSERVER_NAME;
	/* Server Version */
	@Property(key = "gameserver.version", defaultValue = "5.0")
	public static String SERVER_VERSION;
	/* Players Max Level */
	@Property(key = "gameserver.players.max.level", defaultValue = "75")
	public static int PLAYER_MAX_LEVEL;
	@Property(key = "gameserver.lang", defaultValue = "en")
	public static String LANG;
	/* Time Zone name (used for events & timed spawns) */
	@Property(key = "gameserver.timezone", defaultValue = "")
	public static String TIME_ZONE_ID = Calendar.getInstance().getTimeZone().getID();
	/* Enable connection with CS (ChatServer) */
	@Property(key = "gameserver.chatserver.enable", defaultValue = "true")
	public static boolean ENABLE_CHAT_SERVER;

	@Property(key = "gameserver.chatserver.port", defaultValue = "10241")
	public static int CHATSERVER_PORT;
	/* Server MOTD Display revision */
	@Property(key = "gameserver.revisiondisplay.enable", defaultValue = "false")
	public static boolean SERVER_MOTD_DISPLAYREV;
	/**
	 * Character creation
	 */
	@Property(key = "gameserver.character.creation.mode", defaultValue = "0")
	public static int CHARACTER_CREATION_MODE;
	/**
	 * Server Mode
	 */
	@Property(key = "gameserver.character.limit.count", defaultValue = "8")
	public static int CHARACTER_LIMIT_COUNT;
	@Property(key = "gameserver.character.faction.limitation.mode", defaultValue = "0")
	public static int CHARACTER_FACTION_LIMITATION_MODE;
	@Property(key = "gameserver.ratio.limitation.enable", defaultValue = "false")
	public static boolean ENABLE_RATIO_LIMITATION;
	@Property(key = "gameserver.ratio.min.value", defaultValue = "60")
	public static int RATIO_MIN_VALUE;
	@Property(key = "gameserver.ratio.min.required.level", defaultValue = "10")
	public static int RATIO_MIN_REQUIRED_LEVEL;
	@Property(key = "gameserver.ratio.min.characters_count", defaultValue = "50")
	public static int RATIO_MIN_CHARACTERS_COUNT;
	@Property(key = "gameserver.ratio.high_player_count.disabling", defaultValue = "500")
	public static int RATIO_HIGH_PLAYER_COUNT_DISABLING;
	/**
	 * Misc
	 */
	@Property(key = "gameserver.abyssranking.small.cache", defaultValue = "false")
	public static boolean ABYSSRANKING_SMALL_CACHE;
	@Property(key = "gameserver.character.reentry.time", defaultValue = "20")
	public static int CHARACTER_REENTRY_TIME;

	/**
	 * Player Starting Level
	 */
	@Property(key = "gameserver.starting.level", defaultValue = "1")
	public static int STARTING_LEVEL;

	@Property(key = "gameserver.startClass.maxLevel", defaultValue = "10")
	public static int STARTCLASS_MAXLEVEL;

	/**
	 * Memory Optimization Configs
	 */
	@Property(key = "gameserver.gc.enable", defaultValue = "true")
	public static boolean ENABLE_MEMORY_GC;

	@Property(key = "gameserver.gc.optimization.time", defaultValue = "5")
	public static int GC_OPTIMIZATION_TIME;
}
