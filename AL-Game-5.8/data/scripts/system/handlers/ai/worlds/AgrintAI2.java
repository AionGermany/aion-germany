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
package ai.worlds;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("agrint")
public class AgrintAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isSpawned = new AtomicBoolean(false);

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		int msg = 0;
		switch (getNpcId()) {
			case 218862:
			case 218850:
				msg = 1401246;
				break;
			case 218863:
			case 218851:
				msg = 1401247;
				break;
			case 218864:
			case 218852:
				msg = 1401248;
				break;
			case 218865:
			case 218853:
				msg = 1401249;
				break;
		}
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, 2000);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private synchronized void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 50) {
			if (isSpawned.compareAndSet(false, true)) {
				int npcId;
				switch (getNpcId()) {
					case 218850:
					case 218851:
					case 218852:
					case 218853:
						npcId = getNpcId() + 320;
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						break;
					case 218862:
					case 218863:
					case 218864:
					case 218865:
						npcId = getNpcId() + 308;
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						rndSpawnInRange(npcId, Rnd.get(1, 2));
						break;
				}
			}
		}
	}

	private Npc rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		return (Npc) spawn(npcId, p.getX() + x1, p.getY() + y1, p.getZ(), (byte) 0);
	}

	@Override
	protected void handleBackHome() {
		isSpawned.set(false);
		super.handleBackHome();
	}

	private void spawnChests(int npcId) {
		rndSpawnInRange(npcId, Rnd.get(1, 6));
		rndSpawnInRange(npcId, Rnd.get(1, 6));
		rndSpawnInRange(npcId, Rnd.get(1, 6));
		rndSpawnInRange(npcId, Rnd.get(1, 6));
		rndSpawnInRange(npcId, Rnd.get(1, 6));
		rndSpawnInRange(npcId, Rnd.get(1, 6));
	}

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 218850:
				spawnChests(218874);
				break;
			case 218851:
				spawnChests(218876);
				break;
			case 218852:
				spawnChests(218878);
				break;
			case 218853:
				spawnChests(218880);
				break;
			case 218862:
				spawnChests(218882);
				break;
			case 218863:
				spawnChests(218884);
				break;
			case 218864:
				spawnChests(218886);
				break;
			case 218865:
				spawnChests(218888);
				break;
		}
		super.handleDied();
	}

	@Override
	public int modifyOwnerDamage(int damage) {
		return 1;
	}

	@Override
	public int modifyDamage(int damage) {
		return 1;
	}
}
