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
package com.aionemu.gameserver.geoEngine.math;

import com.aionemu.gameserver.geoEngine.bounding.BoundingVolume;
import com.aionemu.gameserver.geoEngine.collision.Collidable;
import com.aionemu.gameserver.geoEngine.collision.CollisionResult;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.collision.UnsupportedCollisionException;

/**
 * <code>Ray</code> defines a line segment which has an origin and a direction. That is, a point and an infinite ray is cast from this point. The ray is defined by the following equation: R(t) =
 * origin + t*direction for t >= 0.
 *
 * @author Mark Powell
 * @author Joshua Slack
 */
public final class Ray implements Cloneable, Collidable {

	/**
	 * The ray's begining point.
	 */
	public Vector3f origin;
	/**
	 * The direction of the ray.
	 */
	public Vector3f direction;
	public float limit = Float.POSITIVE_INFINITY;

	// protected static final Vector3f tempVa=new Vector3f();
	// protected static final Vector3f tempVb=new Vector3f();
	// protected static final Vector3f tempVc=new Vector3f();
	// protected static final Vector3f tempVd=new Vector3f();

	/**
	 * Constructor instantiates a new <code>Ray</code> object. As default, the origin is (0,0,0) and the direction is (0,0,0).
	 */
	public Ray() {
		origin = new Vector3f();
		direction = new Vector3f();
	}

	/**
	 * Constructor instantiates a new <code>Ray</code> object. The origin and direction are given.
	 *
	 * @param origin
	 *            the origin of the ray.
	 * @param direction
	 *            the direction the ray travels in.
	 */
	public Ray(Vector3f origin, Vector3f direction) {
		this.origin = origin;
		this.direction = direction;
	}

	/**
	 * <code>intersect</code> determines if the Ray intersects a triangle.
	 *
	 * @param t
	 *            the Triangle to test against.
	 * @return true if the ray collides.
	 */
	// public boolean intersect(Triangle t) {
	// return intersect(t.get(0), t.get(1), t.get(2));
	// }
	/**
	 * <code>intersect</code> determines if the Ray intersects a triangle defined by the specified points.
	 *
	 * @param v0
	 *            first point of the triangle.
	 * @param v1
	 *            second point of the triangle.
	 * @param v2
	 *            third point of the triangle.
	 * @return true if the ray collides.
	 */
	// public boolean intersect(Vector3f v0,Vector3f v1,Vector3f v2){
	// return intersectWhere(v0, v1, v2, null);
	// }

	/**
	 * <code>intersectWhere</code> determines if the Ray intersects a triangle. It then stores the point of intersection in the given loc vector
	 *
	 * @param t
	 *            the Triangle to test against.
	 * @param loc
	 *            storage vector to save the collision point in (if the ray collides)
	 * @return true if the ray collides.
	 */
	public boolean intersectWhere(Triangle t, Vector3f loc) {
		return intersectWhere(t.get(0), t.get(1), t.get(2), loc);
	}

	/**
	 * <code>intersectWhere</code> determines if the Ray intersects a triangle defined by the specified points and if so it stores the point of intersection in the given loc vector.
	 *
	 * @param v0
	 *            first point of the triangle.
	 * @param v1
	 *            second point of the triangle.
	 * @param v2
	 *            third point of the triangle.
	 * @param loc
	 *            storage vector to save the collision point in (if the ray collides) if null, only boolean is calculated.
	 * @return true if the ray collides.
	 */
	public boolean intersectWhere(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f loc) {
		return intersects(v0, v1, v2, loc, false, false);
	}

	/**
	 * <code>intersectWherePlanar</code> determines if the Ray intersects a triangle and if so it stores the point of intersection in the given loc vector as t, u, v where t is the distance from the
	 * origin to the point of intersection and u,v is the intersection point in terms of the triangle plane.
	 *
	 * @param t
	 *            the Triangle to test against.
	 * @param loc
	 *            storage vector to save the collision point in (if the ray collides) as t, u, v
	 * @return true if the ray collides.
	 */
	public boolean intersectWherePlanar(Triangle t, Vector3f loc) {
		return intersectWherePlanar(t.get(0), t.get(1), t.get(2), loc);
	}

	/**
	 * <code>intersectWherePlanar</code> determines if the Ray intersects a triangle defined by the specified points and if so it stores the point of intersection in the given loc vector as t, u, v
	 * where t is the distance from the origin to the point of intersection and u,v is the intersection point in terms of the triangle plane.
	 *
	 * @param v0
	 *            first point of the triangle.
	 * @param v1
	 *            second point of the triangle.
	 * @param v2
	 *            third point of the triangle.
	 * @param loc
	 *            storage vector to save the collision point in (if the ray collides) as t, u, v
	 * @return true if the ray collides.
	 */
	public boolean intersectWherePlanar(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f loc) {
		return intersects(v0, v1, v2, loc, true, false);
	}

	/**
	 * <code>intersects</code> does the actual intersection work.
	 *
	 * @param v0
	 *            first point of the triangle.
	 * @param v1
	 *            second point of the triangle.
	 * @param v2
	 *            third point of the triangle.
	 * @param store
	 *            storage vector - if null, no intersection is calc'd
	 * @param doPlanar
	 *            true if we are calcing planar results.
	 * @param quad
	 * @return true if ray intersects triangle
	 */
	private boolean intersects(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f store, boolean doPlanar, boolean quad) {

		Vector3f tempVa = Vector3f.newInstance(), tempVb = Vector3f.newInstance(), tempVc = Vector3f.newInstance(), tempVd = Vector3f.newInstance();

		Vector3f diff = origin.subtract(v0, tempVa);
		Vector3f edge1 = v1.subtract(v0, tempVb);
		Vector3f edge2 = v2.subtract(v0, tempVc);
		Vector3f norm = edge1.cross(edge2, tempVd);

		float dirDotNorm = direction.dot(norm);
		float sign;
		if (dirDotNorm > FastMath.FLT_EPSILON) {
			sign = 1;
		}
		else if (dirDotNorm < -FastMath.FLT_EPSILON) {
			sign = -1f;
			dirDotNorm = -dirDotNorm;
		}
		else {
			// ray and triangle/quad are parallel
			return false;
		}

		float dirDotDiffxEdge2 = sign * direction.dot(diff.cross(edge2, edge2));
		if (dirDotDiffxEdge2 >= 0.0f) {
			float dirDotEdge1xDiff = sign * direction.dot(edge1.crossLocal(diff));

			if (dirDotEdge1xDiff >= 0.0f) {
				if (!quad ? dirDotDiffxEdge2 + dirDotEdge1xDiff <= dirDotNorm : dirDotEdge1xDiff <= dirDotNorm) {
					float diffDotNorm = -sign * diff.dot(norm);
					if (diffDotNorm >= 0.0f) {
						// this method always returns
						Vector3f.recycle(tempVa);
						Vector3f.recycle(tempVb);
						Vector3f.recycle(tempVc);
						Vector3f.recycle(tempVd);

						// ray intersects triangle
						// if storage vector is null, just return true,
						if (store == null) {
							return true;
						}

						// else fill in.
						float inv = 1f / dirDotNorm;
						float t = diffDotNorm * inv;
						if (!doPlanar) {
							store.set(origin).addLocal(direction.x * t, direction.y * t, direction.z * t);
						}
						else {
							// these weights can be used to determine
							// interpolated values, such as texture coord.
							// eg. texcoord s,t at intersection point:
							// s = w0*s0 + w1*s1 + w2*s2;
							// t = w0*t0 + w1*t1 + w2*t2;
							float w1 = dirDotDiffxEdge2 * inv;
							float w2 = dirDotEdge1xDiff * inv;
							// float w0 = 1.0f - w1 - w2;
							store.set(t, w1, w2);
						}
						return true;
					}
				}
			}
		}
		Vector3f.recycle(tempVa);
		Vector3f.recycle(tempVb);
		Vector3f.recycle(tempVc);
		Vector3f.recycle(tempVd);
		return false;
	}

	public float intersects(Vector3f v0, Vector3f v1, Vector3f v2) {
		float edge1X = v1.x - v0.x;
		float edge1Y = v1.y - v0.y;
		float edge1Z = v1.z - v0.z;

		float edge2X = v2.x - v0.x;
		float edge2Y = v2.y - v0.y;
		float edge2Z = v2.z - v0.z;

		float normX = ((edge1Y * edge2Z) - (edge1Z * edge2Y));
		float normY = ((edge1Z * edge2X) - (edge1X * edge2Z));
		float normZ = ((edge1X * edge2Y) - (edge1Y * edge2X));

		float dirDotNorm = direction.x * normX + direction.y * normY + direction.z * normZ;

		float diffX = origin.x - v0.x;
		float diffY = origin.y - v0.y;
		float diffZ = origin.z - v0.z;

		float sign;
		if (dirDotNorm > FastMath.FLT_EPSILON) {
			sign = 1;
		}
		else if (dirDotNorm < -FastMath.FLT_EPSILON) {
			sign = -1f;
			dirDotNorm = -dirDotNorm;
		}
		else {
			// ray and triangle/quad are parallel
			return Float.POSITIVE_INFINITY;
		}

		float diffEdge2X = ((diffY * edge2Z) - (diffZ * edge2Y));
		float diffEdge2Y = ((diffZ * edge2X) - (diffX * edge2Z));
		float diffEdge2Z = ((diffX * edge2Y) - (diffY * edge2X));

		float dirDotDiffxEdge2 = sign * (direction.x * diffEdge2X + direction.y * diffEdge2Y + direction.z * diffEdge2Z);

		if (dirDotDiffxEdge2 >= 0.0f) {
			diffEdge2X = ((edge1Y * diffZ) - (edge1Z * diffY));
			diffEdge2Y = ((edge1Z * diffX) - (edge1X * diffZ));
			diffEdge2Z = ((edge1X * diffY) - (edge1Y * diffX));

			float dirDotEdge1xDiff = sign * (direction.x * diffEdge2X + direction.y * diffEdge2Y + direction.z * diffEdge2Z);

			if (dirDotEdge1xDiff >= 0.0f) {
				if (dirDotDiffxEdge2 + dirDotEdge1xDiff <= dirDotNorm) {
					float diffDotNorm = -sign * (diffX * normX + diffY * normY + diffZ * normZ);
					if (diffDotNorm >= 0.0f) {
						// ray intersects triangle
						// fill in.
						float inv = 1f / dirDotNorm;
						float t = diffDotNorm * inv;
						return t;
					}
				}
			}
		}

		return Float.POSITIVE_INFINITY;
	}

	/**
	 * <code>intersectWherePlanar</code> determines if the Ray intersects a quad defined by the specified points and if so it stores the point of intersection in the given loc vector as t, u, v where
	 * t is the distance from the origin to the point of intersection and u,v is the intersection point in terms of the quad plane. One edge of the quad is [v0,v1], another one [v0,v2]. The behaviour
	 * thus is like {@link #intersectWherePlanar(Vector3f, Vector3f, Vector3f, Vector3f)} except for the extended area, which is equivalent to the union of the triangles [v0,v1,v2] and
	 * [-v0+v1+v2,v1,v2].
	 *
	 * @param v0
	 *            top left point of the quad.
	 * @param v1
	 *            top right point of the quad.
	 * @param v2
	 *            bottom left point of the quad.
	 * @param loc
	 *            storage vector to save the collision point in (if the ray collides) as t, u, v
	 * @return true if the ray collides with the quad.
	 */
	public boolean intersectWherePlanarQuad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f loc) {
		return intersects(v0, v1, v2, loc, true, true);
	}

	/**
	 * @param p
	 * @param loc
	 * @return true if the ray collides with the given Plane
	 */
	public boolean intersectsWherePlane(Plane p, Vector3f loc) {
		float denominator = p.getNormal().dot(direction);

		if (denominator > -FastMath.FLT_EPSILON && denominator < FastMath.FLT_EPSILON) {
			return false; // coplanar
		}
		float numerator = -(p.getNormal().dot(origin) - p.getConstant());
		float ratio = numerator / denominator;

		if (ratio < FastMath.FLT_EPSILON) {
			return false; // intersects behind origin
		}
		loc.set(direction).multLocal(ratio).addLocal(origin);

		return true;
	}

	@Override
	public int collideWith(Collidable other, CollisionResults results) {
		if (other instanceof BoundingVolume) {
			BoundingVolume bv = (BoundingVolume) other;
			return bv.collideWith(this, results);
		}
		else if (other instanceof AbstractTriangle) {
			AbstractTriangle tri = (AbstractTriangle) other;
			float d = intersects(tri.get1(), tri.get2(), tri.get3());
			if (Float.isInfinite(d) || Float.isNaN(d)) {
				return 0;
			}

			Vector3f point = new Vector3f(direction).multLocal(d).addLocal(origin);
			results.addCollision(new CollisionResult(point, d));
			return 1;
		}
		else {
			throw new UnsupportedCollisionException();
		}
	}

	public float distanceSquared(Vector3f point) {

		Vector3f tempVa = Vector3f.newInstance(), tempVb = Vector3f.newInstance();

		point.subtract(origin, tempVa);
		float rayParam = direction.dot(tempVa);
		if (rayParam > 0) {
			origin.add(direction.mult(rayParam, tempVb), tempVb);
		}
		else {
			tempVb.set(origin);
			rayParam = 0.0f;
		}

		tempVb.subtract(point, tempVa);
		float len = tempVa.lengthSquared();
		Vector3f.recycle(tempVa);
		Vector3f.recycle(tempVb);
		return len;
	}

	/**
	 * <code>getOrigin</code> retrieves the origin point of the ray.
	 *
	 * @return the origin of the ray.
	 */
	public Vector3f getOrigin() {
		return origin;
	}

	/**
	 * <code>setOrigin</code> sets the origin of the ray.
	 *
	 * @param origin
	 *            the origin of the ray.
	 */
	public void setOrigin(Vector3f origin) {
		this.origin.set(origin);
	}

	/**
	 * <code>getLimit</code> returns the limit or the ray, aka the length. If the limit is not infinity, then this ray is a line with length <code>
	 * limit</code>.
	 *
	 * @return
	 */
	public float getLimit() {
		return limit;
	}

	/**
	 * <code>setLimit</code> sets the limit of the ray.
	 *
	 * @param limit
	 *            the limit of the ray.
	 * @see Ray#getLimit()
	 */
	public void setLimit(float limit) {
		this.limit = limit;
	}

	/**
	 * <code>getDirection</code> retrieves the direction vector of the ray.
	 *
	 * @return the direction of the ray.
	 */
	public Vector3f getDirection() {
		return direction;
	}

	/**
	 * <code>setDirection</code> sets the direction vector of the ray.
	 *
	 * @param direction
	 *            the direction of the ray.
	 */
	public void setDirection(Vector3f direction) {
		this.direction.set(direction);
	}

	/**
	 * Copies information from a source ray into this ray.
	 *
	 * @param source
	 *            the ray to copy information from
	 */
	public void set(Ray source) {
		origin.set(source.getOrigin());
		direction.set(source.getDirection());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [Origin: " + origin + ", Direction: " + direction + "]";
	}

	public Class<? extends Ray> getClassTag() {
		return this.getClass();
	}

	@Override
	public Ray clone() {
		try {
			Ray r = (Ray) super.clone();
			r.direction = direction.clone();
			r.origin = origin.clone();
			return r;
		}
		catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
