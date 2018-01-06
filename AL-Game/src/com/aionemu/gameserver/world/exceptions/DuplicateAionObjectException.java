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
 * This Exception will be thrown when some AionObject will be stored more then one time. This Exception indicating serious error.
 *
 * @author -Nemesiss-
 */
@SuppressWarnings("serial")
public class DuplicateAionObjectException extends RuntimeException {

	/**
	 * Constructs an <code>DuplicateAionObjectException</code> with no detail message.
	 */
	public DuplicateAionObjectException() {
		super();
	}

	/**
	 * Constructs an <code>DuplicateAionObjectException</code> with the specified detail message.
	 *
	 * @param s
	 *            the detail message.
	 */
	public DuplicateAionObjectException(String s) {
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
	public DuplicateAionObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates new error
	 *
	 * @param cause
	 *            reason of this exception
	 */
	public DuplicateAionObjectException(Throwable cause) {
		super(cause);
	}
}
