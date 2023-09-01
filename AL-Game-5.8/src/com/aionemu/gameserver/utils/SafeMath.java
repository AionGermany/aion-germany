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
package com.aionemu.gameserver.utils;

/**
 * @author MrPoke
 */
public class SafeMath {

	public static int addSafe(int source, int value) throws OverfowException {
		long s = (long) source + (long) value;
		if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
			throw new OverfowException(source + " + " + value + " = " + ((long) source + (long) value));
		}
		return (int) s;
	}

	public static long addSafe(long source, long value) throws OverfowException {
		if ((source > 0 && value > Long.MAX_VALUE - source) || (source < 0 && value < Long.MIN_VALUE - source)) {
			throw new OverfowException(source + " + " + value + " = " + (source + value));
		}
		return source + value;
	}

	public static int multSafe(int source, int value) throws OverfowException {
		long m = ((long) source) * ((long) value);
		if (m < Integer.MIN_VALUE || m > Integer.MAX_VALUE) {
			throw new OverfowException(source + " * " + value + " = " + ((long) source * (long) value));
		}
		return (int) m;
	}

	public static long multSafe(long a, long b) throws OverfowException {

		long ret;
		String msg = "overflow: multiply";
		if (a > b) {
			// use symmetry to reduce boundry cases
			ret = multSafe(b, a);
		}
		else {
			if (a < 0) {
				if (b < 0) {
					// check for positive overflow with negative a, negative b
					if (a >= Long.MAX_VALUE / b) {
						ret = a * b;
					}
					else {
						throw new OverfowException(msg);
					}
				}
				else if (b > 0) {
					// check for negative overflow with negative a, positive b
					if (Long.MIN_VALUE / b <= a) {
						ret = a * b;
					}
					else {
						throw new OverfowException(msg);

					}
				}
				else {
					ret = 0;
				}
			}
			else if (a > 0) {
				// check for positive overflow with positive a, positive b
				if (a <= Long.MAX_VALUE / b) {
					ret = a * b;
				}
				else {
					throw new OverfowException(msg);
				}
			}
			else {
				ret = 0;
			}
		}
		return ret;
	}
}
