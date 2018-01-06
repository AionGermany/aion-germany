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
import com.aionemu.loginserver.model.base.BannedMacEntry;

/**
 *
 * @author KID
 *
 */
public abstract class BannedMacDAO implements DAO {

    public abstract boolean update(BannedMacEntry entry);

    public abstract boolean remove(String address);

    public abstract Map<String, BannedMacEntry> load();

    public abstract void cleanExpiredBans();

    @Override
    public final String getClassName() {
        return BannedMacDAO.class.getName();
    }
}
