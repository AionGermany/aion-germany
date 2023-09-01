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
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author Antraxx
 */
public class PlayerList extends AdminCommand {

	public PlayerList() {
		super("playerlist");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(player, "syntax //playerlist <all|ely|asmo|premium|vip>");
			return;
		}

		// get all currently connected players
		Collection<Player> players = World.getInstance().getAllPlayers();
		PacketSendUtility.sendMessage(player, "Currently connected players:");

		for (Player p : players) {
			if (params != null && params.length > 0) {
				String cmd = params[0].toLowerCase().trim();
				if (("ely").startsWith(cmd)) {
					if (p.getCommonData().getRace() == Race.ASMODIANS) {
						continue;
					}
				}
				if (("asmo").startsWith(cmd)) {
					if (p.getCommonData().getRace() == Race.ELYOS) {
						continue;
					}
				}
				if (("premium").startsWith(cmd)) {
					if (p.getPlayerAccount().getMembership() == 2) {
						continue;
					}
				}
				if (("vip").startsWith(cmd)) {
					if (p.getPlayerAccount().getMembership() == 1) {
						continue;
					}
				}
			}

			PacketSendUtility.sendMessage(player, "Char: " + p.getName() + " (" + p.getAcountName() + ") " + " - " + p.getCommonData().getRace().name() + "/" + p.getCommonData().getPlayerClass().name() + " - Location: " + WorldMapType.getWorld(p.getWorldId()).name());
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //playerlist <all|ely|asmo|premium|vip>");
	}
}
