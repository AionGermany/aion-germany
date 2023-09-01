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

/**
 * @author Alex
 */
public class IPv4 {

	public static String getIP(int ip) {
		byte[] s = new byte[] { (byte) ip, (byte) (ip >>> 8), (byte) (ip >>> 16), (byte) (ip >>> 24) };
		return (s[0] & 0xFF) + "." + (s[1] & 0xFF) + "." + (s[2] & 0xFF) + "." + (s[3] & 0xFF);
	}
}
