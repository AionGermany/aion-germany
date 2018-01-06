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
package com.aionemu.gameserver.model.autogroup;

/**
 * @author xTz
 */
public enum EntryRequestType {

	NEW_GROUP_ENTRY((byte) 0),
	QUICK_GROUP_ENTRY((byte) 1),
	GROUP_ENTRY((byte) 2);

	private byte id;

	private EntryRequestType(byte id) {
		this.id = id;
	}

	public byte getId() {
		return id;
	}

	public boolean isQuickGroupEntry() {
		return id == 1;
	}

	public boolean isGroupEntry() {
		return id == 2;
	}

	public static EntryRequestType getTypeById(byte id) {
		for (EntryRequestType ert : values()) {
			if (ert.getId() == id) {
				return ert;
			}
		}
		return null;
	}
}
