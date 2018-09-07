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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.AttackCalcObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STANCE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_ACTIVATION;
import com.aionemu.gameserver.skillengine.condition.Conditions;
import com.aionemu.gameserver.skillengine.effect.DamageEffect;
import com.aionemu.gameserver.skillengine.effect.DelayedSpellAttackInstantEffect;
import com.aionemu.gameserver.skillengine.effect.EffectTemplate;
import com.aionemu.gameserver.skillengine.effect.Effects;
import com.aionemu.gameserver.skillengine.effect.FearEffect;
import com.aionemu.gameserver.skillengine.effect.HideEffect;
import com.aionemu.gameserver.skillengine.effect.ParalyzeEffect;
import com.aionemu.gameserver.skillengine.effect.PetOrderUseUltraSkillEffect;
import com.aionemu.gameserver.skillengine.effect.SanctuaryEffect;
import com.aionemu.gameserver.skillengine.effect.SummonEffect;
import com.aionemu.gameserver.skillengine.effect.TransformEffect;
import com.aionemu.gameserver.skillengine.periodicaction.PeriodicAction;
import com.aionemu.gameserver.skillengine.periodicaction.PeriodicActions;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import javolution.util.FastMap;

/**
 * @author ATracer
 * @modified by Wakizashi
 * @modified by Sippolo
 * @modified by kecimis
 * @Reworked Kill3r (updated 4.8)
 */
public class Effect implements StatOwner {

	private Skill skill;
	private SkillTemplate skillTemplate;
	private int skillLevel;
	private int duration;
	private long endTime;
	private PeriodicActions periodicActions;
	private SkillMoveType skillMoveType = SkillMoveType.DEFAULT;
	private Creature effected;
	private Creature effector;
	private Future<?> task = null;
	private Future<?>[] periodicTasks = null;
	private Future<?> periodicActionsTask = null;
	private boolean isHideEffect = false;
	private boolean isParalyzeEffect = false;
	private boolean isSanctuaryEffect = false;
	private float targetX = 0;
	private float targetY = 0;
	private float targetZ = 0;
	private int mpShield = 0;
	private boolean isPhysicalState = false;
	private boolean isMagicalState = false;
	/**
	 * Used for damage/heal values
	 */
	private int reserved1;
	/**
	 * Used for shield total hit damage;
	 */
	private int reserved2;
	/**
	 * Used for shield hit damage
	 */
	private int reserved3;
	/**
	 * Used for tick heals from HoT's (Heal Over Time)
	 */
	private int reserved4;
	/**
	 * Used for tick damages from DoT's (Damage Over Time)
	 */
	private int reserved5;
	private int[] reservedInts;
	/**
	 * Spell Status 1 : stumble 2 : knockback 4 : open aerial 8 : close aerial 16 : spin 32 : block 64 : parry 128 : dodge 256 : resist
	 */
	private SpellStatus spellStatus = SpellStatus.NONE;
	private DashStatus dashStatus = DashStatus.NONE;
	private AttackStatus attackStatus = AttackStatus.NORMALHIT;
	/**
	 * shield effects related
	 */
	private int shieldDefense;
	private int reflectedDamage = 0;
	private int reflectedSkillId = 0;
	private int protectedSkillId = 0;
	private int protectedDamage = 0;
	private int protectorId = 0;
	private boolean addedToController;
	private AttackCalcObserver[] attackStatusObserver;
	private AttackCalcObserver[] attackShieldObserver;
	private boolean launchSubEffect = true;
	private Effect subEffect;
	private boolean isStopped;
	private boolean isDelayedDamage;
	private boolean isDamageEffect;
	private boolean isPetOrder;
	private boolean isSummoning;
	// Xp Boost
	private boolean isXpBoost;
	// Ap Boost
	private boolean isApBoost;
	// Dr Boost
	private boolean isDrBoost;
	// Bdr Boost
	private boolean isBdrBoost;
	// Authorize Boost
	private boolean isAuthorizeBoost;
	// Enchant Boost
	private boolean isEnchantBoost;
	// Enchant Option Boost
	private boolean isEnchantOptionBoost;
	// Idun Drop Boost
	private boolean isIdunDropBoost;
	// New Effect
	private boolean isSprintFpReduce;
	private boolean isReturnCoolReduce;
	private boolean isOdellaRecoverIncrease;
	private boolean isCancelOnDmg;
	private boolean subEffectAbortedBySubConditions;
	private ItemTemplate itemTemplate;
	private boolean isDeathPenaltyReduce;
	private boolean isNoDeathPenalty;
	private boolean isNoDeathPenaltyReduce;
	private boolean isNoResurrectPenalty;
	private boolean isHiPass;
	/**
	 * Hate that will be placed on effected list
	 */
	private int tauntHate;
	/**
	 * Total hate that will be broadcasted
	 */
	private int effectHate;
	private Map<Integer, EffectTemplate> successEffects = new FastMap<Integer, EffectTemplate>().shared();
	private int carvedSignet = 0;
	private int signetBurstedCount = 0;
	protected int abnormals;
	/**
	 * Action observer that should be removed after effect end
	 */
	private ActionObserver[] actionObserver;
	float x, y, z;
	int worldId, instanceId;
	/**
	 * used to force duration, you should be very careful when to use it
	 */
	private boolean forcedDuration = false;
	private boolean isForcedEffect = false;
	/**
	 * power of effect ( used for dispels)
	 */
	private int power = 10;
	/**
	 * accModBoost used for SignetBurstEffect
	 */
	private int accModBoost = 0;
	private EffectResult effectResult = EffectResult.NORMAL;

	public final Skill getSkill() {
		return skill;
	}

	public void setAbnormal(int mask) {
		abnormals |= mask;
	}

	public int getAbnormals() {
		return abnormals;
	}

	public Effect(Creature effector, Creature effected, SkillTemplate skillTemplate, int skillLevel, int duration) {
		this.effector = effector;
		this.effected = effected;
		this.skillTemplate = skillTemplate;
		this.skillLevel = skillLevel;
		this.duration = duration;
		this.periodicActions = skillTemplate.getPeriodicActions();

		this.power = initializePower(skillTemplate);
	}

	public Effect(Creature effector, Creature effected, SkillTemplate skillTemplate, int skillLevel, int duration, ItemTemplate itemTemplate) {
		this(effector, effected, skillTemplate, skillLevel, duration);
		this.itemTemplate = itemTemplate;
	}

	public Effect(Skill skill, Creature effected, int duration, ItemTemplate itemTemplate) {
		this(skill.getEffector(), effected, skill.getSkillTemplate(), skill.getSkillLevel(), duration, itemTemplate);
		this.skill = skill;
	}

	public void setWorldPosition(int worldId, int instanceId, float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.worldId = worldId;
		this.instanceId = instanceId;
	}

	public int getEffectorId() {
		return effector.getObjectId();
	}

	public int getSkillId() {
		return skillTemplate.getSkillId();
	}

	public String getSkillName() {
		return skillTemplate.getName();
	}

	public final SkillTemplate getSkillTemplate() {
		return skillTemplate;
	}

	public SkillSubType getSkillSubType() {
		return skillTemplate.getSubType();
	}

	public String getStack() {
		return skillTemplate.getStack();
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public int getSkillStackLvl() {
		return skillTemplate.getLvl();
	}

	public SkillType getSkillType() {
		return skillTemplate.getType();
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int newDuration) {
		this.duration = newDuration;
	}

	public Creature getEffected() {
		return effected;
	}

	public Creature getEffector() {
		return effector;
	}

	public boolean isPassive() {
		return skillTemplate.isPassive();
	}

	public void setTask(Future<?> task) {
		this.task = task;
	}

	public Future<?> getPeriodicTask(int i) {
		return periodicTasks[i - 1];
	}

	public void setPeriodicTask(Future<?> periodicTask, int i) {
		if (periodicTasks == null) {
			periodicTasks = new Future<?>[4];
		}
		this.periodicTasks[i - 1] = periodicTask;
	}

	public int getReserved1() {
		return reserved1;
	}

	public void setReserved1(int reserved1) {
		this.reserved1 = reserved1;
	}

	public int getReserved2() {
		return reserved2;
	}

	public void setReserved2(int reserved2) {
		this.reserved2 = reserved2;
	}

	public int getReserved3() {
		return reserved3;
	}

	public void setReserved3(int reserved3) {
		this.reserved3 = reserved3;
	}

	public int getReserved4() {
		return reserved4;
	}

	public void setReserved4(int reserved4) {
		this.reserved4 = reserved4;
	}

	public int getReserved5() {
		return reserved5;
	}

	public void setReserved5(int reserved5) {
		this.reserved5 = reserved5;
	}

	public AttackStatus getAttackStatus() {
		return attackStatus;
	}

	public void setAttackStatus(AttackStatus attackStatus) {
		this.attackStatus = attackStatus;
	}

	public List<EffectTemplate> getEffectTemplates() {
		return skillTemplate.getEffects().getEffects();
	}

	public boolean isMphealInstant() {
		Effects effects = skillTemplate.getEffects();
		return effects != null && effects.isMpHealInstant();
	}

	public boolean isToggle() {
		return skillTemplate.getActivationAttribute() == ActivationAttribute.TOGGLE;
	}

	public boolean isChant() {
		return skillTemplate.getTargetSlot() == SkillTargetSlot.CHANT;
	}

	public boolean isRangerEye() {
		int skillId = skillTemplate.getSkillId();
		switch (skillId) {
			case 796: // Strong Shots.
			case 809: // Dodging.
			case 813: // Focused Shots.
			case 888: // Hunter's Might.
			case 889: // Bestial Fury.
			case 1053: // Aiming.
			case 1099: // Hunter's Eye.
				return true;
			default:
				return false;
		}
	}

	public boolean isBuff() {
		return skillTemplate.getTargetSlot() == SkillTargetSlot.BUFF;
	}

	public int getTargetSlot() {
		return skillTemplate.getTargetSlot().ordinal();
	}

	public SkillTargetSlot getTargetSlotEnum() {
		return skillTemplate.getTargetSlot();
	}

	public int getTargetSlotLevel() {
		return skillTemplate.getTargetSlotLevel();
	}

	public DispelCategoryType getDispelCategory() {
		return skillTemplate.getDispelCategory();
	}

	public int getReqDispelLevel() {
		return skillTemplate.getReqDispelLevel();
	}

	/**
	 * @param i
	 * @return attackStatusObserver for this effect template or null
	 */
	public AttackCalcObserver getAttackStatusObserver(int i) {
		return attackStatusObserver != null ? attackStatusObserver[i - 1] : null;
	}

	/**
	 * @param attackStatusObserver
	 *            the attackCalcObserver to set
	 */
	public void setAttackStatusObserver(AttackCalcObserver attackStatusObserver, int i) {
		if (this.attackStatusObserver == null) {
			this.attackStatusObserver = new AttackCalcObserver[4];
		}
		this.attackStatusObserver[i - 1] = attackStatusObserver;
	}

	/**
	 * @param i
	 * @return attackShieldObserver for this effect template or null
	 */
	public AttackCalcObserver getAttackShieldObserver(int i) {
		return attackShieldObserver != null ? attackShieldObserver[i - 1] : null;
	}

	/**
	 * @param attackShieldObserver
	 *            the attackShieldObserver to set
	 */
	public void setAttackShieldObserver(AttackCalcObserver attackShieldObserver, int i) {
		if (this.attackShieldObserver == null) {
			this.attackShieldObserver = new AttackCalcObserver[4];
		}
		this.attackShieldObserver[i - 1] = attackShieldObserver;
	}

	public int getReservedInt(int i) {
		return reservedInts != null ? reservedInts[i - 1] : 0;
	}

	public void setReservedInt(int i, int value) {
		if (this.reservedInts == null) {
			this.reservedInts = new int[4];
		}
		this.reservedInts[i - 1] = value;
	}

	/**
	 * @return the launchSubEffect
	 */
	public boolean isLaunchSubEffect() {
		return launchSubEffect;
	}

	/**
	 * @param launchSubEffect
	 *            the launchSubEffect to set
	 */
	public void setLaunchSubEffect(boolean launchSubEffect) {
		this.launchSubEffect = launchSubEffect;
	}

	/**
	 * @return the shieldDefense
	 */
	public int getShieldDefense() {
		return shieldDefense;
	}

	/**
	 * @param shieldDefense
	 *            the shieldDefense to set
	 */
	public void setShieldDefense(int shieldDefense) {
		this.shieldDefense = shieldDefense;
	}

	/**
	 * reflected damage
	 *
	 * @return
	 */
	public int getReflectedDamage() {
		return this.reflectedDamage;
	}

	public void setReflectedDamage(int value) {
		this.reflectedDamage = value;
	}

	public int getReflectedSkillId() {
		return this.reflectedSkillId;
	}

	public void setReflectedSkillId(int value) {
		this.reflectedSkillId = value;
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

	/**
	 * @return the spellStatus
	 */
	public SpellStatus getSpellStatus() {
		return spellStatus;
	}

	/**
	 * @param spellStatus
	 *            the spellStatus to set
	 */
	public void setSpellStatus(SpellStatus spellStatus) {
		this.spellStatus = spellStatus;
	}

	/**
	 * @return the dashStatus
	 */
	public DashStatus getDashStatus() {
		return dashStatus;
	}

	/**
	 * @param dashStatus
	 *            the dashStatus to set
	 */
	public void setDashStatus(DashStatus dashStatus) {
		this.dashStatus = dashStatus;
	}

	/**
	 * Number of signets carved on target
	 *
	 * @return
	 */
	public int getCarvedSignet() {
		return this.carvedSignet;
	}

	public void setCarvedSignet(int value) {
		this.carvedSignet = value;
	}

	/**
	 * @return the subEffect
	 */
	public Effect getSubEffect() {
		return subEffect;
	}

	/**
	 * @param subEffect
	 *            the subEffect to set
	 */
	public void setSubEffect(Effect subEffect) {
		this.subEffect = subEffect;
	}

	/**
	 * @param effectId
	 * @return true or false
	 */
	public boolean containsEffectId(int effectId) {
		for (EffectTemplate template : successEffects.values()) {
			if (template.getEffectid() == effectId) {
				return true;
			}
		}
		return false;
	}

	public TransformType getTransformType() {
		for (EffectTemplate et : skillTemplate.getEffects().getEffects()) {
			if (et instanceof TransformEffect) {
				return ((TransformEffect) et).getTransformType();
			}
		}
		return TransformType.NONE;
	}

	public void setForcedDuration(boolean forcedDuration) {
		this.forcedDuration = forcedDuration;
	}

	public void setIsForcedEffect(boolean isForcedEffect) {
		this.isForcedEffect = isForcedEffect;
	}

	public boolean getIsForcedEffect() {
		return this.isForcedEffect || DataManager.MATERIAL_DATA.isMaterialSkill(this.getSkillId());
	}

	/**
	 * Correct lifecycle of Effect - INITIALIZE - APPLY - START - END
	 */
	/**
	 * Do initialization with proper calculations
	 */
	public void initialize() {
		if (skillTemplate.getEffects() == null) {
			System.out.println("Effect = NULL");
			return;
		}

		for (EffectTemplate template : getEffectTemplates()) {
			template.calculate(this);

			if (template instanceof DelayedSpellAttackInstantEffect) {
				setDelayedDamage(true);
			}
			if (template instanceof PetOrderUseUltraSkillEffect) {
				setPetOrder(true);
			}
			if (template instanceof SummonEffect) {
				setSumonning(true);
			}
			if (template instanceof DamageEffect) {
				setDamageEffect(true);
			}
			if (template instanceof HideEffect) {
				isHideEffect = true;
			}
			if (template instanceof ParalyzeEffect) {
				isParalyzeEffect = true;
			}
			if (template instanceof SanctuaryEffect) {
				isSanctuaryEffect = true;
			}
		}

		for (EffectTemplate template : getEffectTemplates()) {
			template.calculateHate(this);
		}
		if (this.isLaunchSubEffect()) {
			for (EffectTemplate template : successEffects.values()) {
				template.calculateSubEffect(this);
			}
		}

		if (successEffects.isEmpty()) {
			skillMoveType = SkillMoveType.RESIST;
			if (getSkillType() == SkillType.PHYSICAL) {
				if (getEffector() instanceof Player) {
					Player pl = (Player) getEffector();
					if (pl.getPlayerClass() == PlayerClass.GUNNER || pl.getPlayerClass() == PlayerClass.RIDER) {
						if (getAttackStatus() == AttackStatus.CRITICAL) {
							setAttackStatus(AttackStatus.CRITICAL_RESIST);// TODO recheck
						}
						else {
							setAttackStatus(AttackStatus.RESIST);
						}
					}
					else {
						if (getAttackStatus() == AttackStatus.CRITICAL) {
							setAttackStatus(AttackStatus.CRITICAL_DODGE);
						}
						else {
							setAttackStatus(AttackStatus.DODGE);
						}
					}
				}
				else {
					if (getAttackStatus() == AttackStatus.CRITICAL) {
						setAttackStatus(AttackStatus.CRITICAL_DODGE);
					}
					else {
						setAttackStatus(AttackStatus.DODGE);
					}
				}
			}
			else {
				if (getAttackStatus() == AttackStatus.CRITICAL) {
					setAttackStatus(AttackStatus.CRITICAL_RESIST);// TODO recheck
				}
				else {
					setAttackStatus(AttackStatus.RESIST);
				}
			}
		}

		// set spellstatus for sm_castspell_end packet
		switch (AttackStatus.getBaseStatus(getAttackStatus())) {
			case DODGE:
				setSpellStatus(SpellStatus.DODGE);
				break;
			case PARRY:
				if (getSpellStatus() == SpellStatus.NONE) {
					setSpellStatus(SpellStatus.PARRY);
				}
				break;
			case BLOCK:
				if (getSpellStatus() == SpellStatus.NONE) {
					setSpellStatus(SpellStatus.BLOCK);
				}
				break;
			case RESIST:
				setSpellStatus(SpellStatus.RESIST);
				break;
			default:
				break;
		}
	}

	/**
	 * Apply all effect templates
	 */
	public void applyEffect() {

		// TODO move it somewhere more appropriate
		// Fear is not applied on players who are gliding
		if (isFearEffect()) {
			if (getEffected().isInState(CreatureState.GLIDING)) {
				// Only if player is not in Flying mode
				// TODO: verify on retail if this check is needed
				if (getEffected() instanceof Player) {
					if (!((Player) getEffected()).isInFlyingMode()) {
						((Player) getEffected()).getFlyController().onStopGliding(true);
						return;
					}
				}
			}
		}

		/**
		 * broadcast final hate to all visible objects
		 */
		// TODO hostile_type?
		if (effectHate != 0) {
			if (getEffected() instanceof Npc && !isDelayedDamage() && !isPetOrder() && !isSummoning()) {
				getEffected().getAggroList().addHate(effector, 1);
			}

			effector.getController().broadcastHate(effectHate);
		}

		if (skillTemplate.getEffects() == null || successEffects.isEmpty()) {
			return;
		}

		for (EffectTemplate template : successEffects.values()) {
			if (getEffected() != null) {
				if (getEffected().getLifeStats().isAlreadyDead() && !skillTemplate.hasResurrectEffect()) {
					continue;
				}
			}
			template.applyEffect(this);
			template.startSubEffect(this);
		}
	}

	public boolean isRiderEffect(int skillId) {
		switch (skillId) {
			case 2767: // Embark
			case 2768:
			case 2769:
			case 2770:
			case 2771:
			case 2772:
			case 2773:
			case 2774:
			case 2775:
			case 2776:
			case 2777:
			case 2778:
			case 2440: // Kinetic Battery
			case 2441:
			case 2442:
			case 2443:
			case 2444:
			case 2445:
			case 2446:
			case 2447:
			case 2448:
			case 2449:
			case 2579: // Kinetic Bulwark
			case 2580:
			case 2581:
			case 2421: // Mobility Thrusters
			case 2422:
			case 2736: // Stability Thrusters
			case 2737:
			case 2738:
			case 2739:
			case 2740:
			case 2838: // Mounting Frustration
			case 2839:
			case 2840:
			case 2841:
			case 2842:
			case 2843:
			case 2844:
			case 2845:
			case 2846:
			case 2847:
			case 2848:
				return true;
		}

		return false;
	}

	public int checkForToggleBardEffect(int skillId) {
		// returns 0 if its Impassion
		// returns 1 if its exultation
		// returns 2 if its another skill
		int Impassion = 4590;
		int Exultation = 4589;

		if (skillId == Impassion) {
			return 0;
		}
		else {
			if (skillId == Exultation) {
				return 1;
			}
			return 2;
		}
	}

	public int checkForToggleRideEffect(int skillId) {
		int[] Battery = { 2440, 2441, 2442, 2443, 2444, 2445, 2446, 2447, 2448, 2449 };
		int[] Bulwark = { 2579, 2580, 2581 };

		for (int id : Battery) {
			if (id == skillId) {
				return 0;
			}
		}
		for (int id : Bulwark) {
			if (id == skillId) {
				return 1;
			}
		}
		return 2;
	}

	/**
	 * Start effect which includes: - start effect defined in template - start subeffect if possible - activate toogle skill if needed - schedule end of effect
	 */
	public void startEffect(boolean restored) {
		if (successEffects.isEmpty()) {
			return;
		}

		schedulePeriodicActions();

		for (EffectTemplate template : successEffects.values()) {
			template.startEffect(this);
			checkUseEquipmentConditions();
			checkCancelOnDmg();
		}

		if (isToggle() && effector instanceof Player || isRiderEffect(getSkillId())) {
			activateToggleSkill();
		}
		if (!restored && !forcedDuration) {
			duration = getEffectsDuration();
		}
		if (checkForToggleBardEffect(getSkillId()) == 0 || checkForToggleBardEffect(getSkillId()) == 1 && checkForToggleBardEffect(getSkillId()) != 2) {
			if (checkForToggleBardEffect(getSkillId()) == 0) {
				checkBardEffects(0);
			}
			else {
				checkBardEffects(1);
			}
		}
		if (checkForToggleRideEffect(getSkillId()) == 0 || checkForToggleRideEffect(getSkillId()) == 1 && checkForToggleRideEffect(getSkillId()) != 2) {
			if (checkForToggleRideEffect(getSkillId()) == 0) {
				checkRideEffects(0);
			}
			else {
				checkRideEffects(1);
			}
		}
		if (isKineticSkill()) {
			duration = skillTemplate.getToggleTimer();
		}
		if (isToggle()) {
			duration = skillTemplate.getToggleTimer();
		}
		if (duration == 0) {
			return;
		}
		if (isOpenAerialSkill()) {
			duration = skillTemplate.getDuration();
		}
		endTime = System.currentTimeMillis() + duration;
		task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				endEffect();
			}
		}, duration);
	}

	/**
	 * Just for Rider class skills
	 */
	private boolean isKineticSkill() {
		switch (getSkillId()) {
			case 2440:// Kinetic BAttery Start
			case 2441:
			case 2442:
			case 2443:
			case 2444:
			case 2445:
			case 2446:
			case 2447:
			case 2448:
			case 2449:
			case 2579:// Kinetic Bulwark Start
			case 2580:
			case 2581:
				return true;
		}
		return false;
	}

	/**
	 * Check if ride has Kinetic Bulwark and Kinetic BAttery turned on same time Updated 4.8
	 */
	private void checkRideEffects(int code) {
		int[] Battery = { 2440, 2441, 2442, 2443, 2444, 2445, 2446, 2447, 2448, 2449 };
		int[] Bulwark = { 2579, 2580, 2581 };
		// 0 = Kinetic Battery
		// 1 = Kinetic Bulwark
		// 2 = NULL
		if (effector instanceof Player) {
			Player player = (Player) effector;

			if (code == 0) {
				for (int skillId : Bulwark) {
					if (player.getEffectController().isNoshowPresentBySkillId(skillId)) {
						player.getEffectController().removeNoshowEffect(skillId);
					}
				}
			}
			else {
				for (int skillId : Battery) {
					if (player.getEffectController().isNoshowPresentBySkillId(skillId)) {
						player.getEffectController().removeNoshowEffect(skillId);
					}
				}
			}
		}
	}

	/**
	 * Check if bard has Impassion and Exultation activated at same time.
	 */
	private void checkBardEffects(int code) {
		// 0 = Impassion == 4590
		// 1 = Excultation == 4589
		if (effector instanceof Player) {
			Player player = (Player) effector;

			if (code == 0) {
				if (player.getEffectController().isNoshowPresentBySkillId(4589)) { // if Impassion is on check for Excultation
					player.getEffectController().removeNoshowEffect(4589);
				}
			}
			else {
				if (player.getEffectController().isNoshowPresentBySkillId(4590)) { // if Excultation is on Check for Impassion
					player.getEffectController().removeNoshowEffect(4590);
				}
			}
		}
	}

	/**
	 * Will activate toggle skill and start checking task
	 */
	private void activateToggleSkill() {
		PacketSendUtility.sendPacket((Player) effector, new SM_SKILL_ACTIVATION(getSkillId(), true));
	}

	/**
	 * Will deactivate toggle skill and stop checking task
	 */
	private void deactivateToggleSkill() {
		PacketSendUtility.sendPacket((Player) effector, new SM_SKILL_ACTIVATION(getSkillId(), false));
	}

	/**
	 * End effect and all effect actions This method is synchronized and prevented to be called several times which could cause unexpected behavior
	 */
	public synchronized void endEffect() {
		if (isStopped) {
			return;
		}

		for (EffectTemplate template : successEffects.values()) {
			template.endEffect(this);
		}

		// if effect is a stance, remove stance from player
		if (effector instanceof Player) {
			Player player = (Player) effector;
			if (player.getController().getStanceSkillId() == getSkillId()) {
				PacketSendUtility.sendPacket(player, new SM_PLAYER_STANCE(player, 0));
				player.getController().startStance(0);
			}
		}
		if (isToggle() && effector instanceof Player || isRiderEffect(getSkillId())) {
			deactivateToggleSkill();
		}
		stopTasks();
		effected.getEffectController().clearEffect(this);
		this.isStopped = true;
		this.addedToController = false;
	}

	/**
	 * Stop all scheduled tasks
	 */
	public void stopTasks() {
		if (task != null) {
			task.cancel(false);
			task = null;
		}

		if (periodicTasks != null) {
			for (Future<?> periodicTask : this.periodicTasks) {
				if (periodicTask != null) {
					periodicTask.cancel(false);
					periodicTask = null;
				}
			}
		}

		stopPeriodicActions();
	}

	/**
	 * Time till the effect end
	 *
	 * @return
	 */
	public int getRemainingTime() {
		return this.getDuration() >= 86400000 ? -1 : (int) (endTime - System.currentTimeMillis());
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * PVP damage ration
	 *
	 * @return
	 */
	public int getPvpDamage() {
		return skillTemplate.getPvpDamage();
	}

	public ItemTemplate getItemTemplate() {
		return itemTemplate;
	}

	/**
	 * Try to add this effect to effected controller
	 */
	public void addToEffectedController() {
		if ((!addedToController) && (effected.getLifeStats() != null) && (!effected.getLifeStats().isAlreadyDead())) {
			effected.getEffectController().addEffect(this);
			addedToController = true;
		}
	}

	/**
	 * @return the effectHate
	 */
	public int getEffectHate() {
		return effectHate;
	}

	/**
	 * @param effectHate
	 *            the effectHate to set
	 */
	public void setEffectHate(int effectHate) {
		this.effectHate = effectHate;
	}

	/**
	 * @return the tauntHate
	 */
	public int getTauntHate() {
		return tauntHate;
	}

	/**
	 * @param tauntHate
	 *            the tauntHate to set
	 */
	public void setTauntHate(int tauntHate) {
		this.tauntHate = tauntHate;
	}

	/**
	 * @param i
	 * @return actionObserver for this effect template
	 */
	public ActionObserver getActionObserver(int i) {
		return actionObserver[i - 1];
	}

	/**
	 * @param observer
	 *            the observer to set
	 */
	public void setActionObserver(ActionObserver observer, int i) {
		if (actionObserver == null) {
			actionObserver = new ActionObserver[4];
		}
		actionObserver[i - 1] = observer;
	}

	public void addSucessEffect(EffectTemplate effect) {
		successEffects.put(effect.getPosition(), effect);
	}

	public boolean isInSuccessEffects(int position) {
		if (successEffects.get(position) != null) {
			return true;
		}

		return false;
	}

	/**
	 * @return
	 */
	public Collection<EffectTemplate> getSuccessEffect() {
		return successEffects.values();
	}

	public void addAllEffectToSucess() {
		successEffects.clear();
		for (EffectTemplate template : getEffectTemplates()) {
			successEffects.put(template.getPosition(), template);
		}
	}

	public void clearSucessEffects() {
		successEffects.clear();
	}

	private void schedulePeriodicActions() {
		if (periodicActions == null || periodicActions.getPeriodicActions() == null || periodicActions.getPeriodicActions().isEmpty()) {
			return;
		}
		int checktime = periodicActions.getChecktime();
		periodicActionsTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				for (PeriodicAction action : periodicActions.getPeriodicActions()) {
					action.act(Effect.this);
				}
			}
		}, 0, checktime);
	}

	private void stopPeriodicActions() {
		if (periodicActionsTask != null) {
			periodicActionsTask.cancel(false);
			periodicActionsTask = null;
		}
	}

	public int getEffectsDuration() {
		int duration = 0;

		// iterate skill's effects until we can calculate a duration time, which is valid for all of them
		Iterator<EffectTemplate> itr = successEffects.values().iterator();
		while (itr.hasNext() && duration == 0) {
			EffectTemplate et = itr.next();
			int effectDuration = et.getDuration2() + et.getDuration1() * getSkillLevel();
			if (et.getRandomTime() > 0) {
				effectDuration -= Rnd.get(et.getRandomTime());
			}
			duration = duration > effectDuration ? duration : effectDuration;
		}

		// adjust with BOOST_DURATION
		switch (skillTemplate.getSubType()) {
			case BUFF:
				duration = effector.getGameStats().getStat(StatEnum.BOOST_DURATION_BUFF, duration).getCurrent();
				break;
			default:
				break;
		}

		// adjust with pvp duration
		if (effected instanceof Player && skillTemplate.getPvpDuration() != 0) {
			duration = duration * skillTemplate.getPvpDuration() / 100;
		}

		if (duration > 86400000) {
			duration = 86400000;
		}

		return duration;
	}

	public boolean isDeityAvatar() {
		return skillTemplate.isDeityAvatar();
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	public int getWorldId() {
		return worldId;
	}

	public int getInstanceId() {
		return instanceId;
	}

	/**
	 * @return the skillMoveType
	 */
	public SkillMoveType getSkillMoveType() {
		return skillMoveType;
	}

	/**
	 * @param skillMoveType
	 *            the skillMoveType to set
	 */
	public void setSkillMoveType(SkillMoveType skillMoveType) {
		this.skillMoveType = skillMoveType;
	}

	/**
	 * @return the targetX
	 */
	public float getTargetX() {
		return targetX;
	}

	/**
	 * @return the targetY
	 */
	public float getTargetY() {
		return targetY;
	}

	/**
	 * @return the targetZ
	 */
	public float getTargetZ() {
		return targetZ;
	}

	public void setTargetLoc(float x, float y, float z) {
		this.targetX = x;
		this.targetY = y;
		this.targetZ = z;
	}

	public void setSubEffectAborted(boolean value) {
		this.subEffectAbortedBySubConditions = value;
	}

	public boolean isSubEffectAbortedBySubConditions() {
		return this.subEffectAbortedBySubConditions;
	}

	public void setXpBoost(boolean value) {
		this.isXpBoost = value;
	}

	public boolean isXpBoost() {
		return this.isXpBoost;
	}

	public void setApBoost(boolean value) {
		this.isApBoost = value;
	}

	public boolean isApBoost() {
		return this.isApBoost;
	}

	public void setDrBoost(boolean value) {
		this.isDrBoost = value;
	}

	public boolean isDrBoost() {
		return this.isDrBoost;
	}

	public void setBdrBoost(boolean value) {
		this.isBdrBoost = value;
	}

	public boolean isBdrBoost() {
		return this.isBdrBoost;
	}

	public void setAuthorizeBoost(boolean value) {
		this.isAuthorizeBoost = value;
	}

	public boolean isAuthorizeBoost() {
		return this.isAuthorizeBoost;
	}

	public void setEnchantBoost(boolean value) {
		this.isEnchantBoost = value;
	}

	public boolean isEnchantBoost() {
		return this.isEnchantBoost;
	}

	public void setEnchantOptionBoost(boolean value) {
		this.isEnchantOptionBoost = value;
	}

	public boolean isEnchantOptionBoost() {
		return this.isEnchantOptionBoost;
	}

	public void setIdunDropBoost(boolean value) {
		this.isIdunDropBoost = value;
	}

	public boolean isIdunDropBoost() {
		return this.isIdunDropBoost;
	}

	public void setNoDeathPenaltyReduce(boolean value) {
		this.isNoDeathPenaltyReduce = value;
	}

	public boolean isNoDeathPenaltyReduce() {
		return this.isNoDeathPenaltyReduce;
	}

	public void setNoDeathPenalty(boolean value) {
		this.isNoDeathPenalty = value;
	}

	public boolean isNoDeathPenalty() {
		return this.isNoDeathPenalty;
	}

	public void setNoResurrectPenalty(boolean value) {
		this.isNoResurrectPenalty = value;
	}

	public boolean isNoResurrectPenalty() {
		return this.isNoResurrectPenalty;
	}

	public void setHiPass(boolean value) {
		this.isHiPass = value;
	}

	public boolean isHiPass() {
		return this.isHiPass;
	}

	public void setReturnCoolReduce(boolean value) {
		this.isReturnCoolReduce = value;
	}

	public boolean isReturnCoolReduce() {
		return this.isReturnCoolReduce;
	}

	public void setOdellaRecoverIncrease(boolean value) {
		this.isOdellaRecoverIncrease = value;
	}

	public boolean isOdellaRecoverIncrease() {
		return this.isOdellaRecoverIncrease;
	}

	public void setSprintFpReduce(boolean value) {
		this.isSprintFpReduce = value;
	}

	public boolean isSprintFpReduce() {
		return this.isSprintFpReduce;
	}

	public void setDeathPenaltyReduce(boolean value) {
		this.isDeathPenaltyReduce = value;
	}

	public boolean isDeathPenaltyReduce() {
		return this.isDeathPenaltyReduce;
	}

	/**
	 * Check all in use equipment conditions
	 *
	 * @return true if all conditions have been satisfied
	 */
	private boolean useEquipmentConditionsCheck() {
		Conditions useEquipConditions = skillTemplate.getUseEquipmentconditions();
		return useEquipConditions != null ? useEquipConditions.validate(this) : true;
	}

	/**
	 * Check use equipment conditions by adding Unequip observer if needed
	 */
	private void checkUseEquipmentConditions() {
		// If skill has use equipment conditions
		// Observe for unequip event and remove effect if event occurs
		if ((getSkillTemplate().getUseEquipmentconditions() != null) && (getSkillTemplate().getUseEquipmentconditions().getConditions().size() > 0)) {
			ActionObserver observer = new ActionObserver(ObserverType.UNEQUIP) {

				@Override
				public void unequip(Item item, Player owner) {
					if (!useEquipmentConditionsCheck()) {
						endEffect();
						if (this != null) {
							effected.getObserveController().removeObserver(this);
						}
					}
				}
			};
			effected.getObserveController().addObserver(observer);
		}
	}

	/**
	 * Add Attacked/Dot_Attacked observers if this effect needs to be removed on damage received by effected
	 */
	private void checkCancelOnDmg() {
		if (isCancelOnDmg()) {
			effected.getObserveController().attach(new ActionObserver(ObserverType.ATTACKED) {

				@Override
				public void attacked(Creature creature) {
					effected.getEffectController().removeEffect(getSkillId());
				}
			});

			effected.getObserveController().attach(new ActionObserver(ObserverType.DOT_ATTACKED) {

				@Override
				public void dotattacked(Creature creature, Effect dotEffect) {
					effected.getEffectController().removeEffect(getSkillId());
				}
			});
		}
	}

	public void setCancelOnDmg(boolean value) {
		this.isCancelOnDmg = value;
	}

	public boolean isCancelOnDmg() {
		return this.isCancelOnDmg;
	}

	public void endEffects() {
		for (EffectTemplate template : successEffects.values()) {
			template.endEffect(this);
		}
	}

	public boolean isFearEffect() {
		for (EffectTemplate template : successEffects.values()) {
			if (template instanceof FearEffect) {
				return true;
			}
		}
		return false;
	}

	public boolean isDelayedDamage() {
		return this.isDelayedDamage;
	}

	public void setDelayedDamage(boolean value) {
		this.isDelayedDamage = value;
	}

	public boolean isPetOrder() {
		return this.isPetOrder;
	}

	public void setPetOrder(boolean value) {
		this.isPetOrder = value;
	}

	public boolean isSummoning() {
		return this.isSummoning;
	}

	public void setSumonning(boolean value) {
		this.isSummoning = value;
	}

	private int initializePower(SkillTemplate skill) {
		// tweak for pet order spirit substitution and bodyguard
		if (skill.getActivationAttribute().equals(ActivationAttribute.MAINTAIN)) {
			return 30;
		}
		switch (skill.getSkillId()) {
			case 4170: // Word Of Destruction I
			case 4171: // Word Of Destruction II
			case 4172: // Word Of Destruction III
			case 4173: // Word Of Destruction IV
			case 4174: // Word Of Destruction V
			case 4175: // Word Of Destruction VI
			case 1066: // Silence Arrow I
			case 1067: // Silence Arrow II
			case 1068: // Silence Arrow III
			case 1069: // Silence Arrow IV
			case 1070: // Silence Arrow V
			case 1071: // Silence Arrow VI
			case 1072: // Silence Arrow VII
			case 1073: // Silence Arrow VIII
			case 1074: // Silence Arrow IX
				return 20;
			case 2932: // Unwavering Devotion I
			case 3127: // Iron Skin I
			case 2922: // Empyrean Providence I
			case 3128: // Prayer Of Freedom I
			case 3839: // Spirit Preserve I
			case 1832: // Elemental Screen I
			case 1192: // Gain Mana I
			case 1193: // Gain Mana II
			case 1194: // Gain Mana III
			case 1195: // Gain Mana IV
			case 1196: // Gain Mana V
			case 1197: // Gain Mana VI
			case 1198: // Gain Mana VII
			case 1199: // Gain Mana VIII
			case 1200: // Gain Mana IX
			case 1201: // Gain Mana X
			case 1202: // Gain Mana XI
			case 1203: // Gain Mana XII
			case 4725: // [ArchDaeva] Gain Mana 5.1
			case 1022: // Shackle Arrow I
			case 1023: // Shackle Arrow II
			case 1024: // Shackle Arrow III
			case 1025: // Shackle Arrow IV
			case 1026: // Shackle Arrow V
			case 618: // Ankle Snare I
			case 1329: // Curse Of Weakness I
			case 1330: // Curse Of Weakness II
			case 1331: // Curse Of Weakness II
			case 1332: // Curse Of Weakness IV
			case 1333: // Curse Of Weakness V
			case 1334: // Curse Of Weakness VI
			case 1335: // Curse Of Weakness VII
			case 1336: // Curse Of Weakness VIII
			case 4144: // Chain Of Suffering I
			case 4145: // Chain Of Suffering II
			case 4146: // Chain Of Suffering III
			case 4147: // Chain Of Suffering IV
			case 4148: // Chain Of Suffering V
			case 4149: // Chain Of Suffering VI
			case 1754: // Stilling Word I
			case 3854: // Wing Root I
				// Npc Skill
			case 18214: // Protective Shield
			case 18232: // Explosion Of Wrath
			case 18239: // Soul Petrify
				return 30;
			case 3790: // Cursecloud I
			case 3791: // Cursecloud II
			case 3792: // Cursecloud III
			case 3793: // Cursecloud IV
			case 3794: // Cursecloud V
			case 3795: // Cursecloud VI
				return 40;
			// Npc Skill
			case 18889: // Submissive Strike I
			case 18892: // Weeping Curtain I
			case 18994: // Weakness I
			case 19090: // Spinning Smash I
			case 19148: // Resistance I
			case 19504: // Canyonguard's Target I
			case 19505: // Relic Explosion I
			case 19512: // Sap Damage I
			case 19513: // Sap Damage II
			case 19514: // Sap Damage III
			case 19515: // Sap Damage IV
			case 19516: // Sap Damage V
			case 19644: // Total Exhaustion I
			case 19647: // Weaken I
				return 255;
		}

		return 10;
	}

	/**
	 * @return the power
	 */
	public int getPower() {
		return power;
	}

	/**
	 * @param power
	 *            the power to set
	 */
	public void setPower(int power) {
		this.power = power;
	}

	public int removePower(int power) {
		this.power -= power;
		return this.power;
	}

	public void setAccModBoost(int accModBoost) {
		this.accModBoost = accModBoost;
	}

	public int getAccModBoost() {
		return this.accModBoost;
	}

	public boolean isHideEffect() {
		return isHideEffect;
	}

	public boolean isParalyzeEffect() {
		return isParalyzeEffect;
	}

	public boolean isSanctuaryEffect() {
		return isSanctuaryEffect;
	}

	/**
	 * @return the isDamageEffect
	 */
	public boolean isDamageEffect() {
		return isDamageEffect;
	}

	/**
	 * @param isDamageEffect
	 *            the isDamageEffect to set
	 */
	public void setDamageEffect(boolean isDamageEffect) {
		this.isDamageEffect = isDamageEffect;
	}

	/**
	 * @return the signetBurstedCount
	 */
	public int getSignetBurstedCount() {
		return signetBurstedCount;
	}

	/**
	 * @param signetBurstedCount
	 *            the signetBurstedCount to set
	 */
	public void setSignetBurstedCount(int signetBurstedCount) {
		this.signetBurstedCount = signetBurstedCount;
	}

	public final EffectResult getEffectResult() {
		return effectResult;
	}

	public final void setEffectResult(EffectResult effectResult) {
		this.effectResult = effectResult;
	}

	public int getMpShield() {
		return this.mpShield;
	}

	public void setMpShield(int mpShield) {
		this.mpShield = mpShield;
	}

	public boolean isPhysicalState() {
		return isPhysicalState;
	}

	public void setIsPhysicalState(boolean isPhysicalState) {
		this.isPhysicalState = isPhysicalState;
	}

	public boolean isMagicalState() {
		return isMagicalState;
	}

	public void setIsMagicalState(boolean isMagicalState) {
		this.isMagicalState = isMagicalState;
	}

	private boolean isOpenAerialSkill() {
		switch (getSkillId()) {
			case 8224:
			case 8678:
			case 9173:
			case 19552:
			case 20371:
			case 20680:
			case 20872:
			case 21133:
			case 21476:
			case 21529:
			case 21911:
				return true;
		}
		return false;
	}
}
