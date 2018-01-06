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
package com.aionemu.gameserver.geoEngine.scene.mesh;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author lex
 */
public class IndexByteBuffer extends IndexBuffer {

	private ByteBuffer buf;

	public IndexByteBuffer(ByteBuffer buffer) {
		this.buf = buffer;
	}

	@Override
	public int get(int i) {
		return buf.get(i) & 0x000000FF;
	}

	@Override
	public void put(int i, int value) {
		buf.put(i, (byte) value);
	}

	@Override
	public int size() {
		return buf.limit();
	}

	@Override
	public Buffer getBuffer() {
		return buf;
	}
}
