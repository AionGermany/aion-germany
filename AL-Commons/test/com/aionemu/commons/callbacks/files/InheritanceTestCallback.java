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

package com.aionemu.commons.callbacks.files;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;

@SuppressWarnings("rawtypes")
public class InheritanceTestCallback implements Callback<InheritanceTestSuperclass> {

	private final String res;

	public InheritanceTestCallback(String res) {
		this.res = res;
	}

	@Override
	public CallbackResult beforeCall(InheritanceTestSuperclass obj, Object[] args) {
		return CallbackResult.newContinue();
	}

	@Override
	public CallbackResult afterCall(InheritanceTestSuperclass obj, Object[] args, Object methodResult) {
		return CallbackResult.newFullBlocker(res);
	}

	@Override
	public Class<? extends Callback> getBaseClass() {
		return InheritanceTestCallback.class;
	}
}
