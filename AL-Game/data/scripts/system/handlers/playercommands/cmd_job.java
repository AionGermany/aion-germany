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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

public class cmd_job extends PlayerCommand {

	public cmd_job() {
		super("job");
	}

	@Override
	public void execute(Player player, String... params) {
		player.getSkillList().addSkill(player, 30002, 499); // Vita
		player.getSkillList().addSkill(player, 30003, 499); // Ether
		player.getSkillList().addSkill(player, 40001, 550); // Cuisine
		player.getSkillList().addSkill(player, 40002, 550); // Armes
		player.getSkillList().addSkill(player, 40003, 550); // Armure
		player.getSkillList().addSkill(player, 40004, 550); // Couture
		player.getSkillList().addSkill(player, 40007, 550); // Alchimie
		player.getSkillList().addSkill(player, 40008, 550); // Artisanat
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: .job ");
	}
}
