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
package ai.instance.shugoImperialTomb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;
import com.aionemu.gameserver.world.World;

import ai.AggressiveNpcAI2;

/**
 * @author Swig
 */
@AIName("captain_lediar")
// 219531
public class CaptainLediarAI2 extends AggressiveNpcAI2 {

	private WalkerTemplate template;
	private boolean canThink = true;
	private final List<Integer> spawnedNpc = new ArrayList<Integer>();
	private AtomicBoolean isSpawnedHelpers = new AtomicBoolean(false);
	private final static int[] npc_ids = { 831251, 831250, 831305 };

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage == 75) {
			if (isSpawnedHelpers.compareAndSet(false, true)) {
				spawnHelpers();
			}
		}
	}

	private void spawnHelpers() {
		if (!isAlreadyDead()) {
			int count = 4;
			int npcId = 219509;
			for (int i = 0; i < count; i++) {
				rndSpawnInRange(npcId, 2);
			}
		}
	}

	private void rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x = (float) (Math.cos(Math.PI * direction) * distance);
		float y = (float) (Math.sin(Math.PI * direction) * distance);
		spawn(npcId, getPosition().getX() + x, getPosition().getY() + y, getPosition().getZ(), (byte) 0);
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		removeHelpersSpawn();
	}

	private void removeHelpersSpawn() {
		for (Integer object : spawnedNpc) {
			VisibleObject npc = World.getInstance().findVisibleObject(object);
			if (npc != null && npc.isSpawned()) {
				npc.getController().onDelete();
			}
		}
	}

	@Override
	public int modifyOwnerDamage(int damage) {
		return damage = 1;
	}

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleSpawned() {
		canThink = false;
		super.handleSpawned();
	}

	@Override
	protected void handleMoveArrived() {
		super.handleMoveArrived();
		String walkerId = getOwner().getSpawn().getWalkerId();
		if (walkerId != null) {
			template = DataManager.WALKER_DATA.getWalkerTemplate(walkerId);
		}
		int point = getOwner().getMoveController().getCurrentPoint();
		if (template.getRouteSteps().size() - 1 == point) {
			getSpawnTemplate().setWalkerId(null);
			WalkManager.stopWalking(this);
			canThink = true;
			addHate();
		}
	}

	private void addHate() {
		EmoteManager.emoteStopAttacking(getOwner());
		for (int npc_id : npc_ids) {
			Npc tower = getOwner().getPosition().getWorldMapInstance().getNpc(npc_id);
			if (tower != null && !tower.getLifeStats().isAlreadyDead()) {
				switch (npc_id) {
					case 831251:
						getOwner().getAggroList().addHate(tower, 100);
					case 831250:
						getOwner().getAggroList().addHate(tower, 100);
					case 831305:
						getOwner().getAggroList().addHate(tower, 100);
						break;
				}
			}
		}
	}
}
