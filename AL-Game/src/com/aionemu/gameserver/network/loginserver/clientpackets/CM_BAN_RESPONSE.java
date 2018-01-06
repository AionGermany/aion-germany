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
import com.aionemu.gameserver.network.loginserver.LsClientPacket;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author Watson
 */
public class CM_BAN_RESPONSE extends LsClientPacket {

	public CM_BAN_RESPONSE(int opCode) {
		super(opCode);
	}

	private byte type;
	private int accountId;
	private String ip;
	private int time;
	private int adminObjId;
	private boolean result;

	@Override
	public void readImpl() {
		this.type = (byte) readC();
		this.accountId = readD();
		this.ip = readS();
		this.time = readD();
		this.adminObjId = readD();
		this.result = readC() == 1;
	}

	@Override
	public void runImpl() {
		Player admin = World.getInstance().findPlayer(adminObjId);

		if (admin == null) {
			return;
		}

		// Some messages stuff
		String message;
		if (type == 1 || type == 3) {
			if (result) {
				if (time < 0) {
					message = "Account ID " + accountId + " was successfully unbanned";
				}
				else if (time == 0) {
					message = "Account ID " + accountId + " was successfully banned";
				}
				else {
					message = "Account ID " + accountId + " was successfully banned for " + time + " minutes";
				}
			}
			else {
				message = "Error occurred while banning player's account";
			}
			PacketSendUtility.sendMessage(admin, message);
		}
		if (type == 2 || type == 3) {
			if (result) {
				if (time < 0) {
					message = "IP mask " + ip + " was successfully removed from block list";
				}
				else if (time == 0) {
					message = "IP mask " + ip + " was successfully added to block list";
				}
				else {
					message = "IP mask " + ip + " was successfully added to block list for " + time + " minutes";
				}
			}
			else {
				message = "Error occurred while adding IP mask " + ip;
			}
			PacketSendUtility.sendMessage(admin, message);
		}
	}
}
