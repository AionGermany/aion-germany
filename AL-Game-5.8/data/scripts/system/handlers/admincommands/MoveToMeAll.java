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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Shepper Helped by
 * @alfa24t
 */
public class MoveToMeAll extends AdminCommand {

	public MoveToMeAll() {
		super("movetomeall");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, "syntax //movetomeall < all | elyos | asmos >");
			return;
		}

		if (params[0].equals("all")) {
			for (final Player p : World.getInstance().getAllPlayers()) {
				if (!p.equals(admin)) {
					TeleportService2.teleportTo(p, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading());
					PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));

					PacketSendUtility.sendMessage(admin, "Player " + p.getName() + " teleported.");
					PacketSendUtility.sendMessage(p, "Teleportd by " + admin.getName() + ".");
				}
			}
		}

		if (params[0].equals("elyos")) {
			for (final Player p : World.getInstance().getAllPlayers()) {
				if (!p.equals(admin)) {
					if (p.getRace() == Race.ELYOS) {
						TeleportService2.teleportTo(p, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading());
						PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));

						PacketSendUtility.sendMessage(admin, "Player " + p.getName() + " teleported.");
						PacketSendUtility.sendMessage(p, "Teleportd by " + admin.getName() + ".");
					}
				}
			}
		}

		if (params[0].equals("asmos")) {
			for (final Player p : World.getInstance().getAllPlayers()) {
				if (!p.equals(admin)) {
					if (p.getRace() == Race.ASMODIANS) {
						TeleportService2.teleportTo(p, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading());
						PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));

						PacketSendUtility.sendMessage(admin, "Player " + p.getName() + " teleported.");
						PacketSendUtility.sendMessage(p, "Teleportd by " + admin.getName() + ".");
					}
				}
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //movetomeall < all | elyos | asmos >");
	}
}
