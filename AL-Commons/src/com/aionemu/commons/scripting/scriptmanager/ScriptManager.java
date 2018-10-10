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

package com.aionemu.commons.scripting.scriptmanager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.ScriptCompiler;
import com.aionemu.commons.scripting.ScriptContext;
import com.aionemu.commons.scripting.ScriptContextFactory;
import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.scripting.impl.javacompiler.ScriptCompilerImpl;
import com.google.common.collect.Lists;

/**
 * Class that represents managers of script contexts. It loads, reloads and
 * unload script contexts. In the future it may be extended to support
 * programatic manipulation of contexts, but for now it's not needed. <br />
 * Example:
 * 
 * <pre>
 *      ScriptManager sm = new ScriptManager();
 *      sm.load(new File(&quot;st/contexts.xml&quot;));
 *      ...
 *      sm.shutdown();
 * </pre>
 * 
 * <br>
 * 
 * @author SoulKeeper, Aquanox
 */
public class ScriptManager {

	/**
	 * Logger for script context
	 */
	private static final Logger log = LoggerFactory.getLogger(ScriptManager.class);

	public static final Class<? extends ScriptCompiler> DEFAULT_COMPILER_CLASS = ScriptCompilerImpl.class;

	/**
	 * Collection of script contexts
	 */
	private Set<ScriptContext> contexts = new HashSet<ScriptContext>();

	/**
	 * Global ClassListener instance. Automatically assigned for each new
	 * context. Fires after each successful compilation.
	 */
	private ClassListener globalClassListener;

	/**
	 * Loads script contexes from descriptor
	 * 
	 * @param scriptDescriptor
	 *            xml file that describes contexes
	 * @throws Exception
	 *             if can't load file
	 */
	public synchronized void load(File scriptDescriptor) throws Exception {
		FileInputStream fin = new FileInputStream(scriptDescriptor);
		JAXBContext c = JAXBContext.newInstance(ScriptInfo.class, ScriptList.class);
		Unmarshaller u = c.createUnmarshaller();

		ScriptList list = null;
		try {
			list = (ScriptList) u.unmarshal(fin);
		} catch (Exception e) {
			throw e;
		} finally {
			fin.close();
		}

		for (ScriptInfo si : list.getScriptInfos()) {
			ScriptContext context = createContext(si, null);
			if (context != null) {
				contexts.add(context);
				context.init();
			}
		}
	}

	/**
	 * Convenient method that is used to load all script files and libraries
	 * from specific directory.<br>
	 * Descriptor is not required.<br>
	 * <br>
	 * <b>If you wish complex context hierarchy - you will have to use context
	 * descriptors</b> <br>
	 * <br>
	 * .java files are treated as sources.<br>
	 * .jar files are treated as libraries.<br>
	 * Both .java and .jar files will be loaded recursively
	 *
	 * @see #DEFAULT_COMPILER_CLASS
	 * @param directory
	 *            - directory with .java and .jar files
	 * @throws RuntimeException
	 *             if failed to load script context
	 */
	public synchronized void loadDirectory(File directory) throws RuntimeException {
		Collection<File> libraries = FileUtils.listFiles(directory, new String[] { "jar" }, true);
		List<File> list = Lists.newArrayList(libraries);
		try {
			loadDirectory(directory, list, DEFAULT_COMPILER_CLASS.getName());
		} catch (Exception e) {
			throw new RuntimeException("Failed to load script context from directory " + directory.getAbsolutePath(),
					e);
		}
	}

	/**
	 * Load scripts directly from<br>
	 * <br>
	 * <b>If you wish complex context hierarchy - you will have to use context
	 * descriptors</b> <br>
	 * <br>
	 * 
	 * @param directory
	 *            - directory with source files
	 * @param libraries
	 *            - collection with libraries to load
	 * @param compilerClassName
	 *            -
	 * @throws Exception
	 *             if failed to load script context
	 */
	public synchronized void loadDirectory(File directory, List<File> libraries, String compilerClassName)
			throws Exception {

		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("File should be directory");
		}

		ScriptInfo si = new ScriptInfo();
		si.setRoot(directory);
		si.setCompilerClass(compilerClassName);
		si.setScriptInfos(Collections.<ScriptInfo>emptyList());
		si.setLibraries(libraries);

		ScriptContext sc = createContext(si, null);
		contexts.add(sc);
		sc.init();
	}

	/**
	 * Creates new context and checks to not produce copies
	 * 
	 * @param si
	 *            script context descriptor
	 * @param parent
	 *            parent script context
	 * @return created script context
	 * @throws Exception
	 *             if can't create context
	 */
	protected ScriptContext createContext(ScriptInfo si, ScriptContext parent) throws Exception {
		ScriptContext context = ScriptContextFactory.getScriptContext(si.getRoot(), parent);
		context.setLibraries(si.getLibraries());
		context.setCompilerClassName(si.getCompilerClass());

		if (parent == null && contexts.contains(context)) {
			log.warn("Double root script context definition: " + si.getRoot().getAbsolutePath());
			return null;
		}

		if (si.getScriptInfos() != null && !si.getScriptInfos().isEmpty()) {
			for (ScriptInfo child : si.getScriptInfos()) {
				createContext(child, context);
			}
		}

		if (parent == null && globalClassListener != null)
			context.setClassListener(globalClassListener);

		return context;
	}

	/**
	 * Initializes shutdown on all contexts
	 */
	public synchronized void shutdown() {
		for (ScriptContext context : contexts) {
			context.shutdown();
		}

		contexts.clear();
	}

	/**
	 * Reloads all contexts
	 */
	public synchronized void reload() {
		for (ScriptContext context : contexts) {
			reloadContext(context);
		}
	}

	/**
	 * Reloads specified context.
	 * 
	 * @param ctx
	 *            Script context instance.
	 */
	public void reloadContext(ScriptContext ctx) {
		ctx.reload();
	}

	/**
	 * Returns unmodifiable set with script contexts
	 * 
	 * @return unmodifiable set of script contexts
	 */
	public synchronized Collection<ScriptContext> getScriptContexts() {
		return Collections.unmodifiableSet(contexts);
	}

	/**
	 * Set Global class listener instance.
	 * 
	 * @param instance
	 *            listener instance.
	 */
	public void setGlobalClassListener(ClassListener instance) {
		this.globalClassListener = instance;
	}
}
