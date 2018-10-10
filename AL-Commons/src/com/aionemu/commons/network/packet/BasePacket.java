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

package com.aionemu.commons.network.packet;

/**
 * Basic superclass for packets.
 * <p/>
 * Created on: 29.06.2009 17:59:25
 * 
 * @author Aquanox
 */
public abstract class BasePacket {

	/**
	 * Default packet string representation pattern.
	 * 
	 * @see java.util.Formatter
	 * @see String#format(String, Object[])
	 */
	public static final String TYPE_PATTERN = "[%s] 0x%02X %s";

	/**
	 * Packet type field.
	 */
	private final PacketType packetType;

	/**
	 * Packet opcode field
	 */
	private int opcode;

	/**
	 * Constructs a new packet with specified type and id.
	 * 
	 * @param packetType
	 *            Type of packet
	 * @param opcode
	 *            Id of packet
	 */
	protected BasePacket(PacketType packetType, int opcode) {
		this.packetType = packetType;
		this.opcode = opcode;
	}

	/**
	 * Constructs a new packet with given type.<br>
	 * If this constructor is used, then setOpcode() must be used just after it.
	 * 
	 * @param packetType
	 */
	protected BasePacket(PacketType packetType) {
		this.packetType = packetType;
	}

	/**
	 * Sets opcode of this packet.<br>
	 * <font color='red'>NOTICE: </font> Use only if BasePacket(PacketType)
	 * constructor was use
	 * 
	 * @param opcode
	 */
	protected void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	/**
	 * Returns packet opcode.
	 * 
	 * @return packet id
	 */
	public final int getOpcode() {
		return opcode;
	}

	/**
	 * Returns packet type.
	 * 
	 * @return type of this packet.
	 * @see com.aionemu.commons.network.packet.BasePacket.PacketType
	 */
	public final PacketType getPacketType() {
		return packetType;
	}

	/**
	 * Returns packet name.
	 * <p/>
	 * Actually packet name is a simple name of the underlying class.
	 * 
	 * @return packet name
	 * @see Class#getSimpleName()
	 */
	public String getPacketName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Enumeration of packet types.
	 */
	public static enum PacketType {
		/** Server packet */
		SERVER("S"),

		/** Client packet */
		CLIENT("C");

		/**
		 * String representing packet type.
		 */
		private final String name;

		/**
		 * Constructor.
		 * 
		 * @param name
		 */
		private PacketType(String name) {
			this.name = name;
		}

		/**
		 * Returns packet type name.
		 * 
		 * @return packet type name.
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * Returns string representation of this packet based on packet type, opcode
	 * and name.
	 * 
	 * @return packet type string
	 * @see #TYPE_PATTERN
	 * @see java.util.Formatter
	 * @see String#format(String, Object[])
	 */
	@Override
	public String toString() {
		return String.format(TYPE_PATTERN, getPacketType().getName(), getOpcode(), getPacketName());
	}
}
