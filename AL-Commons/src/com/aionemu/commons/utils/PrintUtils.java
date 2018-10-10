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

package com.aionemu.commons.utils;

import java.nio.ByteBuffer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class PrintUtils {

	public static void printSection(final String sectionName) {
		final StringBuilder sb = new StringBuilder();
		sb.append("-[ " + sectionName + " ]");
		while (sb.length() < 79) {
			sb.insert(0, "=");
		}
		System.out.println(sb.toString());
	}

	public static byte[] hex2bytes(final String string) {
		final String finalString = string.replaceAll("\\s+", "");
		final byte[] bytes = new byte[finalString.length() / 2];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = (byte) Integer.parseInt(finalString.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	public static String bytes2hex(final byte[] bytes) {
		final StringBuilder result = new StringBuilder();
		for (final byte b : bytes) {
			final int value = b & 0xFF;
			result.append(String.format("%02X", value));
		}
		return result.toString();
	}

	public static String reverseHex(final String input) {
		final String[] chunked = new String[input.length() / 2];
		int position = 0;
		for (int i = 0; i < input.length(); i += 2) {
			chunked[position] = input.substring(position * 2, position * 2 + 2);
			++position;
		}
		ArrayUtils.reverse((Object[]) chunked);
		return StringUtils.join((Object[]) chunked);
	}

	public static String toHex(final ByteBuffer data) {
		final int position = data.position();
		final StringBuilder result = new StringBuilder();
		int counter = 0;
		while (data.hasRemaining()) {
			if (counter % 16 == 0) {
				result.append(String.format("%04X: ", counter));
			}
			final int b = data.get() & 0xFF;
			result.append(String.format("%02X ", b));
			if (++counter % 16 == 0) {
				result.append("  ");
				toText(data, result, 16);
				result.append("\n");
			}
		}
		final int rest = counter % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; ++i) {
				result.append("   ");
			}
			toText(data, result, rest);
		}
		data.position(position);
		return result.toString();
	}

	private static void toText(final ByteBuffer data, final StringBuilder result, final int cnt) {
		int charPos = data.position() - cnt;
		for (int a = 0; a < cnt; ++a) {
			final int c = data.get(charPos++);
			if (c > 31 && c < 128) {
				result.append((char) c);
			} else {
				result.append('.');
			}
		}
	}
}
