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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.model.Skill;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Properties")
public class Properties {

	@XmlAttribute(name = "first_target", required = true)
	protected FirstTargetAttribute firstTarget;
	@XmlAttribute(name = "first_target_range", required = true)
	protected int firstTargetRange;
	@XmlAttribute(name = "awr")
	protected boolean addWeaponRange;
	@XmlAttribute(name = "target_relation", required = true)
	protected TargetRelationAttribute targetRelation;
	@XmlAttribute(name = "target_type", required = true)
	protected TargetRangeAttribute targetType;
	@XmlAttribute(name = "target_distance")
	protected int targetDistance;
	@XmlAttribute(name = "target_maxcount")
	protected int targetMaxCount;
	@XmlAttribute(name = "target_status")
	private List<String> targetStatus;
	@XmlAttribute(name = "revision_distance")
	protected int revisionDistance;
	@XmlAttribute(name = "effective_range")
	private int effectiveRange;
	@XmlAttribute(name = "effective_altitude")
	private int effectiveAltitude;
	@XmlAttribute(name = "effective_angle")
	private int effectiveAngle;
	@XmlAttribute(name = "effective_dist")
	private int effectiveDist;
	@XmlAttribute(name = "direction")
	protected AreaDirections direction = AreaDirections.NONE;
	@XmlAttribute(name = "target_species")
	protected TargetSpeciesAttribute targetSpecies = TargetSpeciesAttribute.ALL;

	/**
	 * @param skill
	 */
	public boolean validate(Skill skill) {
		if (firstTarget != null) {
			if (!FirstTargetProperty.set(skill, this)) {
				return false;
			}
		}
		if (firstTargetRange != 0 || addWeaponRange) {
			if (!FirstTargetRangeProperty.set(skill, this, CastState.CAST_START)) {
				return false;
			}
		}
		if (targetType != null) {
			if (!TargetRangeProperty.set(skill, this)) {
				return false;
			}
		}
		if (targetRelation != null) {
			if (!TargetRelationProperty.set(skill, this)) {
				return false;
			}
		}
		if (targetType != null) {
			if (!MaxCountProperty.set(skill, this)) {
				return false;
			}
		}
		if (targetStatus != null) {
			if (!TargetStatusProperty.set(skill, this)) {
				return false;
			}
		}
		if (targetSpecies != TargetSpeciesAttribute.ALL) {
			if (!TargetSpeciesProperty.set(skill, this)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param skill
	 */
	public boolean endCastValidate(Skill skill) {
		Creature firstTarget = skill.getFirstTarget();
		skill.getEffectedList().clear();
		skill.getEffectedList().add(firstTarget);

		if (firstTargetRange != 0) {
			if (!FirstTargetRangeProperty.set(skill, this, CastState.CAST_END)) {
				return false;
			}
		}
		if (targetType != null) {
			if (!TargetRangeProperty.set(skill, this)) {
				return false;
			}
		}
		if (targetRelation != null) {
			if (!TargetRelationProperty.set(skill, this)) {
				return false;
			}
		}
		if (targetType != null) {
			if (!MaxCountProperty.set(skill, this)) {
				return false;
			}
		}
		if (targetStatus != null) {
			if (!TargetStatusProperty.set(skill, this)) {
				return false;
			}
		}
		if (targetSpecies != TargetSpeciesAttribute.ALL) {
			if (!TargetSpeciesProperty.set(skill, this)) {
				return false;
			}
		}
		return true;
	}

	public FirstTargetAttribute getFirstTarget() {
		return firstTarget;
	}

	public int getFirstTargetRange() {
		return firstTargetRange;
	}

	public boolean isAddWeaponRange() {
		return addWeaponRange;
	}

	public TargetRelationAttribute getTargetRelation() {
		return targetRelation;
	}

	public TargetRangeAttribute getTargetType() {
		return targetType;
	}

	public int getTargetDistance() {
		return targetDistance;
	}

	public int getTargetMaxCount() {
		return targetMaxCount;
	}

	public List<String> getTargetStatus() {
		return targetStatus;
	}

	public int getRevisionDistance() {
		return revisionDistance;
	}

	public int getEffectiveRange() {
		return effectiveRange;
	}

	public int getEffectiveAltitude() {
		return effectiveAltitude;
	}

	public int getEffectiveDist() {
		return effectiveDist;
	}

	public int getEffectiveAngle() {
		return effectiveAngle;
	}

	public AreaDirections getDirection() {
		return direction;
	}

	public TargetSpeciesAttribute getTargetSpecies() {
		return targetSpecies;
	}

	public enum CastState {

		CAST_START(true),
		CAST_END(false);

		private final boolean isCastStart;

		CastState(boolean isCastStart) {
			this.isCastStart = isCastStart;
		}

		public boolean isCastStart() {
			return isCastStart;
		}
	}
}
