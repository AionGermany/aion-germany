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

import javax.annotation.Nullable;

import com.google.common.base.Function;

/**
 * This is the base class for all "in-game" objects, that player can interact with, such as: npcs, monsters, players, items.<br>
 * <br>
 * Each AionObject is uniquely identified by objectId.
 *
 * @author -Nemesiss-, SoulKeeper
 */
public abstract class AionObject {

	public static Function<AionObject, Integer> OBJECT_TO_ID_TRANSFORMER = new Function<AionObject, Integer>() {

		@Override
		public Integer apply(@Nullable AionObject input) {
			return input != null ? input.getObjectId() : null;
		}
	};
	/**
	 * Unique id, for all game objects such as: items, players, monsters.
	 */
	private Integer objectId;

	public AionObject(Integer objId) {
		this.objectId = objId;
	}

	/**
	 * Returns unique ObjectId of AionObject
	 *
	 * @return Int ObjectId
	 */
	public Integer getObjectId() {
		return objectId;
	}

	/**
	 * Returns name of the object.<br>
	 * Unique for players, common for NPCs, items, etc
	 *
	 * @return name of the object
	 */
	public abstract String getName();
}
