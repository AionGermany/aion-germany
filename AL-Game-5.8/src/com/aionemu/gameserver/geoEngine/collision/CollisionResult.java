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
package com.aionemu.gameserver.geoEngine.collision;

import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.scene.Geometry;

/**
 * @author Kirill
 */
public class CollisionResult implements Comparable<CollisionResult> {

	private Geometry geometry;
	private Vector3f contactPoint;
	private Vector3f contactNormal;
	private float distance;

	public CollisionResult(Vector3f contactPoint, float distance) {
		this.contactPoint = contactPoint;
		this.distance = distance;
	}

	public CollisionResult() {
	}

	public void setContactPoint(Vector3f point) {
		this.contactPoint = point;
	}

	public void setDistance(float dist) {
		this.distance = dist;
	}

	@Override
	public int compareTo(CollisionResult other) {
		if (distance < other.distance) {
			return -1;
		}
		else if (distance > other.distance) {
			return 1;
		}
		else {
			return 0;
		}
	}

	public void setContactNormal(Vector3f norm) {
		this.contactNormal = norm;
	}

	public void setGeometry(Geometry geom) {
		this.geometry = geom;
	}

	public Vector3f getContactNormal() {
		return contactNormal;
	}

	public Vector3f getContactPoint() {
		return contactPoint;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public float getDistance() {
		return distance;
	}
}
