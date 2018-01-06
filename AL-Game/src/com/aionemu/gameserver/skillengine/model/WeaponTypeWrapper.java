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
package com.aionemu.gameserver.skillengine.model;

import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.services.MotionLoggingService;

/**
 * @author kecimis
 */
public class WeaponTypeWrapper implements Comparable<WeaponTypeWrapper> {

	private WeaponType mainHand = null;
	private WeaponType offHand = null;

	public WeaponTypeWrapper(WeaponType mainHand, WeaponType offHand) {
		if (mainHand != null && offHand != null) {
			switch (mainHand) {
				case DAGGER_1H:
					this.mainHand = WeaponType.DAGGER_1H;
					this.offHand = WeaponType.DAGGER_1H;
					break;
				case SWORD_1H:
					this.mainHand = WeaponType.SWORD_1H;
					this.offHand = WeaponType.SWORD_1H;
					break;
				case TOOLHOE_1H:
					this.mainHand = WeaponType.TOOLHOE_1H;
					this.offHand = WeaponType.TOOLHOE_1H;
				case GUN_1H:
					this.mainHand = WeaponType.GUN_1H;
					this.offHand = WeaponType.GUN_1H;
					break;
				default:
					this.mainHand = mainHand;
					this.offHand = null;
			}
		}
		else {
			this.mainHand = mainHand;
			this.offHand = offHand;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WeaponTypeWrapper other = (WeaponTypeWrapper) obj;
		if (!getOuterType().equals(other.getOuterType())) {
			return false;
		}
		if (mainHand != other.mainHand) {
			return false;
		}
		if (offHand != other.offHand) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mainHandType=\"" + (mainHand != null ? mainHand.toString() : "null") + "\"" + " offHandType=\"" + (offHand != null ? offHand.toString() : "null");
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getOuterType().hashCode();
		result = prime * result + ((mainHand == null) ? 0 : mainHand.hashCode());
		result = prime * result + ((offHand == null) ? 0 : offHand.hashCode());
		return result;
	}

	@Override
	public int compareTo(WeaponTypeWrapper o) {
		if (mainHand == null || o.getMainHand() == null) {
			return 0;
		}
		else if (offHand != null && o.getOffHand() != null) {
			return 0;
		}
		else if (offHand != null && o.getOffHand() == null) {
			return 1;
		}
		else if (offHand == null && o.getOffHand() != null) {
			return -1;
		}
		else {
			return mainHand.toString().compareTo(o.getMainHand().toString());
		}
	}

	public WeaponType getMainHand() {
		return this.mainHand;
	}

	public WeaponType getOffHand() {
		return this.offHand;
	}

	private MotionLoggingService getOuterType() {
		return MotionLoggingService.getInstance();
	}
}
