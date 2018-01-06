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
package com.aionemu.gameserver.services.player.CreativityPanel.stats;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;

public class Agility implements StatOwner {

	private List<IStatFunction> agility = new ArrayList<IStatFunction>();

	public void onChange(Player player, int point) {
		if (point >= 1) {
			agility.clear();
			player.getGameStats().endEffect(this);
			agility.add(new StatAddFunction(StatEnum.CONCENTRATION, (int) (0.56f * point), true));
			agility.add(new StatAddFunction(StatEnum.PARRY, (int) (5.6f * point), true));
			agility.add(new StatAddFunction(StatEnum.EVASION, (int) (3.7f * point), true));
			agility.add(new StatAddFunction(StatEnum.PHYSICAL_CRITICAL_RESIST, (int) (2.4f * point), true));
			player.getGameStats().addEffect(this, agility);
		}
		else if (point == 0) {
			agility.clear();
			agility.add(new StatAddFunction(StatEnum.CONCENTRATION, (int) (0.56f * point), false));
			agility.add(new StatAddFunction(StatEnum.PARRY, (int) (5.6f * point), false));
			agility.add(new StatAddFunction(StatEnum.EVASION, (int) (3.7f * point), false));
			agility.add(new StatAddFunction(StatEnum.PHYSICAL_CRITICAL_RESIST, (int) (2.4f * point), false));
			player.getGameStats().endEffect(this);
		}
	}

	public static Agility getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final Agility INSTANCE = new Agility();
	}
}
