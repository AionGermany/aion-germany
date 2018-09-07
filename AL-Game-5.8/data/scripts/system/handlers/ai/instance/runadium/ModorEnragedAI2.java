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

package ai.instance.runadium;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

import ai.AggressiveNpcAI2;

/**
 * @author Ever
 */
@AIName("modorenraged")
public class ModorEnragedAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	protected List<Integer> percents = new ArrayList<Integer>();
	private boolean canThink = true;
	private Future<?> skillTask;
	private Future<?> TeleTask;

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
		if (isHome.compareAndSet(true, false)) {
			sendMsg(1500743);
			startSkillTask();
			startTeleportTask();
		}
	}

	private void startTeleportTask() {
		TeleTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelTask();
				}
				else {
					TeleportRandomLoc();
				}
			}
		}, 120000, 130000);
	}

	private void TeleportRandomLoc() {
		switch (Rnd.get(1, 4)) {
			case 1:
				Teleport1();
				break;
			case 2:
				Teleport2();
				break;
			case 3:
				Teleport3();
				break;
			case 4:
				Teleport4();
				break;
		}
	}

	private void cancelTask() {
		if (TeleTask != null && !TeleTask.isCancelled()) {
			TeleTask.cancel(true);
		}
	}

	private void startSkillTask() {
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelTask1();
				}
				else {
					chooseRandomEvent();
				}
			}
		}, 4000, 50000);
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 99:
						Scream();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void cancelTask1() {
		if (skillTask != null && !skillTask.isCancelled()) {
			skillTask.cancel(true);
		}
	}

	private void chooseRandomEvent() {
		int rand = Rnd.get(0, 3);
		if (rand == 0) {
			Anger();
		}
		if (rand == 1) {
			Scream();
		}
		if (rand == 2) {
			Roar();
		}
		else {
			Storm();
		}
	}

	private void Roar() {
		AI2Actions.targetSelf(ModorEnragedAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21269, 55, getOwner()).useNoAnimationSkill();
	}

	private void Scream() {
		AI2Actions.targetSelf(ModorEnragedAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21268, 55, getOwner()).useNoAnimationSkill();
	}

	private void Anger() {
		AI2Actions.targetSelf(ModorEnragedAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21171, 55, getOwner()).useNoAnimationSkill();
	}

	private void Storm() {
		AI2Actions.targetSelf(ModorEnragedAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21173, 55, getOwner()).useNoAnimationSkill();
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelTask();
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		isHome.set(true);
		cancelTask();
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		cancelTask();
	}

	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), 0, 0);
	}

	private void Teleport1() {
		AI2Actions.targetSelf(ModorEnragedAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 65, getOwner()).useNoAnimationSkill();
		sendMsg(1500741);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().updatePosition(getOwner(), 255, 293, 253, (byte) 22);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
				spawn(284382, 266.879f, 247.496f, 242.03f, (byte) 45);
				spawn(284663, 246.349f, 247.481f, 242.01f, (byte) 15);
				spawn(284660, 257.405f, 243.156f, 241.91f, (byte) 31);
			}
		}, 2000);
	}

	private void Teleport2() {
		AI2Actions.targetSelf(ModorEnragedAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 65, getOwner()).useNoAnimationSkill();
		sendMsg(1500741);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().updatePosition(getOwner(), 284, 262, 248, (byte) 22);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
				spawn(284661, 266.879f, 247.496f, 242.03f, (byte) 45);
				spawn(284662, 246.349f, 247.481f, 242.01f, (byte) 15);
				spawn(284659, 257.405f, 243.156f, 241.91f, (byte) 31);
			}
		}, 2000);
	}

	private void Teleport3() {
		AI2Actions.targetSelf(ModorEnragedAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 65, getOwner()).useNoAnimationSkill();
		sendMsg(1500741);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().updatePosition(getOwner(), 271, 230, 251, (byte) 22);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
				spawn(284382, 266.879f, 247.496f, 242.03f, (byte) 45);
				spawn(284663, 246.349f, 247.481f, 242.01f, (byte) 15);
				spawn(284660, 257.405f, 243.156f, 241.91f, (byte) 31);
			}
		}, 2000);
	}

	private void Teleport4() {
		AI2Actions.targetSelf(ModorEnragedAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 65, getOwner()).useNoAnimationSkill();
		sendMsg(1500741);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().updatePosition(getOwner(), 240, 235, 251, (byte) 22);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
				spawn(284661, 266.879f, 247.496f, 242.03f, (byte) 45);
				spawn(284662, 246.349f, 247.481f, 242.01f, (byte) 15);
				spawn(284659, 257.405f, 243.156f, 241.91f, (byte) 31);
			}
		}, 2000);
	}
}
