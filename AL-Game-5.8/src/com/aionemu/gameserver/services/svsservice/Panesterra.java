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
package com.aionemu.gameserver.services.svsservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.svs.SvsLocation;
import com.aionemu.gameserver.model.svs.SvsStateType;
import com.aionemu.gameserver.services.SvsService;

public abstract class Panesterra<PL extends SvsLocation> {

	private boolean started;
	private final PL svsLocation;

	protected abstract void stopSvs();

	protected abstract void startSvs();

	private final AtomicBoolean finished = new AtomicBoolean();

	public Panesterra(PL svsLocation) {
		this.svsLocation = svsLocation;
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
		startSvs();
	}

	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopSvs();
		}
	}

	protected void spawn(SvsStateType type) {
		SvsService.getInstance().spawn(getSvsLocation(), type);
	}

	protected void despawn() {
		SvsService.getInstance().despawn(getSvsLocation());
	}

	public boolean isFinished() {
		return finished.get();
	}

	public PL getSvsLocation() {
		return svsLocation;
	}

	public int getSvsLocationId() {
		return svsLocation.getId();
	}
}
