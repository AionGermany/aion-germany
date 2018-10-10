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

package com.aionemu.commons.scripting.classlistener;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.metadata.OnClassLoad;
import com.aionemu.commons.scripting.metadata.OnClassUnload;

/**
 * @author SoulKeeper
 */
public class OnClassLoadUnloadListener implements ClassListener {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(OnClassLoadUnloadListener.class);

	@Override
	public void postLoad(Class<?>[] classes) {
		for (Class<?> c : classes) {
			doMethodInvoke(c.getDeclaredMethods(), OnClassLoad.class);
		}
	}

	@Override
	public void preUnload(Class<?>[] classes) {
		for (Class<?> c : classes) {
			doMethodInvoke(c.getDeclaredMethods(), OnClassUnload.class);
		}
	}

	/**
	 * Actually invokes method where given annotation class is present. Only
	 * static methods can be invoked
	 * 
	 * @param methods
	 *            Methods to scan for annotations
	 * @param annotationClass
	 *            class of annotation to search for
	 */
	protected final void doMethodInvoke(Method[] methods, Class<? extends Annotation> annotationClass) {
		for (Method m : methods) {
			if (!Modifier.isStatic(m.getModifiers()))
				continue;

			boolean accessible = m.isAccessible();
			m.setAccessible(true);

			if (m.getAnnotation(annotationClass) != null) {
				try {
					m.invoke(null);
				} catch (IllegalAccessException e) {
					log.error("Can't access method " + m.getName() + " of class " + m.getDeclaringClass().getName(), e);
				} catch (InvocationTargetException e) {
					log.error("Can't invoke method " + m.getName() + " of class " + m.getDeclaringClass().getName(), e);
				}
			}

			m.setAccessible(accessible);
		}
	}
}
