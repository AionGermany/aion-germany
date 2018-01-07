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
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

public class Landing extends AdminCommand {

	public Landing() {
		super("landing");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length != 0) {
			int i = 0;
			if ("help".startsWith(params[i])) {
				if (params[i + 1] == null) {
					showHelp(admin);
				}
				else if ("level".startsWith(params[i + 1])) {
					showHelpLevel(admin);
				}
				return;
			}
			if ("update".startsWith(params[i])) {
				AbyssLandingService.getInstance().onUpdate();
			}
			if ("level".startsWith(params[i])) {
				int level = Integer.parseInt(params[i + 2]);
				if (params[i + 1].equalsIgnoreCase("elyos")) {
					if (level > AbyssLandingService.getInstance().redemptionLanding().getLevel()) {
						AbyssLandingService.getInstance().levelUpRedemptionLanding(level);
					}
					else if (level < AbyssLandingService.getInstance().redemptionLanding().getLevel()) {
						AbyssLandingService.getInstance().onRedemptionLandinggLevelDown(level);
					}
				}
				if (params[i + 1].equalsIgnoreCase("asmodians")) {
					if (level > AbyssLandingService.getInstance().harbingerLanding().getLevel()) {
						AbyssLandingService.getInstance().levelUpHarbingerLanding(level);
					}
					else if (level < AbyssLandingService.getInstance().harbingerLanding().getLevel()) {
						AbyssLandingService.getInstance().onHarbingerLandingLevelDown(level);
					}
				}
				return;
			}
		}
	}

	private void showHelp(Player admin) {
		PacketSendUtility.sendMessage(admin, "[Help: Landing Command]\n" + " Use Ex: //landing level elyos 8.\n" + " Notice: This command uses smart matching. You may abbreviate most commands.\n");
	}

	private void showHelpLevel(Player admin) {
		PacketSendUtility.sendMessage(admin, "Syntax: //landing level [Elyos/Asmodians] [Lvl 1-8]\n");
	}

	@Override
	public void onFail(Player player, String message) {
		showHelp(player);
	}
}
