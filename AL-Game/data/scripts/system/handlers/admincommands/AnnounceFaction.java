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

import java.util.Iterator;

import com.aionemu.gameserver.configs.administration.CommandsConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * Admin announce faction
 *
 * @author Divinity
 */
public class AnnounceFaction extends AdminCommand {

	public AnnounceFaction() {
		super("announcefaction");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length < 2) {
			PacketSendUtility.sendMessage(player, "Syntax: //announcefaction <ely | asmo> <message>");
		}
		else {
			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			String message = null;

			if (params[0].equals("ely")) {
				message = "Elyos : ";
			}
			else {
				message = "Asmodians : ";
			}

			// Add with space
			for (int i = 1; i < params.length - 1; i++) {
				message += params[i] + " ";
			}

			// Add the last without the end space
			message += params[params.length - 1];

			Player target = null;

			while (iter.hasNext()) {
				target = iter.next();

				if (target.getAccessLevel() > CommandsConfig.ANNONCEFACTION || target.getRace() == Race.ELYOS && params[0].equals("ely")) {
					PacketSendUtility.sendBrightYellowMessageOnCenter(target, message);
				}
				else if (target.getAccessLevel() > CommandsConfig.ANNONCEFACTION || target.getCommonData().getRace() == Race.ASMODIANS && params[0].equals("asmo")) {
					PacketSendUtility.sendBrightYellowMessageOnCenter(target, message);
				}
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: //announcefaction <ely | asmo> <message>");
	}
}
