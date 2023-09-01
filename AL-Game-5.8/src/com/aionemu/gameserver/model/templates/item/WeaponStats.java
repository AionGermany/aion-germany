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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ATracer
 */
public class WeaponStats {

	@XmlAttribute(name = "min_damage")
	protected int minDamage;
	@XmlAttribute(name = "max_damage")
	protected int maxDamage;
	@XmlAttribute(name = "attack_speed")
	protected int attackSpeed;
	@XmlAttribute(name = "physical_critical")
	protected int physicalCritical;
	@XmlAttribute(name = "physical_accuracy")
	protected int physicalAccuracy;
	@XmlAttribute
	protected int parry;
	@XmlAttribute(name = "magical_accuracy")
	protected int magicalAccuracy;
	@XmlAttribute(name = "boost_magical_skill")
	protected int boostMagicalSkill;
	@XmlAttribute(name = "attack_range")
	protected int attackRange;
	@XmlAttribute(name = "hit_count")
	protected int hitCount;
	@XmlAttribute(name = "reduce_max")
	protected int reduceMax;

	public final int getMinDamage() {
		return minDamage;
	}

	public final int getMaxDamage() {
		return maxDamage;
	}

	public final int getMeanDamage() {
		return (minDamage + maxDamage) / 2;
	}

	public final int getAttackSpeed() {
		return attackSpeed;
	}

	public final int getPhysicalCritical() {
		return physicalCritical;
	}

	public final int getPhysicalAccuracy() {
		return physicalAccuracy;
	}

	public final int getParry() {
		return parry;
	}

	public final int getMagicalAccuracy() {
		return magicalAccuracy;
	}

	public final int getBoostMagicalSkill() {
		return boostMagicalSkill;
	}

	public final int getAttackRange() {
		return attackRange;
	}

	public final int getHitCount() {
		return hitCount;
	}

	public final int getReduceMax() {
		return reduceMax;
	}
}
