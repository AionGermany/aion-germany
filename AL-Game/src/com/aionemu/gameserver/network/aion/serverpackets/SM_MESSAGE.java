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

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Massage [chat, etc]
 *
 * @author -Nemesiss-, Sweetkr
 */
public class SM_MESSAGE extends AionServerPacket {

	/**
	 * Player.
	 */
	private Player player;
	/**
	 * Object that is saying smth or null.
	 */
	private int senderObjectId;
	/**
	 * Message.
	 */
	private String message;
	/**
	 * Name of the sender
	 */
	private String senderName;
	/**
	 * Sender race
	 */
	private Race race;
	/**
	 * Chat type
	 */
	private ChatType chatType;
	/**
	 * Sender coordinates
	 */
	private float x;
	private float y;
	private float z;

	/**
	 * Constructs new <tt>SM_MESSAGE </tt> packet
	 *
	 * @param player
	 *            who sent message
	 * @param message
	 *            actual message
	 * @param chatType
	 *            what chat type should be used
	 */
	public SM_MESSAGE(Player player, String message, ChatType chatType) {
		this.player = player;
		this.senderObjectId = player.getObjectId();
		this.senderName = player.getName();
		this.message = message;
		this.race = player.getRace();
		this.chatType = chatType;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
	}

	/**
	 * Manual creation of chat message.<br>
	 *
	 * @param senderObjectId
	 *            - can be 0 if system message(like announcements)
	 * @param senderName
	 *            - used for shout ATM, can be null in other cases
	 * @param message
	 *            - actual text
	 * @param chatType
	 *            type of chat, Normal, Shout, Announcements, Etc...
	 */
	public SM_MESSAGE(int senderObjectId, String senderName, String message, ChatType chatType) {
		this.senderObjectId = senderObjectId;
		this.senderName = senderName;
		this.message = message;
		this.chatType = chatType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		boolean canRead = true;

		if (race != null) {
			canRead = chatType.isSysMsg() || CustomConfig.SPEAKING_BETWEEN_FACTIONS || player.getAccessLevel() > 0 || (con.getActivePlayer() != null && con.getActivePlayer().getAccessLevel() > 0);
		}

		writeC(chatType.toInteger()); // type

		/*
		 * 0 : all 1 : elyos 2 : asmodians
		 */
		writeC(canRead ? 0 : race.getRaceId() + 1);
		writeD(senderObjectId); // sender object id

		switch (chatType) {
			case NORMAL:
			case WHITE:
			case YELLOW:
			case BRIGHT_YELLOW:
			case WHITE_CENTER:
			case YELLOW_CENTER:
			case BRIGHT_YELLOW_CENTER:
			case BRIGHT_YELLOW_CENTER_NEW:
				writeH(0x00); // unknown
				writeS(message);
				break;
			case SHOUT:
				writeS(senderName);
				writeS(message);
				writeF(x);
				writeF(y);
				writeF(z);
				break;
			case ALLIANCE:
			case GROUP:
			case GROUP_LEADER:
			case LEGION:
			case WHISPER:
			case LEAGUE:
			case LEAGUE_ALERT:
			case CH1:
			case CH2:
			case CH3:
			case CH4:
			case CH5:
			case CH6:
			case CH7:
			case CH8:
			case CH9:
			case CH10:
			case COMMAND:
			case UNION_WAR:
			case GMRESPONSE:
				writeS(senderName);
				writeS(message);
				break;
		default:
			break;
		}
	}
}
