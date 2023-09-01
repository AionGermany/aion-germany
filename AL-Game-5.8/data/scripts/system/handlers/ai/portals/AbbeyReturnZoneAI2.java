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
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Himiko
 */
@AIName("AbbeyPortal")
// 209676, 209677
public class AbbeyReturnZoneAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		// Abbey Return Stone (30 days)
		long AbbeyStoneEly = player.getInventory().getItemCountByItemId(164000335);
		long AbbeyStoneAsmo = player.getInventory().getItemCountByItemId(164000336);
		switch (dialogId) {
			case 10000: // 209676, 209677
				if (AbbeyStoneEly >= 1 || AbbeyStoneAsmo >= 1) {
					TeleportAbbey(player);
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402673));
				}
				break;
		}
		return true;
	}

	public static void TeleportAbbey(Player player) {
		if (player.getRace() == Race.ELYOS) {
			TeleportService2.teleportTo(player, 130090000, 247.07394f, 224.17471f, 129.3827f, (byte) 30, TeleportAnimation.BEAM_ANIMATION);
		}
		else {
			TeleportService2.teleportTo(player, 140010000, 285.88828f, 266.15005f, 96.48745f, (byte) 60, TeleportAnimation.BEAM_ANIMATION);
		}
	}
}
