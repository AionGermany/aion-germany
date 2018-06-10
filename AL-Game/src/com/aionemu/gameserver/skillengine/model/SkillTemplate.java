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
package com.aionemu.gameserver.skillengine.model;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.skillengine.action.Actions;
import com.aionemu.gameserver.skillengine.condition.ChainCondition;
import com.aionemu.gameserver.skillengine.condition.Condition;
import com.aionemu.gameserver.skillengine.condition.Conditions;
import com.aionemu.gameserver.skillengine.condition.DpCondition;
import com.aionemu.gameserver.skillengine.condition.HpCondition;
import com.aionemu.gameserver.skillengine.condition.PlayerMovedCondition;
import com.aionemu.gameserver.skillengine.condition.SkillChargeCondition;
import com.aionemu.gameserver.skillengine.effect.EffectTemplate;
import com.aionemu.gameserver.skillengine.effect.EffectType;
import com.aionemu.gameserver.skillengine.effect.Effects;
import com.aionemu.gameserver.skillengine.periodicaction.PeriodicActions;
import com.aionemu.gameserver.skillengine.properties.Properties;

/**
 * @author ATracer modified by Wakizashi
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "skillTemplate", propOrder = { "properties", "startconditions", "useconditions", "endconditions", "useequipmentconditions", "effects", "actions", "periodicActions", "motion" })
public class SkillTemplate {

	protected Properties properties;
	protected Conditions startconditions;
	protected Conditions useconditions;
	private Conditions endconditions;
	protected Conditions useequipmentconditions;
	protected Effects effects;
	protected Actions actions;
	@XmlElement(name = "periodicactions")
	protected PeriodicActions periodicActions;
	protected Motion motion;
	@XmlAttribute(name = "skill_id", required = true)
	protected int skillId;
	@XmlAttribute(required = true)
	protected String name;
	@XmlAttribute(name = "name_desc")
	private String namedesc;
	@XmlAttribute(required = true)
	protected int nameId;
	@XmlAttribute
	protected String stack = "NONE";
	@XmlAttribute
	protected int cooldownId;
	@XmlAttribute
	protected int lvl;
	@XmlAttribute(name = "skilltype", required = true)
	protected SkillType type = SkillType.NONE;
	@XmlAttribute(name = "skillsubtype", required = true)
	protected SkillSubType subType;
	@XmlAttribute(name = "tslot")
	protected SkillTargetSlot targetSlot;
	@XmlAttribute(name = "tslot_level")
	protected int targetSlotLevel;
	@XmlAttribute(name = "toggle_timer")
	protected int toggleTimer;
	@XmlAttribute(name = "dispel_category")
	protected DispelCategoryType dispelCategory = DispelCategoryType.NONE;
	@XmlAttribute(name = "req_dispel_level")
	protected int reqDispelLevel;
	@XmlAttribute(name = "activation", required = true)
	protected ActivationAttribute activationAttribute;
	@XmlAttribute(required = true)
	protected int duration;
	@XmlAttribute(name = "cooldown")
	protected int cooldown;
	@XmlAttribute(name = "penalty_skill_id")
	protected int penaltySkillId;
	@XmlAttribute(name = "pvp_damage")
	protected int pvpDamage;
	@XmlAttribute(name = "pvp_duration")
	protected int pvpDuration;
	@XmlAttribute(name = "chain_skill_prob")
	protected int chainSkillProb = 100;
	@XmlAttribute(name = "cancel_rate")
	protected int cancelRate;
	@XmlAttribute(name = "stance")
	protected boolean stance;
	@XmlAttribute(name = "avatar")
	protected boolean isDeityAvatar;
	@XmlAttribute(name = "ground")
	protected boolean isGroundSkill;// TODO remove!
	@XmlAttribute(name = "unpottable")
	protected boolean isUndispellableByPotions;
	@XmlAttribute(name = "ammospeed")
	protected int ammoSpeed;
	@XmlAttribute(name = "conflict_id")
	protected int conflictId;
	@XmlAttribute(name = "counter_skill")
	protected AttackStatus counterSkill = null;
	@XmlAttribute(name = "noremoveatdie")
	protected boolean noRemoveAtDie = false;
	@XmlAttribute(name = "boost_casting_time")
	protected boolean boostCastingTime = false;
	@XmlAttribute(name = "stigma")
	protected StigmaType stigmaType = StigmaType.NONE;
	@XmlTransient
	protected HashMap<Integer, Integer> effectIds = null;
	@XmlAttribute(name="skill_group")
	private String skill_group;

	/**
	 * @return the Properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Gets the value of the startconditions property.
	 *
	 * @return possible object is {@link Conditions }
	 */
	public Conditions getStartconditions() {
		return startconditions;
	}

	public int getToggleTimer() {
		return toggleTimer;
	}

	/**
	 * Gets the value of the useconditions property.
	 *
	 * @return possible object is {@link Conditions }
	 */
	public Conditions getUseconditions() {
		return useconditions;
	}

	/**
	 * Gets the value of the useequipmentconditions property.
	 *
	 * @return possible object is {@link Conditions }
	 */
	public Conditions getUseEquipmentconditions() {
		return useequipmentconditions;
	}

	/**
	 * Gets the value of the effects property.
	 *
	 * @return possible object is {@link Effects }
	 */
	public Effects getEffects() {
		return effects;
	}

	/**
	 * Gets the value of the actions property.
	 *
	 * @return possible object is {@link Actions }
	 */
	public Actions getActions() {
		return actions;
	}

	/**
	 * Gets the value of the periodicActions property.
	 *
	 * @return possible object is {@link PeriodicActions }
	 */
	public PeriodicActions getPeriodicActions() {
		return periodicActions;
	}

	/**
	 * Gets the value of the motion property.
	 *
	 * @return possible object is {@link Motion }
	 */
	public Motion getMotion() {
		return motion;
	}

	/**
	 * Gets the value of the skillId property.
	 */
	public int getSkillId() {
		return skillId;
	}

	/**
	 * Gets the value of the name property.
	 *
	 * @return possible object is {@link String }
	 */
	public String getName() {
		return name;
	}

	public String getNamedesc() {
		return namedesc;
	}

	/**
	 * @return the nameId
	 */
	public int getNameId() {
		return nameId;
	}

	/**
	 * @return the stack
	 */
	public String getStack() {
		return stack;
	}

	/**
	 * @return SkillGroup
	 */
	public String getSkillGroup() {
		return skill_group;
	}

	/**
	 * @return the lvl
	 */
	public int getLvl() {
		return lvl;
	}

	/**
	 * Gets the value of the type property.
	 *
	 * @return possible object is {@link SkillType }
	 */
	public SkillType getType() {
		return type;
	}

	/**
	 * @return the subType
	 */
	public SkillSubType getSubType() {
		return subType;
	}

	/**
	 * @return the targetSlot
	 */
	public SkillTargetSlot getTargetSlot() {
		return targetSlot;
	}

	/**
	 * @return the targetSlot Level
	 */
	public int getTargetSlotLevel() {
		return targetSlotLevel;
	}

	/**
	 * @return the dispelCategory
	 */
	public DispelCategoryType getDispelCategory() {
		return dispelCategory;
	}

	/**
	 * @return the reqDispelLevel
	 */
	public int getReqDispelLevel() {
		return reqDispelLevel;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @return the stigmaType
	 */
	public StigmaType getStigmaType() {
		return stigmaType;
	}

	/**
	 * @return the activationAttribute
	 */
	public ActivationAttribute getActivationAttribute() {
		return activationAttribute;
	}

	public boolean isPassive() {
		return activationAttribute == ActivationAttribute.PASSIVE;
	}

	public boolean isToggle() {
		return activationAttribute == ActivationAttribute.TOGGLE;
	}

	public boolean isProvoked() {
		return activationAttribute == ActivationAttribute.PROVOKED;
	}

	public boolean isMaintain() {
		return activationAttribute == ActivationAttribute.MAINTAIN;
	}

	public boolean isActive() {
		return activationAttribute == ActivationAttribute.ACTIVE;
	}

	public boolean isCharge() {
		return activationAttribute == ActivationAttribute.CHARGE;
	}

	/**
	 * @param position
	 * @return EffectTemplate
	 */
	public EffectTemplate getEffectTemplate(int position) {
		return effects != null && effects.getEffects().size() >= position ? effects.getEffects().get(position - 1) : null;

	}

	/**
	 * @return the cooldown
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * @return the penaltySkillId
	 */
	public int getPenaltySkillId() {
		return penaltySkillId;
	}

	/**
	 * @return the pvpDamage
	 */
	public int getPvpDamage() {
		return pvpDamage;
	}

	/**
	 * @return the pvpDuration
	 */
	public int getPvpDuration() {
		return pvpDuration;
	}

	/**
	 * @return chainSkillProb
	 */
	public int getChainSkillProb() {
		return chainSkillProb;
	}

	/**
	 * @return cancelRate
	 */
	public int getCancelRate() {
		return cancelRate;
	}

	/**
	 * @return stance
	 */
	public boolean isStance() {
		return stance;
	}

	public boolean hasResurrectEffect() {
		return getEffects() != null && getEffects().isResurrect();
	}

	public boolean hasItemHealFpEffect() {
		return getEffects() != null && getEffects().isEffectTypePresent(EffectType.PROCFPHEALINSTANT);
	}

	public boolean hasEvadeEffect() {
		return getEffects() != null && getEffects().isEffectTypePresent(EffectType.EVADE);
	}

	public boolean hasRecallInstant() {
		return getEffects() != null && getEffects().isEffectTypePresent(EffectType.RECALLINSTANT);
	}

	public boolean hasHealEffect() {
		return getEffects() != null && (getEffects().isEffectTypePresent(EffectType.HEAL) || getEffects().isEffectTypePresent(EffectType.HEALINSTANT));
	}

	public boolean hasRandomMoveEffect() {
		return getEffects() != null && getEffects().isEffectTypePresent(EffectType.RANDOMMOVELOC) && (getSkillId() != 3818 || getSkillId() != 3853); // all move loc except hypergate detonation
	}

	public int getCooldownId() {
		return (cooldownId > 0) ? cooldownId : skillId;
	}

	public boolean isDeityAvatar() {
		return isDeityAvatar;
	}

	public boolean isGroundSkill() {
		return isGroundSkill;
	}

	public AttackStatus getCounterSkill() {
		return counterSkill;
	}

	public boolean isUndispellableByPotions() {
		return isUndispellableByPotions;
	}

	public int getAmmoSpeed() {
		return ammoSpeed;
	}

	public int getConflictId() {
		return conflictId;
	}

	public boolean isNoRemoveAtDie() {
		return noRemoveAtDie;
	}

	public int getEffectsDuration(int skillLevel) {
		int duration = 0;
		Iterator<EffectTemplate> itr = getEffects().getEffects().iterator();
		while (itr.hasNext() && duration == 0) {
			EffectTemplate et = itr.next();
			int effectDuration = et.getDuration2() + et.getDuration1() * skillLevel;
			if (et.getRandomTime() > 0) {
				effectDuration -= Rnd.get(et.getRandomTime());
			}
			duration = duration > effectDuration ? duration : effectDuration;
		}

		return duration;
	}

	public ChainCondition getChainCondition() {
		if (startconditions != null) {
			for (Condition cond : startconditions.getConditions()) {
				if (cond instanceof ChainCondition) {
					return (ChainCondition) cond;
				}
			}
		}

		return null;
	}

	public SkillChargeCondition getSkillChargeCondition() {
		if (startconditions != null) {
			for (Condition cond : startconditions.getConditions()) {
				if (cond instanceof SkillChargeCondition) {
					return (SkillChargeCondition) cond;
				}
			}
		}

		return null;
	}

	public HashMap<Integer, Integer> getEffectIds() {
		return this.effectIds;
	}

	void afterUnmarshal(Unmarshaller u, Object parent) {
		if (this.getEffects() != null && this.getEffects().getEffects() != null) {
			for (EffectTemplate et : this.getEffects().getEffects()) {
				if (et.getEffectid() != 0) {
					if (effectIds == null) {
						effectIds = new HashMap<Integer, Integer>();
					}

					effectIds.put(et.getEffectid(), et.getBasicLvl());
				}
			}
		}
	}

	/**
	 * @return
	 */
	public HpCondition getHpCondition() {
		for (Condition c : startconditions.getConditions()) {
			if (c instanceof HpCondition) {
				return ((HpCondition) c);
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public DpCondition getDpCondition() {
		for (Condition c : startconditions.getConditions()) {
			if (c instanceof DpCondition) {
				return ((DpCondition) c);
			}
		}
		return null;
	}

	public PlayerMovedCondition getMovedCondition() {
		for (Condition c : startconditions.getConditions()) {
			if (c instanceof PlayerMovedCondition) {
				return ((PlayerMovedCondition) c);
			}
		}
		return null;
	}

	public Conditions getEndConditions() {
		return endconditions;
	}

	public void setNoRemoveAtDie(boolean noRemoveAtDie) {
		this.noRemoveAtDie = noRemoveAtDie;
	}

	public boolean isBoostCastingTime() {
		return boostCastingTime;
	}
}
