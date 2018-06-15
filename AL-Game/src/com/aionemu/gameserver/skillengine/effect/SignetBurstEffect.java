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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.controllers.attack.AttackUtil;
import com.aionemu.gameserver.skillengine.action.DamageType;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author ATracer, kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignetBurstEffect")
public class SignetBurstEffect extends DamageEffect {

	@XmlAttribute
	protected int signetlvl;
	@XmlAttribute
	protected String signet;

	@Override
	public void calculate(Effect effect) {

		Effect signetEffect = effect.getEffected().getEffectController().getAnormalEffect(signet);
		if (!super.calculate(effect, DamageType.MAGICAL)) {
			if (signetEffect != null) {
				signetEffect.endEffect();
			}
			return;
		}
		int valueWithDelta = value + delta * effect.getSkillLevel();
		int critAddDmg = this.critAddDmg2 + this.critAddDmg1 * effect.getSkillLevel();

		if (signetEffect == null) {
			valueWithDelta *= 0.05f;
			AttackUtil.calculateMagicalSkillResult(effect, valueWithDelta, null, getElement(), true, true, false, getMode(),
				this.critProbMod2, critAddDmg, shared, false);
			effect.setLaunchSubEffect(false);
		}
		else {

			int level = signetEffect.getSkillLevel();
			if (level < 3) { // why is 3 not 5? tmp fix
				effect.setSubEffectAborted(true);
			}
			effect.setSignetBurstedCount(level);
			switch (level) {
				case 1:
					valueWithDelta *= 0.2f;
					break;
				case 2:
					valueWithDelta *= 0.5f;
					break;
				case 3:
					valueWithDelta *= 1.0f;
					break;
				case 4:
					valueWithDelta *= 1.2f;
					break;
				case 5:
					valueWithDelta *= 1.5f;
					break;
			}

			/**
			 * custom bonuses for magical accurancy according to rune level and effector level follows same logic as
			 * damage
			 */
			int accmod = 0;
			int mAccurancy = effect.getEffector().getGameStats().getMAccuracy().getCurrent();
			switch (level) {
				case 1:
					accmod = (int) (-10.8f * mAccurancy);
					break;
				case 2:
					accmod = (int) (-10.5f * mAccurancy);
					break;
				case 3:
					accmod = 0;
					break;
				case 4:
					accmod = (int) (13.5f * mAccurancy);
					break;
				case 5:
					accmod = (int) (18.5f * mAccurancy);
					break;
			}
			effect.setAccModBoost(accmod);

			AttackUtil.calculateMagicalSkillResult(effect, valueWithDelta, null, getElement(), true, true, false, getMode(),
				this.critProbMod2, critAddDmg, shared, false);

			if (signetEffect != null) {
				signetEffect.endEffect();
			}
		}
	}
}
