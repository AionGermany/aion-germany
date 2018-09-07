/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package ai.instance.libraryKnowledge;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;

import ai.AggressiveNpcAI2;

@AIName("high_daeva")
// 857785, 857786, 857787, 857788
public class HighDaevaAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean check = new AtomicBoolean(true);
	private int nextNpc = 0;

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {

		if (nextNpc == 0) {
			return;
		}

		if (hpPercentage <= 80) {
			if (check.compareAndSet(true, false)) {
				getOwner().getController().cancelCurrentSkill();
				if (getNpcId() == 857788) { // WIND
					AI2Actions.useSkill(this, 22969);
				}
				else if (getNpcId() == 857787) { // EARTH
					AI2Actions.useSkill(this, 22944);
				}
				else if (getNpcId() == 857786) { // WATER
					AI2Actions.useSkill(this, 22945);
				}
				else {
					AI2Actions.useSkill(this, 22943); // FIRE
				}
				AI2Actions.useSkill(this, 22936);
				SpawnTemplate spawn1 = SpawnEngine.addNewSpawn(301570000, nextNpc, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0, 0);
				SpawnEngine.spawnObject(spawn1, getOwner().getInstanceId());
				AI2Actions.deleteOwner(this);
			}
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 857788: // High Daeva of Wind
				nextNpc = 857787; // High Daeva of Earth
				break;
			case 857787: // High Daeva of Earth
				nextNpc = 857786; // High Daeva of Water
				break;
			case 857786: // High Daeva of Water
				nextNpc = 857785; // High Daeva of Fire
				break;
		}
	}
}
