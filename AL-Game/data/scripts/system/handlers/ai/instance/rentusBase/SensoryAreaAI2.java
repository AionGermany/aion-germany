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
package ai.instance.rentusBase;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.MathUtil;

import ai.AggressiveNpcAI2;

@AIName("sensory_area")
// 206180
public class SensoryAreaAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean startedEvent = new AtomicBoolean(false);

	@Override
	public void think() {
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= 10) {
				if (startedEvent.compareAndSet(false, true)) {
					switch (player.getWorldId()) {
						case 300280000: // Rentus Base
							NpcShoutsService.getInstance().sendMsg(getOwner(), 1402776, getObjectId(), 0, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {

								@Override
								public void run() {
									spawn(217309, 445.6442f, 439.13187f, 168.64172f, (byte) 40);
								}
							}, 10000);
							NpcShoutsService.getInstance().sendMsg(getOwner(), 1402775, 5000);
							AI2Actions.deleteOwner(SensoryAreaAI2.this);
							break;
					}
					switch (player.getWorldId()) {
						case 300620000: // [Occupied] Rentus Base 4.8
							NpcShoutsService.getInstance().sendMsg(getOwner(), 1402776, getObjectId(), 0, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {

								@Override
								public void run() {
									spawn(236296, 445.6442f, 439.13187f, 168.64172f, (byte) 40);
								}
							}, 10000);
							NpcShoutsService.getInstance().sendMsg(getOwner(), 1402775, 5000);
							AI2Actions.deleteOwner(SensoryAreaAI2.this);
							break;
					}
				}
			}
		}
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}

	@Override
	protected AIAnswer pollInstance(AIQuestion question) {
		switch (question) {
			case SHOULD_DECAY:
				return AIAnswers.NEGATIVE;
			case SHOULD_RESPAWN:
				return AIAnswers.NEGATIVE;
			case SHOULD_REWARD:
				return AIAnswers.NEGATIVE;
			default:
				return null;
		}
	}
}
