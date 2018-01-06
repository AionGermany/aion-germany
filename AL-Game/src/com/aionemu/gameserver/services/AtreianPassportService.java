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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerPassportsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.event.AtreianPassport;

/**
 * @author Alcapwnd
 * @reworked Lyras
 */
public class AtreianPassportService {

	private static final Logger log = LoggerFactory.getLogger(AtreianPassportService.class);
	private Map<Integer, AtreianPassport> basic = new HashMap<Integer, AtreianPassport>(1);
	private Map<Integer, AtreianPassport> anny = new HashMap<Integer, AtreianPassport>(1);
	public Map<Integer, AtreianPassport> data = new HashMap<Integer, AtreianPassport>(1);

	public Map<Integer, AtreianPassport> getCurrentCumuPassports() {
		Map<Integer, AtreianPassport> passports = new HashMap<Integer, AtreianPassport>();
		return passports;
	}

	public int getMonthsSinceAscension(Player player) {
		Timestamp firstDate = null;
		Iterator<PlayerAccountData> it = player.getPlayerAccount().iterator();
		while (it.hasNext()) {
			PlayerAccountData data = it.next();
			if (firstDate == null) {
				firstDate = data.getCreationDate();
			}
			else {
				if (data.getCreationDate().before(firstDate)) {
					firstDate = data.getCreationDate();
				}
			}
		}
		if (firstDate != null) {
			Calendar first = Calendar.getInstance();
			Calendar now = Calendar.getInstance();
			first.setTimeInMillis(firstDate.getTime());
			now.setTimeInMillis(System.currentTimeMillis());

			int diffYear = now.get(Calendar.YEAR) - first.get(Calendar.YEAR);

			return diffYear * 12 + now.get(Calendar.MONTH) - first.get(Calendar.MONTH);
		}
		else {
			log.error("FIRST DATE == NULL");
			return 0;
		}
	}

	public Map<Integer, AtreianPassport> getPlayerPassports(int accountId) {
		Map<Integer, AtreianPassport> passports = new HashMap<Integer, AtreianPassport>();
		List<Integer> ids = DAOManager.getDAO(PlayerPassportsDAO.class).getPassports(accountId);
		for (Integer i : ids) {
			passports.put(i, data.get(i));
		}
		return passports;
	}

	public void onLogin(Player player) {
		if (player == null)
			return;
		boolean newPassport = false;
		int accountId = player.getPlayerAccount().getId();
		PlayerPassportsDAO dao = DAOManager.getDAO(PlayerPassportsDAO.class);
		Map<Integer, AtreianPassport> currentCumuPassports = getCurrentCumuPassports();
		Map<Integer, AtreianPassport> playerPassports = getPlayerPassports(accountId);

		// ANNIVERSARY
		int annyMonths = getMonthsSinceAscension(player);
		for (int i = 14; i < annyMonths + 14; i++) {
			if (!playerPassports.containsKey(i)) {
				//dao.insertPassport(accountId, anny.get(i).getId(), anny.get(i).getAttendNum(), new Timestamp(System.currentTimeMillis()));
				newPassport = true;
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
	public void onGetReward(Player player, int timestamp, List<Integer> passportId) {
		int accountId = player.getPlayerAccount().getId();
		Map<Integer, AtreianPassport> playerPassports = getPlayerPassports(accountId);
		for (Integer i : passportId) {
			//if (playerPassports.containsKey(i) && DAOManager.getDAO(PlayerPassportsDAO.class).getStamps(accountId, playerPassports.get(i).getId()) == playerPassports.get(i).getAttendNum() && !DAOManager.getDAO(PlayerPassportsDAO.class).isRewarded(accountId, playerPassports.get(i).getId())) {
			//	ItemService.addItem(player, playerPassports.get(i).getRewardItem(), playerPassports.get(i).getRewardItemNum(), new ItemUpdatePredicate(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_PASSPORT_ADD));
			//	DAOManager.getDAO(PlayerPassportsDAO.class).updatePassport(accountId, playerPassports.get(i).getId(), playerPassports.get(i).getStamps(), true);
			//}
		}
		onLogin(player);
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
			}
		}
		log.info("[AtreianPassportService] Loaded " + basic.size() + " basic passports");
		log.info("[AtreianPassportService] Loaded " + anny.size() + " anniversary passports");
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
