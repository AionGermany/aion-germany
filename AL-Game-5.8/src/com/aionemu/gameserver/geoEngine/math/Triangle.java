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

import com.aionemu.gameserver.configs.main.GeoDataConfig;

import javolution.context.ObjectFactory;
import javolution.lang.Reusable;

/**
 * <code>Triangle</code> defines a object for containing triangle information. The triangle is defined by a collection of three <code>Vector3f</code> objects.
 *
 * @author Mark Powell
 * @author Joshua Slack
 */
public class Triangle extends AbstractTriangle implements Reusable {

	@SuppressWarnings("rawtypes")
	private static final ObjectFactory FACTORY = new ObjectFactory() {

		@Override
		public Object create() {
			return new Triangle();
		}
	};
	private Vector3f pointa = new Vector3f();
	private Vector3f pointb = new Vector3f();
	private Vector3f pointc = new Vector3f();
	private transient Vector3f center;
	private transient Vector3f normal;
	private float projection;
	private int index;

	public Triangle() {
	}

	/**
	 * Constructor instantiates a new <Code>Triangle</code> object with the supplied vectors as the points. It is recommended that the vertices be supplied in a counter clockwise winding to support
	 * normals for a right handed coordinate system.
	 *
	 * @param p1
	 *            the first point of the triangle.
	 * @param p2
	 *            the second point of the triangle.
	 * @param p3
	 *            the third point of the triangle.
	 */
	public Triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
		pointa.set(p1);
		pointb.set(p2);
		pointc.set(p3);
	}

	/**
	 * <code>get</code> retrieves a point on the triangle denoted by the index supplied.
	 *
	 * @param i
	 *            the index of the point.
	 * @return the point.
	 */
	public Vector3f get(int i) {
		switch (i) {
			case 0:
				return pointa;
			case 1:
				return pointb;
			case 2:
				return pointc;
			default:
				return null;
		}
	}

	@Override
	public Vector3f get1() {
		return pointa;
	}

	@Override
	public Vector3f get2() {
		return pointb;
	}

	@Override
	public Vector3f get3() {
		return pointc;
	}

	/**
	 * <code>set</code> sets one of the triangles points to that specified as a parameter.
	 *
	 * @param i
	 *            the index to place the point.
	 * @param point
	 *            the point to set.
	 */
	public void set(int i, Vector3f point) {
		switch (i) {
			case 0:
				pointa.set(point);
				break;
			case 1:
				pointb.set(point);
				break;
			case 2:
				pointc.set(point);
				break;
		}
	}

	/**
	 * <code>set</code> sets one of the triangles points to that specified as a parameter.
	 *
	 * @param i
	 *            the index to place the point.
	 * @param point
	 *            the point to set.
	 */
	public void set(int i, float x, float y, float z) {
		switch (i) {
			case 0:
				pointa.set(x, y, z);
				break;
			case 1:
				pointb.set(x, y, z);
				break;
			case 2:
				pointc.set(x, y, z);
				break;
		}
	}

	public void set1(Vector3f v) {
		pointa.set(v);
	}

	public void set2(Vector3f v) {
		pointb.set(v);
	}

	public void set3(Vector3f v) {
		pointc.set(v);
	}

	@Override
	public void set(Vector3f v1, Vector3f v2, Vector3f v3) {
		pointa.set(v1);
		pointb.set(v2);
		pointc.set(v3);
	}

	/**
	 * calculateCenter finds the average point of the triangle.
	 */
	public void calculateCenter() {
		if (center == null) {
			center = new Vector3f(pointa);
		}
		else {
			center.set(pointa);
		}
		center.addLocal(pointb).addLocal(pointc).multLocal(FastMath.ONE_THIRD);
	}

	/**
	 * calculateCenter finds the average point of the triangle.
	 */
	public void calculateNormal() {
		if (normal == null) {
			normal = new Vector3f(pointb);
		}
		else {
			normal.set(pointb);
		}
		normal.subtractLocal(pointa).crossLocal(pointc.x - pointa.x, pointc.y - pointa.y, pointc.z - pointa.z);
		normal.normalizeLocal();
	}

	/**
	 * obtains the center point of this triangle (average of the three triangles)
	 *
	 * @return the center point.
	 */
	public Vector3f getCenter() {
		if (center == null) {
			calculateCenter();
		}
		return center;
	}

	/**
	 * sets the center point of this triangle (average of the three triangles)
	 *
	 * @param center
	 *            the center point.
	 */
	public void setCenter(Vector3f center) {
		this.center = center;
	}

	/**
	 * obtains the unit length normal vector of this triangle, if set or calculated
	 *
	 * @return the normal vector
	 */
	public Vector3f getNormal() {
		if (normal == null) {
			calculateNormal();
		}
		return normal;
	}

	/**
	 * sets the normal vector of this triangle (to conform, must be unit length)
	 *
	 * @param normal
	 *            the normal vector.
	 */
	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

	/**
	 * obtains the projection of the vertices relative to the line origin.
	 *
	 * @return the projection of the triangle.
	 */
	public float getProjection() {
		return this.projection;
	}

	/**
	 * sets the projection of the vertices relative to the line origin.
	 *
	 * @param projection
	 *            the projection of the triangle.
	 */
	public void setProjection(float projection) {
		this.projection = projection;
	}

	/**
	 * obtains an index that this triangle represents if it is contained in a OBBTree.
	 *
	 * @return the index in an OBBtree
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * sets an index that this triangle represents if it is contained in a OBBTree.
	 *
	 * @param index
	 *            the index in an OBBtree
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public static Vector3f computeTriangleNormal(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f store) {
		if (store == null) {
			store = new Vector3f(v2);
		}
		else {
			store.set(v2);
		}

		store.subtractLocal(v1).crossLocal(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
		return store.normalizeLocal();
	}

	public Class<? extends Triangle> getClassTag() {
		return this.getClass();
	}

	@Override
	public Triangle clone() {
		try {
			Triangle t = (Triangle) super.clone();
			t.pointa = pointa.clone();
			t.pointb = pointb.clone();
			t.pointc = pointc.clone();
			return t;
		}
		catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javolution.lang.Reusable#reset()
	 */
	@Override
	public void reset() {
		pointa.reset();
		pointb.reset();
		pointc.reset();
		center = null;
		normal = null;
		projection = 0;
		index = 0;
	}

	/**
	 * Returns a new, preallocated or {@link #recycle recycled} text builder (on the stack when executing in a {@link javolution.context.StackContext StackContext}).
	 *
	 * @return a new, preallocated or recycled text builder instance.
	 */
	public static Triangle newInstance() {
		if (GeoDataConfig.GEO_OBJECT_FACTORY_ENABLE) {
			return (Triangle) FACTORY.object();
		}
		else {
			return new Triangle();
		}
	}

	/**
	 * Recycles a text builder {@link #newInstance() instance} immediately (on the stack when executing in a {@link javolution.context.StackContext StackContext}).
	 */
	@SuppressWarnings("unchecked")
	public static void recycle(Triangle instance) {
		if (GeoDataConfig.GEO_OBJECT_FACTORY_ENABLE) {
			FACTORY.recycle(instance);
		}
		else {
			instance = null;
		}
	}
}
