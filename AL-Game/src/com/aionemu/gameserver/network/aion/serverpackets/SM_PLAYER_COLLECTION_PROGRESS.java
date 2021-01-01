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
import com.aionemu.gameserver.model.gameobjects.player.collection.PlayerCollectionEntry;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_PLAYER_COLLECTION_PROGRESS extends AionServerPacket {

    private final Player player;

    public SM_PLAYER_COLLECTION_PROGRESS(Player player) {
        this.player = player;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeH(player.getPlayerCollection().getPlayerCollectionEntry().size());
        for (PlayerCollectionEntry entry : player.getPlayerCollection().getPlayerCollectionEntry().values()) {
            writeD(entry.getId());
            writeC(entry.isItem1() ? 1 : 0);
            writeC(entry.isItem2() ? 1 : 0);
            writeC(entry.isItem3() ? 1 : 0);
            writeC(entry.isItem4() ? 1 : 0);
            writeC(entry.isItem5() ? 1 : 0);
            writeC(entry.isItem6() ? 1 : 0);
            writeC(entry.isItem7() ? 1 : 0);
            writeC(entry.isItem8() ? 1 : 0);
            writeC(entry.isItem9() ? 1 : 0);
            writeC(entry.isItem10() ? 1 : 0);
            writeC(entry.isItem11() ? 1 : 0);
            writeC(entry.isItem12() ? 1 : 0);
            writeC(entry.isItem13() ? 1 : 0);
            writeC(entry.isItem14() ? 1 : 0);
        }
    }
}
