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
import com.aionemu.gameserver.network.BannedHDDManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Alex
 */
public class UnBanHdd extends AdminCommand {

	public UnBanHdd() {
		super("unbanhdd");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 1) {
			onFail(player, null);
			return;
		}

		String hdd_serial = params[0];
		boolean result = BannedHDDManager.getInstance().unbanAddress(hdd_serial, "uban;hdd=" + hdd_serial + ", " + player.getObjectId() + "; admin=" + player.getName());
		if (result) {
			PacketSendUtility.sendMessage(player, "hdd serial " + hdd_serial + " has unbanned");
		}
		else {
			PacketSendUtility.sendMessage(player, "hdd serial " + hdd_serial + " is not banned");
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: //unbanhdd <hdd serial>");
	}
}
