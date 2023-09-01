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
package com.aionemu.gameserver.skillengine.properties;

import java.util.List;

import org.apache.commons.lang.math.FloatRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PositionUtil;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author ATracer
 */
public class TargetRangeProperty {

	private static final Logger log = LoggerFactory.getLogger(TargetRangeProperty.class);

	/**
	 * @param skill
	 * @param properties
	 * @return
	 */
	public static final boolean set(final Skill skill, Properties properties) {

		TargetRangeAttribute value = properties.getTargetType();
		int distanceToTarget = properties.getTargetDistance();
		int maxcount = properties.getTargetMaxCount();
		int effectiveRange = properties.getEffectiveRange();
		int altitude = properties.getEffectiveAltitude() != 0 ? properties.getEffectiveAltitude() : 1;

		final List<Creature> effectedList = skill.getEffectedList();
		skill.setTargetRangeAttribute(value);
		switch (value) {
			case ONLYONE:
				break;
			case AREA:
				final Creature firstTarget = skill.getFirstTarget();

				if (firstTarget == null) {
					log.warn("CHECKPOINT: first target is null for skillid " + skill.getSkillTemplate().getSkillId());
					return false;
				}

				// Create a sorted map of the objects in knownlist
				// and filter them properly
				for (VisibleObject nextCreature : firstTarget.getKnownList().getKnownObjects().values()) {
					if (!(nextCreature instanceof Creature)) {
						continue;
					}
					if (((Creature) nextCreature).getLifeStats() == null) {
						continue;
					}
					if (((Creature) nextCreature).getLifeStats().isAlreadyDead()) {
						continue;
					}

					// if (nextCreature instanceof Kisk && isInsideDisablePvpZone((Creature) nextCreature))
					// continue;
					if (Math.abs(firstTarget.getZ() - nextCreature.getZ()) > altitude || ((nextCreature instanceof Player) && ((Player) nextCreature).isInPlayerMode(PlayerMode.WINDSTREAM))) {
						continue;
					}

					// TODO this is a temporary hack for traps
					if (skill.getEffector() instanceof Trap && ((Trap) skill.getEffector()).getCreator() == nextCreature) {
						continue;
					}

					// Players in blinking state must not be counted
					if ((nextCreature instanceof Player) && (((Player) nextCreature).isProtectionActive())) {
						continue;
					}

					if (skill.isPointSkill()) {
						if (MathUtil.isIn3dRange(skill.getX(), skill.getY(), skill.getZ(), nextCreature.getX(), nextCreature.getY(), nextCreature.getZ(), distanceToTarget + 1)) {
							skill.getEffectedList().add((Creature) nextCreature);
						}
					}
					if (properties.getEffectiveAngle() > 0) {
						// Fire Storm; only positive angles
						float angle = properties.getEffectiveAngle() / 2f;
						FloatRange range = new FloatRange(angle - 180, -angle);
						if (range.containsFloat(PositionUtil.getAngleToTarget(skill.getEffector(), nextCreature))) {
							continue;
						}
						if (!MathUtil.isIn3dRange(skill.getEffector(), nextCreature, effectiveRange)) {
							continue;
						}
						if (!skill.shouldAffectTarget(nextCreature)) {
							continue;
						}
						skill.getEffectedList().add((Creature) nextCreature);
					}
					else if (properties.getEffectiveDist() > 0) {
						// Lightning bolt
						if (MathUtil.isInsideAttackCylinder(skill.getEffector(), nextCreature, distanceToTarget, properties.getEffectiveDist(), properties.getDirection()) || MathUtil.isIn3dRange(firstTarget, nextCreature, effectiveRange + firstTarget.getObjectTemplate().getBoundRadius().getCollision())) {
							if (!skill.shouldAffectTarget(nextCreature)) {
								continue;
							}
							skill.getEffectedList().add((Creature) nextCreature);
						}
					}
					else if (MathUtil.isIn3dRange(firstTarget, nextCreature, effectiveRange + firstTarget.getObjectTemplate().getBoundRadius().getCollision())) {
						if (!skill.shouldAffectTarget(nextCreature)) {
							continue;
						}
						skill.getEffectedList().add((Creature) nextCreature);
					}
				}

				break;
			case PARTY:
				// fix for Bodyguard(417)
				if (maxcount == 1) {
					break;
				}
				int partyCount = 0;
				if (skill.getEffector() instanceof Player) {
					Player effector = (Player) skill.getEffector();
					// TODO merge groups ?
					if (effector.isInAlliance2()) {
						effectedList.clear();
						for (Player player : effector.getPlayerAllianceGroup2().getMembers()) {
							if (partyCount >= 6 || partyCount >= maxcount) {
								break;
							}
							if (!player.isOnline()) {
								continue;
							}
							if (MathUtil.isIn3dRange(effector, player, effectiveRange + 1)) {
								effectedList.add(player);
								partyCount++;
							}
						}
					}
					else if (effector.isInGroup2()) {
						effectedList.clear();
						for (Player member : effector.getPlayerGroup2().getMembers()) {
							if (partyCount >= maxcount) {
								break;
							}
							// TODO: here value +4 till better move controller developed
							if (member != null && MathUtil.isIn3dRange(effector, member, effectiveRange + 1)) {
								effectedList.add(member);
								partyCount++;
							}
						}
					}
				}
				break;
			case PARTY_WITHPET:
				if (skill.getEffector() instanceof Player) {
					final Player effector = (Player) skill.getEffector();
					if (effector.isInAlliance2()) {
						effectedList.clear();
						// TODO may be alliance group ?
						for (Player player : effector.getPlayerAlliance2().getMembers()) {
							if (!player.isOnline()) {
								continue;
							}
							if (player.getLifeStats().isAlreadyDead()) {
								continue;
							}
							if (MathUtil.isIn3dRange(effector, player, distanceToTarget + 1)) {
								effectedList.add(player);
								Summon aMemberSummon = player.getSummon();
								if (aMemberSummon != null) {
									effectedList.add(aMemberSummon);
								}
							}
						}
					}
					else if (effector.isInGroup2()) {
						effectedList.clear();
						for (Player member : effector.getPlayerGroup2().getMembers()) {
							if (!member.isOnline()) {
								continue;
							}
							if (member.getLifeStats().isAlreadyDead()) {
								continue;
							}
							if (MathUtil.isIn3dRange(effector, member, distanceToTarget + 1)) {
								effectedList.add(member);
								Summon aMemberSummon = member.getSummon();
								if (aMemberSummon != null) {
									effectedList.add(aMemberSummon);
								}
							}
						}
					}
				}
				break;
			case POINT:
				for (VisibleObject nextCreature : skill.getEffector().getKnownList().getKnownObjects().values()) {
					if (!(nextCreature instanceof Creature)) {
						continue;
					}
					if (((Creature) nextCreature).getLifeStats().isAlreadyDead()) {
						continue;
					}

					// if (nextCreature instanceof Kisk && isInsideDisablePvpZone((Creature) nextCreature))
					// continue;
					// Players in blinking state must not be counted
					if ((nextCreature instanceof Player) && (((Player) nextCreature).isProtectionActive())) {
						continue;
					}

					if (MathUtil.getDistance(skill.getX(), skill.getY(), skill.getZ(), nextCreature.getX(), nextCreature.getY(), nextCreature.getZ()) <= distanceToTarget + 1) {
						effectedList.add((Creature) nextCreature);
					}
				}
			case NONE:
				break;

			// TODO other enum values
		}
		return true;
	}

	@SuppressWarnings("unused")
	private static final boolean isInsideDisablePvpZone(Creature creature) {
		for (ZoneInstance zone : creature.getPosition().getMapRegion().getZones(creature)) {
			if (creature.isInsideZoneType(ZoneType.PVP) && zone.getZoneTemplate().getFlags() == 0) {
				return true;
			}
		}
		return false;
	}
}
