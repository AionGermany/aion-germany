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

import com.aionemu.gameserver.model.gameobjects.Gatherable;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Maestros
 */
public class cmd_userinfo extends PlayerCommand {

	public cmd_userinfo() {
		super("userinfo");
	}

	@Override
	public void execute(Player player, String... params) {
		VisibleObject target = player.getTarget();

		if (target instanceof Player) {
			PacketSendUtility.sendMessage(player, "Du darfst dir keine Infos von anderen Spielern holen");
		}
		else if (target instanceof Npc) {
			Npc npc = (Npc) player.getTarget();
			PacketSendUtility.sendMessage(player, "[NPC Info]" + "\nName: " + npc.getName() + "\nId: " + npc.getNpcId() + "\nMap ID: " + player.getTarget().getWorldId());
		}
		else if (target instanceof Gatherable) {
			Gatherable gather = (Gatherable) target;
			PacketSendUtility.sendMessage(player, "[Gather Info]\n" + "Name: " + gather.getName() + "\nId: " + gather.getObjectTemplate().getTemplateId() + "\nMap ID: " + player.getTarget().getWorldId());
		}
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
