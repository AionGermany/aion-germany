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

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains utilities that are used when we are working with classes
 *
 * @author SoulKeeper
 */
public class ClassUtils {

	private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

	/**
	 * Return true if class a is either equivalent to class b, or if class a is
	 * a subclass of class b, i.e. if a either "extends" or "implements" b. Note
	 * tht either or both "Class" objects may represent interfaces.
	 *
	 * @param a
	 *            class
	 * @param b
	 *            class
	 * @return true if a == b or a extends b or a implements b
	 */
	public static boolean isSubclass(Class<?> a, Class<?> b) {
		// We rely on the fact that for any given java class or
		// primtitive type there is a unqiue Class object, so
		// we can use object equivalence in the comparisons.
		if (a == b) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}
		for (Class<?> x = a; x != null; x = x.getSuperclass()) {
			if (x == b) {
				return true;
			}
			if (b.isInterface()) {
				Class<?>[] interfaces = x.getInterfaces();
				for (Class<?> anInterface : interfaces) {
					if (isSubclass(anInterface, b)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if class in member of the package
	 *
	 * @param clazz
	 *            class to check
	 * @param packageName
	 *            package
	 * @return true if is member
	 */
	public static boolean isPackageMember(Class<?> clazz, String packageName) {
		return isPackageMember(clazz.getName(), packageName);
	}

	/**
	 * Checks if classNames belongs to package
	 *
	 * @param className
	 *            class name
	 * @param packageName
	 *            package
	 * @return true if belongs
	 */
	public static boolean isPackageMember(String className, String packageName) {
		if (!className.contains(".")) {
			return packageName == null || packageName.isEmpty();
		}
		String classPackage = className.substring(0, className.lastIndexOf('.'));
		return packageName.equals(classPackage);
	}

	/**
	 * Returns class names from directory.
	 *
	 * @param directory
	 *            folder with class files
	 * @return Set of fully qualified class names
	 * @throws IllegalArgumentException
	 *             if specified file is not directory or does not exists
	 * @throws NullPointerException
	 *             if directory is null
	 */
	public static Set<String> getClassNamesFromDirectory(File directory) throws IllegalArgumentException {

		if (!directory.isDirectory() || !directory.exists()) {
			throw new IllegalArgumentException("Directory " + directory + " doesn't exists or is not directory");
		}

		return getClassNamesFromPackage(directory, null, true);
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 *
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @param recursive
	 *            include subpackages or not
	 * @return The classes
	 */
	public static Set<String> getClassNamesFromPackage(File directory, String packageName, boolean recursive) {
		Set<String> classes = new HashSet<String>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {

				if (!recursive) {
					continue;
				}

				String newPackage = file.getName();
				if (!GenericValidator.isBlankOrNull(packageName)) {
					newPackage = packageName + "." + newPackage;
				}
				classes.addAll(getClassNamesFromPackage(file, newPackage, recursive));
			} else if (file.getName().endsWith(".class")) {
				String className = file.getName().substring(0, file.getName().length() - 6);
				if (!GenericValidator.isBlankOrNull(packageName)) {
					className = packageName + "." + className;
				}
				classes.add(className);
			}
		}
		return classes;
	}

	/**
	 * Method that returns all class file names from given jar file
	 *
	 * @param file
	 *            jar file
	 * @return class names from jar file
	 * @throws IOException
	 *             if something went wrong
	 * @throws IllegalArgumentException
	 *             if file doesn't exists or is not jar file
	 * @throws NullPointerException
	 *             if file is null
	 */
	public static Set<String> getClassNamesFromJarFile(File file) throws IOException {

		if (!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("File " + file + " is not valid jar file");
		}

		Set<String> result = new HashSet<String>();

		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);

			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();

				String name = entry.getName();
				if (name.endsWith(".class")) {
					name = name.substring(0, name.length() - 6);
					name = name.replace('/', '.');
					result.add(name);
				}
			}
		} finally {
			if (jarFile != null) {
				try {
					jarFile.close();
				} catch (IOException e) {
					log.error("Failed to close jar file " + jarFile.getName(), e);
				}
			}
		}

		return result;
	}
}
