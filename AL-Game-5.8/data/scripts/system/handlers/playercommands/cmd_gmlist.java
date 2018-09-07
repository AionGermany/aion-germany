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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Eloann
 */
public class cmd_gmlist extends PlayerCommand {

	public cmd_gmlist() {
		super("gmlist");
	}

	@Override
	public void execute(Player player, String... params) {
		final List<Player> admins = new ArrayList<Player>();
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player object) {
				if (object.getAccessLevel() > 0 && object.getFriendList().getStatus() != FriendList.Status.OFFLINE) {
					admins.add(object);
				}
			}
		});

		if (admins.size() > 0) {
			PacketSendUtility.sendMessage(player, "====================");
			if (admins.size() == 1) {
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.ONE_GM_ONLINE));
			}
			else {
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.MORE_GMS_ONLINE));
			}

			for (Player admin : admins) {

				if (AdminConfig.CUSTOMTAG_ENABLE) {
					String adminTag = "%s";
					StringBuilder sb = new StringBuilder(adminTag);
					if (player.getAccessLevel() == 1) {
						adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS1.substring(0, AdminConfig.CUSTOMTAG_ACCESS1.length() - 3)).toString();
					}
					else if (player.getAccessLevel() == 2) {
						adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS2.substring(0, AdminConfig.CUSTOMTAG_ACCESS2.length() - 3)).toString();
					}
					else if (player.getAccessLevel() == 3) {
						adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS3.substring(0, AdminConfig.CUSTOMTAG_ACCESS3.length() - 3)).toString();
					}
					else if (player.getAccessLevel() == 4) {
						adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS4.substring(0, AdminConfig.CUSTOMTAG_ACCESS4.length() - 3)).toString();
					}
					else if (player.getAccessLevel() == 5) {
						adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS5.substring(0, AdminConfig.CUSTOMTAG_ACCESS5.length() - 3)).toString();
					}
					else if (player.getAccessLevel() == 6) {
						adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS6.substring(0, AdminConfig.CUSTOMTAG_ACCESS6.length() - 3)).toString();
					}
					PacketSendUtility.sendMessage(player, String.format(adminTag, admin.getName()));
				}
			}
			PacketSendUtility.sendMessage(player, "====================");

		}
		else {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NO_GM_ONLINE));
		}

	}
}
