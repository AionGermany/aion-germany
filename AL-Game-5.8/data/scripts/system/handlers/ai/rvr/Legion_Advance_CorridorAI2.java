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
package ai.rvr;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AI2Request;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

@AIName("direct_portal") // 703093, 703094, 703095, 703096
public class Legion_Advance_CorridorAI2 extends NpcAI2 {

	private final int CANCEL_DIALOG_METERS = 10;

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getLevel() >= 45) {
			AI2Actions.addRequest(this, player, SM_QUESTION_WINDOW.STR_ASK_PASS_BY_CHAOS_DIRECT_PORTAL, getOwner().getObjectId(), CANCEL_DIALOG_METERS, new AI2Request() {

				private boolean decisionTaken = false;

				@Override
				public void acceptRequest(Creature requester, Player responder) {
					if (!decisionTaken) {
						switch (getNpcId()) {
							case 703093: // Directportal_Start_LF3_DF3 In
								transferRvrElyos1(responder);
								break;
							case 703094: // Directportal_Start_LF3_LF3 In
								transferRvrElyos2(responder);
								break;
							case 703095: // Directportal_Start_DF3_LF3 In
								transferRvrAsmodians1(responder);
								break;
							case 703096: // Directportal_Start_DF3_DF3 In
								transferRvrAsmodians2(responder);
								break;
						}
						decisionTaken = true;
					}
				}

				@Override
				public void denyRequest(Creature requester, Player responder) {
					decisionTaken = true;
				}
			});
		}
		else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_INVADE_DIRECT_PORTAL_LEVEL_LIMIT);
		}
	}

	@Override
	protected void handleDialogFinish(Player player) {
	}

	/**
	 * Elyos.
	 */
	private void transferRvrElyos1(Player responder) {
		TeleportService2.teleportTo(responder, 220040000, 720.6933f, 2137.6619f, 305.9253f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
		PacketSendUtility.playerSendPacketTime(responder, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL_OPEN_NOTICE, 15000);
	}

	private void transferRvrElyos2(Player responder) {
		TeleportService2.teleportTo(responder, 210040000, 1370.1682f, 671.9592f, 180.76796f, (byte) 12, TeleportAnimation.BEAM_ANIMATION);
		PacketSendUtility.playerSendPacketTime(responder, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL_OPEN_NOTICE, 15000);
	}

	/**
	 * Asmodians.
	 */
	private void transferRvrAsmodians1(Player responder) {
		TeleportService2.teleportTo(responder, 210040000, 862.318f, 318.10703f, 139.89037f, (byte) 6, TeleportAnimation.BEAM_ANIMATION);
		PacketSendUtility.playerSendPacketTime(responder, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL_OPEN_NOTICE, 15000);
	}

	private void transferRvrAsmodians2(Player responder) {
		TeleportService2.teleportTo(responder, 220040000, 790.0702f, 1434.9897f, 312.33909f, (byte) 66, TeleportAnimation.BEAM_ANIMATION);
		PacketSendUtility.playerSendPacketTime(responder, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL_OPEN_NOTICE, 15000);
	}
}
