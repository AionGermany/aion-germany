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

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.taskmanager.tasks.MovementNotifyTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Rolandas
 */
public class Map extends AdminCommand {

	public Map() {
		super("map");
	}

	@Override
	public void execute(Player admin, String... params) {
		WorldMapInstance instance = admin.getPosition().getWorldMapInstance();
		if ("freeze".equalsIgnoreCase(params[0])) {
			for (Npc npc : instance.getNpcs()) {
				npc.getAi2().onGeneralEvent(AIEventType.FREEZE);
			}
			PacketSendUtility.sendMessage(admin, "World map is frozen!");
		}
		else if ("unfreeze".equalsIgnoreCase(params[0])) {
			for (Npc npc : instance.getNpcs()) {
				npc.getAi2().onGeneralEvent(AIEventType.UNFREEZE);
			}
			PacketSendUtility.sendMessage(admin, "World map is unfrozen!");
		}
		else if ("stats".equalsIgnoreCase(params[0])) {
			for (String line : MovementNotifyTask.getInstance().dumpBroadcastStats()) {
				PacketSendUtility.sendMessage(admin, line);
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "usage: //map freeze | unfreeze | stats");
	}
}
