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

import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rolandas
 */
public class SemisphereArea extends SphereArea {

	public SemisphereArea(ZoneName zoneName, int worldId, float x, float y, float z, float r) {
		super(zoneName, worldId, x, y, z, r);
	}

	@Override
	public boolean isInside3D(Point3D point) {
		return this.z < point.getZ() && MathUtil.isIn3dRange(x, y, z, point.getX(), point.getY(), point.getZ(), r);
	}

	@Override
	public boolean isInside3D(float x, float y, float z) {
		return this.z < z && MathUtil.isIn3dRange(x, y, z, this.x, this.y, this.z, r);
	}

	@Override
	public boolean isInsideZ(Point3D point) {
		return isInsideZ(point.getZ());
	}

	@Override
	public float getMinZ() {
		return z;
	}

	@Override
	public float getMaxZ() {
		return z + r;
	}

	@Override
	public double getDistance3D(Point3D point) {
		return getDistance3D(point.getX(), point.getY(), point.getZ());
	}

	@Override
	public double getDistance3D(float x, float y, float z) {
		double distance = MathUtil.getDistance(x, y, z, this.x, this.y, this.z) - r;
		if (z < this.z) {
			return distance;
		}
		return distance > 0 ? distance : 0;
	}

	@Override
	public boolean intersectsRectangle(RectangleArea area) {
		if ((area.getMaxZ() >= z || z <= area.getMinZ()) && area.getDistance3D(x, y, z) <= r) {
			return true;
		}
		return false;
	}
}
