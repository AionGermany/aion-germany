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

package com.aionemu.commons.callbacks.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.commons.utils.GenericValidator;
import com.google.common.collect.Maps;

/**
 * Class that implements helper methods for callbacks.<br>
 * All enhanced objects are delegating main part of their logic to this class
 *
 * @author SoulKeeper
 */
@SuppressWarnings("rawtypes")
public class ObjectCallbackHelper {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ObjectCallbackHelper.class);

	/**
	 * Private empty constructor to prevent initialization
	 */
	private ObjectCallbackHelper() {

	}

	/**
	 * Adds callback to the list.<br>
	 * Sorting is done while adding to avoid extra calls.
	 *
	 * @param callback
	 *            what to add
	 * @param object
	 *            add callback to which objec
	 */
	@SuppressWarnings({ "unchecked" })
	public static void addCallback(Callback callback, EnhancedObject object) {
		try {
			object.getCallbackLock().writeLock().lock();

			Map<Class<? extends Callback>, List<Callback>> cbMap = object.getCallbacks();
			if (cbMap == null) {
				cbMap = Maps.newHashMap();
				object.setCallbacks(cbMap);
			}

			List<Callback> list = cbMap.get(callback.getBaseClass());
			if (list == null) {
				list = new CopyOnWriteArrayList<Callback>();
				cbMap.put(callback.getBaseClass(), list);
			}

			CallbacksUtil.insertCallbackToList(callback, list);
		} finally {
			object.getCallbackLock().writeLock().unlock();
		}
	}

	/**
	 * Removes callback from the list
	 *
	 * @param callback
	 *            what to remove
	 * @param object
	 *            remove callback from which object
	 */
	public static void removeCallback(Callback callback, EnhancedObject object) {
		try {
			object.getCallbackLock().writeLock().lock();

			Map<Class<? extends Callback>, List<Callback>> cbMap = object.getCallbacks();
			if (GenericValidator.isBlankOrNull(cbMap)) {
				return;
			}

			List<Callback> list = cbMap.get(callback.getBaseClass());
			if (list == null || !list.remove(callback)) {
				// noinspection ThrowableInstanceNeverThrown
				log.error("Attempt to remove callback that doesn't exists", new RuntimeException());
				return;
			}

			if (list.isEmpty()) {
				cbMap.remove(callback.getBaseClass());
			}

			if (cbMap.isEmpty()) {
				object.setCallbacks(null);
			}

		} finally {
			object.getCallbackLock().writeLock().unlock();
		}
	}

	/**
	 * This method call callbacks before actual method invocation takes place
	 *
	 * @param obj
	 *            object that callbacks are invoked for
	 * @param callbackClass
	 *            base callback class
	 * @param args
	 *            args of method
	 * @return {@link Callback#beforeCall(Object, Object[])}
	 */
	@SuppressWarnings("unchecked")
	public static CallbackResult<?> beforeCall(EnhancedObject obj, Class callbackClass, Object... args) {
		Map<Class<? extends Callback>, List<Callback>> cbMap = obj.getCallbacks();
		if (GenericValidator.isBlankOrNull(cbMap)) {
			return CallbackResult.newContinue();
		}

		CallbackResult<?> cr = null;
		List<Callback> list = null;

		try {
			obj.getCallbackLock().readLock().lock();
			list = cbMap.get(callbackClass);
		} finally {
			obj.getCallbackLock().readLock().unlock();
		}

		if (GenericValidator.isBlankOrNull(list)) {
			return CallbackResult.newContinue();
		}

		for (Callback c : list) {
			try {
				cr = c.beforeCall(obj, args);
				if (cr.isBlockingCallbacks()) {
					break;
				}
			} catch (Exception e) {
				log.error("Uncaught exception in callback", e);
			}
		}

		return cr == null ? CallbackResult.newContinue() : cr;
	}

	/**
	 * This method invokes callbacks after method invocation
	 *
	 * @param obj
	 *            object that invokes this method
	 * @param callbackClass
	 *            superclass of callback
	 * @param args
	 *            method args
	 * @param result
	 *            method invokation result
	 * @return {@link Callback#afterCall(Object, Object[], Object)}
	 */
	@SuppressWarnings("unchecked")
	public static CallbackResult<?> afterCall(EnhancedObject obj, Class callbackClass, Object[] args, Object result) {
		Map<Class<? extends Callback>, List<Callback>> cbMap = obj.getCallbacks();
		if (GenericValidator.isBlankOrNull(cbMap)) {
			return CallbackResult.newContinue();
		}

		CallbackResult<?> cr = null;
		List<Callback> list = null;

		try {
			obj.getCallbackLock().readLock().lock();
			list = cbMap.get(callbackClass);
		} finally {
			obj.getCallbackLock().readLock().unlock();
		}

		if (GenericValidator.isBlankOrNull(list)) {
			return CallbackResult.newContinue();
		}

		for (Callback c : list) {
			try {
				cr = c.afterCall(obj, args, result);
				if (cr.isBlockingCallbacks()) {
					break;
				}
			} catch (Exception e) {
				log.error("Uncaught exception in callback", e);
			}
		}

		return cr == null ? CallbackResult.newContinue() : cr;
	}
}
