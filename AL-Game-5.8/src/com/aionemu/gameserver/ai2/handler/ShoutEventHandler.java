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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npcshout.NpcShout;
import com.aionemu.gameserver.model.templates.npcshout.ShoutEventType;
import com.aionemu.gameserver.model.templates.npcshout.ShoutType;
import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Rolandas
 */
public final class ShoutEventHandler {

	public static void onSee(NpcAI2 npcAI, Creature target) {
		Npc npc = npcAI.getOwner();
		if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.SEE)) {
			List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.SEE, null, 0);
			NpcShoutsService.getInstance().shout(npc, target, shouts, 0, false);
			shouts.clear();
		}
	}

	public static void onBeforeDespawn(NpcAI2 npcAI) {
		Npc npc = npcAI.getOwner();
		if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.BEFORE_DESPAWN)) {
			List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.BEFORE_DESPAWN, null, 0);
			NpcShoutsService.getInstance().shout(npc, null, shouts, 0, false);
			shouts.clear();
		}
	}

	public static void onReachedWalkPoint(NpcAI2 npcAI) {
		Npc npc = npcAI.getOwner();
		WalkerTemplate tp = DataManager.WALKER_DATA.getWalkerTemplate(npc.getSpawn().getWalkerId());
		int stepCount = tp.getRouteSteps().size();
		ShoutEventType shoutType = npc.getMoveController().isChangingDirection() ? ShoutEventType.WALK_DIRECTION : ShoutEventType.WALK_WAYPOINT;
		if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), shoutType)) {
			if (Rnd.get(stepCount) < 2) {
				List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), shoutType, null, 0);
				if (npc.getTarget() instanceof Creature) {
					NpcShoutsService.getInstance().shout(npc, (Creature) npc.getTarget(), shouts, 0, false);
				}
				else {
					NpcShoutsService.getInstance().shout(npc, null, shouts, 0, false);
				}
				shouts.clear();
			}
		}
	}

	public static void onSwitchedTarget(NpcAI2 npcAI, Creature creature) {
		Npc npc = npcAI.getOwner();
		if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.SWITCH_TARGET)) {
			List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.SWITCH_TARGET, null, 0);
			NpcShoutsService.getInstance().shout(npc, creature, shouts, 0, false);
			shouts.clear();
		}
	}

	public static void onDied(NpcAI2 npcAI) {
		Npc owner = npcAI.getOwner();
		if (DataManager.NPC_SHOUT_DATA.hasAnyShout(owner.getPosition().getMapId(), owner.getNpcId(), ShoutEventType.DIED)) {
			List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(owner.getPosition().getMapId(), owner.getNpcId(), ShoutEventType.DIED, null, 0);
			if (shouts.size() > 0) {
				NpcShoutsService.getInstance().shout(owner, (Creature) owner.getTarget(), shouts, 0, false);
			}
			shouts.clear();
		}
	}

	// TODO: Figure out what the difference between ATTACK_BEGIN and HELP; HELPCALL should make NPC run

	/**
	 * Called on Aggro when NPC is ready to attack
	 */
	public static void onAttackBegin(NpcAI2 npcAI, Creature creature) {
		Npc npc = npcAI.getOwner();
		if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACK_BEGIN)) {
			List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACK_BEGIN, null, 0);
			NpcShoutsService.getInstance().shout(npc, creature, shouts, 0, false);
			shouts.clear();
			return;
		}
	}

	/**
	 * Handle NPC attacked event (when damage was received or not)
	 */
	public static void onHelp(NpcAI2 npcAI, Creature creature) {
		// TODO: [RR] change AI or randomise behaviour for "cowards" and "fanatics" ???
		Npc npc = npcAI.getOwner();
		if (npc.getAttackedCount() == 0) {
			if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACKED)) {
				List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACKED, null, 0);
				NpcShoutsService.getInstance().shout(npc, creature, shouts, 0, false);
				shouts.clear();
				return;
			}
			if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.HELPCALL)) {
				List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.HELPCALL, null, 0);
				NpcShoutsService.getInstance().shout(npc, creature, shouts, 0, false);
				shouts.clear();
			}
		}
	}

	/**
	 * Handles attacks from NPC to NPC. <br>
	 * <b><font color='red'>IMPORTANT!!! </font>All such shouts must be of type SAY and poll_delay specified.</b>
	 */
	public static void onEnemyAttack(NpcAI2 npcAI, Creature target) {
		final Npc npc = npcAI.getOwner();
		if (!DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACKED)) {
			return;
		}

		List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACKED, null, 0);

		List<NpcShout> finalShouts = new ArrayList<NpcShout>();
		for (NpcShout s : shouts) {
			if (s.getShoutType() == ShoutType.SAY) {
				finalShouts.add(s);
			}
		}

		if (finalShouts.size() == 0) {
			return;
		}

		int randomShout = Rnd.get(finalShouts.size());
		final NpcShout shout = finalShouts.get(randomShout);
		finalShouts.clear();
		shouts.clear();

		if (!npc.mayShout(shout.getPollDelay() / 1000)) {
			return;
		}

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Iterator<Player> iter = npc.getKnownList().getKnownPlayers().values().iterator();
				while (iter.hasNext()) {
					Player kObj = iter.next();
					if (kObj.getLifeStats().isAlreadyDead()) {
						return;
					}
					NpcShoutsService.getInstance().shout(npc, kObj, shout, shout.getPollDelay() / 1000);
				}
			}
		}, 0);
	}

	public static void onCast(NpcAI2 npcAI, Creature creature) {
		handleNumericEvent(npcAI, creature, ShoutEventType.CAST_K);
	}

	/**
	 * Handle target attacked events
	 */
	public static void onAttack(NpcAI2 npcAI, Creature creature) {
		handleNumericEvent(npcAI, creature, ShoutEventType.ATTACK_K);
	}

	private static void handleNumericEvent(NpcAI2 npcAI, Creature creature, ShoutEventType eventType) {
		Npc owner = npcAI.getOwner();
		List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(owner.getPosition().getMapId(), owner.getNpcId(), eventType, null, 0);
		if (shouts == null) {
			return;
		}

		List<NpcShout> validShouts = new ArrayList<NpcShout>();
		List<NpcShout> nonNumberedShouts = new ArrayList<NpcShout>();
		for (NpcShout shout : shouts) {
			if (shout.getSkillNo() == 0) {
				nonNumberedShouts.add(shout);
			}
			else if (shout.getSkillNo() == owner.getSkillNumber()) {
				validShouts.add(shout);
			}
		}

		if (validShouts.size() == 0) {
			validShouts.clear();
			validShouts = nonNumberedShouts;
		}
		else {
			nonNumberedShouts.clear();
		}

		if (validShouts.size() > 0) {
			NpcShoutsService.getInstance().shout(owner, creature, validShouts, 0, false);
		}

		validShouts.clear();
		shouts.clear();
	}

	public static void onAttackEnd(NpcAI2 npcAI) {
		Npc npc = npcAI.getOwner();
		if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACK_END)) {
			List<NpcShout> shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACK_END, null, 0);
			NpcShoutsService.getInstance().shout(npc, null, shouts, 0, false);
			shouts.clear();
		}
	}
}
