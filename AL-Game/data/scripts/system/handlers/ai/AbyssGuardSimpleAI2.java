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
package ai;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.StateEvents;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.handler.SimpleAbyssGuardHandler;
import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Rolandas
 */
@AIName("simple_abyssguard")
public class AbyssGuardSimpleAI2 extends AggressiveNpcAI2 {

	@Override
	protected boolean canHandleEvent(AIEventType eventType) {
		switch (getState()) {
			case DESPAWNED:
				return StateEvents.DESPAWN_EVENTS.hasEvent(eventType);
			case DIED:
				return StateEvents.DEAD_EVENTS.hasEvent(eventType);
			case CREATED:
				return StateEvents.CREATED_EVENTS.hasEvent(eventType);
			default:
				break;
		}
		switch (eventType) {
			case DIALOG_START:
			case DIALOG_FINISH:
				return isNonFightingState();
			case CREATURE_MOVED:
				return getState() != AIState.FIGHT;
			default:
				break;
		}
		return true;
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
		SimpleAbyssGuardHandler.onCreatureSee(this, creature);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		SimpleAbyssGuardHandler.onCreatureMoved(this, creature);
	}

	@Override
	protected boolean handleGuardAgainstAttacker(Creature attacker) {
		return false;
	}
}
