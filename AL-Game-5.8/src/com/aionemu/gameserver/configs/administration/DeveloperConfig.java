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
package com.aionemu.gameserver.configs.administration;

import com.aionemu.commons.configuration.Property;

/**
 * @author ATracer
 */
public class DeveloperConfig {

	/**
	 * if false - not spawns will be loaded
	 */
	@Property(key = "gameserver.developer.spawn.enable", defaultValue = "true")
	public static boolean SPAWN_ENABLE;
	/**
	 * if true - checks spawns being outside any known zones
	 */
	@Property(key = "gameserver.developer.spawn.check", defaultValue = "false")
	public static boolean SPAWN_CHECK;
	/**
	 * if set, adds specified stat bonus for items with random bonusess
	 */
	@Property(key = "gameserver.developer.itemstat.id", defaultValue = "0")
	public static int ITEM_STAT_ID;
	/**
	 * Show sended cm/sm packets in game server log
	 */
	@Property(key = "gameserver.developer.showpackets.enable", defaultValue = "false")
	public static boolean SHOW_PACKETS;
	/**
	 * Display Packets Name in Chat Window
	 */
	@Property(key = "gameserver.developer.show.packetnames.inchat.enable", defaultValue = "false")
	public static boolean SHOW_PACKET_NAMES_INCHAT;
	/**
	 * Display Packets Hex-Bytes in Chat Window
	 */
	@Property(key = "gameserver.developer.show.packetbytes.inchat.enable", defaultValue = "false")
	public static boolean SHOW_PACKET_BYTES_INCHAT;
	/**
	 * How many Packet Bytes should be shown in Chat Window? Default: 200-Hexed bytes
	 */
	@Property(key = "gameserver.developer.show.packetbytes.inchat.total", defaultValue = "200")
	public static int TOTAL_PACKET_BYTES_INCHAT;
	/**
	 * Filters which Packets should be shown in Chat Windows? Default: * e.g. SM_MOVE, CM_CASTSPELL, CM_ATTACK
	 */
	@Property(key = "gameserver.developer.filter.packets.inchat", defaultValue = "*")
	public static String FILTERED_PACKETS_INCHAT;
	/**
	 * if Player Access Level is meet, display Packets-Name or Hex-Bytes in Chat Window Tip: Player Access-Level higher than or equal to 3 is recommended 10 - Server-Owner 9 - Server-CoOwner 8 -
	 * Server-Admin 7 - Server-CoAdmin 6 - Developer 5 - Admin 4 - Head-GM 3 - Senior-GM 2 - Junior-GM 1 - Supporter 0 - Players
	 */
	@Property(key = "gameserver.developer.show.packets.inchat.accesslevel", defaultValue = "6")
	public static int SHOW_PACKETS_INCHAT_ACCESSLEVEL;
}
