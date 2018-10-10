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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import com.aionemu.commons.scripting.ScriptClassLoader;

/**
 * This class extends manages loaded classes. It is also responsible for
 * tricking compiler. Unfortunally compiler doen't work with classloaders, so we
 * have to pass class data manually for each compilation.
 *
 * @author SoulKeeper
 */
public class ClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	/**
	 * This map contains classes compiled for this classloader
	 */
	private final Map<String, BinaryClass> compiledClasses = new HashMap<String, BinaryClass>();

	/**
	 * Classloader that will be used to load compiled classes
	 */
	protected ScriptClassLoaderImpl loader;

	/**
	 * Parent classloader for loader
	 */
	protected ScriptClassLoader parentClassLoader;

	/**
	 * Creates new ClassFileManager.
	 *
	 * @param compiler
	 *            that will be used
	 * @param listener
	 *            class that will report compilation errors
	 */
	public ClassFileManager(JavaCompiler compiler, DiagnosticListener<? super JavaFileObject> listener) {
		super(compiler.getStandardFileManager(listener, null, null));
	}

	/**
	 * Returns JavaFileObject that will be used to write class data into it by
	 * compier
	 *
	 * @param location
	 *            not used
	 * @param className
	 *            JavaFileObject will have this className
	 * @param kind
	 *            not used
	 * @param sibling
	 *            not used
	 * @return JavaFileObject that will be uesd to store compiled class data
	 * @throws IOException
	 *             never thrown
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
			throws IOException {
		BinaryClass co = new BinaryClass(className);
		compiledClasses.put(className, co);
		return co;
	}

	/**
	 * Returns classloaded of this ClassFileManager. If not exists, creates new
	 *
	 * @param location
	 *            not used
	 * @return classLoader of this ClassFileManager
	 */
	@Override
	public synchronized ScriptClassLoaderImpl getClassLoader(Location location) {
		if (loader == null) {
			if (parentClassLoader != null) {
				loader = new ScriptClassLoaderImpl(this, parentClassLoader);
			} else {
				loader = new ScriptClassLoaderImpl(this);
			}
		}
		return loader;
	}

	/**
	 * Sets paraentClassLoader for this classLoader
	 *
	 * @param classLoader
	 *            parent class loader
	 */
	public void setParentClassLoader(ScriptClassLoader classLoader) {
		this.parentClassLoader = classLoader;
	}

	/**
	 * Adds library file. Library file must be a .jar archieve
	 *
	 * @param file
	 *            link to jar archieve
	 * @throws IOException
	 *             if something goes wrong
	 */
	public void addLibrary(File file) throws IOException {
		ScriptClassLoaderImpl classLoader = getClassLoader(null);
		classLoader.addJarFile(file);
	}

	/**
	 * Adds list of files as libraries. Files must be jar archieves
	 *
	 * @param files
	 *            list of jar archives
	 * @throws IOException
	 *             if something goes wrong
	 */
	public void addLibraries(Iterable<File> files) throws IOException {
		for (File f : files) {
			addLibrary(f);
		}
	}

	/**
	 * Returns list of classes that were compiled by compiler related to this
	 * ClassFileManager
	 *
	 * @return list of classes
	 */
	public Map<String, BinaryClass> getCompiledClasses() {
		return compiledClasses;
	}

	/**
	 * This method overrides class resolving procedure for compiler. It uses
	 * classloaders to resolve classes that compiler may need during
	 * compilation. Compiler by itself can't detect them. So we have to use this
	 * hack here. Hack is used only if compiler requests for classes in
	 * classpath.
	 *
	 * @param location
	 *            Location to search classes
	 * @param packageName
	 *            package to scan for classes
	 * @param kinds
	 *            FileTypes to search
	 * @param recurse
	 *            not used
	 * @return list of requered files
	 * @throws IOException
	 *             if something foes wrong
	 */
	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
			throws IOException {
		Iterable<JavaFileObject> objects = super.list(location, packageName, kinds, recurse);

		if (StandardLocation.CLASS_PATH.equals(location) && kinds.contains(Kind.CLASS)) {
			List<JavaFileObject> temp = new ArrayList<JavaFileObject>();
			for (JavaFileObject object : objects) {
				temp.add(object);
			}

			temp.addAll(loader.getClassesForPackage(packageName));
			objects = temp;
		}

		return objects;
	}

	@Override
	public String inferBinaryName(Location location, JavaFileObject file) {
		if (file instanceof BinaryClass) {
			return ((BinaryClass) file).inferBinaryName(null);
		}

		return super.inferBinaryName(location, file);
	}
}
