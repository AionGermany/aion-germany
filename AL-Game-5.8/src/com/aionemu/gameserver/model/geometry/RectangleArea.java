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

import java.awt.Point;
import java.awt.Rectangle;

import com.aionemu.gameserver.model.templates.zone.Point2D;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * Rectangle area, most wide spread in the game
 *
 * @author SoulKeeper
 */
public class RectangleArea extends AbstractArea {

	/**
	 * Min x point
	 */
	private final float minX;

	/**
	 * @return the minX
	 */
	public float getMinX() {
		return minX;
	}

	/**
	 * @return the maxX
	 */
	public float getMaxX() {
		return maxX;
	}

	/**
	 * @return the minY
	 */
	public float getMinY() {
		return minY;
	}

	/**
	 * @return the maxY
	 */
	public float getMaxY() {
		return maxY;
	}

	/**
	 * Max x point
	 */
	private final float maxX;
	/**
	 * Min y point
	 */
	private final float minY;
	/**
	 * Max y point
	 */
	private final float maxY;

	/**
	 * Creates new area from given points. Point order doesn't matter
	 *
	 * @param p1
	 *            point
	 * @param p2
	 *            point
	 * @param p3
	 *            point
	 * @param p4
	 *            point
	 * @param minZ
	 *            minimal z
	 * @param maxZ
	 *            maximal z
	 */
	public RectangleArea(ZoneName zoneName, int worldId, Point p1, Point p2, Point p3, Point p4, int minZ, int maxZ) {
		super(zoneName, worldId, minZ, maxZ);

		Rectangle r = new Rectangle();
		r.add(p1);
		r.add(p2);
		r.add(p3);
		r.add(p4);

		minX = (int) r.getMinX();
		maxX = (int) r.getMaxX();
		minY = (int) r.getMinY();
		maxY = (int) r.getMaxY();
	}

	/**
	 * Creates new are from given coords
	 *
	 * @param minX
	 *            mimal x point
	 * @param minY
	 *            minimal y point
	 * @param maxX
	 *            maximal x point
	 * @param maxY
	 *            maximal y point
	 * @param minZ
	 *            minimal z point
	 * @param maxZ
	 *            maximal z point
	 */
	public RectangleArea(ZoneName zoneName, int worldId, float minX, float minY, float maxX, float maxY, float minZ, float maxZ) {
		super(zoneName, worldId, minZ, maxZ);
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInside2D(float x, float y) {
		return x >= minX && x <= maxX && y >= minY && y <= maxY;
	}

	@Override
	public boolean isInside3D(float x, float y, float z) {
		if (!isInside2D(x, y)) {
			return false;
		}
		return super.isInside3D(x, y, z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDistance2D(float x, float y) {
		if (isInside2D(x, y)) {
			return 0;
		}
		else {
			Point2D cp = getClosestPoint(x, y);
			return MathUtil.getDistance(x, y, cp.getX(), cp.getY());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDistance3D(float x, float y, float z) {
		if (isInside3D(x, y, z)) {
			return 0;
		}
		else if (isInsideZ(z)) {
			return getDistance2D(x, y);
		}
		else {
			Point3D cp = getClosestPoint(x, y, z);
			return MathUtil.getDistance(x, y, z, cp.getX(), cp.getY(), cp.getZ());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point2D getClosestPoint(float x, float y) {

		if (isInside2D(x, y)) {
			return new Point2D(x, y);
		}
		else {
			// bottom edge
			Point2D closestPoint = MathUtil.getClosestPointOnSegment(minX, minY, maxX, minY, x, y);
			double distance = MathUtil.getDistance(x, y, closestPoint.getX(), closestPoint.getY());

			// top edge
			Point2D cp = MathUtil.getClosestPointOnSegment(minX, maxY, maxX, maxY, x, y);
			double d = MathUtil.getDistance(x, y, cp.getX(), cp.getY());
			if (d < distance) {
				closestPoint = cp;
				distance = d;
			}

			// left edge
			cp = MathUtil.getClosestPointOnSegment(minX, minY, minX, maxY, x, y);
			d = MathUtil.getDistance(x, y, cp.getX(), cp.getY());
			if (d < distance) {
				closestPoint = cp;
				distance = d;
			}

			// Right edge
			cp = MathUtil.getClosestPointOnSegment(maxX, minY, maxX, maxY, x, y);
			d = MathUtil.getDistance(x, y, cp.getX(), cp.getY());
			if (d < distance) {
				closestPoint = cp;
				// distance = d;
			}

			return closestPoint;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aionemu.gameserver.model.geometry.Area#intersectsRectangle(com.aionemu.gameserver.model.geometry.RectangleArea)
	 */
	@Override
	public boolean intersectsRectangle(RectangleArea area) {
		// TODO Auto-generated method stub
		return false;
	}
}
