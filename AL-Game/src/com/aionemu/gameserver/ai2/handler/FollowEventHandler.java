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
package com.aionemu.gameserver.ai2.handler;

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author ATracer
 */
public class FollowEventHandler {

	/**
	 * @param npcAI
	 * @param creature
	 */
	public static void follow(NpcAI2 npcAI, Creature creature) {
		if (npcAI.setStateIfNot(AIState.FOLLOWING)) {
			npcAI.getOwner().setTarget(creature);
			EmoteManager.emoteStartFollowing(npcAI.getOwner());
		}
	}

	/**
	 * @param npcAI
	 * @param creature
	 */
	public static void creatureMoved(NpcAI2 npcAI, Creature creature) {
		if (npcAI.isInState(AIState.FOLLOWING)) {
			if (npcAI.getOwner().isTargeting(creature.getObjectId()) && !creature.getLifeStats().isAlreadyDead()) {
				checkFollowTarget(npcAI, creature);
			}
		}
	}

	/**
	 * @param creature
	 */
	public static void checkFollowTarget(NpcAI2 npcAI, Creature creature) {
		if (!isInRange(npcAI, creature)) {
			npcAI.onGeneralEvent(AIEventType.TARGET_TOOFAR);
		}
	}

	public static boolean isInRange(AbstractAI ai, VisibleObject object) {
		if (object == null) {
			return false;
		}
		if (object.isInInstance()) {
			return MathUtil.isIn3dRange(ai.getOwner(), object, 9999);
		}
		else if (ai.getOwner().getLifeStats().getHpPercentage() < 100) {
			return MathUtil.isIn3dRange(ai.getOwner(), object, 30);
		}
		else {
			return MathUtil.isIn3dRange(ai.getOwner(), object, 15);
		}
	}

	/**
	 * @param npcAI
	 * @param creature
	 */
	public static void stopFollow(NpcAI2 npcAI, Creature creature) {
		if (npcAI.setStateIfNot(AIState.IDLE)) {
			npcAI.getOwner().setTarget(null);
			npcAI.getOwner().getMoveController().abortMove();
			npcAI.getOwner().getController().scheduleRespawn();
			npcAI.getOwner().getController().onDelete();
		}
	}
}
