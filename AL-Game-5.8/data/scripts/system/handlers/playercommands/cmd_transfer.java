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
package playercommands;

import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Source
 */
public class cmd_transfer extends PlayerCommand {

	Player owner;

	public cmd_transfer() {
		super("transfer");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length != 1) {
			PacketSendUtility.sendMessage(player, ".transfer <player name>" + "\nTransfer your character to player account");
			return;
		}

		// new player owner
		owner = null;
		String accountOwnerName = Util.convertName(params[0]);

		// chk player name
		if (DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(accountOwnerName) == null) {
			PacketSendUtility.sendMessage(player, "Wrong player name.");
			return;
		}

		// get account id by name
		int accountId = DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(accountOwnerName);

		// chk same account transfer
		if (player.getPlayerAccount().getId() == accountId) {
			PacketSendUtility.sendMessage(player, "Your character already there.");
			return;
		}

		// chk players count on target account
		List<Integer> playersIds = DAOManager.getDAO(PlayerDAO.class).getPlayerOidsOnAccount(accountId);
		int count = playersIds.size();
		if (count > 7) {
			PacketSendUtility.sendMessage(player, "No free slot for player.");
			return;
		}

		// chk players count on target account
		for (int playerId : playersIds) {
			PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(playerId);

			if (pcd.getPlayer() != null) {
				owner = pcd.getPlayer();
			}

			if (!player.getRace().equals(pcd.getRace())) {
				PacketSendUtility.sendMessage(player, "You can't transfer to enemy faction account.");
				return;
			}
		}

		// chk toll count for transfer
		if (player.getPlayerAccount().getToll() < 2500) {
			PacketSendUtility.sendMessage(player, "You don't have enough toll.");
			return;
		}

		// set new account id to player
		player.getPlayerAccount().setId(accountId);

		// save new id in db & quit
		DAOManager.getDAO(PlayerDAO.class).changePlayerId(player, accountId);
		player.getClientConnection().close(new SM_QUIT_RESPONSE(), false);

		if (owner != null) {
			PacketSendUtility.sendMessage(owner, "Player: " + player.getName() + " was transferd to yours account." + "\nYou will be disconnected in 5 seconds");
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (owner.isOnline()) {
						owner.getClientConnection().close(new SM_QUIT_RESPONSE(), false);
					}
				}
			}, 5000);
		}
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
