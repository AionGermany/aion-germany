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
package com.aionemu.gameserver.services.dynamicportal;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.dynamicportal.DynamicPortalLocation;
import com.aionemu.gameserver.model.dynamicportal.DynamicPortalStateType;
import com.aionemu.gameserver.services.DynamicPortalService;

/**
 * @author Falke_34
 */
public abstract class DynamicPortal<DL extends DynamicPortalLocation> {

	private boolean started;
	private final DL dynamicPortalLocation;
	protected abstract void stopDynamicPortal();
	protected abstract void startDynamicPortal();
	private final AtomicBoolean closed = new AtomicBoolean();
	
	public DynamicPortal(DL dynamicPortalLocation) {
		this.dynamicPortalLocation = dynamicPortalLocation;
	}
	
	public final void start() {
		boolean doubleStart = false;
		synchronized (this) {
			if (started) {
				doubleStart = true;
			} else {
				started = true;
			}
		} if (doubleStart) {
			return;
		}
		startDynamicPortal();
	}
	
	public final void stop() {
		if (closed.compareAndSet(false, true)) {
			stopDynamicPortal();
		}
	}
	
	protected void spawn(DynamicPortalStateType type) {
		DynamicPortalService.getInstance().spawn(getDynamicPortalLocation(), type);
	}
	
	protected void despawn() {
		DynamicPortalService.getInstance().despawn(getDynamicPortalLocation());
	}
	
	public boolean isClosed() {
		return closed.get();
	}
	
	public DL getDynamicPortalLocation() {
		return dynamicPortalLocation;
	}
	
	public int getDynamicPortalLocationId() {
		return dynamicPortalLocation.getId();
	}
}
