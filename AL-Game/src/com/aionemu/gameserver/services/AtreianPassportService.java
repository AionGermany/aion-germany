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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.dao.AtreianPassportDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.atreianpassport.AtreianPassportRewards;
import com.aionemu.gameserver.model.templates.atreianpassport.AtreianPassportTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATREIAN_PASSPORT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34
 */
public class AtreianPassportService {

	private static final Logger log = LoggerFactory.getLogger(AtreianPassportService.class);
	private final Map<Integer, AtreianPassportTemplate> pc_basic = new HashMap<>(1);
	public Map<Integer, AtreianPassportTemplate> data = new HashMap<>(1);

	public Map<Integer, AtreianPassportTemplate> getPlayerPassports(int accountId) {
		Map<Integer, AtreianPassportTemplate> passports = new HashMap<Integer, AtreianPassportTemplate>();
        List<Integer> ids = DAOManager.getDAO(AtreianPassportDAO.class).getPassports(accountId);
        for (Integer i : ids) {
            passports.put(i, data.get(i));
        }
		return passports;
	}

	public void onLogin(Player player) {
		if (player == null) {
			return;
		}
		int passportId = EventsConfig.ATREIAN_PASSPORT_ID;
		int accountId = player.getPlayerAccount().getId();
		AtreianPassportDAO dao = DAOManager.getDAO(AtreianPassportDAO.class);
		Map<Integer, AtreianPassportTemplate> playerPassports = getPlayerPassports(accountId);

		// Added reset if all Stamps are received
		if (dao.getStamps(accountId, passportId) == 7) {
			dao.updatePassport(accountId, passportId, 0, true, new Timestamp(System.currentTimeMillis() - 86400000L));
		}

		if (!playerPassports.containsKey(passportId)) {
			final Timestamp now = new Timestamp(System.currentTimeMillis() - 86400000L);
			dao.insertPassport(accountId, passportId, 0, now);
			PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(passportId, 0, 1, false));
		} else {
			int stamps = dao.getStamps(accountId, passportId);
			Timestamp now2 = new Timestamp(System.currentTimeMillis());
			Timestamp lastStamp = dao.getLastStamp(accountId, passportId);
			if (now2.getTime() - lastStamp.getTime() >= 86400000L) {
				DAOManager.getDAO(AtreianPassportDAO.class).updatePassport(accountId, passportId, stamps, false, lastStamp);
				PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(passportId, 0, 1, false));
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NEW_PASSPORT_AVAIBLE);
			} else {
				PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(passportId, 0, 1, true));
			}
		}
	}

	public void onStart() {
		Map<Integer, AtreianPassportTemplate> raw = DataManager.ATREIAN_PASSPORT_DATA.getAll();
		if (raw.size() != 0) {
			getPassports(raw);
		} else {
			log.warn("[AtreianPassportService] Passports from static data = 0");
		}
		log.info("[AtreianPassportService] is initialized...");
	}

	public void getReward(Player player, int passportId) {
		AtreianPassportTemplate atreianPassportRewards = DataManager.ATREIAN_PASSPORT_DATA.getAtreianPassportId(passportId);
		int accountId = player.getPlayerAccount().getId();
		AtreianPassportDAO dao = DAOManager.getDAO(AtreianPassportDAO.class);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(player.getCreationDate());
		int stamps = dao.getStamps(accountId, passportId);
		for (AtreianPassportRewards component : atreianPassportRewards.getRewards()) {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			Timestamp lastStamp = dao.getLastStamp(accountId, passportId);
			if (now.getTime() - lastStamp.getTime() >= 86400000L) {
				if (component.getRewardItemNum() == stamps + 1) {
					ItemService.addItem(player, component.getRewardItemId(), component.getRewardItemCount());
					PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(passportId, stamps + 1, 1, true));
					DAOManager.getDAO(AtreianPassportDAO.class).updatePassport(accountId, passportId, stamps + 1, true, now);
				}
			}
		}
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
		log.info("[AtreianPassportService] Loaded " + pc_basic.size() + " Basic Passports");
	}

	public void getBasicPassports(int id, AtreianPassportTemplate atp) {
		if (pc_basic.containsKey(id)) {
			return;
		}
		pc_basic.put(id, atp);
	}

	public static AtreianPassportService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		protected static final AtreianPassportService instance = new AtreianPassportService();
	}
}
