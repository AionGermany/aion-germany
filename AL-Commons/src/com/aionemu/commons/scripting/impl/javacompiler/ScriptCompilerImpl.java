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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.CompilationResult;
import com.aionemu.commons.scripting.ScriptClassLoader;
import com.aionemu.commons.scripting.ScriptCompiler;
import com.sun.tools.javac.api.JavacTool;

/**
 * Wrapper for JavaCompiler api
 * 
 * @author SoulKeeper
 */
public class ScriptCompilerImpl implements ScriptCompiler {

	private static final Logger log = LoggerFactory.getLogger(ScriptCompilerImpl.class);

	/**
	 * Instance of JavaCompiler that will be used to compile classes
	 */
	protected final JavaCompiler javaCompiler;

	/**
	 * List of jar files
	 */
	protected Iterable<File> libraries;

	/**
	 * Parent classloader that has to be used for this compiler
	 */
	protected ScriptClassLoader parentClassLoader;

	/**
	 * Creates new instance of JavaCompilerImpl. If system compiler is not
	 * available - throws RuntimeExcetion
	 * 
	 * @throws RuntimeException
	 *             if compiler is not available
	 */
	public ScriptCompilerImpl() {
		this.javaCompiler = JavacTool.create();

		if (javaCompiler == null) {
			if (ToolProvider.getSystemJavaCompiler() != null) {
				throw new RuntimeException(new InstantiationException("JavaCompiler is not aviable."));
			}
		}
	}

	/**
	 * Sets parent classLoader for this JavaCompilerImpl
	 * 
	 * @param classLoader
	 *            parent classloader
	 */
	@Override
	public void setParentClassLoader(ScriptClassLoader classLoader) {
		this.parentClassLoader = classLoader;
	}

	/**
	 * Sets jar files that should be used for this compiler as libraries
	 * 
	 * @param files
	 *            list of jar files
	 */
	@Override
	public void setLibraires(Iterable<File> files) {
		libraries = files;
	}

	/**
	 * Compiles given class.
	 * 
	 * @param className
	 *            Name of the class
	 * @param sourceCode
	 *            source code
	 * @return CompilationResult with the class
	 * @throws RuntimeException
	 *             if compilation failed with errros
	 */
	@Override
	public CompilationResult compile(String className, String sourceCode) {
		return compile(new String[] { className }, new String[] { sourceCode });
	}

	/**
	 * Compiles list of classes. Amount of classNames must be equal to amount of
	 * sourceCodes
	 * 
	 * @param classNames
	 *            classNames
	 * @param sourceCode
	 *            list of source codes
	 * @return CompilationResult with needed files
	 * @throws IllegalArgumentException
	 *             if size of classNames not equals to size of sourceCodes
	 * @throws RuntimeException
	 *             if compilation failed with errros
	 */
	@Override
	public CompilationResult compile(String[] classNames, String[] sourceCode) throws IllegalArgumentException {

		if (classNames.length != sourceCode.length) {
			throw new IllegalArgumentException("Amount of classes is not equal to amount of sources");
		}

		List<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();

		for (int i = 0; i < classNames.length; i++) {
			JavaFileObject compilationUnit = new JavaSourceFromString(classNames[i], sourceCode[i]);
			compilationUnits.add(compilationUnit);
		}

		return doCompilation(compilationUnits);
	}

	/**
	 * Compiles given files. Files must be java sources.
	 * 
	 * @param compilationUnits
	 *            files to compile
	 * @return CompilationResult with classes
	 * @throws RuntimeException
	 *             if compilation failed with errros
	 */
	@Override
	public CompilationResult compile(Iterable<File> compilationUnits) {
		List<JavaFileObject> list = new ArrayList<JavaFileObject>();

		for (File f : compilationUnits) {
			list.add(new JavaSourceFromFile(f, JavaFileObject.Kind.SOURCE));
		}

		return doCompilation(list);
	}

	/**
	 * Actually performs compilation. Compiler expects sources in UTF-8
	 * encoding. Also compiler generates full debugging info for classes.
	 * 
	 * @param compilationUnits
	 *            Units that will be compiled
	 * @return CompilationResult with compiledClasses
	 * @throws RuntimeException
	 *             if compilation failed with errros
	 */
	protected CompilationResult doCompilation(Iterable<JavaFileObject> compilationUnits) {
		List<String> options = Arrays.asList("-encoding", "UTF-8", "-g");
		DiagnosticListener<JavaFileObject> listener = new ErrorListener();
		ClassFileManager manager = new ClassFileManager(JavacTool.create(), listener);
		manager.setParentClassLoader(parentClassLoader);

		if (libraries != null) {
			try {
				manager.addLibraries(libraries);
			} catch (IOException e) {
				log.error("Can't set libraries for compiler.", e);
			}
		}

		JavaCompiler.CompilationTask task = javaCompiler.getTask(null, manager, listener, options, null,
				compilationUnits);

		if (!task.call()) {
			throw new RuntimeException("Error while compiling classes");
		}

		ScriptClassLoader cl = manager.getClassLoader(null);
		Class<?>[] compiledClasses = classNamesToClasses(manager.getCompiledClasses().keySet(), cl);
		return new CompilationResult(compiledClasses, cl);
	}

	/**
	 * Reolves list of classes by their names
	 * 
	 * @param classNames
	 *            names of the classes
	 * @param cl
	 *            classLoader to use to resove classes
	 * @return resolved classes
	 * @throws RuntimeException
	 *             if can't find class
	 */
	protected Class<?>[] classNamesToClasses(Collection<String> classNames, ScriptClassLoader cl) {
		Class<?>[] classes = new Class<?>[classNames.size()];

		int i = 0;
		for (String className : classNames) {
			try {
				Class<?> clazz = cl.loadClass(className);
				classes[i] = clazz;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			i++;
		}

		return classes;
	}

	/**
	 * Only java files are supported by java compiler
	 * 
	 * @return "java";
	 */
	@Override
	public String[] getSupportedFileTypes() {
		return new String[] { "java" };
	}
}
