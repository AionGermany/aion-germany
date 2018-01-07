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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Pan
 */
public class Enemy extends AdminCommand {

	public Enemy() {
		super("enemy");
	}

	@Override
	public void execute(Player player, String... params) {
		String help = "Syntax: //enemy < players | npcs | all | cancel >\n" + "Players - You're enemy to Players of both factions.\n" + "Npcs - You're enemy to all Npcs and Monsters.\n" + "All - You're enemy to Players of both factions and all Npcs.\n" + "Cancel - Cancel all. Players and Npcs have default enmity to you.";

		if (params.length != 1) {
			onFail(player, null);
			return;
		}

		String output = "You now appear as enemy to " + params[0] + ".";

		int neutralType = player.getAdminNeutral();

		if (params[0].equals("all")) {
			player.setAdminEnmity(3);
			player.setAdminNeutral(0);
		}
		else if (params[0].equals("players")) {
			player.setAdminEnmity(2);
			if (neutralType > 1) {
				player.setAdminNeutral(0);
			}
		}
		else if (params[0].equals("npcs")) {
			player.setAdminEnmity(1);
			if (neutralType == 1 || neutralType == 3) {
				player.setAdminNeutral(0);
			}
		}
		else if (params[0].equals("cancel")) {
			player.setAdminEnmity(0);
			output = "You appear regular to both Players and Npcs.";
		}
		else if (params[0].equals("help")) {
			PacketSendUtility.sendMessage(player, help);
			return;
		}
		else {
			onFail(player, null);
			return;
		}

		PacketSendUtility.sendMessage(player, output);

		player.clearKnownlist();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		player.updateKnownlist();
	}

	@Override
	public void onFail(Player player, String message) {
		String syntax = "Syntax: //enemy < players | npcs | all | cancel >\nIf you're unsure about what you want to do, type //enemy help";
		PacketSendUtility.sendMessage(player, syntax);
	}
}
