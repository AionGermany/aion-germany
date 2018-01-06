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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.player.FriendList.Status;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Ben
 */
public class Friend {

	private static final Logger log = LoggerFactory.getLogger(Friend.class);
	private PlayerCommonData pcd;

	private String friendNote = "";

	public Friend(PlayerCommonData pcd) {
		this.pcd = pcd;
	}

	/**
	 * Returns the status of this player
	 *
	 * @return Friend's status
	 */
	public Status getStatus() {
		// second check is temporary
		if (pcd.getPlayer() == null || !pcd.isOnline()) {
			return FriendList.Status.OFFLINE;
		}
		return pcd.getPlayer().getFriendList().getStatus();
	}

	public void setPCD(PlayerCommonData pcd) {
		this.pcd = pcd;
	}

	/**
	 * Returns this friend's name
	 *
	 * @return Friend's name
	 */
	public String getName() {
		return pcd.getName();
	}

	public int getLevel() {
		return pcd.getLevel();
	}

	public String getNote() {
		return pcd.getNote();
	}

	public String getFriendNote() {
		return friendNote;
	}

	public void setNote(String note) {
		friendNote = note;
	}

	public PlayerClass getPlayerClass() {
		return pcd.getPlayerClass();
	}

	public int getMapId() {
		WorldPosition position = pcd.getPosition();
		if (position == null) {
			// doubt its possible, but need check warnings
			log.warn("Null friend position: {}", pcd.getPlayerObjId());
			return 0;
		}
		return position.getMapId();
	}

	/**
	 * Gets the last time this player was online as a unix timestamp<br />
	 * Returns 0 if the player is online now
	 *
	 * @return Unix timestamp the player was last online
	 */
	public int getLastOnlineTime() {
		if (pcd.getLastOnline() == null || isOnline()) {
			return 0;
		}

		return (int) (pcd.getLastOnline().getTime() / 1000); // Convert to int, unix time format (ms -> seconds)
	}

	public int getOid() {
		return pcd.getPlayerObjId();
	}

	public Player getPlayer() {
		return pcd.getPlayer();
	}

	public boolean isOnline() {
		return pcd.isOnline();
	}
}
