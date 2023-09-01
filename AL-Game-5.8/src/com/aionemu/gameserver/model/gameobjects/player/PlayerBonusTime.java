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

import java.sql.Timestamp;

/**
 * @author Steve
 * @modified Alex
 */
public class PlayerBonusTime {

	private Timestamp time;
	private PlayerBonusTimeStatus status;

	public PlayerBonusTime() {
		this.time = null;
		this.status = PlayerBonusTimeStatus.NORMAL;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public void setStatus(PlayerBonusTimeStatus status) {
		this.status = status;
	}

	public Timestamp getTime() {
		return time;
	}

	public PlayerBonusTimeStatus getStatus() {
		return status;
	}

	public boolean isBonus() {
		return getStatus().isBonus();
	}
}
