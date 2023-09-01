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

import java.util.Iterator;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.MailDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.CharacterBanInfo;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.PlayerInfo;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.player.PlayerService;

/**
 * In this packet Server is sending Character List to client.
 *
 * @author Nemesiss, AEJTester
 * @author GiGatR00n
 */
public class SM_CHARACTER_LIST extends PlayerInfo {

	/**
	 * List Unk 0 and 2 AccountId
	 */
	private final int list;
	private final int accountId;

	/**
	 * Constructs new <tt>SM_CHARACTER_LIST </tt> packet
	 */
	public SM_CHARACTER_LIST(int list, int accountId) {
		this.list = list; // 0 = Empty List, 2 = SendChars
		this.accountId = accountId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeC(list);
		if (list == 0) {
			writeD(accountId);
			writeC(0x00);
		}
		else if (list == 2) {
			writeD(accountId);

			Account account = con.getAccount();

			/* Checks for Deleted Characters for each client request */
			removeDeletedCharacters(account);

			writeC(account.size()); // characters count

			for (PlayerAccountData playerData : account.getSortedAccountsList()) {
				PlayerCommonData pcd = playerData.getPlayerCommonData();
				CharacterBanInfo cbi = playerData.getCharBanInfo();
				Player player = PlayerService.getPlayer(pcd.getPlayerObjId(), account);

				writePlayerInfo(playerData);
				writeB(new byte[40]);

				if (cbi != null && cbi.getEnd() > System.currentTimeMillis() / 1000) {
					// client wants int so let's hope we do not reach long limit with timestamp while this server is used :P
					writeD((int) cbi.getStart()); // startPunishDate
					writeD((int) cbi.getEnd()); // endPunishDate
					writeS(cbi.getReason());
					writeB(new byte[200]); // unk 4.5.0.18
				}
				else {
					writeB(new byte[52]); // unk 4.9
					writeD(playerData.getDeletionTimeInSeconds()); // v4.9
					writeH(player.getPlayerSettings().getDisplay());// display helmet 0 show, 5 dont show
					writeH(0); // unk
					writeD(DAOManager.getDAO(MailDAO.class).mailCount(pcd.getPlayerObjId())); // All Mail Count
					writeD(DAOManager.getDAO(MailDAO.class).unreadedMails(pcd.getPlayerObjId())); // Unread Mail Count
					writeD(0); // unk
					writeD(0); // unk
					writeD(0); // unk
					writeD(BrokerService.getInstance().getCollectedMoney(pcd)); // collected money from broker
					writeB(new byte[154]); // unk 5.0
				}
			}
		}
	}

	/**
	 * @param account
	 */
	public void removeDeletedCharacters(Account account) {
		/* Removes chars that should be removed */
		Iterator<PlayerAccountData> it = account.iterator();
		while (it.hasNext()) {
			PlayerAccountData pad = it.next();
			Race race = pad.getPlayerCommonData().getRace();
			long deletionTime = (long) pad.getDeletionTimeInSeconds() * (long) 1000;
			if (deletionTime != 0 && deletionTime <= System.currentTimeMillis()) {
				it.remove();
				account.decrementCountOf(race);
				PlayerService.deletePlayerFromDB(pad.getPlayerCommonData().getPlayerObjId());
			}
		}
		if (account.isEmpty()) {
			removeAccountWH(account.getId());
			account.getAccountWarehouse().clear();
		}
	}

	private static void removeAccountWH(int accountId) {
		DAOManager.getDAO(InventoryDAO.class).deleteAccountWH(accountId);
	}
}
