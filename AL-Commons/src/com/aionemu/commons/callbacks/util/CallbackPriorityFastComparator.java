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

import com.aionemu.commons.callbacks.Callback;

import javolution.util.FastComparator;

public class CallbackPriorityFastComparator extends FastComparator<Callback<?>> {

	private static final long serialVersionUID = 5346780764438744817L;
	private final CallbackPriorityComparator cpc = new CallbackPriorityComparator();

	@Override
	public int hashCodeOf(Callback<?> obj) {
		return obj.hashCode();
	}

	@Override
	public boolean areEqual(Callback<?> o1, Callback<?> o2) {
		return cpc.compare(o1, o2) == 0;
	}

	@Override
	public int compare(Callback<?> o1, Callback<?> o2) {
		return cpc.compare(o1, o2);
	}
}