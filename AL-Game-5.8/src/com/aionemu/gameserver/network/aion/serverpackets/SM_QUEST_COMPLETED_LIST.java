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
import com.aionemu.gameserver.questEngine.model.QuestState;

import javolution.util.FastList;

/**
 * @author MrPoke
 */
public class SM_QUEST_COMPLETED_LIST extends AionServerPacket {

	private FastList<QuestState> questState;

	public SM_QUEST_COMPLETED_LIST(FastList<QuestState> questState) {
		this.questState = questState;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(0x01); // 2.1
		writeH(-questState.size() & 0xFFFF);
		for (QuestState qs : questState) {
			writeD(qs.getQuestId());
			writeD(qs.getCompleteCount());
			writeD(1); // unk 5.6
		}
		FastList.recycle(questState);
		questState = null;
	}
}
