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
import com.aionemu.gameserver.services.RiftService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

public class Rift extends AdminCommand {

	private static final String COMMAND_OPEN = "open";
	private static final String COMMAND_CLOSE = "close";

	public Rift() {
		super("rift");
	}

	@Override
	public void execute(Player player, String... params) {

		if (params.length == 0) {
			showHelp(player);
			return;
		}

		if (COMMAND_CLOSE.equalsIgnoreCase(params[0]) || COMMAND_OPEN.equalsIgnoreCase(params[0])) {
			handleRift(player, params);
		}
	}

	protected void handleRift(Player player, String... params) {
		if (params.length < 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}

		int id = NumberUtils.toInt(params[1]);
		boolean result;
		if (!isValidId(player, id)) {
			showHelp(player);
			return;
		}

		if (COMMAND_OPEN.equalsIgnoreCase(params[0])) {
			boolean guards = Boolean.parseBoolean(params[2]);
			result = RiftService.getInstance().openRifts(id, guards);
			PacketSendUtility.sendMessage(player, result ? "Rifts is opened!" : "Rifts was already opened");
		}
		else if (COMMAND_CLOSE.equalsIgnoreCase(params[0])) {
			result = RiftService.getInstance().closeRifts(id);
			PacketSendUtility.sendMessage(player, result ? "Rifts is closed!" : "Rifts was already closed");
		}
	}

	protected boolean isValidId(Player player, int id) {
		if (!RiftService.getInstance().isValidId(id)) {
			PacketSendUtility.sendMessage(player, "Id " + id + " is invalid");
			return false;
		}

		return true;
	}

	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //rift open|close <Id|worldId> (open with boolean for guards)");
	}
}
