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
import com.aionemu.gameserver.network.NetworkBannedManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Alex
 */
public class UnBanNet extends AdminCommand {

	public UnBanNet() {
		super("unbannet");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 1) {
			onFail(player, null);
			return;
		}

		String ip = params[0];
		boolean result = NetworkBannedManager.getInstance().unbanAddress(ip, "uban;net=" + ip + ", " + player.getObjectId() + "; admin=" + player.getName());
		if (result) {
			PacketSendUtility.sendMessage(player, "network address " + ip + " has unbanned");
		}
		else {
			PacketSendUtility.sendMessage(player, "network address " + ip + " is not banned");
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: //unbannet <network address>");
	}
}
