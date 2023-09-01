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
package ai.instance.theobomosLab;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

import ai.AggressiveNpcAI2;

/**
 * @author Ritsu
 */
@AIName("triroan_summon")
public class TriroansSummonAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isDestroyed = new AtomicBoolean(false);
	private int walkPosition;
	private int helperSkill;

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 280975:
				walkPosition = 3;
				helperSkill = 18493;
				break;
			case 280976:
				walkPosition = 4;
				helperSkill = 18492;
				break;
			case 280977:
				walkPosition = 2;
				helperSkill = 18485;
				break;
			case 280978:
				walkPosition = 5;
				helperSkill = 18491;
				break;
		}
	}

	@Override
	protected void handleMoveArrived() {
		super.handleMoveArrived();
		int point = getOwner().getMoveController().getCurrentPoint();
		if (walkPosition == point) {
			if (isDestroyed.compareAndSet(false, true)) {
				getSpawnTemplate().setWalkerId(null);
				WalkManager.stopWalking(this);
				useSkill();
				startDespawnTask();
			}
		}
	}

	private synchronized void useSkill() {
		Npc boss = getPosition().getWorldMapInstance().getNpc(214669);
		if (boss != null && checkLocation(getOwner()) && !boss.getLifeStats().isAlreadyDead()) {
			SkillEngine.getInstance().getSkill(boss, helperSkill, 50, boss).useSkill();
		}
		else {
			checkSkillUse(boss);
		}
	}

	private void checkSkillUse(Npc boss) {
		if (boss != null && checkDistance() == 0 && !boss.getLifeStats().isAlreadyDead()) {
			if (!boss.isCasting()) {
				SkillEngine.getInstance().getSkill(boss, helperSkill, 50, boss).useSkill();
			}
			else {
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						Npc boss = getPosition().getWorldMapInstance().getNpc(214669);
						if (boss != null && checkDistance() == 0 && !boss.getLifeStats().isAlreadyDead()) {
							checkSkillUse(boss);
						}
					}
				}, 5000);
			}
		}
	}

	private boolean checkLocation(Npc npc) {
		if (checkDistance() == 1 && npc.getNpcId() == 280975) {
			return true;
		}
		else if (checkDistance() == 2 && npc.getNpcId() == 280976) {
			return true;
		}
		else if (checkDistance() == 3 && npc.getNpcId() == 280977) {
			return true;
		}
		else if (checkDistance() == 4 && npc.getNpcId() == 280978) {
			return true;
		}
		else {
			return false;
		}
	}

	public int checkDistance() {
		Npc boss = getPosition().getWorldMapInstance().getNpc(214669);
		if (MathUtil.getDistance(boss, 624.002f, 474.241f, 196.160f) <= 5) {
			return 1;
		}
		else if (MathUtil.getDistance(boss, 623.23f, 502.715f, 196.087f) <= 5) {
			return 2;
		}
		else if (MathUtil.getDistance(boss, 579.943f, 500.999f, 196.604f) <= 5) {
			return 3;
		}
		else if (MathUtil.getDistance(boss, 578.323f, 475.784f, 196.463f) <= 5) {
			return 4;
		}
		else {
			return 0;
		}
	}

	private void startDespawnTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AI2Actions.deleteOwner(TriroansSummonAI2.this);
			}
		}, 3000);
	}
}
