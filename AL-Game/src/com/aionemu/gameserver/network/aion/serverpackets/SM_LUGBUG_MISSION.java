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

import com.aionemu.gameserver.model.templates.lugbug.LugbugQuestTemplate;
import com.aionemu.gameserver.model.templates.lugbug.LugbugSpecialQuestTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 */
public class SM_LUGBUG_MISSION extends AionServerPacket {

	@SuppressWarnings("unused")
	private Collection<LugbugQuestTemplate> lugbug_quests;
	@SuppressWarnings("unused")
	private Collection<LugbugSpecialQuestTemplate> lugbug_special_quests;

	public SM_LUGBUG_MISSION(Collection<LugbugQuestTemplate> lugbug_quests) {
		this.lugbug_quests = lugbug_quests;
	}

	public SM_LUGBUG_MISSION() {

	}

	@Override
	protected void writeImpl(AionConnection con) {
		System.out.println("MISSION");
		writeD(1586509115); // Current Time / Login Time ?
		writeD(0);
		writeH(2); // Case ?
		// Final Daily Reward Start
		writeC(1);
		writeD(21813);
		writeD(0);
		writeD(23); // Reward
		writeQ(0);
		writeH(1);
		writeC(1);
		writeC(0); // Finish Count
		writeB(new byte[24]); // 24 x 0
		writeD(1586502000);
		writeD(0);
		writeD(1586504044);
		writeD(0);
		// Final Daily Reward End

		// Daily Quests Start
		writeH(1); // size Daily
		writeD(43014692);
		writeD(0);
		writeD(2022); // Quest
		writeD(21813); // Identifyer Daily
		writeD(0);
		writeC(3); // Always 3
		writeC(0);
		writeC(1); // Active
		writeB(new byte[25]); // TODO Kill count
		writeD(1586502000);
		writeD(0);
		writeD(1586504037);
		writeD(0);
		// Daily Quests End

		// Final Weekly Reward Start
		writeC(1);
		writeD(21826); // Identifyer Weekly
		writeD(0);
		writeD(26); // Reward
		writeQ(0);
		writeH(2);
		writeC(1);
		writeC(0); // Finish Count
		writeB(new byte[24]); // 24 x 0
		writeD(1586329200);
		writeD(0);
		writeD(1586340953);
		writeD(0);
		// Final Weekly Reward End

		// Weekly Quests Start
		writeH(1); // size Weekly
		writeD(42647808);
		writeD(0);
		writeD(3021); // Quest
		writeD(21826); // Identifyer Weekly
		writeD(0);
		writeC(3); // Always 3
		writeC(0);
		writeC(1); // Active
		writeB(new byte[25]); // TODO Kill count
		writeD(1586329200);
		writeD(0);
		writeD(1586340953);
		writeD(0);
		// Weekly Quests End

	}
}
