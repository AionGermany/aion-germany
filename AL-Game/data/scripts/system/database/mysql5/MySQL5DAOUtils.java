/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package mysql5;

/**
 * DAO utils for MySQL5
 * 
 * @author SoulKeeper
 */
public class MySQL5DAOUtils {

	/**
	 * Constant for MySQL name ;)
	 */
	public static final String MYSQL_DB_NAME = "MySQL";

	/**
	 * Returns true only if DB supports MySQL5
	 * 
	 * @param db
	 *          database name
	 * @param majorVersion
	 *          major version
	 * @param minorVersion
	 *          minor version, ignored
	 * @return supports or not
	 */
	public static boolean supports(String db, int majorVersion, int minorVersion) {
		return MYSQL_DB_NAME.equals(db) && majorVersion == 5;
	}
}
