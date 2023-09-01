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
import com.aionemu.gameserver.geoEngine.collision.CollisionResult;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.collision.UnsupportedCollisionException;
import com.aionemu.gameserver.geoEngine.math.Array3f;
import com.aionemu.gameserver.geoEngine.math.FastMath;
import com.aionemu.gameserver.geoEngine.math.Matrix3f;
import com.aionemu.gameserver.geoEngine.math.Matrix4f;
import com.aionemu.gameserver.geoEngine.math.Plane;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Triangle;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.scene.Mesh;
import com.aionemu.gameserver.geoEngine.utils.BufferUtils;

//import com.jme.scene.TriMesh;

/**
 * <code>BoundingBox</code> defines an axis-aligned cube that defines a container for a group of vertices of a particular piece of geometry. This box defines a center and extents from that center
 * along the x, y and z axis. <br>
 * <br>
 * A typical usage is to allow the class define the center and radius by calling either <code>containAABB</code> or <code>averagePoints</code>. A call to <code>computeFramePoint</code> in turn calls
 * <code>containAABB</code>.
 *
 * @author Joshua Slack
 * @version $Id: BoundingBox.java,v 1.50 2007/09/22 16:46:35 irrisor Exp $
 */
public class BoundingBox extends BoundingVolume {

	float xExtent, yExtent, zExtent;

	/**
	 * Default constructor instantiates a new <code>BoundingBox</code> object.
	 */
	public BoundingBox() {
	}

	/**
	 * Contstructor instantiates a new <code>BoundingBox</code> object with given specs.
	 */
	public BoundingBox(Vector3f c, float x, float y, float z) {
		this.center.set(c);
		this.xExtent = x;
		this.yExtent = y;
		this.zExtent = z;
	}

	public BoundingBox(BoundingBox source) {
		this.center.set(source.center);
		this.xExtent = source.xExtent;
		this.yExtent = source.yExtent;
		this.zExtent = source.zExtent;
	}

	public BoundingBox(Vector3f min, Vector3f max) {
		setMinMax(min, max);
	}

	@Override
	public Type getType() {
		return Type.AABB;
	}

	/**
	 * <code>computeFromPoints</code> creates a new Bounding Box from a given set of points. It uses the <code>containAABB</code> method as default.
	 *
	 * @param points
	 *            the points to contain.
	 */
	@Override
	public void computeFromPoints(FloatBuffer points) {
		containAABB(points);
	}

	/**
	 * <code>computeFromTris</code> creates a new Bounding Box from a given set of triangles. It is used in OBBTree calculations.
	 *
	 * @param tris
	 * @param start
	 * @param end
	 */
	public void computeFromTris(Triangle[] tris, int start, int end) {
		if (end - start <= 0) {
			return;
		}
		Vector3f min = Vector3f.newInstance();
		Vector3f max = Vector3f.newInstance();
		min.set(new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
		max.set(new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));

		Vector3f point;
		for (int i = start; i < end; i++) {
			point = tris[i].get(0);
			checkMinMax(min, max, point);
			point = tris[i].get(1);
			checkMinMax(min, max, point);
			point = tris[i].get(2);
			checkMinMax(min, max, point);
		}

		center.set(min.addLocal(max));
		center.multLocal(0.5f);

		xExtent = max.x - center.x;
		yExtent = max.y - center.y;
		zExtent = max.z - center.z;
		Vector3f.recycle(min);
		Vector3f.recycle(max);
	}

	public void computeFromTris(int[] indices, Mesh mesh, int start, int end) {
		if (end - start <= 0) {
			return;
		}
		Vector3f vect1 = Vector3f.newInstance();
		Vector3f vect2 = Vector3f.newInstance();
		Triangle triangle = Triangle.newInstance();

		Vector3f min = vect1.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
		Vector3f max = vect2.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
		Vector3f point;

		for (int i = start; i < end; i++) {
			mesh.getTriangle(indices[i], triangle);
			point = triangle.get(0);
			checkMinMax(min, max, point);
			point = triangle.get(1);
			checkMinMax(min, max, point);
			point = triangle.get(2);
			checkMinMax(min, max, point);
		}

		center.set(min.addLocal(max));
		center.multLocal(0.5f);

		xExtent = max.x - center.x;
		yExtent = max.y - center.y;
		zExtent = max.z - center.z;
		Vector3f.recycle(vect1);
		Vector3f.recycle(vect2);
		Triangle.recycle(triangle);
	}

	public static final void checkMinMax(Vector3f min, Vector3f max, Vector3f point) {
		if (point.x < min.x) {
			min.x = point.x;
		}
		if (point.x > max.x) {
			max.x = point.x;
		}
		if (point.y < min.y) {
			min.y = point.y;
		}
		if (point.y > max.y) {
			max.y = point.y;
		}
		if (point.z < min.z) {
			min.z = point.z;
		}
		if (point.z > max.z) {
			max.z = point.z;
		}
	}

	/**
	 * <code>containAABB</code> creates a minimum-volume axis-aligned bounding box of the points, then selects the smallest enclosing sphere of the box with the sphere centered at the boxes center.
	 *
	 * @param points
	 *            the list of points.
	 */
	public void containAABB(FloatBuffer points) {
		if (points == null) {
			return;
		}

		points.rewind();
		if (points.remaining() <= 2) // we need at least a 3 float vector
		{
			return;
		}

		Vector3f vect1 = Vector3f.newInstance();
		BufferUtils.populateFromBuffer(vect1, points, 0);
		float minX = vect1.x, minY = vect1.y, minZ = vect1.z;
		float maxX = vect1.x, maxY = vect1.y, maxZ = vect1.z;

		for (int i = 1, len = points.remaining() / 3; i < len; i++) {
			BufferUtils.populateFromBuffer(vect1, points, i);
			if (vect1.x < minX) {
				minX = vect1.x;
			}
			else if (vect1.x > maxX) {
				maxX = vect1.x;
			}

			if (vect1.y < minY) {
				minY = vect1.y;
			}
			else if (vect1.y > maxY) {
				maxY = vect1.y;
			}

			if (vect1.z < minZ) {
				minZ = vect1.z;
			}
			else if (vect1.z > maxZ) {
				maxZ = vect1.z;
			}
		}
		Vector3f.recycle(vect1);

		center.set(minX + maxX, minY + maxY, minZ + maxZ);
		center.multLocal(0.5f);

		xExtent = maxX - center.x;
		yExtent = maxY - center.y;
		zExtent = maxZ - center.z;
	}

	@Override
	public BoundingVolume transform(Matrix4f trans, BoundingVolume store) {
		BoundingBox box;
		if (store == null || store.getType() != Type.AABB) {
			box = new BoundingBox();
		}
		else {
			box = (BoundingBox) store;
		}

		float w = trans.multProj(center, box.center);
		box.center.divideLocal(w);

		Matrix3f transMatrix = Matrix3f.newInstance();
		trans.toRotationMatrix(transMatrix);

		// Make the rotation matrix all positive to get the maximum x/y/z extent
		transMatrix.absoluteLocal();
		Vector3f vect1 = Vector3f.newInstance();
		vect1.set(xExtent, yExtent, zExtent);
		transMatrix.mult(vect1, vect1);

		// Assign the biggest rotations after scales.
		box.xExtent = FastMath.abs(vect1.getX());
		box.yExtent = FastMath.abs(vect1.getY());
		box.zExtent = FastMath.abs(vect1.getZ());
		Vector3f.recycle(vect1);
		Matrix3f.recycle(transMatrix);

		return box;
	}

	/**
	 * <code>whichSide</code> takes a plane (typically provided by a view frustum) to determine which side this bound is on.
	 *
	 * @param plane
	 *            the plane to check against.
	 */
	@Override
	public Plane.Side whichSide(Plane plane) {
		float radius = FastMath.abs(xExtent * plane.getNormal().getX()) + FastMath.abs(yExtent * plane.getNormal().getY()) + FastMath.abs(zExtent * plane.getNormal().getZ());

		float distance = plane.pseudoDistance(center);

		// changed to < and > to prevent floating point precision problems
		if (distance < -radius) {
			return Plane.Side.Negative;
		}
		else if (distance > radius) {
			return Plane.Side.Positive;
		}
		else {
			return Plane.Side.None;
		}
	}

	/**
	 * <code>merge</code> combines this sphere with a second bounding sphere. This new sphere contains both bounding spheres and is returned.
	 *
	 * @param volume
	 *            the sphere to combine with this sphere.
	 * @return the new sphere
	 */
	@Override
	public BoundingVolume merge(BoundingVolume volume) {
		if (volume == null) {
			return this;
		}

		switch (volume.getType()) {
			case AABB: {
				BoundingBox vBox = (BoundingBox) volume;
				return merge(vBox.center, vBox.xExtent, vBox.yExtent, vBox.zExtent, new BoundingBox(new Vector3f(0, 0, 0), 0, 0, 0));
			}

			// case OBB: {
			// OrientedBoundingBox box = (OrientedBoundingBox) volume;
			// BoundingBox rVal = (BoundingBox) this.clone(null);
			// return rVal.mergeOBB(box);
			// }
			default:
				return null;
		}
	}

	/**
	 * <code>mergeLocal</code> combines this sphere with a second bounding sphere locally. Altering this sphere to contain both the original and the additional sphere volumes;
	 *
	 * @param volume
	 *            the sphere to combine with this sphere.
	 * @return this
	 */
	@Override
	public BoundingVolume mergeLocal(BoundingVolume volume) {
		if (volume == null) {
			return this;
		}

		switch (volume.getType()) {
			case AABB: {
				BoundingBox vBox = (BoundingBox) volume;
				return merge(vBox.center, vBox.xExtent, vBox.yExtent, vBox.zExtent, this);
			}

			// case OBB: {
			// return mergeOBB((OrientedBoundingBox) volume);
			// }
			default:
				return null;
		}
	}

	/**
	 * Merges this AABB with the given OBB.
	 *
	 * @param volume
	 *            the OBB to merge this AABB with.
	 * @return This AABB extended to fit the given OBB.
	 */
	// private BoundingBox mergeOBB(OrientedBoundingBox volume) {
	// if (!volume.correctCorners)
	// volume.computeCorners();
	//
	// TempVars vars = TempVars.get();
	// Vector3f min = vars.compVect1.set(center.x - xExtent, center.y - yExtent,
	// center.z - zExtent);
	// Vector3f max = vars.compVect2.set(center.x + xExtent, center.y + yExtent,
	// center.z + zExtent);
	//
	// for (int i = 1; i < volume.vectorStore.length; i++) {
	// Vector3f temp = volume.vectorStore[i];
	// if (temp.x < min.x)
	// min.x = temp.x;
	// else if (temp.x > max.x)
	// max.x = temp.x;
	//
	// if (temp.y < min.y)
	// min.y = temp.y;
	// else if (temp.y > max.y)
	// max.y = temp.y;
	//
	// if (temp.z < min.z)
	// min.z = temp.z;
	// else if (temp.z > max.z)
	// max.z = temp.z;
	// }
	//
	// center.set(min.addLocal(max));
	// center.multLocal(0.5f);
	//
	// xExtent = max.x - center.x;
	// yExtent = max.y - center.y;
	// zExtent = max.z - center.z;
	// return this;
	// }

	/**
	 * <code>merge</code> combines this bounding box with another box which is defined by the center, x, y, z extents.
	 *
	 * @param boxCenter
	 *            the center of the box to merge with
	 * @param boxX
	 *            the x extent of the box to merge with.
	 * @param boxY
	 *            the y extent of the box to merge with.
	 * @param boxZ
	 *            the z extent of the box to merge with.
	 * @param rVal
	 *            the resulting merged box.
	 * @return the resulting merged box.
	 */
	private BoundingBox merge(Vector3f boxCenter, float boxX, float boxY, float boxZ, BoundingBox rVal) {
		Vector3f vect1 = Vector3f.newInstance();
		Vector3f vect2 = Vector3f.newInstance();

		vect1.x = center.x - xExtent;
		if (vect1.x > boxCenter.x - boxX) {
			vect1.x = boxCenter.x - boxX;
		}
		vect1.y = center.y - yExtent;
		if (vect1.y > boxCenter.y - boxY) {
			vect1.y = boxCenter.y - boxY;
		}
		vect1.z = center.z - zExtent;
		if (vect1.z > boxCenter.z - boxZ) {
			vect1.z = boxCenter.z - boxZ;
		}

		vect2.x = center.x + xExtent;
		if (vect2.x < boxCenter.x + boxX) {
			vect2.x = boxCenter.x + boxX;
		}
		vect2.y = center.y + yExtent;
		if (vect2.y < boxCenter.y + boxY) {
			vect2.y = boxCenter.y + boxY;
		}
		vect2.z = center.z + zExtent;
		if (vect2.z < boxCenter.z + boxZ) {
			vect2.z = boxCenter.z + boxZ;
		}

		center.set(vect2).addLocal(vect1).multLocal(0.5f);

		xExtent = vect2.x - center.x;
		yExtent = vect2.y - center.y;
		zExtent = vect2.z - center.z;

		Vector3f.recycle(vect1);
		Vector3f.recycle(vect2);
		return rVal;
	}

	/**
	 * <code>clone</code> creates a new BoundingBox object containing the same data as this one.
	 *
	 * @param store
	 *            where to store the cloned information. if null or wrong class, a new store is created.
	 * @return the new BoundingBox
	 */
	@Override
	public BoundingBox clone(BoundingVolume store) {
		if (store != null && store.getType() == Type.AABB) {
			BoundingBox rVal = (BoundingBox) store;
			rVal.center.set(center);
			rVal.xExtent = xExtent;
			rVal.yExtent = yExtent;
			rVal.zExtent = zExtent;
			rVal.checkPlane = checkPlane;
			return rVal;
		}

		BoundingBox rVal = new BoundingBox(center.clone(), xExtent, yExtent, zExtent);
		return rVal;
	}

	/**
	 * <code>toString</code> returns the string representation of this object. The form is: "Radius: RRR.SSSS Center: <Vector>".
	 *
	 * @return the string representation of this.
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [Center: " + center + "  xExtent: " + xExtent + "  yExtent: " + yExtent + "  zExtent: " + zExtent + "]";
	}

	/**
	 * determines if this bounding box intersects a given bounding sphere.
	 *
	 * @see com.jme.bounding.BoundingVolume#intersectsSphere(com.jme.bounding.BoundingSphere)
	 */
	@Override
	public boolean intersectsSphere(BoundingSphere bs) {
		return ((FastMath.abs(center.x - bs.center.x) < bs.getRadius() + xExtent) && (FastMath.abs(center.y - bs.center.y) < bs.getRadius() + yExtent) && (FastMath.abs(center.z - bs.center.z) < bs.getRadius() + zExtent));
	}

	/**
	 * intersects determines if this Bounding Box intersects with another given bounding volume. If so, true is returned, otherwise, false is returned.
	 *
	 * @see com.aionemu.gameserver.geoEngine.bounding.jme.bounding.BoundingVolume#intersects(com.aionemu.gameserver.geoEngine.bounding.jme.bounding.BoundingVolume)
	 */
	@Override
	public boolean intersects(BoundingVolume bv) {
		return bv.intersectsBoundingBox(this);
	}

	/**
	 * determines if this bounding box intersects a given bounding box. If the two boxes intersect in any way, true is returned. Otherwise, false is returned.
	 *
	 * @see com.aionemu.gameserver.geoEngine.bounding.jme.bounding.BoundingVolume#intersectsBoundingBox(com.aionemu.gameserver.geoEngine.bounding.jme.bounding.BoundingBox)
	 */
	@Override
	public boolean intersectsBoundingBox(BoundingBox bb) {
		assert Vector3f.isValidVector(center) && Vector3f.isValidVector(bb.center);

		if (center.x + xExtent < bb.center.x - bb.xExtent || center.x - xExtent > bb.center.x + bb.xExtent) {
			return false;
		}
		else if (center.y + yExtent < bb.center.y - bb.yExtent || center.y - yExtent > bb.center.y + bb.yExtent) {
			return false;
		}
		else if (center.z + zExtent < bb.center.z - bb.zExtent || center.z - zExtent > bb.center.z + bb.zExtent) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * determines if this bounding box intersects with a given oriented bounding box.
	 *
	 * @see com.jme.bounding.BoundingVolume#intersectsOrientedBoundingBox(com.jme.bounding.OrientedBoundingBox)
	 */
	// public boolean intersectsOrientedBoundingBox(OrientedBoundingBox obb) {
	// return obb.intersectsBoundingBox(this);
	// }

	/**
	 * determines if this bounding box intersects with a given ray object. If an intersection has occurred, true is returned, otherwise false is returned.
	 *
	 * @see com.aionemu.gameserver.geoEngine.bounding.jme.bounding.BoundingVolume#intersects(com.jme.math.Ray)
	 */
	@Override
	public boolean intersects(Ray ray) {
		// assert Vector3f.isValidVector(center);

		float rhs;

		Vector3f vect1 = Vector3f.newInstance();
		Vector3f vect2 = Vector3f.newInstance();
		Vector3f diff = ray.origin.subtract(getCenter(vect2), vect1);

		final Array3f fWdU = Array3f.newInstance();
		final Array3f fAWdU = Array3f.newInstance();
		final Array3f fDdU = Array3f.newInstance();
		final Array3f fADdU = Array3f.newInstance();
		final Array3f fAWxDdU = Array3f.newInstance();

		fWdU.a = ray.getDirection().dot(Vector3f.UNIT_X);
		fAWdU.a = FastMath.abs(fWdU.a);
		fDdU.a = diff.dot(Vector3f.UNIT_X);
		fADdU.a = FastMath.abs(fDdU.a);
		if (fADdU.a > xExtent && fDdU.a * fWdU.a >= 0.0) {
			Vector3f.recycle(vect1);
			Vector3f.recycle(vect2);
			Array3f.recycle(fWdU);
			Array3f.recycle(fAWdU);
			Array3f.recycle(fDdU);
			Array3f.recycle(fADdU);
			Array3f.recycle(fAWxDdU);
			return false;
		}

		fWdU.b = ray.getDirection().dot(Vector3f.UNIT_Y);
		fAWdU.b = FastMath.abs(fWdU.b);
		fDdU.b = diff.dot(Vector3f.UNIT_Y);
		fADdU.b = FastMath.abs(fDdU.b);
		if (fADdU.b > yExtent && fDdU.b * fWdU.b >= 0.0) {
			Vector3f.recycle(vect1);
			Vector3f.recycle(vect2);
			Array3f.recycle(fWdU);
			Array3f.recycle(fAWdU);
			Array3f.recycle(fDdU);
			Array3f.recycle(fADdU);
			Array3f.recycle(fAWxDdU);
			return false;
		}

		fWdU.c = ray.getDirection().dot(Vector3f.UNIT_Z);
		fAWdU.c = FastMath.abs(fWdU.c);
		fDdU.c = diff.dot(Vector3f.UNIT_Z);
		fADdU.c = FastMath.abs(fDdU.c);
		if (fADdU.c > zExtent && fDdU.c * fWdU.c >= 0.0) {
			Vector3f.recycle(vect1);
			Vector3f.recycle(vect2);
			Array3f.recycle(fWdU);
			Array3f.recycle(fAWdU);
			Array3f.recycle(fDdU);
			Array3f.recycle(fADdU);
			Array3f.recycle(fAWxDdU);
			return false;
		}

		Vector3f wCrossD = ray.getDirection().cross(diff, vect2);

		fAWxDdU.a = FastMath.abs(wCrossD.dot(Vector3f.UNIT_X));
		rhs = yExtent * fAWdU.c + zExtent * fAWdU.b;
		if (fAWxDdU.a > rhs) {
			Vector3f.recycle(vect1);
			Vector3f.recycle(vect2);
			Array3f.recycle(fWdU);
			Array3f.recycle(fAWdU);
			Array3f.recycle(fDdU);
			Array3f.recycle(fADdU);
			Array3f.recycle(fAWxDdU);
			return false;
		}

		fAWxDdU.b = FastMath.abs(wCrossD.dot(Vector3f.UNIT_Y));
		rhs = xExtent * fAWdU.c + zExtent * fAWdU.a;
		if (fAWxDdU.b > rhs) {
			Vector3f.recycle(vect1);
			Vector3f.recycle(vect2);
			Array3f.recycle(fWdU);
			Array3f.recycle(fAWdU);
			Array3f.recycle(fDdU);
			Array3f.recycle(fADdU);
			Array3f.recycle(fAWxDdU);
			return false;
		}

		fAWxDdU.c = FastMath.abs(wCrossD.dot(Vector3f.UNIT_Z));
		rhs = xExtent * fAWdU.b + yExtent * fAWdU.a;
		if (fAWxDdU.c > rhs) {
			Vector3f.recycle(vect1);
			Vector3f.recycle(vect2);
			Array3f.recycle(fWdU);
			Array3f.recycle(fAWdU);
			Array3f.recycle(fDdU);
			Array3f.recycle(fADdU);
			Array3f.recycle(fAWxDdU);
			return false;
		}
		Vector3f.recycle(vect1);
		Vector3f.recycle(vect2);
		Array3f.recycle(fWdU);
		Array3f.recycle(fAWdU);
		Array3f.recycle(fDdU);
		Array3f.recycle(fADdU);
		Array3f.recycle(fAWxDdU);
		return true;
	}

	/**
	 * @see com.aionemu.gameserver.geoEngine.bounding.jme.bounding.BoundingVolume#intersectsWhere(com.jme.math.Ray)
	 */
	private int collideWithRay(Ray ray, CollisionResults results) {
		Vector3f diff = Vector3f.newInstance().set(ray.origin).subtractLocal(center);
		Vector3f direction = Vector3f.newInstance().set(ray.direction);

		float[] t = { 0f, Float.POSITIVE_INFINITY };

		float saveT0 = t[0], saveT1 = t[1];
		boolean notEntirelyClipped = clip(+direction.x, -diff.x - xExtent, t) && clip(-direction.x, +diff.x - xExtent, t) && clip(+direction.y, -diff.y - yExtent, t) && clip(-direction.y, +diff.y - yExtent, t) && clip(+direction.z, -diff.z - zExtent, t) && clip(-direction.z, +diff.z - zExtent, t);
		Vector3f.recycle(diff);
		Vector3f.recycle(direction);

		if (notEntirelyClipped && (t[0] != saveT0 || t[1] != saveT1)) {
			if (t[1] > t[0]) {
				float[] distances = t;
				Vector3f[] points = new Vector3f[] { new Vector3f(ray.direction).multLocal(distances[0]).addLocal(ray.origin), new Vector3f(ray.direction).multLocal(distances[1]).addLocal(ray.origin) };

				CollisionResult result = new CollisionResult(points[0], distances[0]);
				results.addCollision(result);
				result = new CollisionResult(points[1], distances[1]);
				results.addCollision(result);
				return 2;
			}

			Vector3f point = new Vector3f(ray.direction).multLocal(t[0]).addLocal(ray.origin);
			CollisionResult result = new CollisionResult(point, t[0]);
			results.addCollision(result);
			return 1;
		}
		return 0;
	}

	@Override
	public int collideWith(Collidable other, CollisionResults results) {
		if (other instanceof Ray) {
			Ray ray = (Ray) other;
			return collideWithRay(ray, results);
		}
		else if (other instanceof Triangle) {
			Triangle t = (Triangle) other;
			if (intersects(t.get1(), t.get2(), t.get3())) {
				CollisionResult r = new CollisionResult();
				results.addCollision(r);
				return 1;
			}
			return 0;
		}
		else {
			throw new UnsupportedCollisionException("With: " + other.getClass().getSimpleName());
		}
	}

	/**
	 * C code ported from http://www.cs.lth.se/home/Tomas_Akenine_Moller/code/tribox3.txt
	 *
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return
	 */
	public boolean intersects(Vector3f v1, Vector3f v2, Vector3f v3) {
		return Intersection.intersect(this, v1, v2, v3);
	}

	@Override
	public boolean contains(Vector3f point) {
		return FastMath.abs(center.x - point.x) < xExtent && FastMath.abs(center.y - point.y) < yExtent && FastMath.abs(center.z - point.z) < zExtent;
	}

	@Override
	public boolean intersects(Vector3f point) {
		return FastMath.abs(center.x - point.x) <= xExtent && FastMath.abs(center.y - point.y) <= yExtent && FastMath.abs(center.z - point.z) <= zExtent;
	}

	@Override
	public float distanceToEdge(Vector3f point) {
		// compute coordinates of point in box coordinate system
		Vector3f closest = point.subtract(center);

		// project test point onto box
		float sqrDistance = 0.0f;
		float delta;

		if (closest.x < -xExtent) {
			delta = closest.x + xExtent;
			sqrDistance += delta * delta;
			closest.x = -xExtent;
		}
		else if (closest.x > xExtent) {
			delta = closest.x - xExtent;
			sqrDistance += delta * delta;
			closest.x = xExtent;
		}

		if (closest.y < -yExtent) {
			delta = closest.y + yExtent;
			sqrDistance += delta * delta;
			closest.y = -yExtent;
		}
		else if (closest.y > yExtent) {
			delta = closest.y - yExtent;
			sqrDistance += delta * delta;
			closest.y = yExtent;
		}

		if (closest.z < -zExtent) {
			delta = closest.z + zExtent;
			sqrDistance += delta * delta;
			closest.z = -zExtent;
		}
		else if (closest.z > zExtent) {
			delta = closest.z - zExtent;
			sqrDistance += delta * delta;
			closest.z = zExtent;
		}

		return FastMath.sqrt(sqrDistance);
	}

	/**
	 * <code>clip</code> determines if a line segment intersects the current test plane.
	 *
	 * @param denom
	 *            the denominator of the line segment.
	 * @param numer
	 *            the numerator of the line segment.
	 * @param t
	 *            test values of the plane.
	 * @return true if the line segment intersects the plane, false otherwise.
	 */
	private boolean clip(float denom, float numer, float[] t) {
		// Return value is 'true' if line segment intersects the current test
		// plane. Otherwise 'false' is returned in which case the line segment
		// is entirely clipped.
		if (denom > 0.0f) {
			if (numer > denom * t[1]) {
				return false;
			}
			if (numer > denom * t[0]) {
				t[0] = numer / denom;
			}
			return true;
		}
		else if (denom < 0.0f) {
			if (numer > denom * t[0]) {
				return false;
			}
			if (numer > denom * t[1]) {
				t[1] = numer / denom;
			}
			return true;
		}
		else {
			return numer <= 0.0;
		}
	}

	/**
	 * Query extent.
	 *
	 * @param store
	 *            where extent gets stored - null to return a new vector
	 * @return store / new vector
	 */
	public Vector3f getExtent(Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		store.set(xExtent, yExtent, zExtent);
		return store;
	}

	public float getXExtent() {
		return xExtent;
	}

	public float getYExtent() {
		return yExtent;
	}

	public float getZExtent() {
		return zExtent;
	}

	public void setXExtent(float xExtent) {
		if (xExtent < 0) {
			throw new IllegalArgumentException();
		}

		this.xExtent = xExtent;
	}

	public void setYExtent(float yExtent) {
		if (yExtent < 0) {
			throw new IllegalArgumentException();
		}

		this.yExtent = yExtent;
	}

	public void setZExtent(float zExtent) {
		if (zExtent < 0) {
			throw new IllegalArgumentException();
		}

		this.zExtent = zExtent;
	}

	public Vector3f getMin(Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		store.set(center).subtractLocal(xExtent, yExtent, zExtent);
		return store;
	}

	public Vector3f getMax(Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		store.set(center).addLocal(xExtent, yExtent, zExtent);
		return store;
	}

	public void setMinMax(Vector3f min, Vector3f max) {
		this.center.set(max).addLocal(min).multLocal(0.5f);
		xExtent = FastMath.abs(max.x - center.x);
		yExtent = FastMath.abs(max.y - center.y);
		zExtent = FastMath.abs(max.z - center.z);
	}

	@Override
	public float getVolume() {
		return (8 * xExtent * yExtent * zExtent);
	}
}
