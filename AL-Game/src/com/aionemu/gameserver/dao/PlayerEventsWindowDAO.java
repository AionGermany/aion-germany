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
import com.aionemu.gameserver.model.event_window.PlayerEventWindowList;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ghostfur (Aion-Unique)
 */
public abstract class PlayerEventsWindowDAO
        implements DAO {
    public abstract PlayerEventWindowList load(Player id);

    public abstract void insert(int id, int eventId, Timestamp last_stamp);

    public abstract boolean store(int id, int eventId, Timestamp last_stamp, int elapsed);

    public abstract void delete(int id, int eventId);

    public abstract Timestamp getLastStamp(int id, int eventId);

    public abstract double getElapsed(int id);

    public abstract void updateElapsed(int id, double eventId);

    public abstract List<Integer> getEventsWindow(int id);

    public final String getClassName() {
        return PlayerEventsWindowDAO.class.getName();
    }
}
