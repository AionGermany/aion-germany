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
package com.aionemu.gameserver.model.gameobjects;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
public enum MinionAction {

	ADOPT(0), 
	DELETE(1), 
	RENAME(2), 
	LOCK(3), 
	SPAWN(4), 
	DISMISS(5), 
	GROWTH(6), 
	EVOLVE(7), 
	COMBINE(8), 
	SET_FUNCTION(9),
	USE_FUNCTION(10), 
	CHARGE(11), 
	FUNCTION_RENEW(12), 
	STOP_FUNCTION(13), 
	UNKACT(14), 
	UNKNOWN(255);

	private static TIntObjectHashMap<MinionAction> minionActions;

	static {
		minionActions = new TIntObjectHashMap<MinionAction>();
		for (MinionAction action : values()) {
			minionActions.put(action.getActionId(), action);
		}
	}

	private int actionId;

	private MinionAction(int actionId) {
		this.actionId = actionId;
	}

	public int getActionId() {
		return actionId;
	}

	public static MinionAction getActionById(int actionId) {
		MinionAction action = minionActions.get(actionId);
		return action != null ? action : UNKNOWN;
	}
}
