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
package com.aionemu.gameserver.geoEngine.bounding;

import java.nio.FloatBuffer;

import com.aionemu.gameserver.geoEngine.collision.Collidable;
import com.aionemu.gameserver.geoEngine.math.Matrix4f;
import com.aionemu.gameserver.geoEngine.math.Plane;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Vector3f;

/**
 * <code>BoundingVolume</code> defines an interface for dealing with containment of a collection of points.
 *
 * @author Mark Powell
 * @version $Id: BoundingVolume.java,v 1.24 2007/09/21 15:45:32 nca Exp $
 */
public abstract class BoundingVolume implements Collidable {

	public enum Type {

		Sphere,
		AABB,
		OBB,
		Capsule;
	}

	protected int checkPlane = 0;
	Vector3f center = new Vector3f();

	public BoundingVolume() {
	}

	public BoundingVolume(Vector3f center) {
		this.center.set(center);
	}

	/**
	 * Grabs the checkplane we should check first.
	 */
	public int getCheckPlane() {
		return checkPlane;
	}

	/**
	 * Sets the index of the plane that should be first checked during rendering.
	 *
	 * @param value
	 */
	public final void setCheckPlane(int value) {
		checkPlane = value;
	}

	/**
	 * getType returns the type of bounding volume this is.
	 */
	public abstract Type getType();

	/**
	 * <code>transform</code> alters the location of the bounding volume by a rotation, translation and a scalar.
	 *
	 * @param trans
	 *            the transform to affect the bound.
	 * @param store
	 *            sphere to store result in
	 * @return the new bounding volume.
	 */
	public abstract BoundingVolume transform(Matrix4f trans, BoundingVolume store);

	/**
	 * <code>whichSide</code> returns the side on which the bounding volume lies on a plane. Possible values are POSITIVE_SIDE, NEGATIVE_SIDE, and NO_SIDE.
	 *
	 * @param plane
	 *            the plane to check against this bounding volume.
	 * @return the side on which this bounding volume lies.
	 */
	public abstract Plane.Side whichSide(Plane plane);

	/**
	 * <code>computeFromPoints</code> generates a bounding volume that encompasses a collection of points.
	 *
	 * @param points
	 *            the points to contain.
	 */
	public abstract void computeFromPoints(FloatBuffer points);

	/**
	 * <code>merge</code> combines two bounding volumes into a single bounding volume that contains both this bounding volume and the parameter volume.
	 *
	 * @param volume
	 *            the volume to combine.
	 * @return the new merged bounding volume.
	 */
	public abstract BoundingVolume merge(BoundingVolume volume);

	/**
	 * <code>mergeLocal</code> combines two bounding volumes into a single bounding volume that contains both this bounding volume and the parameter volume. The result is stored locally.
	 *
	 * @param volume
	 *            the volume to combine.
	 * @return this
	 */
	public abstract BoundingVolume mergeLocal(BoundingVolume volume);

	/**
	 * <code>clone</code> creates a new BoundingVolume object containing the same data as this one.
	 *
	 * @param store
	 *            where to store the cloned information. if null or wrong class, a new store is created.
	 * @return the new BoundingVolume
	 */
	public abstract BoundingVolume clone(BoundingVolume store);

	public final Vector3f getCenter() {
		return center;
	}

	public final Vector3f getCenter(Vector3f store) {
		store.set(center);
		return store;
	}

	public final void setCenter(Vector3f newCenter) {
		center = newCenter;
	}

	/**
	 * Find the distance from the center of this Bounding Volume to the given point.
	 *
	 * @param point
	 *            The point to get the distance to
	 * @return distance
	 */
	public final float distanceTo(Vector3f point) {
		return center.distance(point);
	}

	/**
	 * Find the squared distance from the center of this Bounding Volume to the given point.
	 *
	 * @param point
	 *            The point to get the distance to
	 * @return distance
	 */
	public final float distanceSquaredTo(Vector3f point) {
		return center.distanceSquared(point);
	}

	/**
	 * Find the distance from the nearest edge of this Bounding Volume to the given point.
	 *
	 * @param point
	 *            The point to get the distance to
	 * @return distance
	 */
	public abstract float distanceToEdge(Vector3f point);

	/**
	 * determines if this bounding volume and a second given volume are intersecting. Intersecting being: one volume contains another, one volume overlaps another or one volume touches another.
	 *
	 * @param bv
	 *            the second volume to test against.
	 * @return true if this volume intersects the given volume.
	 */
	public abstract boolean intersects(BoundingVolume bv);

	/**
	 * determines if a ray intersects this bounding volume.
	 *
	 * @param ray
	 *            the ray to test.
	 * @return true if this volume is intersected by a given ray.
	 */
	public abstract boolean intersects(Ray ray);

	/**
	 * determines if this bounding volume and a given bounding sphere are intersecting.
	 *
	 * @param bs
	 *            the bounding sphere to test against.
	 * @return true if this volume intersects the given bounding sphere.
	 */
	public abstract boolean intersectsSphere(BoundingSphere bs);

	/**
	 * determines if this bounding volume and a given bounding box are intersecting.
	 *
	 * @param bb
	 *            the bounding box to test against.
	 * @return true if this volume intersects the given bounding box.
	 */
	public abstract boolean intersectsBoundingBox(BoundingBox bb);

	/**
	 * determines if this bounding volume and a given bounding box are intersecting.
	 *
	 * @param bb
	 *            the bounding box to test against.
	 * @return true if this volume intersects the given bounding box.
	 */
	// public abstract boolean intersectsOrientedBoundingBox(OrientedBoundingBox bb);

	/**
	 * determines if a given point is contained within this bounding volume.
	 *
	 * @param point
	 *            the point to check
	 * @return true if the point lies within this bounding volume.
	 */
	public abstract boolean contains(Vector3f point);

	/**
	 * Determines if a given point intersects (touches or is inside) this bounding volume.
	 *
	 * @param point
	 *            the point to check
	 * @return true if the point lies within this bounding volume.
	 */
	public abstract boolean intersects(Vector3f point);

	public abstract float getVolume();

	@Override
	public BoundingVolume clone() {
		try {
			BoundingVolume clone = (BoundingVolume) super.clone();
			clone.center = center.clone();
			return clone;
		}
		catch (CloneNotSupportedException ex) {
			throw new AssertionError();
		}
	}
}
