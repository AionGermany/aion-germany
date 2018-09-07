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
package com.aionemu.gameserver.world.exceptions;

/**
 * This Exception will be thrown when some object is referencing to World map that do not exist. This Exception indicating serious error.
 *
 * @author -Nemesiss-
 */
@SuppressWarnings("serial")
public class WorldMapNotExistException extends RuntimeException {

	/**
	 * Constructs an <code>WorldMapNotExistException</code> with no detail message.
	 */
	public WorldMapNotExistException() {
		super();
	}

	/**
	 * Constructs an <code>WorldMapNotExistException</code> with the specified detail message.
	 *
	 * @param s
	 *            the detail message.
	 */
	public WorldMapNotExistException(String s) {
		super(s);
	}
}
