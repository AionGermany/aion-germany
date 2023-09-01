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
package ai.instance.mechanerksWeaponsFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI2;

/**
 * @author FrozenKiller
 */
@AIName("lunapatrol")
public class LunaPatrolAI2 extends AggressiveNpcAI2 {

	private boolean canThink = true;
	private AtomicBoolean startedEvent = new AtomicBoolean(false);

	@Override
	public boolean canThink() {
		return canThink;
	}

	Npc target;

	// Herez -> 1502543, Marek -> 1501598, Manad -> 1501599, Roxy -> 1501597, Joel -> 1501600
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				target = getPosition().getWorldMapInstance().getNpc(243705);
				AI2Actions.targetCreature(LunaPatrolAI2.this, target);
				getAggroList().addHate(target, 100000);
			}
		}, 6000);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		super.handleCreatureMoved(creature);
		if (startedEvent.compareAndSet(false, true)) {
			switch (getNpcId()) {
				case 833826: // Roxy
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1501597, getObjectId(), 0, 0);
					break;
				case 833827: // Marek
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1501598, getObjectId(), 0, 0);
					break;
				case 833828: // Manad
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1501599, getObjectId(), 0, 0);
					break;
				case 833829: // Herez
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1502543, getObjectId(), 0, 0);
					break;
				case 833897: // Joel
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1501600, getObjectId(), 0, 0);
					break;
			}
			setStateIfNot(AIState.FOLLOWING);
			getOwner().setState(1);
			AI2Actions.targetCreature(this, getPosition().getWorldMapInstance().getNpc(833829));
			getMoveController().moveToTargetObject();
		}
	}

	@Override
	protected void handleMoveArrived() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				canThink = true;
				target = getPosition().getWorldMapInstance().getNpc(243705);
				AI2Actions.targetCreature(LunaPatrolAI2.this, target);
				getAggroList().addHate(target, 100000);
				setStateIfNot(AIState.FIGHT);
				think();
			}
		}, 2000);
		super.handleMoveArrived();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}
}
