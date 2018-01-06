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
package com.aionemu.gameserver.utils.javaagent;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.commons.callbacks.metadata.GlobalCallback;
import com.aionemu.commons.callbacks.metadata.ObjectCallback;
import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;

public class JavaAgentUtils {

	static {
		GlobalCallbackHelper.addCallback(new CheckCallback());
	}

	public static boolean isConfigured() {
		JavaAgentUtils jau = new JavaAgentUtils();
		if (!(jau instanceof EnhancedObject)) {
			throw new Error("Please configure -javaagent jvm option.");
		}

		if (!checkGlobalCallback()) {
			throw new Error("Global callbacks are not working correctly!");
		}

		((EnhancedObject) jau).addCallback(new CheckCallback());
		if (!jau.checkObjectCallback()) {
			throw new Error("Object callbacks are not working correctly!");
		}

		return true;
	}

	@GlobalCallback(CheckCallback.class)
	private static boolean checkGlobalCallback() {
		return false;
	}

	@ObjectCallback(CheckCallback.class)
	private boolean checkObjectCallback() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static class CheckCallback implements Callback {

		@Override
		public CallbackResult<Boolean> beforeCall(Object obj, Object[] args) {
			return CallbackResult.newFullBlocker(true);
		}

		@Override
		public CallbackResult<Boolean> afterCall(Object obj, Object[] args, Object methodResult) {
			return CallbackResult.newContinue();
		}

		@Override
		public Class<? extends Callback> getBaseClass() {
			return CheckCallback.class;
		}
	}
}
