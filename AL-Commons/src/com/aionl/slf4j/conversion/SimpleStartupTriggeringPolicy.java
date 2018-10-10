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

package com.aionl.slf4j.conversion;

import java.io.File;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;

/**
 * SimpleStartupTriggeringPolicy triggers a rollover once at startup only. This
 * is useful for preserving older logfiles during development.
 * 
 * @author Rick Beton
 */
public final class SimpleStartupTriggeringPolicy<E> extends TriggeringPolicyBase<E> {

	private boolean fired = false;

	@Override
	public boolean isTriggeringEvent(File activeFile, E event) {
		boolean result = (!this.fired) && (activeFile.length() > 0L);
		this.fired = true;
		if (result) {
			addInfo("Triggering rollover for " + activeFile);
		}
		return result;
	}
}