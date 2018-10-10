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

package com.aionemu.commons.network;

import java.util.Arrays;

/**
 * Utility class that is designed to check belongings of one address to the
 * range.<br>
 * This class is designed in the way that we won't need any changes to external
 * classes in case of migration to IPv6.
 * 
 * @author Taran
 * @author SoulKeeper
 */
public class IPRange {

	/**
	 * Minimal ip address of the range
	 */
	private final long min;

	/**
	 * Maximum ip address of the range
	 */
	private final long max;

	/**
	 * Address that is host for this range
	 */
	private final byte[] address;

	/**
	 * Creates new IPRange object.
	 * 
	 * @param min
	 *            minal ip address
	 * @param max
	 *            maximal ip address
	 * @param address
	 *            ip address that will be used as host for this range
	 */
	public IPRange(String min, String max, String address) {
		this.min = toLong(toByteArray(min));
		this.max = toLong(toByteArray(max));
		this.address = toByteArray(address);
	}

	/**
	 * Creates new IPRange object
	 * 
	 * @param min
	 *            minimal ip address
	 * @param max
	 *            maximal ip address
	 * @param address
	 *            ip address that will be used as host for this range
	 */
	public IPRange(byte[] min, byte[] max, byte[] address) {
		this.min = toLong(min);
		this.max = toLong(max);
		this.address = address;
	}

	/**
	 * Checks if address is in range
	 * 
	 * @param address
	 *            address to check if is in range
	 * @return true if is in range, false in other case
	 */
	public boolean isInRange(String address) {
		long addr = toLong(toByteArray(address));
		return addr >= min && addr <= max;
	}

	/**
	 * Retuns address that is assigned to this range
	 * 
	 * @return address that is assigned to this range
	 */
	public byte[] getAddress() {
		return address;
	}

	/**
	 * Returns minimal ip address of the range
	 * 
	 * @return minimal ip address of the range
	 */
	public byte[] getMinAsByteArray() {
		return toBytes(min);
	}

	/**
	 * Returns maximal ip address of the range
	 * 
	 * @return maximal ip address of the range
	 */
	public byte[] getMaxAsByteArray() {
		return toBytes(max);
	}

	/**
	 * Converts IPv4 address to long
	 * 
	 * @param bytes
	 *            byte array to convert
	 * @return long that represents address
	 */
	private static long toLong(byte[] bytes) {
		long result = 0;
		result += (bytes[3] & 0xFF);
		result += ((bytes[2] & 0xFF) << 8);
		result += ((bytes[1] & 0xFF) << 16);
		result += (bytes[0] << 24);
		return result & 0xFFFFFFFFL;
	}

	/**
	 * Converts long to byte array
	 * 
	 * @param val
	 *            long to convert
	 * @return byte array
	 */
	private static byte[] toBytes(long val) {
		byte[] result = new byte[4];
		result[3] = (byte) (val & 0xFF);
		result[2] = (byte) ((val >> 8) & 0xFF);
		result[1] = (byte) ((val >> 16) & 0xFF);
		result[0] = (byte) ((val >> 24) & 0xFF);
		return result;
	}

	/**
	 * Convers IPv4 address to byte array
	 * 
	 * @param address
	 *            String to convert
	 * @return byte array that represents string
	 */
	public static byte[] toByteArray(String address) {
		byte[] result = new byte[4];
		String[] strings = address.split("\\.");
		for (int i = 0, n = strings.length; i < n; i++) {
			result[i] = (byte) Integer.parseInt(strings[i]);
		}

		return result;
	}

	/**
	 * Equals of IPRange object. Auto-Generated.
	 * 
	 * @param o
	 *            object to compare with
	 * @return true if IPRanges are equal, false in other case
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof IPRange))
			return false;

		IPRange ipRange = (IPRange) o;
		return max == ipRange.max && min == ipRange.min && Arrays.equals(address, ipRange.address);
	}

	/**
	 * Hashcode of IPRange object. Auto generated.
	 * 
	 * @return hashcode
	 */
	@Override
	public int hashCode() {
		int result = (int) (min ^ (min >>> 32));
		result = 31 * result + (int) (max ^ (max >>> 32));
		result = 31 * result + Arrays.hashCode(address);
		return result;
	}
}
