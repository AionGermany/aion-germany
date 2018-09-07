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
package com.aionemu.gameserver.services.rvrservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.rvr.RvrLocation;
import com.aionemu.gameserver.model.rvr.RvrStateType;
import com.aionemu.gameserver.services.RvrService;

public abstract class Rvrlf3df3<RL extends RvrLocation> {

	private boolean started;
	private final RL rvrLocation;

	protected abstract void stopRvr();

	protected abstract void startRvr();

	private final AtomicBoolean finished = new AtomicBoolean();

	public Rvrlf3df3(RL rvrLocation) {
		this.rvrLocation = rvrLocation;
	}

	public final void start() {
		boolean doubleStart = false;
		synchronized (this) {
			if (started) {
				doubleStart = true;
			}
			else {
				started = true;
			}
		}
		if (doubleStart) {
			return;
		}
		startRvr();
	}

	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopRvr();
		}
	}

	protected void spawn(RvrStateType type) {
		RvrService.getInstance().spawn(getRvrLocation(), type);
	}

	protected void despawn() {
		RvrService.getInstance().despawn(getRvrLocation());
	}

	public boolean isFinished() {
		return finished.get();
	}

	public RL getRvrLocation() {
		return rvrLocation;
	}

	public int getRvrLocationId() {
		return rvrLocation.getId();
	}
}
