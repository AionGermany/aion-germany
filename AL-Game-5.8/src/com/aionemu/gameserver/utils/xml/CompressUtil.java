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
package com.aionemu.gameserver.utils.xml;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author Rolandas
 */
public final class CompressUtil {

	public static String Decompress(byte[] bytes) throws Exception {
		Inflater decompressor = new Inflater();
		decompressor.setInput(bytes);

		// Create an expandable byte array to hold the decompressed data
		ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);

		byte[] buffer = new byte[1024];
		try {
			while (true) {
				int count = decompressor.inflate(buffer);
				if (count > 0) {
					bos.write(buffer, 0, count);
				}
				else if (count == 0 && decompressor.finished()) {
					break;
				}
				else {
					throw new RuntimeException("Bad zip data, size: " + bytes.length);
				}
			}
		}
		finally {
			decompressor.end();
		}

		bos.close();
		return bos.toString("UTF-16LE");
	}

	public static byte[] Compress(String text) throws Exception {
		Deflater compressor = new Deflater();
		byte[] bytes = text.getBytes("UTF-16LE");
		compressor.setInput(bytes);

		// Create an expandable byte array to hold the compressed data
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		compressor.finish();

		byte[] buffer = new byte[1024];
		try {
			while (!compressor.finished()) {
				int count = compressor.deflate(buffer);
				bos.write(buffer, 0, count);
			}
		}
		finally {
			compressor.finish();
		}

		bos.close();
		return bos.toByteArray();
	}
}
