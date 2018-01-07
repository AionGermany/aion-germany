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

import com.aionemu.gameserver.configs.administration.CommandsConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author -Evilwizard-, Wakizashi World Channel, only for GM/Admins
 */
public class Wc extends AdminCommand {

	public Wc() {
		super("wc");
	}

	@Override
	public void execute(Player admin, String... params) {
		int i = 1;
		boolean check = true;
		Race adminRace = admin.getRace();

		if (params.length < 2) {
			PacketSendUtility.sendMessage(admin, "syntax : //wc <ELY | ASM | ALL | default> <message>");
			return;
		}

		StringBuilder sbMessage;
		if (params[0].equals("ELY")) {
			sbMessage = new StringBuilder("[World-Elyos]" + admin.getName() + ": ");
			adminRace = Race.ELYOS;
		}
		else if (params[0].equals("ASM")) {
			sbMessage = new StringBuilder("[World-Asmodian]" + admin.getName() + ": ");
			adminRace = Race.ASMODIANS;
		}
		else if (params[0].equals("ALL")) {
			sbMessage = new StringBuilder("[World-All]" + admin.getName() + ": ");
		}
		else {
			check = false;
			if (adminRace == Race.ELYOS) {
				sbMessage = new StringBuilder("[World-Elyos]" + admin.getName() + ": ");
			}
			else {
				sbMessage = new StringBuilder("[World-Asmodian]" + admin.getName() + ": ");
			}
		}

		for (String s : params) {
			if (i++ != 1 && (check)) {
				sbMessage.append(s + " ");
			}
		}

		String message = sbMessage.toString().trim();
		int messageLenght = message.length();

		final String sMessage = message.substring(0, CustomConfig.MAX_CHAT_TEXT_LENGHT > messageLenght ? messageLenght : CustomConfig.MAX_CHAT_TEXT_LENGHT);
		final boolean toAll = params[0].equals("ALL");
		final Race race = adminRace;

		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (toAll || player.getRace() == race || player.getAccessLevel() >= CommandsConfig.WC) {
					PacketSendUtility.sendMessage(player, sMessage);
				}
			}
		});
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax : //wc <ELY | ASM | ALL | default> <message>");
	}
}
