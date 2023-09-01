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
 * This exception will be thrown when object without set position will be spawned or despawned. This exception indicating error when coder forget to set position but is spawning or despawning object.
 *
 * @author -Nemesiss-
 */
@SuppressWarnings("serial")
public class NotSetPositionException extends RuntimeException {

	/**
	 * Constructs an <code>NotSetPositionException</code> with no detail message.
	 */
	public NotSetPositionException() {
		super();
	}

	/**
	 * Constructs an <code>NotSetPositionException</code> with the specified detail message.
	 *
	 * @param s
	 *            the detail message.
	 */
	public NotSetPositionException(String s) {
		super(s);
	}

	/**
	 * Creates new error
	 *
	 * @param message
	 *            exception description
	 * @param cause
	 *            reason of this exception
	 */
	public NotSetPositionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates new error
	 *
	 * @param cause
	 *            reason of this exception
	 */
	public NotSetPositionException(Throwable cause) {
		super(cause);
	}
}
