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

import java.sql.Timestamp;
import java.util.List;

import com.aionemu.commons.database.dao.DAO;

/**
 * @author Alcapwnd
 * @reworked Lyras
 */
public abstract class PlayerPassportsDAO implements DAO {

	public abstract void insertPassport(int accountId, int passportId, int stamps, Timestamp last_stamp);

	public abstract void updatePassport(int accountId, int passportId, int stamps, boolean rewarded, Timestamp last_stamp);

	public abstract int getStamps(int accountId, int passportId);

	public abstract Timestamp getLastStamp(int accountId, int passportId);

	public abstract List<Integer> getPassports(int accountId);

	@Override
	public final String getClassName() {
		return PlayerPassportsDAO.class.getName();
	}
}
