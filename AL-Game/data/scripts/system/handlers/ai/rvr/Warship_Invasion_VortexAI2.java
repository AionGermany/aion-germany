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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AI2Request;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

@AIName("F6_INVADE_Direct_Portal_Start") // 833766
public class Warship_Invasion_VortexAI2 extends NpcAI2 {

	private final int CANCEL_DIALOG_METERS = 10;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 833766: // F6_INVADE_Direct_Portal_Start.
				startLifeTask();
				break;
		}
	}

	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AI2Actions.deleteOwner(Warship_Invasion_VortexAI2.this);
			}
		}, 300000); // 5 Minutes.
	}

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getLevel() >= 66) {
			// Do you want to travel through the Legion Rift ?
			AI2Actions.addRequest(this, player, SM_QUESTION_WINDOW.STR_ASK_PASS_BY_RVR_DIRECT_PORTAL, getOwner().getObjectId(), CANCEL_DIALOG_METERS, new AI2Request() {

				private boolean decisionTaken = false;

				@Override
				public void acceptRequest(Creature requester, Player responder) {
					if (!decisionTaken) {
						switch (getNpcId()) {
							case 833766: // F6_INVADE_Direct_Portal_Start.
								if (responder.getCommonData().getRace() == Race.ELYOS) {
									transferInvasion1(responder);
								}
								else if (responder.getCommonData().getRace() == Race.ASMODIANS) {
									transferInvasion2(responder);
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
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_INVADE_DIRECT_PORTAL_LEVEL_LIMIT);
		}
	}

	@Override
	protected void handleDialogFinish(Player player) {
	}

	/**
	 * Elyos.
	 */
	private void transferInvasion1(Player responder) {
		TeleportService2.teleportTo(responder, 220110000, 1549.2898f, 2402.2078f, 195.69263f, (byte) 110);
	}

	/**
	 * Asmodians.
	 */
	private void transferInvasion2(Player responder) {
		TeleportService2.teleportTo(responder, 210100000, 1352.1334f, 1761.8057f, 325.0038f, (byte) 1);
	}
}
