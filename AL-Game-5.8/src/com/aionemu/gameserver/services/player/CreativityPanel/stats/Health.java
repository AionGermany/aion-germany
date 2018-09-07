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

public class Health implements StatOwner {

	private List<IStatFunction> health = new ArrayList<IStatFunction>();

	public void onChange(Player player, int point) {
		if (point >= 1) {
			health.clear();
			player.getGameStats().endEffect(this);
			health.add(new StatAddFunction(StatEnum.MAXHP, (int) (22.5f * point), true));
			health.add(new StatAddFunction(StatEnum.BLOCK, (int) (5.5f * point), true));
			health.add(new StatAddFunction(StatEnum.REGEN_HP, (int) (2.5f * point), true));
			player.getGameStats().addEffect(this, health);
		}
		else if (point == 0) {
			health.clear();
			health.add(new StatAddFunction(StatEnum.MAXHP, (int) (22.5f * point), false));
			health.add(new StatAddFunction(StatEnum.BLOCK, (int) (5.5f * point), false));
			health.add(new StatAddFunction(StatEnum.REGEN_HP, (int) (2.5f * point), false));
			player.getGameStats().endEffect(this);
		}
	}

	public static Health getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final Health INSTANCE = new Health();
	}
}
