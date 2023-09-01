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
package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Alcapwnd
 */
public class CmdChangeClass extends AbstractGMHandler {

	public CmdChangeClass(Player admin, String params) {
		super(admin, params);
		run();
	}

	public void run() {
		// Only for admins !
		byte classId;
		String ClassChoose = params;
		if (ClassChoose.equalsIgnoreCase("warrior")) {
			classId = 0;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("fighter")) {
			classId = 1;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("knight")) {
			classId = 2;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("scout")) {
			classId = 3;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("assassin")) {
			classId = 4;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("ranger")) {
			classId = 5;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("mage")) {
			classId = 6;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("wizard")) {
			classId = 7;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("elementalist")) {
			classId = 8;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("cleric")) {
			classId = 9;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("priest")) {
			classId = 10;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("chanter")) {
			classId = 11;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("engineer")) {
			classId = 12;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("rider")) {
			classId = 13;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("gunner")) {
			classId = 14;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("artist")) {
			classId = 15;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else if (ClassChoose.equalsIgnoreCase("bard")) {
			classId = 16;
			PlayerClass playerClass = PlayerClass.getPlayerClassById(classId);
			admin.getCommonData().setPlayerClass(playerClass);
			admin.getController().upgradePlayer();
			PacketSendUtility.sendMessage(admin, "You have successfuly switched class");
		}
		else
			PacketSendUtility.sendMessage(admin, "Invalid class switch chosen!");
	}
}
