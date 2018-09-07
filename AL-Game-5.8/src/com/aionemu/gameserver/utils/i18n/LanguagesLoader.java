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
package com.aionemu.gameserver.utils.i18n;

import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.utils.ClassUtils;
import com.aionemu.gameserver.GameServer;

/**
 * @author blakawk
 */
public class LanguagesLoader implements ClassListener {

	private static final Logger log = LoggerFactory.getLogger(Language.class);
	private final LanguageHandler handler;

	public LanguagesLoader(LanguageHandler handler) {
		this.handler = handler;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void postLoad(Class<?>[] classes) {
		for (Class<?> clazz : classes) {
			if (log.isDebugEnabled()) {
				log.debug("Loading class " + clazz.getName());
			}

			if (!isValidClass(clazz)) {
				continue;
			}

			if (ClassUtils.isSubclass(clazz, Language.class)) {
				Class<? extends Language> language = (Class<? extends Language>) clazz;
				if (language != null) {
					try {
						handler.registerLanguage(language.newInstance());
					}
					catch (Exception e) {
						log.error("Registering " + language.getName(), e);
					}
				}
			}
		}

		GameServer.log.info("[LanguagesLoader] Loaded " + handler.size() + " custom message handlers.");
	}

	@Override
	public void preUnload(Class<?>[] classes) {
		if (log.isDebugEnabled()) {
			for (Class<?> clazz : classes) {
				log.debug("Unload language " + clazz.getName());
			}
		}
		handler.clear();
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
