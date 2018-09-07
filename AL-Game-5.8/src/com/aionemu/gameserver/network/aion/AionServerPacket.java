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
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.packet.BaseServerPacket;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.Crypt;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;

/**
 * Base class for every GS -> Aion Server Packet.
 *
 * @author -Nemesiss-
 * @author GiGatR00n
 */
public abstract class AionServerPacket extends BaseServerPacket {

	// private static final Logger log = LoggerFactory.getLogger(AionServerPacket.class);

	/**
	 * Constructs new server packet
	 */
	protected AionServerPacket() {
		super();
		setOpcode(ServerPacketsOpcodes.getOpcode(getClass()));
	}

	/**
	 * Write packet opcodec and two additional bytes
	 *
	 * @param buf
	 * @param value
	 */
	private final void writeOP(int value) {
		/**
		 * obfuscate packet id
		 */
		int op = Crypt.encodeOpcodec(value);
		buf.putShort((short) (op));
		/**
		 * put static server packet code
		 */
		buf.put(Crypt.staticServerPacketCode);

		/**
		 * for checksum?
		 */
		buf.putShort((short) (~op));
	}

	public final void write(AionConnection con) {
		write(con, buf);
	}

	/**
	 * Write data that this packet represents to given byte buffer.
	 *
	 * @param con
	 * @param buf
	 */
	protected void writeImpl(AionConnection con) {
	}

	public final ByteBuffer getBuf() {
		return this.buf;
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
		tmpBuffer.position(5 + 2);
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
	 * Write and encrypt this packet data for given connection, to given buffer.
	 *
	 * @param con
	 * @param buf
	 */
	public final void write(AionConnection con, ByteBuffer buffer) {
		if (con.getState().equals(AionConnection.State.IN_GAME) && con.getActivePlayer().getPlayerAccount().getMembership() == 10) {
			if (!this.getPacketName().equals("SM_MESSAGE")) {
				PacketSendUtility.sendMessage(con.getActivePlayer(), "0x" + Integer.toHexString(this.getOpcode()).toUpperCase() + " : " + this.getPacketName());
			}
		}

		this.setBuf(buffer);
		buf.putShort((short) 0);
		writeOP(getOpcode());
		writeImpl(con);
		buf.flip();
		/**
		 * Display Packets Name + Hex-Bytes in Chat Window
		 */
		int BufCurrentPos = buf.position();
		buf.position(5 + 2);

		Player player = con.getActivePlayer();

		if (con.getState().equals(State.IN_GAME) && player != null && this.getOpcode() != 24 && player.getAccessLevel() >= DeveloperConfig.SHOW_PACKETS_INCHAT_ACCESSLEVEL) {
			if (isPacketFilterd(DeveloperConfig.FILTERED_PACKETS_INCHAT, this.getPacketName())) {
				if (DeveloperConfig.SHOW_PACKET_BYTES_INCHAT) {
					String PckName = String.format("0x%04X : %s", this.getOpcode(), this.getPacketName());
					PacketSendUtility.sendMessage(player, "********************************************");
					PacketSendUtility.sendMessage(player, PckName);
					PacketSendUtility.sendMessage(player, Util.toHexStream(getByteBuffer(buf, DeveloperConfig.TOTAL_PACKET_BYTES_INCHAT)));

				}
				else if (DeveloperConfig.SHOW_PACKET_NAMES_INCHAT) {
					String PckName = String.format("0x%04X : %s", this.getOpcode(), this.getPacketName());
					PacketSendUtility.sendMessage(player, PckName);
				}
			}
		}
		buf.position(BufCurrentPos);

		buf.putShort((short) buf.limit());
		ByteBuffer b = buf.slice();
		buf.position(0);
		con.encrypt(b);
	}

	/**
	 * Write String to buffer
	 *
	 * @param text
	 * @param size
	 */
	protected final void writeS(String text, int size) {
		if (text == null) {
			buf.put(new byte[size]);
		}
		else {
			final int len = text.length();
			for (int i = 0; i < len; i++) {
				buf.putChar(text.charAt(i));
			}
			buf.put(new byte[size - (len * 2)]);
		}
	}

	protected void writeNameId(int nameId) {
		writeH(0x24);
		writeD(nameId);
		writeH(0x00);
	}
}
