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

import com.aionemu.gameserver.geoEngine.math.Vector3f;

/**
 * An eight sided box.
 * <p/>
 * A {@code Box} is defined by a minimal point and a maximal point. The eight vertices that make the box are then computed, they are computed in such a way as to generate an axis-aligned box.
 * <p/>
 * This class does not control how the geometry data is generated, see {@link Box} for that.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 * @version $Revision: 4131 $, $Date: 2009-03-19 16:15:28 -0400 (Thu, 19 Mar 2009) $
 */
public abstract class AbstractBox extends Mesh {

	public final Vector3f center = new Vector3f(0f, 0f, 0f);
	public float xExtent, yExtent, zExtent;

	public AbstractBox() {
		super();
	}

	/**
	 * Gets the array or vectors representing the 8 vertices of the box.
	 *
	 * @return a newly created array of vertex vectors.
	 */
	protected final Vector3f[] computeVertices() {
		Vector3f[] axes = { Vector3f.UNIT_X.mult(xExtent), Vector3f.UNIT_Y.mult(yExtent), Vector3f.UNIT_Z.mult(zExtent) };
		return new Vector3f[] { center.subtract(axes[0]).subtractLocal(axes[1]).subtractLocal(axes[2]), center.add(axes[0]).subtractLocal(axes[1]).subtractLocal(axes[2]), center.add(axes[0]).addLocal(axes[1]).subtractLocal(axes[2]), center.subtract(axes[0]).addLocal(axes[1]).subtractLocal(axes[2]), center.add(axes[0]).subtractLocal(axes[1]).addLocal(axes[2]), center.subtract(axes[0]).subtractLocal(axes[1]).addLocal(axes[2]), center.add(axes[0]).addLocal(axes[1]).addLocal(axes[2]), center.subtract(axes[0]).addLocal(axes[1]).addLocal(axes[2]) };
	}

	/**
	 * Convert the indices into the list of vertices that define the box's geometry.
	 */
	protected abstract void duUpdateGeometryIndices();

	/**
	 * Update the normals of each of the box's planes.
	 */
	protected abstract void duUpdateGeometryNormals();

	/**
	 * Update the position of the vertices that define the box.
	 * <p/>
	 * These eight points are determined from the minimum and maximum point.
	 */
	protected abstract void duUpdateGeometryVertices();

	/**
	 * Get the center point of this box.
	 */
	public final Vector3f getCenter() {
		return center;
	}

	/**
	 * Get the x-axis size (extent) of this box.
	 */
	public final float getXExtent() {
		return xExtent;
	}

	/**
	 * Get the y-axis size (extent) of this box.
	 */
	public final float getYExtent() {
		return yExtent;
	}

	/**
	 * Get the z-axis size (extent) of this box.
	 */
	public final float getZExtent() {
		return zExtent;
	}

	/**
	 * Rebuilds the box after a property has been directly altered.
	 * <p/>
	 * For example, if you call {@code getXExtent().x = 5.0f} then you will need to call this method afterwards in order to update the box.
	 */
	public final void updateGeometry() {
		duUpdateGeometryVertices();
		duUpdateGeometryNormals();
		duUpdateGeometryIndices();
	}

	/**
	 * Rebuilds this box based on a new set of parameters.
	 * <p/>
	 * Note that the actual sides will be twice the given extent values because the box extends in both directions from the center for each extent.
	 *
	 * @param center
	 *            the center of the box.
	 * @param x
	 *            the x extent of the box, in each directions.
	 * @param y
	 *            the y extent of the box, in each directions.
	 * @param z
	 *            the z extent of the box, in each directions.
	 */
	public final void updateGeometry(Vector3f center, float x, float y, float z) {
		if (center != null) {
			this.center.set(center);
		}
		this.xExtent = x;
		this.yExtent = y;
		this.zExtent = z;
		updateGeometry();
	}

	/**
	 * Rebuilds this box based on a new set of parameters.
	 * <p/>
	 * The box is updated so that the two opposite corners are {@code minPoint} and {@code maxPoint}, the other corners are created from those two positions.
	 *
	 * @param minPoint
	 *            the new minimum point of the box.
	 * @param maxPoint
	 *            the new maximum point of the box.
	 */
	public final void updateGeometry(Vector3f minPoint, Vector3f maxPoint) {
		center.set(maxPoint).addLocal(minPoint).multLocal(0.5f);
		float x = maxPoint.x - center.x;
		float y = maxPoint.y - center.y;
		float z = maxPoint.z - center.z;
		updateGeometry(center, x, y, z);
	}
}
