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
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * Admin promote command.
 *
 * @author Cyrakuse
 * @modified By Aionchs-Wylovech
 */
public class Promote extends AdminCommand {

	public Promote() {
		super("promote");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length != 3) {
			PacketSendUtility.sendMessage(admin, "syntax //promote <characterName> <accesslevel | membership> <mask> ");
			return;
		}

		int mask = 0;
		try {
			mask = Integer.parseInt(params[2]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "Only number!");
			return;
		}

		int type = 0;
		if (params[1].toLowerCase().equals("accesslevel")) {
			type = 1;
			if (mask > 10 || mask < 0) {
				PacketSendUtility.sendMessage(admin, "accesslevel can be 0 - 10");
				return;
			}
		}
		else if (params[1].toLowerCase().equals("membership")) {
			type = 2;
			if (mask > 3 || mask < 0) {
				PacketSendUtility.sendMessage(admin, "membership can be 0 - 3");
				return;
			}
		}
		else {
			PacketSendUtility.sendMessage(admin, "syntax //promote <characterName> <accesslevel | membership> <mask>");
			return;
		}

		Player player = World.getInstance().findPlayer(Util.convertName(params[0]));
		if (player == null) {
			PacketSendUtility.sendMessage(admin, "The specified player is not online.");
			return;
		}
		LoginServer.getInstance().sendLsControlPacket(player.getAcountName(), player.getName(), admin.getName(), mask, type);

	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //promote <characterName> <accesslevel | membership> <mask> ");
	}
}
