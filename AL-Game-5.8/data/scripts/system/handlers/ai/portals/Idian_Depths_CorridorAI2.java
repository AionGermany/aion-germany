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

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI2;

@AIName("idian_depths_corridor")
// 731631, 731632, 731641, 731642
public class Idian_Depths_CorridorAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			case 731631: // Cygnea To Idian Depths.
			case 731641: // Levinshor To Idian Depths.
				if (player.getLevel() >= 65) {
					TeleportService2.teleportTo(player, 210090000, 671.0f, 646.0f, 514.8738f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
				}
				else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Telepoter_Under_User);
				}
				break;
			case 731632: // Enshar To Idian Depths.
			case 731642: // Kaldor To Idian Depths.
				if (player.getLevel() >= 65) {
					TeleportService2.teleportTo(player, 220100000, 671.0f, 646.0f, 514.8738f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
				}
				else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Telepoter_Under_User);
				}
				break;
		}
	}
}
