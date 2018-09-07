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
 * Admin revoke command.
 *
 * @author Cyrakuse
 * @modified By Aionchs-Wylovech
 */
public class Revoke extends AdminCommand {

	public Revoke() {
		super("revoke");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length != 2) {
			PacketSendUtility.sendMessage(admin, "syntax //revoke <characterName> <acceslevel | membership>");
			return;
		}

		int type = 0;
		if (params[1].toLowerCase().equals("accesslevel")) {
			type = 1;
		}
		else if (params[1].toLowerCase().equals("membership")) {
			type = 2;
		}
		else {
			PacketSendUtility.sendMessage(admin, "syntax //revoke <characterName> <acceslevel | membership>");
			return;
		}

		Player player = World.getInstance().findPlayer(Util.convertName(params[0]));
		if (player == null) {
			PacketSendUtility.sendMessage(admin, "The specified player is not online.");
			return;
		}
		LoginServer.getInstance().sendLsControlPacket(player.getAcountName(), player.getName(), admin.getName(), 0, type);
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //revoke <characterName> <acceslevel | membership>");
	}
}
