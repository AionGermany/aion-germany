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
package com.aionemu.gameserver.configs.network;

import java.net.InetSocketAddress;

import com.aionemu.commons.configuration.Property;

public class NetworkConfig {

	/**
	 * Game Server port
	 */
	@Property(key = "gameserver.network.client.port", defaultValue = "7777")
	public static int GAME_PORT;
	/**
	 * Game Server bind ip
	 */
	@Property(key = "gameserver.network.client.host", defaultValue = "*")
	public static String GAME_BIND_ADDRESS;
	/**
	 * Max allowed online players
	 */
	@Property(key = "gameserver.network.client.maxplayers", defaultValue = "100")
	public static int MAX_ONLINE_PLAYERS;
	/**
	 * LoginServer address
	 */
	@Property(key = "gameserver.network.login.address", defaultValue = "localhost:9014")
	public static InetSocketAddress LOGIN_ADDRESS;
	/**
	 * ChatServer address
	 */
	@Property(key = "gameserver.network.chat.address", defaultValue = "localhost:9021")
	public static InetSocketAddress CHAT_ADDRESS;
	/**
	 * Password for this GameServer ID for authentication at ChatServer.
	 */
	@Property(key = "gameserver.network.chat.password", defaultValue = "")
	public static String CHAT_PASSWORD;
	/**
	 * GameServer id that this GameServer will request at LoginServer.
	 */
	@Property(key = "gameserver.network.login.gsid", defaultValue = "0")
	public static int GAMESERVER_ID;
	/**
	 * Password for this GameServer ID for authentication at LoginServer.
	 */
	@Property(key = "gameserver.network.login.password", defaultValue = "")
	public static String LOGIN_PASSWORD;
	/**
	 * Number of Threads dedicated to be doing io read & write. There is always 1 acceptor thread. If value is < 1 - acceptor thread will also handle read & write. If value is > 0 - there will be
	 * given amount of read & write threads + 1 acceptor thread.
	 */
	@Property(key = "gameserver.network.nio.threads", defaultValue = "1")
	public static int NIO_READ_WRITE_THREADS;
	/**
	 * Number of minimum threads that will be used to execute aion client packets.
	 */
	@Property(key = "gameserver.network.packet.processor.threads.min", defaultValue = "4")
	public static int PACKET_PROCESSOR_MIN_THREADS;
	/**
	 * Number of maximum threads that will be used to execute aion client packets.
	 */
	@Property(key = "gameserver.network.packet.processor.threads.max", defaultValue = "4")
	public static int PACKET_PROCESSOR_MAX_THREADS;
	/**
	 * Threshold that will be used to decide when extra threads are not needed. (it doesn't have any effect if min threads == max threads)
	 */
	@Property(key = "gameserver.network.packet.processor.threshold.kill", defaultValue = "3")
	public static int PACKET_PROCESSOR_THREAD_KILL_THRESHOLD;
	/**
	 * Threshold that will be used to decide when extra threads should be spawned. (it doesn't have any effect if min threads == max threads)
	 */
	@Property(key = "gameserver.network.packet.processor.threshold.spawn", defaultValue = "50")
	public static int PACKET_PROCESSOR_THREAD_SPAWN_THRESHOLD;
	/**
	 * If unknown packets should be logged.
	 */
	@Property(key = "gameserver.network.display.unknownpackets", defaultValue = "false")
	public static boolean DISPLAY_UNKNOWNPACKETS;
	@Property(key = "gameserver.network.flood.connections", defaultValue = "false")
	public static boolean ENABLE_FLOOD_CONNECTIONS;
	@Property(key = "gameserver.network.flood.tick", defaultValue = "1000")
	public static int Flood_Tick;
	@Property(key = "gameserver.network.flood.short.warn", defaultValue = "10")
	public static int Flood_SWARN;
	@Property(key = "gameserver.network.flood.short.reject", defaultValue = "20")
	public static int Flood_SReject;
	@Property(key = "gameserver.network.flood.short.tick", defaultValue = "10")
	public static int Flood_STick;
	@Property(key = "gameserver.network.flood.long.warn", defaultValue = "30")
	public static int Flood_LWARN;
	@Property(key = "gameserver.network.flood.long.reject", defaultValue = "60")
	public static int Flood_LReject;
	@Property(key = "gameserver.network.flood.long.tick", defaultValue = "60")
	public static int Flood_LTick;
}
