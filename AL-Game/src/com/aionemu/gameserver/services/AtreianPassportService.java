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
package com.aionemu.gameserver.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.atreianpassport.AtreianPassportTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATREIAN_PASSPORT;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34
 */
public class AtreianPassportService {

	private static final Logger log = LoggerFactory.getLogger(AtreianPassportService.class);
	private final Map<Integer, AtreianPassportTemplate> basic = new HashMap<>(1);
	public Map<Integer, AtreianPassportTemplate> data = new HashMap<>(1);
	private int year;
	private int month;
	private int day;

	public void onLogin(Player player) {
		if (player == null) {
			return;
		}
		PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(basic, year, month, day));
	}

	public void onStart() {
		final Map<Integer, AtreianPassportTemplate> raw = DataManager.ATREIAN_PASSPORT_DATA.getAll();
		if (raw.size() != 0) {
			getPassports(raw);
		} else {
			log.warn("[Atreian Passport] passports from static data = 0");
		}
		log.info("[Atreian Passport] initialized");
	}

	public void getPassports(Map<Integer, AtreianPassportTemplate> raw) {
		data.putAll(raw);
		for (AtreianPassportTemplate atp : data.values()) {
			switch (atp.getAttendType()) {
				case PC_BASIC: {
					getBasicPassports(atp.getId(), atp);
					break;
				}
			}
		}
	}

	public void getBasicPassports(int id, AtreianPassportTemplate atp) {
		if (basic.containsKey(id)) {
			return;
		}
		basic.put(id, atp);
	}

	public void onGetReward(Player player, List<Integer> passportId) {
		onLogin(player);
	}	

	public static AtreianPassportService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		protected static final AtreianPassportService instance = new AtreianPassportService();
	}
}
