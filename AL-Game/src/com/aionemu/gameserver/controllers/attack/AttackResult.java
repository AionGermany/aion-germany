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
package com.aionemu.gameserver.controllers.attack;

import com.aionemu.gameserver.skillengine.model.HitType;

/**
 * @author ATracer modified by Sippolo, kecimis
 */
public class AttackResult {

	private int damage;
	private AttackStatus attackStatus;
	private HitType hitType = HitType.EVERYHIT;
	/**
	 * shield effects related
	 */
	private int shieldType;
	private int reflectedDamage = 0;
	private int reflectedSkillId = 0;
	private int protectedSkillId = 0;
	private int protectedDamage = 0;
	private int protectorId = 0;
	private int shieldMp = 0;
	private boolean launchSubEffect = true;

	public AttackResult(int damage, AttackStatus attackStatus) {
		this.damage = damage;
		this.attackStatus = attackStatus;
	}

	public AttackResult(int damage, AttackStatus attackStatus, HitType type) {
		this(damage, attackStatus);
		this.hitType = type;
	}

	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * @param damage
	 *            the damage to set
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * @return the attackStatus
	 */
	public AttackStatus getAttackStatus() {
		return attackStatus;
	}

	/**
	 * @return the Damage Type
	 */
	public HitType getDamageType() {
		return hitType;
	}

	/**
	 * @param type
	 *            the Damage Type to set
	 */
	public void setDamageType(HitType type) {
		this.hitType = type;
	}

	/**
	 * shield effects related
	 */
	/**
	 * @return the shieldType
	 */
	public int getShieldType() {
		return shieldType;
	}

	/**
	 * @param shieldType
	 *            the shieldType to set
	 */
	public void setShieldType(int shieldType) {
		this.shieldType |= shieldType;
	}

	public int getReflectedDamage() {
		return this.reflectedDamage;
	}

	public void setReflectedDamage(int reflectedDamage) {
		this.reflectedDamage = reflectedDamage;
	}

	public int getReflectedSkillId() {
		return this.reflectedSkillId;
	}

	public void setReflectedSkillId(int skillId) {
		this.reflectedSkillId = skillId;
	}

	public int getProtectedSkillId() {
		return this.protectedSkillId;
	}

	public void setProtectedSkillId(int skillId) {
		this.protectedSkillId = skillId;
	}

	public int getProtectedDamage() {
		return this.protectedDamage;
	}

	public void setProtectedDamage(int protectedDamage) {
		this.protectedDamage = protectedDamage;
	}

	public int getProtectorId() {
		return this.protectorId;
	}

	public void setProtectorId(int protectorId) {
		this.protectorId = protectorId;
	}

	public boolean isLaunchSubEffect() {
		return launchSubEffect;
	}

	public void setLaunchSubEffect(boolean launchSubEffect) {
		this.launchSubEffect = launchSubEffect;
	}

	public int getShieldMp() {
		return this.shieldMp;
	}

	public void setShieldMp(int shieldMp) {
		this.shieldMp = shieldMp;
	}
}
