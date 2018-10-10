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

package com.aionemu.commons.scripting.impl.javacompiler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.tools.JavaFileObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.ScriptClassLoader;
import com.aionemu.commons.utils.ClassUtils;

/**
 * This classloader is used to load script classes. <br>
 * <br>
 * Due to JavaCompiler limitations we have to keep list of available classes
 * here.
 * 
 * @author SoulKeeper
 */
public class ScriptClassLoaderImpl extends ScriptClassLoader {

	private static final Logger log = LoggerFactory.getLogger(ScriptClassLoaderImpl.class);

	/**
	 * ClassFileManager that is related to this ClassLoader
	 */
	private final ClassFileManager classFileManager;

	/**
	 * Creates new ScriptClassLoader with given ClassFileManger. <br>
	 * Parent ClassLoader is ClassLoader of current class:
	 * 
	 * <pre>
	 * ScriptClassLoaderImpl.class.getClassLoader()
	 * </pre>
	 *
	 * @param classFileManager
	 *            classFileManager of this classLoader
	 */
	ScriptClassLoaderImpl(ClassFileManager classFileManager) {
		super(new URL[] {}, ScriptClassLoaderImpl.class.getClassLoader());
		this.classFileManager = classFileManager;
	}

	/**
	 * Creates new ScriptClassLoader with given ClassFileManger and another
	 * classLoader as parent
	 * 
	 * @param classFileManager
	 *            classFileManager of this classLoader
	 * @param parent
	 *            parent classLoader
	 */
	ScriptClassLoaderImpl(ClassFileManager classFileManager, ClassLoader parent) {
		super(new URL[] {}, parent);
		this.classFileManager = classFileManager;
	}

	/**
	 * Returns ClassFileManager that is related to this ClassLoader
	 * 
	 * @return classFileManager of this classLoader
	 */
	public ClassFileManager getClassFileManager() {
		return classFileManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getCompiledClasses() {
		Set<String> compiledClasses = classFileManager.getCompiledClasses().keySet();
		return Collections.unmodifiableSet(compiledClasses);
	}

	/**
	 * Returns list of classes that are members of a package
	 * 
	 * @param packageName
	 *            package to search for classes
	 * @return list of classes that are package members
	 * @throws IOException
	 *             if was unable to load class
	 */
	public Set<JavaFileObject> getClassesForPackage(String packageName) throws IOException {
		Set<JavaFileObject> result = new HashSet<JavaFileObject>();

		// load parent
		ClassLoader parent = getParent();
		if (parent instanceof ScriptClassLoaderImpl) {
			ScriptClassLoaderImpl pscl = (ScriptClassLoaderImpl) parent;
			result.addAll(pscl.getClassesForPackage(packageName));
			pscl.close();
		}

		// load current classloader compiled classes
		for (String cn : classFileManager.getCompiledClasses().keySet()) {
			if (ClassUtils.isPackageMember(cn, packageName)) {
				BinaryClass bc = classFileManager.getCompiledClasses().get(cn);
				result.add(bc);
			}
		}

		// initialize set with class names, will be used to resolve classes
		Set<String> classNames = new HashSet<String>();

		// load package members from this classloader
		Enumeration<URL> urls = getResources(packageName.replace('.', '/'));
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			String path = URLDecoder.decode(url.getPath(), "UTF-8");
			if (new File(path).isDirectory()) {
				Set<String> packageClasses = ClassUtils.getClassNamesFromPackage(new File(path), packageName, false);
				classNames.addAll(packageClasses);
			} else if (path.toLowerCase().contains(".jar!")) {
				File file = new File(path);
				while (!file.getName().toLowerCase().endsWith(".jar!")) {
					file = file.getParentFile();
				}
				path = file.getPath().substring(0, file.getPath().length() - 1);
				path = path.replace('\\', '/');
				path = path.substring(path.indexOf(":") + 1);
				file = new File(path);
				// add jar file as library. Actually it's doesn't matter if we
				// have it as library
				// or as file in class path
				addJarFile(file);
			}
		}

		// add library class names from this classloader to available classes
		classNames.addAll(getLibraryClassNames());

		// load classes for class names from this classloader
		for (String cn : classNames) {
			if (ClassUtils.isPackageMember(cn, packageName)) {
				BinaryClass bc = new BinaryClass(cn);
				try {
					byte[] data = getRawClassByName(cn);
					OutputStream os = bc.openOutputStream();
					os.write(data);
				} catch (IOException e) {
					log.error("Error while loading class from package " + packageName, e);
					throw e;
				}
				result.add(bc);
			}
		}

		return result;
	}

	/**
	 * Finds class with the specified name from the URL search path. Any URLs
	 * referring to JAR files are loaded and opened as needed until the class is
	 * found.
	 * 
	 * @param name
	 *            the name of the class
	 * @return the resulting class data
	 * @throws IOException
	 *             if failed to load class
	 * @throws IllegalArgumentException
	 *             if failed to open input stream for class
	 */
	protected byte[] getRawClassByName(String name) throws IOException {
		String resourceName = name.replace('.', '/').concat(".class");
		URL resource = getResource(resourceName);
		InputStream is = null;
		byte[] clazz = null;

		try {
			is = resource.openStream();
			clazz = IOUtils.toByteArray(is);
		} catch (IOException e) {
			log.error("Error while loading class data: " + name, e);
			throw e;
		} catch (NullPointerException e) {
			log.error("Can't open input stream for resource: " + name);
			throw new IllegalArgumentException("Failed to open input stream for resource: " + name);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("Error while closing stream", e);
				}
			}
		}
		return clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getByteCode(String className) {
		BinaryClass bc = getClassFileManager().getCompiledClasses().get(className);
		byte[] b = new byte[bc.getBytes().length];
		System.arraycopy(bc.getBytes(), 0, b, 0, b.length);
		return b;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getDefinedClass(String name) {
		BinaryClass bc = classFileManager.getCompiledClasses().get(name);
		if (bc == null) {
			return null;
		}

		return bc.getDefinedClass();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefinedClass(String name, Class<?> clazz) {
		BinaryClass bc = classFileManager.getCompiledClasses().get(name);

		if (bc == null) {
			throw new IllegalArgumentException("Attempt to set defined class for class that was not compiled?");
		}

		bc.setDefinedClass(clazz);
	}
}
