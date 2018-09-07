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
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Sarynth, (edited by Pan)
 */
public class Neutral extends AdminCommand {

	public Neutral() {
		super("neutral");
	}

	@Override
	public void execute(Player admin, String... params) {
		String help = "Syntax: //neutral < players | npcs | all | cancel >\n" + "Players - You're neutral to Players of both factions.\n" + "Npcs - You're neutral to all Npcs and Monsters.\n" + "All - You're neutral to Players of both factions and all Npcs.\n" + "Cancel - Cancel all. Players and Npcs have default enmity to you.";

		if (params.length != 1) {
			onFail(admin, null);
			return;
		}

		String output = "You now appear neutral to " + params[0] + ".";

		int enemyType = admin.getAdminEnmity();

		if (params[0].equals("all")) {
			admin.setAdminNeutral(3);
			admin.setAdminEnmity(0);
		}
		else if (params[0].equals("players")) {
			admin.setAdminNeutral(2);
			if (enemyType > 1) {
				admin.setAdminEnmity(0);
			}
		}
		else if (params[0].equals("npcs")) {
			admin.setAdminNeutral(1);
			if (enemyType == 1 || enemyType == 3) {
				admin.setAdminEnmity(0);
			}
		}
		else if (params[0].equals("cancel")) {
			admin.setAdminNeutral(0);
			output = "You appear regular to both Players and Npcs.";
		}
		else if (params[0].equals("help")) {
			PacketSendUtility.sendMessage(admin, help);
			return;
		}
		else {
			onFail(admin, null);
			return;
		}

		PacketSendUtility.sendMessage(admin, output);

		admin.clearKnownlist();
		PacketSendUtility.sendPacket(admin, new SM_PLAYER_INFO(admin, false));
		PacketSendUtility.sendPacket(admin, new SM_MOTION(admin.getObjectId(), admin.getMotions().getActiveMotions()));
		admin.updateKnownlist();
	}

	@Override
	public void onFail(Player player, String message) {
		String syntax = "Syntax: //neutral < players | npcs | all | cancel >\n" + "If you're unsure about what you want to do, type //neutral help";
		PacketSendUtility.sendMessage(player, syntax);
	}
}
