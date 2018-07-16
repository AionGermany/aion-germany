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
package com.aionemu.gameserver.services.events;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerPassportsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.event.AtreianPassport;
import com.aionemu.gameserver.model.templates.event.AtreianPassportRewards;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATREIAN_PASSPORT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Alcapwnd
 * @reworked Lyras
 */
public class AtreianPassportService {

	private static final Logger log = LoggerFactory.getLogger(AtreianPassportService.class);
	private Map<Integer, AtreianPassport> basic = new HashMap<Integer, AtreianPassport>(1);
	private Map<Integer, AtreianPassport> anny = new HashMap<Integer, AtreianPassport>(1);
	public Map<Integer, AtreianPassport> data = new HashMap<Integer, AtreianPassport>(1);

	public Map<Integer, AtreianPassport> getPlayerPassports(int accountId) {
		Map<Integer, AtreianPassport> passports = new HashMap<Integer, AtreianPassport>();
		List<Integer> ids = DAOManager.getDAO(PlayerPassportsDAO.class).getPassports(accountId);
		for (Integer i : ids) {
			passports.put(i, data.get(i));
		}
		return passports;
	}

	public void onLogin(Player player) {
		if (player == null) {
			return;
		}
		int atreianId = 8;
		int accountId = player.getPlayerAccount().getId();
		PlayerPassportsDAO dao = DAOManager.getDAO(PlayerPassportsDAO.class);
		Map<Integer, AtreianPassport> playerPassports = getPlayerPassports(accountId);
		
		//Added reset if all Stamps are received
		if (dao.getStamps(accountId, atreianId) == 28) {
			dao.updatePassport(accountId, atreianId, 0, true,  new Timestamp(System.currentTimeMillis() - 86400000L));
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(player.getCreationDate());
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		if (!playerPassports.containsKey(atreianId)) {
			final Timestamp now = new Timestamp(System.currentTimeMillis() - 86400000L);
			dao.insertPassport(accountId, atreianId, 0, now);
			PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(atreianId, 0, 1, false, month + 1, year)); //NEW
		}
		else {
			int stamps = dao.getStamps(accountId, atreianId);
			Timestamp now2 = new Timestamp(System.currentTimeMillis());
			Timestamp lastStamp = dao.getLastStamp(accountId, atreianId);
			if (now2.getTime() - lastStamp.getTime() >= 86400000L) {
				DAOManager.getDAO(PlayerPassportsDAO.class).updatePassport(accountId, atreianId, stamps, false, lastStamp);
				PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(atreianId, stamps, 1, false, month + 1, year));
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NEW_PASSPORT_AVAIBLE);
			}
			else {
				PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(atreianId, stamps, 1, true, month + 1, year));
			}
		}
	}

	public void onStart() {
		Map<Integer, AtreianPassport> raw = DataManager.ATREIAN_PASSPORT_DATA.getAll();
		if (raw.size() != 0) {
			getPassports(raw);
		}
		else {
			log.warn("[AtreianPassportService] passports from static data = 0");
		}
		log.info("[AtreianPassportService] AtreianPassportService initialized");
	}

	/**
	 * @param count
	 * @param timestamp
	 */
	public void getReward(Player player, int atreianId) {
		AtreianPassport loginRewardTemplate = DataManager.ATREIAN_PASSPORT_DATA.getAtreianPassportId(atreianId);
		int accountId = player.getPlayerAccount().getId();
		PlayerPassportsDAO dao = DAOManager.getDAO(PlayerPassportsDAO.class);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(player.getCreationDate());
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int stamps = dao.getStamps(accountId, atreianId);
		for (AtreianPassportRewards component : loginRewardTemplate.getAtreianPassportRewards()) {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			Timestamp lastStamp = dao.getLastStamp(accountId, atreianId);
			if (now.getTime() - lastStamp.getTime() >= 86400000L) {
				if (component.getRewardItemNum() == stamps + 1) {
					ItemService.addItem(player, component.getRewardItem(), component.getRewardItemCount());
//					PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(atreianId, stamps + 1, (int) now.getTime(), true, month + 1, year)); //OLD
					PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(atreianId, stamps + 1, 1, true, month + 1, year)); //NEW
					DAOManager.getDAO(PlayerPassportsDAO.class).updatePassport(accountId, atreianId, stamps + 1, true, now);
				}
			}
		}
	}
			

	public void getPassports(Map<Integer, AtreianPassport> raw) {
		data.putAll(raw);
		for (AtreianPassport atp : data.values()) {
			switch (atp.getAttendType()) {
				case BASIC:
					getBasicPassports(atp.getId(), atp);
					break;
				case ANNIVERSARY:
					getAnniversaryPassports(atp.getId(), atp);
					break;
				default:
					break;
			}
		}
		log.info("[AtreianPassportService] Loaded " + basic.size() + " basic passports");
		log.info("[AtreianPassportService] Loaded " + anny.size() + " anniversary passports");
	}

	public void getPassports(int id, AtreianPassport atp) {
		if (data.containsValue(id))
			return;
		data.put(id, atp);
	}

	public void getBasicPassports(int id, AtreianPassport atp) {
		if (basic.containsValue(id))
			return;
		basic.put(id, atp);
	}

	public void getAnniversaryPassports(int id, AtreianPassport atp) {
		if (anny.containsValue(id))
			return;
		anny.put(id, atp);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final AtreianPassportService instance = new AtreianPassportService();
	}

	public static AtreianPassportService getInstance() {
		return SingletonHolder.instance;
	}
}
