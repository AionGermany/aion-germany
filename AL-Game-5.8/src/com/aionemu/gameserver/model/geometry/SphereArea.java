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
package com.aionemu.gameserver.model.geometry;

import com.aionemu.gameserver.model.templates.zone.Point2D;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author MrPoke
 */
public class SphereArea implements Area {

	protected float x;
	protected float y;
	protected float z;
	protected float r;
	protected int worldId;
	protected ZoneName zoneName;

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param r
	 * @param worldId
	 * @param zoneName
	 */
	public SphereArea(ZoneName zoneName, int worldId, float x, float y, float z, float r) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.worldId = worldId;
		this.zoneName = zoneName;
	}

	@Deprecated
	@Override
	public boolean isInside2D(Point2D point) {
		return false;
	}

	@Deprecated
	@Override
	public boolean isInside2D(float x, float y) {
		return false;
	}

	@Override
	public boolean isInside3D(Point3D point) {
		return MathUtil.isIn3dRange(x, y, z, point.getX(), point.getY(), point.getZ(), r);
	}

	@Override
	public boolean isInside3D(float x, float y, float z) {
		return MathUtil.isIn3dRange(x, y, z, this.x, this.y, this.z, r);
	}

	@Override
	public boolean isInsideZ(Point3D point) {
		return isInsideZ(point.getZ());
	}

	@Override
	public boolean isInsideZ(float z) {
		return z >= this.getMinZ() && z <= this.getMaxZ();
	}

	@Deprecated
	@Override
	public double getDistance2D(Point2D point) {
		return 0;
	}

	@Deprecated
	@Override
	public double getDistance2D(float x, float y) {
		return 0;
	}

	@Override
	public double getDistance3D(Point3D point) {
		return getDistance3D(point.getX(), point.getY(), point.getZ());
	}

	@Override
	public double getDistance3D(float x, float y, float z) {
		double distance = MathUtil.getDistance(x, y, z, this.x, this.y, this.z) - r;
		return distance > 0 ? distance : 0;
	}

	@Deprecated
	@Override
	public Point2D getClosestPoint(Point2D point) {
		return null;
	}

	@Deprecated
	@Override
	public Point2D getClosestPoint(float x, float y) {
		return null;
	}

	@Override
	public Point3D getClosestPoint(Point3D point) {
		return null;
	}

	@Override
	public Point3D getClosestPoint(float x, float y, float z) {
		return null;
	}

	@Override
	public float getMinZ() {
		return z - r;
	}

	@Override
	public float getMaxZ() {
		return z + r;
	}

	@Override
	public boolean intersectsRectangle(RectangleArea area) {
		if (area.getDistance3D(x, y, z) <= r) {
			return true;
		}
		return false;
	}

	@Override
	public int getWorldId() {
		return worldId;
	}

	@Override
	public ZoneName getZoneName() {
		return zoneName;
	}
}
