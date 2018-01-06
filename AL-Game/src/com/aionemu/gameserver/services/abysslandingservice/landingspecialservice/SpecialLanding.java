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
package com.aionemu.gameserver.services.abysslandingservice.landingspecialservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.landing_special.LandingSpecialLocation;
import com.aionemu.gameserver.model.landing_special.LandingSpecialStateType;
import com.aionemu.gameserver.services.AbyssLandingSpecialService;

public abstract class SpecialLanding<RL extends LandingSpecialLocation> {

	private boolean started;
	private final RL spacialLandingLocation;
	private LandingSpecialStateType type;

	protected abstract void stopLanding();

	protected abstract void startLanding();

	private final AtomicBoolean closed = new AtomicBoolean();

	public SpecialLanding(RL specialLandingLocation) {
		this.spacialLandingLocation = specialLandingLocation;
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
		startLanding();
	}

	public final void stop() {
		stopLanding();
	}

	protected void spawn(LandingSpecialStateType type) {
		AbyssLandingSpecialService.spawn(getSpecialLandingLocation(), type);
	}

	protected void despawn() {
		AbyssLandingSpecialService.despawn(getSpecialLandingLocation());
	}

	public boolean isClosed() {
		return closed.get();
	}

	public RL getSpecialLandingLocation() {
		return spacialLandingLocation;
	}

	public int getSpecialLandingLocationId() {
		return spacialLandingLocation.getId();
	}

	public LandingSpecialStateType getType() {
		return this.type;
	}

	public void setType(LandingSpecialStateType tp) {
		this.type = tp;
	}
}
