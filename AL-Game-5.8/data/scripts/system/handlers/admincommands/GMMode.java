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
import com.aionemu.gameserver.utils.audit.GMService;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Eloann
 */
public class GMMode extends AdminCommand {

	public GMMode() {
		super("gm");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (admin.getAccessLevel() < 1) {
			PacketSendUtility.sendMessage(admin, "You cannot use this command.");
			return;
		}

		if (params.length != 1) {
			onFail(admin, null);
			return;
		}

		if (params[0].toLowerCase().equals("on")) {
			if (!admin.isGmMode()) {
				admin.setGmMode(true);
				admin.setWispable();

				GMService.getInstance().onPlayerLogin(admin); // put gm into
				// gmlist
				GMService.getInstance().onPlayerAvailable(admin); // send
				// available
				// message
				admin.clearKnownlist();
				PacketSendUtility.sendPacket(admin, new SM_PLAYER_INFO(admin, false));
				PacketSendUtility.sendPacket(admin, new SM_MOTION(admin.getObjectId(), admin.getMotions().getActiveMotions()));
				admin.updateKnownlist();
				PacketSendUtility.sendMessage(admin, "you are now Available and Wispable by players");

			}
		}
		if (params[0].equals("off")) {
			if (admin.isGmMode()) {
				admin.setGmMode(false);
				admin.setUnWispable();

				GMService.getInstance().onPlayerLogedOut(admin); // remove gm
				// into
				// gmlist
				GMService.getInstance().onPlayerUnavailable(admin); // send
				// unavailable
				// message
				admin.clearKnownlist();
				PacketSendUtility.sendPacket(admin, new SM_PLAYER_INFO(admin, false));
				PacketSendUtility.sendPacket(admin, new SM_MOTION(admin.getObjectId(), admin.getMotions().getActiveMotions()));
				admin.updateKnownlist();
				PacketSendUtility.sendMessage(admin, "you are now Unavailable and Unwispable by players");
			}
		}
	}

	@Override
	public void onFail(Player admin, String message) {
		String syntax = "syntax //gm <on|off>";
		PacketSendUtility.sendMessage(admin, syntax);
	}
}
