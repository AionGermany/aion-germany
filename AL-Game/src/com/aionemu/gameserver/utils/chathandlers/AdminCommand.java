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
package com.aionemu.gameserver.utils.chathandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author synchro2
 */
public abstract class AdminCommand extends ChatCommand {

	static final Logger log = LoggerFactory.getLogger("ADMINAUDIT_LOG");

	public AdminCommand(String alias) {
		super(alias);
	}

	@Override
	public boolean checkLevel(Player player) {
		return player.getAccessLevel() >= getLevel();
	}

	@Override
	boolean process(Player player, String text) {

		if (!checkLevel(player)) {
			if (LoggingConfig.LOG_GMAUDIT) {
				log.info("[ADMIN COMMAND] > [Player: " + player.getName() + "] has tried to use the command " + getAlias() + " without having the rights");
			}
			if (player.isGM()) {
				PacketSendUtility.sendMessage(player, "[WARN] You need to have access level " + this.getLevel() + " or more to use " + getAlias());
				return true;
			}
			return false;
		}

		boolean success = false;
		if (text.length() == getAlias().length()) {
			success = this.run(player, EMPTY_PARAMS);
		}
		else {
			success = this.run(player, text.substring(getAlias().length() + 1).split(" "));
		}

		if (LoggingConfig.LOG_GMAUDIT) {
			if (player.getTarget() != null && player.getTarget() instanceof Creature) {
				Creature target = (Creature) player.getTarget();
				log.info("[ADMIN COMMAND] > [Name: " + player.getName() + "][Target : " + target.getName() + "]: " + text);
			}
			else {
				log.info("[ADMIN COMMAND] > [Name: " + player.getName() + "]: " + text);
			}
		}

		if (!success) {
			PacketSendUtility.sendMessage(player, "<You have failed to execute " + text + ">");
			return true;
		}
		else {
			return success;
		}
	}
}
