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
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;

/**
 * @author cura
 */
public abstract class PlayerPasskeyDAO implements DAO {

	/**
	 * @param accountId
	 * @param passkey
	 */
	public abstract void insertPlayerPasskey(int accountId, String passkey);

	/**
	 * @param accountId
	 * @param oldPasskey
	 * @param newPasskey
	 * @return
	 */
	public abstract boolean updatePlayerPasskey(int accountId, String oldPasskey, String newPasskey);

	/**
	 * @param accountId
	 * @param newPasskey
	 * @return
	 */
	public abstract boolean updateForcePlayerPasskey(int accountId, String newPasskey);

	/**
	 * @param accountId
	 * @param passkey
	 * @return
	 */
	public abstract boolean checkPlayerPasskey(int accountId, String passkey);

	/**
	 * @param accountId
	 * @return
	 */
	public abstract boolean existCheckPlayerPasskey(int accountId);

	/*
	 * (non-Javadoc)
	 * @see com.aionemu.commons.database.dao.DAO#getClassName()
	 */
	@Override
	public final String getClassName() {
		return PlayerPasskeyDAO.class.getName();
	}
}
