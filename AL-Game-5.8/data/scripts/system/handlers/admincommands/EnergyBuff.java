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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Source
 */
public class EnergyBuff extends AdminCommand {

	public EnergyBuff() {
		super("energy");
	}

	@Override
	public void execute(Player player, String... params) {
		VisibleObject target = player.getTarget();
		if (target == null) {
			PacketSendUtility.sendMessage(player, "No target selected");
			return;
		}

		Creature creature = (Creature) target;
		if (params == null || params.length < 1) {
			onFail(player, null);
		}
		else if (target instanceof Player) {
			if (params[0].equals("repose")) {
				Player targetPlayer = (Player) creature;
				if (params[1].equals("info")) {
					PacketSendUtility.sendMessage(player, "Current EoR: " + targetPlayer.getCommonData().getCurrentReposteEnergy() + "\n Max EoR: " + targetPlayer.getCommonData().getMaxReposteEnergy());
				}
				else if (params[1].equals("add")) {
					targetPlayer.getCommonData().addReposteEnergy(Long.parseLong(params[2]));
				}
				else if (params[1].equals("reset")) {
					targetPlayer.getCommonData().setCurrentReposteEnergy(0);
				}
			}
			else if (params[0].equals("salvation")) {
				Player targetPlayer = (Player) creature;
				if (params[1].equals("info")) {
					PacketSendUtility.sendMessage(player, "Current EoS: " + targetPlayer.getCommonData().getCurrentSalvationPercent());
				}
				else if (params[1].equals("add")) {
					targetPlayer.getCommonData().addSalvationPoints(Long.parseLong(params[2]));
				}
				else if (params[1].equals("reset")) {
					targetPlayer.getCommonData().resetSalvationPoints();
				}
			}
			else if (params[0].equals("refresh")) {
				Player targetPlayer = (Player) creature;
				PacketSendUtility.sendPacket(targetPlayer, new SM_STATS_INFO(targetPlayer));
			}
		}
		else {
			PacketSendUtility.sendMessage(player, "This is not player");
		}
	}

	@Override
	public void onFail(Player player, String message) {
		String syntax = "//energy repose|salvation|refresh info|reset|add [points]";
		PacketSendUtility.sendMessage(player, syntax);
	}
}
