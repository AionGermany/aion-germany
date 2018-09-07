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
import com.aionemu.gameserver.services.player.PlayerSecurityTokenService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author xXMashUpXx
 */
public class SToken extends AdminCommand {

	public SToken() {
		super("stoken");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length < 1) {
			PacketSendUtility.sendMessage(player, "Syntax: //stoken <playername> || //stoken show <playername>");
			return;
		}
		Player receiver = null;

		if (params[0].equals("show")) {
			receiver = World.getInstance().findPlayer(Util.convertName(params[1]));
			if (receiver == null) {
				PacketSendUtility.sendMessage(player, "Can't find this player, maybe he's not online");
				return;
			}

			if (!"".equals(receiver.getPlayerAccount().getSecurityToken())) {
				PacketSendUtility.sendMessage(player, "The Security Token of this player is: " + receiver.getPlayerAccount().getSecurityToken());
			}
			else {
				PacketSendUtility.sendMessage(player, "This player haven't an Security Token!");
			}

		}
		else {
			receiver = World.getInstance().findPlayer(Util.convertName(params[0]));

			if (receiver == null) {
				PacketSendUtility.sendMessage(player, "Can't find this player, maybe he's not online");
				return;
			}

			PlayerSecurityTokenService.getInstance().generateToken(receiver);
		}
	}

	@Override
	public void onFail(Player admin, String message) {
		PacketSendUtility.sendMessage(admin, "Syntax: //stoken <playername> || //stoken show <playername>");
	}
}
