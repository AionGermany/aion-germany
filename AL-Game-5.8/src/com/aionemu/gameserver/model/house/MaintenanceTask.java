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
package com.aionemu.gameserver.model.house;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_ACQUIRE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_OWNER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.HousingBidService;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.mail.MailFormatter;
import com.aionemu.gameserver.taskmanager.AbstractCronTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

import javolution.util.FastList;

/**
 * @author Rolandas
 */
public class MaintenanceTask extends AbstractCronTask {

	private static final Logger log = LoggerFactory.getLogger(MaintenanceTask.class);
	private static final FastList<House> maintainedHouses;
	private static MaintenanceTask instance;

	static {
		maintainedHouses = FastList.newInstance();
		try {
			instance = new MaintenanceTask(HousingConfig.HOUSE_MAINTENANCE_TIME);
		}
		catch (ParseException pe) {
		}
	}

	public static final MaintenanceTask getInstance() {
		return instance;
	}

	private MaintenanceTask(String maintainTime) throws ParseException {
		super(maintainTime);
	}

	@Override
	protected long getRunDelay() {
		int left = (int) (getRunTime() - System.currentTimeMillis() / 1000);
		if (left < 0) {
			return 0;
		}
		return left * 1000;
	}

	@Override
	protected String getServerTimeVariable() {
		return "houseMaintainTime";
	}

	@Override
	protected boolean canRunOnInit() {
		return false;
	}

	public boolean isMaintainTime() {
		return (getRunTime() - System.currentTimeMillis() / 1000) <= 0;
	}

	@Override
	protected void preInit() {
		GameServer.log.info("[HouseService] Initializing House maintenance task...");
	}

	@Override
	protected void preRun() {
		updateMaintainedHouses();
		GameServer.log.info("[HouseService] Executing House maintenance. Maintained Houses: " + maintainedHouses.size());
	}

	private void updateMaintainedHouses() {
		maintainedHouses.clear();

		if (!HousingConfig.ENABLE_HOUSE_PAY) {
			return;
		}

		Date now = new Date();
		FastList<House> houses = HousingService.getInstance().getCustomHouses();
		for (House house : houses) {
			if (house.getStatus() == HouseStatus.INACTIVE) {
				continue;
			}
			if (house.getOwnerId() == 0) {
				continue;
			}
			if (house.isFeePaid()) {
				if (house.getNextPay() == null || house.getNextPay().before(now)) {
					house.setFeePaid(false);
					// if never paid, just set time to the next period
					if (house.getNextPay() == null) {
						house.setNextPay(new Timestamp((long) getRunTime() * 1000));
					}
					house.save();
				}
				else {
					continue;
				}
			}
			maintainedHouses.add(house);
		}
	}

	@Override
	protected void executeTask() {
		if (!HousingConfig.ENABLE_HOUSE_PAY) {
			return;
		}

		// Get times based on configuration values
		DateTime now = new DateTime();
		DateTime previousRun = now.minus(getPeriod()); // usually week ago
		DateTime beforePreviousRun = previousRun.minus(getPeriod()); // usually two weeks ago

		for (House house : maintainedHouses) {
			if (house.isFeePaid()) {
				continue; // player already paid, don't check
			}
			long payTime = house.getNextPay().getTime();
			long impoundTime = 0;
			int warnCount = 0;

			PlayerCommonData pcd = null;
			Player player = World.getInstance().findPlayer(house.getOwnerId());
			if (player == null) {
				pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(house.getOwnerId());
			}
			else {
				pcd = player.getCommonData();
			}

			if (pcd == null) {
				// player doesn't exist already for some reasons
				log.warn("[HouseService] House " + house.getAddress().getId() + " had player assigned but no player exists. Auctioned.");
				putHouseToAuction(house, null);
				continue;
			}

			if (payTime <= beforePreviousRun.getMillis()) {
				DateTime plusDay = beforePreviousRun.minusDays(1);
				if (payTime <= plusDay.getMillis()) {
					// player didn't pay after the second warning and one day passed
					impoundTime = now.getMillis();
					warnCount = 3;
					putHouseToAuction(house, pcd);
				}
				else {
					impoundTime = now.plusDays(1).getMillis();
					warnCount = 2;
				}
			}
			else if (payTime <= previousRun.getMillis()) {
				// player did't pay 1 period
				impoundTime = now.plus(getPeriod()).plusDays(1).getMillis();
				warnCount = 1;
			}
			else {
				continue; // should not happen
			}

			if (pcd.isOnline()) {
				if (warnCount == 3) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_SEQUESTRATE);
				}
				else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_OVERDUE);
				}
			}
			MailFormatter.sendHouseMaintenanceMail(house, warnCount, impoundTime);
		}
	}

	private void putHouseToAuction(House house, PlayerCommonData playerCommonData) {
		house.revokeOwner();
		HousingBidService.getInstance().addHouseToAuction(house);
		house.save();
		log.info("[HouseService] House " + house.getAddress().getId() + " overdued and put to auction.");
		if (playerCommonData == null) {
			return;
		}
		if (playerCommonData.isOnline()) {
			Player player = playerCommonData.getPlayer();
			player.getHouses().remove(house);
			player.setHouseRegistry(null);
			// TODO: check this
			PacketSendUtility.sendPacket(player, new SM_HOUSE_ACQUIRE(player.getObjectId(), house.getAddress().getId(), false));
			PacketSendUtility.sendPacket(player, new SM_HOUSE_OWNER_INFO(player, null));
		}
	}
}
