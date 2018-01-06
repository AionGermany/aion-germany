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

import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractOverTimeEffect")
public abstract class AbstractOverTimeEffect extends EffectTemplate {

	@XmlAttribute(required = true)
	protected int checktime;
	@XmlAttribute
	protected boolean percent;
	@XmlAttribute
	protected boolean shared;

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect) {
		this.startEffect(effect, null);
	}

	public void startEffect(final Effect effect, AbnormalState abnormal) {
		final Creature effected = effect.getEffected();

		if (abnormal != null) {
			effect.setAbnormal(abnormal.getId());
			effected.getEffectController().setAbnormal(abnormal.getId());
		}
		// TODO figure out what to do with such cases
		if (checktime == 0) {
			return;
		}
		try {
			Future<?> task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					onPeriodicAction(effect);
				}
			}, checktime, checktime);
			effect.setPeriodicTask(task, position);
		}
		catch (Exception e) {
			log.warn("Exception in skillId: " + effect.getSkillId());
			e.printStackTrace();
		}
	}

	public void endEffect(Effect effect, AbnormalState abnormal) {
		if (abnormal != null) {
			effect.getEffected().getEffectController().unsetAbnormal(abnormal.getId());
		}
	}
}
