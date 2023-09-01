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

import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.loginserver.LsClientPacket;
import com.aionemu.gameserver.services.transfers.PlayerTransferService;

/**
 * @author KID
 */
public class CM_PTRANSFER_RESPONSE extends LsClientPacket {

	public CM_PTRANSFER_RESPONSE(int opCode) {
		super(opCode);
	}

	@Override
	protected void readImpl() {
		int actionId = this.readD();
		switch (actionId) {
			case 20: // send info
			{
				int targetAccount = readD();
				int taskId = readD();
				String name = readS();
				String account = readS();
				int len = readD();
				byte[] db = this.readB(len);
				PlayerTransferService.getInstance().cloneCharacter(taskId, targetAccount, name, account, db);
			}
				break;
			case 21:// ok
			{
				int taskId = readD();
				PlayerTransferService.getInstance().onOk(taskId);
			}
				break;
			case 22:// error
			{
				int taskId = readD();
				String reason = readS();
				PlayerTransferService.getInstance().onError(taskId, reason);
			}
				break;
			case 23: {
				byte serverId = readSC();
				if (NetworkConfig.GAMESERVER_ID != serverId) {
					try {
						throw new Exception("Requesting player transfer for server id " + serverId + " but this is " + NetworkConfig.GAMESERVER_ID + " omgshit!");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					byte targetServerId = readSC();
					int account = readD();
					int targetAccount = readD();
					int playerId = readD();
					int taskId = readD();
					PlayerTransferService.getInstance().startTransfer(account, targetAccount, playerId, targetServerId, taskId);
				}
			}
				break;
		}
	}

	@Override
	protected void runImpl() {
	}
}
