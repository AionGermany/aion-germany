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
package com.aionemu.gameserver.skillengine.effect;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.SkillElement;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.skillengine.change.Change;
import com.aionemu.gameserver.skillengine.condition.Conditions;
import com.aionemu.gameserver.skillengine.effect.modifier.ActionModifier;
import com.aionemu.gameserver.skillengine.effect.modifier.ActionModifiers;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.HitType;
import com.aionemu.gameserver.skillengine.model.HopType;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.skillengine.model.SkillType;
import com.aionemu.gameserver.skillengine.model.SpellStatus;
import com.aionemu.gameserver.skillengine.model.TransformType;
import com.aionemu.gameserver.utils.stats.StatFunctions;

import javolution.util.FastList;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Effect")
public abstract class EffectTemplate {

	protected ActionModifiers modifiers;
	protected List<Change> change;
	@XmlAttribute
	protected int effectid;
	@XmlAttribute(required = true)
	protected int duration2;
	@XmlAttribute
	protected int duration1;
	@XmlAttribute(name = "randomtime")
	protected int randomTime;
	@XmlAttribute(name = "e")
	protected int position;
	@XmlAttribute(name = "basiclvl")
	protected int basicLvl;
	@XmlAttribute(name = "hittype", required = false)
	protected HitType hitType = HitType.EVERYHIT;
	@XmlAttribute(name = "hittypeprob2", required = false)
	protected float hitTypeProb = 100f;
	@XmlAttribute(name = "element")
	protected SkillElement element = SkillElement.NONE;
	@XmlElement(name = "subeffect")
	protected SubEffect subEffect;
	@XmlElement(name = "conditions")
	protected Conditions effectConditions;
	@XmlElement(name = "subconditions")
	protected Conditions effectSubConditions;
	@XmlAttribute(name = "hoptype")
	protected HopType hopType;
	@XmlAttribute(name = "hopa")
	protected int hopA; // effects the agro-value (hate)
	@XmlAttribute(name = "hopb")
	protected int hopB; // effects the agro-value (hate)
	@XmlAttribute(name = "noresist")
	protected boolean noResist;
	@XmlAttribute(name = "accmod1")
	protected int accMod1;// accdelta
	@XmlAttribute(name = "accmod2")
	protected int accMod2;// accvalue
	@XmlAttribute(name = "preeffect")
	protected String preEffect;
	@XmlAttribute(name = "preeffect_prob")
	protected int preEffectProb = 100;
	@XmlAttribute(name = "critprobmod2")
	protected int critProbMod2 = 100;
	@XmlAttribute(name = "critadddmg1")
	protected int critAddDmg1 = 0;
	@XmlAttribute(name = "critadddmg2")
	protected int critAddDmg2 = 0;
	@XmlAttribute
	protected int value;
	@XmlAttribute
	protected int delta;
	@XmlTransient
	protected EffectType effectType = null;
	@XmlTransient
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the delta
	 */
	public int getDelta() {
		return delta;
	}

	/**
	 * @return the duration2
	 */
	public int getDuration2() {
		return duration2;
	}

	/**
	 * @return the duration1
	 */
	public int getDuration1() {
		return duration1;
	}

	/**
	 * @return the randomtime
	 */
	public int getRandomTime() {
		return randomTime;
	}

	/**
	 * @return the modifiers
	 */
	public ActionModifiers getModifiers() {
		return modifiers;
	}

	/**
	 * @return the change
	 */
	public List<Change> getChange() {
		return change;
	}

	/**
	 * @return the effectid
	 */
	public int getEffectid() {
		return effectid;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return the basicLvl
	 */
	public int getBasicLvl() {
		return basicLvl;
	}

	/**
	 * @return the element
	 */
	public SkillElement getElement() {
		return element;
	}

	/**
	 * @return the preEffect
	 */
	public String getPreEffect() {
		return preEffect;
	}

	/**
	 * @return the preEffectProb
	 */
	public int getPreEffectProb() {
		return preEffectProb;
	}

	/**
	 * @return the critProbMod2
	 */
	public int getCritProbMod2() {
		return critProbMod2;
	}

	/**
	 * @return the critAddDmg1
	 */
	public int getCritAddDmg1() {
		return critAddDmg1;
	}

	/**
	 * @return the critAddDmg2
	 */
	public int getCritAddDmg2() {
		return critAddDmg2;
	}

	/**
	 * Gets the effect conditions status
	 *
	 * @return list of Conditions for effect template
	 */
	public Conditions getEffectConditions() {
		return effectConditions;
	}

	/**
	 * Gets the sub effect conditions status
	 *
	 * @return list of Conditions for sub effects within effect template
	 */
	public Conditions getEffectSubConditions() {
		return effectSubConditions;
	}

	/**
	 * @param value
	 * @return
	 */
	protected ActionModifier getActionModifiers(Effect effect) {
		if (modifiers == null) {
			return null;
		}

		/**
		 * Only one of modifiers will be applied now
		 */
		for (ActionModifier modifier : modifiers.getActionModifiers()) {
			if (modifier.check(effect)) {
				return modifier;
			}
		}

		return null;
	}

	/**
	 * @return the effectType
	 */
	public EffectType getEffectType() {
		return effectType;
	}

	/**
	 * @return the subEffect
	 */
	public SubEffect getSubEffect() {
		return subEffect;
	}

	/**
	 * Calculate effect result
	 *
	 * @param effect
	 */
	public void calculate(Effect effect) {
		calculate(effect, null, null);
	}

	/**
	 * 1) check conditions 2) check preeffect 3) check effectresistrate 4) check noresist 5) decide if its magical or physical effect 6) physical - check cannotmiss 7) check magic resist / dodge 8)
	 * addsuccess
	 * <p/>
	 * exceptions: buffbind buffsilence buffsleep buffstun randommoveloc recallinstant returneffect returnpoint shieldeffect signeteffect summoneffect xpboosteffect
	 *
	 * @param effect
	 * @param statEnum
	 * @param spellStatus
	 */
	public boolean calculate(Effect effect, StatEnum statEnum, SpellStatus spellStatus) {
		if (effect.getSkillTemplate().isPassive()) {
			this.addSuccessEffect(effect, spellStatus);
			return true;
		}

		if (statEnum != null && isAlteredState(statEnum) && isImuneToAbnormal(effect, statEnum)) {
			return false;
		}

		// dont check for forced effect
		if (effect.getIsForcedEffect()) {
			this.addSuccessEffect(effect, spellStatus);
			return true;
		}

		// check conditions
		if (!effectConditionsCheck(effect)) {
			return false;
		}

		// preeffects
		if (this.getPosition() > 1) {
			FastList<Integer> positions = getPreEffects();
			for (int pos : positions) {
				if (!effect.isInSuccessEffects(pos)) {
					return false;
				}
			}

			// check preeffect probability
			if (Rnd.get(0, 100) > this.getPreEffectProb()) {
				return false;
			}
		}

		// check effectresistrate
		if (!this.calculateEffectResistRate(effect, statEnum)) {
			if (!effect.isDamageEffect()) {
				effect.clearSucessEffects();
			}

			effect.setAttackStatus(AttackStatus.BUF);
			return false;
		}

		SkillType skillType = effect.getSkillType();
		// certain effects are magical by default
		if (isMagicalEffectTemp()) {
			skillType = SkillType.MAGICAL;
		}

		boolean cannotMiss = false;
		if (this instanceof SkillAttackInstantEffect) {
			cannotMiss = ((SkillAttackInstantEffect) this).isCannotmiss();
		}
		if (!noResist && !cannotMiss) {
			// check for BOOST_RESIST
			int boostResist = 0;
			switch (effect.getSkillTemplate().getSubType()) {
				case DEBUFF:
					boostResist = effect.getEffector().getGameStats().getStat(StatEnum.BOOST_RESIST_DEBUFF, 0).getCurrent();
					break;
				default:
					break;
			}

			int accMod = accMod2 + accMod1 * effect.getSkillLevel() + effect.getAccModBoost() + boostResist;
			switch (skillType) {
				case PHYSICAL:
					if (effect.getEffector() instanceof Player) {
						Player player = (Player) effect.getEffector();
						if (player.getPlayerClass() == PlayerClass.GUNNER || player.getPlayerClass() == PlayerClass.RIDER) {
							if (Rnd.get(0, 1000) < StatFunctions.calculateMagicalResistRate(effect.getEffector(), effect.getEffected(), accMod)) {
								return false;
							}
						}
						else {
							if (StatFunctions.calculatePhysicalDodgeRate(effect.getEffector(), effect.getEffected(), accMod)) {
								return false;
							}
						}
					}
					else {
						if (StatFunctions.calculatePhysicalDodgeRate(effect.getEffector(), effect.getEffected(), accMod)) {
							return false;
						}
					}
					break;
				case MAGICAL:
					if (Rnd.get(0, 1000) < StatFunctions.calculateMagicalResistRate(effect.getEffector(), effect.getEffected(), accMod)) {
						return false;
					}
					break;
				case ALL:
					if (effect.getEffector() instanceof Player) {
						Player player = (Player) effect.getEffector();
						if (player.getPlayerClass() == PlayerClass.GUNNER || player.getPlayerClass() == PlayerClass.RIDER) {
							if (Rnd.get(0, 1000) < StatFunctions.calculateMagicalResistRate(effect.getEffector(), effect.getEffected(), accMod)) {
								return false;
							}
						}
						else {
							if (StatFunctions.calculatePhysicalDodgeRate(effect.getEffector(), effect.getEffected(), accMod)) {
								return false;
							}
						}
					}
					else {
						if (StatFunctions.calculatePhysicalDodgeRate(effect.getEffector(), effect.getEffected(), accMod)) {
							return false;
						}
					}
					if (Rnd.get(0, 1000) < StatFunctions.calculateMagicalResistRate(effect.getEffector(), effect.getEffected(), accMod)) {
						return false;
					}
					break;
				default:
					break;
			}
		}

		this.addSuccessEffect(effect, spellStatus);
		return true;
	}

	private void addSuccessEffect(Effect effect, SpellStatus spellStatus) {
		effect.addSucessEffect(this);
		if (spellStatus != null) {
			effect.setSpellStatus(spellStatus);
		}
	}

	/**
	 * Check all condition statuses for effect template
	 */
	private boolean effectConditionsCheck(Effect effect) {
		Conditions effectConditions = getEffectConditions();
		return effectConditions != null ? effectConditions.validate(effect) : true;
	}

	private FastList<Integer> getPreEffects() {
		FastList<Integer> preEffects = new FastList<Integer>();

		if (this.getPreEffect() == null) {
			return preEffects;
		}

		String[] parts = this.getPreEffect().split("_");
		for (String part : parts) {
			preEffects.add(Integer.parseInt(part));
		}

		return preEffects;
	}

	/**
	 * Apply effect to effected
	 *
	 * @param effect
	 */
	public abstract void applyEffect(Effect effect);

	/**
	 * Start effect on effected
	 *
	 * @param effect
	 */
	public void startEffect(Effect effect) {
	}

	;

	/**
	 * @param effect
	 */
	public void calculateSubEffect(Effect effect) {
		if (subEffect == null) {
			return;
		}
		// Pre-Check for sub effect conditions
		if (!effectSubConditionsCheck(effect)) {
			effect.setSubEffectAborted(true);
			return;
		}

		// chance to trigger subeffect
		if (Rnd.get(100) > subEffect.getChance()) {
			return;
		}

		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(subEffect.getSkillId());
		int level = 1;
		if (subEffect.isAddEffect()) {
			level = effect.getSignetBurstedCount();
		}
		Effect newEffect = new Effect(effect.getEffector(), effect.getEffected(), template, level, 0);
		newEffect.setAccModBoost(effect.getAccModBoost());
		newEffect.initialize();
		if (newEffect.getSpellStatus() != SpellStatus.DODGE && newEffect.getSpellStatus() != SpellStatus.RESIST) {
			effect.setSpellStatus(newEffect.getSpellStatus());
		}
		effect.setSubEffect(newEffect);
		effect.setSkillMoveType(newEffect.getSkillMoveType());
		effect.setTargetLoc(newEffect.getTargetX(), newEffect.getTargetY(), newEffect.getTargetZ());
	}

	/**
	 * Check all sub effect condition statuses for effect
	 */
	private boolean effectSubConditionsCheck(Effect effect) {
		return effectSubConditions != null ? effectSubConditions.validate(effect) : true;
	}

	/**
	 * Hate will be added to result value only if particular effect template has success result
	 *
	 * @param effect
	 */
	public void calculateHate(Effect effect) {
		if (hopType == null) {
			return;
		}

		if (effect.getSuccessEffect().isEmpty()) {
			return;
		}

		int currentHate = effect.getEffectHate();
		if (hopType != null) {
			switch (hopType) {
				case DAMAGE:
					currentHate += effect.getReserved1();
					break;
				case SKILLLV:
					int skillLvl = effect.getSkillLevel();
					currentHate += hopB + hopA * skillLvl; // Agro-value of the effect
				default:
					break;
			}
		}
		if (currentHate == 0) {
			currentHate = 1;
		}
		effect.setEffectHate(StatFunctions.calculateHate(effect.getEffector(), currentHate));
	}

	/**
	 * @param effect
	 */
	public void startSubEffect(Effect effect) {
		if (subEffect == null) {
			return;
		}

		// Apply-Check for sub effect conditions
		if (effect.isSubEffectAbortedBySubConditions()) {
			return;
		}
		if (effect.getSubEffect() != null) {
			effect.getSubEffect().applyEffect();
		}
	}

	/**
	 * Do periodic effect on effected
	 *
	 * @param effect
	 */
	public void onPeriodicAction(Effect effect) {
	}

	/**
	 * End effect on effected
	 *
	 * @param effect
	 */
	public void endEffect(Effect effect) {
	}

	/**
	 * @param effect
	 * @param statEnum
	 * @return true = no resist, false = resisted
	 */
	public boolean calculateEffectResistRate(Effect effect, StatEnum statEnum) {
		if (effect.getEffected() == null || effect.getEffected().getGameStats() == null || effect.getEffector() == null || effect.getEffector().getGameStats() == null) {
			return false;
		}

		Creature effected = effect.getEffected();
		Creature effector = effect.getEffector();

		if (statEnum == null) {
			return true;
		}

		int effectPower = 1000;

		if (isAlteredState(statEnum)) {
			effectPower -= effect.getEffected().getGameStats().getStat(StatEnum.ABNORMAL_RESISTANCE_ALL, 0).getCurrent();
		}

		// effect resistance
		effectPower -= effect.getEffected().getGameStats().getStat(statEnum, 0).getCurrent();

		// penetration
		StatEnum penetrationStat = this.getPenetrationStat(statEnum);
		if (penetrationStat != null) {
			effectPower += effector.getGameStats().getStat(penetrationStat, 0).getCurrent();
		}

		// resist mod pvp
		if (effector.isPvpTarget(effect.getEffected())) {
			int differ = (effected.getLevel() - effector.getLevel());
			if (differ > 2 && differ < 8) {
				effectPower -= Math.round((effectPower * (differ - 2) / 15f));
			}
			else if (differ >= 8) {
				effectPower *= 0.1f;
			}
		}

		// resist mod PvE
		if (effect.getEffected() instanceof Npc) {
			Npc effectrd = (Npc) effect.getEffected();
			int hpGaugeMod = effectrd.getObjectTemplate().getRank().ordinal() - 1;
			effectPower -= hpGaugeMod * 100;
		}

		return Rnd.get(1000) <= effectPower;
	}

	private boolean isImuneToAbnormal(Effect effect, StatEnum statEnum) {
		Creature effected = effect.getEffected();
		if (effected != effect.getEffector()) {
			if (effected instanceof Npc) {
				Npc npc = (Npc) effected;
				if (npc.isBoss() || npc.hasStatic() || npc instanceof Kisk || npc.getAi2().ask(AIQuestion.CAN_RESIST_ABNORMAL).isPositive()) {
					return true;
				}
				if (npc.getObjectTemplate().getStatsTemplate().getRunSpeed() == 0) {
					if (statEnum == StatEnum.PULLED_RESISTANCE || statEnum == StatEnum.STAGGER_RESISTANCE || statEnum == StatEnum.STUMBLE_RESISTANCE) {
						return true;
					}
				}
			}
			if (effected.getTransformModel().getType() == TransformType.AVATAR) {
				if (statEnum == StatEnum.SLOW_RESISTANCE) {
					return true;
				}
			}
			int resist = effected.getGameStats().getStat(StatEnum.ABNORMAL_RESISTANCE_ALL, 0).getCurrent();
			return Rnd.get(1000) < resist;
		}
		return false;
	}

	/**
	 * @param statEnum
	 * @return true = it's an altered state effect, false = it is Poison/Bleed dot (normal Dots have statEnum null here)
	 */
	private boolean isAlteredState(StatEnum stat) {
		switch (stat) {
			case BIND_RESISTANCE:
			case BLIND_RESISTANCE:
			case CHARM_RESISTANCE:
			case CONFUSE_RESISTANCE:
			case CURSE_RESISTANCE:
			case DEFORM_RESISTANCE:
			case FEAR_RESISTANCE:
			case OPENAREIAL_RESISTANCE:
			case PARALYZE_RESISTANCE:
			case PULLED_RESISTANCE:
			case ROOT_RESISTANCE:
			case SILENCE_RESISTANCE:
			case SLEEP_RESISTANCE:
			case SLOW_RESISTANCE:
			case SNARE_RESISTANCE:
			case SPIN_RESISTANCE:
			case STAGGER_RESISTANCE:
			case STUMBLE_RESISTANCE:
			case STUN_RESISTANCE:
				return true;
			default:
				break;
		}
		return false;
	}

	private StatEnum getPenetrationStat(StatEnum statEnum) {
		switch (statEnum) {
			case BLEED_RESISTANCE:
				return StatEnum.BLEED_RESISTANCE_PENETRATION;
			case BLIND_RESISTANCE:
				return StatEnum.BLIND_RESISTANCE_PENETRATION;
			// case BIND_RESISTANCE:
			case CHARM_RESISTANCE:
				return StatEnum.CHARM_RESISTANCE_PENETRATION;
			case CONFUSE_RESISTANCE:
				return StatEnum.CONFUSE_RESISTANCE_PENETRATION;
			case CURSE_RESISTANCE:
				return StatEnum.CURSE_RESISTANCE_PENETRATION;
			// case DEFORM_RESISTANCE:
			case DISEASE_RESISTANCE:
				return StatEnum.DISEASE_RESISTANCE_PENETRATION;
			case FEAR_RESISTANCE:
				return StatEnum.FEAR_RESISTANCE_PENETRATION;
			case OPENAREIAL_RESISTANCE:
				return StatEnum.OPENAREIAL_RESISTANCE_PENETRATION;
			case PARALYZE_RESISTANCE:
				return StatEnum.PARALYZE_RESISTANCE_PENETRATION;
			case PERIFICATION_RESISTANCE:
				return StatEnum.PERIFICATION_RESISTANCE_PENETRATION;
			case POISON_RESISTANCE:
				return StatEnum.POISON_RESISTANCE_PENETRATION;
			case ROOT_RESISTANCE:
				return StatEnum.ROOT_RESISTANCE_PENETRATION;
			case SILENCE_RESISTANCE:
				return StatEnum.SILENCE_RESISTANCE_PENETRATION;
			case SLEEP_RESISTANCE:
				return StatEnum.SLEEP_RESISTANCE_PENETRATION;
			case SLOW_RESISTANCE:
				return StatEnum.SLOW_RESISTANCE_PENETRATION;
			case SNARE_RESISTANCE:
				return StatEnum.SNARE_RESISTANCE_PENETRATION;
			case SPIN_RESISTANCE:
				return StatEnum.SPIN_RESISTANCE_PENETRATION;
			case STAGGER_RESISTANCE:
				return StatEnum.STAGGER_RESISTANCE_PENETRATION;
			case STUMBLE_RESISTANCE:
				return StatEnum.STUMBLE_RESISTANCE_PENETRATION;
			case STUN_RESISTANCE:
				return StatEnum.STUN_RESISTANCE_PENETRATION;
			default:
				return null;
		}
	}

	/**
	 * certain effects are magical even when used in physical skills it includes stuns from chanter/sin/ranger etc these effects(effecttemplates) are dependent on magical accuracy and magical resist
	 *
	 * @return
	 */
	private boolean isMagicalEffectTemp() {
		if (this instanceof SilenceEffect || this instanceof SleepEffect || this instanceof RootEffect || this instanceof SnareEffect || this instanceof StunEffect || this instanceof PoisonEffect || this instanceof BindEffect || this instanceof BleedEffect || this instanceof BlindEffect || this instanceof DeboostHealEffect || this instanceof ParalyzeEffect || this instanceof SlowEffect) {
			return true;
		}

		return false;
	}

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		EffectType temp = null;
		try {
			temp = EffectType.valueOf(this.getClass().getName().replaceAll("com.aionemu.gameserver.skillengine.effect.", "").replaceAll("Effect", "").toUpperCase());
		}
		catch (Exception e) {
			log.info("missing effectype for " + this.getClass().getName().replaceAll("com.aionemu.gameserver.skillengine.effect.", "").replaceAll("Effect", "").toUpperCase());
		}

		this.effectType = temp;
	}
}
