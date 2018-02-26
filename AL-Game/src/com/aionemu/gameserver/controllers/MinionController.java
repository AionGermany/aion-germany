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
package com.aionemu.gameserver.controllers;

import com.aionemu.gameserver.model.gameobjects.Minion;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 */
public class MinionController extends VisibleObjectController<Minion> {

	@Override
	public void see(VisibleObject object) {
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
	}

	public static class MinionUpdateTask implements Runnable {

		private final Player player;
		private long startTime = 0;

		public MinionUpdateTask(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			if (startTime == 0) {
				startTime = System.currentTimeMillis();
			}

			try {
				Minion minion = player.getMinion();
				if (minion == null) {
					throw new IllegalStateException("Minion is null");
				}

				int currentPoints = 0;
				//boolean saved = false;

				if (currentPoints < 9000) {
					//PacketSendUtility.sendPacket(player, new SM_MINIONS(minion, 4, 0));
				}
			}
			catch (Exception ex) {
				//player.getController().cancelTask(TaskId.MINION_UPDATE);
			}
		}
	}
}