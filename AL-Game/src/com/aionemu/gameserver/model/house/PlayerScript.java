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
package com.aionemu.gameserver.model.house;

import com.aionemu.commons.taskmanager.AbstractLockManager;

/**
 * @author Rolandas Use readLock + readUnlock to read both fields, because they may change !!!
 */
public final class PlayerScript extends AbstractLockManager {

	/**
	 * Default constructor, bytes null, uncompressed size -1
	 */
	public PlayerScript() {
	}

	public PlayerScript(byte[] compressedBytes, int uncompressedSize) {
		this.compressedBytes = compressedBytes;
		this.uncompressedSize = uncompressedSize;
	}

	private int uncompressedSize = -1;
	private byte[] compressedBytes = null;

	public int getUncompressedSize() {
		return uncompressedSize;
	}

	public byte[] getCompressedBytes() {
		return compressedBytes;
	}

	public void setData(byte[] compressedBytes, int uncompressedSize) {
		writeLock();
		this.compressedBytes = compressedBytes;
		this.uncompressedSize = uncompressedSize;
		writeUnlock();
	}
}
