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

import org.apache.commons.lang.math.NumberUtils;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.SvsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

public class Svs extends AdminCommand {

	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";

	public Svs() {
		super("svs");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		}
		if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopSvs(player, params);
		}
	}

	protected void handleStartStopSvs(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}
		int svsId = NumberUtils.toInt(params[1]);
		if (!isValidSvsLocationId(player, svsId)) {
			showHelp(player);
			return;
		}
		if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (SvsService.getInstance().isSvsInProgress(svsId)) {
				PacketSendUtility.sendMessage(player, "<S.v.s> " + svsId + " is already start");
			}
			else {
				PacketSendUtility.sendMessage(player, "<S.v.s> " + svsId + " started!");
				SvsService.getInstance().startSvs(svsId);
			}
		}
		else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!SvsService.getInstance().isSvsInProgress(svsId)) {
				PacketSendUtility.sendMessage(player, "<S.v.s> " + svsId + " is not start!");
			}
			else {
				PacketSendUtility.sendMessage(player, "<S.v.s> " + svsId + " stopped!");
				SvsService.getInstance().stopSvs(svsId);
			}
		}
	}

	protected boolean isValidSvsLocationId(Player player, int svsId) {
		if (!SvsService.getInstance().getSvsLocations().keySet().contains(svsId)) {
			PacketSendUtility.sendMessage(player, "Id " + svsId + " is invalid");
			return false;
		}
		return true;
	}

	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //svs start|stop <Id>");
	}
}
