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
package com.aionemu.gameserver.questEngine.handlers;

import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.utils.ClassUtils;
import com.aionemu.gameserver.questEngine.QuestEngine;

/**
 * @author MrPoke
 */
public class QuestHandlerLoader implements ClassListener {

	private static final Logger logger = LoggerFactory.getLogger(QuestHandlerLoader.class);

	public QuestHandlerLoader() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postLoad(Class<?>[] classes) {
		for (Class<?> c : classes) {
			if (logger.isDebugEnabled()) {
				logger.debug("Load class " + c.getName());
			}

			if (!isValidClass(c)) {
				continue;
			}

			if (ClassUtils.isSubclass(c, QuestHandler.class)) {
				try {
					Class<? extends QuestHandler> tmp = (Class<? extends QuestHandler>) c;
					if (tmp != null) {
						QuestEngine.getInstance().addQuestHandler(tmp.newInstance());
					}
				}
				catch (Exception e) {
					throw new RuntimeException("Failed to load quest handler class: " + c.getName(), e);
				}
			}
		}
	}

	@Override
	public void preUnload(Class<?>[] classes) {
		if (logger.isDebugEnabled()) {
			for (Class<?> c : classes) // debug messages
			{
				logger.debug("Unload class " + c.getName());
			}
		}

		QuestEngine.getInstance().clear();
	}

	public boolean isValidClass(Class<?> clazz) {
		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
			return false;
		}

		if (!Modifier.isPublic(modifiers)) {
			return false;
		}

		return true;
	}
}
