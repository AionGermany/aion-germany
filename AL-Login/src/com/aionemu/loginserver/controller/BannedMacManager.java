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


package com.aionemu.loginserver.controller;

import java.sql.Timestamp;
import java.util.Map;

import javolution.util.FastMap;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.dao.BannedMacDAO;
import com.aionemu.loginserver.model.base.BannedMacEntry;

/**
 *
 * @author KID
 *
 */
public class BannedMacManager {

    private static BannedMacManager manager = new BannedMacManager();
    private Map<String, BannedMacEntry> bannedList = new FastMap<String, BannedMacEntry>();

    public static BannedMacManager getInstance() {
        return manager;
    }
    private BannedMacDAO dao = DAOManager.getDAO(BannedMacDAO.class);

    public BannedMacManager() {
        bannedList = dao.load();
    }

    public void unban(String address, String details) {
        if (bannedList.containsKey(address)) {
            bannedList.remove(address);
            dao.remove(address);
        }
    }

    public void ban(String address, long time, String details) {
        BannedMacEntry mac = new BannedMacEntry(address, new Timestamp(time), details);
        this.bannedList.put(address, mac);
        this.dao.update(mac);
    }

    public final Map<String, BannedMacEntry> getMap() {
        return this.bannedList;
    }
}
