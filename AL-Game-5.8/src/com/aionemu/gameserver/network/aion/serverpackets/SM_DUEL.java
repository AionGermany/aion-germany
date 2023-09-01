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

import com.aionemu.gameserver.model.DuelResult;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author xavier
 */
public class SM_DUEL extends AionServerPacket {

	private String playerName;
	private DuelResult result;
	private int requesterObjId;
	private int type;

	private SM_DUEL(int type) {
		this.type = type;
	}

	public static SM_DUEL SM_DUEL_STARTED(int requesterObjId) {
		SM_DUEL packet = new SM_DUEL(0x00);
		packet.setRequesterObjId(requesterObjId);
		return packet;
	}

	private void setRequesterObjId(int requesterObjId) {
		this.requesterObjId = requesterObjId;
	}

	public static SM_DUEL SM_DUEL_RESULT(DuelResult result, String playerName) {
		SM_DUEL packet = new SM_DUEL(0x01);
		packet.setPlayerName(playerName);
		packet.setResult(result);
		return packet;
	}

	private void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	private void setResult(DuelResult result) {
		this.result = result;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(type);

		switch (type) {
			case 0x00:
				writeD(requesterObjId);
				break;
			case 0x01:
				writeC(result.getResultId()); // unknown
				writeD(result.getMsgId());
				writeS(playerName);
				break;
			case 0xE0:
				break;
			default:
				throw new IllegalArgumentException("invalid SM_DUEL packet type " + type);
		}
	}
}
