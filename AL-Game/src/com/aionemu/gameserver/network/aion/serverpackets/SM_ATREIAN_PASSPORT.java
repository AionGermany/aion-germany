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

import com.aionemu.gameserver.model.templates.event.AtreianPassport;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Alcapwnd
 */
public class SM_ATREIAN_PASSPORT extends AionServerPacket {

	private int month;
	private Map<Integer, AtreianPassport> passports;
	private int year;

	public SM_ATREIAN_PASSPORT(Map<Integer, AtreianPassport> passports, int month, int year) {
		this.passports = passports;
		this.month = month;
		this.year = year;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(year);
		writeH(month);
		writeH(11);// unk //can be variable
		writeH(this.passports.size());
		for (AtreianPassport atp : passports.values()) {
			writeD(atp.getId());
			//writeD(atp.getStamps());
			//if (atp.getStamps() == atp.getRewardItemNum() && !DAOManager.getDAO(PlayerPassportsDAO.class).isRewarded(con.getAccount().getId(), atp.getId()))
			//	writeH(1);
			//else
				writeH(0);
			writeH(0); // unk
			//Long time = atp.getLastStamp().getTime() / 1000;
			//writeD(time.intValue());
		}
	}
}
