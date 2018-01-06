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

import java.util.Collection;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Source & xTz
 */
public class SM_CONQUEROR_PROTECTOR extends AionServerPacket {

	private int type;
	private int debuffLvl;
	private Collection<Player> players;
	private Player player;

	public SM_CONQUEROR_PROTECTOR(boolean showMsg, int debuffLvl) {
		this.type = showMsg ? 1 : 0;
		this.debuffLvl = debuffLvl;
	}

	public SM_CONQUEROR_PROTECTOR(Collection<Player> players, boolean intruderRadar) {
		this.type = intruderRadar ? 5 : 4;
		this.players = players;
		GameServer.log.info("Sending SM_SERIAL_KILLER Type: " + type + " Players Size: " + players.size());
	}

	public SM_CONQUEROR_PROTECTOR(Player player, boolean isProtector, boolean broadcastPacket, int buffLvl) {
		this.player = player;
		if (broadcastPacket) {
			this.type = isProtector ? 9 : 6;
		}
		else {
			this.type = isProtector ? 8 : 1;
		}
		this.debuffLvl = buffLvl;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(type);
		writeD(1); // 0x01
		writeD(1); // 0x01
		switch (type) {
			// case 0: // Conqueror Without Msg (not used)
			case 1: // Conqueror With Msg
			case 6: // goes to other players - Conquerer
				// case 7: // Protector Without msg (not used)
			case 8: // Protector With Msg
			case 9: // goes to other players - Protector
				writeH(1); // size ?!
				writeD(debuffLvl); // lvl
				writeD(type == 9 || type == 6 ? player.getObjectId() : 0);
				break;
			case 4: // Automatic Territory Intruder Scan
				writeH(players.size());
				for (Player player : players) {
					writeD(1);// player.getSKInfo().getRank());
					writeD(player.getObjectId());
					writeD(0x01); // unk
					writeD(player.getAbyssRank().getRank().getId());
					writeH(player.getLevel());
					writeF(player.getX());
					writeF(player.getY());
					writeS(player.getName(), 134);
					// 134
					/*
					 * //fill the rest with some crap ... int nameSize = (player.getName().length() * 2) + 2; int fordiffsize = (134 - nameSize)/4; //int restdiffsize = 134 - (fordiffsize*4); for (int
					 * i=0; i < fordiffsize; i++) { writeD(Rnd.get(0, 8547584)); } if (restdiffsize > 0) { for (int i=0; i < restdiffsize;i++) writeC(5); }
					 */ writeH(4); // unk
				}
				break;
			case 5: // Intruder Radar
				writeH(players.size());
				for (Player player : players) {
					writeD(player.getConquerorProtectorData().getConquerorBuffLevel());
					writeD(player.getObjectId());
					writeD(0x01); // unk
					writeD(player.getAbyssRank().getRank().getId());
					writeH(player.getLevel());
					writeF(player.getX());
					writeF(player.getY());
					writeS(player.getName(), 134);
					writeH(0); // unk
				}
				break;
		}
	}
}
