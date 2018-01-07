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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
//import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

//import com.aionemu.gameserver.model.EmotionType;

/**
 * @author IXren
 * @reworked Himiko
 */

@AIName("linkgatefoundrybossportal")
public class LinkgateFoundryBossPortalAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		long keyCount = (player.getInventory().getFirstItemByItemId(185000196) != null ? player.getInventory().getFirstItemByItemId(185000196).getItemCount() : 0);
		switch (dialogId) {
			case 20007:
				if (keyCount >= 1) {
					player.getInventory().decreaseByItemId(185000196, 1);
					TeleportService2.teleportTo(player, 301270000, 212.75757f, 259.69754f, 313.6179f, (byte) 2, TeleportAnimation.BEAM_ANIMATION);
					despawnNpc(getPosition().getWorldMapInstance().getNpc(233898));
					spawn(234990, 244.41353f, 259.64795f, 312.30777f, (byte) 42);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402440));
				}
				break;
			case 20008:
				if (keyCount >= 5) {
					player.getInventory().decreaseByItemId(185000196, 5);
					TeleportService2.teleportTo(player, 301270000, 212.75757f, 259.69754f, 313.6179f, (byte) 2, TeleportAnimation.BEAM_ANIMATION);
					despawnNpc(getPosition().getWorldMapInstance().getNpc(233898));
					spawn(233898, 244.41353f, 259.64795f, 312.30777f, (byte) 42);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402441));
				}
				break;
			case 20009:
				if (keyCount >= 7) {
					player.getInventory().decreaseByItemId(185000196, 7);
					TeleportService2.teleportTo(player, 301270000, 212.75757f, 259.69754f, 313.6179f, (byte) 2, TeleportAnimation.BEAM_ANIMATION);
					despawnNpc(getPosition().getWorldMapInstance().getNpc(233898));
					spawn(234991, 244.41353f, 259.64795f, 312.30777f, (byte) 42);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402442));
				}
				break;
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
		return true;
	}

	// TODO
	// private void startWalker(Npc npc, String walkerId) {
	// npc.getSpawn().setWalkerId(walkerId);
	// WalkManager.startWalking((NpcAI2) npc.getAi2());
	// npc.setState(1);
	// PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	// }

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
}
