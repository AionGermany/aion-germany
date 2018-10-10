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

package com.aionemu.commons.services.cron;

public class CronServiceException extends RuntimeException {

	private static final long serialVersionUID = -354186843536711803L;

	public CronServiceException() {
	}

	public CronServiceException(String message) {
		super(message);
	}

	public CronServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CronServiceException(Throwable cause) {
		super(cause);
	}
}
