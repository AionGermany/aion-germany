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
package com.aionemu.gameserver.network.aion;

/**
 * @author ATracer
 */
public enum InventoryPacketType {

	WAREHOUSE(false, false, false),
	INVENTORY(true, false, false),
	MAIL_REPURCHASE(false, true, false),
	PRIVATE_STORE(false, false, true),
	WEAPON_SWITCH(true, false, false, true);

	private boolean isInventory;
	private boolean isMailOrRepurchase;
	private boolean isPrivateStore;
	private boolean isWeaponSwitch;

	private InventoryPacketType(boolean isInventory, boolean isMail, boolean isPrivateStore) {
		this(isInventory, isMail, isPrivateStore, false);
	}

	private InventoryPacketType(boolean isInventory, boolean isMail, boolean isPrivateStore, boolean isWeaponSwitch) {
		this.isInventory = isInventory;
		this.isMailOrRepurchase = isMail;
		this.isPrivateStore = isPrivateStore;
		this.isWeaponSwitch = isWeaponSwitch;
	}

	public final boolean isInventory() {
		return isInventory;
	}

	public final boolean isMail() {
		return isMailOrRepurchase;
	}

	public final boolean isRepurchase() {
		return isMailOrRepurchase;
	}

	public final boolean isPrivateStore() {
		return isPrivateStore;
	}

	public final boolean isWeaponSwitch() {
		return isWeaponSwitch;
	}
}
