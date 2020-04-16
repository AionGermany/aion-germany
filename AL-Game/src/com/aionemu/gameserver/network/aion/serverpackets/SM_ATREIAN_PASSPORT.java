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

import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.model.templates.atreianpassport.AtreianPassportTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 */
public class SM_ATREIAN_PASSPORT extends AionServerPacket {

	private final Map<Integer, AtreianPassportTemplate> rewards;
	private final int year;
	private final int month;
	private final int day;

	public SM_ATREIAN_PASSPORT(Map<Integer, AtreianPassportTemplate> rewards, int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.rewards = rewards;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(year); // Year?
		writeH(month); // Month?
		writeH(day); // Day?
		writeH(EventsConfig.ENABLE_ATREIAN_PASSPORT); // 1=on, 0=off
		for (AtreianPassportTemplate rewards : rewards.values()) {
			writeD(EventsConfig.ATREIAN_PASSPORT_ID); // Id
			writeD(0);
			writeD(0);
			writeC(0);
		}
	}
}
