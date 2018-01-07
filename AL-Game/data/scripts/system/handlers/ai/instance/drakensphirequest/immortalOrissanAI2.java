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
package ai.instance.drakensphirequest;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI2;

/**
 * @author Falke_34
 */
@AIName("immortal_orissan_quest")
// 237230
public class immortalOrissanAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isStartEvent = new AtomicBoolean(false);

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 80) {
			if (isStartEvent.compareAndSet(false, true)) {
				scheduleSpawnExhaustedOrissan(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), this.getPosition().getHeading());
				AI2Actions.deleteOwner(this);
			}
		}
	}

	private void scheduleSpawnExhaustedOrissan(final float x, final float y, final float z, final byte h) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(237231, x, y, z, h); // Exhausted Orissan
			}
		}, 1000);
	}
}
