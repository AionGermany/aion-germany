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
package com.aionemu.gameserver.utils;

import com.aionemu.commons.objects.filter.ObjectFilter;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.SiegeZoneInstance;

/**
 * This class contains static methods, which are utility methods, all of them are interacting only with objects passed as parameters.<br>
 * These methods could be placed directly into Player class, but we want to keep Player class as a pure data holder.<br>
 *
 * @author Luno
 */
public class PacketSendUtility {

	/**
	 * Global message sending
	 */
	public static void sendMessage(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW));
	}

	public static void sendWhiteMessage(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE));
	}

	public static void sendWhiteMessageOnCenter(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE_CENTER));
	}

	public static void sendYellowMessage(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW));
	}

	public static void sendYellowMessageOnCenter(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW_CENTER));
	}

	public static void sendBrightYellowMessage(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.BRIGHT_YELLOW));
	}

	public static void sendBrightYellowMessageOnCenter(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.BRIGHT_YELLOW_CENTER));
	}

	public static void sendSys1Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.GROUP_LEADER));
	}

	public static void sendSys2Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE));
	}

	public static void sendSys3Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.COMMAND));
	}

	public static void sendWarnMessageOnCenter(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.LEAGUE_ALERT));
	}

	/**
	 * Send packet to this player
	 */
	public static void sendPacket(Player player, AionServerPacket packet) {
		if (player.getClientConnection() != null) {
			player.getClientConnection().sendPacket(packet);
		}
	}

	/**
	 * Player Send Packet
	 */
	public static void playerSendPacketTime(final Player player, final AionServerPacket packet, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (player.getClientConnection() != null) {
					player.getClientConnection().sendPacket(packet);
				}
			}
		}, time);
	}

	/**
	 * Npc Send Packet
	 */
	public static void npcSendPacketTime(final Npc npc, final AionServerPacket packet, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				npc.getKnownList().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, packet);
						}
					}
				});
			}
		}, time);
	}

	/**
	 * Broadcast packet to all visible players.
	 *
	 * @param player
	 * @param packet
	 *            ServerPacket that will be broadcast
	 * @param toSelf
	 *            true if packet should also be sent to this player
	 */
	public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf) {
		if (toSelf) {
			sendPacket(player, packet);
		}

		broadcastPacket(player, packet);
	}

	/**
	 * Broadcast packet to all visible players.
	 *
	 * @param visibleObject
	 * @param packet
	 */
	public static void broadcastPacketAndReceive(VisibleObject visibleObject, AionServerPacket packet) {
		if (visibleObject instanceof Player) {
			sendPacket((Player) visibleObject, packet);
		}

		broadcastPacket(visibleObject, packet);
	}

	/**
	 * Broadcast packet to all Players from knownList of the given visible object.
	 *
	 * @param visibleObject
	 * @param packet
	 */
	public static void broadcastPacket(VisibleObject visibleObject, final AionServerPacket packet) {
		visibleObject.getKnownList().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					sendPacket(player, packet);
				}
			}
		});
	}

	/**
	 * Broadcasts packet to all visible players matching a filter
	 *
	 * @param player
	 * @param packet
	 *            ServerPacket to be broadcast
	 * @param toSelf
	 *            true if packet should also be sent to this player
	 * @param filter
	 *            filter determining who should be messaged
	 */
	public static void broadcastPacket(Player player, final AionServerPacket packet, boolean toSelf, final ObjectFilter<Player> filter) {
		if (toSelf) {
			sendPacket(player, packet);
		}

		player.getKnownList().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player object) {
				if (filter.acceptObject(object)) {
					sendPacket(object, packet);
				}
			}
		});
	}

	/**
	 * Broadcasts packet to all Players from knownList of the given visible object within the specified distance in meters
	 *
	 * @param visibleObject
	 * @param packet
	 * @param distance
	 */
	public static void broadcastPacket(final VisibleObject visibleObject, final AionServerPacket packet, final int distance) {
		visibleObject.getKnownList().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player p) {
				if (MathUtil.isIn3dRange(visibleObject, p, distance)) {
					sendPacket(p, packet);
				}
			}
		});
	}

	/**
	 * Broadcasts packet to ALL players matching a filter
	 *
	 * @param player
	 * @param packet
	 *            ServerPacket to be broadcast
	 * @param filter
	 *            filter determining who should be messaged
	 */
	public static void broadcastFilteredPacket(final AionServerPacket packet, final ObjectFilter<Player> filter) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player object) {
				if (filter.acceptObject(object)) {
					sendPacket(object, packet);
				}
			}
		});
	}

	/**
	 * Broadcasts packet to all legion members of a legion
	 *
	 * @param legion
	 *            Legion to broadcast packet to
	 * @param packet
	 *            ServerPacket to be broadcast
	 */
	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet) {
		for (Player onlineLegionMember : legion.getOnlineLegionMembers()) {
			sendPacket(onlineLegionMember, packet);
		}
	}

	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet, int playerObjId) {
		for (Player onlineLegionMember : legion.getOnlineLegionMembers()) {
			if (onlineLegionMember.getObjectId() != playerObjId) {
				sendPacket(onlineLegionMember, packet);
			}
		}
	}

	public static void broadcastPacketToZone(SiegeZoneInstance zone, final AionServerPacket packet) {
		zone.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				sendPacket(player, packet);

			}
		});
	}

	public static void broadcastPacketToGroup(Player player, AionServerPacket packet, boolean toSelf) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group == null) {
			return;
		}
		for (Player member : group.getMembers()) {
			if (member == player && !toSelf) {
				continue;
			}
			sendPacket(member, packet);
		}
	}

	public static void broadcastPacketToAlliance(Player player, AionServerPacket packet, boolean toSelf) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group == null) {
			return;
		}
		for (Player member : group.getMembers()) {
			if (member == player && !toSelf) {
				continue;
			}
			sendPacket(member, packet);
		}
	}

	/**
	 * Special function for Aion Absolute Announce :)
	 */
	/**
	 * Crazy Daeva
	 */
	public static void event(final String msg) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player sender) {
				sendBrightYellowMessageOnCenter(sender, "[EVENT ALL ON A DAEVA]: " + msg);
			}
		});
	}

	public static void event2(final String msg) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player sender) {
				broadcastPacket(sender, new SM_MESSAGE(sender, "[EVENT]: " + msg, ChatType.GROUP_LEADER), true);
			}
		});
	}
}
