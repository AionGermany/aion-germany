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
package com.aionemu.gameserver.model.account;

/**
 * @author cura
 */
public class CharacterPasskey {

	private int objectId;
	private int wrongCount = 0;
	private boolean isPass = false;
	private ConnectType connectType;

	/**
	 * @return the objectId
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId
	 *            the objectId to set
	 */
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the wrongCount
	 */
	public int getWrongCount() {
		return wrongCount;
	}

	/**
	 * @param count
	 *            the wrongCount to set
	 */
	public void setWrongCount(int count) {
		this.wrongCount = count;
	}

	/**
	 * @return the isPass
	 */
	public boolean isPass() {
		return isPass;
	}

	/**
	 * @param isPass
	 *            the isPass to set
	 */
	public void setIsPass(boolean isPass) {
		this.isPass = isPass;
	}

	/**
	 * @return the connectType
	 */
	public ConnectType getConnectType() {
		return connectType;
	}

	/**
	 * @param connectType
	 *            the connectType to set
	 */
	public void setConnectType(ConnectType connectType) {
		this.connectType = connectType;
	}

	public enum ConnectType {

		ENTER,
		DELETE
	}
}
