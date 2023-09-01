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

public class Knowledge implements StatOwner {

	private List<IStatFunction> knowledge = new ArrayList<IStatFunction>();

	public void onChange(Player player, int point) {
		if (point >= 1) {
			knowledge.clear();
			player.getGameStats().endEffect(this);
			knowledge.add(new StatAddFunction(StatEnum.BOOST_MAGICAL_SKILL, (int) (6.0f * point), true));
			knowledge.add(new StatAddFunction(StatEnum.MAGICAL_CRITICAL, (int) (2.25f * point), true));
			knowledge.add(new StatAddFunction(StatEnum.MAGIC_SKILL_BOOST_RESIST, (int) (3.0f * point), true));
			player.getGameStats().addEffect(this, knowledge);
		}
		else if (point == 0) {
			knowledge.clear();
			knowledge.add(new StatAddFunction(StatEnum.BOOST_MAGICAL_SKILL, (int) (6.0f * point), false));
			knowledge.add(new StatAddFunction(StatEnum.MAGICAL_CRITICAL, (int) (2.25f * point), false));
			knowledge.add(new StatAddFunction(StatEnum.MAGIC_SKILL_BOOST_RESIST, (int) (3.0f * point), false));
			player.getGameStats().endEffect(this);
		}
	}

	public static Knowledge getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final Knowledge INSTANCE = new Knowledge();
	}
}
