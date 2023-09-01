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
package com.aionemu.gameserver.controllers.observer;

import java.util.List;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.summons.SummonMode;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.effect.EffectTemplate;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.HealType;
import com.aionemu.gameserver.skillengine.model.HitType;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer modified by Sippolo, kecimis, Luzien
 */
public class AttackShieldObserver extends AttackCalcObserver {

	private int hit;
	private int totalHit;
	private boolean hitPercent;
	private boolean totalHitPercent;
	private Effect effect;
	private HitType hitType;
	private int shieldType;
	private float probability = 100f;
	private int minradius = 0;
	private int maxradius = 100;
	private HealType healType = null;
	private int effectorDamage;
	private int mpValue;
	private boolean totalHitPercentSet = false;

	/**
	 * @param percent
	 * @param value
	 * @param status
	 */
	public AttackShieldObserver(int hit, int totalHit, boolean percent, Effect effect, HitType type, int shieldType, float probability) {
		this(hit, totalHit, percent, false, effect, type, shieldType, probability, 0, 100, null, 0, 0);
	}

	public AttackShieldObserver(int hit, int effectorDamage, int totalHit, boolean percent, Effect effect, HitType type, int shieldType, float probability) {
		this(hit, totalHit, percent, false, effect, type, shieldType, probability, 0, 100, null, effectorDamage, 0);
	}

	public AttackShieldObserver(int hit, int totalHit, boolean percent, Effect effect, HitType type, int shieldType, float probability, int mpValue) {
		this(hit, totalHit, percent, false, effect, type, shieldType, probability, 0, 100, null, 0, mpValue);
	}

	public AttackShieldObserver(int hit, int totalHit, boolean hitPercent, boolean totalHitPercent, Effect effect, HitType type, int shieldType, float probability, int minradius, int maxradius, HealType healType, int effectorDamage, int mpValue) {
		this.hit = hit;
		this.totalHit = totalHit;// total absorbed dmg for shield, percentage for reflector
		this.effect = effect;
		this.hitPercent = hitPercent;
		this.totalHitPercent = totalHitPercent;
		this.hitType = type;
		this.shieldType = shieldType;
		this.probability = probability / 10;
		this.minradius = minradius;// only for reflector
		this.maxradius = maxradius;// only for reflector
		this.healType = healType;// only for convertheal
		this.effectorDamage = effectorDamage;// only for protect
		this.mpValue = mpValue;
	}

	@Override
	public void checkShield(List<AttackResult> attackList, Effect attackerEffect, Creature attacker) {
		for (AttackResult attackResult : attackList) {

			if (AttackStatus.getBaseStatus(attackResult.getAttackStatus()) == AttackStatus.DODGE || AttackStatus.getBaseStatus(attackResult.getAttackStatus()) == AttackStatus.RESIST) {
				continue;
			}
			// Handle Hit Types for Shields
			if (this.hitType != HitType.EVERYHIT) {
				if ((attackResult.getDamageType() != null) && (attackResult.getDamageType() != this.hitType)) {
					continue;
				}
			}

			if (Rnd.get(0, 100) > probability) {
				continue;
			}

			// shield type 2, normal shield
			if (shieldType == 2 || shieldType == 16) {
				int damage = attackResult.getDamage();

				int absorbedDamage = 0;
				if (hitPercent) {
					absorbedDamage = damage * hit / 100;
				}
				else {
					absorbedDamage = damage >= hit ? hit : damage;
				}

				absorbedDamage = absorbedDamage >= totalHit ? totalHit : absorbedDamage;
				totalHit -= absorbedDamage;

				if (absorbedDamage > 0) {
					attackResult.setShieldType(shieldType);
				}
				attackResult.setDamage(damage - absorbedDamage);

				// dont launch subeffect if damage is fully absorbed
				if (absorbedDamage >= damage && !isPunchShield(attackerEffect)) {
					attackResult.setLaunchSubEffect(false);
				}

				if (this.mpValue > 0) {
					attackResult.setShieldMp((int) (absorbedDamage * this.mpValue * 0.01F));
					this.effect.getEffected().getLifeStats().reduceMp((int) (absorbedDamage * this.mpValue * 0.01F));
					attackResult.setReflectedSkillId(this.effect.getSkillId());
				}

				if (totalHit <= 0) {
					effect.endEffect();
					return;
				}
			} // shield type 1, reflected damage
			else if (shieldType == 1) {
				// totalHit is radius
				if (minradius != 0) {
					if (MathUtil.isIn3dRange(attacker, effect.getEffected(), minradius)) {
						continue;
					}
				}
				if (MathUtil.isIn3dRange(attacker, effect.getEffected(), maxradius)) {
					int reflectedDamage = attackResult.getDamage() * totalHit / 100;
					int reflectedHit = Math.max(reflectedDamage, hit); // percentage of damage, but at least hit value
					attackResult.setShieldType(shieldType);
					if (attacker instanceof Npc) {
						reflectedHit = attacker.getAi2().modifyDamage(reflectedHit);
					}
					attackResult.setReflectedDamage(reflectedHit);
					attackResult.setReflectedSkillId(effect.getSkillId());
					attacker.getController().onAttack(effect.getEffected(), reflectedHit, false);

					if (effect.getEffected() instanceof Player) {
						PacketSendUtility.sendPacket((Player) effect.getEffected(), SM_SYSTEM_MESSAGE.STR_SKILL_PROC_EFFECT_OCCURRED(effect.getSkillTemplate().getNameId()));
					}
				}
				break;
			} // shield type 8, protect effect (ex. skillId: 417 Bodyguard I)
			else if (shieldType == 8) {
				// totalHit is radius
				if (effect.getEffector() == null || effect.getEffector().getLifeStats().isAlreadyDead()) {
					effect.endEffect();
					break;
				}
				if (effect.getEffector() instanceof Summon && (((Summon) effect.getEffector()).getMode() == SummonMode.RELEASE || ((Summon) effect.getEffector()).getMaster() == null)) {
					effect.endEffect();
					break;
				}

				if (MathUtil.isIn3dRange(effect.getEffector(), effect.getEffected(), totalHit)) {
					int damageProtected = 0;
					int effectorDamage = 0;

					if (hitPercent) {
						damageProtected = ((int) (attackResult.getDamage() * hit * 0.01));
						if (this.effectorDamage == 0) {
							this.effectorDamage = 100;
						}
						effectorDamage = ((int) (attackResult.getDamage() * this.effectorDamage * 0.01));
					}
					else {
						damageProtected = hit;
					}
					int finalDamage = attackResult.getDamage() - damageProtected;
					attackResult.setDamage((finalDamage <= 0 ? 0 : finalDamage));
					attackResult.setShieldType(shieldType);
					attackResult.setProtectedSkillId(effect.getSkillId());
					attackResult.setProtectedDamage(effectorDamage);
					attackResult.setProtectorId(effect.getEffectorId());
					effect.getEffector().getController().onAttack(attacker, effect.getSkillId(), TYPE.PROTECTDMG, effectorDamage, false, LOG.REGULAR);
				}
			} // shield type 0, convertHeal
			else if (shieldType == 0) {
				int damage = attackResult.getDamage();

				int absorbedDamage = damage;

				if (totalHitPercent && !totalHitPercentSet) {
					totalHit = (int) (totalHit * 0.01 * effect.getEffected().getGameStats().getHealth().getCurrent());
					totalHitPercentSet = true;
				}

				absorbedDamage = absorbedDamage >= totalHit ? totalHit : absorbedDamage;
				totalHit -= absorbedDamage;

				attackResult.setDamage(damage - absorbedDamage);

				// heal part
				int healValue = 0;
				if (hitPercent) {
					healValue = damage * hit / 100;
				}
				else {
					healValue = hit;
				}

				switch (healType) {
					case HP:
						effect.getEffected().getLifeStats().increaseHp(TYPE.HP, healValue, effect.getSkillId(), LOG.REGULAR);
						break;
					case MP:
						effect.getEffected().getLifeStats().increaseMp(TYPE.HEAL_MP, healValue, effect.getSkillId(), LOG.REGULAR);
						break;
					default:
						break;
				}

				// dont launch subeffect if damage is fully absorbed
				if (absorbedDamage >= damage && !isPunchShield(attackerEffect)) {
					attackResult.setLaunchSubEffect(false);
				}

				if (totalHit <= 0) {
					effect.endEffect();
					return;
				}
			}
		}
	}

	private boolean isPunchShield(Effect effect) {
		if (effect == null) {
			return false;
		}
		for (EffectTemplate template : effect.getEffectTemplates()) {
			if (template.getSubEffect() != null) {
				SkillTemplate skill = DataManager.SKILL_DATA.getSkillTemplate(template.getSubEffect().getSkillId());
				if (skill.isProvoked()) {
					return true;
				}
			}
		}
		return false;
	}
}
