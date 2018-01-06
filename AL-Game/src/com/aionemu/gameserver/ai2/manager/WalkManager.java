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
package com.aionemu.gameserver.ai2.manager;

import java.util.List;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.walker.RouteStep;
import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author ATracer
 */
public class WalkManager {

	private static final int WALK_RANDOM_RANGE = 5;

	/**
	 * @param npcAI
	 */
	public static boolean startWalking(NpcAI2 npcAI) {
		npcAI.setStateIfNot(AIState.WALKING);
		Npc owner = npcAI.getOwner();
		WalkerTemplate template = DataManager.WALKER_DATA.getWalkerTemplate(owner.getSpawn().getWalkerId());
		if (template != null) {
			npcAI.setSubStateIfNot(AISubState.WALK_PATH);
			startRouteWalking(npcAI, owner, template);
		}
		else {
			return startRandomWalking(npcAI, owner);
		}
		return true;
	}

	public static boolean startRouteWalking(NpcAI2 npcAI, WalkerTemplate template) {
		npcAI.setStateIfNot(AIState.WALKING);
		Npc owner = npcAI.getOwner();

		if (template != null) {
			npcAI.setSubStateIfNot(AISubState.WALK_PATH);
			startRouteWalking(npcAI, owner, template);
		}
		else {
			return startRandomWalking(npcAI, owner);
		}
		return true;
	}

	/**
	 * @param npcAI
	 * @param owner
	 */
	private static boolean startRandomWalking(NpcAI2 npcAI, Npc owner) {
		if (!AIConfig.ACTIVE_NPC_MOVEMENT) {
			return false;
		}
		int randomWalkNr = owner.getSpawn().getRandomWalk();
		if (randomWalkNr == 0) {
			return false;
		}
		if (npcAI.setSubStateIfNot(AISubState.WALK_RANDOM)) {
			EmoteManager.emoteStartWalking(npcAI.getOwner());
			chooseNextRandomPoint(npcAI);
			return true;
		}
		return false;
	}

	/**
	 * @param npcAI
	 * @param owner
	 * @param template
	 */
	protected static void startRouteWalking(NpcAI2 npcAI, Npc owner, WalkerTemplate template) {
		if (!AIConfig.ACTIVE_NPC_MOVEMENT) {
			return;
		}
		List<RouteStep> route = template.getRouteSteps();
		int currentPoint = owner.getMoveController().getCurrentPoint();
		RouteStep nextStep = findNextRoutStep(owner, route);
		owner.getMoveController().setCurrentRoute(route);
		owner.getMoveController().setRouteStep(nextStep, route.get(currentPoint));
		EmoteManager.emoteStartWalking(npcAI.getOwner());
		npcAI.getOwner().getMoveController().moveToNextPoint();
	}

	/**
	 * @param owner
	 * @param route
	 * @return
	 */
	protected static RouteStep findNextRoutStep(Npc owner, List<RouteStep> route) {
		int currentPoint = owner.getMoveController().getCurrentPoint();
		RouteStep nextStep = null;
		if (currentPoint != 0) {
			nextStep = findNextRouteStepAfterPause(owner, route, currentPoint);
		}
		else {
			nextStep = findClosestRouteStep(owner, route, nextStep);
		}
		return nextStep;
	}

	/**
	 * @param owner
	 * @param route
	 * @param nextStep
	 * @return
	 */
	protected static RouteStep findClosestRouteStep(Npc owner, List<RouteStep> route, RouteStep nextStep) {
		double closestDist = 0;
		float x = owner.getX();
		float y = owner.getY();
		float z = owner.getZ();

		if (owner.getWalkerGroup() != null) {
			// always choose the 1st step, not the last which is close enough
			if (owner.getWalkerGroup().getGroupStep() < 2) {
				nextStep = route.get(0);
			}
			else {
				nextStep = route.get(owner.getWalkerGroup().getGroupStep() - 1);
			}
		}
		else {
			for (RouteStep step : route) {
				double stepDist = MathUtil.getDistance(x, y, z, step.getX(), step.getY(), step.getZ());
				if (closestDist == 0 || stepDist < closestDist) {
					closestDist = stepDist;
					nextStep = step;
				}
			}
		}
		return nextStep;
	}

	/**
	 * @param owner
	 * @param route
	 * @param currentPoint
	 * @return
	 */
	protected static RouteStep findNextRouteStepAfterPause(Npc owner, List<RouteStep> route, int currentPoint) {
		RouteStep nextStep = route.get(currentPoint);
		double stepDist = MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(), nextStep.getX(), nextStep.getY(), nextStep.getZ());
		if (stepDist < 1) {
			nextStep = nextStep.getNextStep();
		}
		return nextStep;
	}

	/**
	 * Is this npc will walk. Currently all monsters will walk and those npc wich has walk routes
	 *
	 * @param npcAI
	 * @return
	 */
	public static boolean isWalking(NpcAI2 npcAI) {
		return npcAI.isMoveSupported() && (hasWalkRoutes(npcAI) || npcAI.getOwner().isAttackableNpc());
	}

	/**
	 * @param npcAI
	 * @return
	 */
	public static boolean hasWalkRoutes(NpcAI2 npcAI) {
		return npcAI.getOwner().hasWalkRoutes();
	}

	/**
	 * @param npcAI
	 */
	public static void targetReached(final NpcAI2 npcAI) {
		if (npcAI.isInState(AIState.WALKING)) {
			switch (npcAI.getSubState()) {
				case WALK_PATH:
					npcAI.getOwner().updateKnownlist();
					if (npcAI.getOwner().getWalkerGroup() != null) {
						npcAI.getOwner().getWalkerGroup().targetReached(npcAI);
					}
					else {
						chooseNextRouteStep(npcAI);
					}
					break;
				case WALK_WAIT_GROUP:
					npcAI.setSubStateIfNot(AISubState.WALK_PATH);
					chooseNextRouteStep(npcAI);
					break;
				case WALK_RANDOM:
					chooseNextRandomPoint(npcAI);
					break;
				case TALK:
					npcAI.getOwner().getMoveController().abortMove();
					break;
				default:
					break;
			}
		}
	}

	/**
	 * @param npcAI
	 */
	protected static void chooseNextRouteStep(final NpcAI2 npcAI) {
		int walkPause = npcAI.getOwner().getMoveController().getWalkPause();
		if (walkPause == 0) {
			npcAI.getOwner().getMoveController().resetMove();
			npcAI.getOwner().getMoveController().chooseNextStep();
			npcAI.getOwner().getMoveController().moveToNextPoint();
		}
		else {
			npcAI.getOwner().getMoveController().abortMove();
			npcAI.getOwner().getMoveController().chooseNextStep();
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (npcAI.isInState(AIState.WALKING)) {
						npcAI.getOwner().getMoveController().moveToNextPoint();
					}
				}
			}, walkPause);
		}
	}

	/**
	 * @param npcAI
	 */
	private static void chooseNextRandomPoint(final NpcAI2 npcAI) {
		final Npc owner = npcAI.getOwner();
		owner.getMoveController().abortMove();
		int randomWalkNr = owner.getSpawn().getRandomWalk();
		final int walkRange = Math.max(randomWalkNr, WALK_RANDOM_RANGE);

		final float distToSpawn = (float) owner.getDistanceToSpawnLocation();

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (npcAI.isInState(AIState.WALKING)) {
					if (distToSpawn > walkRange) {
						owner.getMoveController().moveToPoint(owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ());
					}
					else {
						int nextX = Rnd.nextInt(walkRange * 2) - walkRange;
						int nextY = Rnd.nextInt(walkRange * 2) - walkRange;
						if (GeoDataConfig.GEO_ENABLE && GeoDataConfig.GEO_NPC_MOVE) {
							byte flags = (byte) (CollisionIntention.PHYSICAL.getId() | CollisionIntention.DOOR.getId() | CollisionIntention.WALK.getId());
							Vector3f loc = GeoService.getInstance().getClosestCollision(owner, owner.getX() + nextX, owner.getY() + nextY, owner.getZ(), true, flags);
							owner.getMoveController().moveToPoint(loc.x, loc.y, loc.z);
						}
						else {
							owner.getMoveController().moveToPoint(owner.getX() + nextX, owner.getY() + nextY, owner.getZ());
						}
					}
				}
			}
		}, Rnd.get(AIConfig.MINIMIMUM_DELAY, AIConfig.MAXIMUM_DELAY) * 1000);

	}

	/**
	 * @param npcAI
	 */
	public static void stopWalking(NpcAI2 npcAI) {
		npcAI.getOwner().getMoveController().abortMove();
		npcAI.setStateIfNot(AIState.IDLE);
		npcAI.setSubStateIfNot(AISubState.NONE);
		EmoteManager.emoteStopWalking(npcAI.getOwner());
	}

	/**
	 * @param owner
	 * @return
	 */
	public static boolean isArrivedAtPoint(NpcAI2 npcAI) {
		return npcAI.getOwner().getMoveController().isReachedPoint();
	}
}
