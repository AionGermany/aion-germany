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

/**
 * @author Rolandas
 */
public class WalkerGroupShift {

	private float sagittalShift; // left and right (sides)
	private float coronalShift; // or dorsoventral (back and front)
	private float angle; // if positioned in circle
	public static final float DISTANCE = 2; // 2 meters distance by default

	public WalkerGroupShift(float leftRight, float backFront) {
		sagittalShift = leftRight;
		coronalShift = backFront;
	}

	public WalkerGroupShift(float angle) {
		this.angle = angle;
	}

	/**
	 * left and right (sides)
	 *
	 * @return the sagittalShift
	 */
	public float getSagittalShift() {
		return sagittalShift;
	}

	/**
	 * dorsoventral (back and front)
	 *
	 * @return the coronalShift
	 */
	public float getCoronalShift() {
		return coronalShift;
	}

	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}
}
