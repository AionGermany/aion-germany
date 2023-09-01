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

import java.util.Random;

/**
 * <code>FastMath</code> provides 'fast' math approximations and float equivalents of Math functions. These are all used as static values and functions.
 *
 * @author Various
 * @version $Id: FastMath.java,v 1.45 2007/08/26 08:44:20 irrisor Exp $
 */
final public class FastMath {

	private FastMath() {
	}

	/**
	 * A "close to zero" double epsilon value for use
	 */
	public static final double DBL_EPSILON = 2.220446049250313E-16d;
	/**
	 * A "close to zero" float epsilon value for use
	 */
	public static final float FLT_EPSILON = 1.1920928955078125E-7f;
	/**
	 * A "close to zero" float epsilon value for use
	 */
	public static final float ZERO_TOLERANCE = 0.0001f;
	public static final float ONE_THIRD = 1f / 3f;
	/**
	 * The value PI as a float. (180 degrees)
	 */
	public static final float PI = (float) Math.PI;
	/**
	 * The value 2PI as a float. (360 degrees)
	 */
	public static final float TWO_PI = 2.0f * PI;
	/**
	 * The value PI/2 as a float. (90 degrees)
	 */
	public static final float HALF_PI = 0.5f * PI;
	/**
	 * The value PI/4 as a float. (45 degrees)
	 */
	public static final float QUARTER_PI = 0.25f * PI;
	/**
	 * The value 1/PI as a float.
	 */
	public static final float INV_PI = 1.0f / PI;
	/**
	 * The value 1/(2PI) as a float.
	 */
	public static final float INV_TWO_PI = 1.0f / TWO_PI;
	/**
	 * A value to multiply a degree value by, to convert it to radians.
	 */
	public static final float DEG_TO_RAD = PI / 180.0f;
	/**
	 * A value to multiply a radian value by, to convert it to degrees.
	 */
	public static final float RAD_TO_DEG = 180.0f / PI;
	/**
	 * A precreated random object for random numbers.
	 */
	public static final Random rand = new Random(System.currentTimeMillis());

	/**
	 * Returns true if the number is a power of 2 (2,4,8,16...)
	 * <p/>
	 * A good implementation found on the Java boards. note: a number is a power of two if and only if it is the smallest number with that number of significant bits. Therefore, if you subtract 1, you
	 * know that the new number will have fewer bits, so ANDing the original number with anything less than it will give 0.
	 *
	 * @param number
	 *            The number to test.
	 * @return True if it is a power of two.
	 */
	public static boolean isPowerOfTwo(int number) {
		return (number > 0) && (number & (number - 1)) == 0;
	}

	public static int nearestPowerOfTwo(int number) {
		return (int) Math.pow(2, Math.ceil(Math.log(number) / Math.log(2)));
	}

	/**
	 * Linear interpolation from startValue to endValue by the given percent. Basically: ((1 - percent) * startValue) + (percent * endValue)
	 *
	 * @param scale
	 *            scale value to use. if 1, use endValue, if 0, use startValue.
	 * @param startValue
	 *            Begining value. 0% of f
	 * @param endValue
	 *            ending value. 100% of f
	 * @return The interpolated value between startValue and endValue.
	 */
	public static float interpolateLinear(float scale, float startValue, float endValue) {
		if (startValue == endValue) {
			return startValue;
		}
		if (scale <= 0f) {
			return startValue;
		}
		if (scale >= 1f) {
			return endValue;
		}
		return ((1f - scale) * startValue) + (scale * endValue);
	}

	/**
	 * Linear interpolation from startValue to endValue by the given percent. Basically: ((1 - percent) * startValue) + (percent * endValue)
	 *
	 * @param scale
	 *            scale value to use. if 1, use endValue, if 0, use startValue.
	 * @param startValue
	 *            Begining value. 0% of f
	 * @param endValue
	 *            ending value. 100% of f
	 * @return The interpolated value between startValue and endValue.
	 */
	public static Vector3f interpolateLinear(float scale, Vector3f startValue, Vector3f endValue) {
		Vector3f res = new Vector3f();
		res.x = interpolateLinear(scale, startValue.x, endValue.x);
		res.y = interpolateLinear(scale, startValue.y, endValue.y);
		res.z = interpolateLinear(scale, startValue.z, endValue.z);
		return res;
	}

	/**
	 * Interpolate a spline between at least 4 control points following the Catmull-Rom equation. here is the interpolation matrix m = [ 0.0 1.0 0.0 0.0 ] [-T 0.0 T 0.0 ] [ 2T T-3 3-2T -T ] [-T 2-T
	 * T-2 T ] where T is the curve tension the result is a value between p1 and p2, t=0 for p1, t=1 for p2
	 *
	 * @param u
	 *            value from 0 to 1
	 * @param T
	 *            The tension of the curve
	 * @param p0
	 *            control point 0
	 * @param p1
	 *            control point 1
	 * @param p2
	 *            control point 2
	 * @param p3
	 *            control point 3
	 * @return catmull-Rom interpolation
	 */
	public static float interpolateCatmullRom(float u, float T, float p0, float p1, float p2, float p3) {
		double c1, c2, c3, c4;
		c1 = p1;
		c2 = -1.0 * T * p0 + T * p2;
		c3 = 2 * T * p0 + (T - 3) * p1 + (3 - 2 * T) * p2 + -T * p3;
		c4 = -T * p0 + (2 - T) * p1 + (T - 2) * p2 + T * p3;

		return (float) (((c4 * u + c3) * u + c2) * u + c1);
	}

	/**
	 * Interpolate a spline between at least 4 control points following the Catmull-Rom equation. here is the interpolation matrix m = [ 0.0 1.0 0.0 0.0 ] [-T 0.0 T 0.0 ] [ 2T T-3 3-2T -T ] [-T 2-T
	 * T-2 T ] where T is the tension of the curve the result is a value between p1 and p2, t=0 for p1, t=1 for p2
	 *
	 * @param u
	 *            value from 0 to 1
	 * @param T
	 *            The tension of the curve
	 * @param p0
	 *            control point 0
	 * @param p1
	 *            control point 1
	 * @param p2
	 *            control point 2
	 * @param p3
	 *            control point 3
	 * @return catmull-Rom interpolation
	 */
	public static Vector3f interpolateCatmullRom(float u, float T, Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
		Vector3f res = new Vector3f();
		res.x = interpolateCatmullRom(u, T, p0.x, p1.x, p2.x, p3.x);
		res.y = interpolateCatmullRom(u, T, p0.y, p1.y, p2.y, p3.y);
		res.z = interpolateCatmullRom(u, T, p0.z, p1.z, p2.z, p3.z);
		return res;
	}

	/**
	 * Returns the arc cosine of an angle given in radians.<br>
	 * Special cases:
	 * <ul>
	 * <li>If fValue is smaller than -1, then the result is PI.
	 * <li>If the argument is greater than 1, then the result is 0.
	 * </ul>
	 *
	 * @param fValue
	 *            The angle, in radians.
	 * @return fValue's acos
	 * @see java.lang.Math#acos(double)
	 */
	public static float acos(float fValue) {
		if (-1.0f < fValue) {
			if (fValue < 1.0f) {
				return (float) Math.acos(fValue);
			}

			return 0.0f;
		}

		return PI;
	}

	/**
	 * Returns the arc sine of an angle given in radians.<br>
	 * Special cases:
	 * <ul>
	 * <li>If fValue is smaller than -1, then the result is -HALF_PI.
	 * <li>If the argument is greater than 1, then the result is HALF_PI.
	 * </ul>
	 *
	 * @param fValue
	 *            The angle, in radians.
	 * @return fValue's asin
	 * @see java.lang.Math#asin(double)
	 */
	public static float asin(float fValue) {
		if (-1.0f < fValue) {
			if (fValue < 1.0f) {
				return (float) Math.asin(fValue);
			}

			return HALF_PI;
		}

		return -HALF_PI;
	}

	/**
	 * Returns the arc tangent of an angle given in radians.<br>
	 *
	 * @param fValue
	 *            The angle, in radians.
	 * @return fValue's asin
	 * @see java.lang.Math#atan(double)
	 */
	public static float atan(float fValue) {
		return (float) Math.atan(fValue);
	}

	/**
	 * A direct call to Math.atan2.
	 *
	 * @param fY
	 * @param fX
	 * @return Math.atan2(fY, fX)
	 * @see java.lang.Math#atan2(double, double)
	 */
	public static float atan2(float fY, float fX) {
		return (float) Math.atan2(fY, fX);
	}

	/**
	 * Rounds a fValue up. A call to Math.ceil
	 *
	 * @param fValue
	 *            The value.
	 * @return The fValue rounded up
	 * @see java.lang.Math#ceil(double)
	 */
	public static float ceil(float fValue) {
		return (float) Math.ceil(fValue);
	}

	/**
	 * Fast Trig functions for x86. This forces the trig functiosn to stay within the safe area on the x86 processor (-45 degrees to +45 degrees) The results may be very slightly off from what the
	 * Math and StrictMath trig functions give due to rounding in the angle reduction but it will be very very close.
	 * <p/>
	 * note: code from wiki posting on java.net by jeffpk
	 */
	public static float reduceSinAngle(float radians) {
		radians %= TWO_PI; // put us in -2PI to +2PI space
		if (Math.abs(radians) > PI) { // put us in -PI to +PI space
			radians = radians - (TWO_PI);
		}
		if (Math.abs(radians) > HALF_PI) {// put us in -PI/2 to +PI/2 space
			radians = PI - radians;
		}

		return radians;
	}

	/**
	 * Returns sine of a value.
	 * <p/>
	 * note: code from wiki posting on java.net by jeffpk
	 *
	 * @param fValue
	 *            The value to sine, in radians.
	 * @return The sine of fValue.
	 * @see java.lang.Math#sin(double)
	 */
	public static float sin2(float fValue) {
		fValue = reduceSinAngle(fValue); // limits angle to between -PI/2 and +PI/2
		if (Math.abs(fValue) <= Math.PI / 4) {
			return (float) Math.sin(fValue);
		}

		return (float) Math.cos(Math.PI / 2 - fValue);
	}

	/**
	 * Returns cos of a value.
	 *
	 * @param fValue
	 *            The value to cosine, in radians.
	 * @return The cosine of fValue.
	 * @see java.lang.Math#cos(double)
	 */
	public static float cos2(float fValue) {
		return sin2(fValue + HALF_PI);
	}

	public static float cos(float v) {
		return (float) Math.cos(v);
	}

	public static float sin(float v) {
		return (float) Math.sin(v);
	}

	/**
	 * Returns E^fValue
	 *
	 * @param fValue
	 *            Value to raise to a power.
	 * @return The value E^fValue
	 * @see java.lang.Math#exp(double)
	 */
	public static float exp(float fValue) {
		return (float) Math.exp(fValue);
	}

	/**
	 * Returns Absolute value of a float.
	 *
	 * @param fValue
	 *            The value to abs.
	 * @return The abs of the value.
	 * @see java.lang.Math#abs(float)
	 */
	public static float abs(float fValue) {
		if (fValue < 0) {
			return -fValue;
		}
		return fValue;
	}

	/**
	 * Returns a number rounded down.
	 *
	 * @param fValue
	 *            The value to round
	 * @return The given number rounded down
	 * @see java.lang.Math#floor(double)
	 */
	public static float floor(float fValue) {
		return (float) Math.floor(fValue);
	}

	/**
	 * Returns 1/sqrt(fValue)
	 *
	 * @param fValue
	 *            The value to process.
	 * @return 1/sqrt(fValue)
	 * @see java.lang.Math#sqrt(double)
	 */
	public static float invSqrt(float fValue) {
		return (float) (1.0f / Math.sqrt(fValue));
	}

	public static float fastInvSqrt(float x) {
		float xhalf = 0.5f * x;
		int i = Float.floatToIntBits(x); // get bits for floating value
		i = 0x5f375a86 - (i >> 1); // gives initial guess y0
		x = Float.intBitsToFloat(i); // convert bits back to float
		x = x * (1.5f - xhalf * x * x); // Newton step, repeating increases accuracy
		return x;
	}

	/**
	 * Returns the log base E of a value.
	 *
	 * @param fValue
	 *            The value to log.
	 * @return The log of fValue base E
	 * @see java.lang.Math#log(double)
	 */
	public static float log(float fValue) {
		return (float) Math.log(fValue);
	}

	/**
	 * Returns the logarithm of value with given base, calculated as log(value)/log(base), so that pow(base, return)==value (contributed by vear)
	 *
	 * @param value
	 *            The value to log.
	 * @param base
	 *            Base of logarithm.
	 * @return The logarithm of value with given base
	 */
	public static float log(float value, float base) {
		return (float) (Math.log(value) / Math.log(base));
	}

	/**
	 * Returns a number raised to an exponent power. fBase^fExponent
	 *
	 * @param fBase
	 *            The base value (IE 2)
	 * @param fExponent
	 *            The exponent value (IE 3)
	 * @return base raised to exponent (IE 8)
	 * @see java.lang.Math#pow(double, double)
	 */
	public static float pow(float fBase, float fExponent) {
		return (float) Math.pow(fBase, fExponent);
	}

	/**
	 * Returns the value squared. fValue ^ 2
	 *
	 * @param fValue
	 *            The vaule to square.
	 * @return The square of the given value.
	 */
	public static float sqr(float fValue) {
		return fValue * fValue;
	}

	/**
	 * Returns the square root of a given value.
	 *
	 * @param fValue
	 *            The value to sqrt.
	 * @return The square root of the given value.
	 * @see java.lang.Math#sqrt(double)
	 */
	public static float sqrt(float fValue) {
		return (float) Math.sqrt(fValue);
	}

	/**
	 * Returns the tangent of a value. If USE_FAST_TRIG is enabled, an approximate value is returned. Otherwise, a direct value is used.
	 *
	 * @param fValue
	 *            The value to tangent, in radians.
	 * @return The tangent of fValue.
	 * @see java.lang.Math#tan(double)
	 */
	public static float tan(float fValue) {
		return (float) Math.tan(fValue);
	}

	/**
	 * Returns 1 if the number is positive, -1 if the number is negative, and 0 otherwise
	 *
	 * @param iValue
	 *            The integer to examine.
	 * @return The integer's sign.
	 */
	public static int sign(int iValue) {
		if (iValue > 0) {
			return 1;
		}
		if (iValue < 0) {
			return -1;
		}
		return 0;
	}

	/**
	 * Returns 1 if the number is positive, -1 if the number is negative, and 0 otherwise
	 *
	 * @param fValue
	 *            The float to examine.
	 * @return The float's sign.
	 */
	public static float sign(float fValue) {
		return Math.signum(fValue);
	}

	/**
	 * Given 3 points in a 2d plane, this function computes if the points going from A-B-C are moving counter clock wise.
	 *
	 * @param p0
	 *            Point 0.
	 * @param p1
	 *            Point 1.
	 * @param p2
	 *            Point 2.
	 * @return 1 If they are CCW, -1 if they are not CCW, 0 if p2 is between p0 and p1.
	 */
	public static int counterClockwise(Vector2f p0, Vector2f p1, Vector2f p2) {
		float dx1, dx2, dy1, dy2;
		dx1 = p1.x - p0.x;
		dy1 = p1.y - p0.y;
		dx2 = p2.x - p0.x;
		dy2 = p2.y - p0.y;
		if (dx1 * dy2 > dy1 * dx2) {
			return 1;
		}
		if (dx1 * dy2 < dy1 * dx2) {
			return -1;
		}
		if ((dx1 * dx2 < 0) || (dy1 * dy2 < 0)) {
			return -1;
		}
		if ((dx1 * dx1 + dy1 * dy1) < (dx2 * dx2 + dy2 * dy2)) {
			return 1;
		}
		return 0;
	}

	/**
	 * Test if a point is inside a triangle. 1 if the point is on the ccw side, -1 if the point is on the cw side, and 0 if it is on neither.
	 *
	 * @param t0
	 *            First point of the triangle.
	 * @param t1
	 *            Second point of the triangle.
	 * @param t2
	 *            Third point of the triangle.
	 * @param p
	 *            The point to test.
	 * @return Value 1 or -1 if inside triangle, 0 otherwise.
	 */
	public static int pointInsideTriangle(Vector2f t0, Vector2f t1, Vector2f t2, Vector2f p) {
		int val1 = counterClockwise(t0, t1, p);
		if (val1 == 0) {
			return 1;
		}
		int val2 = counterClockwise(t1, t2, p);
		if (val2 == 0) {
			return 1;
		}
		if (val2 != val1) {
			return 0;
		}
		int val3 = counterClockwise(t2, t0, p);
		if (val3 == 0) {
			return 1;
		}
		if (val3 != val1) {
			return 0;
		}
		return val3;
	}

	/**
	 * Returns the determinant of a 4x4 matrix.
	 */
	public static float determinant(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {

		double det01 = m20 * m31 - m21 * m30;
		double det02 = m20 * m32 - m22 * m30;
		double det03 = m20 * m33 - m23 * m30;
		double det12 = m21 * m32 - m22 * m31;
		double det13 = m21 * m33 - m23 * m31;
		double det23 = m22 * m33 - m23 * m32;
		return (float) (m00 * (m11 * det23 - m12 * det13 + m13 * det12) - m01 * (m10 * det23 - m12 * det03 + m13 * det02) + m02 * (m10 * det13 - m11 * det03 + m13 * det01) - m03 * (m10 * det12 - m11 * det02 + m12 * det01));
	}

	/**
	 * Returns a random float between 0 and 1.
	 *
	 * @return A random float between <tt>0.0f</tt> (inclusive) to <tt>1.0f</tt> (exclusive).
	 */
	public static float nextRandomFloat() {
		return rand.nextFloat();
	}

	/**
	 * Returns a random float between min and max.
	 *
	 * @return A random int between <tt>min</tt> (inclusive) to <tt>max</tt> (inclusive).
	 */
	public static int nextRandomInt(int min, int max) {
		return (int) (nextRandomFloat() * (max - min + 1)) + min;
	}

	public static int nextRandomInt() {
		return rand.nextInt();
	}

	/**
	 * Converts a point from Spherical coordinates to Cartesian (using positive Y as up) and stores the results in the store var.
	 */
	public static Vector3f sphericalToCartesian(Vector3f sphereCoords, Vector3f store) {
		store.y = sphereCoords.x * FastMath.sin(sphereCoords.z);
		float a = sphereCoords.x * FastMath.cos(sphereCoords.z);
		store.x = a * FastMath.cos(sphereCoords.y);
		store.z = a * FastMath.sin(sphereCoords.y);

		return store;
	}

	/**
	 * Converts a point from Cartesian coordinates (using positive Y as up) to Spherical and stores the results in the store var. (Radius, Azimuth, Polar)
	 */
	public static Vector3f cartesianToSpherical(Vector3f cartCoords, Vector3f store) {
		if (cartCoords.x == 0) {
			cartCoords.x = FastMath.FLT_EPSILON;
		}
		store.x = FastMath.sqrt((cartCoords.x * cartCoords.x) + (cartCoords.y * cartCoords.y) + (cartCoords.z * cartCoords.z));
		store.y = FastMath.atan(cartCoords.z / cartCoords.x);
		if (cartCoords.x < 0) {
			store.y += FastMath.PI;
		}
		store.z = FastMath.asin(cartCoords.y / store.x);
		return store;
	}

	/**
	 * Converts a point from Spherical coordinates to Cartesian (using positive Z as up) and stores the results in the store var.
	 */
	public static Vector3f sphericalToCartesianZ(Vector3f sphereCoords, Vector3f store) {
		store.z = sphereCoords.x * FastMath.sin(sphereCoords.z);
		float a = sphereCoords.x * FastMath.cos(sphereCoords.z);
		store.x = a * FastMath.cos(sphereCoords.y);
		store.y = a * FastMath.sin(sphereCoords.y);

		return store;
	}

	/**
	 * Converts a point from Cartesian coordinates (using positive Z as up) to Spherical and stores the results in the store var. (Radius, Azimuth, Polar)
	 */
	public static Vector3f cartesianZToSpherical(Vector3f cartCoords, Vector3f store) {
		if (cartCoords.x == 0) {
			cartCoords.x = FastMath.FLT_EPSILON;
		}
		store.x = FastMath.sqrt((cartCoords.x * cartCoords.x) + (cartCoords.y * cartCoords.y) + (cartCoords.z * cartCoords.z));
		store.z = FastMath.atan(cartCoords.z / cartCoords.x);
		if (cartCoords.x < 0) {
			store.z += FastMath.PI;
		}
		store.y = FastMath.asin(cartCoords.y / store.x);
		return store;
	}

	/**
	 * Takes an value and expresses it in terms of min to max.
	 *
	 * @param val
	 *            - the angle to normalize (in radians)
	 * @return the normalized angle (also in radians)
	 */
	public static float normalize(float val, float min, float max) {
		if (Float.isInfinite(val) || Float.isNaN(val)) {
			return 0f;
		}
		float range = max - min;
		while (val > max) {
			val -= range;
		}
		while (val < min) {
			val += range;
		}
		return val;
	}

	/**
	 * @param x
	 *            the value whose sign is to be adjusted.
	 * @param y
	 *            the value whose sign is to be used.
	 * @return x with its sign changed to match the sign of y.
	 */
	public static float copysign(float x, float y) {
		if (y >= 0 && x <= -0) {
			return -x;
		}
		else if (y < 0 && x >= 0) {
			return -x;
		}
		else {
			return x;
		}
	}

	/**
	 * Take a float input and clamp it between min and max.
	 *
	 * @param input
	 * @param min
	 * @param max
	 * @return clamped input
	 */
	public static float clamp(float input, float min, float max) {
		return (input < min) ? min : (input > max) ? max : input;
	}

	/**
	 * Clamps the given float to be between 0 and 1.
	 *
	 * @param input
	 * @return input clamped between 0 and 1.
	 */
	public static float saturate(float input) {
		return clamp(input, 0f, 1f);
	}

	/**
	 * Converts a single precision (32 bit) floating point value into half precision (16 bit).
	 * <p/>
	 * Source: http://www.fox-toolkit.org/ftp/fasthalffloatconversion.pdf
	 *
	 * @param half
	 *            The half floating point value as a short.
	 * @return floating point value of the half.
	 */
	public static float convertHalfToFloat(int half) {
		switch (half) {
			case 0x0000:
				return 0f;
			case 0x8000:
				return -0f;
			case 0x7c00:
				return Float.POSITIVE_INFINITY;
			case 0xfc00:
				return Float.NEGATIVE_INFINITY;
			// TODO: Support for NaN?
			default:
				return Float.intBitsToFloat(((half & 0x8000) << 16) | (((half & 0x7c00) + 0x1C000) << 13) | ((half & 0x03FF) << 13));
		}
	}

	public static short convertFloatToHalf(float flt) {
		if (Float.isNaN(flt)) {
			throw new UnsupportedOperationException("NaN to half conversion not supported!");
		}
		else if (flt == Float.POSITIVE_INFINITY) {
			return (short) 0x7c00;
		}
		else if (flt == Float.NEGATIVE_INFINITY) {
			return (short) 0xfc00;
		}
		else if (flt == 0f) {
			return (short) 0x0000;
		}
		else if (flt == -0f) {
			return (short) 0x8000;
		}
		else if (flt > 65504f) {
			// max value supported by half float
			return 0x7bff;
		}
		else if (flt < -65504f) {
			return (short) (0x7bff | 0x8000);
		}
		else if (flt > 0f && flt < 5.96046E-8f) {
			return 0x0001;
		}
		else if (flt < 0f && flt > -5.96046E-8f) {
			return (short) 0x8001;
		}

		int f = Float.floatToIntBits(flt);
		return (short) (((f >> 16) & 0x8000) | ((((f & 0x7f800000) - 0x38000000) >> 13) & 0x7c00) | ((f >> 13) & 0x03ff));
	}
}
