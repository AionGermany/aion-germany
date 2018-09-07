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

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.PacketLoggerService;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Cx3
 */
public class SM_EMOTION_SWITCH extends AionServerPacket {

	private Creature npc;
	private Player pl;
	private int state = 0;
	private int emotionType = 0;
	private boolean isPlayer = false;

	public SM_EMOTION_SWITCH(Npc npc, int state, EmotionType et) {
		this.npc = npc;
		this.state = state;
		this.emotionType = et.getTypeId();
		this.isPlayer = false;
	}

	public SM_EMOTION_SWITCH(Player pl, int state, EmotionType et) {
		this.pl = pl;
		this.state = state;
		this.emotionType = et.getTypeId();
		this.isPlayer = true;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		PacketLoggerService.getInstance().logPacketSM(this.getPacketName());
		if (isPlayer)
			writeD(pl.getObjectId());
		else
			writeD(npc.getObjectId());
		writeC(state);
		writeD(emotionType);
		writeD(0);
	}
}
