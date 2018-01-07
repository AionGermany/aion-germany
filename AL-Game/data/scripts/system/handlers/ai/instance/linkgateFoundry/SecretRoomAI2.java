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
package ai.instance.linkgateFoundry;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Himiko
 */

@AIName("secretroom")
public class SecretRoomAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	public boolean onDialogSelect(Player player, int dialogId) {
		switch (dialogId) {
			case 10000:
				TeleportService2.teleportTo(player, 301270000, 170.84601f, 258.30237f, 312.6399f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
				break;
			case 10001:
				TeleportService2.teleportTo(player, 301270000, 170.84601f, 258.30237f, 353.1993f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
				break;
			case 10002:
				TeleportService2.teleportTo(player, 301270000, 170.84601f, 258.30237f, 393.7201f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
				break;
		}
		return true;
	}
}
