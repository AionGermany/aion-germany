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

import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfCooperationRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfDisciplineRank;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 */
public class SM_MY_DOCUMENTATION extends AionServerPacket {

	private int tableId;
	private ArenaOfDisciplineRank arenaOfDiscipline;
	private ArenaOfCooperationRank arenaOfCooperation;

	public SM_MY_DOCUMENTATION(int tableId, ArenaOfDisciplineRank ranking) {
		this.tableId = tableId;
		this.arenaOfDiscipline = ranking;
	}

	public SM_MY_DOCUMENTATION(int tableId, ArenaOfCooperationRank ranking) {
		this.tableId = tableId;
		this.arenaOfCooperation = ranking;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(tableId);
		switch (tableId) {
		case 541: // Arena Of Discipline
			writeD(arenaOfDiscipline.getRank()); // actual Rank
			writeD(arenaOfDiscipline.getPoints()); // current Points
			writeD(1);
			writeD(arenaOfDiscipline.getPossitionMatch()); // position match
			writeD(arenaOfDiscipline.getBestRank());// lasted rank
			writeD(arenaOfDiscipline.getLastPoints()); // last Points
			writeD(arenaOfDiscipline.getLowPoints()); // low points
			writeD(arenaOfDiscipline.getHighPoints()); // high points
			return;
		case 741: // Arena Of Cooperation
			writeD(arenaOfCooperation.getRank()); // actual Rank
			writeD(arenaOfCooperation.getPoints()); // current Points
			writeD(1);
			writeD(arenaOfCooperation.getPossitionMatch()); // position match
			writeD(arenaOfCooperation.getBestRank());// lasted rank
			writeD(arenaOfCooperation.getLastPoints()); // last Points
			writeD(arenaOfCooperation.getLowPoints()); // low points
			writeD(arenaOfCooperation.getHighPoints()); // high points
			return;
		default:
			writeD(0); // actual Rank
			writeD(0); // current Points
			writeD(0);
			writeD(0);
			writeD(0);// lasted rank
			writeD(0); // last Points
			writeD(0); // low points
			writeD(0); // high points
			return;
		}
	}
}