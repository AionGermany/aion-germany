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
package ai.instance.beshmundirTemple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI2;

@AIName("stormwing")
public class StormwingAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	protected List<Integer> percents = new ArrayList<Integer>();
	private Future<?> taskVoltaicStorm;
	private Future<?> taskSharpTwister;
	private Future<?> taskRootTwister;

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 90, 80, 75, 50, 10 });
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 90:
						bossUseSkill(18616);
						break;
					case 80:
						bossUseSkill(18615);
						break;
					case 75:
						bossUseSkill(18615);
						break;
					case 50:
						bossUseSkill(18616);
						break;
					case 10:
						bossUseSkill(18616);
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500086, getObjectId(), 0, 0);
			scheduleRespawn();
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleDied() {
		cancelTasks();
		super.handleDied();
		percents.clear();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500092, getObjectId(), 0, 0);
		despawnNpcs(getPosition().getWorldMapInstance().getNpcs(281794));
		despawnNpcs(getPosition().getWorldMapInstance().getNpcs(281796));
		despawnNpcs(getPosition().getWorldMapInstance().getNpcs(281798));
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		cancelTasks();
		super.handleBackHome();
		isHome.set(true);
	}

	/**
	 * Boss uses Skill
	 *
	 * @param skillId
	 */
	private void bossUseSkill(int skillId) {
		AI2Actions.useSkill(this, skillId);
	}

	// Root Twister.
	private void spawnrootTwister() {
		Npc stormwing = getPosition().getWorldMapInstance().getNpc(216183); // Stormwing.
		Npc rootTwister = getPosition().getWorldMapInstance().getNpc(281794); // Root Twister.
		if (stormwing != null && !stormwing.getLifeStats().isAlreadyDead()) {
			if (rootTwister == null) {
				spawn(281794, 539.695f, 1363.87f, 223.529f, (byte) 8);
				spawn(281794, 547.128f, 1354.55f, 223.529f, (byte) 16);
				spawn(281794, 557.449f, 1350.75f, 223.529f, (byte) 28);
				AI2Actions.useSkill(this, 18613); // Threshing Wind.
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500088, getObjectId(), 0, 0);
			}
			scheduleRespawn();
		}
	}

	// Sharp Twister.
	private void spawnsharpTwister() {
		Npc stormwing = getPosition().getWorldMapInstance().getNpc(216183); // Stormwing.
		Npc sharpTwister = getPosition().getWorldMapInstance().getNpc(281796); // Sharp Twister.
		if (stormwing != null && !stormwing.getLifeStats().isAlreadyDead()) {
			if (sharpTwister == null) {
				spawn(281796, 544.181f, 1380.58f, 223.529f, (byte) 108);
				spawn(281796, 572.159f, 1358.19f, 223.529f, (byte) 48);
				AI2Actions.useSkill(this, 18612); // Distant Wind.
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500087, getObjectId(), 0, 0);
			}
			scheduleRespawn();
		}
	}

	// Voltaic Storm.
	private void spawnvoltaicStorm() {
		Npc stormwing = getPosition().getWorldMapInstance().getNpc(216183); // Stormwing.
		Npc voltaicStorm = getPosition().getWorldMapInstance().getNpc(281798); // Voltaic Storm.
		if (stormwing != null && !stormwing.getLifeStats().isAlreadyDead()) {
			if (voltaicStorm == null) {
				spawn(281798, 553.692f, 1330.63f, 2230529f, (byte) 35);
				spawn(281798, 525.885f, 1348.14f, 223.529f, (byte) 13);
				AI2Actions.useSkill(this, 18617); // Voltaic Storm.
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500089, getObjectId(), 0, 0);
			}
			scheduleRespawn();
		}
	}

	private void cancelTasks() {
		if ((taskVoltaicStorm != null) && !taskVoltaicStorm.isDone()) {
			taskVoltaicStorm.cancel(true);
		}
		if ((taskSharpTwister != null) && !taskSharpTwister.isDone()) {
			taskSharpTwister.cancel(true);
		}
		if ((taskRootTwister != null) && !taskRootTwister.isDone()) {
			taskRootTwister.cancel(true);
		}
	}

	private void scheduleRespawn() {
		taskVoltaicStorm = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawnvoltaicStorm();
			}
		}, 150000); // Voltaic Storm are spawned every 2 Minutes.
		taskSharpTwister = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawnsharpTwister();
			}
		}, 300000); // Sharp Twister are spawned every 5 Minutes.
		taskRootTwister = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawnrootTwister();
			}
		}, 600000); // Root Twister are spawned every 10 Minutes.
	}

	private void despawnNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			npc.getController().onDelete();
		}
	}
}
