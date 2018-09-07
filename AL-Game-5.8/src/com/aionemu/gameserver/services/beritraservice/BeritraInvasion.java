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
package com.aionemu.gameserver.services.beritraservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.beritra.BeritraStateType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.services.BeritraService;

/**
 * @author Cx3
 */
public abstract class BeritraInvasion<BL extends BeritraLocation> {

	private Npc worldBoss;
	private boolean started;
	private final BL beritraLocation;
	private boolean worldBossDestroyed;

	protected abstract void stopBeritraInvasion();

	protected abstract void startBeritraInvasion();

	private final AtomicBoolean finished = new AtomicBoolean();
	private final WorldBossDestroyListener worldBossDestroyListener = new WorldBossDestroyListener(this);

	public BeritraInvasion(BL beritraLocation) {
		this.beritraLocation = beritraLocation;
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
		startBeritraInvasion();
	}

	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopBeritraInvasion();
		}
	}

	protected void initWorldBoss() {
		Npc wb = null;
		for (VisibleObject obj : getBeritraLocation().getSpawned()) {
			int npcId = ((Npc) obj).getNpcId();
			if ((npcId < 234558) || npcId > 234615) {
				wb = (Npc) obj;
			}
		}
		if (wb == null) {
			throw new NullPointerException("No <World Boss> was found in loc:" + getBeritraLocationId());
		}
		setWorldBoss(wb);
		addWorldBossListeners();
	}

	protected void spawn(BeritraStateType type) {
		BeritraService.getInstance().spawn(getBeritraLocation(), type);
	}

	protected void despawn() {
		BeritraService.getInstance().despawn(getBeritraLocation());
	}

	protected void addWorldBossListeners() {
		AbstractAI ai = (AbstractAI) getWorldBoss().getAi2();
		EnhancedObject eo = (EnhancedObject) ai;
		eo.addCallback(getWorldBossDestroyListener());
	}

	protected void rmvWorldBossListener() {
		AbstractAI ai = (AbstractAI) getWorldBoss().getAi2();
		EnhancedObject eo = (EnhancedObject) ai;
		eo.removeCallback(getWorldBossDestroyListener());
	}

	public boolean isWorldBossDestroyed() {
		return worldBossDestroyed;
	}

	public void setWorldBossDestroyed(boolean state) {
		this.worldBossDestroyed = state;
	}

	public Npc getWorldBoss() {
		return worldBoss;
	}

	public void setWorldBoss(Npc worldBoss) {
		this.worldBoss = worldBoss;
	}

	public WorldBossDestroyListener getWorldBossDestroyListener() {
		return worldBossDestroyListener;
	}

	public boolean isFinished() {
		return finished.get();
	}

	public BL getBeritraLocation() {
		return beritraLocation;
	}

	public int getBeritraLocationId() {
		return beritraLocation.getId();
	}
}
