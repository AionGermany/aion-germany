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
package com.aionemu.gameserver.model.templates.staticdoor;

import java.util.EnumSet;

/**
 * @author Rolandas
 */
public enum StaticDoorState {

	NONE(0),
	OPENED(1 << 0),
	CLICKABLE(1 << 1),
	CLOSEABLE(1 << 2),
	ONEWAY(1 << 3);

	private StaticDoorState(int flag) {
		this.flag = flag;
	}

	private int flag;

	public int getFlag() {
		return flag;
	}

	public static void setStates(int flags, EnumSet<StaticDoorState> state) {
		for (StaticDoorState states : StaticDoorState.values()) {
			if (states == NONE) {
				continue;
			}
			if ((flags & states.flag) == 0) {
				state.remove(states);
			}
			else {
				state.add(states);
			}
		}
	}

	public static int getFlags(EnumSet<StaticDoorState> doorStates) {
		int result = 0;
		for (StaticDoorState state : StaticDoorState.values()) {
			if (state == NONE) {
				continue;
			}
			if (doorStates.contains(state)) {
				result |= state.flag;
			}
		}
		return result;
	}
}
