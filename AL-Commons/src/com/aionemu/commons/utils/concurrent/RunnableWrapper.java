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

package com.aionemu.commons.utils.concurrent;

/**
 * @author -Nemesiss-
 */
public class RunnableWrapper implements Runnable {

	private final Runnable runnable;
	private final long maxRuntimeMsWithoutWarning;

	public RunnableWrapper(Runnable runnable) {
		this(runnable, Long.MAX_VALUE);
	}

	public RunnableWrapper(Runnable runnable, long maxRuntimeMsWithoutWarning) {
		this.runnable = runnable;
		this.maxRuntimeMsWithoutWarning = maxRuntimeMsWithoutWarning;
	}

	@Override
	public final void run() {
		ExecuteWrapper.execute(runnable, maxRuntimeMsWithoutWarning);
	}
}
