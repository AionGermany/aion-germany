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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.assemblednpc.AssembledNpc;
import com.aionemu.gameserver.model.assemblednpc.AssembledNpcPart;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.assemblednpc.AssembledNpcTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NPC_ASSEMBLER;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;

import javolution.util.FastList;

/**
 * @author xTz
 */
public class SpawnAssembledNpc extends AdminCommand {

	public SpawnAssembledNpc() {
		super("spawnAssembledNpc");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length != 1) {
			onFail(player, null);
			return;
		}
		int spawnId = 0;
		try {
			spawnId = Integer.parseInt(params[0]);
		}
		catch (Exception e) {
			onFail(player, null);
			return;
		}

		AssembledNpcTemplate template = DataManager.ASSEMBLED_NPC_DATA.getAssembledNpcTemplate(spawnId);
		if (template == null) {
			PacketSendUtility.sendMessage(player, "This spawnId is Wrong.");
			return;
		}
		FastList<AssembledNpcPart> assembledPatrs = new FastList<AssembledNpcPart>();
		for (AssembledNpcTemplate.AssembledNpcPartTemplate npcPart : template.getAssembledNpcPartTemplates()) {
			assembledPatrs.add(new AssembledNpcPart(IDFactory.getInstance().nextId(), npcPart));
		}
		AssembledNpc npc = new AssembledNpc(template.getRouteId(), template.getMapId(), template.getLiveTime(), assembledPatrs);
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		Player findedPlayer = null;
		while (iter.hasNext()) {
			findedPlayer = iter.next();
			PacketSendUtility.sendPacket(findedPlayer, new SM_NPC_ASSEMBLER(npc));
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //spawnAssembledNpc <sapwnId>");
	}
}
