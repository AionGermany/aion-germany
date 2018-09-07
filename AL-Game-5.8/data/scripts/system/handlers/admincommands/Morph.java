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
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author ATracer
 * @modified By aionchs- Wylovech
 */
public class Morph extends AdminCommand {

	public Morph() {
		super("morph");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length != 1) {
			PacketSendUtility.sendMessage(admin, "syntax //morph <NPC Id | cancel> ");
			return;
		}

		Player target = admin;
		int param = 0;

		if (admin.getTarget() instanceof Player) {
			target = (Player) admin.getTarget();
		}

		if (!("cancel").startsWith(params[0].toLowerCase())) {
			try {
				param = Integer.parseInt(params[0]);

			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "Parameter must be an integer, or cancel.");
				return;
			}
		}

		if ((param != 0 && param < 200000) || param > 298021) {
			PacketSendUtility.sendMessage(admin, "Something wrong with the NPC Id!");
			return;
		}

		target.getTransformModel().setModelId(param);
		PacketSendUtility.broadcastPacketAndReceive(target, new SM_TRANSFORM(target, true));

		if (param == 0) {
			if (target.equals(admin)) {
				PacketSendUtility.sendMessage(target, "Morph successfully cancelled.");
			}
			else {
				PacketSendUtility.sendMessage(target, "Your morph has been cancelled by " + admin.getName() + ".");
				PacketSendUtility.sendMessage(admin, "You have cancelled " + target.getName() + "'s morph.");
			}
		}
		else {
			if (target.equals(admin)) {
				PacketSendUtility.sendMessage(target, "Successfully morphed to npcId " + param + ".");
			}
			else {
				PacketSendUtility.sendMessage(target, admin.getName() + " morphs you into an NPC form.");
				PacketSendUtility.sendMessage(admin, "You morph " + target.getName() + " to npcId " + param + ".");
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //morph <NPC Id | cancel> ");
	}
}
