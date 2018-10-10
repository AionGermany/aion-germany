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

package com.aionemu.commons.services;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.scriptmanager.ScriptManager;

import javolution.util.FastMap;

/**
 * Script Service class that is designed to manage all loaded contexts
 * 
 * @author SoulKeeper
 */
public class ScriptService {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ScriptService.class);

	/**
	 * Container for ScriptManagers, sorted by file
	 */
	private final Map<File, ScriptManager> map = new FastMap<File, ScriptManager>().shared();

	/**
	 * Loads script descriptor from given directory or file
	 * 
	 * @param file
	 *            directory or file
	 * @throws RuntimeException
	 *             if failed to load script descriptor
	 */
	public void load(String file) throws RuntimeException {
		load(new File(file));
	}

	/**
	 * Loads script descriptor from given file or directory
	 * 
	 * @param file
	 *            file that has to be loaded
	 * @throws RuntimeException
	 *             if something went wrong
	 */
	public void load(File file) throws RuntimeException {
		if (file.isFile()) {
			loadFile(file);
		} else if (file.isDirectory()) {
			loadDir(file);
		}
	}

	/**
	 * Load script descriptor from given file
	 * 
	 * @param file
	 *            script descriptor
	 */
	private void loadFile(File file) {
		if (map.containsKey(file)) {
			throw new IllegalArgumentException("ScriptManager by file:" + file + " already loaded");
		}

		ScriptManager sm = new ScriptManager();
		try {
			sm.load(file);
		} catch (Exception e) {
			log.error("loadFile", e);
			throw new RuntimeException(e);
		}

		map.put(file, sm);
	}

	/**
	 * Loads all files from given directory
	 * 
	 * @param dir
	 *            directory to scan for files
	 */
	private void loadDir(File dir) {
		for (Object file : FileUtils.listFiles(dir, new String[] { "xml" }, false)) {
			loadFile((File) file);
		}
	}

	/**
	 * Unloads given script descriptor
	 * 
	 * @param file
	 *            script descriptor
	 * @throws IllegalArgumentException
	 *             if descriptor is not loaded
	 */
	public void unload(File file) throws IllegalArgumentException {
		ScriptManager sm = map.remove(file);
		if (sm == null) {
			throw new IllegalArgumentException("ScriptManager by file " + file + " is not loaded.");
		}

		sm.shutdown();
	}

	/**
	 * Reloads given script descriptor
	 * 
	 * @param file
	 *            Script Descriptro
	 * @throws IllegalArgumentException
	 *             if descriptor is not loaded
	 */
	public void reload(File file) throws IllegalArgumentException {
		ScriptManager sm = map.get(file);
		if (sm == null) {
			throw new IllegalArgumentException("ScriptManager by file " + file + " is not loaded.");
		}

		sm.reload();
	}

	/**
	 * Adds script manager to script service.<br>
	 * Should be used if scriptManager uses custom loading logic for scripts.
	 * 
	 * @param scriptManager
	 *            Script manager object
	 * @param file
	 *            script descriptor file
	 */
	public void addScriptManager(ScriptManager scriptManager, File file) {
		if (map.containsKey(file)) {
			throw new IllegalArgumentException("ScriptManager by file " + file + " is already loaded.");
		}

		map.put(file, scriptManager);
	}

	/**
	 * Returns unmodifiable map with loaded script managers
	 * 
	 * @return unmodifiable map
	 */
	public Map<File, ScriptManager> getLoadedScriptManagers() {
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Broadcast shutdown to all attached Script Managers.
	 */
	public void shutdown() {
		for (Iterator<Entry<File, ScriptManager>> it = this.map.entrySet().iterator(); it.hasNext();) {
			try {
				it.next().getValue().shutdown();
			} catch (Exception e) {
				log.warn("An exception occured during shudown procedure.", e);
			}

			it.remove();
		}
	}
}
