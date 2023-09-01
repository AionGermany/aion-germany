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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Replies to a request to add or delete a friend
 *
 * @author Ben
 */
public class SM_FRIEND_RESPONSE extends AionServerPacket {

	/**
	 * The friend was successfully added to your list
	 */
	public static final int TARGET_ADDED = 0x00;
	/**
	 * The target of a friend request is offline
	 */
	public static final int TARGET_OFFLINE = 0x01;
	/**
	 * The target is already a friend
	 */
	public static final int TARGET_ALREADY_FRIEND = 0x02;
	/**
	 * The target does not exist
	 */
	public static final int TARGET_NOT_FOUND = 0x03;
	/**
	 * The friend denied your request to add him
	 */
	public static final int TARGET_DENIED = 0x04;
	/**
	 * The target's friend list is full
	 */
	public static final int TARGET_LIST_FULL = 0x05;
	/**
	 * The friend was removed from your list
	 */
	public static final int TARGET_REMOVED = 0x06;
	/**
	 * The target is in your blocked list, and cannot be added to your friends list.
	 */
	public static final int TARGET_BLOCKED = 0x08;
	/**
	 * The target is dead and cannot be befriended yet.
	 */
	public static final int TARGET_DEAD = 0x09;
	/**
	 * The target become a note.
	 */
	public static final int TARGET_NOTE = 0x21;
	private final String player;
	private final int code;

	public SM_FRIEND_RESPONSE(String playerName, int messageType) {
		player = playerName;
		code = messageType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeS(player);
		writeC(code);
	}
}
