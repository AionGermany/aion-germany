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
package com.aionemu.gameserver.questEngine.model;

/**
 * @author MrPoke
 */
public class QuestVars {

	private Integer[] questVars = new Integer[6];

	public QuestVars() {
	}

	public QuestVars(int var) {
		setVar(var);
	}

	/**
	 * @param id
	 * @return Quest var by id.
	 */
	public int getVarById(int id) {
		return questVars[id];
	}

	/**
	 * @param id
	 * @param var
	 */
	public void setVarById(int id, int var) {
		questVars[id] = var;
	}

	/**
	 * @return int value of all values, stored in the array. Representation: Sum(value_on_index_i * 64^i)
	 */
	public int getQuestVars() {
		int var = 0;
		for (int i = 5; i >= 0; i--) {
			var <<= 0x06;
			var |= questVars[i];
		}
		return var;
	}

	/**
	 * Fill the array with values, based on
	 *
	 * @param int
	 *            value, represented like above
	 */
	public void setVar(int var) {
		for (int i = 0; i <= 5; i++) {
			questVars[i] = var & 0x3F;
			var >>= 0x06;
		}
	}
}
