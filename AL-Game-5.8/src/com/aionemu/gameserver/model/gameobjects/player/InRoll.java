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
package com.aionemu.gameserver.model.gameobjects.player;

/**
 * @author xTz
 */
public class InRoll {

	private int npcId;
	private int itemId;
	private int rollType;
	private int index;

	public InRoll(int npcId, int itemId, int index, int rollType) {
		this.npcId = npcId;
		this.itemId = itemId;
		this.index = index;
		this.rollType = rollType;
	}

	public int getNpcId() {
		return npcId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getIndex() {
		return index;
	}

	public int getRollType() {
		return rollType;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	/**
	 * @param index
	 */
	public void setIndexd(int index) {
		this.index = itemId;
	}

	public void setRollType(int rollType) {
		this.rollType = rollType;
	}
}
