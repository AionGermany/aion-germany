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

import java.util.Iterator;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dao.OldNamesDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NameRestrictionService;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author xTz
 */
public class Rename extends AdminCommand {

	public Rename() {
		super("rename");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 1 || params.length > 2) {
			PacketSendUtility.sendMessage(admin, "No parameters detected.\n" + "Please use //rename <Player name> <rename>\n" + "or use //rename [target] <rename>");
			return;
		}

		Player player = null;
		String recipient = null;
		String rename = null;

		if (params.length == 2) {
			recipient = Util.convertName(params[0]);
			rename = Util.convertName(params[1]);

			if (!DAOManager.getDAO(PlayerDAO.class).isNameUsed(recipient)) {
				PacketSendUtility.sendMessage(admin, "Could not find a Player by that name.");
				return;
			}
			PlayerCommonData recipientCommonData = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(recipient);
			player = recipientCommonData.getPlayer();

			if (!check(admin, rename)) {
				return;
			}

			if (!CustomConfig.OLD_NAMES_COMMAND_DISABLED) {
				DAOManager.getDAO(OldNamesDAO.class).insertNames(player.getObjectId(), player.getName(), rename);
			}
			recipientCommonData.setName(rename);
			DAOManager.getDAO(PlayerDAO.class).storePlayerName(recipientCommonData);
			if (recipientCommonData.isOnline()) {
				PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
				PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
				sendPacket(admin, player, rename, recipient);
			}
			else {
				PacketSendUtility.sendMessage(admin, "Player " + recipient + " has been renamed to " + rename);
			}
		}
		if (params.length == 1) {
			rename = Util.convertName(params[0]);

			VisibleObject target = admin.getTarget();
			if (target == null) {
				PacketSendUtility.sendMessage(admin, "You should select a target first!");
				return;
			}

			if (target instanceof Player) {
				player = (Player) target;
				if (!check(admin, rename)) {
					return;
				}

				if (!CustomConfig.OLD_NAMES_COMMAND_DISABLED) {
					DAOManager.getDAO(OldNamesDAO.class).insertNames(player.getObjectId(), player.getName(), rename);
				}
				player.getCommonData().setName(rename);
				PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
				DAOManager.getDAO(PlayerDAO.class).storePlayerName(player.getCommonData());
			}
			else {
				PacketSendUtility.sendMessage(admin, "The command can be applied only on the player.");
			}

			recipient = target.getName();
			sendPacket(admin, player, rename, recipient);
		}
	}

	private static boolean check(Player admin, String rename) {
		if (!NameRestrictionService.isValidName(rename)) {
			PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400151));
			return false;
		}
		if (!PlayerService.isFreeName(rename)) {
			PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400155));
			return false;
		}
		if (!CustomConfig.OLD_NAMES_COMMAND_DISABLED && PlayerService.isOldName(rename)) {
			PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400155));
			return false;
		}
		return true;
	}

	public void sendPacket(Player admin, Player player, String rename, String recipient) {
		Iterator<Friend> knownFriends = player.getFriendList().iterator();

		while (knownFriends.hasNext()) {
			Friend nextObject = knownFriends.next();
			if (nextObject.getPlayer() != null && nextObject.getPlayer().isOnline()) {
				PacketSendUtility.sendPacket(nextObject.getPlayer(), new SM_PLAYER_INFO(player, false));
			}
		}

		if (player.isLegionMember()) {
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
		}
		PacketSendUtility.sendMessage(player, "You have been renamed to " + rename);
		PacketSendUtility.sendMessage(admin, "Player " + recipient + " has been renamed to " + rename);
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "No parameters detected.\n" + "Please use //rename <Player name> <rename>\n" + "or use //rename [target] <rename>");
	}
}
