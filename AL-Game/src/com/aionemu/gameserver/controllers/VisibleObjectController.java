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
package com.aionemu.gameserver.controllers;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.world.World;

/**
 * This class is for controlling VisibleObjects [players, npc's etc]. Its controlling movement, visibility etc.
 *
 * @author -Nemesiss-
 */
public abstract class VisibleObjectController<T extends VisibleObject> {

	/**
	 * Object that is controlled by this controller.
	 */
	private T owner;

	/**
	 * Set owner (controller object).
	 *
	 * @param owner
	 */
	public void setOwner(T owner) {
		this.owner = owner;
	}

	/**
	 * Get owner (controller object).
	 */
	public T getOwner() {
		return owner;
	}

	/**
	 * Called when controlled object is seeing other VisibleObject.
	 *
	 * @param object
	 */
	public void see(VisibleObject object) {
	}

	/**
	 * Called when controlled object no longer see some other VisibleObject.
	 *
	 * @param object
	 * @param isOutOfRange
	 */
	public void notSee(VisibleObject object, boolean isOutOfRange) {
	}

	/**
	 * Removes controlled object from the world.
	 */
	public void delete() {
		/**
		 * despawn object from world.
		 */
		if (getOwner().isSpawned()) {
			World.getInstance().despawn(getOwner());
		}
		/**
		 * Delete object from World.
		 */
		World.getInstance().removeObject(getOwner());
	}

	/**
	 * Called before object is placed into world
	 */
	public void onBeforeSpawn() {
	}

	/**
	 * Called after object was placed into world
	 */
	public void onAfterSpawn() {
	}

	/**
	 * Properly despawn object
	 */
	public void onDespawn() {
	}

	/**
	 * Properly respawn object
	 */
	public void onRespawn() {
	}

	/**
	 * This method should be called to make despawn of VisibleObject and delete it from the world
	 */
	public void onDelete() {
		if (getOwner().isInWorld()) {
			this.onDespawn();
			this.delete();
		}
	}
}
