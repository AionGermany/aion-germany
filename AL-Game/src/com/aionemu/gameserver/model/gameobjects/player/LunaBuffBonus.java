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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.templates.luna.LunaBonusAttr;
import com.aionemu.gameserver.model.templates.luna.LunaBonusTemplate;
import com.aionemu.gameserver.skillengine.change.Func;

public class LunaBuffBonus implements StatOwner {

	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	private LunaBonusTemplate lunaBonusTemplate;

	public LunaBuffBonus(int bonusId) {
		lunaBonusTemplate = DataManager.LUNA_BUFF_DATA.getLunaBuffId(bonusId);
	}

	public void applyEffect(Player player) {
		if (lunaBonusTemplate == null) {
			return;
		}
		for (LunaBonusAttr lunaBonusAttr : lunaBonusTemplate.getPenaltyAttr()) {
			if (lunaBonusAttr.getFunc().equals(Func.PERCENT)) {
				functions.add(new StatRateFunction(lunaBonusAttr.getStat(), lunaBonusAttr.getValue(), true));
			} else {
				functions.add(new StatAddFunction(lunaBonusAttr.getStat(), lunaBonusAttr.getValue(), true));
			}
		}
		player.getGameStats().addEffect(this, functions);
	}

	public void endEffect(Player player) {
		functions.clear();
		player.getGameStats().endEffect(this);
	}
}
