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
package ai.instance.museumKnowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @author Rinzler
 */
@AIName("IDEternity_03_Def_Boss_75") //246418
public class IDEternity_03_Def_Boss_75AI2 extends AggressiveNpcAI2 {

	private Future<?> phaseTask;
	private boolean canThink = true;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	private AtomicBoolean isStartedEvent = new AtomicBoolean(false);

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 90) {
			if (isStartedEvent.compareAndSet(false, true)) {
				spawn(246421, 408.66196f, 1013.1304f, 711.93115f, (byte) 0, 177); // IDEternity_03_Def_Boss_Energy_01.
				spawn(246422, 408.77426f, 1037.3873f, 711.90881f, (byte) 0, 179); // IDEternity_03_Def_Boss_Energy_02.
				spawn(246423, 386.68903f, 1037.3842f, 711.95770f, (byte) 0, 181); // IDEternity_03_Def_Boss_Energy_03.
				spawn(246424, 386.68146f, 1013.2744f, 711.93091f, (byte) 0, 183); // IDEternity_03_Def_Boss_Energy_04.
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
					// SkillEngine.getInstance().getSkill(getOwner(), -1, 60, getOwner()).useNoAnimationSkill();
					List<Player> players = getLifedPlayers();
					if (!players.isEmpty()) {
						int size = players.size();
						if (players.size() < 6) {
							for (Player p : players) {
								spawnDefBossSum01(p);
							}
						}
						else {
							int count = Rnd.get(6, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnDefBossSum01(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 20000, 40000);
	}

	private void spawnDefBossSum01(Player player) {
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if (x > 0 && y > 0 && z > 0) {
			spawn(246419, x, y, z, (byte) 0); // IDEternity_03_Def_Boss_Sum_01.
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (!isAlreadyDead()) {
						spawn(246419, x, y, z, (byte) 0); // IDEternity_03_Def_Boss_Sum_01.
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
			if (!PlayerActions.isAlreadyDead(player)) {
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
			deleteNpcs(instance.getNpcs(246419)); // IDEternity_03_Def_Boss_Sum_01.
			deleteNpcs(instance.getNpcs(246421)); // IDEternity_03_Def_Boss_Energy_01.
			deleteNpcs(instance.getNpcs(246422)); // IDEternity_03_Def_Boss_Energy_02.
			deleteNpcs(instance.getNpcs(246423)); // IDEternity_03_Def_Boss_Energy_03.
			deleteNpcs(instance.getNpcs(246424)); // IDEternity_03_Def_Boss_Energy_04.
		}
	}

	@Override
	protected void handleDied() {
		cancelPhaseTask();
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(246419)); // IDEternity_03_Def_Boss_Sum_01.
			deleteNpcs(p.getWorldMapInstance().getNpcs(246421)); // IDEternity_03_Def_Boss_Energy_01.
			deleteNpcs(p.getWorldMapInstance().getNpcs(246422)); // IDEternity_03_Def_Boss_Energy_02.
			deleteNpcs(p.getWorldMapInstance().getNpcs(246423)); // IDEternity_03_Def_Boss_Energy_03.
			deleteNpcs(p.getWorldMapInstance().getNpcs(246424)); // IDEternity_03_Def_Boss_Energy_04.
		}
		super.handleDied();
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
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
