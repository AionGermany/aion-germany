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
package admincommands;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.controllers.PlayerController;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

public class Repair extends AdminCommand {

	public Repair() {
		super("repair");
	}

	@Override
	public void execute(Player player, String... params) {
		String arg = params[0];
		WorldPosition position;
		PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(arg);
		int accountId = DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(arg);

		if (accountId != player.getPlayerAccount().getId()) {
			PacketSendUtility.sendMessage(player, "You cannot Repair that player");
			return;
		}

		if (player.getName().toLowerCase().equalsIgnoreCase(pcd.getName().toLowerCase())) {
			PacketSendUtility.sendMessage(player, "You cannot Repair yourself");
			return;
		}

		switch (pcd.getRace()) {
			case ELYOS:
				position = World.getInstance().createPosition(210010000, 1212.9423f, 1044.8516f, 140.75568f, (byte) 32, 0);
				pcd.setPosition(position);
				break;
			case ASMODIANS:
				position = World.getInstance().createPosition(220010000, 571.0388f, 2787.3420f, 299.8750f, (byte) 32, 0);
				pcd.setPosition(position);
				break;
			default:
				break;
		}

		Player repairPlayer = new Player(new PlayerController(), pcd, null, player.getPlayerAccount());
		DAOManager.getDAO(PlayerDAO.class).storePlayer(repairPlayer);
		PacketSendUtility.sendMessage(player, "Player " + pcd.getName() + " was repaired");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //repair <player name>");
	}
}
