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

import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.DynamicPortalService;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import org.apache.commons.lang.math.NumberUtils;

public class DynamicPortal extends AdminCommand {

	private static final String COMMAND_OPEN = "open";
	private static final String COMMAND_CLOSE = "close";
	private static final String COMMAND_LIST = "list";
	
	public DynamicPortal() {
		super("dynamicportal");
	}
	
	@Override
	public void execute(Player player, String... params) {

		if (params.length == 0) {
			showHelp(player);
			return;
		} 

		if (COMMAND_CLOSE.equalsIgnoreCase(params[0]) || COMMAND_OPEN.equalsIgnoreCase(params[0])) {
			handleStartStopDynamic(player, params);
		}
		else if(COMMAND_LIST.equalsIgnoreCase(params[0])) {
			HTMLService.showHTML(player, HTMLCache.getInstance().getHTML("dynamicportals.xhtml"));
		}
	}
	
	protected void handleStartStopDynamic(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}

		int dynamicRiftId = NumberUtils.toInt(params[1]);
		if (!isValidDynamicPortalLocationId(player, dynamicRiftId)) {
			showHelp(player);
			return;
		} 
		if (COMMAND_OPEN.equalsIgnoreCase(params[0])) {
			if (DynamicPortalService.getInstance().isDynamicPortalInProgress(dynamicRiftId)) {
				PacketSendUtility.sendMessage(player, "Dynamic Portal " + dynamicRiftId + " is already start");
			} 
			else {
				PacketSendUtility.sendMessage(player, "Dynamic Portal " + dynamicRiftId + " started!");
				DynamicPortalService.getInstance().startDynamicPortal(dynamicRiftId);
			}
		} 
		else if (COMMAND_CLOSE.equalsIgnoreCase(params[0])) {
			if (!DynamicPortalService.getInstance().isDynamicPortalInProgress(dynamicRiftId)) {
				PacketSendUtility.sendMessage(player, "Dynamic Portal " + dynamicRiftId + " is not start!");
			} 
			else {
				PacketSendUtility.sendMessage(player, "Dynamic Portal " + dynamicRiftId + " stopped!");
				DynamicPortalService.getInstance().stopDynamicPortal(dynamicRiftId);
			}
		}
	}
	
	protected boolean isValidDynamicPortalLocationId(Player player, int dynamicRiftId) {
		if (!DynamicPortalService.getInstance().getDynamicPortalLocations().keySet().contains(dynamicRiftId)) {
			PacketSendUtility.sendMessage(player, "Id " + dynamicRiftId + " is invalid");
			return false;
		}

		return true;
	}
	
	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //dynamicportal open|close <Id>\nAdminCommand //dynamicportal list (Shows an HTML with a dynamic portal list)");
	}

	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "<usage //dynamicportal>");
	}
}