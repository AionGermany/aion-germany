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

import java.util.Arrays;

import com.aionemu.gameserver.configs.administration.CommandsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TITLE_INFO;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.player.FatigueService;
import com.aionemu.gameserver.services.player.LunaShopService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Nemiroff, ATracer, IceReaper Date: 11.12.2009
 * @author Sarynth - Added AP
 * @author Alcapwnd - Added Fatigue
 */
public class Set extends AdminCommand {

	public Set() {
		super("set");
	}

	@Override
	public void execute(Player admin, String... params) {
		Player target = null;
		VisibleObject creature = admin.getTarget();

		if (admin.getTarget() instanceof Player) {
			target = (Player) creature;
		}

		if (target == null) {
			PacketSendUtility.sendMessage(admin, "You should select a target first!");
			return;
		}

		if (params.length < 2) {
			PacketSendUtility.sendMessage(admin, "You should enter second params!");
			return;
		}
		String paramValue = params[1];

		if (params[0].equals("class")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}

			byte newClass;
			try {
				newClass = Byte.parseByte(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}

			PlayerClass oldClass = target.getPlayerClass();
			setClass(target, oldClass, newClass);
		}
		else if (params[0].equals("exp")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}

			long exp;
			try {
				exp = Long.parseLong(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}

			target.getCommonData().setExp(exp);
			PacketSendUtility.sendMessage(admin, "Set exp of target to " + paramValue);
		}
		else if (params[0].equals("ap")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}

			int ap;
			try {
				ap = Integer.parseInt(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}

			AbyssPointsService.setAp(target, ap);
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Set your Abyss Points to " + ap + ".");
			}
			else {
				PacketSendUtility.sendMessage(admin, "Set " + target.getName() + " Abyss Points to " + ap + ".");
				PacketSendUtility.sendMessage(target, "Admin set your Abyss Points to " + ap + ".");
			}
		}
		else if (params[0].equals("gp")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}

			int gp;
			try {
				gp = Integer.parseInt(paramValue);
				if (gp > 15000) {
					return;
				}
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}

			AbyssPointsService.setGp(target, gp);
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Set your Glory Points to " + gp + ".");
			}
			else {
				PacketSendUtility.sendMessage(admin, "Set " + target.getName() + " Glory Points to " + gp + ".");
				PacketSendUtility.sendMessage(target, "Admin set your Glory Points to " + gp + ".");
			}
		}
		else if (params[0].equals("fatigue")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}

			int fatigue;
			try {
				fatigue = Integer.parseInt(paramValue);
				if (fatigue > 100)
					fatigue = 100;
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}

			int currentFatigue = target.getCommonData().getFatigue();

			target.getCommonData().setFatigue(currentFatigue + fatigue);
			if (target.getCommonData().getFatigue() > 100)
				target.getCommonData().setFatigue(100);
			FatigueService.getInstance().checkFatigue(target);
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Set your Fatigue to " + fatigue + "%.");
			}
			else {
				PacketSendUtility.sendMessage(admin, "Set " + target.getName() + " Fatigue to " + fatigue + "%.");
				PacketSendUtility.sendMessage(target, "Admin set your Fatigue to " + fatigue + "%.");
			}
		}
		else if (params[0].equals("level")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}

			int level;
			try {
				level = Integer.parseInt(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}

			Player player = target;

			if (level <= GSConfig.PLAYER_MAX_LEVEL) {
				player.getCommonData().setLevel(level);
			}
			PacketSendUtility.sendMessage(admin, "Set " + player.getCommonData().getName() + " level to " + level);
		}
		else if (params[0].equals("title")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}

			int titleId;
			try {
				titleId = Integer.parseInt(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}

			Player player = target;
			if (titleId <= 330) {
				setTitle(player, titleId);
			}
			PacketSendUtility.sendMessage(admin, "Set " + player.getCommonData().getName() + " title to " + titleId);
		}
		else if (params[0].equals("luna")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int luna;
			try {
				luna = Integer.parseInt(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			LunaShopService.getInstance().lunaPointController(target, (int) (target.getLunaAccount() + luna));
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Added " + luna + " <Luna Coins>.");
			}
			else {
				PacketSendUtility.sendMessage(admin, "Added " + luna + " <Luna Coins> to " + target.getName() + ".");
				PacketSendUtility.sendMessage(target, "Admin added " + luna + " <Luna Coins> to your Account.");
			}
		}
		else if (params[0].equals("key")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int key;
			try {
				key = Integer.parseInt(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			LunaShopService.getInstance().muniKeysController(target, target.getMuniKeys() + key);
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Added " + key + " <Munirunerk's Keys>.");
			}
			else {
				PacketSendUtility.sendMessage(admin, "Added " + key + " <Munirunerk's Keys> to " + target.getName() + ".");
				PacketSendUtility.sendMessage(target, "Admin added " + key + " <Munirunerk's Keys> to your Char.");
			}
		}
	}

	private void setTitle(Player player, int value) {
		PacketSendUtility.sendPacket(player, new SM_TITLE_INFO(value));
		PacketSendUtility.broadcastPacket(player, (new SM_TITLE_INFO(player, value)));
		player.getCommonData().setTitleId(value);
	}

	private void setClass(Player player, PlayerClass oldClass, byte value) {
		PlayerClass playerClass = PlayerClass.getPlayerClassById(value);
		int level = player.getLevel();
		if (level < 9) {
			PacketSendUtility.sendMessage(player, "You can only switch class after reach level 9");
			return;
		}
		if (Arrays.asList(1, 2, 4, 5, 7, 8, 10, 11, 13, 14, 16).contains(oldClass.ordinal())) {
			PacketSendUtility.sendMessage(player, "You already switched class");
			return;
		}
		int newClassId = playerClass.ordinal();
		switch (oldClass.ordinal()) {
			case 0:
				if (newClassId == 1 || newClassId == 2) {
					break;
				}
			case 3:
				if (newClassId == 4 || newClassId == 5) {
					break;
				}
			case 6:
				if (newClassId == 7 || newClassId == 8) {
					break;
				}
			case 9:
				if (newClassId == 10 || newClassId == 11) {
					break;
				}
			case 12:
				if (newClassId == 13 || newClassId == 14) {
					break;
				}
			case 15:
				if (newClassId == 16) {
					break;
				}
			default:
				PacketSendUtility.sendMessage(player, "Invalid class switch chosen");
				return;
		}
		player.getCommonData().setPlayerClass(playerClass);
		player.getController().upgradePlayer();
		PacketSendUtility.sendMessage(player, "You have successfuly switched class");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //set <class|exp|ap|gp|fatigue|level|title|luna>");
	}
}
