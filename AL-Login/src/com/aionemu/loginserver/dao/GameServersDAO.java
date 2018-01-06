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


package com.aionemu.loginserver.dao;

import java.util.Map;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.loginserver.GameServerInfo;

/**
 * DAO that manages GameServers
 *
 * @author -Nemesiss-
 */
public abstract class GameServersDAO implements DAO {

    /**
     * Returns all gameservers from database.
     *
     * @return all gameservers from database.
     */
    public abstract Map<Byte, GameServerInfo> getAllGameServers();

    /**
     * Returns class name that will be uses as unique identifier for all DAO
     * classes
     *
     * @return class name
     */
    @Override
    public final String getClassName() {
        return GameServersDAO.class.getName();
    }
}
