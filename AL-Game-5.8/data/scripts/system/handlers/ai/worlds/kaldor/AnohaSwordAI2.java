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
package ai.worlds.kaldor;

import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * Sword Script to Spawn Anoha
 *
 * @author Antraxx2K
 */
@AIName("anohasword")
// 804576, 804577
public class AnohaSwordAI2 extends NpcAI2 {

	public static int itemIdSealingStone = 185000215;
	private Future<?> taskSpawnAnoha;

	/*
	 * Delay Time to spawn Anoha in Minutes
	 */
	private int timeSpawnDelayAnoha = 30;

	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		switch (dialogId) {
			case 10000:
				if (player.getInventory().getItemCountByItemId(itemIdSealingStone) == 0) {
					// TODO send message to Player that he don't has the Stone in the Inventory
					return false;
				}

				// Anoha Sealing Stone
				if (!this.timerStartSpawnAnoha(player)) {
					return false;
				}

				this.timerStartSpawnAnoha(player);
				// spawn the Berserk Anoha Flag

				break;
		}
		AI2Actions.deleteOwner(this);

		return true;
	}

	// Beim aktivieren des Schwertes startet ein 30min Timer bis Bererk Anoha spawnt
	// Beim aktivieren des Schwertes wird die Flag gespawnt
	// Beim aktivieren des Schwertes bleibt das Item (Anoha Sealing Stone) erhalten
	// Beim aktivieren des Schwertes wird eine meldung für BEIDE RASSEN ausgegeben
	// 1402483 - Versiegelung aufheben
	// 1402503 - Rasenden Anoha beschwören

	// 1402501 - Im ausgewählten Bereich wurde die Beschwörung bereits abgeschlossen.
	// 1402502 - Die Beschwörung ist fehlgeschlagen.
	// 1402484 - Zur Entsiegelung wurde Anohas Versiegelungsstein verwendet.
	// 1402584 - In 30 Minuten wird der rasende Anoha beschworen.

	/**
	 * Check and Start Task for spawn Anoha in given Delay
	 *
	 * @param player
	 */
	private boolean timerStartSpawnAnoha(Player player) {
		// TODO: check Anoha is already Spawned

		// check timer is already running
		if ((this.taskSpawnAnoha != null) && !this.taskSpawnAnoha.isDone()) {
			return false;
		}

		// create new Task for Spawn Anoha
		this.taskSpawnAnoha = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// Spawn Anoha - npcId: 855263
				announceAnohaBoss();
				spawn(855263, 791.3753f, 488.7251f, 143.47517f, (byte) 0);
			};
		}, ((this.timeSpawnDelayAnoha * 60) * 1000));

		spawn(702618, 791.3753f, 488.7251f, 143.47517f, (byte) 8);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402483));

		return true;
	}

	private void announceAnohaBoss() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402503));
			}
		});
	}

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getInventory().getFirstItemByItemId(185000215) != null) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
		}
	}

}
