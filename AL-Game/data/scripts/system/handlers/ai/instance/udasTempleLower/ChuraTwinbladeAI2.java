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
package ai.instance.udasTempleLower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.controllers.attack.AggroInfo;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI2;

/**
 * Anvilface BossScript
 *
 * @author Antraxx
 */
@AIName("churatwinblade")
public class ChuraTwinbladeAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	protected List<Integer> percents = new ArrayList<Integer>();
	private boolean canThink = true;

	@Override
	public boolean canThink() {
		return canThink;
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 50 });
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 50:
						aggroChange();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void aggroChange() {
		canThink = false;
		setStateIfNot(AIState.WALKING);
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500029, getObjectId(), 0, 0);
		AI2Actions.useSkill(ChuraTwinbladeAI2.this, 18624);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// normal continue
				if (isHome.equals(true) || isAlreadyDead()) {
					return;
				}
				canThink = true;
				Creature creature = getRandomTarget();
				if (creature == null || creature.getLifeStats().isAlreadyDead() || !getOwner().canSee(creature)) {
					setStateIfNot(AIState.FIGHT);
					think();
				}
				else {
					getMoveController().abortMove();
					getOwner().setTarget(creature);
					getOwner().getGameStats().renewLastAttackTime();
					getOwner().getGameStats().renewLastAttackedTime();
					getOwner().getGameStats().renewLastChangeTargetTime();
					getOwner().getGameStats().renewLastSkillTime();
					setStateIfNot(AIState.FIGHT);
					handleMoveValidate();
				}
			}
		}, 2500);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// normal weiter
				if (isHome.equals(true) || isAlreadyDead()) {
					return;
				}
				Collection<AggroInfo> aggroList = getAggroList().getList();
				if (aggroList == null) {
					return;
				}
				if (aggroList.size() < 2) {
					return;
				}

				ArrayList<Creature> playerList = new ArrayList<Creature>();
				for (AggroInfo ai : aggroList) {
					if (ai.getAttacker() instanceof Creature) {
						playerList.add((Creature) ai.getAttacker());
					}
				}
				if (playerList.size() == 0) {
					return;
				}

				int targetNum = Rnd.get(2, playerList.size());
				Creature creature = playerList.get(targetNum - 1);

				if (creature == null || creature.getLifeStats().isAlreadyDead() || !getOwner().canSee(creature)) {
					setStateIfNot(AIState.FIGHT);
					think();
				}
				else {
					getMoveController().abortMove();
					getOwner().setTarget(creature);
					getOwner().getGameStats().renewLastAttackTime();
					getOwner().getGameStats().renewLastAttackedTime();
					getOwner().getGameStats().renewLastChangeTargetTime();
					getOwner().getGameStats().renewLastSkillTime();
					setStateIfNot(AIState.FIGHT);
					handleMoveValidate();
				}
			}
		}, 5000);
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
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500028, getObjectId(), 0, 0);
			callForHelp(60);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
		isHome.set(true);
		canThink = true;
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		percents.clear();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500030, getObjectId(), 0, 0);
	}
}
