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

package com.aionemu.commons.database.dao;

/**
 * This class represents basic DAO. It should be subclasses by abstract class
 * and that class has to implement method {@link #getClassName()}.<br>
 * This class must return {@link Class#getName()}, {@link #getClassName()}
 * should be final.<br>
 * DAO subclass must have public no-arg constructor, in other case
 * {@link InstantiationException} will be thrown by
 * {@link com.aionemu.commons.database.dao.DAOManager}
 * 
 * @author SoulKeeper
 */
public interface DAO {

	/**
	 * Unique identifier for DAO class, all subclasses must have same
	 * identifiers. Must return {@link Class#getName()} of abstract class
	 * 
	 * @return identifier of DAO class
	 */
	public String getClassName();

	/**
	 * Returns true if DAO implementation supports database or false if not.
	 * Database information is provided by {@link java.sql.DatabaseMetaData}
	 * 
	 * @param databaseName
	 *            name of database
	 * @param majorVersion
	 *            major version of database
	 * @param minorVersion
	 *            minor version of database
	 * @return true if database is supported or false in other case
	 */
	public boolean supports(String databaseName, int majorVersion, int minorVersion);
}
