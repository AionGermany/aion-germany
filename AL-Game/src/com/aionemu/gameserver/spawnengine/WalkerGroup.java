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
package com.aionemu.gameserver.spawnengine;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.sum;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ai2.AI2Logger;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.zone.Point2D;

/**
 * @author vlog
 * @modified Rolandas
 */
public class WalkerGroup {

	private static final Logger log = LoggerFactory.getLogger(WalkerGroup.class);
	private List<ClusteredNpc> members;
	private WalkerGroupType type;
	private final float walkerXpos;
	private final float walkerYpos;
	private int[] memberSteps;
	private volatile int groupStep;
	private final String versionId;
	private boolean isSpawned;

	public WalkerGroup(List<ClusteredNpc> members) {
		this.members = sort(members, on(ClusteredNpc.class).getWalkerIndex());
		memberSteps = new int[members.size()];
		walkerXpos = members.get(0).getX();
		walkerYpos = members.get(0).getY();
		type = members.get(0).getWalkTemplate().getType();
		versionId = members.get(0).getWalkTemplate().getVersionId();
	}

	public void form() {
		if (getWalkType() == WalkerGroupType.SQUARE) {
			int[] rows = members.get(0).getWalkTemplate().getRows();
			if (sum(ArrayUtils.toObject(rows), on(Integer.class)) != members.size()) {
				log.warn("Invalid row sizes for walk cluster " + members.get(0).getWalkTemplate().getRouteId());
			}
			if (rows.length == 1) {
				// Line formation: distance 2 meters from each other (divide by 2 and multiple by 2)
				// negative at left hand and positive at the right hand
				float bounds = sum(members, on(ClusteredNpc.class).getNpc().getObjectTemplate().getBoundRadius().getSide());
				float distance = (1 - members.size()) / 2f * (WalkerGroupShift.DISTANCE + bounds);
				Point2D origin = new Point2D(walkerXpos, walkerYpos);
				Point2D destination = new Point2D(members.get(0).getWalkTemplate().getRouteStep(2).getX(), members.get(0).getWalkTemplate().getRouteStep(2).getY());
				for (int i = 0; i < members.size(); i++, distance += WalkerGroupShift.DISTANCE) {
					WalkerGroupShift shift = new WalkerGroupShift(distance, 0);
					Point2D loc = getLinePoint(origin, destination, shift);
					members.get(i).setX(loc.getX());
					members.get(i).setY(loc.getY());
					Npc member = members.get(i).getNpc();
					member.setWalkerGroup(this);
					member.setWalkerGroupShift(shift);
					// distance += npc.getObjectTemplate().getBoundRadius().getSide();
				}
			}
			else if (rows.length != 0) {
				float rowDistances[] = new float[rows.length - 1];
				float coronalDist = 0;
				for (int i = 0; i < rows.length - 1; i++) {
					if (rows[i] % 2 != rows[i + 1] % 2) {
						rowDistances[i] = 0.86602540378443864676372317075294f * WalkerGroupShift.DISTANCE;
					}
					else {
						rowDistances[i] = WalkerGroupShift.DISTANCE;
					}
					coronalDist -= rowDistances[i];
				}
				Point2D origin = new Point2D(walkerXpos, walkerYpos);
				Point2D destination = new Point2D(members.get(0).getWalkTemplate().getRouteStep(2).getX(), members.get(0).getWalkTemplate().getRouteStep(2).getY());
				int index = 0;
				for (int i = 0; i < rows.length; i++) {
					float sagittalDist = (1 - rows[i]) / 2f * WalkerGroupShift.DISTANCE;
					for (int j = 0; j < rows[i]; j++, sagittalDist += WalkerGroupShift.DISTANCE) {
						if (index > members.size() - 1) {
							break;
						}
						WalkerGroupShift shift = new WalkerGroupShift(sagittalDist, coronalDist);
						Point2D loc = getLinePoint(origin, destination, shift);
						ClusteredNpc cnpc = members.get(index++);
						cnpc.setX(loc.getX());
						cnpc.setY(loc.getY());
						cnpc.getNpc().setWalkerGroup(this);
						cnpc.getNpc().setWalkerGroupShift(shift);
					}
					if (i < rows.length - 1) {
						coronalDist += rowDistances[i];
					}
				}
			}
		}
		else if (getWalkType() == WalkerGroupType.CIRCLE) {
			// TODO: if needed
		}
		else if (getWalkType() == WalkerGroupType.POINT) {
			log.warn("No formation specified for walk cluster " + members.get(0).getWalkTemplate().getRouteId());
		}
	}

	@SuppressWarnings("unused")
	private float getSidesExtra(int[] rows, int startIndex, int endIndex) {
		return 0;
	}

	/**
	 * Returns coordinates of NPC in 2D from the initial spawn location
	 *
	 * @param origin
	 *            - initial spawn location
	 * @param destination
	 *            - point of next move
	 * @param shift
	 *            - distance from origin located in lines perpendicular to destination; for SagittalShift if negative then located to the left from origin, otherwise, to the right for CoronalShift if
	 *            negative then located to back, otherwise to the front
	 * @category TODO: move to MathUtil when all kinds of WalkerGroupType are implemented.
	 */
	public static Point2D getLinePoint(Point2D origin, Point2D destination, WalkerGroupShift shift) {
		// TODO: implement angle shift
		WalkerGroupShift dir = getShiftSigns(origin, destination);
		Point2D result = null;
		if (origin.getY() - destination.getY() == 0) {
			return new Point2D(origin.getX() + dir.getCoronalShift() * shift.getCoronalShift(), origin.getY() - dir.getSagittalShift() * shift.getSagittalShift());
		}
		else if (origin.getX() - destination.getX() == 0) {
			return new Point2D(origin.getX() + dir.getCoronalShift() * shift.getSagittalShift(), origin.getY() + dir.getCoronalShift() * shift.getCoronalShift());
		}
		else {
			double slope = (origin.getX() - destination.getX()) / (origin.getY() - destination.getY());
			double dx = Math.abs(shift.getSagittalShift()) / Math.sqrt(1 + slope * slope);
			if (shift.getSagittalShift() * dir.getCoronalShift() < 0) {
				result = new Point2D((float) (origin.getX() - dx), (float) (origin.getY() + dx * slope));
			}
			else {
				result = new Point2D((float) (origin.getX() + dx), (float) (origin.getY() - dx * slope));
			}
		}
		if (shift.getCoronalShift() != 0) {
			Point2D rotatedShift = null;
			if (shift.getSagittalShift() != 0) {
				rotatedShift = getLinePoint(origin, destination, new WalkerGroupShift(Math.signum(shift.getSagittalShift()) * Math.abs(shift.getCoronalShift()), 0));
			}
			else {
				rotatedShift = getLinePoint(origin, destination, new WalkerGroupShift(Math.abs(shift.getCoronalShift()), 0));
			}

			// since it's rotated, and perpendicular, dx and dy are reciprocal when not rotated
			float dx = Math.abs(origin.getX() - rotatedShift.getX());
			float dy = Math.abs(origin.getY() - rotatedShift.getY());
			if (shift.getCoronalShift() < 0) {
				if (dir.getSagittalShift() < 0 && dir.getCoronalShift() < 0) {
					result = new Point2D(result.getX() + dy, result.getY() + dx);
				}
				else if (dir.getSagittalShift() > 0 && dir.getCoronalShift() > 0) {
					result = new Point2D(result.getX() - dy, result.getY() - dx);
				}
				else if (dir.getSagittalShift() < 0 && dir.getCoronalShift() > 0) {
					result = new Point2D(result.getX() + dy, result.getY() - dx);
				}
				else if (dir.getSagittalShift() > 0 && dir.getCoronalShift() < 0) {
					result = new Point2D(result.getX() - dy, result.getY() + dx);
				}
			}
			else {
				if (dir.getSagittalShift() < 0 && dir.getCoronalShift() < 0) {
					result = new Point2D(result.getX() - dy, result.getY() - dx);
				}
				else if (dir.getSagittalShift() > 0 && dir.getCoronalShift() > 0) {
					result = new Point2D(result.getX() + dy, result.getY() + dx);
				}
				else if (dir.getSagittalShift() < 0 && dir.getCoronalShift() > 0) {
					result = new Point2D(result.getX() - dy, result.getY() + dx);
				}
				else if (dir.getSagittalShift() > 0 && dir.getCoronalShift() < 0) {
					result = new Point2D(result.getX() + dy, result.getY() - dx);
				}
			}
		}
		return result;
	}

	/*
	 * Return a normalized direction vector
	 */
	private static WalkerGroupShift getShiftSigns(Point2D origin, Point2D destination) {
		float dx = Math.signum(destination.getX() - origin.getX());
		float dy = Math.signum(destination.getY() - origin.getY());
		return new WalkerGroupShift(dx, dy);
	}

	public void setStep(Npc member, int step) {
		int currentStep = 0;
		for (int i = 0; i < members.size(); i++) {
			if (memberSteps[i] > currentStep) {
				currentStep = memberSteps[i];
			}
			if (members.get(i).getNpc().equals(member)) {
				AI2Logger.info(members.get(i).getNpc().getAi2(), "Setting step to " + step);
				memberSteps[i] = step;
			}
		}
		if (step > currentStep || step == 1) {
			groupStep = step;
		}
	}

	public void targetReached(NpcAI2 npcAI) {
		synchronized (members) {
			npcAI.setSubStateIfNot(AISubState.WALK_WAIT_GROUP);
			boolean allArrived = true;
			for (ClusteredNpc snpc : members) {
				allArrived &= snpc.getNpc().getAi2().getSubState() == AISubState.WALK_WAIT_GROUP;
				if (!allArrived) {
					break;
				}
			}

			for (int i = 0; i < members.size(); i++) {
				ClusteredNpc snpc = members.get(i);
				if ((memberSteps[i] == groupStep) && !allArrived) {
					npcAI.getOwner().getMoveController().abortMove();
					npcAI.setStateIfNot(AIState.WALKING);
					npcAI.setSubStateIfNot(AISubState.WALK_WAIT_GROUP);
					continue;
				}
				npcAI = (NpcAI2) (snpc.getNpc().getAi2());
				if (npcAI.getSubState() == AISubState.WALK_WAIT_GROUP) {
					WalkManager.targetReached(npcAI);
				}
			}
		}
	}

	public boolean isSpawned() {
		return isSpawned;
	}

	public void spawn() {
		for (ClusteredNpc snpc : members) {
			float height = getHeight(snpc.getX(), snpc.getY(), snpc.getNpc().getSpawn());
			snpc.spawn(height);
		}
		isSpawned = true;
	}

	public void respawn(Npc npc) {
		for (int index = 0; index < members.size(); index++) {
			ClusteredNpc snpc = members.get(index);
			if (snpc.getWalkerIndex() == npc.getSpawn().getWalkerIndex() && snpc.getNpc().getNpcId() == npc.getNpcId()) {
				synchronized (members) {
					snpc.setNpc(npc);
					memberSteps[index] = 1;
				}
				break;
			}
		}
	}

	public void despawn() {
		for (ClusteredNpc snpc : members) {
			snpc.despawn();
			// reset positions
			form();
			for (int index = 0; index < memberSteps.length; index++) {
				memberSteps[index] = 1;
			}
			groupStep = 1;
		}
		isSpawned = false;
	}

	public ClusteredNpc getClusterData(Npc npc) {
		for (ClusteredNpc snpc : members) {
			if (snpc.getNpc().equals(npc)) {
				return snpc;
			}
		}
		return null;
	}

	private float getHeight(float x, float y, SpawnTemplate template) {
		/*
		 * if (GeoService.getInstance().isGeoOn()) { return GeoService.getInstance().getZ(template.getWorldId(), x, y, z, ); }
		 */
		return template.getZ();
	}

	public int getPool() {
		return members.size();
	}

	/**
	 * @return the type
	 */
	public WalkerGroupType getWalkType() {
		return type;
	}

	public boolean isLinearlyPositioned(Npc npc) {
		if (type != WalkerGroupType.SQUARE) {
			return false;
		}
		for (ClusteredNpc snpc : members) {
			if (snpc.getNpc().equals(npc)) {
				return snpc.getWalkTemplate().getRows().length == 1;
			}
		}
		return false;
	}

	/**
	 * @return the groupStep
	 */
	public int getGroupStep() {
		return groupStep;
	}

	public String getVersionId() {
		return versionId;
	}
}
