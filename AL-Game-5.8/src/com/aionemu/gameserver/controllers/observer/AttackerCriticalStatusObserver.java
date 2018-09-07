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
package com.aionemu.gameserver.controllers.observer;

import com.aionemu.gameserver.controllers.attack.AttackStatus;

/**
 * @author kecimis
 */
public class AttackerCriticalStatusObserver extends AttackCalcObserver {

	protected AttackerCriticalStatus acStatus = null;
	protected AttackStatus status;

	public AttackerCriticalStatusObserver(AttackStatus status, int count, int value, boolean isPercent) {
		this.status = status;
		this.acStatus = new AttackerCriticalStatus(count, value, isPercent);
	}

	public int getCount() {
		return acStatus.getCount();
	}

	public void decreaseCount() {
		acStatus.setCount((acStatus.getCount() - 1));
	}
}
