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
package com.aionemu.gameserver.network.loginserver.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.LsClientPacket;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.rates.Rates;
import com.aionemu.gameserver.world.World;

/**
 * @author Aionchs-Wylovech
 */
public class CM_LS_CONTROL_RESPONSE extends LsClientPacket {

	public CM_LS_CONTROL_RESPONSE(int opCode) {
		super(opCode);
	}

	private int type;
	private boolean result;
	private String playerName;
	private byte param;
	private String adminName;
	private int accountId;

	@Override
	public void readImpl() {
		type = readC();
		result = readC() == 1;
		adminName = readS();
		playerName = readS();
		param = (byte) readC();
		accountId = readD();
	}

	@Override
	public void runImpl() {
		World world = World.getInstance();
		Player admin = world.findPlayer(Util.convertName(adminName));
		Player player = world.findPlayer(Util.convertName(playerName));
		LoginServer.getInstance().accountUpdate(accountId, param, type);
		switch (type) {
			case 1:
				if (!result) {
					if (admin != null) {
						PacketSendUtility.sendMessage(admin, playerName + " has been promoted Administrator with role " + param);
					}
					if (player != null) {
						PacketSendUtility.sendMessage(player, "You have been promoted Administrator with role " + param + " by " + adminName);
					}
				}
				else {
					if (admin != null) {
						PacketSendUtility.sendMessage(admin, " Abnormal, the operation failed! ");
					}
				}
				break;
			case 2:
				if (!result) {
					if (admin != null) {
						PacketSendUtility.sendMessage(admin, playerName + " has been promoted membership with level " + param);
					}
					if (player != null) {
						player.setRates(Rates.getRatesFor(param));
						PacketSendUtility.sendMessage(player, "You have been promoted membership with level " + param + " by " + adminName);
					}
				}
				else {
					if (admin != null) {
						PacketSendUtility.sendMessage(admin, " Abnormal, the operation failed! ");
					}
				}
				break;
		}
	}
}
