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
package com.aionemu.gameserver.world;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
import com.aionemu.gameserver.GameServerError;
import com.aionemu.gameserver.model.GameEngine;
import com.aionemu.gameserver.world.handlers.GeneralWorldHandler;
import com.aionemu.gameserver.world.handlers.WorldHandler;
import com.aionemu.gameserver.world.handlers.WorldID;

/**
 * @author ATracer
 */
public class WorldEngine implements GameEngine {

	private static final Logger log = LoggerFactory.getLogger(WorldEngine.class);
	private static ScriptManager scriptManager = new ScriptManager();
	public static final File INSTANCE_DESCRIPTOR_FILE = new File("./data/scripts/system/worldhandlers.xml");
	public static final WorldHandler DUMMY_INSTANCE_HANDLER = new GeneralWorldHandler();
	private Map<Integer, Class<? extends WorldHandler>> handlers = new HashMap<Integer, Class<? extends WorldHandler>>();

	@Override
	public void load(CountDownLatch progressLatch) {
		log.info("[Map Engine] Map engine load started");
		scriptManager = new ScriptManager();

		AggregatedClassListener acl = new AggregatedClassListener();
		acl.addClassListener(new OnClassLoadUnloadListener());
		acl.addClassListener(new ScheduledTaskClassListener());
		acl.addClassListener(new WorldHandlerClassListener());
		scriptManager.setGlobalClassListener(acl);

		try {
			scriptManager.load(INSTANCE_DESCRIPTOR_FILE);
			log.info("[Map Engine] Loaded " + handlers.size() + " World Script");
		} 
		catch (Exception e) {
			throw new GameServerError("[Map Engine] Can't initialize map handlers.", e);
		} 
		finally {
			if (progressLatch != null) {
				progressLatch.countDown();
			}
		}
	}

	@Override
	public void shutdown() {
		log.info("[Map Engine] Map engine shutdown started");
		scriptManager.shutdown();
		scriptManager = null;
		handlers.clear();
		log.info("[Map Engine] Map engine shutdown complete");
	}

	public WorldHandler getNewInstanceHandler(int worldId) {
		Class<? extends WorldHandler> instanceClass = handlers.get(worldId);
		WorldHandler worldHandler = null;
		if (instanceClass != null) {
			try {
				worldHandler = instanceClass.newInstance();
			} 
			catch (Exception ex) {
				log.warn("[Map Engine] Can't instantiate map handler " + worldId, ex);
			}
		}
		if (worldHandler == null) {
			worldHandler = DUMMY_INSTANCE_HANDLER;
		}
		return worldHandler;
	}

	/**
	 * @param handler
	 */
	final void addWorldHandlerClass(Class<? extends WorldHandler> handler) {
		WorldID idAnnotation = handler.getAnnotation(WorldID.class);
		if (idAnnotation != null) {
			handlers.put(idAnnotation.value(), handler);
		}
	}

	/**
	 * @param instance
	 */
	public void onWorldCreate(WorldMap map) {
		map.getWorldHandler().onWorldCreate(map);
	}

	public static final WorldEngine getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final WorldEngine instance = new WorldEngine();
	}
}
