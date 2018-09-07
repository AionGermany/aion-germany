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
package com.aionemu.gameserver.utils;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;

/**
 * @author ATracer
 */
public class PositionUtil {

	private static final float MAX_ANGLE_DIFF = 90f;

	/**
	 * @param object1
	 * @param object2
	 * @return true or false
	 */
	public static boolean isBehindTarget(VisibleObject object1, VisibleObject object2) {
		float angleObject1 = MathUtil.calculateAngleFrom(object1, object2);
		float angleObject2 = MathUtil.convertHeadingToDegree(object2.getHeading());
		float angleDiff = angleObject1 - angleObject2;

		if (angleDiff <= -360 + MAX_ANGLE_DIFF) {
			angleDiff += 360;
		}
		if (angleDiff >= 360 - MAX_ANGLE_DIFF) {
			angleDiff -= 360;
		}
		return Math.abs(angleDiff) <= MAX_ANGLE_DIFF;
	}

	/**
	 * @param object1
	 * @param object2
	 * @return true or false
	 */
	public static boolean isInFrontOfTarget(VisibleObject object1, VisibleObject object2) {
		float angleObject2 = MathUtil.calculateAngleFrom(object2, object1);
		float angleObject1 = MathUtil.convertHeadingToDegree(object2.getHeading());
		float angleDiff = angleObject1 - angleObject2;

		if (angleDiff <= -360 + MAX_ANGLE_DIFF) {
			angleDiff += 360;
		}
		if (angleDiff >= 360 - MAX_ANGLE_DIFF) {
			angleDiff -= 360;
		}
		return Math.abs(angleDiff) <= MAX_ANGLE_DIFF;
	}

	/**
	 * Analyse two object position by coordinates
	 *
	 * @param object1
	 * @param object2
	 * @return true if the analysed object is behind base object
	 */
	public static boolean isBehind(VisibleObject object1, VisibleObject object2) {
		float angle = MathUtil.convertHeadingToDegree(object1.getHeading()) + 90;
		if (angle >= 360) {
			angle -= 360;
		}
		double radian = Math.toRadians(angle);
		float x0 = object1.getX();
		float y0 = object1.getY();
		float x1 = (float) (Math.cos(radian) * 5) + x0;
		float y1 = (float) (Math.sin(radian) * 5) + y0;
		float xA = object2.getX();
		float yA = object2.getY();
		float temp = (x1 - x0) * (yA - y0) - (y1 - y0) * (xA - x0);
		return temp > 0;
	}

	/**
	 * <pre>
	 *       0 (head view)
	 *  270     90
	 *      180  (back)
	 * </pre>
	 */
	public static float getAngleToTarget(VisibleObject object1, VisibleObject object2) {
		float angleObject1 = MathUtil.convertHeadingToDegree(object1.getHeading()) - 180;
		if (angleObject1 < 0) {
			angleObject1 += 360;
		}
		float angleObject2 = MathUtil.calculateAngleFrom(object1, object2);
		float angleDiff = angleObject1 - angleObject2 - 180;
		if (angleDiff < 0) {
			angleDiff += 360;
		}
		return angleDiff;
	}

	public static float getDirectionalBound(VisibleObject object1, VisibleObject object2, boolean inverseTarget) {
		float angle = 90 - (inverseTarget ? getAngleToTarget(object2, object1) : getAngleToTarget(object1, object2));
		if (angle < 0) {
			angle += 360;
		}
		double radians = Math.toRadians(angle);
		float x1 = (float) (object1.getX() + object1.getObjectTemplate().getBoundRadius().getSide() * Math.cos(radians));
		float y1 = (float) (object1.getY() + object1.getObjectTemplate().getBoundRadius().getFront() * Math.sin(radians));
		float x2 = (float) (object2.getX() + object2.getObjectTemplate().getBoundRadius().getSide() * Math.cos(Math.PI + radians));
		float y2 = (float) (object2.getY() + object2.getObjectTemplate().getBoundRadius().getFront() * Math.sin(Math.PI + radians));
		float bound1 = (float) MathUtil.getDistance(object1.getX(), object1.getY(), x1, y1);
		float bound2 = (float) MathUtil.getDistance(object2.getX(), object2.getY(), x2, y2);
		return bound1 - bound2;
	}

	public static float getDirectionalBound(VisibleObject object1, VisibleObject object2) {
		return getDirectionalBound(object1, object2, false);
	}

	public static byte getMoveAwayHeading(VisibleObject fromObject, VisibleObject object) {
		float angle = MathUtil.calculateAngleFrom(fromObject, object);
		byte heading = MathUtil.convertDegreeToHeading(angle);
		return heading;
	}
}
