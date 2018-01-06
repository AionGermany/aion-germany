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
package com.aionemu.gameserver.network.aion.clientpackets;

import java.sql.Timestamp;

import org.joda.time.DateTime;

import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.MaintenanceTask;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_PAY_RENT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas
 */
public class CM_HOUSE_PAY_RENT extends AionClientPacket {

	int weekCount;

	public CM_HOUSE_PAY_RENT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		weekCount = readC();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		if (!HousingConfig.ENABLE_HOUSE_PAY) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_F2P_CASH_HOUSE_FEE_FREE);
			return;
		}

		House house = player.getActiveHouse();
		long toPay = house.getLand().getMaintenanceFee() * weekCount;
		if (toPay <= 0) {
			return;
		}
		if (player.getInventory().getKinah() < toPay) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_NOT_ENOUGH_MONEY);
			return;
		}

		long payTime = house.getNextPay() != null ? house.getNextPay().getTime() : (long) MaintenanceTask.getInstance().getRunTime() * 1000;
		int counter = weekCount;
		while ((--counter) >= 0) {
			payTime += MaintenanceTask.getInstance().getPeriod();
		}

		DateTime nextRun = new DateTime((long) MaintenanceTask.getInstance().getRunTime() * 1000);
		if (nextRun.plusWeeks(4).isBefore(payTime)) { // client cap
			return;
		}

		player.getInventory().decreaseKinah(toPay);
		house.setNextPay(new Timestamp(payTime));
		house.setFeePaid(true);
		house.save();
		PacketSendUtility.sendPacket(player, new SM_HOUSE_PAY_RENT(weekCount));
	}
}
