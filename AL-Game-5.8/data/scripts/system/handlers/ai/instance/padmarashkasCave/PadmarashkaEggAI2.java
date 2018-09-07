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
package ai.instance.padmarashkasCave;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.NoActionAI2;

/**
 * @author Ritsu
 */
@AIName("padmarashkaegg")
public class PadmarashkaEggAI2 extends NoActionAI2 {

	boolean isSmallEggProtectorSpawned = false;
	boolean isHugeEggProtectorSpawned = false;
	private Npc protector = null;

	@Override
	protected void handleDied() {
		if (protector != null && !CreatureActions.isAlreadyDead(protector)) {
			SkillEngine.getInstance().getSkill(protector, 20176, 55, protector).useNoAnimationSkill(); // apply wrath buff
		}
		super.handleDied();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (!isSmallEggProtectorSpawned && this.getNpcId() == 282613) {
			switch (Rnd.get(1, 6)) {
				case 1:
					protector = (Npc) spawn(282715, 579.415f, 168.109f, 66.000f, (byte) 0);
					break;
				case 2:
					protector = (Npc) spawn(282715, 581.316f, 157.520f, 66.000f, (byte) 0);
					break;
				case 3:
					protector = (Npc) spawn(282715, 575.073f, 147.338f, 66.000f, (byte) 0);
					break;
				case 4:
					protector = (Npc) spawn(282715, 585.119f, 150.989f, 66.000f, (byte) 0);
					break;
				case 5:
					protector = (Npc) spawn(282716, 581.141f, 148.342f, 66.000f, (byte) 0);
					break;
				case 6:
					protector = (Npc) spawn(282716, 584.240f, 142.233f, 66.000f, (byte) 0);
					break;
			}
			isSmallEggProtectorSpawned = true;
		}
		else if (!isHugeEggProtectorSpawned && this.getNpcId() == 282614) {
			SpawnEliteCommander(); // Random spawn SpawnEliteCommander to protect Egg
			isHugeEggProtectorSpawned = true;
		}
	}

	private void SpawnEliteCommander() {
		SpawnTemplate template = rndSpawnInRange(282712);
		protector = (Npc) SpawnEngine.spawnObject(template, getPosition().getInstanceId());
	}

	private SpawnTemplate rndSpawnInRange(int npcId) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * 5);
		float y1 = (float) (Math.sin(Math.PI * direction) * 5);
		return SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, getPosition().getX() + x1, getPosition().getY() + y1, getPosition().getZ(), getPosition().getHeading());
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (this.getNpcId()) {
			case 282613:
				smallEggSpawn();
				break;
			case 282614:
				hugeEggSpawn();
				break;
		}
	}

	private void smallEggSpawn() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (getOwner() != null && !isAlreadyDead()) {
					AI2Actions.deleteOwner(PadmarashkaEggAI2.this);
					spawn(282616, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
				}

			}
		}, 60000); // TODO: Need right value
	}

	private void hugeEggSpawn() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (getOwner() != null && !isAlreadyDead()) {
					AI2Actions.deleteOwner(PadmarashkaEggAI2.this);
					spawn(282620, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
				}
			}
		}, 120000); // TODO: Need right value
	}
}
