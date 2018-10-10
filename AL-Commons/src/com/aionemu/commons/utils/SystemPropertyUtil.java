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

package com.aionemu.commons.utils;

import java.util.logging.Level;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of utility methods to retrieve and parse the values of the Java
 * system properties.
 */
public final class SystemPropertyUtil {

	@SuppressWarnings("all")
	private static boolean initializedLogger;
	private static final Logger logger;
	private static boolean loggedException;

	static {
		logger = LoggerFactory.getLogger(SystemPropertyUtil.class);
		initializedLogger = true;
	}

	/**
	 * Returns {@code true} if and only if the system property with the
	 * specified {@code key} exists.
	 */
	public static boolean contains(String key) {
		return get(key) != null;
	}

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to {@code null} if the property access
	 * fails.
	 *
	 * @return the property value or {@code null}
	 */
	public static String get(String key) {
		return get(key, null);
	}

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to the specified default value if the
	 * property access fails.
	 *
	 * @return the property value. {@code def} if there's no such property or if
	 *         an access to the specified property is not allowed.
	 */
	public static String get(String key, String def) {
		if (key == null) {
			throw new NullPointerException("key");
		}
		if (key.isEmpty()) {
			throw new IllegalArgumentException("key must not be empty.");
		}

		String value = null;
		try {
			value = System.getProperty(key);
		} catch (Exception e) {
			if (!loggedException) {
				log("Unable to retrieve a system property '" + key + "'; default values will be used.", e);
				loggedException = true;
			}
		}

		if (value == null) {
			return def;
		}

		return value;
	}

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to the specified default value if the
	 * property access fails.
	 *
	 * @return the property value. {@code def} if there's no such property or if
	 *         an access to the specified property is not allowed.
	 */
	public static boolean getBoolean(String key, boolean def) {
		String value = get(key);
		if (value == null) {
			return def;
		}

		value = value.trim().toLowerCase();
		if (value.isEmpty()) {
			return true;
		}

		if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
			return true;
		}

		if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
			return false;
		}

		log("Unable to parse the boolean system property '" + key + "':" + value + " - " + "using the default value: "
				+ def);

		return def;
	}

	private static final Pattern INTEGER_PATTERN = Pattern.compile("-?[0-9]+");

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to the specified default value if the
	 * property access fails.
	 *
	 * @return the property value. {@code def} if there's no such property or if
	 *         an access to the specified property is not allowed.
	 */
	public static int getInt(String key, int def) {
		String value = get(key);
		if (value == null) {
			return def;
		}

		value = value.trim().toLowerCase();
		if (INTEGER_PATTERN.matcher(value).matches()) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				// Ignore
			}
		}

		log("Unable to parse the integer system property '" + key + "':" + value + " - " + "using the default value: "
				+ def);

		return def;
	}

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to the specified default value if the
	 * property access fails.
	 *
	 * @return the property value. {@code def} if there's no such property or if
	 *         an access to the specified property is not allowed.
	 */
	public static long getLong(String key, long def) {
		String value = get(key);
		if (value == null) {
			return def;
		}

		value = value.trim().toLowerCase();
		if (INTEGER_PATTERN.matcher(value).matches()) {
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				// Ignore
			}
		}

		log("Unable to parse the long integer system property '" + key + "':" + value + " - "
				+ "using the default value: " + def);

		return def;
	}

	private static void log(String msg) {
		if (initializedLogger) {
			logger.warn(msg);
		} else {
			// Use JDK logging if logger was not initialized yet.
			java.util.logging.Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg);
		}
	}

	private static void log(String msg, Exception e) {
		if (initializedLogger) {
			logger.warn(msg, e);
		} else {
			// Use JDK logging if logger was not initialized yet.
			java.util.logging.Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg, e);
		}
	}

	private SystemPropertyUtil() {
		// Unused
	}
}
