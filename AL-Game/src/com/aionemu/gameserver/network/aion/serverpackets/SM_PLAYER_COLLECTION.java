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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.collection.PlayerCollection;
import com.aionemu.gameserver.model.templates.collection.CollectionType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_PLAYER_COLLECTION extends AionServerPacket {

    private final Player player;

    public SM_PLAYER_COLLECTION(Player player) {
        this.player = player;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        PlayerCollection playerCollection = player.getPlayerCollection();
        writeH(playerCollection.getCollectionInfos().get(CollectionType.COMMON).getLevel());
        writeD(playerCollection.getCollectionInfos().get(CollectionType.COMMON).getExp());
        writeH(playerCollection.getCollectionInfos().get(CollectionType.ANCIENT).getLevel());
        writeD(playerCollection.getCollectionInfos().get(CollectionType.ANCIENT).getExp());
        writeH(playerCollection.getCollectionInfos().get(CollectionType.RELIC).getLevel());
        writeD(playerCollection.getCollectionInfos().get(CollectionType.RELIC).getExp());
        writeH(playerCollection.getCollectionInfos().get(CollectionType.EVENT).getLevel());
        writeD(playerCollection.getCollectionInfos().get(CollectionType.EVENT).getExp());
        writeH(0);
        writeD(50);
    }
}
