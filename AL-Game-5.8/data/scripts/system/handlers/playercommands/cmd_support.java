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

import com.aionemu.gameserver.model.Support;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.SupportService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author paranaix
 */
public class cmd_support extends PlayerCommand {

	public cmd_support() {
		super("support");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			PacketSendUtility.sendMessage(player, "Syntax: .ticket \"Short description here\" -- will notify GM's of your issue");
			return;
		}
		if (SupportService.getInstance().hasTicket(player)) {
			PacketSendUtility.sendMessage(player, "You already have an open support!");
			return;
		}

		Support support;
		support = new Support(player, params.toString(), "");
		SupportService.getInstance().addTicket(support);
		PacketSendUtility.sendMessage(player, "Your support was sended successfully.");
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
