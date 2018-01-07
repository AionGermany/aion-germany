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
package ai.instance.argentManor;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @author
 */
@AIName("wary_alarm")
// 237186
public class WaryAlarmAI2 extends AggressiveNpcAI2 {

	private boolean canThink = true;
	private AtomicBoolean startedEvent = new AtomicBoolean(false);

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			final WorldPosition p = getPosition();
			if (MathUtil.getDistance(getOwner(), player) <= 23) {
				if (startedEvent.compareAndSet(false, true)) {
					canThink = false;
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1401350, 0); // TODO Shout
					spawn(237187, p.getX(), p.getY(), p.getZ(), (byte) 0); // TODO ID Armored Hetgolem
					getOwner().setState(1);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							if (!isAlreadyDead()) {
								despawn();
							}
						}
					}, 3000);
				}
			}
		}
	}

	private void despawn() {
		AI2Actions.deleteOwner(this);
	}
}
