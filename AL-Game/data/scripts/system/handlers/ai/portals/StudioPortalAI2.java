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
package ai.portals;

import static com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE.STR_HOUSING_ENTER_NEED_HOUSE;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.ActionItemNpcAI2;

/**
 * @author Rolandas
 */
@AIName("studioportal")
public class StudioPortalAI2 extends ActionItemNpcAI2 {

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		return true;
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		int ownerId = player.getPosition().getWorldMapInstance().getOwnerId();
		House studio = HousingService.getInstance().getPlayerStudio(player.getObjectId());
		if (studio == null && ownerId == 0) // Doesn't own studio and not in studio
		{
			PacketSendUtility.sendPacket(player, STR_HOUSING_ENTER_NEED_HOUSE);
			return;
		}

		int exitMapId = 0;
		float x = 0, y = 0, z = 0;
		byte heading = 0;
		int instanceId = 0;

		if (ownerId > 0) { // leaving
			studio = HousingService.getInstance().getPlayerStudio(ownerId);
			exitMapId = studio.getAddress().getExitMapId();
			instanceId = World.getInstance().getWorldMap(exitMapId).getMainWorldMapInstance().getInstanceId();
			x = studio.getAddress().getExitX();
			y = studio.getAddress().getExitY();
			z = studio.getAddress().getExitZ();
		}
		else if (studio == null) {
			return; // doesn't own studio
		}
		else { // entering own studio
			exitMapId = studio.getAddress().getMapId();
			WorldMapInstance instance = InstanceService.getPersonalInstance(exitMapId, player.getObjectId());
			if (instance == null) {
				instance = InstanceService.getNextAvailableInstance(exitMapId, player.getObjectId());
				InstanceService.registerPlayerWithInstance(instance, player);
			}
			instanceId = instance.getInstanceId();
			x = studio.getAddress().getX();
			y = studio.getAddress().getY();
			z = studio.getAddress().getZ();
			if (exitMapId == 710010000) {
				heading = 36;
			}
		}
		TeleportService2.teleportTo(player, exitMapId, instanceId, x, y, z, heading, TeleportAnimation.BEAM_ANIMATION);
	}
}
