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

public class Will implements StatOwner {

	private List<IStatFunction> will = new ArrayList<IStatFunction>();

	public void onChange(Player player, int point) {
		if (point >= 1) {
			will.clear();
			player.getGameStats().endEffect(this);
			will.add(new StatAddFunction(StatEnum.MAXMP, (int) (24.5f * point), true));
			will.add(new StatAddFunction(StatEnum.MAGICAL_RESIST, (int) (3.35f * point), true));
			will.add(new StatAddFunction(StatEnum.MAGICAL_CRITICAL_RESIST, (int) (2.25f * point), true));
			will.add(new StatAddFunction(StatEnum.REGEN_MP, (int) (0.2f * point), true));
			player.getGameStats().addEffect(this, will);
		}
		else if (point == 0) {
			will.clear();
			will.add(new StatAddFunction(StatEnum.MAXMP, (int) (24.5f * point), false));
			will.add(new StatAddFunction(StatEnum.MAGICAL_RESIST, (int) (3.35f * point), false));
			will.add(new StatAddFunction(StatEnum.MAGICAL_CRITICAL_RESIST, (int) (2.25f * point), false));
			will.add(new StatAddFunction(StatEnum.REGEN_MP, (int) (0.2f * point), false));
			player.getGameStats().endEffect(this);
		}
	}

	public static Will getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final Will INSTANCE = new Will();
	}
}
