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
package com.aionemu.gameserver.model.team2.common.legacy;

/**
 * @author Sarynth
 */
public enum PlayerAllianceEvent {

	LEAVE(0),
	LEAVE_TIMEOUT(0),
	BANNED(0),
	MOVEMENT(1),
	DISCONNECTED(3),
	JOIN(5),
	ENTER_OFFLINE(7),
	// Similar to 0, 1, 3 -- only the initial information block.
	UNK(9),
	RECONNECT(13),
	ENTER(13),
	UPDATE(13),
	MEMBER_GROUP_CHANGE(5),
	// Extra? Unused?
	APPOINT_VICE_CAPTAIN(13),
	DEMOTE_VICE_CAPTAIN(13),
	APPOINT_CAPTAIN(13);

	private int id;

	private PlayerAllianceEvent(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}
