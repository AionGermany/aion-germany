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
package com.aionemu.gameserver.spawnengine;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;

/**
 * Stores for the spawn needed information, used for forming walker groups and spawning NPCs
 *
 * @author vlog
 * @modified Rolandas
 */
public class ClusteredNpc {

	private Npc npc;
	private int instance;
	private WalkerTemplate walkTemplate;
	private float x;
	private float y;
	private int walkerIdx;

	public ClusteredNpc(Npc npc, int instance, WalkerTemplate walkTemplate) {
		this.npc = npc;
		this.instance = instance;
		this.walkTemplate = walkTemplate;
		this.x = npc.getSpawn().getX();
		this.y = npc.getSpawn().getY();
		this.walkerIdx = npc.getSpawn().getWalkerIndex();
	}

	public Npc getNpc() {
		return npc;
	}

	public int getInstance() {
		return instance;
	}

	public void spawn(float z) {
		SpawnEngine.bringIntoWorld(npc, npc.getSpawn().getWorldId(), instance, x, y, z, npc.getSpawn().getHeading());
	}

	public void despawn() {
		npc.getMoveController().abortMove();
		npc.getController().cancelTask(TaskId.RESPAWN);
		npc.getController().onDelete();
	}

	public void setNpc(Npc npc) {
		npc.setWalkerGroupShift(this.npc.getWalkerGroupShift());
		this.npc = npc;
		this.x = npc.getSpawn().getX();
		this.y = npc.getSpawn().getY();
	}

	public boolean hasSamePosition(ClusteredNpc other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		return this.x == other.x && this.y == other.y;
	}

	public int getPositionHash() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	public float getXDelta() {
		return walkTemplate.getRouteStep(1).getX() - x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;
		this.getNpc().getSpawn().setX(x);
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	public float getYDelta() {
		return walkTemplate.getRouteStep(1).getY() - y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		this.y = y;
		this.getNpc().getSpawn().setY(y);
	}

	/**
	 * @return the walkTemplate
	 */
	public WalkerTemplate getWalkTemplate() {
		return walkTemplate;
	}

	public int getWalkerIndex() {
		return walkerIdx;
	}
}
