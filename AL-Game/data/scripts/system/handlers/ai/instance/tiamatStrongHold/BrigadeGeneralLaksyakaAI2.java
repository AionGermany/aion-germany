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
package ai.instance.tiamatStrongHold;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.CreatureType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;

import ai.AggressiveNpcAI2;

/**
 * @author Cheatkiller
 */
@AIName("brigadegenerallaksyaka")
public class BrigadeGeneralLaksyakaAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> skeletonTask;
	private boolean isFinalBuff;

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (Rnd.get(0, 100) < 3) {
			spawnSummon();
		}
		if (isHome.compareAndSet(true, false)) {
			startSkillTask();
		}
		if (!isFinalBuff && getOwner().getLifeStats().getHpPercentage() <= 25) {
			isFinalBuff = true;
			AI2Actions.useSkill(this, 20731);
		}
	}

	private void startSkillTask() {
		skeletonTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelTask();
				}
				else {
					startSkeletonEvent();
				}
			}
		}, 5000, 40000);
	}

	private void cancelTask() {
		if (skeletonTask != null && !skeletonTask.isCancelled()) {
			skeletonTask.cancel(true);
		}
	}

	private void startSkeletonEvent() {
		Npc tiamatEye = getPosition().getWorldMapInstance().getNpc(283089);
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!CreatureActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, tiamatEye, 40)) {
				players.add(player);
			}
		}
		Player player = !players.isEmpty() ? players.get(Rnd.get(players.size())) : null;
		SkillEngine.getInstance().applyEffectDirectly(20865, tiamatEye, player, 30000);
	}

	private void spawnSummon() {
		if (getPosition().getWorldMapInstance().getNpc(283115) == null) {
			rndSpawn(283115, 4);
		}
	}

	private void rndSpawn(int npcId, int count) {
		for (int i = 0; i < count; i++) {
			SpawnTemplate template = rndSpawnInRange(npcId, 10);
			SpawnEngine.spawnObject(template, getPosition().getInstanceId());
		}
	}

	private SpawnTemplate rndSpawnInRange(int npcId, int dist) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * dist);
		float y1 = (float) (Math.sin(Math.PI * direction) * dist);
		return SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, getPosition().getX() + x1, getPosition().getY() + y1, getPosition().getZ(), getPosition().getHeading());
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		cancelTask();
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		getOwner().setNpcType(CreatureType.PEACE.getId());
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelTask();
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		cancelTask();
		isFinalBuff = false;
		isHome.set(true);
	}
}
