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
package com.aionemu.gameserver.services.abysslandingservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.AbyssLandingDAO;
import com.aionemu.gameserver.model.landing.LandingLocation;
import com.aionemu.gameserver.model.landing.LandingStateType;
import com.aionemu.gameserver.services.AbyssLandingService;

public abstract class Landing<RL extends LandingLocation> {

	private int level;
	private boolean started;
	private final RL landingLocation;

	protected abstract void stopLanding();

	protected abstract void saveLanding();

	protected abstract void startLanding(int level);

	private final AtomicBoolean closed = new AtomicBoolean();

	public Landing(RL landingLocation) {
		this.landingLocation = landingLocation;
	}

	public final void start(int level) {
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
		startLanding(level);
	}

	public final void stop() {
		stopLanding();
	}

	public final void update() {
		saveLanding();
	}

	protected void spawn(LandingStateType type) {
		AbyssLandingService.spawn(getLandingLocation(), type);
	}

	protected void despawn() {
		AbyssLandingService.despawn(getLandingLocation());
	}

	public boolean isClosed() {
		return closed.get();
	}

	public RL getLandingLocation() {
		return landingLocation;
	}

	public int getLandingLocationId() {
		return landingLocation.getId();
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@SuppressWarnings("unused")
	private AbyssLandingDAO getDAO() {
		return DAOManager.getDAO(AbyssLandingDAO.class);
	}
}
