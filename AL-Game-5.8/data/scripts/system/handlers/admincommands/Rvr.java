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
import com.aionemu.gameserver.services.RvrService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

public class Rvr extends AdminCommand {

	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";

	public Rvr() {
		super("rvr");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		}
		if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopRvr(player, params);
		}
	}

	protected void handleStartStopRvr(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}
		int rvrId = NumberUtils.toInt(params[1]);
		if (!isValidRvrLocationId(player, rvrId)) {
			showHelp(player);
			return;
		}
		if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (RvrService.getInstance().isRvrInProgress(rvrId)) {
				PacketSendUtility.sendMessage(player, "<R.v.r> " + rvrId + " is already start");
			}
			else {
				PacketSendUtility.sendMessage(player, "<R.v.r> " + rvrId + " started!");
				RvrService.getInstance().startRvr(rvrId);
			}
		}
		else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!RvrService.getInstance().isRvrInProgress(rvrId)) {
				PacketSendUtility.sendMessage(player, "<R.v.r> " + rvrId + " is not start!");
			}
			else {
				PacketSendUtility.sendMessage(player, "<R.v.r> " + rvrId + " stopped!");
				RvrService.getInstance().stopRvr(rvrId);
			}
		}
	}

	protected boolean isValidRvrLocationId(Player player, int rvrId) {
		if (!RvrService.getInstance().getRvrLocations().keySet().contains(rvrId)) {
			PacketSendUtility.sendMessage(player, "Id " + rvrId + " is invalid");
			return false;
		}
		return true;
	}

	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //rvr start|stop <Id>");
	}
}
