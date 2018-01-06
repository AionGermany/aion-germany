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
package com.aionemu.gameserver.model.minion;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.templates.minion.MinionTemplate;

public class MinionBuff implements StatOwner {
	private MinionTemplate mt;
	private List<IStatFunction> functions = new ArrayList<IStatFunction>();

	public void apply(Player player, int minionId) {
		if (minionId == 0) {
			return;
		}
		mt = DataManager.MINION_DATA.getMinionTemplate(minionId);
		for (StatFunction statFunction : mt.getModifiers()) {
//			if (player.getPlayerClass().getClassType(player).equals(statFunction.getClassType())) {
				functions.add(new StatAddFunction(statFunction.getName(), statFunction.getValue(), true));
//			}
		}
		player.getGameStats().addEffect(this, functions);
	}

	public void end(Player player) {
		functions.clear();
		player.getGameStats().endEffect(this);
	}
}
