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
package com.aionemu.gameserver.geoEngine.scene;

import java.nio.FloatBuffer;

import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.scene.VertexBuffer.Type;
import com.aionemu.gameserver.geoEngine.utils.BufferUtils;

/**
 * A box with solid (filled) faces.
 *
 * @author Mark Powell
 * @version $Revision: 4131 $, $Date: 2009-03-19 16:15:28 -0400 (Thu, 19 Mar 2009) $
 */
public class Box extends AbstractBox {

	private static final short[] GEOMETRY_INDICES_DATA = { 2, 1, 0, 3, 2, 0, // back
		6, 5, 4, 7, 6, 4, // right
		10, 9, 8, 11, 10, 8, // front
		14, 13, 12, 15, 14, 12, // left
		18, 17, 16, 19, 18, 16, // top
		22, 21, 20, 23, 22, 20 // bottom
	};
	private static final float[] GEOMETRY_NORMALS_DATA = { 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, // back
		1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, // right
		0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, // front
		-1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, // left
		0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, // top
		0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0 // bottom
	};

	/**
	 * Creates a new box.
	 * <p/>
	 * The box has a center of 0,0,0 and extends in the out from the center by the given amount in <em>each</em> direction. So, for example, a box with extent of 0.5 would be the unit cube.
	 *
	 * @param x
	 *            the size of the box along the x axis, in both directions.
	 * @param y
	 *            the size of the box along the y axis, in both directions.
	 * @param z
	 *            the size of the box along the z axis, in both directions.
	 */
	public Box(float x, float y, float z) {
		super();
		updateGeometry(Vector3f.ZERO, x, y, z);
	}

	/**
	 * Creates a new box.
	 * <p/>
	 * The box has the given center and extends in the out from the center by the given amount in <em>each</em> direction. So, for example, a box with extent of 0.5 would be the unit cube.
	 *
	 * @param center
	 *            the center of the box.
	 * @param x
	 *            the size of the box along the x axis, in both directions.
	 * @param y
	 *            the size of the box along the y axis, in both directions.
	 * @param z
	 *            the size of the box along the z axis, in both directions.
	 */
	public Box(Vector3f center, float x, float y, float z) {
		super();
		updateGeometry(center, x, y, z);
	}

	/**
	 * Constructor instantiates a new <code>Box</code> object.
	 * <p/>
	 * The minimum and maximum point are provided, these two points define the shape and size of the box but not it's orientation or position. You should use the
	 * {@link com.jme3.scene.Spatial#setLocalTranslation(com.jme3.math.Vector3f) } and {@link com.jme3.scene.Spatial#setLocalRotation(com.jme3.math.Quaternion) } methods to define those properties.
	 *
	 * @param min
	 *            the minimum point that defines the box.
	 * @param max
	 *            the maximum point that defines the box.
	 */
	public Box(Vector3f min, Vector3f max) {
		super();
		updateGeometry(min, max);
	}

	/**
	 * Empty constructor for serialization only. Do not use.
	 */
	public Box() {
		super();
	}

	/**
	 * Creates a clone of this box.
	 * <p/>
	 * The cloned box will have '_clone' appended to it's name, but all other properties will be the same as this box.
	 */
	@Override
	public Box clone() {
		return new Box(center.clone(), xExtent, yExtent, zExtent);
	}

	@Override
	protected void duUpdateGeometryIndices() {
		if (getBuffer(Type.Index) == null) {
			setBuffer(Type.Index, 3, BufferUtils.createShortBuffer(GEOMETRY_INDICES_DATA));
		}
	}

	@Override
	protected void duUpdateGeometryNormals() {
		if (getBuffer(Type.Normal) == null) {
			setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(GEOMETRY_NORMALS_DATA));
		}
	}

	@Override
	protected void duUpdateGeometryVertices() {
		FloatBuffer fpb = BufferUtils.createVector3Buffer(24);
		Vector3f[] v = computeVertices();
		fpb.put(new float[] { v[0].x, v[0].y, v[0].z, v[1].x, v[1].y, v[1].z, v[2].x, v[2].y, v[2].z, v[3].x, v[3].y, v[3].z, // back
			v[1].x, v[1].y, v[1].z, v[4].x, v[4].y, v[4].z, v[6].x, v[6].y, v[6].z, v[2].x, v[2].y, v[2].z, // right
			v[4].x, v[4].y, v[4].z, v[5].x, v[5].y, v[5].z, v[7].x, v[7].y, v[7].z, v[6].x, v[6].y, v[6].z, // front
			v[5].x, v[5].y, v[5].z, v[0].x, v[0].y, v[0].z, v[3].x, v[3].y, v[3].z, v[7].x, v[7].y, v[7].z, // left
			v[2].x, v[2].y, v[2].z, v[6].x, v[6].y, v[6].z, v[7].x, v[7].y, v[7].z, v[3].x, v[3].y, v[3].z, // top
			v[0].x, v[0].y, v[0].z, v[5].x, v[5].y, v[5].z, v[4].x, v[4].y, v[4].z, v[1].x, v[1].y, v[1].z // bottom
		});
		setBuffer(Type.Position, 3, fpb);
		updateBound();
	}
}
