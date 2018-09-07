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
package com.aionemu.gameserver.skillengine.periodicaction;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author antness
 */
public class HpUsePeriodicAction extends PeriodicAction {

	@XmlAttribute(name = "value")
	protected int value;
	@XmlAttribute(name = "delta")
	protected int delta;
	@XmlAttribute(name = "ratio")
	protected boolean ratio;

	@Override
	public void act(Effect effect) {
		Creature effected = effect.getEffected();
		int requiredHp = value;
		if (effected.getLifeStats().getCurrentHp() < requiredHp) {
			effect.endEffect();
			return;
		}

		if (ratio) {
			int maxHp = effected.getGameStats().getMaxHp().getCurrent();
			requiredHp = (int) (maxHp * (value / 100f));
		}

		effected.getLifeStats().reduceHp(requiredHp, effected);
	}
}
