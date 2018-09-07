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

import java.util.Collection;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

public class Who extends AdminCommand {

	public Who() {
		super("who");
	}

	@Override
	public void execute(Player admin, String... params) {

		Collection<Player> players = World.getInstance().getAllPlayers();

		PacketSendUtility.sendMessage(admin, "Player :");

		for (Player player : players) {
			if (params != null && params.length > 0) {
				String cmd = params[0].toLowerCase();

				if (("ely").startsWith(cmd)) {
					if (player.getCommonData().getRace() == Race.ASMODIANS) {
						continue;
					}
				}

				if (("asmo").startsWith(cmd)) {
					if (player.getCommonData().getRace() == Race.ELYOS) {
						continue;
					}
				}

				if (("member").startsWith(cmd) || ("premium").startsWith(cmd)) {
					if (player.getPlayerAccount().getMembership() == 0) {
						continue;
					}
				}
			}

			PacketSendUtility.sendMessage(admin, "Char: " + player.getName() + " - Race: " + player.getCommonData().getRace().name() + " - Acc: " + player.getAcountName());
		}
	}
}
