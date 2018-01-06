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

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author kecimis
 * @Reworked Kill3r
 */
public class DelayedSkillEffect extends EffectTemplate {

	@XmlAttribute(name = "skill_id")
	protected int skilliD;

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(final Effect effect) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// apply effect
				if (effect.getEffected().getEffectController().hasAbnormalEffect(effect.getSkill().getSkillId())) {
					final SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skilliD);
					if (template.getProperties().getTargetMaxCount() > 1) {
						final Effect e = new Effect(effect.getEffector(), effect.getEffected(), template, template.getLvl(), 0);
						World.getInstance().doOnAllObjects(new Visitor<VisibleObject>() {

							@Override
							public void visit(VisibleObject object) {
								if (MathUtil.getDistance(effect.getEffected(), object) <= template.getProperties().getEffectiveRange()) {
									SkillEngine.getInstance().applyEffectDirectly(template.getSkillId(), effect.getEffected(), (Creature) object, template.getDuration());
									e.applyEffect();
									e.initialize();
								}
							}
						});
					}
					else {
						Effect e = new Effect(effect.getEffector(), effect.getEffected(), template, template.getLvl(), 0);
						e.initialize();
						e.applyEffect();
					}

				}
			}
		}, effect.getEffectsDuration());
	}

	@Override
	public void endEffect(Effect effect) {

	}
}
