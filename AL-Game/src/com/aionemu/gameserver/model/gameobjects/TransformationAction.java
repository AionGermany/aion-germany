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
 * @author Falke_34, FrozenKiller
 */
public enum TransformationAction {

	ADOPT(0),
	TRANSFORM(1),
	COMBINE(2),
	USEBONUS(3), // Collection
	UNKNOWN(255);

	private static TIntObjectHashMap<TransformationAction> transformationActions;

	static {
		transformationActions = new TIntObjectHashMap<TransformationAction>();
		for (TransformationAction action : values()) {
			transformationActions.put(action.getActionId(), action);
		}
	}

	private int actionId;

	private TransformationAction(int actionId) {
		this.actionId = actionId;
	}

	public int getActionId() {
		return actionId;
	}

	public static TransformationAction getActionById(int actionId) {
		TransformationAction action = transformationActions.get(actionId);
		return action != null ? action : UNKNOWN;
	}
}
