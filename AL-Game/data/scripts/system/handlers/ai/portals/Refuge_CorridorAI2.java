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
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
@AIName("refuge_corridor")
public class Refuge_CorridorAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getLevel() >= 65) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Fortress_Entrance_In01);
		}
	}

	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (player.getPlayerGroup2() == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Fortress_Entrance_In02);
			return true;
		}
		if (dialogId == 10000) {
			switch (getNpcId()) {
				case 805613: // Krotan Refuge Corridor [Elyos]
				case 805627: // Krotan Refuge Corridor [Asmodians]
					TeleportService2.teleportTo(player, 400010000, 1425.239f, 1006.4445f, 2976.326f, (byte) 99);
					break;
				case 805614: // Miren Refuge Corridor [Elyos]
				case 805628: // Miren Refuge Corridor [Asmodians]
					TeleportService2.teleportTo(player, 400010000, 1164.388f, 2019.7821f, 2943.519f, (byte) 80);
					break;
				case 805615: // Kysis Refuge Corridor [Elyos]
				case 805629: // Kysis Refuge Corridor [Asmodians]
					TeleportService2.teleportTo(player, 400010000, 1740.073f, 1702.1339f, 3025.284f, (byte) 1);
					break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
}
