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

import java.util.Map;

import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.SiegeService;

public class SM_FORTRESS_STATUS extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		Map<Integer, FortressLocation> fortresses = SiegeService.getInstance().getFortresses();
		Influence inf = Influence.getInstance();

		writeC(1);
		writeD(SiegeService.getInstance().getSecondsBeforeHourEnd());
		writeF(inf.getGlobalElyosInfluence());
		writeF(inf.getGlobalAsmodiansInfluence());
		writeF(inf.getGlobalBalaursInfluence());
		writeH(8);
		writeD(210050000);
		writeF(inf.getInggisonElyosInfluence());
		writeF(inf.getInggisonAsmodiansInfluence());
		writeF(inf.getInggisonBalaursInfluence());
		writeD(220070000);
		writeF(inf.getGelkmarosElyosInfluence());
		writeF(inf.getGelkmarosAsmodiansInfluence());
		writeF(inf.getGelkmarosBalaursInfluence());
		writeD(400010000);
		writeF(inf.getAbyssElyosInfluence());
		writeF(inf.getAbyssAsmodiansInfluence());
		writeF(inf.getAbyssBalaursInfluence());
		writeD(400020000);
		writeF(inf.getBelusElyosInfluence());
		writeF(inf.getBelusAsmodiansInfluence());
		writeF(inf.getBelusBalaursInfluence());
		writeD(400040000);
		writeF(inf.getAspidaElyosInfluence());
		writeF(inf.getAspidaAsmodiansInfluence());
		writeF(inf.getAspidaBalaursInfluence());
		writeD(400050000);
		writeF(inf.getAtanatosElyosInfluence());
		writeF(inf.getAtanatosAsmodiansInfluence());
		writeF(inf.getAtanatosBalaursInfluence());
		writeD(400060000);
		writeF(inf.getDisillonElyosInfluence());
		writeF(inf.getDisillonAsmodiansInfluence());
		writeF(inf.getDisillonBalaursInfluence());
		writeD(600090000);
		writeF(inf.getKaldorElyosInfluence());
		writeF(inf.getKaldorAsmodiansInfluence());
		writeF(inf.getKaldorBalaursInfluence());

		for (FortressLocation fortress : fortresses.values()) {
			writeD(fortress.getLocationId());
			writeC(fortress.getNextState());
		}
	}
}
