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
package com.aionemu.gameserver.network.aion;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;

/**
 * @author -Nemesiss-
 * @author Luno
 * @author GiGatR00n
 */
public class AionPacketHandler {

	/**
	 * logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(AionPacketHandler.class);
	private Map<Integer, AionClientPacket> packetsPrototypes = new HashMap<Integer, AionClientPacket>();

	/**
	 * Reads one packet from given ByteBuffer
	 *
	 * @param data
	 * @param client
	 * @return AionClientPacket object from binary data
	 */
	public AionClientPacket handle(ByteBuffer data, AionConnection client) {
		State state = client.getState();
		int id = data.getShort() & 0xffff;
		/* Second opcodec. */
		data.position(data.position() + 3);

		return getPacket(state, id, data, client);
	}

	public void addPacketPrototype(AionClientPacket packetPrototype) {
		packetsPrototypes.put(packetPrototype.getOpcode(), packetPrototype);
	}

	private AionClientPacket getPacket(State state, int id, ByteBuffer buf, AionConnection con) {
		AionClientPacket prototype = packetsPrototypes.get(id);

		if (prototype == null) {
			unknownPacket(state, id, buf);
			return null;
		}

		/**
		 * Display Packets Name + Hex-Bytes in Chat Window
		 */
		Player player = con.getActivePlayer();

		if (con.getState().equals(State.IN_GAME) && player != null && player.getAccessLevel() >= DeveloperConfig.SHOW_PACKETS_INCHAT_ACCESSLEVEL) {
			if (isPacketFilterd(DeveloperConfig.FILTERED_PACKETS_INCHAT, prototype.getPacketName())) {
				if (DeveloperConfig.SHOW_PACKET_BYTES_INCHAT) {
					String PckName = String.format("0x%04X : %s", id, prototype.getPacketName());
					PacketSendUtility.sendMessage(player, "********************************************");
					PacketSendUtility.sendMessage(player, PckName);
					PacketSendUtility.sendMessage(player, Util.toHexStream(getByteBuffer(buf, DeveloperConfig.TOTAL_PACKET_BYTES_INCHAT)));
					buf.position(5);

				}
				else if (DeveloperConfig.SHOW_PACKET_NAMES_INCHAT) {
					String PckName = String.format("0x%04X : %s", id, prototype.getPacketName());
					PacketSendUtility.sendMessage(player, PckName);
				}
			}
		}
		AionClientPacket res = prototype.clonePacket();
		res.setBuffer(buf);
		res.setConnection(con);

		if (con.getState().equals(State.IN_GAME) && con.getActivePlayer().getPlayerAccount().getMembership() == 10) {
			PacketSendUtility.sendMessage(con.getActivePlayer(), "0x" + Integer.toHexString(res.getOpcode()).toUpperCase() + " : " + res.getPacketName());
		}
		return res;
	}

	private boolean isPacketFilterd(String filterlist, String PacketName) {

		// If FilterList was empty, all packets will be shown.
		if (filterlist == null || filterlist.equalsIgnoreCase("*"))
			return true;

		String[] Parts = null;
		Parts = filterlist.trim().split(",");

		for (String p : Parts) {
			if (p.trim().equalsIgnoreCase(PacketName)) {
				return true;
			}
		}
		return false;
	}

	private ByteBuffer getByteBuffer(ByteBuffer buf, int count) {

		count = (count <= buf.capacity()) ? count : buf.capacity();
		ByteBuffer tmpBuffer = buf.asReadOnlyBuffer();
		tmpBuffer.position(5);
		tmpBuffer.limit(count);

		// Create an empty ByteBuffer with a Requested Capacity.
		ByteBuffer PckBuffer = ByteBuffer.allocate(count);
		try {
			do {
				PckBuffer.put(tmpBuffer.get());
			}
			while (tmpBuffer.remaining() > 0);
		}
		catch (Exception e) {
			// e.printStackTrace();
		}
		PckBuffer.position(0);
		return PckBuffer;
	}

	/**
	 * Logs unknown packet.
	 *
	 * @param state
	 * @param id
	 * @param data
	 */
	private void unknownPacket(State state, int id, ByteBuffer data) {
		if (NetworkConfig.DISPLAY_UNKNOWNPACKETS) {
			log.warn(String.format("Unknown packet received from Aion client: 0x%04X, state=%s %n%s", id, state.toString(), Util.toHex(data)));
		}
	}
}
