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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34, FrozenKiller
 */
public class SM_MONSTERBOOK extends AionServerPacket {

    private int id;
    private int kill;
    private byte level;
    private int rewardLevel;

    public SM_MONSTERBOOK(int id, int kill, byte level, int rewardLevel) {
        this.id = id;
        this.kill = kill;
        this.level = level;
        this.rewardLevel = rewardLevel;
    }

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(id); // MonsterBook ID
		writeD(kill); // Kill Count
		writeC(level); // Level
		writeC(rewardLevel); // Reward Level
	}
}
