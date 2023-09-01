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
package playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Waii
 */
public class cmd_gp extends PlayerCommand {

	public cmd_gp() {
		super("gp");
	}

	@Override
	public void execute(Player player, String... params) {
		int ap = 150000;
		int gp = 200;
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendMessage(player, "Vous n'avez pas asser de points abyssaux, point requis : " + ap);
			return;
		}
		AbyssPointsService.addGp(player, gp);
		AbyssPointsService.addAp(player, -ap);

	}
}
