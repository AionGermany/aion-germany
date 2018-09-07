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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author MrPoke, lord_rex and ginho1
 * @modified Alex
 */
public class MoveToNpc extends AdminCommand {

	public MoveToNpc() {
		super("movetonpc");
	}

	@Override
	public void execute(Player player, String... params) {
		int npcId = 0;
		String npcName = "", npc = params[0];
		String message = "";
		try {
			if (params[0].contains("[where:")) {
				Pattern f = Pattern.compile("\\[where:([^;]+);\\s*+(\\d{6})");
				Matcher fm = f.matcher(npc);
				if (fm.find()) {
					npcName = fm.group(1);
					npcId = Integer.parseInt(fm.group(2));
				}
			}
			else {
				npcId = Integer.valueOf(npc);
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			onFail(player, e.getMessage());
		}
		catch (NumberFormatException e) {
			for (String param : params) {
				npcName += param + " ";
			}
			npcName = npcName.substring(0, npcName.length() - 1);

			for (NpcTemplate template : DataManager.NPC_DATA.getNpcData().valueCollection()) {
				if (template.getName().equalsIgnoreCase(npcName)) {
					if (npcId == 0) {
						npcId = template.getTemplateId();
					}
					else {
						if (message.isEmpty()) {
							message += "Found others (" + npcName + "): \n";
						}
						message += "Id: " + template.getTemplateId() + "\n";
					}
				}
			}
			if (npcId == 0) {
				PacketSendUtility.sendMessage(player, "NPC " + npcName + " cannot be found");
				return;
			}
		}

		if (npcId > 0) {
			if (!message.isEmpty()) {
				message = "Teleporting to Npc: " + npcName + " " + npcId + "\n" + message;
			}
			else {
				message = "Teleporting to Npc: " + npcName + " " + npcId;
			}
			PacketSendUtility.sendMessage(player, message);
			TeleportService2.teleportToNpc(player, npcId);
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //movetonpc <npc_id|npc name>");
	}
}
