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

import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.model.AbyssRankingResult;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Rhys2002, zdead, LokiReborn
 */
public class SM_ABYSS_RANKING_PLAYERS extends AionServerPacket {

	private List<AbyssRankingResult> data;
	private int lastUpdate;
	private int race;
	private int page;
	private boolean isEndPacket;

	public SM_ABYSS_RANKING_PLAYERS(int lastUpdate, List<AbyssRankingResult> data, Race race, int page, boolean isEndPacket) {
		this.lastUpdate = lastUpdate;
		this.data = data;
		this.race = race.getRaceId();
		this.page = page;
		this.isEndPacket = isEndPacket;
	}

	public SM_ABYSS_RANKING_PLAYERS(int lastUpdate, Race race) {
		this.lastUpdate = lastUpdate;
		this.data = Collections.emptyList();
		this.race = race.getRaceId();
		this.page = 0;
		this.isEndPacket = false;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(race); // 0:Elyos 1:Asmo
		writeD(lastUpdate); // Date
		writeD(page); // Current page
		writeD(isEndPacket ? 0x7F : 0); // 0:Nothing 1:Update Table
		writeH(data.size()); // list size

		for (AbyssRankingResult rs : data) {
			writeD(rs.getRankPos()); // Current Rank
			writeD(rs.getPlayerAbyssRank()); // Abyss rank
			writeD((rs.getOldRankPos() == 0) ? 501 : rs.getOldRankPos()); // Old Rank
			writeD(rs.getPlayerId()); // PlayerID
			writeD(race);
			writeD(rs.getPlayerClass().getClassId()); // Class Id
			writeD(rs.getGender().getGenderId()); // Sex ? 0=male / 1=female
			writeQ(rs.getPlayerAP()); // Abyss Points
			writeD(rs.getPlayerGP()); // Glory Points
			writeH(rs.getPlayerLevel());
			writeS(rs.getPlayerName(), 52); // Player Name
			writeS(rs.getLegionName(), 86); // Legion Name
		}
	}
}
