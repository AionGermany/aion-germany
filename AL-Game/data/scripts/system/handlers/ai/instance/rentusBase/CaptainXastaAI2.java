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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("captain_xasta")
// 217310, 236297
public class CaptainXastaAI2 extends AggressiveNpcAI2 {

	private Future<?> phaseTask;
	private boolean canThink = true;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	private AtomicBoolean isStartedEvent = new AtomicBoolean(false);

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			sendMsg(1500388);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 90) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		}
		if (hpPercentage <= 70) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		}
		if (hpPercentage <= 50) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		}
		if (hpPercentage <= 30) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		}
		if (hpPercentage <= 10) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		}
	}

	private void startPhaseTask() {
		phaseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelPhaseTask();
				}
				else {
					SkillEngine.getInstance().getSkill(getOwner(), 19729, 60, getOwner()).useNoAnimationSkill(); // Mana Treatment V.
					sendMsg(1500392);
					List<Player> players = getLifedPlayers();
					if (!players.isEmpty()) {
						int size = players.size();
						if (players.size() < 6) {
							for (Player p : players) {
								spawnInhibitorSikar(p);
							}
						}
						else {
							int count = Rnd.get(1, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnInhibitorSikar(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 20000, 40000);
	}

	private void spawnInhibitorSikar(Player player) {
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if (x > 0 && y > 0 && z > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (!isAlreadyDead()) {
						spawn(282604, x, y, z, (byte) 0); // Inhibitor Sikar.
					}
				}
			}, 3000);
		}
	}

	@Override
	public boolean canThink() {
		return canThink;
	}

	private List<Player> getLifedPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!CreatureActions.isAlreadyDead(player)) {
				players.add(player);
			}
		}
		return players;
	}

	private void cancelPhaseTask() {
		if (phaseTask != null && !phaseTask.isDone()) {
			phaseTask.cancel(true);
		}
	}

	private void deleteHelpers() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			deleteNpcs(instance.getNpcs(282604)); // Inhibitor Sikar.
		}
	}

	@Override
	protected void handleDied() {
		deleteHelpers();
		cancelPhaseTask();
		sendMsg(1500390);
		sendMsg(1500391);
		super.handleDied();
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), 0, 0);
	}

	@Override
	protected void handleDespawned() {
		cancelPhaseTask();
		super.handleDespawned();
	}

	@Override
	protected void handleBackHome() {
		canThink = true;
		cancelPhaseTask();
		deleteHelpers();
		isAggred.set(false);
		isStartedEvent.set(false);
		super.handleBackHome();
	}
}
