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
 * Represents a player who has been blocked
 *
 * @author Ben
 */
public class BlockedPlayer {

	PlayerCommonData pcd;
	String reason;

	public BlockedPlayer(PlayerCommonData pcd) {
		this(pcd, "");
	}

	public BlockedPlayer(PlayerCommonData pcd, String reason) {
		this.pcd = pcd;
		this.reason = reason;
	}

	public int getObjId() {
		return pcd.getPlayerObjId();
	}

	public String getName() {
		return pcd.getName();
	}

	public String getReason() {
		return reason;
	}

	public synchronized void setReason(String reason) {
		this.reason = reason;
	}
}
