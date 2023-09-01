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

import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * @author ATracer
 */
public class PlayerSettings {

	private PersistentState persistentState;
	private byte[] uiSettings;
	private byte[] shortcuts;
	private byte[] houseBuddies;
	private int deny = 0;
	private int display = 0;

	public PlayerSettings() {
	}

	public PlayerSettings(byte[] uiSettings, byte[] shortcuts, byte[] houseBuddies, int deny, int display) {
		this.uiSettings = uiSettings;
		this.shortcuts = shortcuts;
		this.houseBuddies = houseBuddies;
		this.deny = deny;
		this.display = display;
	}

	/**
	 * @return the persistentState
	 */
	public PersistentState getPersistentState() {
		return persistentState;
	}

	/**
	 * @param persistentState
	 *            the persistentState to set
	 */
	public void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	/**
	 * @return the uiSettings
	 */
	public byte[] getUiSettings() {
		return uiSettings;
	}

	/**
	 * @param uiSettings
	 *            the uiSettings to set
	 */
	public void setUiSettings(byte[] uiSettings) {
		this.uiSettings = uiSettings;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * @return the shortcuts
	 */
	public byte[] getShortcuts() {
		return shortcuts;
	}

	/**
	 * @param shortcuts
	 *            the shortcuts to set
	 */
	public void setShortcuts(byte[] shortcuts) {
		this.shortcuts = shortcuts;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * @return the houseBuddies
	 */
	public byte[] getHouseBuddies() {
		return houseBuddies;
	}

	/**
	 * @param houseBuddies
	 *            the houseBuddies to set
	 */
	public void setHouseBuddies(byte[] houseBuddies) {
		this.houseBuddies = houseBuddies;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * @return the display
	 */
	public int getDisplay() {
		return display;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(int display) {
		this.display = display;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * @return the deny
	 */
	public int getDeny() {
		return deny;
	}

	/**
	 * @param deny
	 *            the deny to set
	 */
	public void setDeny(int deny) {
		this.deny = deny;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	public boolean isInDeniedStatus(DeniedStatus deny) {
		int isDeniedStatus = this.deny & deny.getId();

		if (isDeniedStatus == deny.getId()) {
			return true;
		}

		return false;
	}
}
