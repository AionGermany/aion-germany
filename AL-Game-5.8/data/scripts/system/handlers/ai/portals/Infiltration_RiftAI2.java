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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AI2Request;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
@AIName("infiltration_rift")
public class Infiltration_RiftAI2 extends NpcAI2 {

	private final int CANCEL_DIALOG_METERS = 10;

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getLevel() >= 55) {
			AI2Actions.addRequest(this, player, SM_QUESTION_WINDOW.STR_ASK_PASS_BY_LEGION_DIRECT_PORTAL, getOwner().getObjectId(),
				CANCEL_DIALOG_METERS, new AI2Request() {

					private boolean decisionTaken = false;

					@Override
					public void acceptRequest(Creature requester, Player responder) {
						if (!decisionTaken) {
							switch (getNpcId()) {
								case 702721: // Infiltration Rift
									if (responder.getCommonData().getRace() == Race.ELYOS) {
										switch (Rnd.get(1, 9)) {
											case 1:
												TeleportService2.teleportTo(responder, 220080000, 152.02060f, 2572.3533f, 134.02466f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 2:
												TeleportService2.teleportTo(responder, 220080000, 1305.3157f, 2422.7840f, 279.51456f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 3:
												TeleportService2.teleportTo(responder, 220080000, 2307.4660f, 1204.8960f, 325.00000f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 4:
												TeleportService2.teleportTo(responder, 220080000, 172.35520f, 1450.8087f, 306.80670f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 5:
												TeleportService2.teleportTo(responder, 220080000, 1076.3921f, 620.23596f, 218.67900f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 6:
												TeleportService2.teleportTo(responder, 220080000, 2370.0980f, 193.97507f, 242.38257f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 7:
												TeleportService2.teleportTo(responder, 220080000, 2849.7524f, 2445.2960f, 176.62500f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 8:
												TeleportService2.teleportTo(responder, 220080000, 104.40256f, 156.43721f, 165.69632f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 9:
												TeleportService2.teleportTo(responder, 220080000, 1742.1217f, 1212.8824f, 211.69537f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
										}
									}
									else if (responder.getCommonData().getRace() == Race.ASMODIANS) {
										switch (Rnd.get(1, 9)) {
											case 1:
												TeleportService2.teleportTo(responder, 210070000, 2669.0242f, 438.01553f, 576.44300f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 2:
												TeleportService2.teleportTo(responder, 210070000, 1695.2113f, 118.30494f, 541.34860f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 3:
												TeleportService2.teleportTo(responder, 210070000, 780.76843f, 1797.6338f, 457.70422f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 4:
												TeleportService2.teleportTo(responder, 210070000, 2108.6462f, 1445.7365f, 428.34204f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 5:
												TeleportService2.teleportTo(responder, 210070000, 1928.2786f, 2594.9610f, 315.02524f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 6:
												TeleportService2.teleportTo(responder, 210070000, 783.88990f, 2369.2110f, 296.66055f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 7:
												TeleportService2.teleportTo(responder, 210070000, 423.68884f, 861.77814f, 415.61945f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 8:
												TeleportService2.teleportTo(responder, 210070000, 2911.7507f, 2899.1367f, 270.23444f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
											case 9:
												TeleportService2.teleportTo(responder, 210070000, 1990.0380f, 1688.9974f, 195.12500f,
													(byte) 0, TeleportAnimation.BEAM_ANIMATION);
												break;
										}
									}
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
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_DIRECT_PORTAL_LEVEL_LIMIT);
		}
	}

	@Override
	protected void handleDialogFinish(Player player) {
	}
}
