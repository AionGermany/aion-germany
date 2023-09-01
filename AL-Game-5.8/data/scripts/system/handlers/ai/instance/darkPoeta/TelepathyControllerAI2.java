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
package ai.instance.darkPoeta;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

import ai.AggressiveNpcAI2;

/**
 * @author Ritsu
 */
@AIName("telepathycontroller")
public class TelepathyControllerAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isStart50Event = new AtomicBoolean(false);
	private AtomicBoolean isStart10Event = new AtomicBoolean(false);

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 50) {
			if (isStart50Event.compareAndSet(false, true)) {
				helper();
			}
		}
		else if (hpPercentage <= 10) {
			if (isStart10Event.compareAndSet(false, true)) {
				helper();
			}
		}
	}

	@Override
	protected void handleBackHome() {
		isStart50Event.set(false);
		isStart10Event.set(false);
		super.handleBackHome();
	}

	private void helper() {
		if (getPosition().isSpawned() && !isAlreadyDead()) {
			for (int i = 0; i < 1; i++) {
				int distance = Rnd.get(7, 10);
				int nrNpc = Rnd.get(1, 2);
				switch (nrNpc) {
					case 1:
						nrNpc = 281150; // Anuhart Escort.
						break;
					case 2:
						nrNpc = 281334; // Bionic Clodworm.
						break;
				}
				rndSpawnInRange(nrNpc, distance);
			}
		}
	}

	private void rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		spawn(npcId, getPosition().getX() + x1, getPosition().getY() + y1, getPosition().getZ(), (byte) 0);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
}
