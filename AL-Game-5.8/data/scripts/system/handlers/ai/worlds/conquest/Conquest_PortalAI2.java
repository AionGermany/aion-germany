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
package ai.worlds.conquest;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.portal.ConquestPortal;
import com.aionemu.gameserver.model.templates.portal.ConquestPortalLoc;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI2;

/**
 * @author Falke_34, CoolyT
 */
@AIName("conquest_portal")
public class Conquest_PortalAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleUseItemFinish(Player player) {

		if (player.getAccessLevel() >= 2)
			PacketSendUtility.sendMessage(player, "ConquestPortal AI wurde geladen");
		int npcId = getOwner().getObjectTemplate().getTemplateId();

		ConquestPortal portal = DataManager.CONQUEST_PORTAL_DATA.getPortalbyNpcId(npcId);
		int index = Rnd.get(0, portal.locs.size() - 1);
		ConquestPortalLoc loc = portal.locs.get(index);
		if (player.getAccessLevel() >= 2)
			PacketSendUtility.sendMessage(player, "ConquestPortal [DEBUG] - Teleporting to : " + loc.toString());
		TeleportService2.teleportTo(player, getOwner().getWorldId(), loc.x, loc.y, loc.z, loc.h, TeleportAnimation.JUMP_ANIMATION);
		getOwner().getController().delete();
	}
}
