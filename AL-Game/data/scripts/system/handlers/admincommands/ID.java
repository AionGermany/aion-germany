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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

public class ID extends AdminCommand {

	public ID() {
		super("id");
	}

	@Override
	public void execute(Player admin, String... params) {
		VisibleObject target = admin.getTarget();

		if (admin.getAccessLevel() < 1) {
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to use this command!");
			return;
		}

		if (target instanceof Npc) {
			Npc npc = (Npc) admin.getTarget();
			PacketSendUtility.sendMessage(admin, "[Info about target]" + "\nName: " + npc.getName() + "\nId: " + npc.getNpcId());
		}
	}
}
