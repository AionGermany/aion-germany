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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.collection.PlayerCollectionEntry;
import com.aionemu.gameserver.model.gameobjects.player.collection.PlayerCollectionInfos;
import com.aionemu.gameserver.model.templates.collection.CollectionType;
import java.util.Map;

public abstract class PlayerCollectionDAO implements DAO {

    public abstract Map<CollectionType, PlayerCollectionInfos> loadPlayerCollection(Player player);

    public abstract boolean insertPlayerCollection(Player player, PlayerCollectionInfos infos);

    public abstract boolean updatePlayerCollection(Player player, PlayerCollectionInfos infos);

    public abstract void loadCollectionEntry(Player player);

    public abstract boolean insertCollection(Player player, PlayerCollectionEntry entry);

    public abstract boolean updateCollection(Player player, PlayerCollectionEntry entry);

    public final String getClassName() {
        return PlayerCollectionDAO.class.getName();
    }
}
