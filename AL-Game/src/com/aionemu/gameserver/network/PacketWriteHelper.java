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
package com.aionemu.gameserver.network;

import java.nio.ByteBuffer;

/**
 * @author -Nemesiss-
 */
public abstract class PacketWriteHelper {

	protected abstract void writeMe(ByteBuffer buf);

	/**
	 * Write int to buffer.
	 *
	 * @param buf
	 * @param value
	 */
	protected final void writeD(ByteBuffer buf, int value) {
		buf.putInt(value);
	}

	/**
	 * Write short to buffer.
	 *
	 * @param buf
	 * @param value
	 */
	protected final void writeH(ByteBuffer buf, int value) {
		buf.putShort((short) value);
	}

	/**
	 * Write byte to buffer.
	 *
	 * @param buf
	 * @param value
	 */
	protected final void writeC(ByteBuffer buf, int value) {
		buf.put((byte) value);
	}

	/**
	 * Write double to buffer.
	 *
	 * @param buf
	 * @param value
	 */
	protected final void writeDF(ByteBuffer buf, double value) {
		buf.putDouble(value);
	}

	/**
	 * Write float to buffer.
	 *
	 * @param buf
	 * @param value
	 */
	protected final void writeF(ByteBuffer buf, float value) {
		buf.putFloat(value);
	}

	/**
	 * Write long to buffer.
	 *
	 * @param buf
	 * @param value
	 */
	protected final void writeQ(ByteBuffer buf, long value) {
		buf.putLong(value);
	}

	/**
	 * Write String to buffer
	 *
	 * @param buf
	 * @param text
	 */
	protected final void writeS(ByteBuffer buf, String text) {
		if (text == null) {
			buf.putChar('\000');
		}
		else {
			final int len = text.length();
			for (int i = 0; i < len; i++) {
				buf.putChar(text.charAt(i));
			}
			buf.putChar('\000');
		}
	}

	/**
	 * Write byte array to buffer.
	 *
	 * @param buf
	 * @param data
	 */
	protected final void writeB(ByteBuffer buf, byte[] data) {
		buf.put(data);
	}

	/**
	 * Skip specified amount of bytes
	 *
	 * @param buf
	 * @param bytes
	 */
	protected final void skip(ByteBuffer buf, int bytes) {
		buf.put(new byte[bytes]);
	}
}
