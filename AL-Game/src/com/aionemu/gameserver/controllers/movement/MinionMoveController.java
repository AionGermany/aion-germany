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
package com.aionemu.gameserver.controllers.movement;

import com.aionemu.gameserver.model.gameobjects.Minion;

/**
 * @author Falke_34
 */
public class MinionMoveController extends CreatureMoveController<Minion> {

	protected float targetDestX;
	protected float targetDestY;
	protected float targetDestZ;
	protected byte heading;
	protected byte movementMask;

	public MinionMoveController() {
		super(null);// not used yet
	}

	@Override
	public void moveToDestination() {
	}

	@Override
	public float getTargetX2() {
		return targetDestX;
	}

	@Override
	public float getTargetY2() {
		return targetDestY;
	}

	@Override
	public float getTargetZ2() {
		return targetDestZ;
	}

	@Override
	public void setNewDirection(float x2, float y2, float z2) {
		setNewDirection(x2, y2, z2, (byte) 0);
	}

	@Override
	public void setNewDirection(float x, float y, float z, byte heading) {
		this.targetDestX = x;
		this.targetDestY = y;
		this.targetDestZ = z;
		this.heading = heading;
	}

	@Override
	public void startMovingToDestination() {
	}

	@Override
	public void abortMove() {
	}

	@Override
	public byte getMovementMask() {
		return movementMask;
	}

	@Override
	public boolean isInMove() {
		return true;
	}

	@Override
	public void setInMove(boolean value) {
	}

	@Override
	public void skillMovement() {
		this.movementMask = MovementMask.IMMEDIATE;
	}
}